package com.github.kyungmin08g.zephyro.query.interceptor;

import com.github.kyungmin08g.zephyro.core.logger.ZephyroLogger;
import com.github.kyungmin08g.zephyro.core.logger.factory.ZephyroLoggerFactory;
import org.hibernate.resource.jdbc.spi.StatementInspector;

public class QueryLogInterceptor implements StatementInspector {
  private static final ZephyroLogger log = ZephyroLoggerFactory.getLogger(QueryLogInterceptor.class);

  @Override
  public String inspect(String query) {
    log.info(query, false);
    return query;
  }
}
