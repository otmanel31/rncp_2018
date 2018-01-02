package com.applicationrevision.filmRevisionJsp.repositories;

import java.util.List;

import com.applicationrevision.filmRevisionJsp.metier.Film;

public interface IFilmDao {
	List<Film> findAll();
	Film findById(int id);
	Film save(Film f);
	int delete(int i);
}
