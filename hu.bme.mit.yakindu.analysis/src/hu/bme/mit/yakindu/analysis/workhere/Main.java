package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

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
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		ArrayList<String> trapStates = new ArrayList<String>();
		ArrayList<String> endTransitions = new ArrayList<String>();
		ArrayList <String> noNameStates = new ArrayList<String>();
		String startName ="";
		String endName;
		EList<Transition> transitions;
		
		boolean isTrap = false;
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				startName = state.getName();
				if(startName == null || startName =="") {
					startName = "NoName" + noNameStates.size();
					noNameStates.add(startName);
					System.out.println("State without name, recommending name: " + startName);
				}
				else
					System.out.println(startName);
	
				transitions= state.getOutgoingTransitions();
				isTrap=true;
				for (Transition e : transitions) {
					if(!e.getTarget().getName().equals(state.getName())) {
						isTrap=false;
						break;
					}
				}
				
				if(transitions.isEmpty() || isTrap== true) 
					trapStates.add(startName);					 
			}
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				
				endName = transition.getTarget().getName();
				//start node
				if(startName == "") {
					System.out.println("State without name, Recommending name: Start");
					startName = "Start";
				}
				//end node
				if(endName == null) {
					endName= "End";
					endTransitions.add("  "+ startName + " -> "+ endName);
				}
				if(endName=="") {
					endName = "NoName";
				}
					System.out.println("  "+ startName + " -> " + endName);
			}
		
		}
		if(!endTransitions.isEmpty()) {
			System.out.println("State without name, recommending name: End");
			endTransitions.forEach(e->{System.out.println(e);});
		}
		System.out.println("\nTrap States: ");
		trapStates.forEach(e->{ System.out.println("  "+e); });
		System.out.println("No name states:");
		noNameStates.forEach(e->{System.out.println("  " +e);});
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}