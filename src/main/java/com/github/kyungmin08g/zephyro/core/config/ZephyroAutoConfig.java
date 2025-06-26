package com.github.kyungmin08g.zephyro.core.config;

import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import com.github.kyungmin08g.zephyro.timer.aspect.TimerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZephyroAutoConfig {

  @Bean
  public TimerAspect timerAspect() {
    return new TimerAspect();
  }

  @Bean
  public QueryLogInterceptor queryLogInterceptor() {
    return new QueryLogInterceptor();
  }
}
