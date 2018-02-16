package ch.softwareplus.blueprints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * This class is the entry-point to start the Spring Boot Application.
 *
 * @see SpringBootApplication
 * @author Gökhan Demirkiyik
 */
@SpringBootApplication
public class HeroApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(HeroApplication.class);
  }

  /**
   * Starts the application by the given array of arguments.
   *
   * @param args the array of arguments to start with. Will be ignored, because spring boot does not
   *        need any arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(HeroApplication.class);
  }
}
