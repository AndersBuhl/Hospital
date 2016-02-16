package Util;

public class Division {
	private String divisionName;
	
	public Division(String divisionName) {
		this.divisionName = divisionName;
	}
	
	public boolean equlas(Object o) {
		if(o instanceof Division) {
			return divisionName.equals(((Division)o).divisionName);
		}
		
		return false;
	}
}
