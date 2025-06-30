package com.github.kyungmin08g.zephyro.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그 레벨
 */
@Getter
@AllArgsConstructor
public enum LogLevel {
  INFO, ERROR, WARN, DEBUG, QUERY
}
