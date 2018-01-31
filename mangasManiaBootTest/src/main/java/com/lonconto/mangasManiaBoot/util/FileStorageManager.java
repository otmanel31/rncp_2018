package com.lonconto.mangasManiaBoot.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// objet injectable dans dautre bean spring
@Component
public class FileStorageManager {
	private static Logger log = LogManager.getLogger(FileStorageManager.class);
	
	private Random rd = new Random();
	
	@Value("${filestorage.baserep}")
	private File storageRoot; // repertoire de base de sauvegarde 

	public FileStorageManager() {
		super();
		log.info("demarrage sur file storage manager !!!");
	}
	
	// si jamais il ne le trouve pas il retournera empty
	public Optional<File> getImageFile(String storageId) {
		if (storageRoot == null || !storageRoot.exists() || !storageRoot.isDirectory()) {
			return Optional.empty();
		}
		File rep = Paths.get(storageRoot.getPath(), storageId.substring(0,2)).toFile();
		if (!rep.exists() || !rep.isDirectory()) return Optional.empty();
		File f = Paths.get(rep.getAbsolutePath(), storageId).toFile();
		if (f != null && f.exists() && f.isFile()) return Optional.of(f);
		else return Optional.empty();
	}
	
	public String saveNewFile(String collection, InputStream data) {
		if (storageRoot == null || !storageRoot.exists() || !storageRoot.isDirectory()) {
			throw new RuntimeException("invalid storage root");
		}
		String name = collection+"#"+rd.nextLong();
		String sha1hexname = DigestUtils.sha1Hex(name);
		// mise en place de sous rep a  partir des deux prmeier caracteres de la valeur hexa
		String sousRep = sha1hexname.substring(0, 2);
		
		// on recupere un objet file sur le rep ou on srtoke limage
		// File rep = new File(storageRoot.getPath()+sousRep);
		File rep = Paths.get(storageRoot.getPath(), sousRep).toFile();
		if (!rep.exists()) rep.mkdirs();
		if (!rep.isDirectory()) throw new RuntimeException("unable to create a storage directory " + sha1hexname) ;
		//  pour sauvegarder
		try {
			log.info("sauvegarde de " + sha1hexname);
			Files.copy(data, Paths.get(rep.getAbsolutePath(),sha1hexname), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
			//e.printStackTrace();
		}
		return sha1hexname;
		
	}
	
	public boolean delete(String storageId) {
		if (storageRoot == null || !storageRoot.exists() || !storageRoot.isDirectory()) {
			return false;
		}
		File rep = Paths.get(storageRoot.getPath(), storageId.substring(0,2)).toFile();
		if (!rep.exists() || !rep.isDirectory()) return false;
		File f = Paths.get(rep.getAbsolutePath(), storageId).toFile();
		if (f != null && f.exists() && f.isFile()) return f.delete();
		else return false;
	}
}
