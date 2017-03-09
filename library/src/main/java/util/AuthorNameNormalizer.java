package util;

public class AuthorNameNormalizer {

    // hints:
    // - You might try RegEx replace but things might get ugly...
    // - The String method split is useful.
    // - You might find the code simpler later if you use a LinkedList instead of an array.


    public String normalize(String string) {
        throw new RuntimeException("not yet implemented");
    }

    // See http://stackoverflow.com/questions/275944/java-how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
    // ... if you need to convert to < Java 8
   /* private */ long count(String string, char c) {
        return string.chars().filter(ch -> ch == c).count();
    }
}

