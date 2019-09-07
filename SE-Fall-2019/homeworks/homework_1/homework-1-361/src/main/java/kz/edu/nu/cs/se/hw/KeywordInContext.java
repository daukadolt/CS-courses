package kz.edu.nu.cs.se.hw;

public interface KeywordInContext {
    
    /**
     * The location of the word in the KWIC.  Returns -1 if word is not in index. 
     * If word appears multiple times, then return the first location in the KWIC.   
     * 
     * @param word the search term
     * @return location of specified word
     */
    public int find(String word);
    
    /**
     * Get <code>Indexable</code> with index <code>i</code> from the KWIC.  
     * 
     * @param i index
     * @return entry at index i
     */
    public Indexable get(int i);
    
    /**
     * Convert the text to an HTML file. The KWIC will link to the HTML file
     * created by this method.  See Example.  
     */
    public void txt2html();
    
    /**
     * Build the Keyword in Context Index.  
     */
    public void indexLines();
    
    /**
     * Write the Keyword in Context Index to an HTML file. See Example.
     */
    public void writeIndexToFile();
}
