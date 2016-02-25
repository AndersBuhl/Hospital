package Util;

import java.math.BigInteger;

public class Doctor extends Person {

	public Doctor(String name, String division, String id, BigInteger serial) {
		super(division, name, id, serial);
	}

	@Override
	public String type() {
		return "doctor";
	}

}
