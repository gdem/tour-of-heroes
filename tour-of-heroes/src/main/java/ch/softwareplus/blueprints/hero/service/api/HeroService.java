package ch.softwareplus.blueprints.hero.service.api;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface specifies the hero service.
 * 
 * @author Gökhan Demirkiyik
 */
public interface HeroService {

  /**
   * Returns a page of {@code HeroDTO}.
   * 
   * @param pageable the pageable information to retrieve. Cannot be {@code null}.
   * @return the requested page of {@code HeroDTO}.
   */
  Page<HeroDTO> getPage(Pageable pageable);

  /**
   * Returns a optional {@code HeroDTO} by the given id.
   * 
   * @param id the id to find. Cannot be {@code null}.
   * @return the optional {@code HeroDTO}.
   */
  Optional<HeroDTO> findById(Long id);

  /**
   * Creates a new hero.
   * 
   * @param createHero the request object to create a new hero. Cannot be {@code null}.
   * @return the newly saved hero.
   */
  HeroDTO createNew(CreateHero createHero);

  /**
   * Updates an existing hero.
   * 
   * @param updateHero the request object to update a new hero. Cannot be {@code null}.
   * @return the updated hero.
   */
  HeroDTO updateExisting(UpdateHero updateHero);

  /**
   * Deletes the the hero by the given id.
   * 
   * @param id the id to delete. Cannot be {@code null}.
   */
  void delete(Long id);
}
