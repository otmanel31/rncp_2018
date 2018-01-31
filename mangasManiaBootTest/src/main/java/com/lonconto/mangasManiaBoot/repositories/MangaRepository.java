package com.lonconto.mangasManiaBoot.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.lonconto.mangasManiaBoot.metier.Manga;

@RepositoryRestResource
public interface MangaRepository extends PagingAndSortingRepository<Manga, Integer>{
	List<Manga> findByTitreContaining(String titre);
	Page<Manga> findByTitreContaining(String titre, Pageable page);
	Page<Manga> findByTitreContainingAndRatingGreaterThanEqual(String titre, int rating, Pageable p);
	Page<Manga> findByRatingGreaterThanEqual(int rating, Pageable p);
}
