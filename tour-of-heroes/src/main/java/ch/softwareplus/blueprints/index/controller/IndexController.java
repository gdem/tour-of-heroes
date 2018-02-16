package ch.softwareplus.blueprints.index.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.softwareplus.blueprints.index.controller.api.IndexResource;

/**
 * REST controller for building the index.
 * 
 * @author Gökhan Demirkiyik
 */
@RestController
@RequestMapping("/")
public class IndexController {

  private static final Logger log = LoggerFactory.getLogger(IndexController.class);

  private final IndexResourceAssembler assembler;

  /**
   * Constructs the {@code IndexController} by the given {@link IndexResourceAssembler}.
   * 
   * @param assembler the assembler to use for building the index.
   */
  @Inject
  public IndexController(IndexResourceAssembler assembler) {
    this.assembler = assembler;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<IndexResource> index() {
    log.debug("GET request to get index page");
    return ResponseEntity.ok(assembler.buildIndex());
  }
}
