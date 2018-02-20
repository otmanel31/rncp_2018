package com.otmanel.bonjourBatch.beans;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AeroportFieldSetMapper implements FieldSetMapper<Airport> {
	// classe dont a besoin le item reader
	@Override
	public Airport mapFieldSet(FieldSet champs) throws BindException {
		//
		return new Airport(champs.readString(0),
				// on donnne soit lindice de la colonne soit le nom du champs definit ds le pom.xml
				champs.readString(1), champs.readString(2), 
				champs.readString(3), champs.readString(4), champs.readDouble(5), champs.readDouble(6));
	}
	
}
