package ch.softwareplus.blueprints.hero.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Represents the Hero resource.
 */
@Relation(value = "hero", collectionRelation = "heroes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Hero", description = "Hero resource")
public class HeroModel extends RepresentationModel<HeroModel> {

    private String name;
}
