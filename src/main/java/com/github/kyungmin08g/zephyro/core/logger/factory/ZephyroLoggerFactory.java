package com.github.kyungmin08g.zephyro.core.logger.factory;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;

import java.util.concurrent.ConcurrentHashMap;

public class ZephyroLoggerFactory {
  private static final ConcurrentHashMap<Class<?>, ZephyroLogger> cache = new ConcurrentHashMap<>();

  public static ZephyroLogger getLogger(Class<?> clazz) {
    return cache.computeIfAbsent(clazz, ZephyroLogger::new);
  }
}
