import java.io.File;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.FileInputStream;
import org.apache.jena.query.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;


public class Lab6 {

	public static void main(String[] args) 
	{
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
		try
		{
			Model model= ModelFactory.createDefaultModel();
			model.read("eswc-2008-complete_modified.rdf");
			model.read("eswc-2009-complete_modified.rdf");
			
			String query = "select ?person08 ?person09 ?personName ?paperName where"+
					" {"+
					" ?person08  <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person>."+
					" ?person08 <http://xmlns.com/foaf/0.1/mbox_sha1sum> ?mbox."+
					" ?person08 <http://xmlns.com/foaf/0.1/name> ?personName."+
					" ?person09 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person>."+
					" ?person09 <http://xmlns.com/foaf/0.1/mbox_sha1sum> ?mbox."+
					" {?person08 <http://xmlns.com/foaf/0.1/made> ?pprName."+
					" ?pprName <http://purl.org/dc/elements/1.1/title> ?paperName.}"+
					" union "+
					" {?person09 <http://xmlns.com/foaf/0.1/made> ?pprName."+
					" ?pprName <http://purl.org/dc/elements/1.1/title> ?paperName.}"+
					" filter ( regex(str(?person08), \"http://data.semanticweb.org/person2008\")). "+
					" filter ( regex(str(?person09), \"http://data.semanticweb.org/person2009\")) "+
					" } order by ?personName";
	
			Query q = QueryFactory.create(query);
			QueryExecution qe = QueryExecutionFactory.create(q, model);
			ResultSet result =  ResultSetFactory.copyResults(qe.execSelect());
			
			
			List <QuerySolution> solutionList = ResultSetFormatter.toList(result);
			ResultSetFormatter.out(System.out, result, q);
			
			
			Property sameperson = ResourceFactory.createProperty("http://utdallas.semclass/samePerson");
			
			List<Resource> listOfIndiviuals = new ArrayList<Resource>();
			for(int i = 0; i < solutionList.size(); i++)
			{
				QuerySolution binding = solutionList.get(i);
				Resource person2008 = (Resource) binding.get("person08");
				Resource person2009 = (Resource) binding.get("person09");
				model.add(person2008, OWL.sameAs, person2009);    
				model.add(person2008, DC.creator, "Vignesh"); 
				model.add(person2009, sameperson, person2008); 
				model.add(person2009, DC.creator, "Vignesh");  
			
				if(!listOfIndiviuals.contains(person2009))
					listOfIndiviuals.add(person2009);
				
			}
			
			
			for(int i = 0; i < listOfIndiviuals.size();i++){
				String person08 = "<"+listOfIndiviuals.get(i).getNameSpace() + listOfIndiviuals.get(i).getLocalName()+">";
				
				query = "select ?personName ?paperName where{ "+
						person08+"  <http://utdallas.semclass/samePerson> ?obj. "+
						person08+"  <http://xmlns.com/foaf/0.1/name> ?personName. { "+
						person08+"  <http://xmlns.com/foaf/0.1/made> ?pprName. "+
						" ?pprName <http://purl.org/dc/elements/1.1/title> ?paperName.}"+
						" union "+
						" {?obj <http://xmlns.com/foaf/0.1/made> ?pprName."+
						" ?pprName <http://purl.org/dc/elements/1.1/title> ?paperName.}"+
						" } order by ?personName";
				q = QueryFactory.create(query);
				qe = QueryExecutionFactory.create(q, model);
				result =  ResultSetFactory.copyResults(qe.execSelect());
				
				ResultSetFormatter.out(System.out, result, q);
			}
						
			
						StringBuilder sb = new StringBuilder();
						sb.append("[owlsam:  (?x ?p ?y), (?x owl:sameAs ?z) -> (?z ?p ?y)] ");
					    sb.append("[owlsam2: (?x owl:sameAs ?y) -> (?y owl:sameAs ?x)] ");

					    List<Rule> rules = new ArrayList<Rule>();
					    rules.add(Rule.parseRule(sb.toString()));
					    Reasoner reasoner = new GenericRuleReasoner(rules);
					    					    
					   
						InfModel model1 = ModelFactory.createInfModel(reasoner, model);
						
						
						
						for(int i = 0; i < listOfIndiviuals.size();i++){
							String person08 = "<"+listOfIndiviuals.get(i).getNameSpace() + listOfIndiviuals.get(i).getLocalName()+">";
							query = "select ?personName ?paperName  where{ "+
									person08+"  <http://xmlns.com/foaf/0.1/made> ?pprName. "+
									person08+"  <http://xmlns.com/foaf/0.1/name> ?personName. "+
									" ?pprName <http://purl.org/dc/elements/1.1/title> ?paperName. }"
									+ " order by ?personName";
							q = QueryFactory.create(query);
							qe = QueryExecutionFactory.create(q, model1);
							result =  ResultSetFactory.copyResults(qe.execSelect());
							ResultSetFormatter.out(System.out, result, q);
						
						}
				
						FileWriter write = new FileWriter("Lab6_VVIJAYAKUMAR.N3");
						model1.write(write,"N3");

			
		
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}

	
	
	
	
}