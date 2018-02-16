package ch.softwareplus.blueprints.hero.service.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class represents the request object to create a new hero. The name must be not empty.
 * 
 * @author Gökhan Demirkiyik
 */
public class UpdateHero {

  private Long id;

  @NotEmpty
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    // @formatter:off
    return new ToStringBuilder(this)
              .append("name", name).toString();
    // @formatter:on

  }
}
