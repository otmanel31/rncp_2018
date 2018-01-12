package com.lonconto.mangasManiaBoot.metier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString(exclude= {"mangas"})
public class Image {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String fileName;
	private long fileSize;
	private String contentType;
	private String storageId;
	private String thumbStorageId;
	@OneToMany(mappedBy="img")
	@JsonIgnore
	private Set<Manga> mangas;
	
	public Set<Manga> getMangas(){
		if (this.mangas == null ) this.mangas = new HashSet<>();
		return this.mangas;
	}
	
	public Image(int id, String fileName, long fileSize, String contentType, String storageId, String thumbStorageId) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.storageId = storageId;
		this.thumbStorageId = thumbStorageId;
	}
}
