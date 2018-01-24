package com.otmanel.exoUnitTest.metier;

import java.time.LocalDateTime;
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
public class Intervention {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String lieu;
	private LocalDateTime dateDebut;
	private LocalDateTime dateFin;
	@ManyToOne
	private Intervenant intervenant;
	private String materiel;
	
	public Intervention(int id, String lieu, LocalDateTime dateDebut, LocalDateTime dateFin, String materiel) {
		super();
		this.id = id;
		this.lieu = lieu;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.materiel = materiel;
	}
	
	
}
