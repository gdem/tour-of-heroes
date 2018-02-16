package ch.softwareplus.blueprints.hero.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ch.softwareplus.blueprints.hero.repository.domain.Hero;

/**
 * This class represents the heroe repository using Spring Data Repository.
 * 
 * @author Gökhan Demirkiyik
 */
@Repository
public interface HeroRepository extends PagingAndSortingRepository<Hero, Long> {

  List<Hero> findAllByOrderByName();

  Hero findByName(String name);

  List<Hero> findByNameContainingOrderByName(String partialName);
}
