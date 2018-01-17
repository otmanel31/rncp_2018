package com.otmanel.firstSecurity.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.otmanel.firstSecurity.metier.Role;
import com.otmanel.firstSecurity.metier.User;
import com.otmanel.firstSecurity.repositories.IInternalRepository;

@Service
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

	Logger log = LogManager.getLogger(DatabaseInitializer.class);
	
	// au lieu de mettre entity manager on fait appel a notre Interface InternalRepo custom + propre
	@Autowired
	private IInternalRepository internalRepo;
	
	@Autowired
	private PasswordEncoder myPasswordEncoder; // va chercher celui qu'on a declarer ds le security vconfig
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if (internalRepo.countUsers() == 0) {
			// no user in db ==> probably empty db => create default user and their roles
			log.info("DataBase seem empty, initializing default content ............     ");
			
			// Role
			Role radmin = internalRepo.createRole("ROLE_ADMIN");
			Role ruser = internalRepo.createRole("ROLE_USER");
			Role rvisitor = internalRepo.createRole("ROLE_VISITOR");
			// User
			User uadmin = internalRepo.createUser("admin", myPasswordEncoder.encode("admin"), ruser, radmin);
			User uuser = internalRepo.createUser("ellon", myPasswordEncoder.encode("mars4love"), ruser);
			User uvisitor = internalRepo.createUser("otman", myPasswordEncoder.encode("meknes82"), rvisitor);
		}else log.info("database doesn't seem empty ! No initialization required");
		
	}

}
