package com.loncoto.instagraphform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoGridConfig extends AbstractMongoConfiguration {

	@Value("${stockage.mongo.address}")
	private String mongoAddress;
	
	@Value("${stockage.mongo.database}")
	private String mongoDB;
	
	@Override
	protected String getDatabaseName() {
		return this.mongoDB;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(this.mongoAddress);
	}
	
	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		// mongoDbFactory => permet de generer connxion a mon serveur via mongo 
		// mappingMongoConverter => config par defautl our mapper les types de donnees vers mongo
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}

}
