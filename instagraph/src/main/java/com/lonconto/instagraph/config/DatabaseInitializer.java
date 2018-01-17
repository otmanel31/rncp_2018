package com.lonconto.instagraph.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lonconto.instagraph.repositories.RoleRepository;
import com.lonconto.instagraph.repositories.UserRepositoy;

import lombok.extern.log4j.Log4j;

import com.lonconto.instagraph.config.DatabaseInitializer;
import com.lonconto.instagraph.metier.Role;
import com.lonconto.instagraph.metier.User;

@Service
@Log4j
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

	//Logger log = LogManager.getLogger(DatabaseInitializer.class);
	
	@Autowired
	private UserRepositoy userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if (userRepo.count() == 0) {
			this.log.info("********************************** base seem empty, initalizing default user and role");
			Role radmin = roleRepo.save(new Role(0,"ROLE_ADMIN"));
			Role ruser = roleRepo.save(new Role(0, "ROLE_USER"));
			
			User u1 = new User(0, "admin", myPasswordEncoder.encode("admin"), true);
			u1.getRoles().add(radmin);
			u1.getRoles().add(ruser);
			userRepo.save(u1);
			
			User u2 = new User(0, "ellon", myPasswordEncoder.encode("mars4love"), true);
			u2.getRoles().add(ruser);
			userRepo.save(u2);
		}
		else this.log.info("********************************** base hase already users ........");
		
	}

}
