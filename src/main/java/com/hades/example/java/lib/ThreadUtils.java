package com.hades.example.java.lib;

public class ThreadUtils {
    public static String getThreadInfo() {
        return ",[thread =" + Thread.currentThread().getId() + "," + Thread.currentThread().getName() + "]";
    }
}
