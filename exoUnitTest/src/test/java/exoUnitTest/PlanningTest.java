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
	}
	
	@Test
	public void InterventionCount() {
		System.out.println("in test count interventions");
		PageRequest pr = new PageRequest(0, 5);
		when(this.interventionDAO.findAll(any(Pageable.class))).thenReturn(
				new PageImpl<>(Arrays.asList(
						new Intervention(1, "toulouse", LocalDateTime.now(), null),
						new Intervention(2, "versailles", LocalDateTime.now(), null)
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
		Intervention g = new Intervention(1, "", LocalDateTime.now(), LocalDateTime.of(2017, 11, 11, 11, 11));
		this.ps.createNewIntervention(g);
	}
}
