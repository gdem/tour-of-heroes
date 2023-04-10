package ch.softwareplus.blueprints.hero.rest;

import ch.softwareplus.blueprints.hero.api.CreateHero;
import ch.softwareplus.blueprints.hero.api.Hero;
import ch.softwareplus.blueprints.hero.api.HeroService;
import ch.softwareplus.blueprints.hero.api.UpdateHero;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

/**
 * REST controller for managing heroes.
 */
@RestController
@ExposesResourceFor(HeroModel.class)
@RequestMapping("/heroes")
@Slf4j
@RequiredArgsConstructor
public class HeroController {

    private final HeroService heroService;

    private final HeroModelAssembler assembler;

    private final PagedResourcesAssembler<Hero> pagedAssembler;

    @Operation(
            description = "List a page of heroes by page ordered by name",
            tags = {"Heroes Resource"}
    )
    @ApiResponse(responseCode = "200", description = "The requested page of heroes")
    @ApiResponse(responseCode = "401", description = "Not authenticated to access resource.")
    @ApiResponse(responseCode = "403", description = "Not authorized to access resource.")
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred.")
    @Parameters({
            @Parameter(name = "page", description = "Results page you want to retrieve (0..N)", schema = @Schema(type = "integer")),
            @Parameter(name = "size", description = "Number of records per page.", schema = @Schema(type = "integer")),
            @Parameter(name = "sort", description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", schema = @Schema(type = "string"))
    })
    @GetMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<HeroModel> listByPage(@PageableDefault Pageable pageable) {
        log.debug("GET request to get page [{}] of heroes", pageable);

        final var page = heroService.getPage(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @Operation(
            description = "Get hero by the given id",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "The requested page of heroes")
    @ApiResponse(responseCode = "401", description = "Not authenticated to access resource.")
    @ApiResponse(responseCode = "403", description = "Not authorized to access resource.")
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred.")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public HeroModel getHeroById(@PathVariable("id") final Long id) {
        log.debug("GET request to get hero with id: {}", id);
        var result = heroService.findById(id);
        return assembler.toModel(result);
    }

    @Operation(
            description = "Save a new hero",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "The requested page of heroes")
    @ApiResponse(responseCode = "401", description = "Not authenticated to access resource.")
    @ApiResponse(responseCode = "403", description = "Not authorized to access resource.")
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred.")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNewHero(@Valid @RequestBody final CreateHero newHeroe) throws URISyntaxException {
        log.debug("POST request to save new hero : {}.", newHeroe);

        final var result = heroService.createNew(newHeroe);
        return ResponseEntity.created(new URI("/heroes/" + result.getId())).build();
    }

    @Operation(
            description = "Update the given hero",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "The requested page of heroes")
    @ApiResponse(responseCode = "401", description = "Not authenticated to access resource.")
    @ApiResponse(responseCode = "403", description = "Not authorized to access resource.")
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    public HeroModel updateHero(@PathVariable(value = "id") Long id,
                                @Valid @RequestBody final UpdateHero updateHero) {
        log.debug("PUT request to replace the entire record with: {}.", updateHero);

        final var result = heroService.updateExisting(updateHero);
        return assembler.toModel(result);
    }

    @Operation(
            description = "Delete hero by the given id",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "The requested page of heroes")
    @ApiResponse(responseCode = "401", description = "Not authenticated to access resource.")
    @ApiResponse(responseCode = "403", description = "Not authorized to access resource.")
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteHeroById(@PathVariable("id") final Long id) {
        log.debug("DELETE request to delete hero with id: {}.", id);
        heroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
