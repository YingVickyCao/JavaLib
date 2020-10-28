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

    // TODO: 2020/10/28
    public void zip() {

    }

    /**
     * Convert bytes to a file.
     *
     * @param bytes    bytes of the file
     * @param fileDir e.g.,  ./tmp
     * @param fileName e.g.,  QRCode_1.png
     */
    public static void saveFile(byte[] bytes, String fileDir, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            FileUtils.checkDestDirIsExist(fileDir);
            /**
             * e.g., ./tmp/QRCode_1.png
             */
            File file = new File(fileDir + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Convert bytes to a file.
     *
     * @param bytes        bytes of the file
     * @param fileFullName e.g.,  ./tmp/QRCode_1.png
     */
    public static void saveFile(byte[] bytes, String fileFullName) {
        String filePath = fileFullName.substring(0, fileFullName.lastIndexOf("/"));
        String fileName = fileFullName.substring(fileFullName.lastIndexOf("/") + 1);
        saveFile(bytes, filePath, fileName);
    }

    public static byte[] getBytesOfFile(String filePath) {
        File file = new File(filePath);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void checkDestDirIsExist(String destDir) throws IOException {
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

    public <T> T convertJsonToBean(String jsonContent, Class<T> t) throws JsonSyntaxException {
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
