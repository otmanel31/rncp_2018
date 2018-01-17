package com.otmanel.firstSecurity.web;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.otmanel.firstSecurity.metier.Role;
import com.otmanel.firstSecurity.metier.User;
import com.otmanel.firstSecurity.repositories.IInternalRepository;
import com.otmanel.firstSecurity.repositories.RoleRepository;
import com.otmanel.firstSecurity.repositories.UserRepository;

@Controller
@RequestMapping(value="/")
public class indexController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private IInternalRepository internalRepo;
	
	@Autowired
	private PasswordEncoder myPasswordEncoder;
	
	@RequestMapping(value="/", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> index(){
		Map<String, Object> result = new HashMap<>();
		
		result.put("message", "vous etes sur la page d acccueil");
		result.put("date", new Date());
		return result;
	}
	
	@RequestMapping(value="/public", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> indexPublic(){
		Map<String, Object> result = new HashMap<>();
		
		result.put("message", "vous etes sur la page d public");
		result.put("date", new Date());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		result.put("user in principale", auth.getName());
		result.put("authorities", auth.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.toList()));
		return result;
	}
	
	@RequestMapping(value="/client", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> indexClient(Authentication p){
		Map<String, Object> result = new HashMap<>();
		
		result.put("message", "vous etes sur la page d client");
		result.put("date", new Date());
		result.put("user in principale", p.getName());
		
		return result;
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> indexAdmin(Authentication p){
		Map<String, Object> result = new HashMap<>();
		
		result.put("message", "vous etes sur la page d admin");
		result.put("date", new Date());
		result.put("user in principale", p.getName());
		result.put("authorities", p.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.toList()));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_VISITOR')")
	@RequestMapping(value="/toto", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> index2(){
		Map<String, Object> result = new HashMap<>();
		
		result.put("message", "vous etes sur la page d toto");
		result.put("date", new Date());
		return result;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User register(@RequestParam("username") String username,
								@RequestParam("password") String password) {
		//User u = new User(0, username, myPasswordEncoder.encode(password), true);
		Role r = roleRepo.findByName("ROLE_USER");
		User u = internalRepo.createUser(username, password, r);
		return u;
	}
	
}
