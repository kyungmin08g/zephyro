package com.github.kyungmin08g.zephyro.core.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {

  INFO("info"),
  ERROR("error"),
  WARN("warn"),
  DEBUG("debug");

  private final String value;
}
