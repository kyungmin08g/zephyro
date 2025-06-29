package com.github.kyungmin08g.zephyro.query.annotation;

import com.github.kyungmin08g.zephyro.query.registrar.QueryInterceptorBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(QueryInterceptorBeanRegistrar.class)
@Documented
public @interface EnableZephyroQueryLogger {
}
