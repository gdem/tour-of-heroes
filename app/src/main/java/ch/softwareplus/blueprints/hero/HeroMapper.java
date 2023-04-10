package ch.softwareplus.blueprints.hero;

import ch.softwareplus.blueprints.hero.domain.HeroEntity;
import ch.softwareplus.blueprints.hero.api.CreateHero;
import ch.softwareplus.blueprints.hero.api.Hero;
import ch.softwareplus.blueprints.hero.api.UpdateHero;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Represents the mapper to map between data transfer, entity and value objects.
 */
@Mapper(componentModel = "spring")
public interface HeroMapper {

  HeroEntity toEntity(CreateHero createHero);

  void update(UpdateHero updateHero, @MappingTarget HeroEntity entity);

  Hero toHero(HeroEntity entity);
}
