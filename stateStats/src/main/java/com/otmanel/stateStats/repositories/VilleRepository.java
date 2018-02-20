package com.otmanel.stateStats.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.otmanel.stateStats.metier.Ville;

@RepositoryRestResource(path="ville")
public interface VilleRepository extends MongoRepository<Ville, String> {
	Page<Ville> findByPopGreaterThan(int popMin, Pageable p);
}
