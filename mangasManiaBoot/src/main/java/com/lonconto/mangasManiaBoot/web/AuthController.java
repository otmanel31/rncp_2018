package com.lonconto.mangasManiaBoot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import com.lonconto.mangasManiaBoot.metier.User;
import com.lonconto.mangasManiaBoot.repositories.RoleRepository;
import com.lonconto.mangasManiaBoot.repositories.UserRepository;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value="/extended_api/auth")
@Log4j
public class AuthController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	
	@CrossOrigin(origins= {"http://localhost:4200"})
	@RequestMapping(value="/login", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User login(@RequestBody User u) {
		this.log.info("in login meth back end with user => " + u.getUsername());
		User us = this.userRepo.findByUsername(u.getUsername());
		if (us != null) return u;
		else throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "login failed with this username");
	}
	@CrossOrigin(origins= {"http://localhost:4200"})
	@RequestMapping(value="/register", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User register(@RequestParam("username") String username, @RequestParam("password") String password) {
		this.log.info("in register meth back end with user => " + username);
		
		User u = new User(0,username, myPasswordEncoder.encode(password));
		u.getRoles().add(this.roleRepo.findOne(2));
		if (u != null) {
			//u.setPassword(myPasswordEncoder.encode(u.getPassword()));
			//u.getRoles().add(this.roleRepo.findOne(2));
			return this.userRepo.save(u);
		}
		else throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "register failed with this username");
	}
}
