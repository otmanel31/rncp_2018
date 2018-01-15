package com.lonconto.instagraph.repositories;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
	
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * methode de recherche custop necessitant le creation dun entity manager donc ... 
	 * construction de la requete morceaux par morceaux
	 */
	@Override
	@Transactional(readOnly=true) // read only indique quon ne fait pas décriture insert ou update donc spring fait de loptimisation
	public Page<Image> searchWithTags(List<Integer> includedTags, List<Integer> excluededTags, Pageable pageRequest) {
		StringBuilder sb = new StringBuilder("from Image as img");
		// on verifie qu'il y a bien quelque chose ds included tags
		if (!includedTags.isEmpty()) {
			StringBuilder sbJoin = new StringBuilder();
			StringBuilder sbWhere = new StringBuilder(" WHERE");
			
			for (int position = 1; position <= includedTags.size(); position++) {
				sbJoin.append(", IN(img.tags) ta" + position); // jointure sauce spring
				//sbJoin.append("join img.tags as ta" + position); 
				if (position >1 ) {
					sbWhere.append(" AND "); // ici rajouter un and uniquement si on est pas a la premiere position
				}
				sbWhere.append(" ta"+position).append(".id=:tincid"+position);
			}
			// requete prete donc on peut tertminer la constreuction
			
			sb.append(sbJoin);
			sb.append(sbWhere);
		}
		
		
		
		// creation de la requette
		TypedQuery<Image> q = this.em.createQuery("select img "+sb.toString(), Image.class);
		TypedQuery<Long> countQuery = this.em.createQuery("select count(img) "+sb.toString(), Long.class);
		
		// passage des parametre a la requete
		for (int position = 1; position <= includedTags.size(); position++) {
			q.setParameter("tincid"+position, includedTags.get(position -1));
			countQuery.setParameter("tincid"+position, includedTags.get(position -1));
		}
		log.info("requete generer => " + sb.toString());
		// TOUT SA QUE SUR LA QUERY DES IMAGE CAR COUNT QUERY RENVOIE JUSTE LE TOTAL
		// offset renvoie la position a partire de laquelle on renvoie
		q.setFirstResult(pageRequest.getOffset());
		// combien dimgs renvoyer 
		q.setMaxResults(pageRequest.getPageSize());
		// renvoie donne, objet page requets avc tte kes infos et le nombre total delement total
		return new PageImpl<>(q.getResultList(), pageRequest, countQuery.getSingleResult());
	}

}
