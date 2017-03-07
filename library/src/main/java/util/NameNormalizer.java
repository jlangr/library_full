package util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NameNormalizer {

    // hints:
    // - You might try RegEx replace but things might get ugly...
    // - The String method split is useful.
    // - You might find the code simpler later if you use a LinkedList instead of an array.

    public String normalize(String fullName) {
        String[] parts = fullName.trim().split(" ");
        if (parts.length <= 1)
            return fullName;

        String lastName = lastName(parts);
        String firstName = firstName(parts);
        String middleInitials = middleInitials(parts);

        return format(firstName, lastName, middleInitials);
    }

    private String format(String firstName, String lastName, String middleInitials) {
        return lastName + ", " + firstName + middleInitials;
    }

    private String middleInitials(String[] parts) {
        if (parts.length <= 2)
            return "";
        return " " + Arrays.stream(parts)
                .skip(1)
                .limit(parts.length - 2)
                .map(this::initial)
                .collect(Collectors.joining(" "));
    }

    private String initial(String part) {
        if(part.length() == 1)
            return part;
        return String.valueOf(part.charAt(0)) + ".";
    }

    private String firstName(String[] parts) {
        return parts[0];
    }

    private String lastName(String[] parts) {
        return parts[parts.length - 1];
    }

    // See http://stackoverflow.com/questions/275944/java-how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
    // ... if you need to convert to < Java 8
   /* private */ long count(String string, char c) {
        return string.chars().filter(ch -> ch == c).count();
    }
}

