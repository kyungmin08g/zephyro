package com.github.kyungmin08g.zephyro.timer.aspect;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.timer.annotation.MethodTimeTracker;
import com.github.kyungmin08g.zephyro.timer.annotation.PerfTracker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
public class TimerAspect {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(TimerAspect.class);

  private static final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
  private static final ClassLoadingMXBean classLoadingBean = ManagementFactory.getClassLoadingMXBean();
  private static final Runtime runtime = Runtime.getRuntime();

  @Around("@annotation(com.github.kyungmin08g.zephyro.timer.annotation.MethodTimeTracker)")
  public Object executionTimeLog(ProceedingJoinPoint process) {
    try {
      long startSecond = System.currentTimeMillis();
      Object proceed = process.proceed();
      long endSecond = System.currentTimeMillis();

      // 메서드 레벨에서 @MethodTimeTracker 어노테이션의 color 필드 구하기
      MethodSignature methodSignature = (MethodSignature) process.getSignature();
      Method method = methodSignature.getMethod();
      MethodTimeTracker executionTimer = method.getAnnotation(MethodTimeTracker.class);

      log.info(
        getExecutionTimeMessageFormat(process, startSecond, endSecond),
        false,
        executionTimer.color()
      );
      return proceed;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  @Around("@annotation(com.github.kyungmin08g.zephyro.timer.annotation.PerfTracker)")
  public Object performanceLog(ProceedingJoinPoint process) {
    try {
      long startSecond = System.currentTimeMillis();
      long startNano = System.nanoTime();
      Object proceed = process.proceed();
      long endNano = System.nanoTime();
      long endSecond = System.currentTimeMillis();

      // 메서드 레벨에서 @Profiled 어노테이션의 color 필드 구하기
      MethodSignature methodSignature = (MethodSignature) process.getSignature();
      Method method = methodSignature.getMethod();
      PerfTracker profiled = method.getAnnotation(PerfTracker.class);

      log.info(
        getProfiledMessageFormat(process, startNano, endNano, startSecond, endSecond),
        false,
        profiled.color()
      );
      return proceed;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private String getExecutionTimeMessageFormat(ProceedingJoinPoint process, long startSecond, long endSecond) {
    String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // 해당 메서드 실행 시간 구하기
    String elapsedSecond = String.valueOf((endSecond - startSecond) / 1000.0); // 밀리초(ms) -> 초(s)로 변환

    // 클래스와 메서드 이름 구하기
    String className = process.getTarget().getClass().getSimpleName();
    String methodName = process.getSignature().getName();

    return String.format(
      "[%s] %s#%s() 메서드 실행 시간: %s초",
      time, className, methodName, elapsedSecond
    );
  }

  private String getProfiledMessageFormat(
    ProceedingJoinPoint process,
    long startNano,
    long endNano,
    long stratSecond,
    long endSecond
  ) {
    String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // 나노초 구하기
    long nano = (endNano - startNano);
    String elapsedNano = NumberFormat.getInstance().format(nano);

    // 초 구하기
    String elapsedSecond = String.valueOf((endSecond - stratSecond) / 1000.0);

    // 클래스외 메서드 이름 구하기
    String className = String.valueOf(process.getTarget().getClass()).split(" ")[1];
    String methodName = process.getSignature().getName();

    // OS 정보 구하기
    String osName = System.getProperty("os.name");
    String osVersion = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");
    String javaVersion = System.getProperty("java.version");

    // 실행된 컨텍스트 관련 정보 구하기
    String thread = Thread.currentThread().getName().toLowerCase();
    String jvmPip = runtimeBean.getName().split("@")[0];

    // JVM 실행 시간 구하기
    String uptime = String.valueOf(runtimeBean.getUptime());
    String uptimeMs = uptime.charAt(0) + "." + uptime.substring(uptime.length() - 3);

    // JVM 힙 메모리 관련 변수
    long used = runtime.totalMemory() - runtime.freeMemory();
    long maxHeap = runtime.maxMemory() / (1024 * 1024);
    long totalHeap = runtime.totalMemory() / (1024 * 1024);
    long usedHeap = used / (1024 * 1024);

    String maxHeapMemory;
    String totalHeapMemory;
    String usedHeapMemory;

    if (maxHeap >= 1024) {
      maxHeapMemory = (maxHeap / 1024.0) + "GB";
      if (totalHeap >= 1024) {
        totalHeapMemory = (totalHeap / 1024.0) + "GB";
      } else {
        totalHeapMemory = (totalHeap + "MB");
      }

      if (usedHeap >= 1024) {
        usedHeapMemory = (usedHeap / 1024.0) + "GB";
      } else {
        usedHeapMemory = (usedHeap + "MB");
      }
    } else {
      // 기본 포맷 : MB
      maxHeapMemory = (maxHeap + "MB");
      totalHeapMemory = (totalHeap + "MB");
      usedHeapMemory = (usedHeap + "MB");
    }

    // 로드된 클래스 수
    int loadedClasses = classLoadingBean.getLoadedClassCount();

    // GC 정보
    long gcCount = 0; // GC의 횟수
    long gcTime = 0; // GC가 실행된 시간
    for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
      if (gc.getCollectionCount() != -1) gcCount += gc.getCollectionCount();
      if (gc.getCollectionTime() != -1) gcTime += gc.getCollectionTime();
    }

    return String.format(
      "[%s] %s 메서드에 대한 성능 측정 결과입니다.\n" +
        "----------------------------------------------------------------------------\n" +
        "실행 컨텍스트: thread=%s | pip=%s\n" +
        "위치: %s\n" +
        "힙 메모리: 최대=%s / 총=%s / 사용=%s\n" +
        "JVM 가동 시간: %s\n" +
        "경과 시간: %s (%s)\n" +
        "가비지 컬렉션: 횟수=%s, 누적 시간=%sms | 로드된 클래스 수: %s\n" +
        "시스템: %s %s (%s) | 자바 버전: %s\n" +
        "----------------------------------------------------------------------------",
      time,
      (process.getTarget().getClass().getSimpleName() + "#" + methodName + "()"),
      thread, jvmPip,
      (className + "#" + methodName + "()"),
      maxHeapMemory,
      totalHeapMemory,
      usedHeapMemory,
      (uptimeMs + "s"),
      (elapsedSecond + "s"),
      (elapsedNano + "ns"),
      gcCount, gcTime,
      NumberFormat.getInstance().format(loadedClasses),
      osName, osVersion,
      osArch, javaVersion
    );
  }
}
