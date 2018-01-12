package com.lonconto.instagraph.metier.projections;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

import com.lonconto.instagraph.metier.Image;
import com.lonconto.instagraph.metier.Tag;

@Projection(name="ImageWithTags", types=Image.class)
public interface ImageWithTags {
	long getId();
	String getName();
	String getDescription();
	LocalDateTime getDateAdded();
	Set<Tag> getTags();
	String getFileName();
	String getContentType();
	long getFileSize();
	int getWidth();
	int getHeight();

}
