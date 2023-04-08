package ch.softwareplus.blueprints.hero;

import ch.softwareplus.blueprints.hero.api.CreateHero;
import ch.softwareplus.blueprints.hero.api.UpdateHero;
import ch.softwareplus.blueprints.hero.domain.HeroEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Represents the unit test for {@link HeroServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class HeroServiceImplUnitTest {

    private static final long HERO_ID = 1L;
    private static final String HERO_NAME = "Juniter";

    @InjectMocks
    private HeroServiceImpl heroService;

    @Spy
    private HeroMapper mapper;

    @Mock
    private HeroRepository heroRepository;

    @Test
    public void testFindByIdWithNull() {

        assertThrows(NullPointerException.class, () -> heroService.findById(null));

        verifyNoInteractions(heroRepository);
    }

    @Test
    public void testFindById() {
        final var maybeEntity = createEntity();

        when(heroRepository.findById(HERO_ID)).thenReturn(maybeEntity);

        heroService.findById(HERO_ID);

        verify(heroRepository, times(1)).findById(eq(HERO_ID));
        verify(mapper, times(1)).toHero(any());
        verifyNoMoreInteractions(heroRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void testUpdateExisting() {
        final var maybeEntity = createEntity();

        when(heroRepository.findById(HERO_ID)).thenReturn(maybeEntity);
        when(heroRepository.save(maybeEntity.get())).thenReturn(maybeEntity.get());

        final UpdateHero command = new UpdateHero();
        command.setId(HERO_ID);
        command.setName("Daredevil");
        heroService.updateExisting(command);

        verify(heroRepository, times(1)).findById(eq(HERO_ID));
        verify(heroRepository, times(1)).save(eq(maybeEntity.get()));
        verify(mapper, times(1)).toHero(any());
        verify(mapper, times(1)).update(eq(command), eq(maybeEntity.get()));


        verifyNoMoreInteractions(heroRepository);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void testCreateNew() {
        final var entity = createEntity();

        when(heroRepository.save(any())).thenReturn(entity.get());

        final CreateHero createHero = new CreateHero();
        createHero.setName(HERO_NAME);
        heroService.createNew(createHero);
    }

    @Test
    public void testDeleteById() {
        final var maybeEntity = createEntity();

        when(heroRepository.findById(HERO_ID)).thenReturn(maybeEntity);
        doNothing().when(heroRepository).delete(maybeEntity.get());

        heroService.delete(HERO_ID);

        verify(heroRepository, times(1)).findById(eq(HERO_ID));
        verify(heroRepository, times(1)).delete(eq(maybeEntity.get()));
        verifyNoMoreInteractions(heroRepository);
        verifyNoMoreInteractions(mapper);
    }

    private Optional<HeroEntity> createEntity() {
        final HeroEntity entity = new HeroEntity(1L, HERO_NAME);
        entity.setId(HERO_ID);
        return Optional.of(entity);
    }
}
