package com.github.kyungmin08g.zephyro;

import com.github.kyungmin08g.zephyro.query.annotation.EnableZephyroQueryLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZephyroQueryLogger
public class ZephyroApplication {
  public static void main(String[] args) {
    SpringApplication.run(ZephyroApplication.class, args);
  }
}
