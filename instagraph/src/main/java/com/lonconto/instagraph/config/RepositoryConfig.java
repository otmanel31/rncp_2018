package com.lonconto.instagraph.config;

import java.io.InputStream;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.lonconto.instagraph.metier.Content;
import com.lonconto.instagraph.metier.Image;
import com.lonconto.instagraph.metier.Tag;

public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		// TODO Auto-generated method stub
		super.configureRepositoryRestConfiguration(config);
		config.exposeIdsFor(Content.class, Tag.class, Image.class);
	}

}
