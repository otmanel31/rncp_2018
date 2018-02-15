package com.otmanel.stockgeGridFs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoGridFsConfig extends AbstractMongoConfiguration {

	@Value("${stockage.mongo.address}") // equivalent @autowired pour les vars en provenance de properties
	private String mongoAdress; // on injecte ce qui vient du properties ici
	
	@Value("${stockage.mongo.database}")
	private String mongoDatabase;
	
	@Override
	protected String getDatabaseName() {
		return mongoDatabase;
	}

	/**
	 * cette meth retourne la connexion au serveur mongo
	 */
	@Override
	public Mongo mongo() throws Exception {
		
		return new MongoClient(mongoAdress);
	}
	
	@Bean // comme sa on peux linjecter ou on veux
	public GridFsTemplate gridFsTemplate() throws Exception {
		// mongoDbFactory => permet de generer connxion a mon serveur via mongo 
		// mappingMongoConverter => config par defautl our mapper les types de donnees vers mongo
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}

}
