package com.lonconto.instagraph.metier;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Embeddable // on peux inclure celui la ds une autre table autement dit nest oas une entité mais peux etre inclus ds une autre entite 
public class CompositeOwnerKey implements Serializable{

	@ManyToOne
	private Image image;
	@ManyToOne
	private User user;

	// pour ce qui sert de cle composé il faut redefinir le hash code et equal + impl seriali .... 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : (int)image.getId());
		result = prime * result + ((user == null) ? 0 : user.getId());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeOwnerKey other = (CompositeOwnerKey) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (image.getId() != other.image.getId())
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (image.getId() != other.image.getId())
			return false;
		return true;
	}
	
	
}
