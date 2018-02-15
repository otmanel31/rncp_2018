package com.otmanel.stockgeGridFs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.otmanel.stockgeGridFs.workers.FileMongoUploader;


/**
 * 
 * @author Stagiaire
 * commande line runner pour lancer en cli appl et gerer args ... 
 */
@SpringBootApplication
public class StockgeGridFsApplication implements CommandLineRunner {

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	public static void main(String[] args)  {
		SpringApplication.run(StockgeGridFsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("application stockage started .... ");
		System.out.println(Arrays.toString(args));
		if (args.length > 0 ) {
			FileMongoUploader fmu = new FileMongoUploader(args[0], gridFsTemplate);
			fmu.run();
		}
		else {
			System.out.println("no directory given as arg");
		}
	}
}
