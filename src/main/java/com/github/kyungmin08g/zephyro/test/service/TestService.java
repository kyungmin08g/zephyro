package com.github.kyungmin08g.zephyro.test.service;

import com.github.kyungmin08g.zephyro.timer.annotation.ExecutionTimer;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  @ExecutionTimer
  public void print() {
    try {
      Thread.sleep(4000);
      System.out.println("Hello World!");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
