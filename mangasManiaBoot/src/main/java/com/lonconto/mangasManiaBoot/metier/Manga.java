package com.lonconto.mangasManiaBoot.metier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor  @ToString(exclude= {"img"})
public class Manga {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String titre;
	private String auteur;
	/*@JsonFormat(pattern="yyyy-MM-dd")*/
	private LocalDate dateParution;
	private String genre;
	private int rating;
	@ManyToOne
	private Image img;
	
	public Manga(int id, String titre, String auteur, LocalDate dateParution, String genre, int rating) {
		super();
		this.id = id;
		this.titre = titre;
		this.auteur = auteur;
		this.dateParution = dateParution;
		this.genre = genre;
		this.rating = rating;
	}
}
