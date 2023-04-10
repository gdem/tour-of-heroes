package ch.softwareplus.blueprints.hero.rest;

import ch.softwareplus.blueprints.hero.api.CreateHero;
import ch.softwareplus.blueprints.hero.api.Hero;
import ch.softwareplus.blueprints.hero.api.HeroService;
import ch.softwareplus.blueprints.hero.api.UpdateHero;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Represents the unit test for {@link HeroController}.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
public class HeroControllerUnitTest {

    private final String PAGE_NUMBER_STRING = "1";
    private final int PAGE_NUMBER = 1;
    private final String PAGE_SIZE_STRING = "5";
    private final int PAGE_SIZE = 5;
    public static final String HAL_JSON_UTF8_VALUE = "application/hal+json;charset=UTF-8";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HeroService heroService;

    @SpyBean
    private HeroModelAssembler assembler;


    @Autowired
    private ObjectMapper mapper;


    @Test
    public void testListByPage() throws Exception {

        // create mock data
        final List<Hero> heroes = createData();
        final PageImpl<Hero> page =
                new PageImpl<>(heroes, PageRequest.of(PAGE_NUMBER, PAGE_SIZE), heroes.size());

        // mock the behaviour
        given(heroService.getPage(any())).willReturn(page);

        mvc.perform(get("/heroes/")
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

        verify(heroService).getPage(any());
        verifyNoMoreInteractions(heroService);
    }

    @Test
    public void testGetHero() throws Exception {

        given(heroService.findById(1L)).willReturn(new Hero(1L, "Narco"));

        mvc.perform(get("/heroes/" + 1)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("Narco")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/heroes/1")));

        verify(heroService).findById(eq(1L));
        verifyNoMoreInteractions(heroService);
    }

    @Test
    public void testCreateHero() throws Exception {
        final CreateHero newHero = new CreateHero();
        newHero.setName("JUniter");

        given(heroService.createNew(any())).willReturn(new Hero(5L, "Juniter"));

        mvc.perform(post("/heroes/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newHero))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/heroes/5"));

        verify(heroService).createNew(any());
        verifyNoMoreInteractions(heroService);
    }


    @Test
    public void testUpdateHero() throws Exception {
        final UpdateHero updateHero = new UpdateHero();
        updateHero.setId(2L);
        updateHero.setName("UpdatedHero");

        given(heroService.updateExisting(any())).willReturn(new Hero(2L, "UpdatedHero"));

        mvc.perform(put("/heroes/" + 2)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateHero))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("UpdatedHero")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/heroes/2")));

        verify(heroService).updateExisting(any());
        verifyNoMoreInteractions(heroService);
    }


    @Test
    public void testDeleteHero() throws Exception {

        willDoNothing().given(heroService).delete(1L);

        mvc.perform(delete("/heroes/" + 1)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(heroService).delete(eq(1L));
        verifyNoMoreInteractions(heroService);
    }

    private List<Hero> createData() {
        final List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero(2L, "Bombasto"));
        heroes.add(new Hero(3L, "Celeritas"));
        heroes.add(new Hero(4L, "Magneta"));
        heroes.add(new Hero(1L, "Narco"));
        return heroes;
    }
}
