package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.base.expressions.expressions.IntLiteral;
import org.yakindu.base.expressions.expressions.PrimitiveValueExpression;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		ArrayList<String> variables = new ArrayList<String>();
		ArrayList<String> events = new ArrayList<String>();
		String tempString;
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;
				events.add(event.getName());
			}
			if (content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition) content;
				tempString=var.getName();
				variables.add(tempString.substring(0,1).toUpperCase()+tempString.substring(1));
			}
		}
		
		
		tempString = getTop() + "\n";
		for(String event: events) {
			tempString += "				case \""+event+"\":\r\n" + 
					"					s.raise"+event.substring(0,1).toUpperCase()+event.substring(1)+"();\r\n" + 
					"					s.runCycle();\r\n" + 
					"					break;\n";
		}
		tempString += getBottom(variables);
		System.out.println(tempString);
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	private static String getBottom(ArrayList<String> variables) {
		String out= "				default: break;\r\n"+
				"			}\r\n" + 
				"			print(s);\r\n" + 
				"		}\r\n" + 
				"		System.out.println(\"System terminated.\");\r\n" + 
				"		System.exit(0);\r\n" + 
				"	}\n"+
				"	public static void print( IExampleStatemachine s ) {\n";
		for(String e: variables){
			out+=(" 	System.out.println(\""+ e.charAt(0) + " = \" + s.getSCInterface().get" + e+ "());\n");
		};
		out+= "	}\n}";
		return out;
	}
	
	private static String getTop() {
		return "import java.io.BufferedReader;\n" + 
				"import java.io.IOException;\r\n" + 
				"import java.io.InputStreamReader;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		print(s);\r\n" + 
				"		boolean running = true;\r\n" + 
				"		String command=\"\";\r\n" + 
				"		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));\r\n" + 
				"		while(running) {\r\n" + 
				"			command =  reader.readLine();\r\n" + 
				"			switch(command) {\n" +
				"				case \"exit\":\r\n" + 
				"					running = false;\r\n" + 
				"					break;";
	}
	
	
}
