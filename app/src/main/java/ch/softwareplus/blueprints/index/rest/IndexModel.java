package ch.softwareplus.blueprints.index.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * This class represents the index resource.
 */
@Data
@RequiredArgsConstructor
public class IndexModel extends RepresentationModel<IndexModel> {

  private final String name;
  private final String description;
}
