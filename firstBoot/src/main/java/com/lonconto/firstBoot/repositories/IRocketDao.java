package com.lonconto.firstBoot.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.lonconto.firstBoot.metier.Rocket;

public interface IRocketDao extends PagingAndSortingRepository<Rocket, Integer> {

}
