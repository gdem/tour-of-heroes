package ch.softwareplus.blueprints.hero.api;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Record represents the hero object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hero {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String name;
}
