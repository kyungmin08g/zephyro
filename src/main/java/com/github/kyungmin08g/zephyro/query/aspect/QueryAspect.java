package com.github.kyungmin08g.zephyro.query.aspect;

import com.github.kyungmin08g.zephyro.query.enums.UserEnv;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@RequiredArgsConstructor
public class QueryAspect {
  private final HttpServletRequest request;

  @Before(
    "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.DeleteMapping)"
  )
  public void test(JoinPoint joinPoint) {
    String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();

    MDC.put("time", time);
    MDC.put("class", className);
    MDC.put("method", methodName);
    MDC.put("env", getUserEnv(request));
    MDC.put("ip", getClientIp(request));
  }

  private String getUserEnv(HttpServletRequest request) {
    String isMobile = request.getHeader("sec-ch-ua-mobile");
    if (isMobile != null) {
      switch (isMobile) {
        case "?0" -> {
          return UserEnv.PC.name();
        }
        case "?1" -> {
          return UserEnv.MOBILE.name();
        }
      }

      return null;
    } else {
      return UserEnv.API.name();
    }
  }

  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
      ip = ip.split(",")[0].trim();
    } else { // X-Forwarded-For 헤더가 없을 경우 | 대비
      ip = request.getRemoteAddr();
      if (ip == null || ip.isEmpty()) {
        ip = "0.0.0.0";
      } else if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) { // 테스트 시 0:0:0:0:0:0:0:1 이렇게 나옴
        ip = "127.0.0.1";
      }
    }

    return ip;
  }
}
