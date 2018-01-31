package com.otmanel.thirdJunitBoot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.hamcrest.Matchers.equalTo; 
import static org.hamcrest.Matchers.closeTo; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
	
	@Test
	public void testSaveProduit() throws Exception {
		Produit p = new Produit(3, "steak de bison", 50.99, 0.750, 4, "boucherie");
		String produitJson = "{\n" + 
				"   \"id\": 3,\n" + 
				"   \"nom\": \"steak de bison\",\n" + 
				"   \"prix\": 50.99,\n" + 
				"   \"poid\": 0.750,\n" + 
				"   \"stock\": 4,\n" + 
				"   \"categorie\": \"boucherie\"\n" + 
				"}";
		
		when(this.produitDao.save(any(Produit.class))).thenReturn(p);
		
		// close to unqiuement pou rle s virgule flottante etc
		mockMvc.perform(post("/extended_value/save")
				.content(produitJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", equalTo(3)))
				.andExpect(jsonPath("$.nom", equalTo("steak de bison")))
				.andExpect(jsonPath("$.categorie", equalTo("boucherie"))) // etc pou les double attention provbelem arrondi
				.andExpect(jsonPath("$.poid", closeTo( 0.750, 0.00001)))
				.andExpect(jsonPath("$.prix", closeTo(50.99, 0.00001)))
				.andExpect(jsonPath("$.stock", equalTo(4)))
		;
		verify(produitDao, atLeastOnce()).save(
				argThat(new ProduitEqualityMatcher(p))
				); // verifie que ssave de produit dao est apeler au moins une fois
		
	}
	
	@Test
	public void testSaveProduitPrixKo() throws Exception {
		Produit p = new Produit(3, "steak de bison", 50.99, 0.750, 4, "boucherie");
		Produit p2 = new Produit(3, "steak de bison", 0.0, 0.750, 4, "boucherie");
		String produitJson = "{\n" + 
				"   \"id\": 3,\n" + 
				"   \"nom\": \"steak de bison\",\n" + 
				"   \"prix\": -15.5,\n" + 
				"   \"poid\": 0.750,\n" + 
				"   \"stock\": 4,\n" + 
				"   \"categorie\": \"boucherie\"\n" + 
				"}";
		
		when(this.produitDao.save(any(Produit.class))).thenReturn(p2);
		
		// close to unqiuement pou rle s virgule flottante etc
		mockMvc.perform(post("/extended_value/save")
				.content(produitJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", equalTo(3)))
				.andExpect(jsonPath("$.nom", equalTo("steak de bison")))
				.andExpect(jsonPath("$.categorie", equalTo("boucherie"))) // etc pou les double attention provbelem arrondi
				.andExpect(jsonPath("$.poid", closeTo( 0.750, 0.00001)))
				.andExpect(jsonPath("$.prix", closeTo(0.0, 0.00001)))
				.andExpect(jsonPath("$.stock", equalTo(4)))
		;
		// save appeler au moinsu une fois et avc le bon produit avc me prix remis a 0
		verify(produitDao, atLeastOnce()).save(
				argThat(new ProduitEqualityMatcher(p2)) // ancien equals verifier si c t la mm instance et maintenant 
				);
		
	}
	
	@Test
	public void testSaveProduitPoidKo() throws Exception {
		Produit p = new Produit(3, "steak de bison", 50.99, -0.5, 4, "boucherie");

		String produitJson = "{\n" + 
				"   \"id\": 3,\n" + 
				"   \"nom\": \"steak de bison\",\n" + 
				"   \"prix\": 15.5,\n" + 
				"   \"poid\": -0.5,\n" + 
				"   \"stock\": 4,\n" + 
				"   \"categorie\": \"boucherie\"\n" + 
				"}";
		
		when(this.produitDao.save(any(Produit.class))).thenReturn(p);
		
		// close to unqiuement pou rle s virgule flottante etc
		mockMvc.perform(post("/extended_value/save")
				.content(produitJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.fieldError", equalTo("poids")))
				.andExpect(jsonPath("$.errorMessage", equalTo("le poid ne peut etre negatif")))
				.andExpect(jsonPath("$.entite.id", equalTo(3)))
				.andExpect(jsonPath("$.entite.nom", equalTo("steak de bison")))
		;
		// save pas  appeler a cause du poid invalide$
		verify(produitDao, never()).save(
				any(Produit.class)
				);
		
	}
	
	
	/**
	 * 
	 * @author Stagiaire
	 * 
	 * 
	 * cette classe va permettre a maockito de comaprer si 1 produit sont les mm a partir de leurs contenu et nn de linstance
	 * ainsi le equals normal ne marche pas => 
	 * 
	 * 
	 * pour les valeurs double on verifie si la diff entrre les deux est suffisamement petite pour etre considere comme
	 * egale (car les double sont des valeurs arrondi)
	 * on utilise abs pour ne pas sovcuper du signe de la difference
	 * 
	 * cette classe matcher comparera le produti p quon lui indique a celui passer en param ( donc propriété par proprieté)
	 *
	 */
	public static class ProduitEqualityMatcher extends ArgumentMatcher<Produit>{

		private Produit p;
		
		public ProduitEqualityMatcher(Produit p) {
			this.p = p; 
		}
		
		@Override
		public boolean matches(Object argument) {
			Produit other = (Produit)argument;
			return p.getId() == other.getId()
					&&  p.getNom().equals(other.getNom())
					&& p.getCategorie().equals(other.getCategorie())
					&& p.getStock() == other.getStock()
					&& Math.abs(p.getPrix() - other.getPrix()) < 0.00001
					&& Math.abs(p.getPoid() - other.getPoid()) < 0.00001;
		}
		
	}

}
