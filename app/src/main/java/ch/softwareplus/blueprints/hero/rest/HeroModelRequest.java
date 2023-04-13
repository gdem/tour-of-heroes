package ch.softwareplus.blueprints.hero.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HeroRequest", description = "Hero resource input")
public class HeroModelRequest {
    @NotEmpty
    private String name;
}
