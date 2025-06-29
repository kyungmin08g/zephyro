package com.github.kyungmin08g.zephyro.query.interceptor;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.core.enums.Color;
import com.github.kyungmin08g.zephyro.query.enums.SQLKeyword;
import io.micrometer.common.lang.NonNull;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.MDC;

/**
 * 원격 DB에 전송될 쿼리를 가로채는 Interceptor
 */
public class QueryLogInterceptor implements StatementInspector {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(QueryLogInterceptor.class);

  /**
   * StatementInspector의 inspect() 메서드 구현체
   * @param query : Hibernate가 생성한 SQL
   * @return : 원격 DB에 전송할 쿼리 반환 (Hibernate가 생성한 쿼리 반환)
   */
  @Override
  public String inspect(String query) {
    String queryFormat = getMessageFormat(query, MDC.get("time"), MDC.get("ip"), MDC.get("class"), MDC.get("method"), MDC.get("env"));
    if (queryFormat != null) log.info(queryFormat, false);
    return query;
  }

  /**
   * 쿼리 로그 형식을 포맷하는 메서드
   * @param query : Hibernate가 생성한 SQL
   * @param time : 실행된 시간
   * @param clientIp : 클라이언트 IP
   * @param clazz : 클래스 경로 (패키지 경로)
   * @param method : 메서드 이름
   * @param env : 사용자 환경 (ex. pc, mobile)
   * @return : 포맷된 쿼리 로그 반환
   */
  private String getMessageFormat(@NonNull String query, String time, String clientIp, String clazz, String method, String env) {
    if (clazz == null || clazz.isEmpty()) return null;

    // 변환된 쿼리 문자열 | 반환값
    String resQuery = this.getQueryFormat(query);

    // 패키지 경로 축약
    String firstPackageDomain = clazz.split("\\.")[0];
    String secondPackageDomain = clazz.split("\\.")[1];
    String packageName = firstPackageDomain.charAt(0) + "."
      + secondPackageDomain.charAt(0) + "."
      + clazz.substring(firstPackageDomain.length() + secondPackageDomain.length() + 2);

    return String.format(
      "[%s] [%s] [%s] [%s] %s",
      time, clientIp, env, packageName + "#" + method + "()",
      resQuery.substring(1)
    );
  }

  /**
   * ex) select n1_0.id,n1_0.content,n1_0.created_at,n1_0.member_id,n1_0.thumbnail_url,n1_0.title,n1_0.updated_at from notice n1_0 where n1_0.id=?
   * 위 예시 쿼리를 보기 편한 쿼리로 변환하는 메서드
   * @param query : Hibernate가 생성한 SQL
   * @return : 변환된 쿼리 문자열 반환
   */
  private String getQueryFormat(@NonNull String query) {
    // Alias절 생략 (m1_0, te1_0, n1_0)
    String resQuery = query.replaceAll("[a-z]*?[0-9]_[0-9].", "")
      .replace("=", Color.BLUE.getCode() + " = " + Color.RESET.getCode())
      .replace(",", ", ")
      .replace("<>", " <> ");

    // SQL 특정 키워드를 대문자로 변환
    for (SQLKeyword keyword : SQLKeyword.values()) {
      resQuery = resQuery.replaceAll(" ?" + keyword.name().toLowerCase() + " ", " " + Color.BLUE.getCode() + keyword.name() + Color.RESET.getCode() + " ");
    }

    return resQuery + ";";
  }
}
