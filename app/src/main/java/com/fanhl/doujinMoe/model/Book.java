package com.fanhl.doujinMoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanhl on 15/11/5.
 */
public class Book {
    public String token;
    public String name;
    public int    count;
    public String rating;
    public String date;

    public List<Page> pages;
    /*当前看到的页码*/
    public int        position;
    public boolean    downloaded;

    public Book() {
        pages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Book(" + name + "," + token + ")";
    }
}
