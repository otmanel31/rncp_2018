package com.lonconto.instagraph.repositories;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lonconto.instagraph.metier.Image;

public interface ImageRepositoryCustom  {
	boolean saveImageFile(Image img, InputStream f);
	Optional<File> getImageFile(String storageid);
	boolean deleteImg(Image img);
	
	//recherche dimgs par tag ==> requete custom non gerer par spring data 
	Page<Image> searchWithTags(List<Integer> includedTags, List<Integer> excluededTags, Pageable pageRequest);
}
