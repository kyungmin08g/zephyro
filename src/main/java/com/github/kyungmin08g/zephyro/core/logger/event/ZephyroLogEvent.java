package com.github.kyungmin08g.zephyro.core.logger.event;

import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;
import com.github.kyungmin08g.zephyro.core.utils.enums.LevelColor;
import lombok.*;

import java.lang.management.ManagementFactory;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@NoArgsConstructor
public class ZephyroLogEvent {

  private Object message;
  private Class<?> clazz;
  private LevelColor levelColor;
  private LogLevel level;

  public String getMessage() {
    return getMessageFormat(message, clazz, levelColor, level);
  }

  private String getMessageFormat(
    Object message,
    Class<?> clazz,
    LevelColor color,
    LogLevel level
  ) {
    String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    ZonedDateTime now = ZonedDateTime.now();
    String offset = now.getOffset().getId();

    String threadName = ("multi-" + Thread.currentThread().getName()).toLowerCase();
    String jvmPip = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

    String packages = String.valueOf(clazz).split(" ")[1];
    String firstPackageDomain = packages.split("\\.")[0];
    String secondPackageDomain = packages.split("\\.")[1];
    String packageName = firstPackageDomain.charAt(0) + "."
      + secondPackageDomain.charAt(0) + "."
      + packages.substring(firstPackageDomain.length() + secondPackageDomain.length() + 2);

    Runtime runtime = Runtime.getRuntime();
    long used = runtime.totalMemory() - runtime.freeMemory();
    long usedMB = used / (1024 * 1024);
    long total = runtime.totalMemory() / (1024 * 1024);
    long max = runtime.maxMemory() / (1024 * 1024);
    long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();

    return String.format(
      "%s%s%s | %s%s%s | PID:%s%s%s | STATUS:%sthread=%s, %s, %s, %s, %s%s | CLASS:%s%s%s ▶ %s",
      time + offset,
      color.getCode(),
      LevelColor.RESET.getCode(),
      color.getCode(),
      level,
      LevelColor.RESET.getCode(),
      LevelColor.MAGENTA.getCode(),
      jvmPip,
      LevelColor.RESET.getCode(),
      LevelColor.MAGENTA.getCode(),
      threadName,
      "maxHeap=" + max + "MB",
      "allocatedHeap=" + total + "MB",
      "usedHeap=" + usedMB + "MB",
      "uptime=" + uptimeMs + "ms",
      LevelColor.RESET.getCode(),
      LevelColor.CYAN.getCode(),
      packageName,
      LevelColor.RESET.getCode(),
      message
    );
  }
}
