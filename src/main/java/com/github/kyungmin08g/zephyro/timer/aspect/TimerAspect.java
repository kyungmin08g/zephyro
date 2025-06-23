package com.github.kyungmin08g.zephyro.timer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

@Aspect
@Slf4j
public class TimerAspect {
  private final StopWatch timer = new StopWatch();

  @Around("@annotation(com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer)")
  public Object runTimer(ProceedingJoinPoint process) {
    timer.start();
    try {
      Object proceed = process.proceed();
      timer.stop();

      String timeSecond = String.valueOf(timer.getTotalTimeSeconds());
      String time = timeSecond.split("\\.")[0] + timeSecond.substring(1, 5);

      String className = String.valueOf(process.getTarget().getClass())
        .split(" ")[1];

      log.info("{} executed in {} seconds", className, time);
      return proceed;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
