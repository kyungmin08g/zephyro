package com.github.kyungmin08g.zephyro.query.enums;

import lombok.Getter;

/**
 * 사용자 환경 ('어떤 환경에서 호출했나'를 확인하기 위함.)
 */
@Getter
public enum ClientEnvironment {
  PC, MOBILE, API
}
