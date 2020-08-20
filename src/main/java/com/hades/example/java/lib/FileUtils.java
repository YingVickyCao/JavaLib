package com.hades.example.java.lib;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    private Gson _gson;

    /**
     * @param currentZipFilePath zip_dir/full.zip
     * @param destDir            zip_dir/full
     */
    public void unzip(String currentZipFilePath, String destDir, boolean isDeleteZip) {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zipEntry;
        byte[] buffer = new byte[1024];

        try {
            checkDestDirIsExist(destDir);
            fis = new FileInputStream(currentZipFilePath);
            zis = new ZipInputStream(fis);
            while ((zipEntry = zis.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File newFile = new File(destDir + File.separator + fileName);
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());
                    createDirsForSubDirsInZip(newFile);
                    continue;
                }
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                createDirsForSubDirsInZip(newFile);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
            }

            if (isDeleteZip) {
                deleteDir(currentZipFilePath);
            }
            zis.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != zis) {
                    zis.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkDestDirIsExist(String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void deleteDir(String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            System.err.println("deleteDir, path is not exist");
            return;
        }
        if (!dir.delete()) {
            System.err.println("deleteDir, fail. path=" + path);
        }
    }

    public void createDirsForSubDirsInZip(File newFile) throws IOException {
        new File(newFile.getParent()).mkdirs();
    }

    /**
     * @param jsonFileName json in (src/main/resources/ or src/test/resources/)
     */
    public InputStream getResourceAsStream(String jsonFileName) {
        return getClass().getClassLoader().getResourceAsStream(jsonFileName);
//        return TestJson.class.getClassLoader().getResourceAsStream(jsonFileName);
    }

    public String convertStreamToStr(InputStream inputStream) {
        if (null == inputStream) {
            System.err.println("inputStream = null");
            return null;
        }
        ByteArrayOutputStream result = null;
        try {
            result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }

                if (null != result) {
                    result.close();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    public  <T> T convertJsonToBean(String jsonContent, Class<T> t) throws JsonSyntaxException {
        if (null == _gson) {
            _gson = new Gson();
        }
        return _gson.fromJson(jsonContent, t);
    }

    public String readFileAsString(String file) throws Exception {
        URL url = getClass().getClassLoader().getResource(file);
        return new String(Files.readAllBytes(Paths.get(url.getPath())));
    }
}
