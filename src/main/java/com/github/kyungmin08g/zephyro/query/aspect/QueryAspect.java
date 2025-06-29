package com.github.kyungmin08g.zephyro.query.aspect;

import com.github.kyungmin08g.zephyro.query.enums.ClientEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
public class QueryAspect {

  @Autowired
  private HttpServletRequest request;

  @Before("@within(com.github.kyungmin08g.zephyro.query.annotation.MonitorTarget)")
  public void addQueryLogParameters(@NonNull JoinPoint process) {
    String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String className = process.getTarget().getClass().getName();
    String methodName = process.getSignature().getName();

    MDC.put("time", time);
    MDC.put("class", className);
    MDC.put("method", methodName);
    MDC.put("env", getUserEnvironment(request));
    MDC.put("ip", getClientIp(request));
  }

  private String getUserEnvironment(HttpServletRequest request) {
    String isMobile = request.getHeader("sec-ch-ua-mobile");
    if (isMobile != null) {
      switch (isMobile) {
        case "?0" -> {
          return ClientEnvironment.PC.name();
        }
        case "?1" -> {
          return ClientEnvironment.MOBILE.name();
        }
      }

      return null;
    } else {
      return ClientEnvironment.API.name();
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
