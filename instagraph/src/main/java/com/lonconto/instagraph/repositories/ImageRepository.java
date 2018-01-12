package com.lonconto.instagraph.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lonconto.instagraph.metier.Image;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long>, ImageRepositoryCustom {
	
}
