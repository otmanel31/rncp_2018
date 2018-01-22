package com.otmanel.secondJunitSpring.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.otmanel.secondJunitSpring.metier.Gazouille;

public class GazouilleDaoMock implements IGazouilleDao {

	private List<Gazouille> gazouilles;
	
	public GazouilleDaoMock( ) {
		
	}

	public void addGazouilles(Gazouille...gazouilles) {
		// this.gazouilles = Arrays.asList(gazouilles); as list pose probeleme
		this.gazouilles = Arrays.asList(gazouilles);
		this.gazouilles = new ArrayList<>(Arrays.asList(gazouilles));
	}
	
	
	@Override
	public List<Gazouille> findAll() {
		return this.gazouilles;
	}

	@Override
	public Gazouille findById(final int id) {	
		return this.gazouilles.stream().filter(g->g.getId() == id).findFirst().orElse(null);
	}

	@Override
	public int save(Gazouille g) {
		this.gazouilles.add(g);
		g.setId(this.gazouilles.stream().mapToInt(gaz->gaz.getId()).max().orElse(0)+1);
		return 1;
	}

}
