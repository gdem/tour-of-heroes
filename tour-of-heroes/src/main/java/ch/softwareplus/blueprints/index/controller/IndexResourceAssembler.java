package ch.softwareplus.blueprints.index.controller;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Component;

import ch.softwareplus.blueprints.hero.controller.api.HeroResource;
import ch.softwareplus.blueprints.index.controller.api.IndexResource;

/**
 * This class represents the index resource assembler.
 * 
 * @author Gökhan Demirkiyik
 */
@Component
public class IndexResourceAssembler {
  private final RelProvider relProvider;
  private final EntityLinks entityLinks;

  /**
   * Constructs a new {@code IndexResourceAssembler} by the given {@link RelProvider} and
   * {@link EntityLinks}.
   * 
   * @param relProvider
   * @param entityLinks
   */
  @Inject
  public IndexResourceAssembler(RelProvider relProvider, EntityLinks entityLinks) {
    this.relProvider = relProvider;
    this.entityLinks = entityLinks;
  }

  public IndexResource buildIndex() {
    // @formatter:off
    final List<Link> links = Arrays.asList(
        entityLinks.linkToCollectionResource(HeroResource.class).withRel(relProvider.getCollectionResourceRelFor(HeroResource.class))
        );
    final IndexResource resource = new IndexResource("Tour of Heroes", "RESTful API for the Tour of Heroes.");
    resource.add(links);
    // @formatter:on
    return resource;
  }
}
