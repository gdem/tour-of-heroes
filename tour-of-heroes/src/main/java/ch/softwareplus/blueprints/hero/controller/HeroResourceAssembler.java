package ch.softwareplus.blueprints.hero.controller;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import ch.softwareplus.blueprints.hero.controller.api.HeroResource;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;

/**
 * This class represents a resource assembler to move the knowledge on how to build and link
 * resources out of the Resource and Controller implementations.
 * 
 * @author Gökhan Demirkiyik
 */
@Component
public class HeroResourceAssembler extends ResourceAssemblerSupport<HeroDTO, HeroResource> {

  /**
   * Constructs a new {@code HeroResourceAssembler}.
   */
  public HeroResourceAssembler() {
    super(HeroController.class, HeroResource.class);
  }

  @Override
  public HeroResource toResource(HeroDTO dto) {
    final HeroResource resource = createResourceWithId(dto.getId(), dto);
    resource.setName(dto.getName());
    return resource;
  }
}
