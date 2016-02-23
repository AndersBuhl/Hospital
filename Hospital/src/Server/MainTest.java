package Server;

import java.util.Scanner;

import Util.Hospital;


public class MainTest {

	public static void main(String[] args) {
		System.out.println("Test Begins");
		Hospital host = new Hospital();
		
		Scanner in = new Scanner(System.in);
		System.out.println("Type command");
		String s = in.nextLine();
		in.close();
		host.readInput(s);
	}
}
