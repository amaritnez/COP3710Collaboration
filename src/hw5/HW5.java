package hw5;
/*
 * Main Class: HW5.java
 * Date last modified: 3/29/2018 @ 10:55PM
 * Version: 1.0
 */
import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.neo4j.io.fs.FileUtils;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.Traverser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class HW5 {
  private static final String DB_PATH = "target/movies-db"; //database path for this project
  private static GraphDatabaseService graphDb; //actual graph database
  private static List<Movies> allMovies; //list of JSON files
  
  /* orginally the main method for HW3, has been modified slightly
   * to now return a list containing all the movies from each JSON file (a list of lists).
   * Still reads the JSON files and  creates movie objects from them.
   * h2 database related features have been commented out in this version due
   * to irrelevance
   */
  public static List<Movies> load() throws JAXBException, JsonParseException, JsonMappingException, IOException,
      ClassNotFoundException, SQLException { 

    // Object mapper for JSON file
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //a list for all the JSON files
    List<Movies> jsonMovies = new ArrayList<Movies>();
    
    //MovieDBCreate.createTables(); //omitted since we aren't working with h2 anymore
    for (int i = 1; i < 26; i++) { //iterate through each json file
      jsonMovies.add( mapper.readValue(new File("src/data/page" + i + ".json"), Movies.class) );
      List<Movie> moviesList = jsonMovies.get(i-1).getmovies(); //a list of all movies in ONE json file
      /* omitted since we don't need to loop through individual movies here
      for (int k = 0; k < moviesList.size(); k++) { //iterate through each individual movie in a single json file
        //MovieDBCreate.insert(moviesList.get(k)); omitted since we aren't working with h2 anymore
        //System.out.println(++movieCount); debugging only
        //System.out.println(moviesList.get(k).getTitle()); debugging only
      }
      */
    }
    return jsonMovies; //return the list of JSON files
  }

  public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException,
      ClassNotFoundException, JAXBException, SQLException {

    allMovies = load(); // loading the movie data from the json files
    createDb(); // creating a graph database in DB_PATH
    doQueries(); // do some queries
    shutDown(); // shuts down the database
  }

  //shutdown the query
  private static void shutDown() {
    {
      System.out.println();
      System.out.println("Shutting down database ...");
      graphDb.shutdown();
    }
  }

  //creates a graph database
  private static void createDb() {
    clearDb(); //clear any existing databases before continuing

    System.out.println("Starting up database ...\n");
    
    graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
    registerShutdownHook(graphDb); //register a shutdown incase something goes wrong

    try (Transaction tx = graphDb.beginTx()) {

      for (int a = 0; a < allMovies.size(); a++) { //loop through each JSON file

        List<Movie> moviesList = allMovies.get(a).getmovies(); //list of movies in ONE JSON file

        for (int b = 0; b < moviesList.size(); b++) { //then loop through all the movies in one file

          Movie currentMovie = moviesList.get(b); //one single movie
          Node movieNode = graphDb.createNode(HW5Labels.MOVIE); //create one node for one movie
          //add properties for the movie
          movieNode.setProperty("name", currentMovie.getTitle());
          movieNode.setProperty("year", currentMovie.getYear());
          movieNode.setProperty("mpaa_rating", currentMovie.getMpaa_rating());
          movieNode.setProperty("id", currentMovie.getId());
          movieNode.setProperty("audience_score", currentMovie.getRatings().getAudience_score());
          movieNode.setProperty("critics_score", currentMovie.getRatings().getCritics_score());

          //loop through the entire cast of the movie
          for (int c = 0; c < currentMovie.getAbridged_cast().size(); c++) {
            Actor currentAcotr = currentMovie.getAbridged_cast().get(c); //one actor
            Node actorNode = graphDb.createNode(HW5Labels.ACTOR); //create one node for one actor
            //add properties for the actor
            actorNode.setProperty("name", currentAcotr.getName());
            actorNode.setProperty("id", currentAcotr.getId());
            //create a relationship between actor and its movie
            Relationship relationship = actorNode.createRelationshipTo(movieNode, HW5Relationships.ACTS_IN);
          }
        }
      }
      tx.success(); //if it makes it this far, the db creation was a success
    }

  }

  //registers a shutdown in case something goes wrong
  private static void registerShutdownHook(final GraphDatabaseService graphDb) {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graphDb.shutdown();
      }
    });
  }

  //clears any existing databases in DB_PATH
  private static void clearDb() {
    try {
      FileUtils.deleteRecursively(new File(DB_PATH));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //does some basic queries
  private static void doQueries() {
    try (Transaction tx = graphDb.beginTx()) {

      System.out.println("--------- Movies in 1980 (Java)-----------");
      printMoviesIn_Java(1980);
      System.out.println("\n--------- Tautou's Co-Stars (Java)-----------");
      printCoStarts_Java("Audrey Tautou");
      System.out.println("\n--------- Movies in 1980 (CQL)-----------");
      printMoviesIn_Cql(1980);
      System.out.println("\n--------- Tautou's Co-Stars (CQL)-----------");
      printCoStarts_Cql("Audrey Tautou");

      tx.success(); //if it makes it this far, everything went well
    }
  }

  /* prints any actors featured in the same movie as the
   * actor passed into the method. Will not print the actor passed in.
   * Uses CQL statements
   */
  private static void printCoStarts_Cql(String actor) {

    try (Transaction tx = graphDb.beginTx()) {
      //first find all movies the actor is in
      Result result = graphDb
          .execute(String.format("match (m:MOVIE)<-[:ACTS_IN]-(a:ACTOR) where a.name = '%s' return m.name;", actor));

      while (result.hasNext()) { //iterate through the movies
        Map<String, Object> row = result.next();
        String title = (String) row.get("m.name");
        Result resultDos = graphDb.execute( //then find all other actors in a specific movie
            String.format("match (a:ACTOR)-[:ACTS_IN]->(m:MOVIE) where m.name = \"%s\" return a.name;", title));
        while (resultDos.hasNext()) { //iterate through the actors
          Map<String, Object> rowDos = resultDos.next();
          String titleDos = (String) rowDos.get("a.name");
          if (!titleDos.equals(actor)) { //don't print the actor who we used as a parameter
            System.out.println(titleDos);
          }
        }
      }
      tx.success(); //if it makes it this far, everything went well
    }
  }

  /* prints all movies in the given year
   * Uses CQL statements
   */
  private static void printMoviesIn_Cql(int year) {

    try (Transaction tx = graphDb.beginTx()) {
      //find all movie nodes with the property of the given year
      Result result = graphDb.execute(String.format("match (m:MOVIE) where m.year= %d return m.name;", year));

      while (result.hasNext()) { //iterate through all the movies and print them out
        Map<String, Object> row = result.next();
        String title = (String) row.get("m.name");
        System.out.println(title);
      }
      tx.success(); //if it makes it this far, everything went well
    }
  }

  /* prints any actors featured in the same movie as the
   * actor passed into the method. Will not print the actor passed in.
   * Does NOT use CQL statements; just good ol' Java
   */
  private static void printCoStarts_Java(String name) {
    try (Transaction tx = graphDb.beginTx()) {
      //find all nodes with the name passed in
      ResourceIterator<Node> actorNode = graphDb.findNodes(HW5Labels.ACTOR, "name", name);
      while (actorNode.hasNext()) { //iterate through each node
        //traverse to the nodes movie
        Traverser traverser = graphDb.traversalDescription().breadthFirst().evaluator(Evaluators.toDepth(1))
            .relationships(HW5Relationships.ACTS_IN, Direction.OUTGOING).traverse(actorNode.next());

        for (Path path : traverser) { //for each "path", or relationship
          if (path.length() > 0 && path.endNode().hasLabel(HW5Labels.MOVIE)) { //only worry about paths to the movie
            Node parentMovie = path.endNode();
            //traverse to all the immediate nodes of the given movie; it should be all the actors
            Traverser traverserDos = graphDb.traversalDescription().breadthFirst().evaluator(Evaluators.toDepth(1))
                .relationships(HW5Relationships.ACTS_IN, Direction.INCOMING).traverse(parentMovie);
            for (Path pathDos : traverserDos) { //for each "path", or relationship
              //don't print nodes that contain movies, or the actor passed into the method
              if (!pathDos.endNode().hasLabel(HW5Labels.MOVIE) && !pathDos.endNode().getProperty("name").equals(name))
                System.out.println(pathDos.endNode().getProperty("name"));
            }
          }
        }
      }
      tx.success(); //if it makes it this far, everything went well
    }
  }

  /* prints all movies in the given year
   * Does NOT use CQL statements; just good ol' Java
   */
  private static void printMoviesIn_Java(int year) {
    try (Transaction tx = graphDb.beginTx()) {
      //find all nodes with the given year
      ResourceIterator<Node> classNodes = graphDb.findNodes(HW5Labels.MOVIE, "year", year);
      while (classNodes.hasNext()) { //iterate through the nodes
        System.out.println(classNodes.next().getProperty("name")); //print the nodes
      }
      tx.success(); //if it makes it this far, everything went well
    }
  }
}
