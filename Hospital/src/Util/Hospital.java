package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Hospital {
	
	public Hospital(){
		genRecords();
	}
	
	public void readInput(String input){
		String command = input;
		StringBuilder builder = new StringBuilder();
		Scanner scan = new Scanner(command);
		while(scan.hasNext()){  //Will read to the first whitespace
			builder.append(scan.next());
		}
		System.out.println(builder.toString());
	}
	
	private void genRecords(){
		String filePath = new File("").getAbsolutePath();
        //System.out.println (filePath);
        BufferedReader reader;

        try {          
        	reader = new BufferedReader(new FileReader(filePath + "/data/Records.txt"));
            String line = null;         
            while ((line = reader.readLine()) != null) {
                if (!(line.startsWith("//"))) {
                    System.out.println(line);
                }
            }        
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }                 
	}
	
	
}
