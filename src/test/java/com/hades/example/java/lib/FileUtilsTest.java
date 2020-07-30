package com.hades.example.java.lib;

import org.junit.Test;

public class FileUtilsTest {

    @Test
    public void unzip(){
        String zipFilePath = "zip_dir/full.zip";
        String destDir = "zip_dir/full";
        try {
            new FileUtils().unzip(zipFilePath, destDir, true);
        } catch (Exception exception) {
            System.err.println(exception);
        }
    }
}