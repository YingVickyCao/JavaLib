package com.hades.example.java.lib;

public final class Log {
    public static void d(String tag, String msg) {
        System.out.println(tag + ":" + msg);
    }

    public static void e(String tag, String msg) {
        System.err.println(tag + ":" + msg);
    }

}
