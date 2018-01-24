package com.otmanel.thirdJunitBoot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.otmanel.thirdJunitBoot.metier.Produit;
import com.otmanel.thirdJunitBoot.repositories.ProduitRepository;
import com.otmanel.thirdJunitBoot.web.ProduitController;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// test sur du contenu json plus niveau data .... 
//import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize; 

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringRunner.class)
//@SpringBootTest
@WebMvcTest(controllers=ProduitController.class)
@EnableSpringDataWebSupport // sinon e repo srping data rest + pageabl ne fonctionne pas ... activiation donc
public class ThirdJunitBootApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	// injectera automatiquement un mock(via mockito) du repository
	// c un ajout qui foncitonnera automatique en spring boot
	@MockBean
	private ProduitRepository produitDao;
	
	// petite methode pour eviter d'avoir a chaque fois a retaper les donnees
	private Page<Produit> getSampleProduitPage1(){
		return new PageImpl<>(new ArrayList<>(
				Arrays.asList(
					new Produit(1, "test1", 10.0, 1.0, 5, "cat1"),
					new Produit(1, "test2", 10.0, 1.0, 5, "cat2"),
					new Produit(1, "test2", 10.0, 1.0, 5, "cat2")
				)
			), new PageRequest(0, 5), 3);
	}
	
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testList() throws Exception {
		when(produitDao.findAll(any(Pageable.class)))
			.thenReturn(getSampleProduitPage1());
		
		// type request builder contient des methodes get put etc .....
		//mockMvc.perform(requestBuilder)
		mockMvc.perform(get("/extended_value/"))
			.andExpect(status().isOk()) // import egalement des resultmatchers et on lui dit le resultat attentdu
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("$.content", hasSize(3)))
		;
		
		verify(produitDao, atLeastOnce()).findAll(any(Pageable.class));
	}

}
