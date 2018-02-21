package com.otmanel.movieSpark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

/**
 * ratings par genre
 *
 */
public class App2 
{
	public static class Rating implements Serializable{
		public int userId;
		public int movieId;
		public double rating;
		public String timestamp;
		
		public Rating(int userId, int userID2, double rating, String timestamp) {
			super();
			this.userId = userId;
			this.movieId = userID2;
			this.rating = rating;
			this.timestamp = timestamp;
		}
		
		public static Rating fromCsvLine(String line){
			String[] champs = line.split(",");		
			return new Rating(Integer.parseInt(champs[0]), Integer.parseInt(champs[1]), Double.parseDouble(champs[2]), champs[3]);
		}		
	}
	public static class Movie implements Serializable{
		public int movieId;
		public String title;
		public String[] genres;
		
		public Movie(int movieId, String title, String[] genres) {
			this.movieId = movieId;
			this.title = title;
			this.genres = genres;
		}	
		public static Movie fromCsvLine( String line){
			// gestion des virgule ds le ttile
			int posFirst = line.indexOf(','); // 1er champ => pos 1ere virgule
			int lastPost = line.lastIndexOf(','); // dernier champ => pos derniere virgule
			
			String identifiant = line.substring(0, posFirst);
			String titre = line.substring(posFirst+1, lastPost);
			String[] genres = line.substring(lastPost+1).split("[|]"); // extract des genres
			
			return new Movie(Integer.parseInt(identifiant), titre, genres); // return movie object
		}
	}
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SparkConf conf = new SparkConf()
        	.setAppName("movieSpark")
        	.setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> movieLines = sc.textFile("/user/formation/moviesSpark/files/movies.csv").filter(m-> !m.startsWith("movieId")); // on saute la ligne des header
        JavaRDD<String> ratingLines = sc.textFile("/user/formation/moviesSpark/files/ratings.csv").filter(m-> !m.startsWith("userId"));

        // jointure entre movie et rating
        /*
         * SORTIE
         * -> ratings -> <movieId, Rating(object)>
         * -> movies -> <movueID, MOvie()>
         * 
         * POUR permettre la jointure nesuiten il faut cle commune entre les 2 rdd
         */
        JavaPairRDD<Integer, Rating> ratings = ratingLines.map(l->Rating.fromCsvLine(l)).mapToPair(r-> new Tuple2<Integer, Rating>(r.movieId, r));
        JavaPairRDD<Integer, Movie> movies = movieLines.map(l->Movie.fromCsvLine(l)).mapToPair(m-> new Tuple2<Integer, Movie>(m.movieId, m));
        
        // jointure => GO
        /*	ens ortie:
         *  -> (movieID, (rating, movie))
         *  -> (31, (rating(3.5...), movie(DAngerous mind...))
         */
        JavaPairRDD<Integer, Tuple2<Rating, Movie>> jointure = ratings.join(movies);
        
        // (movieid, (titre, note, )
        // je ne conserve que l'assocaition dun genre avc un rating
        // je n'ai plus une fois cette operation effectuer de possibilite de savoir a kel film etait associer quel rating
        JavaPairRDD<String, Tuple2< Double, Integer>> selectPerGenre =
        		jointure.flatMapToPair(j->{
        			              //rat, quantite
        			// "comedie" -> (3.5, 1)
        			List<Tuple2<String, Tuple2<Double, Integer>>> genres_rating = new ArrayList();
        			Arrays.stream(j._2._2.genres)
        				.forEach(genre-> genres_rating.add(new Tuple2<String, Tuple2<Double,Integer>>( genre,  new Tuple2<Double, Integer>(j._2._1.rating, 1))));
        			return genres_rating;
        		});
        
        JavaPairRDD<String, Tuple2< Double, Integer>> reduction = // "genre" -> (somme total rating, nb_par genre)
        		selectPerGenre.reduceByKey((r1, r2)-> new Tuple2<Double, Integer>(r1._1 + r2._1, r1._2 + r2._2));

        // genre 6> moyenne_rating
        JavaPairRDD<String, Double> moyennes = reduction.mapToPair(r-> new Tuple2<String,  Double>(r._1, r._2._1() /r._2()._2()));
        moyennes.saveAsTextFile("/user/formation/moviesSpark/ratingMovie");
    }
}
