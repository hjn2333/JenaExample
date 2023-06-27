package myJena;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.reasoner.Reasoner;

import java.io.InputStream;

public class Encounter {
    @SuppressWarnings("deprecation")
	public static String encounter() {
        // 加载本体文件
        String ontFile = "C:/Users/Zz/Desktop/D/tensorflow/dqn-multi-agent-rl-master/uav.ttl";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(ontFile);
        if (in != null) {
        	RDFDataMgr.read(model, ontFile);
        } else {
            System.err.println("can not find!");
            System.exit(1);
        }

        // 自定义推理规则
        String rules ="[rule1: "
        		+ "(?a rdf:type <urn:example:uav#obstruct>),"
        		+ "(?x1 rdf:type <urn:example:uav#uav>),"
        		+ "(?x1 <urn:example:uav#encounter> ?a)"
        		+ " -> "
        		+ "(?x1 rdf:type <urn:example:uav#Crash>)]\n"
        		+ "[rule2: "
        		+ "(?a rdf:type <urn:example:uav#interference>),"
        		+ "(?x1 rdf:type <urn:example:uav#uav>),"
        		+ "(?x1 <urn:example:uav#encounter> ?a),"
        		+ " -> "
        		+ "(?x1 rdf:type <urn:example:uav#Disturbed>)]\n";
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        // 进行推理
        Resource crash = model.getResource("urn:example:uav#Crash");
        Resource interference = model.getResource("urn:example:uav#Disturbed");
        StmtIterator stmtIterator1 = infModel.listStatements(null,RDF.type,crash);
        StmtIterator stmtIterator2 = infModel.listStatements(null,RDF.type,interference);
        if (stmtIterator1.hasNext()) {
            return "obstruct";
        }
        
        if (stmtIterator2.hasNext()) {       
            return "interference";
        }
        return "";
    }
    
    public static String inside() {
        String ontFile = "C:/Users/Zz/Desktop/D/tensorflow/dqn-multi-agent-rl-master/uav.ttl";
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(ontFile);
        if (in != null) {
        	RDFDataMgr.read(model, ontFile);
        } else {
            System.err.println("can not find!");
            System.exit(1);
        }
        String rules = "[rule1: "
        		+ "(?a rdf:type <urn:example:uav#station>),"
        		+ "(?x1 rdf:type <urn:example:uav#uav>),"
        		+ "(?x1 <urn:example:uav#encounter> ?a)"
        		+ " -> "
        		+ "(?x1 rdf:type <urn:example:uav#Inside>)]\n";
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);
        
        Resource inside = model.getResource("urn:example:uav#Inside");
        StmtIterator stmtIterator = infModel.listStatements(null,RDF.type,inside);
        if (stmtIterator.hasNext()) {
            return "station";
        }
    	return "";
    }
    public static void main(String[] args) {
		String s = inside();
		System.out.println(s);
	}
}
