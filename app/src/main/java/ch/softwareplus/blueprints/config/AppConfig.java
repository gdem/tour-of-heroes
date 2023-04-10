package ch.softwareplus.blueprints.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

/**
 * Java config that contains the main application configuration.
 */
@Configuration
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class AppConfig {
    // intentionally empty
}
