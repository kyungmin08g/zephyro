package com.github.kyungmin08g.zephyro.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그 레벨 색상
 */
@Getter
@AllArgsConstructor
public enum Color {

  RESET("\u001B[0m"),
  RED("\u001B[31m"),
  YELLOW("\u001B[33m"),
  GREEN("\u001B[32m"),
  BLUE("\u001B[34m"),
  MAGENTA("\u001B[35m"),
  CYAN("\u001B[36m"),
  WHITE("\u001B[37m");

  private final String code;
}
