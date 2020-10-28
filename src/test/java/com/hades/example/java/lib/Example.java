package com.hades.example.java.lib;

import org.junit.Test;

public class Example {
    @Test
    public void test() {
        String fileFullName = "./abc/123.png";
        String filePath = fileFullName.substring(0, fileFullName.lastIndexOf("/"));
        String fileName = fileFullName.substring(fileFullName.lastIndexOf("/") + 1);
        System.out.println(filePath);
        System.out.println(fileName);
    }
}
