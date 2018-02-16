package ch.softwareplus.blueprints.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * This java config contains the application configuration.
 * 
 * @author Gökhan Demirkiyik
 */
@Configuration
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class AppConfig {

  @Bean
  public Filter shallowEtagHeaderFilter() {
    // Note this filter does not improve application performance, as it requires the request to be
    // fully processed to generate the ETag
    // It only saves bandwidth
    return new ShallowEtagHeaderFilter();
  }

}
