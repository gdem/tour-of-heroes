package ch.softwareplus.blueprints.hero.api;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents the request object to create a new hero. The name must be not empty.
 */
@Data
public class UpdateHero {

    private Long id;
    @NotEmpty
    private String name;
}
