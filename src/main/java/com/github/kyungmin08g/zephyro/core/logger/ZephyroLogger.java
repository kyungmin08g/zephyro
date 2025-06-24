package com.github.kyungmin08g.zephyro.core.logger;

import com.github.kyungmin08g.zephyro.core.utils.enums.LevelColor;
import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZephyroLogger {
  private final BlockingQueue<String> buffer =  new LinkedBlockingQueue<>(1000);
  private final Class<?> c;

  public ZephyroLogger(Class<?> c) {
    this.c = c;
    Thread thread = new Thread(() -> {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          String log = buffer.take();
          System.out.write((log + "\n").getBytes());
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      } catch (IOException e) {
        throw new RuntimeException(e);
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

  private String getLogFormat(String message, LevelColor color, LogLevel level) {
    return String.format(
      "%s[%s]%s %s%s%s : %s",
      color.getColor(), level,
      LevelColor.RESET.getColor(), LevelColor.WHITE.getColor(),
      String.valueOf(c).split(" ")[1],
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
