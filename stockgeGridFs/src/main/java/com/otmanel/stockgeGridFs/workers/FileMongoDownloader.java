package com.otmanel.stockgeGridFs.workers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.gridfs.GridFSDBFile;

public class FileMongoDownloader implements Runnable {

	private String storageDir;
	private GridFsTemplate gridFsTemplate;
	
	public FileMongoDownloader(String storageDir, GridFsTemplate gft ) {
		this.storageDir = storageDir;
		this.gridFsTemplate = gft;
	}
	
	@Override
	public void run() {
		File rep = new File(this.storageDir);
		if (rep.exists() && rep.isDirectory()) {
			List<GridFSDBFile> fichiers =  this.gridFsTemplate.find(new Query());
			for (GridFSDBFile fichier : fichiers) {
				System.out.println("chargement fichier "  + fichier.getFilename() + " d'id " + fichier.getId());
				
				try {
					// copier le fichier ds notre repertoire
					fichier.writeTo(Paths.get(rep.getAbsolutePath(), fichier.getFilename()).toFile());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
