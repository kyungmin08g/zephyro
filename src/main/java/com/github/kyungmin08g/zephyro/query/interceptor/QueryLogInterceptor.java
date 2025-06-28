package com.github.kyungmin08g.zephyro.query.interceptor;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import com.github.kyungmin08g.zephyro.query.enums.SqlKeyword;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.stream.Stream;

public class QueryLogInterceptor implements StatementInspector {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(QueryLogInterceptor.class);

  @Override
  public String inspect(String query) {
    System.out.println(query);
    log.info(getMessageFormat(query), false);
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
  private String getMessageFormat(String query) {
    String aQuery = query.replaceAll("[a-z]*?[0-9]_[0-9].", "")
      .replace(",", ", "); // m1_0, te1_0, n1_0 등

    for (SqlKeyword keyword : SqlKeyword.values()) {
      aQuery = aQuery.replaceAll(" ?" + keyword.name().toLowerCase() + " ", " " + keyword.name() + " "); // SQL 특정 키워드를 대문자로 변환
    }

    return String.format(aQuery);
  }
}
