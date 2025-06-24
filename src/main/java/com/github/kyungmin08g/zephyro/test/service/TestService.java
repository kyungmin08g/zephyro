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

//  @ExecutionTimer
  public void testRunTimer() {
    long totalLogTime = 0;
    long totalSoutTime = 0;

    log.warmUp();

    long start = System.nanoTime();
    log.info("성공!");
    long end = System.nanoTime();
    totalLogTime += (end - start);

    start = System.nanoTime();
    System.out.println("성공!");
    end = System.nanoTime();
    totalSoutTime += (end - start);

    System.out.println("라이브러리 로깅: " + (totalLogTime) + "ns");
    System.out.println("println: " + (totalSoutTime) + "ns");
  }
}
