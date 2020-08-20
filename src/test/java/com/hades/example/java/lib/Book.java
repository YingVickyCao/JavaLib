package com.hades.example.java.lib;

public class Book {
    public String id;
    public String language;
    public String edition;
    public String author;

    @Override
    public String toString() {
        return "BookBean{" +
                "id='" + id + '\'' +
                ", language='" + language + '\'' +
                ", edition='" + edition + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
