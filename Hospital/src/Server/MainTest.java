package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import Util.Agent;
import Util.Doctor;
import Util.Hospital;
import Util.Patient;
import Util.Person;


public class MainTest {

	public static void main(String[] args) throws IOException {
		System.out.println("Test Begins");
		Hospital host = new Hospital();
		while(true){
//			Scanner in = new Scanner(System.in);
			PrintWriter pOut = new PrintWriter(System.out, true);
			BufferedReader bIn = new BufferedReader(new InputStreamReader(System.in));
			pOut.println("Type command");
			String s = bIn.readLine();
			Person p = new Agent("THE GOOD DOC", "agent", "tpo13dat", null);
			Person p1 = new Doctor("THE GOOD DOC", "head", "5218", null);
			Person p2 = new Patient("THE GOOD PAT", "3201", null);
			host.readInput(s,p, bIn, pOut);
		}
	}
}
