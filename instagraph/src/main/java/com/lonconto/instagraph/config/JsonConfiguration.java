package com.lonconto.instagraph.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JsonConfiguration {

	@Autowired(required=true)
	public void configJackson(ObjectMapper jackson2ObjectMapper) {
		// configuration de la serialisation en json par jackson des dates
		jackson2ObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
	
	// permet de dynamiquement specifier une projection
	// et cette annotation est injecter par spring 
	@Bean
	public SpelAwareProxyProjectionFactory projectionFactory() {
		
		return new SpelAwareProxyProjectionFactory();
	}
}
