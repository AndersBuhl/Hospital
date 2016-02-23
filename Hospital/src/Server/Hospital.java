package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	private Agent agent;

	public Hospital() {
		persons = new HashMap<BigInteger, Person>();
		divisions = new ArrayList<Division>();
		genRecords();
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
			case "agent":
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

	public void readInput(String input) {

	}

	private void genRecords() {
		String filePath = new File("").getAbsolutePath();
		// System.out.println (filePath);
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(filePath + "/data/Records.txt"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!(line.startsWith("//"))) {
					System.out.println(line);
				}
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
