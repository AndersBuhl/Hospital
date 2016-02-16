package Util;

public class Record {
	private static int nbrRecords = 0;
	private int recordNbr;
	private Patient patient;
	private Doctor doctor;
	private Nurse nurse;
	private Division division;
	private String data;
	
	public Record(Patient patient, Doctor doctor, Nurse nurse, Division division, String data) {
		this(patient, doctor, nurse, division, data, nbrRecords);		
	}
	
	public Record(Patient patient, Doctor doctor, Nurse nurse, Division division, String data, int recordNbr) {
		this.patient = patient;
		this.doctor = doctor;
		this.nurse = nurse;
		this.division = division;
		this.data = data;
		this.recordNbr = recordNbr;
		nbrRecords++;
		
	}
	public String getRecord(Person p) {
		if(p.equals(patient) || p.equals(nurse) || p.equals(doctor) || p.getDivision().equals(division))
			return data;
		
		return null;
	}
	
}
