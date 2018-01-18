package com.lonconto.mangasManiaBoot.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.lonconto.mangasManiaBoot.metier.Image;

@RepositoryRestResource
public interface ImageRepository extends PagingAndSortingRepository<Image, Integer>, ImageRepositoryCustom {

}
