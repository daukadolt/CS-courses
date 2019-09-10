package kz.edu.nu.cs.se.hw;

public class MyIndexable implements Indexable {

    private String entry;
    private int lineNumber;

    public MyIndexable(String entry, int lineNumber) {
        this.entry = entry;
        this.lineNumber = lineNumber;
    }

    @Override
    public String getEntry() {
        return this.entry;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    public String toString() {
        return this.entry;
    }
}
