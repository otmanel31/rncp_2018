package com.lonconto.firstBoot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.lonconto.firstBoot.metier.Rocket;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

	// configuration specifique au repositorie spring data rest
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		
		super.configureRepositoryRestConfiguration(config);
		// demander a spring data rest de renvoyer la clé primaire avc le reste de lobjet avc le json
		// pour les entités specifiés
		config.exposeIdsFor(Rocket.class);
	}

	
	
}
