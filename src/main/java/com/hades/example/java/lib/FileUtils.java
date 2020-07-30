package com.hades.example.java.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
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
}
