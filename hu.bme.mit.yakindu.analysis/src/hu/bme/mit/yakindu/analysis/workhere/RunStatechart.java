package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		print(s);
		boolean running = true;
		String command="";
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
		while(running) {
			command =  reader.readLine();
			switch(command) {
				case "exit":
					running = false;
					break;
				case "start":
					s.raiseStart();
					s.runCycle();
					break;
				case "white":
					s.raiseWhite();
					s.runCycle();
					break;
				case "black":
					s.raiseBlack();
					s.runCycle();
					break;
				case "grey":
					s.raiseGrey();
					s.runCycle();
					break;
				default: break;
			}
			print(s);
		}
		System.out.println("System terminated.");
		System.exit(0);
	}
	public static void print( IExampleStatemachine s ) {
 	System.out.println("W = " + s.getSCInterface().getWhiteTime());
 	System.out.println("B = " + s.getSCInterface().getBlackTime());
 	System.out.println("G = " + s.getSCInterface().getGreyTime());
	}
}