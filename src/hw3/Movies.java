package hw3;

import java.util.List;

//A movie object to store the list of movies; consider a single movies object to be one json file
public class Movies {

  private int total;
  private List<Movie> movies; // list of all movies in one json file

  // getter and setters
  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<Movie> getmovies() {
    return movies;
  }

  public void setMovie(List<Movie> movies) {
    this.movies = movies;
  }

}
