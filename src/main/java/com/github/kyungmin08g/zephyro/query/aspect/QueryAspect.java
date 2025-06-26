package com.github.kyungmin08g.zephyro.query.aspect;

import com.github.kyungmin08g.zephyro.query.interceptor.QueryLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import java.util.HashMap;
import java.util.Map;

@Aspect
public class QueryAspect {

  @Around("@annotation(com.github.kyungmin08g.zephyro.query.annotation.QuerySpy)")
  public Object registerQueryInterceptorBean(ProceedingJoinPoint process) {
    try {
      HibernatePropertiesCustomizer proceed = (HibernatePropertiesCustomizer) process.proceed();

      Map<String, Object> hibernateProperties= new HashMap<>();
//      hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, null);
      proceed.customize(hibernateProperties);

      System.out.println(proceed);

      return proceed;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
