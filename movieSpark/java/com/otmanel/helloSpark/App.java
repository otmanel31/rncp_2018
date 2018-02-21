package com.otmanel.helloSpark;

import java.util.Arrays;

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
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        SparkConf conf = new SparkConf();
        conf.setAppName("word count spark")
        	.setMaster("local[*]"); // local indique que le noeud spark maitre est sur localhost, on peut potentioelleùme,t soumettre in job ds un autre cluster spark
        ; 							// et crochet etoile => on lu bindique le parallelisme a utiliser ici on laiasse faire 
        							// si [2) => 2 tache en parallele	
        
        // ensuite on lui indique le context langage // => contexte dexecution spark
        JavaSparkContext sc = new JavaSparkContext();
        
        /*
         * le fonctionnement de spark est centre autour des rdd; ressilient dataset
         * voir note cours 19/02.....
         */
        
        // etape 1 lire les donne via une entree ... ici livre ds hdfs
        JavaRDD<String> lines = sc.textFile("/user/formation/helloSpark/books/miserable.txt"); // eval  paresseuse /!\
        
        // si je veux compter les mots , que veais avoir en sortie
        // map String -> Integer ==> tuple (coupple de valeur)
        
        // split genere un tableau donc on a un ensemble de tableau consécutif a la suite des un des autres
        // flat mat lit le contenu des ces tableau et le ressort ds un seul tableau flux "ecrase" => tableau a 1 niveau
        JavaPairRDD<String, Integer> result = lines.flatMap(l -> Arrays.asList(l.split("[- ,.;!?'\"$!#]+")))
        		.mapToPair(mot-> new Tuple2<String, Integer>(mot, 1))
        		// et regrouper ensemble tt les tuple qui ont la mm clé (le mot)
        		// et appliquer la fonction passer en argument consecutivement sur tte les valeurs
        		.reduceByKey((a, b)-> a+b);
        
        // sauvegarder la sortie ds un fichier ds hdfs
        // cest cela qui declenchera reelement lreexecution
        
        result.saveAsTextFile("/user/formation/helloSpark/wordCount1");
        
    }
}
