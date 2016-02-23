package Util;

import java.math.BigInteger;

public abstract class Person {
	private Division division;
	private String name;
	private String id;
	private BigInteger serial;
	
	public Person(Division division, String name, String id, BigInteger serial) {
		this.division = division;
		this.name = name;
		this.id = id;
		this.serial = serial;
	}

	public Division getDivision() {
		return division;
	}
	
	public BigInteger serial() {
		return serial;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Person) {
			return id.equals(((Person)o).id);
		}
		return false;
	}
}
