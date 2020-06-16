package com.hades.example.java.lib;

public class MemoryCache {

    private static MemoryCache mInstance;

    private boolean mIsRedTheme;

    public static MemoryCache getInstance() {
        if (null == mInstance){
            mInstance  = new MemoryCache();
        }
        return mInstance;
    }
    public boolean isRedTheme() {
        return mIsRedTheme;
    }

    public void useRedTheme(boolean isRedTheme) {
        this.mIsRedTheme = isRedTheme;
    }
}
