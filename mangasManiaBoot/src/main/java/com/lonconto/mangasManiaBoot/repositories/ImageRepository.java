package com.lonconto.mangasManiaBoot.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lonconto.mangasManiaBoot.metier.Image;

public interface ImageRepository extends PagingAndSortingRepository<Image, Integer>, ImageRepositoryCustom {

}
