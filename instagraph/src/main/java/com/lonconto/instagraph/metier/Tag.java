package com.lonconto.instagraph.metier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString(exclude= {"contents"})
public class Tag {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=100)
	private String libelle;
	private String description; // 256 len par defaut en bdd
	@ManyToMany(mappedBy="tags")
	@JsonIgnore
	private Set<Content> contents;
	
	public Set<Content> getContents(){
		if (contents == null) contents = new HashSet<>();
		return contents;
	}
	
	public Tag(int id, String libelle, String description) {
		this.id = id;
		this.libelle = libelle;
		this.description = description;
	}
	
	
}
