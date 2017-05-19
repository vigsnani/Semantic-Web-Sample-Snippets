
import java.io.*;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Logger.*;
public class Lab1_4d {
	static String defaultNameSpace = "http://utdallas/semclass/edu";
	
	public static void main(String[] args) {
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);

		Dataset dataset=TDBFactory.createDataset("MyDatabases/Dataset1");
		 Model model=dataset.getNamedModel("myrdf");
		
	try{
		dataset.begin(ReadWrite.WRITE);
		String personURI="http://utdallas/semclass#KevenLAtes";
		String fullName="Keven L. Ates";
		String email="atescomp@utdallas.edu";
		String position="Parttime Lecturer";
		String bdate=" April 1, 1901";
		Property p=model.createProperty(personURI+"p");
		
		Resource k=model.createResource(personURI).addProperty(VCARD.FN,fullName)
					                                    .addProperty(VCARD.EMAIL,email)
					                                    .addProperty(VCARD.TITLE,position)
					                                    .addProperty(VCARD.BDAY, bdate);
		
		k.addProperty(p,"Lab 1 ",XSDDatatype.XSDstring);
		dataset.commit();
		dataset.end();
	}
	catch(Exception e)
	{
		
	}
		
		
		try{
			dataset.begin(ReadWrite.READ);
			InputStream inFoafInstance = FileManager.get().open("FOAFDATA.rdf");
			model.read(inFoafInstance, defaultNameSpace);
			dataset.commit();
			}
			catch (Exception ex) 
			{
			
			}
			finally
			{
			dataset.end();
			}
			
		
			dataset.begin(ReadWrite.WRITE);
			model=dataset.getNamedModel("myrdf");
		try{
					
			FileOutputStream fileoutputStream = new FileOutputStream("Lab1_4_VVijayakumar.xml");
			FileOutputStream fileOutputStream1= new FileOutputStream("Lab1_4_VVijayakumar.ntp");
			FileOutputStream fileOutputStream2 = new FileOutputStream("Lab1_4_VVijayakumar.n3");
			model.write(fileOutputStream2, "N3");
			model.write(fileOutputStream1,"N-TRIPLE");
			model.write(fileoutputStream);
			dataset.commit();
			}
			catch (Exception ex) 
			{
			
			}
			finally
			{
			dataset.end();
			}
		
	}
}