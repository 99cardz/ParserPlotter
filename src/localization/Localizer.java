package localization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Localizer {
	private final int DEFAULT;
	public static final int GERMAN = 0;
	public static final int ENGLISH = 1;
	private static final int AVAILABLE_LANGUAGES = 2;
	public int currentLanguage;
	
	private HashMap<String, String[]> translationMap = new HashMap<String, String[]>();
	private ArrayList<Localizable> components = new ArrayList<Localizable>();
	
	public Localizer(String path, int defaultLanguage) {
		DEFAULT = defaultLanguage;
		currentLanguage = DEFAULT;
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
			System.out.println("Translation file not found");
		} catch (IOException e2) {
			System.out.println(e2.getMessage());
			System.out.println("Formatfehler.");
		}
	}

	public void register(Localizable l) {
		components.add(l);
		l.update();
	}
	
	public String getTranslation(String key) {
		String ret = translationMap.get(key)[currentLanguage];
		if(ret == null)
			return translationMap.get(key)[DEFAULT];
		return ret;
	}
	
	public void updateLanguage(int language) {
		if (language >= AVAILABLE_LANGUAGES || language < 0)
			currentLanguage = DEFAULT;
		this.currentLanguage = language;
		for (Localizable l: components)
			l.update();
	}
}
