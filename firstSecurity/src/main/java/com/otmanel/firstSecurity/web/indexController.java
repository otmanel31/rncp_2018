package com.otmanel.firstSecurity.web;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/")
public class indexController {
	
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
}
