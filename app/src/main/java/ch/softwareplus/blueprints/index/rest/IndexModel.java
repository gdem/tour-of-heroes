package ch.softwareplus.blueprints.index.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * Represents the index resource using name and description attribute.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class IndexModel extends RepresentationModel<IndexModel> {

    private final String name;
    private final String description;
}
