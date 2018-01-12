package com.lonconto.instagraph.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lonconto.instagraph.metier.Content;

public interface ContentRepository extends PagingAndSortingRepository<Content, Long> {

}
