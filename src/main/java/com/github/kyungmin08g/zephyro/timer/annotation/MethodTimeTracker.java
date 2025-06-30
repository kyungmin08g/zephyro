package com.github.kyungmin08g.zephyro.timer.annotation;

import com.github.kyungmin08g.zephyro.core.enums.Color;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodTimeTracker {
  Color color() default Color.RESET;
}
