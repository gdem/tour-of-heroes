package ch.softwareplus.blueprints.hero.api;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents the request object to create a new hero. The name must be not empty.
 */
@Data
public class CreateHero {

  @NotEmpty
  private String name;

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
