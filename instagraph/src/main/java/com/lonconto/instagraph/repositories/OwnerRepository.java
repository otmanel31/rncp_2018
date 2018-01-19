package com.lonconto.instagraph.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import com.lonconto.instagraph.metier.CompositeOwnerKey;
import com.lonconto.instagraph.metier.Owner;

@PreAuthorize("permitAll")
public interface OwnerRepository extends CrudRepository<Owner, CompositeOwnerKey> {
	// _ on dit de rentrer a linterieur
	List<Owner> findByClef_ImageId(long id);
}
