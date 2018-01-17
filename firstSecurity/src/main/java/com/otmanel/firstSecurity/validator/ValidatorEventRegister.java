package com.otmanel.firstSecurity.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

@Configuration
public class ValidatorEventRegister implements InitializingBean {

	private static Logger log = LogManager.getLogger(UserValidator.class);
	
	// service objet permettant d'enregistrer un validateur aupres de spring data ou repositor
	@Autowired
	private ValidatingRepositoryEventListener vrel;
	// la liste de ts les validateur detecté par spring
	@Autowired
	private Map<String, Validator> validators;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info(" *******************   initialisation des validateurs .... ");
		List<String> events = Arrays.asList("beforeCreate");
		 // Entry permet de controler les entree dune map
		for (Entry<String, Validator> entry: validators.entrySet()) {
			// x == evenement 
			events.stream().filter(x ->entry.getKey()
					.startsWith(x))
					.findFirst()
					.ifPresent(validator -> {
						log.info("ajput validateur " + validators + " => " + entry.getValue());
						vrel.addValidator(validator, entry.getValue());
					});
		}

	}

}
