package com.lonconto.mangasManiaBoot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lonconto.mangasManiaBoot.metier.Role;
import com.lonconto.mangasManiaBoot.metier.User;
import com.lonconto.mangasManiaBoot.repositories.RoleRepository;
import com.lonconto.mangasManiaBoot.repositories.UserRepository;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder myPassworEncoder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if (userRepo.count() == 0) {
			this.log.info("********************************** base seem empty, initalizing default user and role");
			Role r_admin = roleRepo.save(new Role(0, "ROLE_ADMIN"));
			Role r_contributor = roleRepo.save(new Role(0, "ROLE_CONTRIBUTOR"));
			Role r_visitor = roleRepo.save(new Role(0, "ROLE_VISITOR"));
			
			User u1 = new User(0, "admin", myPassworEncoder.encode("admin"));
			u1.getRoles().add(r_admin);
			u1.getRoles().add(r_contributor);
			u1.getRoles().add(r_visitor);
			userRepo.save(u1);
			
			User u2 = new User(0, "elon", myPassworEncoder.encode("mars4love"));
			u2.getRoles().add(r_contributor);
			u2.getRoles().add(r_visitor);
			userRepo.save(u2);
			
			User u3 = new User(0, "visitor", myPassworEncoder.encode("visitor"));
			u3.getRoles().add(r_visitor);
			userRepo.save(u3);
		}else this.log.info("********************************** base hase already users ........");
	}

}
