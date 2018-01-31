package com.lonconto.mangasManiaBoot;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.exparity.hamcrest.date.IsBefore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.lonconto.mangasManiaBoot.metier.Manga;
import com.lonconto.mangasManiaBoot.repositories.ImageRepository;
import com.lonconto.mangasManiaBoot.repositories.MangaRepository;
import com.lonconto.mangasManiaBoot.web.IndexController;
import com.lonconto.mangasManiaBoot.web.IndexController.MangaDateException;

import javassist.bytecode.Mnemonic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize; 
import static org.hamcrest.Matchers.equalTo; 
import static org.hamcrest.Matchers.closeTo; 
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.exparity.hamcrest.date.IsBefore.before;;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers=IndexController.class)
@EnableSpringDataWebSupport // sinon e repo srping data rest + pageabl ne fonctionne pas ... activiation donc
public class MangaManiaTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	// injectera automatiquement un mock(via mockito) du repository
	// c un ajout qui foncitonnera automatique en spring boot
	@MockBean
	private MangaRepository mangaDao;// injectera automatiquement un mock(via mockito) du repository
	// c un ajout qui foncitonnera automatique en spring boot
	@MockBean
	private ImageRepository imageDao;
	
	private Page<Manga> getSampleManga(){
		return new PageImpl<>(new ArrayList<>(
					Arrays.asList(
						new Manga(1, "test1", "testAuetru1", LocalDate.now(), "", 2),
						new Manga(2, "test2", "testAuetru2", LocalDate.now(), "", 3)
					)
				),
				new PageRequest(0, 5), 2)
		;
	}
	
	@Test
	public void simpleTest(){
		int expected = 4;
		int actual = 4;
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetAll() throws Exception{
		Mockito.when(this.mangaDao.findAll(Mockito.any(Pageable.class)))
			.thenReturn(getSampleManga())
		;
		
		mockMvc.perform(get("/extended_api/pmangas"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.content", hasSize(2)));
	}
	
	@Test
	public void testSaveManga() throws Exception {
		Manga m = new Manga(1, "test", "testauteur", LocalDate.now(), "testGnere", 3);
		String mangaJson = "{"
				+ "\"id\" : 1,"
				+ "\"titre\": \"test\","
				+ "\"auteur\":\"testauteur\","
				+ "\"dateParution\":\"2018-01-25\","
				+ "\"genre\":\"testGnere\","
				+ "\"rating\":3"
				+ "}";
		
		Mockito.when(this.mangaDao.save(Mockito.any(Manga.class))).thenReturn(m);
		
		mockMvc.perform(post("/extended_api/mangasOld")
				.content(mangaJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", equalTo(1)))
			;
		
		Mockito.verify(mangaDao, Mockito.atLeastOnce()).save(Mockito.any(Manga.class));
	}
	
	@Test
	public void testSaveMangaDateKo() throws Exception {
		Manga m = new Manga(1, "test", "testauteur", null, "testGnere", 3);
		String mangaJson = "{"
				+ "\"id\" : 1,"
				+ "\"titre\": \"test\","
				+ "\"auteur\":\"testauteur\","
				+ "\"dateParution\": null,"
				+ "\"genre\":\"testGnere\","
				+ "\"rating\":3"
				+ "}";
		
		Mockito.when(this.mangaDao.save(Mockito.any(Manga.class))).thenReturn(m);
		
		mockMvc.perform(post("/extended_api/mangas")
				.content(mangaJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.fieldError", equalTo("dateParution")))
				.andExpect(jsonPath("$.errorMessage", equalTo("la date de parution ne peut etre a null ...")))
				.andExpect(jsonPath("$.entite.id", equalTo(1))) 
				
			;
		
		Mockito.verify(mangaDao, Mockito.never()).save(Mockito.any(Manga.class));
	}
	
	@Test
	public void testDelete() throws Exception {
		Manga m = new Manga(1, "test", "testauteur", null, "testGnere", 3); 
		
		Mockito.when(mangaDao.findOne(Mockito.anyInt())).thenReturn(m);
		
		//Mockito.when(mangaDao.delete(Mockito.anyInt())).thenReturn(m);
		
		mockMvc.perform(delete("/extended_api/mangas/"+m.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				;
		
		Mockito.verify(mangaDao, Mockito.atLeastOnce()).delete(Mockito.any(Manga.class));
	}
	
	@Test
	public void testUpdate() throws Exception {
		Manga m = new Manga(1, "test", "testauteur", LocalDate.now(), "testGnere", 3);
		String mangaJson = "{"
				+ "\"id\" : 1,"
				+ "\"titre\": \"test\","
				+ "\"auteur\":\"testauteur\","
				+ "\"dateParution\": \"2018-01-25\","
				+ "\"genre\":\"testGnere\","
				+ "\"rating\":3"
				+ "}";
		
		Mockito.when(mangaDao.findOne(Mockito.anyInt())).thenReturn(m);
		
		Mockito.when(mangaDao.save(Mockito.any(Manga.class))).thenReturn(m);
		
		mockMvc.perform(put("/extended_api/mangas/")
				.content(mangaJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			;
		
		Mockito.verify(mangaDao, Mockito.atLeastOnce()).save(Mockito.any(Manga.class));
	}
	
	@Test
	public void testUpdateWithDateNull() throws Exception {
		Manga m = new Manga(1, "test", "testauteur", null, "testGnere", 3);
		String mangaJson = "{"
				+ "\"id\" : 1,"
				+ "\"titre\": \"test\","
				+ "\"auteur\":\"testauteur\","
				+ "\"dateParution\": null,"
				+ "\"genre\":\"testGnere\","
				+ "\"rating\":3"
				+ "}";
		
		Mockito.when(mangaDao.findOne(Mockito.anyInt())).thenReturn(m);
		
		//Mockito.when(mangaDao.save(Mockito.any(Manga.class))).thenReturn(m);
		
		mockMvc.perform(put("/extended_api/mangasNew/")
				.content(mangaJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			;
		
		//Mockito.verify(mangaDao, Mockito.atLeastOnce()).save(Mockito.any(Manga.class));
	}
	
	
}
