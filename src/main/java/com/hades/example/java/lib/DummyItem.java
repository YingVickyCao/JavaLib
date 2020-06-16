package com.hades.example.java.lib;

import java.io.Serializable;

/**
 * A dummy item representing a piece of content.
 */
public class DummyItem implements Serializable {
    public final int id;
    public final String colo2;
    public final int col3;

    public DummyItem(int _id, String content, int details) {
        this.id = _id;
        this.colo2 = content;
        this.col3 = details;
    }

    public int getId() {
        return id;
    }

    public String getColo2() {
        return colo2;
    }

    public int getCol3() {
        return col3;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", colo2='" + colo2 + '\'' +
                ", col3='" + col3 + '\'' +
                '}';
    }
}
