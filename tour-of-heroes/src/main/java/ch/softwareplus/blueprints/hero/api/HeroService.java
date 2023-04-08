package ch.softwareplus.blueprints.hero.api;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Specifies the hero service.
 */
public interface HeroService {

    /**
     * Returns a page of {@code HeroDTO}.
     *
     * @param pageable the pageable information to retrieve. Cannot be {@code null}.
     * @return the requested page of {@code HeroDTO}.
     */
    Page<Hero> getPage(Pageable pageable);

    /**
     * Returns a optional {@code HeroDTO} by the given id.
     *
     * @param id the id to find. Cannot be {@code null}.
     * @return the optional {@code HeroDTO}.
     */
    Optional<Hero> findById(Long id);

    /**
     * Creates a new hero.
     *
     * @param createHero the request object to create a new hero. Cannot be {@code null}.
     * @return the newly saved hero.
     */
    Hero createNew(CreateHero createHero);

    /**
     * Updates an existing hero.
     *
     * @param updateHero the request object to update a new hero. Cannot be {@code null}.
     * @return the updated hero.
     */
    Hero updateExisting(UpdateHero updateHero);

    /**
     * Deletes the the hero by the given id.
     *
     * @param id the id to delete. Cannot be {@code null}.
     */
    void delete(Long id);
}
