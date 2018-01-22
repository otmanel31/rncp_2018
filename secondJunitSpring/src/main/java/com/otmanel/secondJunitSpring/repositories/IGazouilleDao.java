package com.otmanel.secondJunitSpring.repositories;

import java.util.List;

import com.otmanel.secondJunitSpring.metier.Gazouille;

public interface IGazouilleDao {

	List<Gazouille> findAll();

	Gazouille findById(int id);

	int save(Gazouille g);

}