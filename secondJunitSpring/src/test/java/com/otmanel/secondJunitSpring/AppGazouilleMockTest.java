package com.otmanel.secondJunitSpring;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.otmanel.secondJunitSpring.config.TestConfigMock;
import com.otmanel.secondJunitSpring.metier.Gazouille;
import com.otmanel.secondJunitSpring.repositories.IGazouilleDao;
import com.otmanel.secondJunitSpring.services.GazouilleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfigMock.class) // au lieu de donner un xml je lui donner la class annoté @bean
public class AppGazouilleMockTest {
	
	private GazouilleService gs;
	
	// va chercher ds le context c a dire ce quon lui dit ds le @contextConfig plus haut
	@Autowired
	private IGazouilleDao gazouilleDao;
	
	// initilaisation du gazouille service
	@Before // execution avant tt ls test
	public void prepareTest() {
		this.gs = new GazouilleService();
		// injecte gazouille dao ds gazouille service a la main => pas terrible  => ds spring boot inutile 
		this.gs.setGazouilleDao(this.gazouilleDao);
	}
	
	@Test
	public void gazouilleTestCount() {
		// et c la ou mockito va prendre tte sa puissance .....
		Mockito.when(this.gazouilleDao.findAll()).thenReturn(
				Arrays.asList(
					new Gazouille(1, "first", "first"),
					new Gazouille(1, "second", "second")
				)
		);
		int expected = 2;
		int actual = this.gs.readAllGazouile().size();
		assertEquals("should return 2 gazouille", expected, actual);
		// verifie que find all a été appelé au moins une fois
		Mockito.verify(gazouilleDao, Mockito.atLeastOnce()).findAll();
	}
	
	@After // execution avant tt ls test
	public void afterTest() {
		// on reinitalise le mock 
		Mockito.reset(this.gazouilleDao);
	}
}
