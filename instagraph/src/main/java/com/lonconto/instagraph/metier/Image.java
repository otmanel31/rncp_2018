package com.lonconto.instagraph.metier;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Image extends Content {
	private String fileName;
	@Column(length=100)
	private String contentType;
	private long fileSize;
	private int width;
	private int height;
	@Column(length=80) // sha1 hex length 40 caracteres mais on est large au cas ou
	private String fileHash;
	@Column(length=80)
	private String storageId; // plutot que path et plus adapté pour pls tard lors de use mongoDb
	private String thumbStorageId; // clé permettant de le trouver ds le systeme de fichier
	
	public Image(long id, String name, String description, LocalDateTime datetime, String fileName, String contentType,
			long fileSize, int width, int height, String fileHash, String storageId, String thumbStorage) {
		
		super(id, name, description, datetime);
		this.fileName = fileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.width = width;
		this.height = height;
		this.fileHash = fileHash;
		this.storageId = storageId;
		this.thumbStorageId = thumbStorage;
	}
	
	

}
