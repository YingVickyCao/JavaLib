package com.hades.example.java.lib;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    private static FileUtils mInstance;

    public FileUtils() {
    }

    public static FileUtils getInstance() {
        if (null == mInstance) {
            mInstance = new FileUtils();
        }
        return mInstance;
    }

    private Gson _gson;

    /**
     * @param currentZipFilePath path/full.zip
     * @param destDir            path/full
     */
    public void unzip(String currentZipFilePath, String destDir, boolean isDeleteZip) {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zipEntry = null;
        byte[] buffer = new byte[1024];

        try {
            checkDestDirIsExist(destDir);
            fis = new FileInputStream(currentZipFilePath);
            zis = new ZipInputStream(fis);
            while ((zipEntry = zis.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
//                    File newFile = new File(destDir + File.separator + fileName);
                    File newFile = new File(destDir, fileName);
                    checkPath(newFile, destDir);
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());
                    createDirsForSubDirsInZip(newFile);
                    continue;
                }
                File newFile = new File(destDir, fileName);
                checkPath(newFile, destDir);
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
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    // Zip Path Traversal Vulnerability
    private void checkPath(File out, String destDir) throws Exception {
        String canonicalPath = out.getCanonicalPath();
        System.out.println("Check isHaveZipPathIssuer: " + canonicalPath + "," + destDir);

        if (!canonicalPath.startsWith(new File(destDir).getCanonicalPath())) {
            throw new Exception(String.format("Zip path Traversal Vulnerability with %s", canonicalPath));
        }
    }

    /**
     * Convert bytes to a file.
     *
     * @param bytes    bytes of the file
     * @param fileDir  e.g.,  ./tmp
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
        saveFile(bytes, splitFilePath(fileFullName), splitFileName(fileFullName));
    }

    /**
     * @param fileFullName e.g.,  ./tmp/QRCode_1.png
     * @return e.g.,  ./tmp
     */
    public static String splitFilePath(String fileFullName) {
        if (null == fileFullName || fileFullName.isEmpty()) {
            return null;
        }
        return fileFullName.substring(0, fileFullName.lastIndexOf("/"));
    }

    /**
     * @param fileFullName e.g.,  ./tmp/QRCode_1.png
     * @return e.g.,  QRCode_1.png
     */
    public static String splitFileName(String fileFullName) {
        if (null == fileFullName || fileFullName.isEmpty()) {
            return null;
        }
        return fileFullName.substring(fileFullName.lastIndexOf("/") + 1);
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

    public String convert(InputStream inputStream) {
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
                inputStream.close();

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

    public String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }

        System.out.println("hashKeyForDisk: key=" + key + ",cacheKey=" + cacheKey);
        return cacheKey;
    }
}
