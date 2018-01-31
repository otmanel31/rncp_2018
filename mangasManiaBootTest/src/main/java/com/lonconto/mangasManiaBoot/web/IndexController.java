package com.lonconto.mangasManiaBoot.web;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lonconto.mangasManiaBoot.metier.Image;
import com.lonconto.mangasManiaBoot.metier.Manga;
import com.lonconto.mangasManiaBoot.repositories.ImageRepository;
import com.lonconto.mangasManiaBoot.repositories.MangaRepository;

@Controller
@RequestMapping(value="/extended_api")
public class IndexController {
	
	private static Logger log = LogManager.getLogger(IndexController.class);
	
	@Autowired
	private MangaRepository mangasDao;
	
	@Autowired
	private ImageRepository imageDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectToIndex() {
		return "bonjour";
	}

	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangas", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public List<Manga> liste(){
		ArrayList<Manga> data = new ArrayList<>();
		mangasDao.findAll().forEach(manga -> data.add(manga)); // ou foreach(data::add) ==> lambda
		return data; 
	}
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/pmangas", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public Page<Manga> listePaginate(@PageableDefault(page=0, size=3) Pageable p
			, @RequestParam("ratingMin") Optional<Integer> ratingMin){
		if (ratingMin.isPresent()) return this.mangasDao.findByRatingGreaterThanEqual(ratingMin.get(), p);
		
		return this.mangasDao.findAll(p);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangas/search/{search:.+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public List<Manga> searchManga(@PathVariable("search") String search){
		return mangasDao.findByTitreContaining(search);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/pmangas/search/{search:.+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody // veux dire que cette reponse ne renvoie pas une jsp mais des datas ds le corps
	public Page<Manga> searchMangaPaginate(@PathVariable("search") String search, @PageableDefault(page=0, size=3) Pageable p
			, @RequestParam("ratingMin") Optional<Integer> ratingMin){
		if (ratingMin.isPresent()) return this.mangasDao.findByTitreContainingAndRatingGreaterThanEqual(search, ratingMin.get(), p);
		
		return mangasDao.findByTitreContaining(search, p);
	}
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/download/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<FileSystemResource> imageData(@PathVariable("id") int id){
		Image img = imageDao.findOne(id);
		if (img == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		Optional<File> file = imageDao.getImageFile(img.getStorageId());
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
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/downloadThumb/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<FileSystemResource> imageDataThumb(@PathVariable("id") int id){
		Image img = imageDao.findOne(id);
		if (img == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		Optional<File> file = imageDao.getImageFile(img.getThumbStorageId());
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
	@RequestMapping(value="/image/upload/{idMangas:[0-9]+}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga upload(@RequestParam("file") MultipartFile file, @PathVariable("idMangas") int idMangas){
		// upload de limage ds le systeme de fichier 
		log.info("File name: " + file.getOriginalFilename());
		log.info("File name: " + file.getContentType());
		
		try {
			Image img = new Image(0, file.getOriginalFilename(), file.getSize(), file.getContentType(), "", "");
			imageDao.saveImageFile(img, file.getInputStream());
			// on save ensuite en bdd l image
			imageDao.save(img);
			// puis on lie limage a mongas recup ds lurl
			Manga m = mangasDao.findOne(idMangas);
			m.setImg(img);
			return mangasDao.save(m);
			// return m;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "erreur lors de la sauvegarde");
		}
	}
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangasOld", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga saveOld(@RequestBody Manga m) {
		if (m.getDateParution() == null || m.getDateParution().isAfter(LocalDate.now().plusYears(1)))
			throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
		// -	save en deux etapes 
		// 1 ) save du fichier ds le FS
		/*log.info("file name: " + file.getOriginalFilename());
		log.info("file content type: " + file.getContentType());
		
		try {
			Image img = new Image(0, file.getOriginalFilename(), file.getSize(), file.getContentType(), "", "");
			mangasDao.saveImageFile(img, file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// 2 ) save en bdd
		return this.mangasDao.save(m);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangas", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> save(@RequestBody Manga m) {
		if (m.getDateParution() == null || m.getDateParution().isAfter(LocalDate.now().plusYears(1))) {
			//throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
			Map<String, Object> result = new HashMap<>();
			result.put("fieldError", "dateParution");
			result.put("errorMessage", "la date de parution ne peut etre a null ...");
			result.put("entite", m);
			return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		// -	save en deux etapes 
		// 1 ) save du fichier ds le FS
		/*log.info("file name: " + file.getOriginalFilename());
		log.info("file content type: " + file.getContentType());
		
		try {
			Image img = new Image(0, file.getOriginalFilename(), file.getSize(), file.getContentType(), "", "");
			mangasDao.saveImageFile(img, file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// 2 ) save en bdd
		Manga ma = this.mangasDao.save(m);
		return new ResponseEntity<>(ma, HttpStatus.OK);
	}
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangas/{id:[0-9]+}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga findById(@PathVariable("id") int id) {
		Manga m =  this.mangasDao.findOne(id);
		if (m == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		return m;
	}
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangas", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga update(@RequestBody Manga m ) {
		Manga ma =  this.mangasDao.findOne(m.getId());
		if (ma == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		return this.mangasDao.save(m);
	}
	
	// new for test
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangasNew", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> updateNew(@RequestBody Manga m ) {
		if (m.getDateParution() == null) {
			//throw new MangaDateException("erreur date");
			Map<String, Object> result = new HashMap<>();
			result.put("fieldError", "dateParution");
			result.put("errorMessage", "la date de parution ne peut etre a null ...");
			result.put("entite", m);
			return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Manga ma =  this.mangasDao.findOne(m.getId());
		if (ma == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		
		
		ma =  this.mangasDao.save(m);
		return new ResponseEntity<>(ma, HttpStatus.OK);
	}
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/mangas/{id:[0-9]+}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Manga delete(@PathVariable("id") int id) {
		Manga m =  this.mangasDao.findOne(id);
		if (m == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Manga incognito pedro");
		this.mangasDao.delete(m);
		return m;
	}
	
	@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
	public static class MangaDateException extends RuntimeException{
		public MangaDateException(String message) {
			super(message);
		}
		
	}
}