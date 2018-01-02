package com.applicationrevision.filmRevisionJsp.web;

import java.util.Date;
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

import com.applicationrevision.filmRevisionJsp.metier.Film;
import com.applicationrevision.filmRevisionJsp.repositories.IFilmDao;

@Controller
@RequestMapping(value="/")
public class IndexController {
	
	@Autowired
	IFilmDao filmDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "redirect:/films";
	}

	
	@RequestMapping(value = "/Index", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", "bonjour depuis spring 3 mvc");
		return "bonjour";

	}

	@RequestMapping(value = "/bonjour/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {
		ModelAndView model = new ModelAndView();
		model.setViewName("bonjour");
		model.addObject("message", "bonjour " + name);

		return model;

	}
	
	@RequestMapping(value="/films", method=RequestMethod.GET)
	public ModelAndView liste() {
		ModelAndView model = new ModelAndView("films");
		model.addObject("films", this.filmDao.findAll());
		return model;
	}
	
	@RequestMapping(value="/films", method=RequestMethod.POST)
	public String save(@RequestParam("name") String name, @RequestParam("author")  String author, 
			@RequestParam("year")  int year, @RequestParam("synopsis")  String synopsis) {
		Film f = new Film(0, name, author, year, synopsis);
		this.filmDao.save(f);
		return "redirect:/films";
	}
	
	// path varible 
	@RequestMapping(value="/films/{id:[0-9]+}", method=RequestMethod.POST)
	public String delete(@PathVariable("id") int id) {
		this.filmDao.delete(id);
		return "redirect:/films";
	}
	

}