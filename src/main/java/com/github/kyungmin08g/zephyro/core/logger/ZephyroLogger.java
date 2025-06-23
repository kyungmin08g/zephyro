package com.github.kyungmin08g.zephyro.core.logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZephyroLogger {
  private final BlockingQueue<String> buffer =  new LinkedBlockingQueue<>(2);
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

  public void info(String log) {
    enqueue(log);
  }

  private void enqueue(String log) {
    boolean isOverflow = buffer.offer(log);
    if (!isOverflow) {
      try {
        buffer.put(log);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.fillInStackTrace();
      }
    }
  }
}
