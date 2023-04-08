package ch.softwareplus.blueprints.hero;

import ch.softwareplus.blueprints.hero.domain.HeroEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The heroe repository using Spring Data Repository.
 */
@Repository
public interface HeroRepository extends ListPagingAndSortingRepository<HeroEntity, Long>, ListCrudRepository<HeroEntity, Long> {

}
