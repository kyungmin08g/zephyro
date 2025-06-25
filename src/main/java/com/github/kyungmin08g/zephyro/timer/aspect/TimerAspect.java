package com.github.kyungmin08g.zephyro.timer.aspect;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.core.utils.enums.Color;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

@Aspect
public class TimerAspect {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(TimerAspect.class);
  private static final StopWatch timer = new StopWatch();

  @Around("@annotation(com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer)")
  public Object runTimer(ProceedingJoinPoint process) {
    timer.start();
    try {
      Object proceed = process.proceed();
      timer.stop();

      String timeSecond = String.valueOf(timer.getTotalTimeSeconds());
      String time = timeSecond.split("\\.")[0] + timeSecond.substring(1, 5);
      String className = String.valueOf(process.getTarget().getClass())
        .split(" ")[1];

      log.error(className + " 실행 시간: " + time + " second", false, Color.MAGENTA);
      return proceed;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
