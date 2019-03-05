package hw3;
/* 
 * Main Class: HW3Main
 * Date last modified: 2/11/2018 @ 3:22PM
 * Version: 1.0
 */

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HW3Main {

  public static void main(String[] args) throws JAXBException, JsonParseException, JsonMappingException, IOException,
      ClassNotFoundException, SQLException {

    // Object mapper for JSON file
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Movies movies = null;
    
    MovieDBCreate.createTables(); //creates the initial tables for our db
    for (int i = 1; i < 26; i++) { //iterate through each json file and insert the values into the db
      movies = mapper.readValue(new File("src/data/page" + i + ".json"), Movies.class);
      List<Movie> moviesList = movies.getmovies();
      for (int k = 0; k < moviesList.size(); k++) { //iterate through each individual movie in a single json file
        MovieDBCreate.insert(moviesList.get(k));
      }
    }
    
  }
  
  //method used by HW5 driver method
  public static void hw5Load(String[] args) throws JAXBException, JsonParseException, JsonMappingException, IOException,
      ClassNotFoundException, SQLException {

    // Object mapper for JSON file
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Movies movies = null;

    MovieDBCreate.createTables(); // creates the initial tables for our db
    for (int i = 1; i < 26; i++) { // iterate through each json file and insert
                                   // the values into the db
      movies = mapper.readValue(new File("movies\\page" + i + ".json"), Movies.class);
      List<Movie> moviesList = movies.getmovies();
      for (int k = 0; k < moviesList.size(); k++) { // iterate through each
                                                    // individual movie in a
                                                    // single json file
        MovieDBCreate.insert(moviesList.get(k));
      }
    }

}
}
