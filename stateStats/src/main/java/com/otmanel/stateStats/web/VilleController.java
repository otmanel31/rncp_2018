package com.otmanel.stateStats.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.otmanel.stateStats.metier.Ville;
import com.otmanel.stateStats.repositories.VilleRepository;

@Controller
@RequestMapping(value="extended_api/villes")
public class VilleController {

	@Autowired
	private VilleRepository villeRepo;
	
	
	@RequestMapping(value="/findWithPop/{popMin:[0-9]+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // serialse donne en json pour eviter de lui faire croire renvoie du jsp
	public Page<Ville> findWithPop( @PathVariable("popMin") int popMin, @PageableDefault(page=0, size=10) Pageable p){
		return this.villeRepo.findByPopGreaterThan(popMin, p);
	}
}
