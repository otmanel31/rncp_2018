package com.otmanel.exoUnitTest.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.exoUnitTest.metier.Intervenant;

public interface IntervenantDao extends PagingAndSortingRepository<Intervenant, Integer> {
	
}
