package com.lonconto.instagraph.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lonconto.instagraph.metier.Tag;
import com.lonconto.instagraph.repositories.ImageRepository;
import com.lonconto.instagraph.repositories.TagRepository;

@Controller
@RequestMapping("/extended_api/tag")
public class TagController {
	
	@Autowired
	private ImageRepository imgDao;
	
	@Autowired
	private TagRepository tagDao;
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/pliste", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Tag> findAll(
			@PageableDefault(page=0, size=8) Pageable page,
			@RequestParam("search") Optional<String> search){
		return this.tagDao.findAll(page);
	}
	
	
}
