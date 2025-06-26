package com.github.kyungmin08g.zephyro.core.config;

import com.github.kyungmin08g.zephyro.query.aspect.QueryAspect;
import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import com.github.kyungmin08g.zephyro.timer.aspect.TimerAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ZephyroAutoConfig {
  private final QueryLogInterceptor queryLogInterceptor;

  @Bean
  public TimerAspect timerAspect() {
    return new TimerAspect();
  }

  @Bean
  public QueryAspect queryAspect() {
    return new QueryAspect(queryLogInterceptor);
  }

//  @Bean
//  public QueryLogInterceptor queryLogInterceptor() {
//    return new QueryLogInterceptor();
//  }
}
