package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Util.Agent;
import Util.Doctor;
import Util.Nurse;
import Util.Patient;
import Util.Person;

public class Hospital {
	private HashMap<BigInteger, Person> persons;
	private ArrayList<Record> records;
	private Agent agent;

	public Hospital() {
		persons = new HashMap<BigInteger, Person>();
		records = new ArrayList<Record>();
		genRecords();
	}

	public void readInput(String input, Person p, BufferedReader in, PrintWriter out) throws IOException {
		String splitter = " ";
		String[] para = input.split(splitter);
		if (input.equals("help")) {
			out.println("Command should look like this: 'operation recordId'");
			out.println("Commands are: read, alter, create, delete");
			return;
		}
		if (para.length <= 1 || para.length > 2 || input.equals("help")) {
			out.println("Faulty prameters in command, type help for commands");
			return;
		}
		String command = para[0];
		String recordId = para[1];
		readCommand(command, recordId, p, in, out); // Input parameters to
													// readCommand
	}

	private void readCommand(String command, String recordId, Person p, BufferedReader in, PrintWriter out)
			throws IOException {
		Record rec = null;
		switch (command) {
		case "read":
			rec = findRecord(recordId);
			if (rec == null) {
				out.println("The requested file: " + recordId + " was not found");
				return;
			}
			String data = null;
			if (agent != null && p.equals(agent.getId())) {
				data = rec.agentData();
			} else {
				data = rec.getRecord(p);
			}
			out.println(data);
			break;
		case "alter":
			rec = findRecord(recordId);
			if (rec == null) {
				out.println("The requested file: " + recordId + " was not found");
				return;
			}
			rec.alterRecord(p, in, out);
			upDateRecord();
			break;
		case "delete":
			out.println("DELETING");
			break;
		case "create":
			out.println("CREATING RECORD");
			String patient;
			String nurse;
			String division;
			String Rdata;
			String RrecordId;
			// Scanner scan = new Scanner(System.in);
			out.println("Type Patient id:");
			out.println("-end");
			patient = in.readLine();
			out.println("Type Nurse id:");
			out.println("-end");
			nurse = in.readLine();
			out.println("Type data about the patient:");
			out.println("-end");
			Rdata = in.readLine();
			out.println("Type record id:");
			out.println("-end");
			RrecordId = in.readLine();

			Record newRec = new Record(patient, p.getId(), nurse, p.getDivision(), Rdata, RrecordId);
			break;

		default:
			out.println("COMMAND NOT FOUND");
			break;
		}
	}

	private Record findRecord(String recordId) {
		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).equals(recordId)) {
				return records.get(i);
			}
		}
		return null;
	}

	public Person login(BigInteger serial, String[] user) {
		Person p = persons.get(serial);
		if (p == null) {
			switch (user[1].toLowerCase()) {
			case "doctor":
				p = new Doctor(user[3], user[2], user[0], serial);
				persons.put(serial, p);
				break;
			case "nurse":
				p = new Nurse(user[3], user[2], user[0], serial);
				persons.put(serial, p);
				break;
			case "agent":
				if (agent == null) {
					agent = new Agent(user[2], user[3], user[0], serial);
					p = agent;
					return agent;
				} else {
					return null;
				}
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
	 * This generates Records from the data file. Only supports format of
	 * patient, doc, nurse, division, data, id...
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
					records.add(new Record(para[0], para[1], para[2], para[3], para[4], para[5]));
				}
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void upDateRecord() {
		String filePath = new File("").getAbsolutePath();
		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter(filePath + "/data/Records2.txt", true)))) {
			for (int i = 0; i < records.size(); i++) {
				out.println(records.get(i).printInfo());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// System.out.println("Save complete \n");
	}
}
