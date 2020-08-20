package com.hades.example.java.lib;

import org.junit.Test;

import java.io.InputStream;

public class FileUtilsTest {

    @Test
    public void unzip() {
        String zipFilePath = "zip_dir/full.zip";
        String destDir = "zip_dir/full";
        try {
            new FileUtils().unzip(zipFilePath, destDir, true);
        } catch (Exception exception) {
            System.err.println(exception);
        }
    }

    @Test
    public void readJsonFile() {
        try {
            FileUtils fileUtils = new FileUtils();
            InputStream inputStream = fileUtils.getResourceAsStream("words.json");

            String json = fileUtils.convertStreamToStr(inputStream);
            System.out.println(json);

            Words bean = fileUtils.convertJsonToBean(json, Words.class);
            System.out.println(bean);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void readJsonFile2() {
        try {
            FileUtils fileUtils = new FileUtils();
            String json = fileUtils.readFileAsString("words.json");
            System.out.println(json);

            Words bean = fileUtils.convertJsonToBean(json, Words.class);
            System.out.println(bean);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}