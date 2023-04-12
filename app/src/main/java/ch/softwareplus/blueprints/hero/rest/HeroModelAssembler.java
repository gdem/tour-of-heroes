package ch.softwareplus.blueprints.hero.rest;

import ch.softwareplus.blueprints.hero.api.Hero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Represents a resource assembler to move the knowledge on how to build and link
 * resources out of the Resource and Controller implementations.
 */
@Component
public class HeroModelAssembler extends RepresentationModelAssemblerSupport<Hero, HeroModel> {

    @Autowired
    private HeroModelMapper mapper;

    public HeroModelAssembler() {
        super(HeroController.class, HeroModel.class);
    }

    @Override
    public HeroModel toModel(Hero hero) {
        var resource = createModelWithId(hero.id(), hero);
        var result = mapper.toResource(hero);
        result.add(resource.getLinks());
        return result;
    }
}
