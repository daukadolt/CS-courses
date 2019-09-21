package kz.edu.nu.cs.exercise;

import java.util.List;

public class PagedHelper {
    private List<String> list;
    private String next;
    private String prev;
    
    public List<String> getList() {
        return list;
    }
    public void setList(List<String> list) {
        this.list = list;
    }
    public String getNext() {
        return next;
    }
    public void setNext(String next) {
        this.next = next;
    }
    public String getPrev() {
        return prev;
    }
    public void setPrev(String prev) {
        this.prev = prev;
    }
    
}
