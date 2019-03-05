package hw3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbc.JdbcSQLException;

public class MovieDBCreate {

  // creates the initial tables
  public static void createTables() throws SQLException, ClassNotFoundException {
    Class.forName("org.h2.Driver");
    Connection conn = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir") + "/db/movies", "sa",
        "");

    Statement statement = conn.createStatement();

    statement.execute(
        "create table actor (" + "id varchar(100),name varchar(100)," + "constraint pk_actor_id primary key (id));");

    statement.execute("create table movie( " + "id varchar(100)," + "title varchar(100)," + "year smallint unsigned,"
        + "mpaa_rating varchar(10)," + "audience_score smallint unsigned," + "critics_score smallint unsigned,"
        + "constraint pk_id primary key(id)" + ");");

    statement.execute("create table character(" + "actor_id varchar(100)," + "movie_id varchar(100),"
        + "character varchar(100)," + "constraint pk_character_id primary key(movie_id, actor_id, character),"
        + "constraint fk_actor_id foreign key (actor_id) references actor (id),"
        + "constraint fk_movie_id foreign key (movie_id) references movie (id)" + ");");

    conn.close();
  }

  // inserts various values from a passed-in movie into the 3 tables created
  public static void insert(Movie currentMovie) throws ClassNotFoundException, SQLException {

    Class.forName("org.h2.Driver");
    Connection conn = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir") + "/db/movies", "sa",
        "");

    Statement statement = conn.createStatement();

    // insert the movie values
    statement.execute("INSERT INTO movie VALUES ('" + currentMovie.getId() + "', '"
        + currentMovie.getTitle().replaceAll("'", "\'\'") + "', '" + currentMovie.getYear() + "', '"
        + currentMovie.getMpaa_rating() + "', '" + currentMovie.getRatings().getAudience_score() + "', '"
        + currentMovie.getRatings().getCritics_score() + "');");

    // insert the actor values
    for (int i = 0; i < currentMovie.getAbridged_cast().size(); i++) {
      try {
        statement.execute("INSERT INTO actor VALUES ('" + currentMovie.getAbridged_cast().get(i).getId() + "', '"
            + currentMovie.getAbridged_cast().get(i).getName().replaceAll("'", "''") + "');");
      } catch (JdbcSQLException e) {
        // exception is caught when an actor stars in multiple movies
      }
    }

    // insert the character values
    for (int i = 0; i < currentMovie.getAbridged_cast().size(); i++) {
      try {
        for (int j = 0; j < currentMovie.getAbridged_cast().get(i).getCharacters().size(); j++) {

          statement.execute("INSERT INTO character VALUES ('" + currentMovie.getAbridged_cast().get(i).getId() + "', '"
              + currentMovie.getId() + "', '"
              + currentMovie.getAbridged_cast().get(i).getCharacters().get(j).replaceAll("'", "''") + "');");
        }
      } catch (NullPointerException e) {
        // an exception is caught when an actor plays an insignificant character
        // and isn't listed in the json file
      }
    }
    conn.close();
  }
}
