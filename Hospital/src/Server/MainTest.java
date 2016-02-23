package Server;

import java.util.Scanner;

import Util.Doctor;
import Util.Hospital;
import Util.Patient;
import Util.Person;


public class MainTest {

	public static void main(String[] args) {
		System.out.println("Test Begins");
		Hospital host = new Hospital();
		while(true){
			Scanner in = new Scanner(System.in);
			System.out.println("Type command");
			String s = in.nextLine();
			Person p = new Doctor("THE GOOD DOC", "head", "4210", null);
			Person p1 = new Doctor("THE GOOD DOC", "head", "5218", null);
			Person p2 = new Patient("THE GOOD PAT", "3201", null);
			host.readInput(s,p);
		}
	}
}
