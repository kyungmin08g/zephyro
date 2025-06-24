package com.github.kyungmin08g.zephyro.core.config;

import com.github.kyungmin08g.zephyro.core.aspect.LoggerAspect;
import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.timer.aspect.TimerAspect;
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

//  @Bean
//  @ConditionalOnMissingBean
//  public LoggerAspect loggerAspect() {
//    return new LoggerAspect();
//  }
}
