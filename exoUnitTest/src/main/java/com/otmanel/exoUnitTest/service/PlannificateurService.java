package com.otmanel.exoUnitTest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import com.otmanel.exoUnitTest.metier.Intervention;
import com.otmanel.exoUnitTest.repositories.IntervenantDao;
import com.otmanel.exoUnitTest.repositories.InterventionDao;

@Service
public class PlannificateurService {
	
	@Autowired
	private InterventionDao interventionDao;

	@Autowired
	private IntervenantDao intervenantDao;

	public void setInterventionDao(InterventionDao interventionDao) {
		this.interventionDao = interventionDao;
	}
	
	public void setIntervenantDao(IntervenantDao intervenantDao) {
		this.intervenantDao = intervenantDao;
	}
	
	public Page<Intervention> getAll( Pageable p){
		return this.interventionDao.findAll(p);
	}
	
	public Intervention createNewIntervention(Intervention i) {
		if (i.getDateFin().isBefore(i.getDateDebut())) throw new InterventionDatetimeError("erreur sur la date ");
		for (Intervention i1 : i.getIntervenant().getInterventions()){
			if (i.getDateDebut().isBefore(i1.getDateFin()) || i.getDateFin().isAfter(i1.getDateDebut()))
				throw new IntervenantNotAvailableError("l'intervenant est indisponible sur ce crenaux");
			
		}
		List<Intervention> inters = this.interventionDao.findByDateDebutAfterAndDateDebutBeforeAndIntervenantId(i.getDateDebut().minusDays(1), i.getDateDebut().plusDays(1), i.getIntervenant().getId());
		
		for (Intervention inter : inters) {
			if (i.getDateFin().isBefore(inter.getDateDebut()) || i.getDateDebut().isAfter(inter.getDateFin()))
				continue;
			else
				throw new IntervenantNotAvailableError("l'intervenant est indisponible sur ce crenaux");
		}
		
		return this.interventionDao.save(i);
	}
	
	public Intervention updateIntervention(Intervention i) {
		if (i.getDateFin().isBefore(i.getDateDebut())) throw new InterventionDatetimeError("La date de fin ne peut etre inferieur a la date de debut");
		return this.interventionDao.save(i);
	}
	
	// exception custom
	public static class InterventionDatetimeError extends RuntimeException{
		public InterventionDatetimeError(String message) {
			super(message);
		}
	}
	
	public static class IntervenantNotAvailableError extends RuntimeException{
		public IntervenantNotAvailableError(String message) {
			super(message);
		}
	}
	
}
