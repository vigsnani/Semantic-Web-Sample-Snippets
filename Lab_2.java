import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.commons.logging.Log;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;

import java.io.*;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;




public class Lab_2{
	public static String defaultnamespace="http://utdallas/semclass#";

public static void main(String []args){
	org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
	 String directory = "MyDatabases/Dataset1" ;
		   Dataset dataset = TDBFactory.createDataset(directory) ;
		   Model m=dataset.getNamedModel("myrdf");
		   try{
			   dataset.begin(ReadWrite.READ);
			 
			   Resource person=m.createResource(defaultnamespace+"PersonClass");
			   
			   Resource Stanley=m.createResource(defaultnamespace+"Stanley").addProperty(RDF.type, person);
			   Stanley.addProperty(VCARD.FN,"Stanley Kubrick");
			   
			   Resource authors= m.createResource(defaultnamespace+"AuthorsClass");
			   
			   Resource Anthony=m.createResource(defaultnamespace+"Anthony").addProperty(RDF.type, authors);
			   Anthony.addProperty(VCARD.FN, "Anthony Burgess");
			  
			   Resource Peter=m.createResource(defaultnamespace+"Peter").addProperty(RDF.type, authors);
			   Peter.addProperty(VCARD.FN, "Peter George");
			  
			  
			   
			   Resource books= m.createResource(defaultnamespace+"BooksClass");
			   
			   Resource Clockwork1=m.createResource(defaultnamespace+"Clockwork Orange").addProperty(RDF.type,books);
			   Clockwork1.addProperty(DC.title, "Clockwork Orange");
			   Clockwork1.addProperty(DC.source, Anthony);
			   
			   Resource RedAlert=m.createResource(defaultnamespace+"Red Alert").addProperty(RDF.type,books);
			   RedAlert.addProperty(DC.title, "Red Alert");
			   RedAlert.addProperty(DC.source, Peter);
			   
			   
			   
			   Resource movies= m.createResource(defaultnamespace+"MoviesClass");
			   Property MovieName=m.createProperty(defaultnamespace+"MovieName");
			   Property MovieYear=m.createProperty(defaultnamespace+"MovieYear");
			   
			   Resource DrStrange=m.createResource(defaultnamespace+"DrStrange").addProperty(RDF.type,movies);
			   DrStrange.addProperty(MovieName, "DrStrangeLove");
			   DrStrange.addProperty(MovieYear, "1964");
			   DrStrange.addProperty(DC.source, RedAlert);
			   DrStrange.addProperty(DC.source,Stanley);
	
			   
			   Resource Clockwork=m.createResource(defaultnamespace+"Clockwork").addProperty(RDF.type, movies);
			   Clockwork.addProperty(MovieName, "Clockwork Orange");
			   Clockwork.addProperty(MovieYear, "1971");
			   Clockwork.addProperty(DC.source,Clockwork1);
			   Clockwork.addProperty(DC.source,Stanley);
			 
			   	dataset.commit();
			     } catch (Exception e) {    
			     
			     }
			  finally
			  {
				  dataset.end();
			  }
		   
		   try{
				  dataset.begin(ReadWrite.WRITE);
				  m=dataset.getNamedModel("myrdf");
				  FileOutputStream fileoutputStream = new FileOutputStream("Lab2_3_VVijayakumar.xml");
				  FileOutputStream fileoutputStream1 = new FileOutputStream("Lab2_3_VVijayakumar.n3");
				  m.write(fileoutputStream);
				  m.write(fileoutputStream1,"N3");
				  dataset.commit();
		   }
		   catch(Exception e){
				  
			  }
			  finally{
				  dataset.end();
			  }
}
}