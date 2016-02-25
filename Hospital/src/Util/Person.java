package Util;

import java.math.BigInteger;

public abstract class Person {
	private String division;
	private String name;
	private String id;
	private BigInteger serial;
	
	public Person(String division, String name, String id, BigInteger serial) {
		this.division = division;
		this.name = name;
		this.id = id;
		this.serial = serial;
	}

	public String getDivision() {
		return division;
	}
	
	public BigInteger serial() {
		return serial;
	}

	public boolean equals(Object o) {
		if(o instanceof String) {
			return id.equals(o);
		}
		return false;
	}
	public String getId(){
		return id;
	}
	
	public abstract String type();
	
	public String printInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(serial + ":");
		sb.append(id + ":");
		sb.append(type() + ":");
		sb.append(division + ":");
		sb.append(name);
		
		return sb.toString();
	}
}
