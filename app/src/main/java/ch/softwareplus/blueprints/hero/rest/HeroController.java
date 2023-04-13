package ch.softwareplus.blueprints.hero.rest;

import ch.softwareplus.blueprints.hero.api.Hero;
import ch.softwareplus.blueprints.hero.api.HeroService;
import ch.softwareplus.blueprints.web.PatchHelper;
import ch.softwareplus.blueprints.web.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.json.JsonPatch;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    private final HeroModelMapper modelMapper;

    private final PatchHelper patchHelper;


    @Operation(
            description = "List a page of heroes by page ordered by name",
            tags = {"Heroes Resource"}
    )
    @ApiResponse(responseCode = "200", description = "Ok",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Hero.class))))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
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
            description = "Save a new hero",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "Ok",
            content = @Content(schema = @Schema(implementation = Hero.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeroModel> createNewHero(@Valid @RequestBody final HeroModelRequest request) {
        log.debug("POST request to save new hero : {}.", request);

        var hero = modelMapper.toHero(request);
        var created = heroService.createNew(hero);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        var result = assembler.toModel(created);
        return ResponseEntity.created(location).body(result);
    }

    @Operation(
            description = "Get hero by the given id",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "Ok",
            content = @Content(schema = @Schema(implementation = Hero.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public HeroModel getHeroById(@PathVariable("id") final Long id) {
        log.debug("GET request to get hero with id: {}", id);
        var result = heroService.findById(id).orElseThrow(ResourceNotFoundException::new);
        return assembler.toModel(result);
    }

    @Operation(
            description = "Update the given hero",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "Ok",
            content = @Content(schema = @Schema(implementation = Hero.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    public HeroModel updateHero(@PathVariable(value = "id") Long id,
                                @RequestBody @Valid HeroModelRequest request) {
        log.debug("PUT request to replace the entire record with: {}.", request);
        // Find the domain model that will be updated
        var hero = heroService.findById(id).orElseThrow(ResourceNotFoundException::new);
        // Update the domain model with the details from the API resource model
        modelMapper.update(hero, request);
        // Persist the changes
        final var result = heroService.update(hero);
        return assembler.toModel(result);
    }

    @Operation(
            description = "Patch the given hero",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "Ok",
            content = @Content(schema = @Schema(implementation = Hero.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json", produces = MediaTypes.HAL_JSON_VALUE)
    public HeroModel patchHero(@PathVariable(value = "id") Long id,
                               @RequestBody JsonPatch patchDocument) {
        log.debug("PATCH request to patch with: {}.", patchDocument);
        // Find the domain model that will be patched
        var hero = heroService.findById(id).orElseThrow(ResourceNotFoundException::new);
        var modelRequest = modelMapper.toModelRequest(hero);
        var patched = patchHelper.patch(patchDocument, modelRequest, HeroModelRequest.class);
        modelMapper.update(hero, patched);
        //var result = heroService.updateExisting(hero);
        return assembler.toModel(hero);
    }

    @Operation(
            description = "Delete hero by the given id",
            tags = {"Heroes Resource"})
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteHeroById(@PathVariable("id") final Long id) {
        log.debug("DELETE request to delete hero with id: {}.", id);
        heroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
