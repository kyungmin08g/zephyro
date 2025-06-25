package com.github.kyungmin08g.zephyro.core.logger.factory;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZephyroLoggerFactory {
  private static final Map<Class<?>, ZephyroLogger> cache = new ConcurrentHashMap<>();

  public static ZephyroLogger getLogger(Class<?> clazz) {
    return cache.computeIfAbsent(clazz, c -> new ZephyroLogger(clazz, true));
  }
}
