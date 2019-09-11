package kz.edu.nu.cs.se.hw.kwic;

import java.io.*;
import java.util.*;

public class MyKeywordInContext implements KeywordInContext {

    private Set<String> stopWords;
    private ArrayList<String> txtLines;
    private ArrayList<Indexable> indexes;
    private Map<String, ArrayList<Integer>> wordToIndexable;
    private String pathToFile;
    private String name;


    public static void main(String[] args) {
        KeywordInContext my = new MyKeywordInContext("name", "book.txt");
        my.txt2html();
        my.indexLines();
        my.writeIndexToFile();
    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

    public MyKeywordInContext(String name, String pathstring) {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.pathToFile = pathstring;
        readStopWords();
    }

    private void readStopWords() {
        BufferedReader reader;
        stopWords = new HashSet<>();
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
    }

    private void generateLines(String pathstring) {
        BufferedReader reader;
        txtLines = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(pathstring));
            String line = reader.readLine();
            while(line != null) {
                txtLines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to read txt file. Reason: " + e.getMessage());
        }
    }

    @Override
    public int find(String word) {
        // TODO Auto-generated method stub
        word = word.toLowerCase();
        return this.wordToIndexable.containsKey(word) ? this.wordToIndexable.get(word).get(0) : -1;
    }

    @Override
    public Indexable get(int i) {
        // TODO Auto-generated method stub
        return this.indexes.get(i);
    }

    @Override
    public void txt2html() {
        // TODO Auto-generated method stub
        File anchoredHTML = new File("myKWIC_output/"+this.name+".html");
        txtLines = new ArrayList<>();
        try {
            anchoredHTML.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(anchoredHTML));
            bw.write("<!DOCTYPE html>\n");
            bw.write("<head> <meta charset=\"UTF-8\"> </head>\n");
            bw.write("<div>\n");
            BufferedReader reader = new BufferedReader(new FileReader(this.pathToFile));
            String line = reader.readLine();
            int currentLine = 1;
            while(line != null) {
                bw.write(line);
                bw.write("<span id=\"line_"+ currentLine +"\">&nbsp&nbsp[" + currentLine + "]</span><br>\n");
                currentLine++;
                line = reader.readLine();
            }
            bw.write("</div>\n");
            bw.write("</html>\n");
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to create anchoredHTML. Reason: " + e.getMessage());
        }
    }

    @Override
    public void indexLines() {
        // TODO Auto-generated method stub
        generateLines(pathToFile);
        this.indexes = new ArrayList<>();
        this.wordToIndexable = new TreeMap<>();
        int currentLine = 1;
        for(String line: txtLines) {
            String[] splittedLine;
            String word;
            splittedLine = line.split("[\\p{Punct}\\s]+");
            for (int i = 0; i<splittedLine.length; i++) {
                word = splittedLine[i].toLowerCase();
                if (stopWords.contains(word) || word.length() == 0) {
                    continue;
                }
//                System.out.println(word);
                if (!wordToIndexable.containsKey(word)) {
                    Indexable anIndex = new MyIndexable(word, currentLine);
                    indexes.add(anIndex);
                    ArrayList<Integer> listOfLines = new ArrayList<>();
                    listOfLines.add(indexes.size()-1);
                    wordToIndexable.put(word, listOfLines);
                } else if( !(this.indexes.get(wordToIndexable.get(word).get(wordToIndexable.get(word).size()-1)).getLineNumber() == currentLine) ) {
                    Indexable anIndex = new MyIndexable(word, currentLine);
                    indexes.add(anIndex);
                    wordToIndexable.get(word).add(indexes.size()-1);
                }
            }
            currentLine++;
        }
    }

    @Override
    public void writeIndexToFile() {
        // TODO Auto-generated method stub
        File kwicHTML = new File("myKWIC_output/kwic-"+this.name+".html");
        try {
            kwicHTML.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(kwicHTML));
            bw.write("<DOCTYPE html>\n");
            bw.write("<html><head><meta charset=\"UTF-8\"></head><body><div style=\"text-align:center;line-height:1.6\">\n");
            for(Map.Entry <String, ArrayList<Integer>> entry: wordToIndexable.entrySet()) {
                for(int index: entry.getValue()) {
                    Indexable currentIndex = indexes.get(index);
                    String[] splittedLine = txtLines.get(currentIndex.getLineNumber()-1).split("[\\p{Punct}\\s]+");
                    for(String word: splittedLine) {
                        String lowerWord = word.toLowerCase();
                        if(lowerWord.equals(entry.getKey())) {
                            bw.write("<a href=\""+this.name+".html#line_"+currentIndex.getLineNumber()+"\">"+lowerWord.toUpperCase()+"</a>");
                            bw.write(" ");
                        } else {
                            bw.write(word);
                            bw.write(" ");
                        }
                    }
                    bw.write("<br>\n");
                }
            }
            bw.write("</div>\n");
            bw.write("</html>\n");
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to create kwicHTML. Reason: " + e.getMessage());
        }
    }

}
