package com.github.kyungmin08g.zephyro.query.interceptor;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.core.enums.Color;
import com.github.kyungmin08g.zephyro.query.enums.SQLKeyword;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.MDC;

public class QueryLogInterceptor implements StatementInspector {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(QueryLogInterceptor.class);

  @Override
  public String inspect(String query) {
    log.info(getMessageFormat(query, MDC.get("time"), MDC.get("ip"), MDC.get("class"), MDC.get("method"), MDC.get("env")), false);
    return query;
  }

  /*
    형식 참고: select m1_0.member_id,m1_0.age,m1_0.created_at,m1_0.gender,m1_0.introduction,m1_0.nick_name,m1_0.password,m1_0.profile_url,m1_0.role,m1_0.updated_at from member m1_0 where m1_0.member_id=?
    단일 테이블 조회	m1_0 또는 this_
    하위 쿼리	sub1_, generatedAlias0 등
    조인 시	m1_0, m1_1, r1_0, 등 여러 개

    ex)
    select n1_0.id,n1_0.content,n1_0.created_at,n1_0.member_id,n1_0.thumbnail_url,n1_0.title,n1_0.updated_at from notice n1_0 where n1_0.id=?
    select m1_0.member_id,m1_0.age,m1_0.created_at,m1_0.gender,m1_0.introduction,m1_0.nick_name,m1_0.password,m1_0.profile_url,m1_0.role,m1_0.updated_at from member m1_0 where m1_0.member_id=?
    select n1_0.id,n1_0.content,n1_0.created_at,n1_0.member_id,n1_0.thumbnail_url,n1_0.title,n1_0.updated_at from notice n1_0 where n1_0.id=?
    update notice set content=?,member_id=?,thumbnail_url=?,title=?,updated_at=? where id=?
   */
  private String getMessageFormat(String query, String time, String ip, String clazz, String method, String env) {
    String resQuery = this.getQueryFormat(query);

    String firstPackageDomain = clazz.split("\\.")[0];
    String secondPackageDomain = clazz.split("\\.")[1];
    String packageName = firstPackageDomain.charAt(0) + "."
      + secondPackageDomain.charAt(0) + "."
      + clazz.substring(firstPackageDomain.length() + secondPackageDomain.length() + 2);

    return String.format(
      "[%s] [%s] [%s] [%s] %s",
      time, ip, env, packageName + "#" + method + "()",
      resQuery.substring(1)
    );
  }

  private String getQueryFormat(String query) {
    String resQuery = query.replaceAll("[a-z]*?[0-9]_[0-9].", "")
      .replace("=", Color.BLUE.getCode() + " = " + Color.RESET.getCode())
      .replace(",", ", "); // m1_0, te1_0, n1_0 등

    // SQL 특정 키워드를 대문자로 변환
    for (SQLKeyword keyword : SQLKeyword.values()) {
      resQuery = resQuery.replaceAll(" ?" + keyword.name().toLowerCase() + " ", " " + Color.BLUE.getCode() + keyword.name() + Color.RESET.getCode() + " ");
    }
    resQuery = resQuery + ";";
    resQuery = resQuery.replace("<>", " <> ");

    return resQuery;
  }
}
