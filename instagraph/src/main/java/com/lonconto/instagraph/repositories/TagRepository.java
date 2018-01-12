package com.lonconto.instagraph.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lonconto.instagraph.metier.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag, Integer> {

}
