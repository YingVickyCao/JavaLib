package com.hades.example.java.lib;

import org.junit.Test;

import java.io.InputStream;

public class FileUtilsTest {

    @Test
    public void unzip() {
        String zipFilePath = "zip_dir/abc.zip";
        String destDir = "zip_dir/abc";
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

            String json = fileUtils.convert(inputStream);
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

    @Test
    public void saveFile() {
        String content = "QR Code Example 苹果梨hellopgfh@123.com";
        FileUtils.saveFile(content.getBytes(), "./tmp/string.txt");
    }

    @Test
    public void getBytesOfFile() {
        byte[] bytes = FileUtils.getBytesOfFile("./src/test/resources/string.txt");
        String content = new String(bytes);
        // QR Code Example 苹果梨hellopgfh@123.com
        System.err.println(content);
    }
}