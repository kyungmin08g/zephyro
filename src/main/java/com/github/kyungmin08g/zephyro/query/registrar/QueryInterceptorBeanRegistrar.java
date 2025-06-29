package com.github.kyungmin08g.zephyro.query.registrar;

import com.github.kyungmin08g.zephyro.query.registrar.customizer.QueryLogCustomizer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class QueryInterceptorBeanRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
    AnnotationMetadata importingClassMetadata,
    BeanDefinitionRegistry registry
  ) {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(QueryLogCustomizer.class);
    registry.registerBeanDefinition("queryLogCustomizer", beanDefinition);
  }
}
