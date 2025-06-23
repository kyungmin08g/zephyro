package com.github.kyungmin08g.zephyro.test.service;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;
import com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
  private final ZephyroLogger log;

  @ExecutionTimer
  public void testRunTimer() {
    for (int i = 0; i < 10; i++) {
      log.info(LogLevel.INFO, this.getClass().getSimpleName(), "안뇽하세요!?" + i);
    }
//    try {
//      Thread.sleep(4000);
////      System.out.println("Hello World!");
//      for (int i = 0; i < 10; i++) {
//        zephyroLogger.info(LogLevel.INFO, this.getClass().getSimpleName(), "안뇽하세요!?" + i);
//      }
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
  }
}
