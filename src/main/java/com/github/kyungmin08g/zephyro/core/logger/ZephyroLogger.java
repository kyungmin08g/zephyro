package com.github.kyungmin08g.zephyro.core.logger;

import com.github.kyungmin08g.zephyro.core.logger.event.ZephyroLogEvent;
import com.github.kyungmin08g.zephyro.core.enums.Color;
import com.github.kyungmin08g.zephyro.core.enums.LogLevel;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * ZephyroLogger : 이벤트 기반으로 동작하는 커스텀 로거
 */
public class ZephyroLogger {
  private static final Integer BUFFER_SIZE = 1024;

  private final EventFactory<ZephyroLogEvent> factory = ZephyroLogEvent::new;
  private final Disruptor<ZephyroLogEvent> handler = new Disruptor<>(factory, BUFFER_SIZE, Thread::new);
  private final RingBuffer<ZephyroLogEvent> buffer = handler.getRingBuffer();

  private final Class<?> clazz;
  private final Color defaultColor;
  private boolean isFormat;

  /**
   * ZephyroLogger 생성자
   * @param clazz : 클래스
   * @param isFormat : 포맷된 로그 사용 여부
   */
  public ZephyroLogger(Class<?> clazz, boolean isFormat) {
    this.clazz = clazz;
    this.isFormat = isFormat;
    this.defaultColor = Color.RESET;
    this.eventHandleRegister();
  }

  /**
   * 사용자가 보낸 메시지를 임시 이벤트 저장소에 저장하고 그 이벤트를 호출하는 메서드
   * @param message : 사용자가 보낸 로그 (메시지)
   * @param clazz : 호출된 클래스 경로
   * @param level : 로그 레벨
   * @param color : 레벨 색상
   * @param defaultColor : 기본 로그 색상
   * @param isLogFormat : 포맷된 로그 형식 사용 여부
   */
  private void callEvent(Object message, Class<?> clazz, LogLevel level, Color color, Color defaultColor, boolean isLogFormat) {
    // 포맷된 로그 사용 여부를 필드에 등록
    if (isLogFormat) {
      if (!this.isFormat) {
        this.isFormat = true;
      }
    } else {
      this.isFormat = false;
    }

    // 다음 로그 이벤트를 기록할 버퍼 위치(sequence) 가져오기
    long sequence = this.buffer.next();
    try {
      // 해당 sequence 위치의 로그 이벤트 객체 가져오기
      ZephyroLogEvent event = this.buffer.get(sequence);
      // 로그 이벤트에 필요한 값 설정
      event.setMessage(message);
      event.setLevel(level);
      event.setColor(color);
      event.setClazz(clazz);
      event.setDefaultColor(
        (defaultColor == Color.RESET) ? this.defaultColor : defaultColor
      );
    } finally {
      // 설정된 로그 이벤트를 버퍼에 푸시
      this.buffer.publish(sequence);
    }
  }

  /**
   * 이벤트 핸들러 등록 메서드 (생성될때 최초(1번)로 호출됨)
   */
  private void eventHandleRegister() {
    this.handler.handleEventsWith((event, sequence, endOfBatch) -> {
      // true라면 포맷된 로그 사용, false면 기본 로그 형식 사용
      if (this.isFormat) {
        System.out.write((event.getFormatMessage() + "\n").getBytes());
      } else {
        System.out.write((event.getDefaultMessage() + "\n").getBytes());
      }
    });
    this.handler.start(); // 이벤트 핸들러 실행
  }

  public void info(Object message) {
    this.callEvent(message, this.clazz, LogLevel.INFO, Color.GREEN, this.defaultColor, true);
  }

  public void info(Object message, boolean isFormat) {
    this.callEvent(message, this.clazz, LogLevel.INFO, Color.GREEN, this.defaultColor, isFormat);
  }

  public void info(Object message, boolean isFormat, Color defaultColor) {
    this.callEvent(message, this.clazz, LogLevel.INFO, Color.GREEN, defaultColor, isFormat);
  }

  public void warn(Object message) {
    this.callEvent(message, this.clazz, LogLevel.WARN, Color.YELLOW, this.defaultColor, true);
  }

  public void warn(Object message, boolean isFormat) {
    this.callEvent(message, this.clazz, LogLevel.WARN, Color.YELLOW, this.defaultColor, isFormat);
  }

  public void warn(Object message, boolean isFormat, Color defaultColor) {
    this.callEvent(message, this.clazz, LogLevel.WARN, Color.YELLOW, defaultColor, isFormat);
  }

  public void debug(Object message) {
    this.callEvent(message, this.clazz, LogLevel.DEBUG, Color.MAGENTA, this.defaultColor, true);
  }

  public void debug(Object message, boolean isFormat) {
    this.callEvent(message, this.clazz, LogLevel.DEBUG, Color.MAGENTA, this.defaultColor, isFormat);
  }

  public void debug(Object message, boolean isFormat, Color defaultColor) {
    this.callEvent(message, this.clazz, LogLevel.DEBUG, Color.MAGENTA, defaultColor, isFormat);
  }

  public void error(Object message) {
    this.callEvent(message, this.clazz, LogLevel.ERROR, Color.RED, this.defaultColor, true);
  }

  public void error(Object message, boolean isFormat) {
    this.callEvent(message, this.clazz, LogLevel.ERROR, Color.RED, this.defaultColor, isFormat);
  }

  public void error(Object message, boolean isFormat, Color defaultColor) {
    this.callEvent(message, this.clazz, LogLevel.ERROR, Color.RED, defaultColor, isFormat);
  }
}
