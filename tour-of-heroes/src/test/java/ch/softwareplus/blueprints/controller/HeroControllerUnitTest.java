package ch.softwareplus.blueprints.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.softwareplus.blueprints.hero.controller.HeroController;
import ch.softwareplus.blueprints.hero.controller.HeroResourceAssembler;
import ch.softwareplus.blueprints.hero.service.api.CreateHero;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;
import ch.softwareplus.blueprints.hero.service.api.HeroService;
import ch.softwareplus.blueprints.hero.service.api.UpdateHero;

/**
 * This class represents the unit test for {@link HeroController}.
 * 
 * @author Gökhan Demirkiyik
 */
@RunWith(SpringRunner.class)
@WebMvcTest(HeroController.class)
@EnableSpringDataWebSupport
public class HeroControllerUnitTest {

  private final String PAGE_NUMBER_STRING = "1";
  private final int PAGE_NUMBER = 1;
  private final String PAGE_SIZE_STRING = "5";
  private final int PAGE_SIZE = 5;
  public static final String HAL_JSON_UTF8_VALUE = "application/hal+json;charset=UTF-8";

  @Inject
  private MockMvc mvc;

  @MockBean
  private HeroService heroService;

  @SpyBean
  private HeroResourceAssembler assembler;

  @Inject
  private ObjectMapper mapper;


  @Test
  public void testListByPage() throws Exception {

    // create mock data
    final List<HeroDTO> heroes = createData();
    final PageImpl<HeroDTO> page =
        new PageImpl<>(heroes, new PageRequest(PAGE_NUMBER, PAGE_SIZE), heroes.size());

    // mock the behaviour
    given(heroService.getPage(any())).willReturn(page);

    // @formatter:off
    mvc.perform(get("/heroes")
          .accept(MediaTypes.HAL_JSON_VALUE)
          .param("number", PAGE_NUMBER_STRING)
          .param("size", PAGE_SIZE_STRING)
        )
      .andExpect(status().isOk())
      .andExpect(content().contentType(HAL_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$._embedded.heroes", hasSize(4)))
      .andExpect(jsonPath("$._embedded.heroes[0].name", is("Bombasto")))
      .andExpect(jsonPath("$._embedded.heroes[1].name", is("Celeritas")))
      .andExpect(jsonPath("$._embedded.heroes[2].name", is("Magneta")))
      .andExpect(jsonPath("$._embedded.heroes[3].name", is("Narco")));
    
    // @formatter:on


    verify(heroService).getPage(any());
    verifyNoMoreInteractions(heroService);
  }

  @Test
  public void testGetHero() throws Exception {

    given(heroService.findById(1L)).willReturn(Optional.of(new HeroDTO(1L, "Narco")));

    // @formatter:off
    mvc.perform(get("/heroes/" + 1)
        .accept(MediaTypes.HAL_JSON_VALUE)
        )
      .andExpect(status().isOk())
      .andExpect(content().contentType(HAL_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$.name", is("Narco")))
      .andExpect(jsonPath("$._links.self.href", is("http://localhost/heroes/1")));
    
    // @formatter:on


    verify(heroService).findById(eq(1L));
    verifyNoMoreInteractions(heroService);
  }

  @Test
  public void testCreateHero() throws Exception {
    final CreateHero newHero = new CreateHero();
    newHero.setName("JUniter");

    given(heroService.createNew(any())).willReturn(new HeroDTO(5L, "Juniter"));

    // @formatter:off
    mvc.perform(post("/heroes/")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(newHero))
        )
      .andExpect(status().isCreated())
      .andExpect(header().string(HttpHeaders.LOCATION, "/heroes/5"));
    // @formatter:on


    verify(heroService).createNew(any());
    verifyNoMoreInteractions(heroService);
  }


  @Test
  public void testUpdateHero() throws Exception {
    final UpdateHero updateHero = new UpdateHero();
    updateHero.setId(2L);
    updateHero.setName("UpdatedHero");

    given(heroService.updateExisting(any())).willReturn(new HeroDTO(2L, "UpdatedHero"));

    // @formatter:off
    mvc.perform(put("/heroes/" + 2)
          .accept(MediaTypes.HAL_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(updateHero))
        )
      .andExpect(status().isOk())
      .andExpect(content().contentType(HAL_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$.name", is("UpdatedHero")))
      .andExpect(jsonPath("$._links.self.href", is("http://localhost/heroes/2")));
    // @formatter:on


    verify(heroService).updateExisting(any());
    verifyNoMoreInteractions(heroService);
  }


  @Test
  public void testDeleteHero() throws Exception {

    willDoNothing().given(heroService).delete(1L);

    // @formatter:off
    mvc.perform(delete("/heroes/" + 1)
          .accept(MediaType.APPLICATION_JSON)
        )
      .andExpect(status().isNoContent());
    // @formatter:on


    verify(heroService).delete(eq(1L));
    verifyNoMoreInteractions(heroService);
  }

  private List<HeroDTO> createData() {
    final List<HeroDTO> heroes = new ArrayList<>();
    heroes.add(new HeroDTO(2L, "Bombasto"));
    heroes.add(new HeroDTO(3L, "Celeritas"));
    heroes.add(new HeroDTO(4L, "Magneta"));
    heroes.add(new HeroDTO(1L, "Narco"));
    return heroes;
  }
}
