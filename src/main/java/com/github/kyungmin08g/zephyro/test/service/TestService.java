package com.github.kyungmin08g.zephyro.test.service;

import com.github.kyungmin08g.zephyro.core.annotation.Logging;
import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.utils.enums.LogLevel;
import com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Logging
public class TestService {
//  private final ZephyroLogger log;
//  private final ZephyroLogger log = new ZephyroLogger(TestService.class);

  @ExecutionTimer
  public void testRunTimer() {
//    for (int i = 0; i < 10; i++) {
//      log.error(LogLevel.ERROR, this.getClass().getSimpleName(), "안뇽하세요!?" + i);
//    }
//    log.info("테스트 정상 로그");
//    log.error("테스트 에러 로그");
//    log.warning("테스트 경고 로그");
    log.debug("테스트 버그 로그");
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
