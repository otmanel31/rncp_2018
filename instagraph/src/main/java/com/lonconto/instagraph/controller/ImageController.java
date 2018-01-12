package com.lonconto.instagraph.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.weaver.AjAttribute.MethodDeclarationLineNumberAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

import com.lonconto.instagraph.metier.Image;
import com.lonconto.instagraph.metier.projections.ImageWithTags;
import com.lonconto.instagraph.repositories.ImageRepository;
import com.lonconto.instagraph.util.FileStorageManager;


@Controller
@RequestMapping(value="/extended_api/images")
public class ImageController {
	// controleur en plus de ce qui existe deja et paramtrer dans le fichier properties
	private static Logger log = LogManager.getLogger(ImageController.class); // le .class en param pour préfix sur les logs ... 
	
	private final ProjectionFactory projectionFactory;
	
	@Autowired
	public ImageController(ProjectionFactory projectionFactory) {
		this.projectionFactory = projectionFactory;
	}
	
	@Autowired
	private ImageRepository imageRepo;
	
	/*@Autowired
	private FileStorageManager fsm;*/
	
	@RequestMapping(value="/download/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<FileSystemResource> imageData(@PathVariable("id") long id){
		Image img = imageRepo.findOne(id);
		if (img == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		Optional<File> file = imageRepo.getImageFile(img.getStorageId());
		if (!file.isPresent()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image en bdd OK mais introuvable sur serveur");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(img.getContentType()));
		headers.setContentLength(img.getFileSize());
		headers.setContentDispositionFormData("attachment", img.getFileName());
		
		ResponseEntity<FileSystemResource> re = new ResponseEntity<FileSystemResource>(new FileSystemResource(file.get()),
					headers,
					HttpStatus.ACCEPTED);
		return re;

	}
	
	@RequestMapping(value="/downloadThumb/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<FileSystemResource> imageDataThumb(@PathVariable("id") long id){
		Image img = imageRepo.findOne(id);
		if (img == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		Optional<File> file = imageRepo.getImageFile(img.getThumbStorageId());
		if (!file.isPresent()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image en bdd OK mais introuvable sur serveur");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentLength(file.get().length());
		headers.setContentDispositionFormData("attachment", img.getFileName()+"_thumb");
		
		ResponseEntity<FileSystemResource> re = new ResponseEntity<FileSystemResource>(new FileSystemResource(file.get()),
					headers,
					HttpStatus.ACCEPTED);
		return re;

	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/upload", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Image upload(@RequestParam("file") MultipartFile file) {
		// EN DEUX ETAPES/1/ copie/2/save en bdd
		log.info("File name: " + file.getOriginalFilename());
		log.info("File name: " + file.getContentType());
		
		try {
			Image img = new Image(0, file.getOriginalFilename(), "", LocalDateTime.now(), file.getOriginalFilename(),
				file.getContentType(), file.getSize(), 0, 0, 
				DigestUtils.sha1Hex(file.getInputStream())/* somme de controle du fuichier*/, "", "");
			// somme de controle pour verifier plutart si le fichier na pas ete corrompu
		
			imageRepo.saveImageFile(img, file.getInputStream());
			// le fichier est save et img contient kle storageid correpondantt
			imageRepo.save(img); // ligne inseré ds la base
			return img;
		}catch(IOException E) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "erreur lors de la sauvegarde");
		}

		
		/*try { ANCIENN POUR LES  PREMIER TEST
			
			String storageId = fsm.saveNewFile("images", file.getInputStream());
			return new Image(1, "mon image", "some dexc", LocalDateTime.now(), file.getOriginalFilename(),
					file.getContentType(), 0, 0, 0, "", "");
		} catch (IOException e) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "error during saving");
		}*/
		
		// file.getInputStream() // recupere le fichier
		
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/plistesByTags", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Image> findByTags(
			@RequestParam("tagsId") Optional<List<Integer>> tagsId, 
			@PageableDefault(page=0, size=12) Pageable page){
		
		if (tagsId.isPresent()) log.info("tags id = " + tagsId.get().toString());
		else log.info("no tags id in parameters");
		
		return this.imageRepo.findAll(page);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/plistesByTagsFull", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<ImageWithTags> findByTagsFull(
			@RequestParam("tagsId") Optional<List<Integer>> tagsId, 
			@PageableDefault(page=0, size=12) Pageable page){
		
		if (tagsId.isPresent()) log.info("tags id = " + tagsId.get().toString());
		else log.info("no tags id in parameters");
		
		return this.imageRepo.findAll(page).map(img-> projectionFactory.createProjection(ImageWithTags.class, img));
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="delete", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> deleteImgs(@RequestParam("imgsId") List<Long> imgsId) {
		Map<String, Object> result = new HashMap<>();
		
		Iterable<Image> imgs = imageRepo.findAll(imgsId);

		// effece img en bdd
		imageRepo.delete(imgs);
		int nbToDelete = 0;
		int nbFilesDeleted = 0;
		// effece les fichier img correspondant in fileSysteme
		for (Image img : imgs) {
			nbToDelete++;
			if (imageRepo.deleteImg(img)) {
				nbFilesDeleted++;
			}
		}
		/*imgs.forEach(img->{
			nbToDelete++;
			if (imageRepo.deleteImg(img)) {
				nbFilesDeleted++;
			}
			
		});*/
		// reoturne info sur les actions effectuées
		result.put("nbToDelete", nbToDelete);
		result.put("nbToDelete", nbFilesDeleted);
		return result;
	}
}
