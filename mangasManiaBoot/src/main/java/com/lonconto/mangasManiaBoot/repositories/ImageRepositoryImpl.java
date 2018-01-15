package com.lonconto.mangasManiaBoot.repositories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.lonconto.mangasManiaBoot.metier.Image;
import com.lonconto.mangasManiaBoot.util.FileStorageManager;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

public class ImageRepositoryImpl implements ImageRepositoryCustom {

	Logger log = LogManager.getLogger(ImageRepositoryImpl.class);
	
	public static final int THUMB_WIDTH = 100;
	public static final int THUMB_HEIGHT = 100;

	@Autowired
	private FileStorageManager fsm;
	
	@Override
	public boolean saveImageFile(Image img, InputStream f) {
		String storageId = fsm.saveNewFile("images", f);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// on cree une image miniature
		try{
			Thumbnails.of(fsm.getImageFile(storageId).get())
				.size(THUMB_WIDTH, THUMB_HEIGHT)
				.scalingMode(ScalingMode.BICUBIC)
				.antialiasing(Antialiasing.ON)
				.outputFormat("jpg")
				.toOutputStream(bos);
			
			String thumbStorageId = fsm.saveNewFile("imageThumb", new ByteArrayInputStream(bos.toByteArray()));
			img.setThumbStorageId(thumbStorageId);
			
		}catch (IOException e) {
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
		if (img == null)
			return false;
		boolean success = this.fsm.delete(img.getStorageId());
		return fsm.delete(img.getThumbStorageId()) && success;
	}

	
	

}
