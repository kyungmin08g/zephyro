package com.github.kyungmin08g.zephyro.core.logger.event;

import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;
import com.github.kyungmin08g.zephyro.core.utils.enums.Color;
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
  private Color color;
  private LogLevel level;
  private Color defaultColor;

  public String getFormatMessage() {
    return this.getMessageFormat(message, clazz, color, level);
  }

  public String getDefaultMessage() {
    return this.getDefaultMessageFormat(message, color, level, defaultColor);
  }

  private String getMessageFormat(
    Object message,
    Class<?> clazz,
    Color color,
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

    return String.format(
      "%s%s%s  %s%s%s %s%s%s --- [%s] %s%s%s : %s",
      time + offset,
      color.getCode(),
      Color.RESET.getCode(),
      color.getCode(),
      level,
      Color.RESET.getCode(),
      Color.MAGENTA.getCode(),
      jvmPip,
      Color.RESET.getCode(),
      threadName,
      Color.CYAN.getCode(),
      packageName,
      Color.RESET.getCode(),
      message
    );
  }

  private String getDefaultMessageFormat(
    Object message,
    Color color,
    LogLevel level,
    Color defaultColor
  ) {
    return String.format(
      "[ %s%s%s ] ▶ %s%s%s",
      color.getCode(),
      level,
      Color.RESET.getCode(),
      defaultColor.getCode(),
      message,
      Color.RESET.getCode()
    );
  }
}
