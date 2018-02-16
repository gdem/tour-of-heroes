package ch.softwareplus.blueprints.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.softwareplus.blueprints.hero.repository.HeroRepository;
import ch.softwareplus.blueprints.hero.repository.domain.Hero;
import ch.softwareplus.blueprints.hero.service.HeroMapper;
import ch.softwareplus.blueprints.hero.service.HeroServiceImpl;
import ch.softwareplus.blueprints.hero.service.api.CreateHero;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;
import ch.softwareplus.blueprints.hero.service.api.UpdateHero;

/**
 * This class represents the unit test for {@link HeroServiceImpl}.
 * 
 * @author Gökhan Demirkiyik
 */
public class HeroServiceImplUnitTest {

  private static final long HERO_ID = 1L;
  private static final String HERO_NAME = "Juniter";

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @InjectMocks
  private HeroServiceImpl heroService;

  @Mock
  private HeroMapper mapper;

  @Mock
  private HeroRepository heroRepository;

  @Test(expected = NullPointerException.class)
  public void testFindByIdWithNull() {
    heroService.findById(null);

    verifyZeroInteractions(heroRepository);
  }

  @Test
  public void testFindById() {
    final Hero entity = createEntity();

    when(heroRepository.findOne(HERO_ID)).thenReturn(entity);
    when(mapper.convert(entity)).thenReturn(new HeroDTO(HERO_ID, HERO_NAME));

    heroService.findById(HERO_ID);

    verify(heroRepository, times(1)).findOne(eq(HERO_ID));
    verify(mapper, times(1)).convert(eq(entity));
    verifyNoMoreInteractions(heroRepository);
    verifyNoMoreInteractions(mapper);
  }

  @Test
  public void testUpdateExisting() {
    final Hero entity = createEntity();

    when(heroRepository.findOne(HERO_ID)).thenReturn(entity);
    when(heroRepository.save(entity)).thenReturn(entity);
    when(mapper.convert(entity)).thenReturn(convert(entity));

    final UpdateHero command = new UpdateHero();
    command.setId(HERO_ID);
    command.setName("Daredevil");
    heroService.updateExisting(command);

    verify(heroRepository, times(1)).findOne(eq(HERO_ID));
    verify(heroRepository, times(1)).save(eq(entity));
    verify(mapper, times(1)).convert(eq(entity));

    verifyNoMoreInteractions(heroRepository);
    verifyNoMoreInteractions(mapper);
  }

  @Test
  public void testCreateNew() {
    final Hero entity = createEntity();
    Hero any = any(Hero.class);

    when(heroRepository.save(any)).thenReturn(entity);
    when(mapper.convert(entity)).thenReturn(convert(entity));

    final CreateHero createHero = new CreateHero();
    createHero.setName(HERO_NAME);
    heroService.createNew(createHero);

    // verify(heroRepository, times(1)).save(any);
    // verify(mapper, times(1)).convert(any);
    // verifyNoMoreInteractions(heroRepository);
    // verifyNoMoreInteractions(mapper);
  }

  @Test
  public void testDeleteById() {
    final Hero entity = createEntity();

    when(heroRepository.findOne(HERO_ID)).thenReturn(entity);
    doNothing().when(heroRepository).delete(entity);

    heroService.delete(HERO_ID);

    verify(heroRepository, times(1)).findOne(eq(HERO_ID));
    verify(heroRepository, times(1)).delete(eq(entity));
    verifyNoMoreInteractions(heroRepository);
    verifyNoMoreInteractions(mapper);
  }

  private Hero createEntity() {
    final Hero entity = new Hero(HERO_NAME);
    entity.setId(HERO_ID);
    return entity;
  }

  private HeroDTO convert(Hero hero) {
    return new HeroDTO(hero.getId(), hero.getName());
  }
}
