package Util;

public abstract class Person {
	private Division division;
	private String name;
	private String id;
	
	public Person(Division division, String name) {
		this.division = division;
		this.name = name;
	}

	public Division getDivision() {
		return division;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Person) {
			return id.equals(((Person)o).id);
		}
		return false;
	}
}
