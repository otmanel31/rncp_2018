package com.loncoto.instagraphform.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.loncoto.instagraphform.metier.Content;

@RepositoryRestResource
public interface ContentRepository extends PagingAndSortingRepository<Content, Long> {
	// necessaire pour forcer le préchargement des tags
	// on peux sen passer mais moins efficaces car jackson devra redeclencher la chargement des tags (relation N-N)
	// on ne veux pas de fetch eager car c un cas exceptionnel
	@Query("select c from Content as c left join fetch c.tags where c.id=:contentId")
	Content findOneIncludingTags( @Param("contentId") long contentId);
}
