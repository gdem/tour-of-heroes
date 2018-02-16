package ch.softwareplus.blueprints.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.hotspot.MemoryPoolsExports;
import io.prometheus.client.hotspot.StandardExports;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;

/**
 * This class represents the java config to enable Prometheus.
 * 
 * @author Gökhan Demirkiyik
 */
@Configuration
@EnablePrometheusEndpoint
public class PrometheusConfig {

  private MetricRegistry dropwizardMetricRegistry;

  @Inject
  public PrometheusConfig(MetricRegistry dropwizardMetricRegistry) {
    this.dropwizardMetricRegistry = dropwizardMetricRegistry;
  }

  @PostConstruct
  public void registerPrometheusCollectors() {
    CollectorRegistry.defaultRegistry.clear();
    new StandardExports().register();
    new MemoryPoolsExports().register();
    new DropwizardExports(dropwizardMetricRegistry).register();
  }
}
