package com.github.kyungmin08g.zephyro.test.service;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(TestService.class);

  @ExecutionTimer
  public void testRunTimer() {
    for (int i = 0; i < 10; i++) {
      log.debug("테스트 버그 로그");
    }
  }
}
