package com.lonconto.instagraph.metier;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Owner {
	// pas besoin d' id possibillité de faire un pk composé mais pas necessaire la ... on fait kan mm
	@EmbeddedId 
	private CompositeOwnerKey clef;
	
}
