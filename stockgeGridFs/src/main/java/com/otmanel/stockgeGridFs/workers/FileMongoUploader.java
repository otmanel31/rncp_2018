package com.otmanel.stockgeGridFs.workers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * @author Stagiaire
 * 
 */
public class FileMongoUploader implements Runnable { // 
	
	private String storageDir;
	
	private GridFsTemplate gridFsTemplate;
	
	public FileMongoUploader(String storageDir, GridFsTemplate gridFsTemplate) {
		this.storageDir = storageDir;
		this.gridFsTemplate = gridFsTemplate;
	}

	@Override
	public void run() {
		File f = new File(storageDir);
		if (f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			
			for (File fichier : files) {
				if (fichier.isFile() && fichier.length() < 50 *1024 * 10244) {
					FileInputStream fs;
					try {
						fs = new FileInputStream(fichier);
						String storageId = this.gridFsTemplate.store(fs, fichier.getName()).getId().toString();
						System.out.println("upload de  " + fichier.getName() + " id= " + storageId);
						fs.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
