package ch.softwareplus.blueprints.hero.rest;

import ch.softwareplus.blueprints.hero.api.Hero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Represents the mapper to map between data transfer, entity and value objects.
 */
@Mapper(componentModel = "spring")
public interface HeroModelMapper {

    HeroModel toResource(Hero hero);

    @Mapping(target = "id", ignore = true)
    Hero toHero(HeroModelRequest request);

    HeroModelRequest toModelRequest(Hero hero);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Hero hero, HeroModelRequest request);

}
