package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Record {
	private static int nbrRecords = 1;
	private String patient;
	private String doctor;
	private String nurse;
	private String division;
	private String data;
	private String recordId;
	
	public Record(String patient, String doctor, String nurse, String division, String data) {
		this(patient, doctor, nurse, division, data, nbrRecords);		
	}
	
	public Record(String patient, String doctor, String nurse, String division, String data, int recordNbr) {
		this.patient = patient;
		this.doctor = doctor;
		this.nurse = nurse;
		this.division = division;
		this.data = data;
		String id = Integer.toString(recordNbr);
		StringBuilder sb = new StringBuilder();
		
		for(int i = 4; i > id.length(); i--) {
			sb.append('0');
		}
		sb.append(id);
		recordId = sb.toString();
		if(recordNbr > nbrRecords) {
			nbrRecords = recordNbr;
		}
		nbrRecords++;
		
	}
	
	
	public String getRecord(Person p) {
		if(p.equals(patient) || p.equals(nurse) || p.equals(doctor) || p.getDivision().toLowerCase().equals(division.toLowerCase())){
			return data;
		}
		return null;
	}
	
	public String agentData() {
		return data;
	}
	public boolean alterRecord(Person p, BufferedReader in, PrintWriter out) throws IOException{
		if(p.equals(doctor) || p.equals(nurse)){
			out.println(data);
			out.println("Type the new data:");
			out.println("-end");
			String s = in.readLine();
			StringBuilder sb = new StringBuilder();
			sb.append(data);
			sb.append("\t");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
			Date date = new Date();
			sb.append("[" + format.format(date) + "] ");
			sb.append(s);
			data = sb.toString();
			out.println("change complete");
			return true;
		} else {
			out.println("PERMISSION DENIED");
			return false;
		}
	}
	public boolean equals(String s) {
		if(s.equals(recordId)) {
			return true;
		}
		return false;
	}
	
	public String printInfo(){
		StringBuilder str = new StringBuilder();
		str.append(patient + ":");
		str.append(doctor + ":");
		str.append(nurse + ":");
		str.append(division + ":");
		str.append(data + ":");
		str.append(recordId);
		return str.toString();
	}
	
	public String getId() {
		return recordId;
	}
	
}
