package hw3;

import java.util.List;

public class Movie {

  // Values for movie
  private String id;
  private String title;
  private int year;
  private String mpaa_rating;
  private Ratings ratings; // rating is an object in json file
  private List<Actor> abridged_cast; // list of the actors in a specific movie

  // getters and setters for the values
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getMpaa_rating() {
    return mpaa_rating;
  }

  public void setMpaa_rating(String mpaa_rating) {
    this.mpaa_rating = mpaa_rating;
  }

  public Ratings getRatings() {
    return ratings;
  }

  public void setRatings(Ratings ratings) {
    this.ratings = ratings;
  }

  public List<Actor> getAbridged_cast() {
    return abridged_cast;
  }

  public void setAbridged_cast(List<Actor> abridged_cast) {
    this.abridged_cast = abridged_cast;
  }

}
