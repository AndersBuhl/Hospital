package Util;

public class Record {
	private Patient patient;
	private Doctor doctor;
	private Nurse nurse;
	private String data;
	
	public Record(Patient patient, Doctor doctor, Nurse nurse, String data) {
		this.patient = patient;
		this.doctor = doctor;
		this.nurse = nurse;
		this.data = data;
	}
}
