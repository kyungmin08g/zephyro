package com.github.kyungmin08g.zephyro.core.logger;

import com.github.kyungmin08g.zephyro.core.logger.event.ZephyroLogEvent;
import com.github.kyungmin08g.zephyro.core.utils.enums.LevelColor;
import com.github.kyungmin08g.zephyro.core.utils.enums.Level;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.*;

public class ZephyroLogger {
  private static final Integer BUFFER_SIZE = 1024;

  private final EventFactory<ZephyroLogEvent> factory = ZephyroLogEvent::new;
  private final Disruptor<ZephyroLogEvent> disruptor = new Disruptor<>(factory, BUFFER_SIZE, Executors.defaultThreadFactory());
  private final RingBuffer<ZephyroLogEvent> ringBuffer = disruptor.getRingBuffer();
  private final Class<?> clazz;

  public ZephyroLogger(Class<?> clazz) {
    this.clazz = clazz;
    this.eventHandleRegister();
  }

  private void eventHandleRegister() {
    disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
      System.out.write((event.getMessage() + "\n").getBytes());
    });
    disruptor.start();
  }

  public void log(String message) {
    long seq = ringBuffer.next();
    try {
      ZephyroLogEvent event = ringBuffer.get(seq);
      event.setMessage(message);
      event.setLevelColor(LevelColor.GREEN);
      event.setLevel(Level.INFO);
      event.setClazz(clazz);
    } finally {
      ringBuffer.publish(seq);
    }
  }
}
