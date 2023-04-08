package ch.softwareplus.blueprints.hero.api;

import lombok.Value;

/**
 * Class represents the hero data transfer object.
 */
@Value
public class Hero {

    private Long id;
    private String name;
}
