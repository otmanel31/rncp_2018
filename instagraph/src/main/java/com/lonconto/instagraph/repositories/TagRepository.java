package com.lonconto.instagraph.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.lonconto.instagraph.metier.Tag;

@RepositoryRestResource
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

}
