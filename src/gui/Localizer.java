package gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Localizer {
	
	public static final int GERMAN = 0;
	public static final int ENGLISH = 1;
	
	private HashMap<String, String[]> translationMap = new HashMap<String, String[]>();
	private HashMap<Object, String> registeredComponents = new HashMap<Object, String>();
	
	public Localizer(String path) {
		String[] errors = {"fehler", "error"};
		translationMap.put("STRING_UNAVAILABLE", errors);
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while((line = br.readLine()) != null) {
				String[] lineAsArr = line.split("\\t");
				String identifier = lineAsArr[0];
				String[] translations = new String[lineAsArr.length-1];
				for (int i = 1; i < lineAsArr.length; i++)
					translations[i-1] = lineAsArr[i];
				translationMap.put(identifier, translations);
			}
		} catch (FileNotFoundException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
			System.out.println("Datei nicht gefunden.");
		} catch (IOException e2) {
			System.out.println(e2.getMessage());
			System.out.println("Formatfehler.");
		}
	}
	
	public void register(Object object, String identifier) {
		if(translationMap.containsKey(identifier)) {
			registeredComponents.put(object, identifier);
		}
 	}
	
	// only for testing
	
	public void printAll() {
		for (String s: translationMap.keySet()) {
			System.out.println(s + ":");
			for (String s1: translationMap.get(s))
				System.out.println(s1);
			System.out.println();
		}
	}
	
	public String[] get(String identifier) {
		return translationMap.get(identifier);
	}
	
	public static void main(String... args) {
		Localizer test = new Localizer("src/gui/translations.txt");
		test.printAll();
	}
}
