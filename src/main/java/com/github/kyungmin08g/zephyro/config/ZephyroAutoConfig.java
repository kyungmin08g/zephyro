package com.github.kyungmin08g.zephyro.config;

import com.github.kyungmin08g.zephyro.aspect.TimerAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZephyroAutoConfig {

  @Bean
  @ConditionalOnMissingBean
  public TimerAspect timerAspect() {
    return new TimerAspect();
  }
}
