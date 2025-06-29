package com.github.kyungmin08g.zephyro.query.registrar;

import com.github.kyungmin08g.zephyro.query.registrar.customizer.QueryLogBeanCustomizer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Bean을 자동으로 등록하기 위한 ImportBeanDefinitionRegistrar 구현체
 */
public class QueryInterceptorBeanRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
    AnnotationMetadata importingClassMetadata,
    BeanDefinitionRegistry registry
  ) {
    // Bean 설정
    RootBeanDefinition beanDefinition = new RootBeanDefinition(QueryLogBeanCustomizer.class);
    registry.registerBeanDefinition("queryLogCustomizer", beanDefinition);
  }
}
