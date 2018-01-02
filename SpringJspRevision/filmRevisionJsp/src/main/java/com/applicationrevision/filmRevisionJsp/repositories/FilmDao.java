package com.applicationrevision.filmRevisionJsp.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.applicationrevision.filmRevisionJsp.metier.Film;

@Service
public class FilmDao implements IFilmDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public List<Film> findAll() {
		return this.em.createQuery("from Film", Film.class).getResultList();
	}

	@Override
	@Transactional
	public Film findById(int id) {
		return this.em.find(Film.class, id);
	}

	@Override
	@Transactional
	public Film save(Film f) {
		if (f.getId() == 0 ) {
			this.em.persist(f);
		}else {
			f = this.em.merge(f);
		}
		return f;
	}

	@Override
	@Transactional
	public int delete(int id) {
		Film f = this.em.find(Film.class, id);
		if (f != null) {
			this.em.remove(f);
			return 1;
		}
		return 0;
	}

}
