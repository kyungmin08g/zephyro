package com.github.kyungmin08g.zephyro.test.service;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.test.domain.TestEntity;
import com.github.kyungmin08g.zephyro.test.repository.TestRepository;
import com.github.kyungmin08g.zephyro.timer.annotation.MethodTimeTracker;
import com.github.kyungmin08g.zephyro.timer.annotation.PerfTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(TestService.class);
  private final TestRepository testRepository;

  @PerfTracker
  @MethodTimeTracker
  public void testRunTimer() {
    try {
//      Thread.sleep(10000);
      long totalLogTime = 0;
      long totalSoutTime = 0;

      long start = System.nanoTime();
      log.info("zephyro");
      long end = System.nanoTime();
      totalLogTime += (end - start);

      start = System.nanoTime();
      System.out.println("println");
      end = System.nanoTime();
      totalSoutTime += (end - start);

      System.out.println("logging -> " + (totalLogTime) + "ns");
      System.out.println("println -> " + (totalSoutTime) + "ns");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void createTest() {
    testRepository.save(
      TestEntity.builder()
        .title("제목1")
        .content("내용1")
        .build()
    );
  }

  public TestEntity getTestById(String id) {
    return testRepository.findById(Long.valueOf(id)).get();
  }
}
