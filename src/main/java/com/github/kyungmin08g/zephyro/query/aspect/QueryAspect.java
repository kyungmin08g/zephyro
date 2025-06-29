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

/**
 * 쿼리 로그 출력을 위해 필요한 값을 미리 설정하기 위한 Aspect
 */
@Aspect
public class QueryAspect {

  @Autowired
  private HttpServletRequest request;

  /**
   * 쿼리 로그 출력을 위해 필요한 값을 설정하는 메서드 (QueryLogTarget 어노테이션이 선언된 클래스에서만 동작)
   * @param process : 메서드
   */
  @Before("@within(com.github.kyungmin08g.zephyro.query.annotation.QueryLogTarget)")
  public void addQueryLogParameters(@NonNull JoinPoint process) {
    String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")); // 실행 시간
    String className = process.getTarget().getClass().getName(); // 클래스 경로 (패키지 경로)
    String methodName = process.getSignature().getName(); // 메서드 이름

    // MDC 객체에 저장
    MDC.put("time", time);
    MDC.put("class", className);
    MDC.put("method", methodName);
    MDC.put("env", getUserEnvironment(request));
    MDC.put("ip", getClientIp(request));
  }

  /**
   * 사용자 환경 추출하는 메서드
   * @param request : 사용자의 환경을 얻기 위한 HttpServletRequest
   * @return : 사용자 환경 반환
   */
  private String getUserEnvironment(@NonNull HttpServletRequest request) {
    String isMobile = request.getHeader("sec-ch-ua-mobile"); // 웹 브라우저로 호출할 경우 반드시 값이 존재함.
    if (isMobile != null) {
      switch (isMobile) {
        case "?0" -> { // ?0이면 데스크탑 환경
          return ClientEnvironment.PC.name();
        }
        case "?1" -> { // ?1이면 모바일/태블릿 환경
          return ClientEnvironment.MOBILE.name();
        }
      }

      return null;
    } else { // sec-ch-ua-mobile가 없다면 모두 API 환경으로 처리
      return ClientEnvironment.API.name();
    }
  }

  /**
   * Client IP를 추출하는 메서드
   * @param request : Client IP를 얻기 위한 HttpServletRequest
   * @return : Client IP 반환
   */
  private String getClientIp(@NonNull HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For"); // 운영 서버일 경우 포함될 확률 높음
    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
      ip = ip.split(",")[0].trim();
    } else { // X-Forwarded-For 헤더가 없을 경우
      ip = request.getRemoteAddr(); // 원격 주소
      if (ip == null || ip.isEmpty()) { // 원격 주소가 없을 경우
        ip = "0.0.0.0";
      } else if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) { // 로컬
        ip = "127.0.0.1";
      }
    }

    return ip;
  }
}
