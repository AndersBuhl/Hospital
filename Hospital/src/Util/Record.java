package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Record {
	private static int nbrRecords = 0;
	private int recordNbr;
	private String patient;
	private String doctor;
	private String nurse;
	private String division;
	private String data;
	private String recordId;
	
	/*
	 Changed Record from objects to strings which make reading in possible
	 */
	public Record(String patient, String doctor, String nurse, String division, String data, String recordId) {
		this(patient, doctor, nurse, division, data, recordId, nbrRecords);		
	}
	
	public Record(String patient, String doctor, String nurse, String division, String data, String recordId, int recordNbr) {
		this.patient = patient;
		this.doctor = doctor;
		this.nurse = nurse;
		this.division = division;
		this.data = data;
		this.recordNbr = recordNbr;
		this.recordId = recordId;
		nbrRecords++;
		
	}
	
	
	public String getRecord(Person p) {
		if(p.equals(patient) || p.equals(nurse) || p.equals(doctor) || p.getDivision().toLowerCase().equals(division.toLowerCase())){
			return data;
		}
		return "PERMISSION DENIED";
	}
	
	public String agentData() {
		return data;
	}
	public void alterRecord(Person p, BufferedReader in, PrintWriter out) throws IOException{
		if(p.equals(doctor) || p.equals(nurse)){
			out.println(data);
			out.println("Type the new data:");
			out.println("-end");
			String s = in.readLine();
			data = s;
			out.println("change complete");
		} else {
			out.println("PERMISSION DENIED");
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
	
}
