package kz.edu.nu.cs.se.hw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

public class MyKeywordInContext implements KeywordInContext {

    private Set<String> stopWords;
    private Map<String, SortedSet<Integer>> wordsAndLines;
    private String name;

    public MyKeywordInContext(String name, String pathstring) {
        // TODO Auto-generated constructor stub
        this.name = name;
        stopWords = new HashSet<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("./stopwords.txt"));
            String line = reader.readLine();
            while(line != null) {
                stopWords.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to load stopwords. Reason: " + e.getMessage());
        }
        wordsAndLines = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(pathstring));
            String line = reader.readLine();
            String[] splittedLine;
            int currentLine = 1;
            while(line != null) {
                splittedLine = line.split("[\\p{Punct}\\s]+");
                for(String word : splittedLine) {
                    if(wordsAndLines.containsKey(word)) {
                        wordsAndLines.get(word).add(currentLine);
                    } else {
                        SortedSet<Integer> newSet = new TreeSet<>();
                        newSet.add(currentLine);
                        wordsAndLines.put(word, newSet);
                    }
                }
                currentLine++;
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to load the text file. Reason: " + e.getMessage());
        }
    }

    @Override
    public int find(String word) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Indexable get(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void txt2html() {
        // TODO Auto-generated method stub

    }

    @Override
    public void indexLines() {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeIndexToFile() {
        // TODO Auto-generated method stub

    }

}
