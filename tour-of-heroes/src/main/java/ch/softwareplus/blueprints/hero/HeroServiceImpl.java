package ch.softwareplus.blueprints.hero;

import ch.softwareplus.blueprints.hero.api.CreateHero;
import ch.softwareplus.blueprints.hero.api.Hero;
import ch.softwareplus.blueprints.hero.api.HeroService;
import ch.softwareplus.blueprints.hero.api.UpdateHero;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * This implementation of {@link HeroService} uses the {@link HeroRepository} to provide access to
 * the database.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HeroServiceImpl implements HeroService {

    private final HeroRepository repository;
    private final HeroMapper mapper;

    @Override
    public Page<Hero> getPage(@NonNull Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toHero);
    }

    @Override
    public Optional<Hero> findById(@NonNull Long id) {
        return repository.findById(id).map(mapper::toHero);
    }


    @Override
    public Hero createNew(@NonNull CreateHero createHero) {
        final var newEntity = repository.save(mapper.toEntity(createHero));
        log.debug("Created new hero with id: {}", newEntity.getId());
        return mapper.toHero(newEntity);
    }

    @Override
    public Hero updateExisting(@NonNull UpdateHero updateHero) {
        var maybeEntity = repository.findById(updateHero.getId());
        var heroEntity = maybeEntity.orElseThrow(() -> new NoSuchElementException());
        mapper.update(updateHero, heroEntity);
        final var updatedHeroEntity = repository.save(heroEntity);
        log.debug("Updated existing hero with id: {}", updatedHeroEntity.getId());
        return mapper.toHero(updatedHeroEntity);
    }

    @Override
    public void delete(@NonNull Long id) {
        final var maybeEntity = repository.findById(id);
        if (maybeEntity.isPresent()) {
            repository.delete(maybeEntity.get());
            log.debug("Deleted hero with id: {}", id);
        }
    }
}
