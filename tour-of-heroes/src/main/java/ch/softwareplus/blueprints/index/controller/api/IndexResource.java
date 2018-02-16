package ch.softwareplus.blueprints.index.controller.api;

import org.springframework.hateoas.ResourceSupport;

/**
 * This class represents the index resource.
 * 
 * @author Gökhan Demirkiyik
 */
public class IndexResource extends ResourceSupport {

  private final String name;
  private final String description;

  public IndexResource(String name, String description) {
    super();
    this.name = name;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }
}
