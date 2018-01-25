package util;

import java.util.*;

public class Roman {
	static Map<Integer,String> conversions = new LinkedHashMap<>();
	static {
		conversions.put(1000, "M");
		conversions.put(900, "CM");
		conversions.put(500, "D");
		conversions.put(400, "CD");
		conversions.put(100, "C");
		conversions.put(90, "XC");
		conversions.put(50, "L");
		conversions.put(40, "XL");
		conversions.put(10, "X");
		conversions.put(9, "IX");
		conversions.put(5, "V");
		conversions.put(4, "IV");
		conversions.put(1, "I");
	}

	public static String convert(int arabic) {
		StringBuilder buffer = new StringBuilder();
		for (Map.Entry<Integer, String> entry: conversions.entrySet()) {
			int arabicDigit = entry.getKey();
			String romanDigit = entry.getValue();
			while (arabic >= arabicDigit) {
				buffer.append(romanDigit);
				arabic -= arabicDigit;
			}
		}
		return buffer.toString();
	}
}
