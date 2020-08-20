package com.hades.example.java.lib;

import java.util.List;

public class Words {
    public List<Book> book;

    @Override
    public String toString() {
        return "WordsBean{" +
                "book=" + book.toString() +
                '}';
    }
}
