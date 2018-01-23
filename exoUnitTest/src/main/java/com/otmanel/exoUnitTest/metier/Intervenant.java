package com.otmanel.exoUnitTest.metier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Intervenant {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nom;
	private String mail;
	@OneToMany(mappedBy="intervenant")
	private Set<Intervention> interventions;
	
	public Set<Intervention> getInterventions(){
		if (this.interventions == null) this.interventions = new HashSet<>();
		return this.interventions;
	}

	public Intervenant(int id, String nom, String mail) {
		super();
		this.id = id;
		this.nom = nom;
		this.mail = mail;
	}
	
	
}
