package com.github.kyungmin08g.zephyro.core.logger;

import com.github.kyungmin08g.zephyro.core.utils.enums.LevelColor;
import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZephyroLogger {
  private static final Integer BUFFER_SIZE = 1024;
  private final BlockingQueue<String> buffer =  new LinkedBlockingQueue<>(BUFFER_SIZE);
  private final Class<?> clazz;

  private boolean isWarmUp = true;

  public ZephyroLogger(Class<?> clazz) {
    this.clazz = clazz;

    Thread thread = new Thread(() -> {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          String log = buffer.take();
          if (!isWarmUp) {
            System.out.write((log + "\n").getBytes());
          }
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    thread.setDaemon(true);
    thread.start();
  }

  private void enqueue(String log) {
    boolean isOverflow = buffer.offer(log);
    if (!isOverflow) {
      try {
        buffer.put(log);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      }
    }
  }

  public void warmUp() {
    isWarmUp = true;
    for (int i = 0; i < 50; i++) {
      enqueue(
        getLogFormat("", LevelColor.GREEN, LogLevel.INFO)
      );
    }
    buffer.clear();
    isWarmUp = false;
  }

  private String getLogFormat(String message, LevelColor color, LogLevel level) {
    return String.format(
      "%s[%s]%s %s%s%s : %s",
      color.getColor(), level,
      LevelColor.RESET.getColor(), LevelColor.WHITE.getColor(),
      String.valueOf(clazz).split(" ")[1],
      LevelColor.RESET.getColor(),
      message
    );
  }

  public void info(String message) {
    String log = getLogFormat(message, LevelColor.GREEN, LogLevel.INFO);
    enqueue(log);
  }

  public void warn(String message) {
    String log = getLogFormat(message, LevelColor.YELLOW, LogLevel.WARN);
    enqueue(log);
  }

  public void error(String message) {
    String log = getLogFormat(message, LevelColor.RED, LogLevel.ERROR);
    enqueue(log);
  }

  public void debug(String message) {
    String log = getLogFormat(message, LevelColor.MAGENTA, LogLevel.DEBUG);
    enqueue(log);
  }
}
