package com.github.kyungmin08g.zephyro.core.logger.event;

import com.github.kyungmin08g.zephyro.core.enums.LogLevel;
import com.github.kyungmin08g.zephyro.core.enums.Color;
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

  /**
   * 포맷된 로그를 반환하는 메서드
   * @param message : 사용자가 보낸 문자열 로그
   * @param clazz : 호출한 클래스 경로
   * @param color : 레벨 색상
   * @param level : 로그 레벨
   * @return : 커스텀으로 포맷된 로그 반환
   */
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

  /**
   * 기본 로그 형식을 포맷하는 메서드
   * @param message : 사용자가 보낸 문자열 로그
   * @param color : 레벨 색상
   * @param level : 로그 레벨
   * @param defaultColor : 로그 기본 색상
   * @return : 포맷된 기본 로그 형식을 반환
   */
  private String getDefaultMessageFormat(
    Object message,
    Color color,
    LogLevel level,
    Color defaultColor
  ) {
    return String.format(
      "[%s%s%s] %s%s%s",
      color.getCode(),
      level,
      Color.RESET.getCode(),
      defaultColor.getCode(),
      message,
      Color.RESET.getCode()
    );
  }
}
