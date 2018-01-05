package com.firstspringdataproject.funspy.web;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.firstspringdataproject.funspy.metier.Article;
import com.firstspringdataproject.funspy.repositories.IArticleDao;

@Controller
@RequestMapping(value="/")
public class IndexController {

	@Autowired
	private IArticleDao articleDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "redirect:/articles";
	}

	@RequestMapping(value = "/bonjour/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		ModelAndView model = new ModelAndView();
		model.setViewName("bonjour");
		model.addObject("message", "bonjour " + name);

		return model;

	}
	
	@RequestMapping(value="/articles", method=RequestMethod.GET)
	public ModelAndView liste(@RequestParam("searchTerm") Optional<String> searchTerm) {
		ModelAndView mv =  new ModelAndView("liste");
		if (searchTerm.isPresent()) mv.addObject("articles", this.articleDao.findByNomContaining(searchTerm.get()));
		else mv.addObject("articles", articleDao.findAll());
		return mv;
	}
	
	@RequestMapping(value="/articles", method=RequestMethod.POST)
	public String save(@RequestParam(value="id", defaultValue="0") int id,@RequestParam("nom") String nom, 
			@RequestParam("description") String description, @RequestParam(value="prix", defaultValue="0.0") double prix,
			@RequestParam(value="poid", defaultValue="0.0") double poid) {
		Article a = null;
		if (id == 0) {
			a = new Article(0, nom, description, prix, poid, LocalDateTime.now());
		}else {
			a = articleDao.findOne(id);
			if (a == null ) return "redirect:/articles";
			a.setNom(nom);
			a.setDescription(description);
			a.setPrix(prix);
			a.setPoid(poid);
		}
		
		this.articleDao.save(a);
		return "redirect:/articles";
	}
	
	@RequestMapping(value="/delete/{id:[0-9]+}", method=RequestMethod.POST)
	public String delete(@PathVariable("id") int id) {
		
		this.articleDao.delete(id);
		return "redirect:/articles";
	}
	
	@RequestMapping(value="/editArticle/{id:[0-9]+}", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") int id) {
		Article a = this.articleDao.findOne(id);
		if (a == null) return new ModelAndView("redirect:/articles");
		ModelAndView model = new ModelAndView("liste");
		model.addObject("id", a.getId());
		model.addObject("nom", a.getNom());
		model.addObject("prix", a.getPrix());
		model.addObject("poid", a.getPoid());
		model.addObject("articles", this.articleDao.findAll()); // reafficher la liste des articles car mm page ici
		return model;
	}

}