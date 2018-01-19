package com.lonconto.instagraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import com.lonconto.instagraph.metier.User;
import com.lonconto.instagraph.repositories.RoleRepository;
import com.lonconto.instagraph.repositories.UserRepositoy;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping(value="/extended_api/auth")
@Log4j
public class AuthController {
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	@Autowired
	private UserRepositoy userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@CrossOrigin(origins={"http://localhost:4200"}, methods={ RequestMethod.OPTIONS, RequestMethod.POST})
	@RequestMapping(value="/login", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User login(@RequestBody User user) {
		this.log.info("in login meth with : " + user.getUsername());
		User u = userRepo.findByUsername(user.getUsername()) ;
		if (u != null ) return u;
		else throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "login failed with this username: " + user.getUsername());
	}
	
	@CrossOrigin(origins={"http://localhost:4200"}, methods={ RequestMethod.OPTIONS, RequestMethod.POST})
	@RequestMapping(value="/register", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@PreAuthorize("permitAll")
	public User register(@RequestParam("username") String username, @RequestParam("password") String password) {
		this.log.info("in REGISTER meth with : " + username + " " + password);
		User u = new User(0, username, myPasswordEncoder.encode(password),true);
		u.getRoles().add(roleRepo.findByName("ROLE_USER"));
		u = userRepo.save(u);
		return u;
	}
	
	
}
