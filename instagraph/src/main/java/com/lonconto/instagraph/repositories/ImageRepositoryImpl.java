package com.lonconto.instagraph.repositories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lonconto.instagraph.metier.Image;
import com.lonconto.instagraph.util.FileStorageManager;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

public class ImageRepositoryImpl implements ImageRepositoryCustom {
	
	Logger log = LogManager.getLogger(ImageRepositoryImpl.class);
	
	public static final int THUMB_WIDTH = 164;
	public static final int THUMB_HEIGHT = 164;

	@Autowired
	private FileStorageManager fsm;
	
	
	@Override
	public boolean saveImageFile(Image img, InputStream f){
		String storageId  = fsm.saveNewFile("images", f); // on save ds le systeme de fichier
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); // genere tableau d'octet => tableau de stream
		try {
			Thumbnails.of(fsm.getImageFile(storageId).get())
				.size(THUMB_WIDTH, THUMB_HEIGHT)
				.scalingMode(ScalingMode.BICUBIC)
				.antialiasing(Antialiasing.ON) // lisse l'image
				.outputFormat("jpg")
				.toOutputStream(bos);
			// sauvegarde de la miniature en memoire tmp au lieu de save un fichier puis le relire 
			String thumbSorageId = fsm.saveNewFile("imagesThumb", new ByteArrayInputStream(bos.toByteArray()));
			// puis on lecrit 
			img.setThumbStorageId(thumbSorageId);
			
		} catch (IOException | ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			log.error("erreur lors de la generation de la miniature: " + e  );
		}
		
		img.setStorageId(storageId);
		
		return true;
	}


	@Override
	public Optional<File> getImageFile(String storageid) {
		return fsm.getImageFile(storageid);
	}


	@Override
	public boolean deleteImg(Image img) {
		if (img == null) {
			return false;
		}
		boolean success = fsm.delete(img.getStorageId());
		return fsm.delete(img.getThumbStorageId()) && success;
	}

}
