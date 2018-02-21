package com.otmanel.airlineSpark;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/**
 * Hello world!
 *
 */
public class App 
{
	public static class InfoVol implements Serializable{

		public static int CANCELLED = 0;
		public static int DIVERTED = 1;
		public static int NORMAL = 2;
		
		public int retardDepart;
		public int retardArrivee;
		public String airport;
		public String company;
		public int distance;
		public int status;
		
		public InfoVol(int retardDepart, int retardArrivee, String company, int distance, int status) {
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
			
			return new InfoVol(parseMinutes(champs[14], 0), parseMinutes(champs[15], 0), champs[8], parseMinutes(champs[18], 0), status);
		}
		
		public static int parseMinutes(String minutes, int defaultValue) {
			try { return Integer.parseInt(minutes); }
			catch (Exception ex) { return defaultValue;}
		}
		
		public static boolean parseBoolean(String bool, boolean defaultValue) {
			try { 
				int val = Integer.parseInt(bool);
				return (val == 1);
			}
			catch (Exception ex) { return defaultValue;}
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
        System.out.println( "Hello World!" );
        
        SparkConf conf = new SparkConf().setAppName("AirlineSpark").setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> air_lines = sc.textFile("/user/formation/airlineSpark/files/1988_light.csv").filter(m-> !m.startsWith("Year")); // on saute la ligne des header
        JavaRDD<String> comp_name = sc.textFile("/user/formation/airlineSpark/files/carriers.csv"); // on saute la ligne des header

        // "compagny_code"-> VOl (v.company = compagny_code
        JavaPairRDD<String, InfoVol> vols = air_lines.map(l->InfoVol.readLineOfCsv(l)).filter(v->v.status == v.NORMAL).mapToPair(v-> new Tuple2<String, InfoVol>(v.company, v));
        JavaPairRDD<String, Company> companies = comp_name.map(l-> Company.readLineOfCsv(l)).mapToPair(c-> new Tuple2<String, Company>(c.code, c));
        
        /*
         * 'compcode'-> (infovol, companie )
         */
        JavaPairRDD<String, Tuple2<InfoVol, Company>> jointure = vols.join(companies);
        
        /*
         * "comp_name -> (retardTotal, nb_vols)
         */
        JavaPairRDD<String, Tuple2<Double, Integer>> reduce =
        		jointure.mapToPair(j-> new Tuple2<String, Tuple2<Double, Integer>>(j._2._2.company_name, new Tuple2<Double, Integer>((double)j._2._1.retardArrivee, 1)))
        		.reduceByKey((vola, volb)-> new Tuple2<Double, Integer>(vola._1 + volb._1, vola._2 +volb._2));
        
        JavaPairRDD<String, Double> moyenne = reduce.mapToPair(r -> new Tuple2(r._1, r._2._1 / r._2._2));
        moyenne.saveAsTextFile("/user/formation/airlineSpark/resultExo1");
    }
}
