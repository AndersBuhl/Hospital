package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import Util.Agent;
import Util.Division;
import Util.Doctor;
import Util.Nurse;
import Util.Patient;
import Util.Person;


public class Hospital {
	private HashMap<BigInteger, Person> persons;
	private ArrayList<Division> divisions;
	private ArrayList<Record> records;
	private Agent agent;

	public Hospital() {
		persons = new HashMap<BigInteger, Person>();
		divisions = new ArrayList<Division>();
		records = new ArrayList<Record>();
		genRecords();
	}
	/*
	public void readInput(String input){
		String command = input;
		StringBuilder builder = new StringBuilder();
		Scanner scan = new Scanner(command);
		if(scan.hasNext()){  //Will read to the first whitespace
			builder.append(scan.next());
		}
		command = builder.toString();
		System.out.println(command);
		readCommand(command);
		scan.close();
	}
	*/
	public void readInput(String input){
		String splitter = " ";
    	String[] para = input.split(splitter);
    	if(para.length <= 1 || para.length > 2 || input.equals("help")){
    		System.out.println("Missing prameters in command, type help for commands");
    		return;
    	}
    	String command = para[0];
    	String recordId = para[1];
    	readCommand(command, recordId); //Input parameters to readCommand
	}
	
	public void readCommand(String command, String recordId){
		switch (command) {
		case "read":
			System.err.println("READING FILE");
			break;
		default: 	
			System.err.println("COMMAND NOT FOUND");
			break;
		}
	}
	
	public Person login(BigInteger serial, String[] user) {
		Person p = persons.get(serial);
		if (p == null) {
			Division div = null;
			for(Division d : divisions) {
				if(user[2].equals(d)) {
					div = d;
				}
			}
			if(div == null) {
				System.err.println("Division " + user[2] + " doesn't exist");
				return null;
			}
			switch (user[1].toLowerCase()) {
			case "doctor":
				p = new Doctor(user[3], divisions.get(divisions.indexOf(user[2])), user[0], serial);
				persons.put(serial, p);
				break;
			case "nurse":
				p = new Nurse(user[3], divisions.get(divisions.indexOf(user[2])), user[0], serial);
				persons.put(serial, p);
				break;
			case "agency":
				if (agent == null) {
					agent = new Agent(divisions.get(divisions.indexOf(user[2])), user[3], user[0], serial);
					p = agent;
				} else {
					return null;
				}
				break;
			case "patient":
				p = new Patient(user[3], user[0], serial);
				persons.put(serial, p);
				break;
			default:
				System.err.println("Wrong title");
				return null;
			}
		}
		
		return p;
	}
	/*
	 This generates Records from the data file. Only supports format of
	 patient, doc, nurse, division, data, id...
	 */
	private void genRecords() {
		String filePath = new File("").getAbsolutePath();
        BufferedReader reader;
        try {          
        	reader = new BufferedReader(new FileReader(filePath + "/data/Records.txt"));
            String line = null;         
            while ((line = reader.readLine()) != null) {
                if (!(line.startsWith("//"))) {
                	String splitter = "[:]";
                	String[] para = line.split(splitter);
//                	for(int i = 0; i < para.length; i++){
//                		System.out.println(para[i]);
//                	}
                	records.add(new Record(para[0],para[1],para[2],para[3],para[4],para[5]));
                }
            }        
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
//        System.out.println("OUTCOME");
//        String test = records.get(0).printInfo();
//        String test2 = records.get(1).printInfo();
//        System.out.println(test + "\n" + test2);
	}
}
