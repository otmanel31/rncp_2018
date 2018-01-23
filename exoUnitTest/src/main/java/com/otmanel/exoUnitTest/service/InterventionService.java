package com.otmanel.exoUnitTest.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import com.otmanel.exoUnitTest.metier.Intervention;
import com.otmanel.exoUnitTest.repositories.InterventionDao;

@Service
public class InterventionService {
	
	@Autowired
	private InterventionDao interventionDao;
	
	// getter setter pour dao pour injecter le mock dao 
	public InterventionDao getInterventionDao() {
		return interventionDao;
	}
	public void setInterventionDao(InterventionDao interventionDao) {
		this.interventionDao = interventionDao;
	}

	public Page<Intervention> findAll(@PageableDefault(page=0, size=3) Pageable p){
		return this.interventionDao.findAll(p);
	}
	
	public Intervention findOne(int id) {
		Intervention i = this.interventionDao.findOne(id);
		if (i == null ) throw new InterventionNotFoundException("intervention fournis introuvable");
		return i;
	}
	
	public Intervention save(Intervention i) {
		return this.interventionDao.save(i);
	}
	
	public Intervention  delete(int id ) {
		Intervention i = this.interventionDao.findOne(id);
		if (i == null) throw new InterventionNotFoundException("intervention fournis introuvable");
		this.interventionDao.delete(i);
		return i;
	}
	
	public static class InterventionNotFoundException extends RuntimeException{

		public InterventionNotFoundException(String message) {
			super(message);
		}
		
	}
} 
