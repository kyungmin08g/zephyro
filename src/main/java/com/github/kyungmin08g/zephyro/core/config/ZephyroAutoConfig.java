package com.github.kyungmin08g.zephyro.core.config;

import com.github.kyungmin08g.zephyro.query.aspect.QueryAspect;
import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import com.github.kyungmin08g.zephyro.timer.aspect.TimerAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZephyroAutoConfig {

  @Bean
  public TimerAspect timerAspect() {
    return new TimerAspect();
  }

  @Bean
  public QueryAspect queryAspect() {
    return new QueryAspect();
  }

  @Bean
  public QueryLogInterceptor queryLogInterceptor() {
    return new QueryLogInterceptor();
  }
}
