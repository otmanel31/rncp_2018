package com.otmanel.todoList.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.ws.ResponseWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.otmanel.todoList.metier.TodoList;
import com.otmanel.todoList.repositorie.ITodoDao;

@Controller
@RequestMapping(value="/")
public class IndexController {
	
	Logger log = LogManager.getLogger();
	@Autowired
	private ITodoDao todoDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "bonjour";
	}
	
	@RequestMapping(value="/todos",  method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TodoList> liste(@RequestParam("libelle") Optional<String> libelle,
			@RequestParam("priorite") Optional<Integer> priorite,
			@RequestParam("dateLimite")
			@DateTimeFormat(iso=ISO.DATE) Optional<LocalDate> dateLimite){

		if (libelle.isPresent()) return todoDao.findByLibelleContaining(libelle.get());
		if (priorite.isPresent()) return todoDao.findByPrioriteGreaterThan(priorite.get());
		if (dateLimite.isPresent())
			log.info("******************************       " + dateLimite.get());
		if (dateLimite.isPresent()) return todoDao.findByDateLimiteBefore(dateLimite.get());
		
		ArrayList<TodoList> data = new ArrayList<>();
		todoDao.findAll().forEach(todo -> data.add(todo));
		return data;
	}
	
	@RequestMapping(value="/ptodos",  method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<TodoList> listePaginate(
			@PageableDefault(page=0, size=3) Pageable p,
			@RequestParam("libelle") Optional<String> libelle,
			@RequestParam("priorite") Optional<Integer> priorite,
			@RequestParam("dateLimite")
			@DateTimeFormat(iso=ISO.DATE) Optional<LocalDate> dateLimite){

		if (libelle.isPresent() && priorite.isPresent()) return this.todoDao.findByLibelleContainingAndPrioriteGreaterThan(libelle.get(), priorite.get(), p);
		
		if (libelle.isPresent()) return todoDao.findByLibelleContaining(libelle.get(), p);
		if (priorite.isPresent()) return todoDao.findByPrioriteGreaterThan(priorite.get(), p);
		if (dateLimite.isPresent())
			log.info("******************************       " + dateLimite.get());
		if (dateLimite.isPresent()) return todoDao.findByDateLimiteBefore(dateLimite.get(), p );
		
		return this.todoDao.findAll(p);
	}
	
	@RequestMapping(value="/todos/{id:[0-9]+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodoList findById(@PathVariable("id") int id) {
		TodoList t = this.todoDao.findOne(id);
		if (t == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "todo liste inexistante");
		return t;
	}
	
	@RequestMapping(value="/todos", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodoList save(@RequestBody TodoList t) {
		return this.todoDao.save(t);
	}
	
	@RequestMapping(value="/todos", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodoList update(@RequestBody TodoList t) {
		TodoList todo = this.todoDao.findOne(t.getId());
		if (todo == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "todo not exist");
		return this.todoDao.save(t);
	}
	
	@RequestMapping(value="/todos/{id:[0-9]+}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodoList delete(@PathVariable("id") int id) {
		TodoList t = this.todoDao.findOne(id);
		if (t == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "todo liste inexistante");
		this.todoDao.delete(t);
		return t;
	}
		
}