package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Hospital {
	private HashMap<BigInteger, Person> persons;
	private ArrayList<Record> records;
	private Agent agent;
	private String[] divisions = {"head", "knee", "shoulder", "feet", "eye"};

	public Hospital() {
		persons = new HashMap<BigInteger, Person>();
		records = new ArrayList<Record>();
		genRecords();
		loadPersons();
	}
	
	private void loadPersons() {
		String filePath = new File("").getAbsolutePath();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath + "/data/Persons.txt"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!(line.startsWith("//")) && !(line.startsWith(":"))) {
					String splitter = "[:]";
					String[] para = line.split(splitter);
					switch(para[2]) {
					case "doctor":
						persons.put(new BigInteger(para[0]), new Doctor(para[4], para[3], para[1], new BigInteger(para[0])));
						break;
					case "nurse":
						persons.put(new BigInteger(para[0]), new Nurse(para[4], para[3], para[1], new BigInteger(para[0])));
						break;
					case "patient":
						persons.put(new BigInteger(para[0]), new Patient(para[4], para[1], new BigInteger(para[0])));
						break;
					case "agent":
						agent =  new Agent(para[3], para[4], para[1], new BigInteger(para[0]));
						break;
					default:
						break;
					}
				}
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void savePersons() {
		String filePath = new File("").getAbsolutePath();
		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter(filePath + "/data/Persons.txt")))) {
			out.println("//serial:id:title:division:name");
			for (BigInteger b : persons.keySet()) {
				out.println(persons.get(b).printInfo());
			}
			if(agent != null) {
				out.println(agent.printInfo());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public void log(String event, Person p) {
		String filePath = new File("").getAbsolutePath();
		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter(filePath + "/data/log.txt", true)))) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			out.println("[" + format.format(date) + "] " + p.getId() + ": " + event);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void readInput(String input, Person p, BufferedReader in, PrintWriter out) throws IOException {
		String splitter = " ";
		String[] para = input.split(splitter);
		if (input.equals("help")) {
			out.println("Command should look like this: 'operation recordId'");
			out.println("Commands are: read, alter, create, delete");
			return;
		}
		if(input.equals("create")){
			readCommand(para[0], "0", p, in, out);
			return;
		}
		if (para.length <= 1 || para.length > 2) {
			out.println("Faulty parameters in command, type 'help' for commands");
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
		StringBuilder sb = new StringBuilder();
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
			sb.append("read ");
			
			if(data == null) {
				sb.append("denied ");
				out.println("PERMISSION DENIED");
			} else {
				out.println(data);
			}
			
			sb.append(recordId);
			log(sb.toString(), p);
			break;
		case "alter":
			rec = findRecord(recordId);
			if (rec == null) {
				out.println("The requested file: " + recordId + " was not found");
				return;
			}
			sb.append("alter ");			
			if(!rec.alterRecord(p, in, out)) {
				sb.append("denied ");
			} else {
				upDateRecord();
			}
			sb.append(recordId);
			log(sb.toString(), p);
			break;
		case "delete":
			rec = findRecord(recordId);
			if (rec == null) {
				out.println("The requested file: " + recordId + " was not found");
				return;
			}
			sb.append("deleting ");
			if(!deleteRecord(p, rec)) {
				sb.append("denied ");
			} else {
				upDateRecord();
				out.println("Record was deleted");
			}
			sb.append(recordId);
			log(sb.toString(), p);
			break;
		case "create":
			if(!p.type().equals("doctor")) {
				out.println("PERMISSION DENIED");
				log("create denied", p);
				break;
			}
			out.println("CREATING RECORD");
			String patient;
			String nurse;
			String Rdata;
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
//			out.println("Type record id:");
//			out.println("-end");
//			RrecordId = in.readLine();

			Record newRec = new Record(patient, p.getId(), nurse, p.getDivision(), Rdata);
			records.add(newRec);
			
			out.println("Successfully created record " + newRec.getId());
			upDateRecord();
			log("create record " + newRec.getId(), p);
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
					savePersons();
				}
				p = agent;
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

		log("logged in", p);
		savePersons();
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
					records.add(new Record(para[0], para[1], para[2], para[3], para[4], Integer.parseInt(para[5])));
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
				new BufferedWriter(new FileWriter(filePath + "/data/Records.txt")))) {
			for (int i = 0; i < records.size(); i++) {
				out.println(records.get(i).printInfo());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// System.out.println("Save complete \n");
	}
	public boolean deleteRecord(Person p, Record rec){
			if(p.type().equals("agent")){
				int i = records.indexOf(rec);
				records.remove(i);
				return true;
			}
		return false;
	}
}
