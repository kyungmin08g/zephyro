package com.github.kyungmin08g.zephyro.core.logger;

import com.github.kyungmin08g.zephyro.core.logger.event.ZephyroLogEvent;
import com.github.kyungmin08g.zephyro.core.enums.Color;
import com.github.kyungmin08g.zephyro.core.enums.LogLevel;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class ZephyroLogger {
  private static final Integer BUFFER_SIZE = 1024;

  private final EventFactory<ZephyroLogEvent> factory = ZephyroLogEvent::new;
  private final Disruptor<ZephyroLogEvent> handler = new Disruptor<>(factory, BUFFER_SIZE, Thread::new);
  private final RingBuffer<ZephyroLogEvent> buffer = handler.getRingBuffer();

  private final Class<?> clazz;
  private final Color defaultColor;
  private boolean isFormat;

  public ZephyroLogger(Class<?> clazz, boolean isFormat) {
    this.clazz = clazz;
    this.isFormat = isFormat;
    this.defaultColor = Color.RESET;
    this.eventHandleRegister();
  }

  private void callEvent(
    Object message,
    Class<?> clazz,
    LogLevel level,
    Color color,
    Color defaultColor,
    boolean isLogFormat
  ) {
    if (isLogFormat) {
      if (!this.isFormat) {
        this.isFormat = true;
      }
    } else {
      this.isFormat = false;
    }

    long sequence = this.buffer.next();
    try {
      ZephyroLogEvent event = this.buffer.get(sequence);
      event.setMessage(message);
      event.setLevel(level);
      event.setColor(color);
      event.setClazz(clazz);
      event.setDefaultColor(
        (defaultColor == Color.RESET) ? this.defaultColor : defaultColor
      );
    } finally {
      this.buffer.publish(sequence);
    }
  }

  private void eventHandleRegister() {
    this.handler.handleEventsWith((event, sequence, endOfBatch) -> {
      if (this.isFormat) {
        System.out.write((event.getFormatMessage() + "\n").getBytes());
      } else {
        System.out.write((event.getDefaultMessage() + "\n").getBytes());
      }
    });
    this.handler.start();
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
