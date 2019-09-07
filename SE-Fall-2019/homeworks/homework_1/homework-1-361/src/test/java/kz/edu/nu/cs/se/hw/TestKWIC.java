package kz.edu.nu.cs.se.hw;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class TestKWIC {
    KeywordInContext kwic;
    
    @Before
    public void setUp() {
        kwic = new MyKeywordInContext("frankensteinsample", "frankensteinsample.txt");
        kwic.txt2html();
        kwic.indexLines();
    }
    
    @Test
    public void test_1() {
        assertTrue("Found word: quitted", kwic.find("quitted") > -1);
    }
    
    @Test
    public void test_2() {
        assertTrue("Did not find word: tree", kwic.find("tree") == -1);
    }
    
    @Test
    public void test_3() {
        assertTrue("Found word: quitted (entered uppercase)", kwic.find("QUITTED") > -1);
    }
    
    @Test
    public void test_4() {
        int i = kwic.find("darkness");
        Indexable item = kwic.get(i);
        
        assertTrue("darkness on line 1", item.getLineNumber() == 1);
    }
    
    @Test
    public void test_5() {
        Indexable item = kwic.get(kwic.find("copet"));
        
        assertTrue("copet on line 11", item.getLineNumber() == 11);
    }
    
    @Test
    public void test_6() {
        Indexable item = kwic.get(kwic.find("fire"));
        
        assertTrue("fire on line 5", item.getLineNumber() == 5);
    }
}
