package ch.softwareplus.blueprints.hero.api;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents the request object to create a new hero. The name must be not empty.
 */
public record CreateHero(@NotEmpty String name) {
}
