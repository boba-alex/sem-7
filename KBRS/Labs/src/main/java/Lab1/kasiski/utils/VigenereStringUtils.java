package Lab1.kasiski.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class VigenereStringUtils {


     //Remove spaces, Remove all non alphabetical characters, Lowercase all characters.
    public static String normalizePlainText(final String plaintText) {
        return plaintText.replaceAll("[^A-Za-z]+", "").toLowerCase();
    }

    public static Map<String, Long> countRepetitionByCharacters(final String text) {
        return Arrays.stream(text.split(""))
                .filter(c -> !c.isEmpty())
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    /**
     * Given a {@code text}, it keeps only one character every {@code leap} value.
     * Example:
     *  - With text: abcdefghijkl
     *  - With startAt: 0
     *  - With leap: 3
     *  output= adgj
     * we keep character at index 0, 3, 6, 9.
     *
     * Sounds weird, but it allow picking one character every X characters.
**/
    public static String peekAndLeap(final int leap, final int startAt, final CharSequence text) {
        final StringBuilder sb = new StringBuilder();
        for (int stringIndex = startAt; stringIndex < text.length(); stringIndex += leap) {
            sb.append(text.charAt(stringIndex));
        }
        return sb.toString();
    }

}
