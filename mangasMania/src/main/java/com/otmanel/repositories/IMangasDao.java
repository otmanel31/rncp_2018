package com.otmanel.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.mangasMania.metier.Manga;

public interface IMangasDao extends PagingAndSortingRepository<Manga, Integer>{
	List<Manga> findByTitreContaining(String titre);
}
