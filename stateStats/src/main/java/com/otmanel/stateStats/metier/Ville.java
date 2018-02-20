package com.otmanel.stateStats.metier;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection="ville") // objet metier mapper ds mongo db => equivaut a @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Ville {
	@Id
	private String id;
	private String type;
	private String city;
	private List<Double> loc;
	private int pop;
	private String state;
}
