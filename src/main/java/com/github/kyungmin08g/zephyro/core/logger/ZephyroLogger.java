package com.github.kyungmin08g.zephyro.core.logger;

import com.github.kyungmin08g.zephyro.core.logger.event.ZephyroLogEvent;
import com.github.kyungmin08g.zephyro.core.utils.enums.LevelColor;
import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class ZephyroLogger {
  private static final Integer BUFFER_SIZE = 1024;

  private final EventFactory<ZephyroLogEvent> factory = ZephyroLogEvent::new;
  private final Disruptor<ZephyroLogEvent> handler = new Disruptor<>(factory, BUFFER_SIZE, Thread::new);
  private final RingBuffer<ZephyroLogEvent> buffer = handler.getRingBuffer();
  private final Class<?> clazz;

  public ZephyroLogger(Class<?> clazz) {
    this.clazz = clazz;
    this.eventHandleRegister();
  }

  public void info(Object message) {
    callEvent(message, clazz, LogLevel.INFO, LevelColor.GREEN);
  }

  public void warn(Object message) {
    callEvent(message, clazz, LogLevel.WARN, LevelColor.YELLOW);
  }

  public void debug(Object message) {
    callEvent(message, clazz, LogLevel.DEBUG, LevelColor.MAGENTA);
  }

  public void error(Object message) {
    callEvent(message, clazz, LogLevel.ERROR, LevelColor.RED);
  }

  private void callEvent(
    Object message,
    Class<?> clazz,
    LogLevel level,
    LevelColor color
  ) {
    long sequence = this.buffer.next();
    try {
      ZephyroLogEvent event = this.buffer.get(sequence);
      event.setMessage(message);
      event.setLevel(level);
      event.setLevelColor(color);
      event.setClazz(clazz);
    } finally {
      this.buffer.publish(sequence);
    }
  }

  private void eventHandleRegister() {
    handler.handleEventsWith((event, sequence, endOfBatch) ->
      System.out.write((event.getMessage() + "\n").getBytes())
    );
    handler.start();
  }
}
