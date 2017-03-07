package util;

public class RomanConverter {
    static String convert(int arabic) {
        Object[][] conversions = {
                {"X", 10},
                {"I", 1}
        };
        String roman = "";
        for (int i = 0; i < conversions.length; i++) {
            String romanDigit = (String) conversions[i][0];
            int arabicDigit = (int) conversions[i][1];
            while (arabic >= arabicDigit) {
                roman += romanDigit;
                arabic -= arabicDigit;
            }
        }
        return roman;
    }
}
