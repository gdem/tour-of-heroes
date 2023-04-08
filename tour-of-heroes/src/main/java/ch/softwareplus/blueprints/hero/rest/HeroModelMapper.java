package ch.softwareplus.blueprints.hero.rest;

import ch.softwareplus.blueprints.hero.api.Hero;
import org.mapstruct.Mapper;

/**
 * Represents the mapper to map between data transfer, entity and value objects.
 */
@Mapper(componentModel = "spring")
public interface HeroModelMapper {

  HeroModel toResource(Hero hero);
}
