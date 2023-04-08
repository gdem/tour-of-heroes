package ch.softwareplus.blueprints.index.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for building the index.
 */
@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class IndexController {

  private final IndexResourceAssembler assembler;

  @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<IndexModel> index() {
    log.debug("GET request to get index page");
    return ResponseEntity.ok(assembler.buildIndex());
  }
}
