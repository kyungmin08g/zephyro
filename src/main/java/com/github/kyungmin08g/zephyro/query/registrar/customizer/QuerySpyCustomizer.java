package com.github.kyungmin08g.zephyro.query.registrar.customizer;

import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import java.util.Map;

public class QuerySpyCustomizer implements HibernatePropertiesCustomizer {

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, new QueryLogInterceptor());
  }
}
