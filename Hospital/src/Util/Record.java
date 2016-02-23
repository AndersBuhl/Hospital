package Util;

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
		if(p.equals(patient) || p.equals(nurse) || p.equals(doctor) || p.getDivision().equals(division))
			return data;
		
		return null;
	}
	
	public String printInfo(){
		StringBuilder str = new StringBuilder();
		str.append(patient + " ");
		str.append(doctor + " ");
		str.append(nurse + " ");
		str.append(division + " ");
		str.append(data + " ");
		str.append(recordId + " ");
		return str.toString();
	}
	
}
