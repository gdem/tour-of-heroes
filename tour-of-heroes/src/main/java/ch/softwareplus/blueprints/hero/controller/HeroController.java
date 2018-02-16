package ch.softwareplus.blueprints.hero.controller;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.softwareplus.blueprints.hero.controller.api.HeroResource;
import ch.softwareplus.blueprints.hero.service.api.CreateHero;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;
import ch.softwareplus.blueprints.hero.service.api.HeroService;
import ch.softwareplus.blueprints.hero.service.api.UpdateHero;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * REST controller for managing heroes.
 * 
 * @author Gökhan Demirkiyik
 */
@RestController
@ExposesResourceFor(HeroResource.class)
@RequestMapping("/heroes")
public class HeroController {

  private static final Logger log = LoggerFactory.getLogger(HeroController.class);

  @Inject
  private HeroService heroService;

  @Inject
  private HeroResourceAssembler assembler;

  // @formatter:off
  @ApiOperation(
      value = "List a page of heroes by page ordered by name", 
      nickname = "listByPage", 
      tags = {"Heroes Resource"})
  @ApiResponses({
      @ApiResponse(code = SC_OK, message = "The requested page of heroes"),
      @ApiResponse(code = SC_UNAUTHORIZED, message = "Not authenticated to access resource."), 
      @ApiResponse(code = SC_FORBIDDEN, message = "Not authorized to access resource."), 
      @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected internal server error occured.")
      })
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
  })
  // @formatter:on
  @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
  public PagedResources<HeroResource> listByPage(@PageableDefault Pageable pageable,
      PagedResourcesAssembler<HeroDTO> pagedAssembler) {
    log.debug("GET request to get page [{}] of heroes", pageable);

    Page<HeroDTO> page = heroService.getPage(pageable);
    return pagedAssembler.toResource(page, assembler);
  }

  // @formatter:off
  @ApiOperation(
      value = "Get hero by the given id", 
      nickname = "getHeroById", 
      tags = {"Heroes Resource"})
  @ApiResponses({
      @ApiResponse(code = SC_OK, message = "The requested hero found."),
      @ApiResponse(code = SC_NOT_FOUND, message = "The requested hero not found."),
      @ApiResponse(code = SC_UNAUTHORIZED, message = "Not authenticated to access resource."), 
      @ApiResponse(code = SC_FORBIDDEN, message = "Not authorized to access resource."), 
      @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected internal server error occured.")
      })
  // @formatter:on
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
  public HeroResource getHeroById(@PathVariable("id") final Long id) {
    log.debug("GET request to get hero with id: {}", id);

    final Optional<HeroDTO> maybeHero = heroService.findById(id);
    return maybeHero.map(assembler::toResource).orElseThrow(NoSuchElementException::new);
  }

  // @formatter:off
  @ApiOperation(
      value = "Save a new hero", 
      nickname = "saveNewHero", 
      tags = {"Heroes Resource"})
  @ApiResponses({
      @ApiResponse(code = SC_CREATED, message = "A new hero saved."),
      @ApiResponse(code = SC_UNAUTHORIZED, message = "Not authenticated to access resource."), 
      @ApiResponse(code = SC_FORBIDDEN, message = "Not authorized to access resource."), 
      @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected internal server error occured.")
      })
  // @formatter:on
  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createNewHero(@Valid @RequestBody final CreateHero newHeroe)
      throws URISyntaxException {
    log.debug("POST request to save new hero : {}.", newHeroe);

    final HeroDTO result = heroService.createNew(newHeroe);
    return ResponseEntity.created(new URI("/heroes/" + result.getId())).build();
  }

  // @formatter:off
  @ApiOperation(
      value = "Update the given hero", 
      nickname = "updateHero", 
      tags = {"Heroes Resource"})
  @ApiResponses({
      @ApiResponse(code = SC_OK, message = "Existing hero updated."),
      @ApiResponse(code = SC_NOT_FOUND, message = "The requested hero to update not found."),
      @ApiResponse(code = SC_UNAUTHORIZED, message = "Not authenticated to access resource."), 
      @ApiResponse(code = SC_FORBIDDEN, message = "Not authorized to access resource."), 
      @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected internal server error occured.")
      })
  // @formatter:on
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
  public HeroResource updateHero(@PathVariable(value = "id") Long id,
      @Valid @RequestBody final UpdateHero updateHero) throws URISyntaxException {
    log.debug("PUT request to replace the entire record with: {}.", updateHero);

    final HeroDTO result = heroService.updateExisting(updateHero);
    return assembler.toResource(result);
  }

  // @formatter:off
  @ApiOperation(
      value = "Delete hero by the given id", 
      nickname = "deleteHeroById", 
      tags = {"Heroes Resource"})
  @ApiResponses({
      @ApiResponse(code = SC_NO_CONTENT, message = "The given hero deleted."),
      @ApiResponse(code = SC_UNAUTHORIZED, message = "Not authenticated to access resource."), 
      @ApiResponse(code = SC_FORBIDDEN, message = "Not authorized to access resource."), 
      @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "An unexpected internal server error occured.")
      })
  // @formatter:on
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteHeroById(@PathVariable("id") final Long id) {
    log.debug("DELETE request to delete hero with id: {}.", id);

    heroService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
