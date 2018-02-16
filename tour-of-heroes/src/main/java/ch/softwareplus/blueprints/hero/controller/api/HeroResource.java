package ch.softwareplus.blueprints.hero.controller.api;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * This class represents the Hero DTO.
 * 
 * @author Gökhan Demirkiyik
 */
@Relation(value = "hero", collectionRelation = "heroes")
public class HeroResource extends ResourceSupport {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
