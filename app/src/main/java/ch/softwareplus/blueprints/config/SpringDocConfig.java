package ch.softwareplus.blueprints.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * The SpringDoc Configuration for REST API documentation.
 */
@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {

    private final Environment env;

    @Value("${springdoc.auth.url:/auth/login}")
    private String authUrl;

    private static final String SECURITY_NAME = "bearer-only";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_NAME, securityScheme()))
                .info(new Info()
                        .title(env.getProperty("springdoc.title"))
                        .version(env.getProperty("springdoc.version"))
                        .description(env.getProperty("springdoc.description"))
                        .termsOfService(env.getProperty("springdoc.termsOfServiceUrl"))
                        .license(new License().name(env.getProperty("springdoc.license"))
                                .url(env.getProperty("springdoc.licenseUrl"))))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_NAME));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme().type(Type.HTTP).in(In.HEADER).bearerFormat("JWT").scheme("bearer")
                .name(SECURITY_NAME);
    }

    private Scopes scopes() {
        return new Scopes().addString("openid", "Open ID Connect Scope");
    }
}
