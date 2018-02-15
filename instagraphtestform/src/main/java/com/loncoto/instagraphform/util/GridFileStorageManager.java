package com.loncoto.instagraphform.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;


@Component
public class GridFileStorageManager {
	private static Logger log = LogManager.getLogger(FileStorageManager.class);
	
	@Autowired
	private GridFsTemplate gridFsTemplate;

	public GridFileStorageManager() {
		super();
		log.info("demarrage du file grid fs storage manager");
	}
	
	public String saveNewFile(String collection, InputStream data, String filename, String contentType) {
		BasicDBObject metadata = new BasicDBObject("collection", collection) ;
		
		String storageId = this.gridFsTemplate.store(data, filename,  contentType, metadata).getId().toString();

		return storageId;
	}
	
	
	public Optional<InputStream> getImageFile(String storageId) {
		GridFSDBFile file = this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(storageId)));
		
		System.out.println("chargement fichier "  + file.getFilename() + " d'id " + file.getId());
		return Optional.of(file.getInputStream());
	}
	
	public boolean deleteImageFile(String storageId) {
		
		this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(storageId)));
		
		return true;
	}
	
}
