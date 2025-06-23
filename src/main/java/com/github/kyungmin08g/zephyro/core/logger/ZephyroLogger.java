package com.github.kyungmin08g.zephyro.core.logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZephyroLogger {
  private final BlockingQueue<String> buffer =  new LinkedBlockingQueue<>(1000);
  private final Thread thread;

  public ZephyroLogger() {
    this.thread = new Thread(() -> {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          String log = buffer.take();
          System.out.println(log);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.fillInStackTrace();
      }
    });

    this.thread.setDaemon(true);
    this.thread.start();
  }

  public void info(String message) {
    boolean offered = buffer.offer(message);
    if (!offered) {
      flushBuffer();
      buffer.offer(message);
    }
  }

  private synchronized void flushBuffer() {
    String log;
    while ((log = buffer.poll()) != null) {
      System.out.println(log);
    }
  }
}
