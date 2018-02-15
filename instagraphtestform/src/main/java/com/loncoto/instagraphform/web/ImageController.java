package com.loncoto.instagraphform.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.loncoto.instagraphform.metier.CompositeOwnerKey;
import com.loncoto.instagraphform.metier.Image;
import com.loncoto.instagraphform.metier.Tag;
import com.loncoto.instagraphform.metier.Utilisateur;
import com.loncoto.instagraphform.metier.projections.ImageWithTags;
import com.loncoto.instagraphform.repositories.ImageRepository;
import com.loncoto.instagraphform.repositories.TagRepository;
import com.loncoto.instagraphform.repositories.UtilisateurRepository;
import com.loncoto.instagraphform.util.FileStorageManager;

@Controller
@RequestMapping("/extended_api/images")
public class ImageController {
	
	private static Logger log = LogManager.getLogger(ImageController.class);

	private final ProjectionFactory projectionFactory;
	
	// injection de la ProjectionFactory via notre constructeur
	@Autowired
	public ImageController(ProjectionFactory projectionFactory) {
		this.projectionFactory = projectionFactory;
	}
	
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private TagRepository tagRepository;
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/findbytag",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Image> findByTags(
			@RequestParam("tagsId") Optional<List<Integer>> tagsId,
			@PageableDefault(page=0, size=12) Pageable page) {
		if (tagsId.isPresent())
			log.info("tagsId = " + tagsId.get().toString());
		else
			log.info("pas de tags en parametre");
		log.info("page demandée " + page);
		return imageRepository.findAll(page);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/findbytagfull",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<ImageWithTags> findByTagsFull(
			@RequestParam("tagsId") Optional<List<Integer>> tagsId,
			@RequestParam("negativeTagsId")  Optional<List<Integer>> negativeTagsId,
			@PageableDefault(page=0, size=12) Pageable page) {
		if (tagsId.isPresent() || negativeTagsId.isPresent()) {
			//log.info("tagsId = " + tagsId.get().toString());
			return imageRepository
					.searchWithTags(tagsId.orElse(Arrays.asList()), negativeTagsId.orElse(Arrays.asList()), page)
					.map(img -> projectionFactory.createProjection(ImageWithTags.class, img));
			/*return imageRepository
						.searchWithTags(tagsId.get(), null, page)
						.map(img -> projectionFactory.createProjection(ImageWithTags.class, img));*/ //OLFD
		}
		else {
			log.info("pas de tags en parametre");
			return imageRepository
						.findAll(page)
						.map(img -> projectionFactory.createProjection(ImageWithTags.class, img));
		}
	}
	
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/upload",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> upload(@RequestParam("idsTagString") Optional<List<Integer>> idsTags,  @RequestParam("file") MultipartFile file) {
		log.info("file name  :" + file.getOriginalFilename());
		log.info("content type  :" + file.getContentType());
		if (!file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) &&
				!file.getContentType().equals(MediaType.IMAGE_PNG_VALUE))
		{
			Map<String , Object> result = new HashMap<>();
			result.put("errorfield", "contentType");
			result.put("errormessage", "only jpeg or png supported");
			return new ResponseEntity<Object>(result, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		try {
			Image img = new Image(0, 
				 file.getOriginalFilename(),
				 "",
				 LocalDateTime.now(),
				 file.getOriginalFilename(),
				 file.getContentType(),
				 file.getSize(),							  // taille du fichier
				 0,
				 0,
				 DigestUtils.sha1Hex(file.getInputStream()),  // somme de controle du fichier
				 "",
				 "");
			if (idsTags.isPresent()) {
				List<Tag> tags= new ArrayList<>();
				this.tagRepository.findAll(idsTags.get()).forEach(tags::add);
				if (idsTags.isPresent()) img.getTags().addAll(tags);
			}
			
			imageRepository.saveImageFile(img, file.getInputStream());
			// le fichier est sauvegardé et img contient le storageId correspondant
			img = imageRepository.save(img);
			// ligne insérée dans la BDD
			
			return new ResponseEntity<Object>(img, HttpStatus.OK);
		} catch (IOException e) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
												"erreur a la sauvegarde");
		}
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/download/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> imageData(@PathVariable("id") long id) {
		Image img = imageRepository.findOne(id);
		if (img == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		// on recupere le fichier correspondant
		Optional<InputStream> fichier = imageRepository.getImageFile(img.getStorageId());
		if (!fichier.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "fichier image introuvable");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(img.getContentType()));
		headers.setContentLength(img.getFileSize());
		headers.setContentDispositionFormData("attachment", img.getFileName());
		ResponseEntity<InputStreamResource> re =
				new ResponseEntity<InputStreamResource>(new InputStreamResource(fichier.get()),
													headers,
													HttpStatus.ACCEPTED);
		return re;
	}
	
	@RequestMapping(value="/downloadthumb/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> imageDataThumb(@PathVariable("id") long id) {
		Image img = imageRepository.findOne(id);
		if (img == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		// on recupere le fichier correspondant
		Optional<InputStream> fichier = imageRepository.getImageFile(img.getThumbStorageId());
		if (!fichier.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "fichier image introuvable");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		//headers.setContentLength(fichier.get().length());
		headers.setContentDispositionFormData("attachment", img.getFileName());
		ResponseEntity<InputStreamResource> re =
				new ResponseEntity<InputStreamResource>(new InputStreamResource(fichier.get()),
													headers,
													HttpStatus.ACCEPTED);
		return re;
	}

	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/delete",
					method=RequestMethod.DELETE,
					produces=MediaType.APPLICATION_JSON_VALUE)	
	@ResponseBody
	public Map<String, Object> deleteImages(
			@RequestParam("imgsId") List<Long> imagesId) {
		Map<String, Object> result = new HashMap<>();
		Iterable<Image> images = imageRepository.findAll(imagesId);
		
		imageRepository.delete(images);
		
		int nbImageToDelete = 0;
		int nbFilesDeleted = 0;
		// efface les fichiers images correspondants
		for (Image img : images) {
			nbImageToDelete++;
			if (imageRepository.deleteImageFile(img))
				nbFilesDeleted++;
		}
		// retrouner les informations sur ce qui a été fait
		result.put("nbImagesDeleted", nbImageToDelete);
		result.put("nbFilesDeleted", nbFilesDeleted);
		return result;
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/findone/{id:[0-9]+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Object> findImageById(@PathVariable("id") long id){
		Image img = imageRepository.findOne(id);
		if (img == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		// return image avc ags associr donc utilisation de la projection
		return new ResponseEntity<Object>(projectionFactory.createProjection(ImageWithTags.class, img), HttpStatus.OK);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/updateone/{id:[0-9]+}", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Object> updateImg(@PathVariable("id") long id,
			@RequestParam("name") String name,@RequestParam("description") String description,
			@RequestParam("fileName") String fileName){
		Image img = imageRepository.findOne(id);
		if (img == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		img.setName(name);
		img.setDescription(description);
		img.setFileName(fileName);
		img = this.imageRepository.save(img);
		
		// return image avc ags associr donc utilisation de la projection
		return new ResponseEntity<Object>(projectionFactory.createProjection(ImageWithTags.class, img), HttpStatus.OK);
	}
	
}
