package com.github.kyungmin08g.zephyro.query.annotation;

import com.github.kyungmin08g.zephyro.query.registrar.QueryInterceptorBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

// 해당 어노테이션을 호출하면 HibernateProperties Bean을 자동으로 등록 가능하게 Import
@Import(QueryInterceptorBeanRegistrar.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableZephyroQueryLogger {
}
