package com.otmanel.secondJunitSpring.services;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otmanel.secondJunitSpring.metier.Gazouille;
import com.otmanel.secondJunitSpring.repositories.IGazouilleDao;

@Service
public class GazouilleService {
	
	public static final  String CENSORED = "twitter";
	private Pattern censoror;
	
	@Autowired
	private IGazouilleDao gazouilleDao;
	
	public Gazouille publish(Gazouille g) {
		// remplacer tte occurence de twitrer par gazouille
		g.setTitre(censoror.matcher(g.getTitre()).replaceAll("gazouille"));
		// idem pour le corp^s
		g.setCorps(censoror.matcher(g.getCorps()).replaceAll("gazouille"));
		gazouilleDao.save(g);
		return g;
	}

	public GazouilleService() {
		this.censoror = Pattern.compile(this.CENSORED);
	}
	
	public Gazouille readGazouille(int id) {
		Gazouille g = gazouilleDao.findById(id);
		if (g == null) {
			throw new GazouilleException("gazouille introuvable");
		}
		return g;
	}
	
	public List<Gazouille> readAllGazouile(){
		return gazouilleDao.findAll();
	}
	
	public static class GazouilleException extends RuntimeException{

		public GazouilleException(String message) {
			super(message);
		}
		
	}
	
}
