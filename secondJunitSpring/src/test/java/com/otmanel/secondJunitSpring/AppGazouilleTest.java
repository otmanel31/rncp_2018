package com.otmanel.secondJunitSpring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.otmanel.secondJunitSpring.metier.Gazouille;
import com.otmanel.secondJunitSpring.repositories.GazouilleDaoMock;
import com.otmanel.secondJunitSpring.services.GazouilleService;

// va executer les test ds un contexte spring soit avc injection de dependance ... 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:testContext.xml") // au lieu de chercher ds le context il va ds ce fichier xml qui est noytr contexte special test avc le mock dao et sexecute ds un context spring
public class AppGazouilleTest {
	@Autowired
	private GazouilleService gazouilleService;
	
	@Autowired
	private GazouilleDaoMock gazouilleDaoMock;
	
	@Before
	public void beforetest() {
		this.gazouilleDaoMock.addGazouilles(new Gazouille(1,"premiere gazouille","bla bla bla vbla vla la voloille"),
				new Gazouille(2,"deuxieme gazouille","22 22 vla la voloille"));
	}
	
	@Test
	public void testCount() {
		int expected = 2;
		int actual = this.gazouilleService.readAllGazouile().size();
		assertEquals("devrait compter deux gazouz", expected, actual);
		
		expected = 3;
		this.gazouilleService.publish(new Gazouille(0, "etest", "test"));
		actual = this.gazouilleService.readAllGazouile().size();
		assertEquals("devrait compter 3 gazouz", expected, actual);
	}
	@Test
	public void testPublishGazouilleFind() {
		Gazouille g = this.gazouilleService.readGazouille(2);
		assertNotNull("devrait trouver 2", g);
		assertEquals("devrait trouvert id 2",2, g.getId());
		assertEquals("titre devrait etre deuxieme gazouille","deuxieme gazouille", g.getTitre());
		
		this.gazouilleService.publish(new Gazouille(0,"other", "blahh"));
		
		g = this.gazouilleService.readGazouille(3);
		//assertNotNull("devrait trouver gazouille 4", g);
		assertEquals("devrait trouvert id 4",3, g.getId());
	}
}
