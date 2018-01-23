package exoUnitTest.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.otmanel.exoUnitTest.repositories.IntervenantDao;
import com.otmanel.exoUnitTest.repositories.InterventionDao;

@Configuration
public class TestConfig {

	@Bean
	public InterventionDao interventionDao() {
		return Mockito.mock(InterventionDao.class);
	}
	
	@Bean
	public IntervenantDao intervenantDao() {
		return Mockito.mock(IntervenantDao.class);
	}
}
