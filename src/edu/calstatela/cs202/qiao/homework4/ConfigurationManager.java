package edu.calstatela.cs202.qiao.homework4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConfigurationManager {
	
	private Scanner scanner;	
	private String[] keyValueArray;
	private final int ARRAYSIZE = 2000;
	private int keySize = 0;

	ConfigurationManager() {}
	
	ConfigurationManager(File file) {
		try {
			scanner = new Scanner(file); 
			keyValueArray = new String[ARRAYSIZE];		
			while (scanner.hasNext()) {
				String line = scanner.nextLine();			
				try {
					if ((line.length() != 0) && (line.charAt(0) != '#')){	
						String[] dataArray = line.split("\\s*=\\s*");
						push(dataArray[0], dataArray[1]);
					}
				} catch (Exception e) {
				}					
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		}
	}
	
	ConfigurationManager(String filename) {
		this(new File(filename));
	}
	// accessors
	int getSize() {
		return keySize;
	}	
	String getString( String key ) {	
		
		for (int i = 0; i < ARRAYSIZE; i++){
			if (keyValueArray[i].equals(key)){
				return keyValueArray[i+1];
			}				
		}	
		return null;
	}	
	int getInteger( String key ) {
		String value = getString(key);
		if (value != null){
			return Integer.parseInt(value);
		}
		return -1;
	}
	// return all keys
	String[] getKeys() {
		String[] keys = new String[keySize];
		for (int i = 0; i < keys.length; i++){		
			keys[i] = keyValueArray[i*2];		
		}	
		return keys;
	} 
	
	// mutators
	void push(String key, String value) { 
		keyValueArray[keySize*2] = key;
		keyValueArray[keySize*2+1] = value;
		keySize++;		
	}
		
	void closeFile(){
		scanner.close();
	}
	
	void save(String outputFile, StringBuilder sb){		
		PrintWriter writer = null;		
		try {
			writer = new PrintWriter(outputFile);
			writer.print(sb);
		} catch (FileNotFoundException e) {
			System.err.println(e);
		}finally{
			writer.close();
		}		
	}
}
