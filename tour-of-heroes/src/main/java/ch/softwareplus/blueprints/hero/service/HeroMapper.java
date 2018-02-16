package ch.softwareplus.blueprints.hero.service;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ch.softwareplus.blueprints.hero.repository.domain.Hero;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;

/**
 * This class represents the hero mapper.
 * 
 * @author Gökhan Demirkiyik
 */
@Component
public class HeroMapper {

  /**
   * Converts the given {@code Hero} entity to is corresponding {@code HeroDTO} data transfer
   * object.
   * 
   * @param hero the hero entity to convert.
   * @return the converted {@code HeroDTO}.
   */
  public HeroDTO convert(Hero hero) {
    Objects.requireNonNull(hero);
    final HeroDTO dto = new HeroDTO();
    dto.setId(hero.getId());
    dto.setName(hero.getName());
    return dto;
  }
}
