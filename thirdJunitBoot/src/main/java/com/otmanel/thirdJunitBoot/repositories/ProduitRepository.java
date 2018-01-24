package com.otmanel.thirdJunitBoot.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.thirdJunitBoot.metier.Produit;

public interface ProduitRepository extends PagingAndSortingRepository<Produit, Integer> {
	Page<Produit> findByCategorie(Pageable p, String categorie);
}
