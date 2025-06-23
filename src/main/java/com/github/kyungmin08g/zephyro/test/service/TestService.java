package com.github.kyungmin08g.zephyro.test.service;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
  private final ZephyroLogger zephyroLogger;

  @ExecutionTimer
  public void testRunTimer() {
    try {
      Thread.sleep(4000);
//      System.out.println("Hello World!");
      for (int i = 0; i < 10; i++) {
        zephyroLogger.info("안뇽하세요!?" + i);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
