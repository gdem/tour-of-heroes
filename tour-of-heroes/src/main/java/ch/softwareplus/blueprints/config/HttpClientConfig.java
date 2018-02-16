package ch.softwareplus.blueprints.config;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * This class represents the http client configuration.
 * 
 * @author Gökhan Demirkiyik
 */
@Configuration
public class HttpClientConfig {

  @Value("${http.client.pool.maxTotal:10}")
  private Integer poolMaxTotal;

  @Bean
  public ClientHttpRequestFactory httpRequestFactory() {
    return new BufferingClientHttpRequestFactory(
        new HttpComponentsClientHttpRequestFactory(httpClient()));
  }

  @Bean
  public HttpClient httpClient() {
    final PoolingHttpClientConnectionManager connectionManager =
        new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(poolMaxTotal);

    //@formatter:off
    return HttpClientBuilder
            .create()
              .setConnectionManager(connectionManager)
              .disableCookieManagement()
              // remove the User-Agent otherwise the call will end up with 400 (bad request)
              .addInterceptorLast((HttpRequest request, HttpContext context) -> {
                request.removeHeaders(HttpHeaders.USER_AGENT);
              })
            .build();
    //@formatter:on
  }
}
