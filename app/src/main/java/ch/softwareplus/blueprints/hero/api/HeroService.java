package ch.softwareplus.blueprints.hero.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
     * Returns an optional {@code Hero} by the given id.
     *
     * @param id the id to find. Cannot be {@code null}.
     * @return the optional {@code Hero}.
     */
    Optional<Hero> findById(Long id);

    /**
     * Creates a new {@code Hero}.
     *
     * @param createHero the request object to create a new hero. Cannot be {@code null}.
     * @return the newly saved {@code Hero}.
     */
    Hero createNew(Hero hero);

    /**
     * Updates an existing {@code Hero}.
     *
     * @param hero the request object to update a new hero. Cannot be {@code null}.
     * @return the updated {@code Hero}.
     */
    Hero update(Hero hero);

    /**
     * Deletes the hero by the given id.
     *
     * @param id the id to delete. Cannot be {@code null}.
     */
    void delete(Long id);
}
