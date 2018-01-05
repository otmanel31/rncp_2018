package com.firstspringdataproject.funspy.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.firstspringdataproject.funspy.metier.Article;

public interface IArticleDao extends CrudRepository<Article, Integer> {
	List<Article> findByNomContaining(String nom);
	List<Article> findByDateSortieBefore(LocalDateTime dateSortie);
	@Query("select a from Article as a where a.prix < :promotion")
	List<Article> trouverPromotion(double promotion);
}
