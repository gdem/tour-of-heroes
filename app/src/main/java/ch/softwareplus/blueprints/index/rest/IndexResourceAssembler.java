package ch.softwareplus.blueprints.index.rest;

import ch.softwareplus.blueprints.hero.rest.HeroModel;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents the index resource assembler.
 */
@Component
@RequiredArgsConstructor
public class IndexResourceAssembler {
    private final EntityLinks entityLinks;

    public IndexModel buildIndex() {
        final var links = Arrays.<Link>asList(
                entityLinks.linkToCollectionResource(HeroModel.class)
        );
        final var resource = new IndexModel("Tour of Heroes", "RESTful API for the Tour of Heroes.");
        resource.add(links);
        return resource;
    }
}
