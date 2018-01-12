package com.lonconto.instagraph.repositories;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

import com.lonconto.instagraph.metier.Image;

public interface ImageRepositoryCustom  {
	boolean saveImageFile(Image img, InputStream f);
	Optional<File> getImageFile(String storageid);
	boolean deleteImg(Image img);
}
