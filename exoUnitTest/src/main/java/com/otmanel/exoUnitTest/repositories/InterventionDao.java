package com.otmanel.exoUnitTest.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.exoUnitTest.metier.Intervention;

public interface InterventionDao extends PagingAndSortingRepository<Intervention, Integer> {
	// filtre par date debut  apres date debut param pour id test le equals par defaut
	List<Intervention> findByDateDebutAfterAndDateDebutBeforeAndIntervenantId(LocalDateTime start, LocalDateTime end, int id);
	//Intervention findByDateDebutEqualsAndintervenantNomEquals(LocalDateTime dateDebut, String nom);
}
