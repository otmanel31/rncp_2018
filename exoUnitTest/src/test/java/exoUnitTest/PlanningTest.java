package exoUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.otmanel.exoUnitTest.metier.Intervenant;
import com.otmanel.exoUnitTest.metier.Intervention;
import com.otmanel.exoUnitTest.repositories.IntervenantDao;
import com.otmanel.exoUnitTest.repositories.InterventionDao;
import com.otmanel.exoUnitTest.service.PlannificateurService;
import com.otmanel.exoUnitTest.service.PlannificateurService.IntervenantNotAvailableError;
import com.otmanel.exoUnitTest.service.PlannificateurService.InterventionDatetimeError;

import static org.mockito.Mockito.*;

import exoUnitTest.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class PlanningTest {

	private PlannificateurService ps;
	@Autowired
	private IntervenantDao intervenantDAO;
	
	@Autowired
	private InterventionDao interventionDAO;
	
	@Before
	public void beforeTest() {
		System.out.println("in before test meth");
		this.ps = new PlannificateurService();
		
		this.ps.setInterventionDao(this.interventionDAO);
		this.ps.setIntervenantDao(this.intervenantDAO);
	}
	
	@After
	public void afterTest() {
		System.out.println("in end test methods");
		reset(this.interventionDAO, this.intervenantDAO);
	}
	
	@Test
	public void InterventionCount() {
		System.out.println("in test count interventions");
		PageRequest pr = new PageRequest(0, 5);
		when(this.interventionDAO.findAll(any(Pageable.class))).thenReturn(
				new PageImpl<>(Arrays.asList(
						new Intervention(1, "toulouse", LocalDateTime.now(), null, "playstation"),
						new Intervention(2, "versailles", LocalDateTime.now(), null,  "playstation")
				), pr, 2));

		/* OLD VERSION
		when(this.interventionDAO.findAll(pr)).thenReturn(
				new PageImpl<>(Arrays.asList(
						new Intervention(1, "toulouse", LocalDateTime.now(), null),
						new Intervention(2, "versailles", LocalDateTime.now(), null)
				), pr, 2));
		*/
		assertNotNull(this.ps);
		int expected = 2;
		int actual = this.ps.getAll(pr).getNumberOfElements();
		
		assertEquals("devrait avoir 2 intervention au total", expected, actual);
	}
	
	@Test(expected=InterventionDatetimeError.class)
	public void testDateFinBehindDateDebutFail (){
		Intervention g = new Intervention(1, "", LocalDateTime.now(), LocalDateTime.of(2017, 11, 11, 11, 11), "playstation");
		this.ps.createNewIntervention(g);
	}
	
	@Test(expected=IntervenantNotAvailableError.class)
	public void testCreateNewInterventionWithIntervenantNotAvailable() {
		System.out.println("in test IntervenantNotAvailableError interventions");
		PageRequest pr = new PageRequest(0, 5);
		
		Intervenant it = new Intervenant(1, "otman", "o@o.fr");
		Intervention i1 = new Intervention(1, "toulouse", LocalDateTime.now(), LocalDateTime.now().plusHours(4), "xbox");
		i1.setIntervenant(it);
		Intervention i2 = new Intervention(2, "versailles", LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(10), "sega");
		
		when(this.interventionDAO.findByDateDebutAfterAndDateDebutBeforeAndIntervenantId(
				any(LocalDateTime.class), any(LocalDateTime.class), anyInt())
		).thenReturn(Arrays.asList(
				i1, i2
				));


		Intervention i3 = new Intervention(3,"stains", LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(9), "nintendo 64");
		//Intervention i3 = new Intervention(3,"stains", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
		i3.setIntervenant(it);
		this.ps.createNewIntervention(i3);
	}
	
	@Test
	public void testGetInterventionAfterSomeDateBeginAndForOneMaterial() {
		PageRequest pr = new PageRequest(0, 5);
		
		/*when(this.interventionDAO.save(anyList())).thenReturn( Arrays.asList(
						new Intervention(1, "toulouse", LocalDateTime.now().minusMinutes(10), null, "playstation"),
						new Intervention(2, "versailles", LocalDateTime.now().minusMinutes(10), null,  "sega"),
						new Intervention(3, "stains", LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(9), "nintendo 64"),
						new Intervention(4, "stains", LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(9), "x box"),
						new Intervention(5, "Porte maillot", LocalDateTime.now().plusHours(9), LocalDateTime.now().plusHours(9), "x box")));
		*/
		when(this.interventionDAO.findByDateDebutAfterAndMateriel(any(LocalDateTime.class), anyString(), any(Pageable.class))).thenReturn(
				new PageImpl<>(Arrays.asList(
						new Intervention(1, "toulouse", LocalDateTime.now().minusMinutes(10), null, "playstation"),
						new Intervention(2, "versailles", LocalDateTime.now().minusMinutes(10), null,  "sega"),
						new Intervention(3, "stains", LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(9), "nintendo 64"),
						new Intervention(4, "stains", LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(9), "x box"),
						new Intervention(5, "  maillot", LocalDateTime.now().plusHours(9), LocalDateTime.now().plusHours(9), "x box")
				), pr, 5));
		
		int expected = 5;
		//int actual = this.interventionDAO.findByDateDebutAfterAndMateriel(LocalDateTime.now(), "x box", pr).getNumberOfElements();
		int actual = this.ps.getInterventionProgOnMateriel(LocalDateTime.now(), "x box", pr).getNumberOfElements();
		assertEquals("devrait retourner 2 intervention ",expected, actual);
	}
}
