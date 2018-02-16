package ch.softwareplus.blueprints.hero.service.api;

/**
 * This class represents the hero data transfer object.
 * 
 * @author Gökhan Demirkiyik
 */
public class HeroDTO {

  private Long id;
  private String name;

  public HeroDTO() {}

  public HeroDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
