package kz.edu.nu.cs.teaching;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class App {
    static final String h = "~~~~~~~~~~~~\n";
    
    public static void main(String[] args) {
        
        System.out.println("\nTask 1 " + h);
        wordcount(getTestLinesStream());
        
        System.out.println("\nTask 2 " + h);
        filterByWordLength(getTestLinesStream());

        System.out.println("\nTask 3 " + h);
        groupWordsByFirstCharacter(getTestLinesStream());
    }
    
    /*
     * Task 1, count words in entire file
     */
    public static void wordcount(Stream<String> stream) {
        // complete method
        long streamCount = stream.flatMap(line -> Stream.of(line.split(" ")))
                .count();
        System.out.println("There are " + streamCount + " words in the stream");

    }
    
    /*
     * Task 2, filter lines by lengths of longest words
     */
    public static void filterByWordLength(Stream<String> stream) {
        // complete method
        stream.filter( line -> Stream.of(line.split(" ")).filter(word -> word.length() > 7).count() > 0 )
                .forEach(System.out::println);
    }
    
    /*
     * Task 3, group words by first character
     */
    public static void groupWordsByFirstCharacter(Stream<String> stream) {
        // complete method
         Map result = stream.flatMap(line -> Stream.of(line.split(" ")))
                .collect(Collectors.groupingBy(word -> word.charAt(0)));
         System.out.println(result);
    }
    
    /*
     * Return Stream of lines from file
     */
    public static Stream<String> getTestLinesStream() {
        File file = new File("lambtest.txt");

        try {
            return Files.lines(file.toPath());
        } catch (Exception e) {
            System.out.println("Error reading from file");
            return null;
        }
    }
    
    
}
