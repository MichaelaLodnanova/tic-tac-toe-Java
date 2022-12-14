package cz.muni.fi.pb162.hw01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Utility methods available to be used in your solution
 */
public final class Utils {

    private Utils() {
        // Intentionally made private to prevent instantiation
    }

    /**
     * Coppies all elements from all given arrays into destination array
     *
     * @param dest destination array
     * @param arrays source arrays
     * @return destination array
     */
    public static Character[][] flattenArrays(Character[][] dest, Character[][]... arrays) {
        var totalLength = Stream.of(arrays).map( it -> it.length).reduce(0, Integer::sum);
        if (dest.length < totalLength) {
            throw new IndexOutOfBoundsException("Destination array has insufficient length");
        }

        var destPos = 0;
        for (var source : arrays) {
            System.arraycopy(source, 0, dest, destPos, source.length);
            destPos += source.length;
        }
        return dest;
    }

    /**
     * Throws an error with given message
     * @param message error message
     */
    public static void error(String message) {
        throw new RuntimeException(message);
    }

    /**
     * Reads a single line from stdin
     * @return read line
     */
    public static String readLineFromStdIn() {
        var scanner =  new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Finds integers from input to be parsed
     * @param stringToSearch represent input
     * @return list of integers
     */
    public static List<String> findPositiveIntegers(String stringToSearch) {
        Pattern integerPattern = Pattern.compile("\\d+");
        Matcher matcher = integerPattern.matcher(stringToSearch);

        List<String> integerList = new ArrayList<>();
        while (matcher.find()) {
            integerList.add(matcher.group());
        }

        return integerList;
    }

}
