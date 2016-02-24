package Util;

import java.math.BigInteger;

public class Agent extends Person {
	public Agent(String division, String name, String id, BigInteger serial) {
		super(division, name, id, serial);
	}

	@Override
	protected String type() {
		return "agent";
	}	
}
