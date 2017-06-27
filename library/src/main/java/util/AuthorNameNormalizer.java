package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorNameNormalizer {

    private Set<String> names = new HashSet<>();

    public String normalizeName(String authorName) {
        names.add(authorName);
        String withoutSuffix = removeSuffix(authorName);
        String[] nameParts = withoutSuffix.trim().split(" ");
        if (nameParts.length == 1)
           return authorName;
        return lastName(nameParts) + ", " +
               firstName(nameParts) +
               middleInitials(nameParts) +
                suffix(authorName);
    }

    private String suffix(String authorName) {
        String[] parts = authorName.split(",");
        if (parts.length == 1) return "";
        return "," + parts[1];
    }

    private String removeSuffix(String authorName) {
        String[] parts = authorName.split(",");
        if (parts.length == 1) return authorName;
        return parts[0];
    }

    private String middleInitials(String[] nameParts) {
        if (nameParts.length == 2) return "";

        return " " + Arrays.stream(nameParts)
                .skip(1)
                .limit(nameParts.length - 2)
                .map(this::middleInitial)
                .collect(Collectors.joining(" "));
    }

    private String middleInitial(String namePart) {
        if (namePart.length() == 1) return namePart;
        return namePart.charAt(0) + ".";
    }

    private String firstName(String[] nameParts) {
        return nameParts[0];
    }

    private String lastName(String[] nameParts) {
        return nameParts[nameParts.length - 1];
    }

    /* private */ long count(String string, char c) {
        return string.chars().filter(ch -> ch == c).count();
    }

    public int countOfNames() {
        return names.size();
    }
}

