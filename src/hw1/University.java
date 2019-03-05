package hw1;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class University {

  //fields for the various information found in schools.json, and the fields that we want in schools.xml
  private String id;
  private String about;
  private String founded;
  private int likes;
  private String link;
  
  //creates a location object, due to this field containing multiple sub-fields
  private Position location; 
  
  private String name;
  private int talking_about_count;
  private String username;
  private String website;
  private int were_here_count;
  
  //general getters and setters for the above fields
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getAbout() {
    return about;
  }
  public void setAbout(String about) {
    this.about = about;
  }
  public String getFounded() {
    return founded;
  }
  public void setFounded(String founded) {
    this.founded = founded;
  }
  public int getLikes() {
    return likes;
  }
  public void setLikes(int likes) {
    this.likes = likes;
  }
  public String getLink() {
    return link;
  }
  public void setLink(String link) {
    this.link = link;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Position getLocation() {
    return location;
  }
  public void setLocation(Position location) {
    this.location = location;
  }
 
  public int getTalking_about_count() {
    return talking_about_count;
  }
  public void setTalking_about_count(int talking_about_count) {
    this.talking_about_count = talking_about_count;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getWebsite() {
    return website;
  }
  public void setWebsite(String website) {
    this.website = website;
  }
  public int getWere_here_count() {
    return were_here_count;
  }
  public void setWere_here_count(int were_here_count) {
    this.were_here_count = were_here_count;
  }
  
  
  
  
  
}
