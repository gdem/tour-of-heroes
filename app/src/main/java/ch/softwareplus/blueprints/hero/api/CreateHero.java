package ch.softwareplus.blueprints.hero.api;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents the request object to create a new hero. The name must be not empty.
 */
@Data
public class CreateHero {

    @NotEmpty
    private String name;
}
