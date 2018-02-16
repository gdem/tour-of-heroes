package ch.softwareplus.blueprints.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import ch.softwareplus.blueprints.hero.repository.domain.Hero;
import ch.softwareplus.blueprints.hero.service.HeroMapper;
import ch.softwareplus.blueprints.hero.service.api.HeroDTO;

/**
 * This class represents the unit test for {@link HeroMapper}.
 * 
 * @author Gökhan Demirkiyik
 */
public class HeroMapperUnitTest {

  private HeroMapper mapper;

  @Before
  public void setUp() {
    this.mapper = new HeroMapper();
  }

  @Test(expected = NullPointerException.class)
  public void testConvertWithNull() {
    mapper.convert(null);
  }

  @Test
  public void testConvert() {
    final Hero hero = new Hero("Junito");
    hero.setId(1L);

    HeroDTO actual = mapper.convert(hero);

    assertThat(actual, notNullValue());
    assertThat(actual.getId(), equalTo(1L));
    assertThat(actual.getName(), equalTo("Junito"));
  }

}
