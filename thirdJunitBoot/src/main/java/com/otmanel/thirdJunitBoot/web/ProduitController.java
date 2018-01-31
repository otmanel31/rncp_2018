package com.otmanel.thirdJunitBoot.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.otmanel.thirdJunitBoot.metier.Produit;
import com.otmanel.thirdJunitBoot.repositories.ProduitRepository;

@Controller
@RequestMapping(value="/extended_value")
public class ProduitController {
	
	@Autowired
	private ProduitRepository produitDao;
	
	@RequestMapping(value="/", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Produit> liste(@PageableDefault(page=0, size=5) Pageable p){
		return this.produitDao.findAll(p);
	}
	
	@RequestMapping(value="/cat/{categorie:.+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Produit> listeByCategorie(@PageableDefault(page=0, size=5) Pageable p, @PathVariable("categorie") String categorie){
		return this.produitDao.findByCategorie(p, categorie);
	}
	
	@RequestMapping(value="/save2", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Produit save2(@RequestBody Produit p) {
		if(p.getPrix() < 0) p.setPrix(0.0);
		return this.produitDao.save(p);
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> save(@RequestBody Produit p) {
		if(p.getPrix() < 0) p.setPrix(0.0);
		if (p.getPoid()< 0 ) {
			Map<String, Object> result = new HashMap<>();
			result.put("fieldError", "poids");
			result.put("errorMessage", "le poid ne peut etre negatif");
			result.put("entite", p);
			return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Produit prod =  this.produitDao.save(p);
		return new ResponseEntity<Object>(p, HttpStatus.OK);
	}

}
