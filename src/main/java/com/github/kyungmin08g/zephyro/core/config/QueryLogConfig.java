package com.github.kyungmin08g.zephyro.core.config;

import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryLogConfig {
  private final QueryLogInterceptor queryLogInterceptor;

  @Bean
  public HibernatePropertiesCustomizer getHibernatePropertiesCustomizer() {
    return hibernateProperties -> {
      hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, queryLogInterceptor);
    };
  }
}
