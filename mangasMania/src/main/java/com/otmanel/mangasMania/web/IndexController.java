package com.otmanel.mangasMania.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.otmanel.mangasMania.metier.Manga;
import com.otmanel.repositories.IMangasDao;

@Controller
@RequestMapping(value="/")
public class IndexController {
	@Autowired
	private IMangasDao mangasDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "bonjour";
	}

	@RequestMapping(value="/mangas", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public List<Manga> liste(){
		ArrayList<Manga> data = new ArrayList<>();
		mangasDao.findAll().forEach(manga -> data.add(manga)); // ou foreach(data::add) ==> lambda
		return data; 
	}
	
	@RequestMapping(value="/pmangas", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public Page<Manga> listePaginate(@PageableDefault(page=0, size=3) Pageable p){
		return this.mangasDao.findAll(p);
	}
	
	@RequestMapping(value="/mangas/search/{search:.+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public List<Manga> searchManga(@PathVariable("search") String search){
		return mangasDao.findByTitreContaining(search);
	}
	
	@RequestMapping(value="/pmangas/search/{search:.+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public Page<Manga> searchMangaPaginate(@PathVariable("search") String search, @PageableDefault(page=0, size=3) Pageable p){
		return mangasDao.findByTitreContaining(search, p);
	}
	
	@RequestMapping(value="/mangas", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga save(@RequestBody Manga m ) {
		return this.mangasDao.save(m);
	}
	
	@RequestMapping(value="/mangas/{id:[0-9]+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga findById(@PathVariable("id") int id) {
		Manga m =  this.mangasDao.findOne(id);
		if (m == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		return m;
	}
	
	@RequestMapping(value="/mangas", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga update(@RequestBody Manga m ) {
		Manga ma =  this.mangasDao.findOne(m.getId());
		if (ma == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		return this.mangasDao.save(m);
	}
	
	@RequestMapping(value="/mangas/{id:[0-9]+}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga delete(@PathVariable("id") int id) {
		Manga m =  this.mangasDao.findOne(id);
		if (m == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		this.mangasDao.delete(m);
		return m;
	}
}