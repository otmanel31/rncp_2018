package com.loncoto.instagraphform.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.loncoto.instagraphform.metier.Content;
import com.loncoto.instagraphform.metier.Tag;
import com.loncoto.instagraphform.repositories.ContentRepository;
import com.loncoto.instagraphform.repositories.ImageRepository;
import com.loncoto.instagraphform.repositories.TagRepository;

@Controller
@RequestMapping("/extended_api/tag")
public class TagController {

	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/liste",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Tag> findAll(@PageableDefault(page=0, size=15) Pageable page,
							 @RequestParam("search") Optional<String> search) {
		if(search.isPresent()) return tagRepository.findByLibelleContaining(search.get(), page);
		else
			return tagRepository.findAll(page);
	}
	
	// ajout tag a des image ou retirer tags a un content
	// on prevoit large donc liste au cas ou etiquetage de plusieur img en mm tps depuis liste component front end
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/addTags/{contentIds:[0-9,]+}/{tagIds:[0-9,]+}",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Tag> addTags(@PathVariable("contentIds") List<Integer> contentIds,@PathVariable("tagIds")  List<Integer> tagIds){
		List<Tag> tags = new ArrayList<>();
		tagRepository.findAll(tagIds).forEach(tags::add);
		
		for(long contentId: contentIds) {
			Content c = contentRepository.findOneIncludingTags(contentId);
			if (c != null ) {
				c.getTags().addAll(tags);
				contentRepository.save(c);
			}
		}
		return tags;
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/removeTags/{contentIds:[0-9,]+}/{tagIds:[0-9,]+}",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Tag> removeTags(@PathVariable("contentIds") List<Integer> contentIds,@PathVariable("tagIds")  List<Integer> tagIds){
		List<Tag> tags = new ArrayList<>();
		tagRepository.findAll(tagIds).forEach(tags::add);
		
		for(long contentId: contentIds) {
			Content c = contentRepository.findOneIncludingTags(contentId);
			if (c != null ) {
				c.getTags().removeAll(tags);
				contentRepository.save(c);
			}
		}
		return tags;
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/findRelated/{id:[0-9]+}",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Tag> findRelatedTo( @PathVariable("id") long id){
		return this.tagRepository.findByContents_id(id);
	}
	
}
