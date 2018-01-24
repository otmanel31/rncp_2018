package com.otmanel.thirdJunitBoot.metier;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Produit {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nom;
	private double prix;
	private double poid;
	private int stock;
	private String categorie;
}
