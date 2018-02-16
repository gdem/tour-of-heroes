package ch.softwareplus.blueprints.hero.service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.softwareplus.blueprints.hero.repository.HeroRepository;
import ch.softwareplus.blueprints.hero.repository.domain.Hero;
import ch.softwareplus.blueprints.hero.service.api.CreateHero;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;
import ch.softwareplus.blueprints.hero.service.api.HeroService;
import ch.softwareplus.blueprints.hero.service.api.UpdateHero;

/**
 * This implementation of {@link HeroService} uses the {@link HeroRepository} to provide access to
 * the database.
 * 
 * @author Gökhan Demirkiyik
 */
@Service
public class HeroServiceImpl implements HeroService {

  private static final Logger log = LoggerFactory.getLogger(HeroServiceImpl.class);

  @Inject
  private HeroRepository heroRepository;

  @Inject
  private HeroMapper heroMapper;

  @Override
  public Page<HeroDTO> getPage(Pageable pageable) {
    Objects.requireNonNull(pageable, "pageable must not be null");

    // @formatter:off
    return heroRepository
           .findAll(pageable)
           .map(heroMapper::convert);
    // @formatter:on
  }

  @Override
  public Optional<HeroDTO> findById(Long id) {
    Objects.requireNonNull(id, "id must not be null");

    return Optional.ofNullable(heroMapper.convert(heroRepository.findOne(id)));
  }


  @Override
  public HeroDTO createNew(CreateHero createHero) {
    Objects.requireNonNull(createHero, "createHero object must not be null");
    final Hero newHero = heroRepository.save(new Hero(createHero.getName()));
    log.debug("Created new hero with id: {}", newHero.getId());
    return heroMapper.convert(newHero);
  }

  @Override
  public HeroDTO updateExisting(UpdateHero updateHero) {
    Objects.requireNonNull(updateHero, "updateHero object must not be null");

    Hero hero = heroRepository.findOne(updateHero.getId());
    if (hero == null) {
      throw new NoSuchElementException("hero with id: " + updateHero.getId() + " not found");
    } else {
      hero.setName(updateHero.getName());
    }

    final Hero updatedHero = heroRepository.save(hero);
    log.debug("Updated existing hero with id: {}", updatedHero.getId());
    return heroMapper.convert(updatedHero);
  }

  @Override
  public void delete(Long id) {
    Objects.requireNonNull(id, "id must not be null");

    final Hero entity = heroRepository.findOne(id);
    if (entity != null) {
      heroRepository.delete(entity);
      log.debug("Deleted hero with id: {}", id);
    }
  }
}
