package hw3;

import java.util.List;

public class Actor {

  // values for actor
  private String name;
  private String id;
  private List<String> characters;

  // getters and setters for the values
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getCharacters() {
    return characters;
  }

  public void setCharacters(List<String> characters) {
    this.characters = characters;
  }

}
