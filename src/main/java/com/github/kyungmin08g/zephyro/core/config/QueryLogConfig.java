package com.github.kyungmin08g.zephyro.core.config;

import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryLogConfig {

  @Bean
  public QueryLogInterceptor getQueryLogInterceptor() {
    return new QueryLogInterceptor();
  }
}
