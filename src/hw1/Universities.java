package hw1;
/*
 * Main Class: Universities
 * Date last Modified: 1/18/2018 @ 8:27PM EST
 * Version: 1.0
 * 
 */

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement (name="University-Data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Universities {
  
  @XmlElementWrapper(name = "Universities")
  @XmlElement(name="University")
  private List<University> universityList;
  
  public List<University> getUniversityList() {
    return universityList;
  }

  public void setUniversityList(List<University> universityList) {
    this.universityList = universityList;
  }

  public static void main(String[] args) throws 
    JAXBException, JsonParseException, JsonMappingException, IOException {
    
    //Object mapper for JSON file
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    University uniArray[] = mapper.readValue(new File("src/data/schools.json"), University[].class);

    //Creates a list of universities for the various universities in the files we have/want
    List<University> universities = Arrays.asList(uniArray);
    Universities unis = new Universities();
    unis.setUniversityList(universities);

    //Begins to create the XML file
    JAXBContext context = JAXBContext.newInstance(Universities.class);
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    // Write to File
    marshaller.marshal(unis, new File("src/data/schools.xml"));
  }


  

}
