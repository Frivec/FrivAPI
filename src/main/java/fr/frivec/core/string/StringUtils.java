package fr.frivec.core.string;

public class StringUtils {
	
	public static boolean compareStringAbsolute(final String str1, final String str2) {
		
		if(str1.equals(str2))
			
			return true;
		
		return false;
		
	}
	
	public static boolean compareStringRelative(final String str1, final String str2) {
	
		if(str1.equalsIgnoreCase(str2))
			
			return true;
		
		return false;
		
	}
	
	public static String translateColorSymbol(String text) {
		
		return text.replaceAll("[&]", "§");
		
	}

}
