package com.otmanel.airlineSpark;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/**
 * retard moyen  a larrivee en fonction de la distance du trajet
 *
 */
public class App2 
{
	
	public static class InfoVol implements Serializable{
		
		public static int CANCELLED = 0;
		public static int DIVERTED = 1;
		public static int NORMAL = 2;
		
		public static final String DISTANCE_0_200 = "0_200";
		public static final String DISTANCE_200_400 ="200_400";
		public static final String DISTANCE_400_800 = "400_800";
		public static final String DISTANCE_800_MARS ="800_MARS";
		
		public int retardDepart;
		public int retardArrivee;
		public String airport;
		public String company;
		public int distance;
		public int status;
		
		public InfoVol(int retardDepart, int retardArrivee, 
				String company, int distance, int status) {
			this.retardDepart = retardDepart;
			this.retardArrivee = retardArrivee;
			this.company = company;
			this.distance = distance;
			this.status = status;
		}
		
		public static InfoVol readLineOfCsv(String line){
			String[] champs = line.split(",");
			int status = NORMAL;
			if (parseBoolean(champs[21], false)) status = CANCELLED;
			else if (parseBoolean(champs[23], false)) status = DIVERTED;
			
			return new InfoVol(parseMinutes(champs[16], 0), parseMinutes(champs[15], 0), champs[8], parseMinutes(champs[19], 0), status);
		}
		public static boolean parseBoolean(String bool, boolean defaultValue) {
			try { 
				int val = Integer.parseInt(bool);
				return (val == 1);
			}
			catch (Exception ex) { return defaultValue;}
		}
		public static int parseMinutes(String minutes, int defaultValue) {
			try { return Integer.parseInt(minutes); }
			catch (Exception ex) { return defaultValue;}
		}
		
		public String codeDistance(){
			if (this.distance < 200) return DISTANCE_0_200;
			if (this.distance < 400) return DISTANCE_200_400;
			if (this.distance < 800) return DISTANCE_400_800;
			return DISTANCE_800_MARS;
		}
	}
	public static class Company implements Serializable{
		public String code;
		public String company_name;
		
		public Company(String code, String company_name) {
			super();
			this.code = code;
			this.company_name = company_name;
		}
		
		public static Company readLineOfCsv(String line){
			String[] champs = line.split("\",\"");
			champs[0] = champs[0].replaceAll("\"", "");
			champs[1] = champs[1].replaceAll("\"", "");
			return new Company(champs[0], champs[1]);
		}
	}
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World 2!" );
        
        SparkConf conf = new SparkConf().setAppName("AirlineSpark").setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> air_lines = sc.textFile("/user/formation/airlineSpark/files/1988_light.csv").filter(m-> !m.startsWith("Year")); // on saute la ligne des header

        // "distance"-> vol
        //JavaPairRDD<Integer, InfoVol> vols = air_lines.map(l->InfoVol.readLineOfCsv(l)).filter(v-> v.status == InfoVol.NORMAL).mapToPair(v-> new Tuple2<Integer, InfoVol>(v.distance, v));
        JavaRDD<InfoVol> vols = air_lines.map(l->InfoVol.readLineOfCsv(l)).filter(v-> v.status == InfoVol.NORMAL);
        /*
         * "cdistance_categorie -> (retardTotal, nb vols)
         */
        JavaPairRDD<String, Tuple2<Double, Integer>> groupBy =
        		vols.mapToPair(v->{
        			return new Tuple2<String, Tuple2<Double, Integer>>(v.codeDistance(), new Tuple2<Double, Integer>((double) v.retardArrivee, 1));
        		});
        
        JavaPairRDD<String, Double> moyenne = groupBy.reduceByKey((vola, volb)-> new Tuple2<Double, Integer>(vola._1+volb._1, vola._2+volb._2))
        		.mapToPair(total -> new Tuple2<String, Double>( total._1, total._2._1/total._2._2));
        moyenne.saveAsTextFile("/user/formation/airlineSpark/resultExo2");
    }
}
