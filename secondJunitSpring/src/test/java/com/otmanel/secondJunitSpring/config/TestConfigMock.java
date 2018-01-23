package com.otmanel.secondJunitSpring.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.otmanel.secondJunitSpring.repositories.IGazouilleDao;


@Configuration
public class TestConfigMock {
	@Bean
	public IGazouilleDao gazouilleDao() {
		// renvoyer un faux dao qui ressemble au Igazouille dao
		return Mockito.mock(IGazouilleDao.class);
	}
	
}
