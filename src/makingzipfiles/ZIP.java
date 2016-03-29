/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package makingzipfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Joco
 */
public class ZIP {

    public static void pack(String sourceFilePath, String targetFilePath) {
        pack(new File(sourceFilePath), targetFilePath);
    }

    public static void pack(File sourceFile, String targetFilePath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFilePath))) {
            if (sourceFile.isDirectory()) {
                if (sourceFile.list() != null && sourceFile.list().length > 0) { // if it is folder and has something in it
                    int pathStartingIndex = sourceFile.getAbsolutePath().length() - sourceFile.getName().length();
                    packMoreFiles(zos, sourceFile.listFiles(), pathStartingIndex);
                } else {
                    addEmptyFolder(zos, sourceFile.getName());
                }
            } else {
                addFile(zos, sourceFile, sourceFile.getName());
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    // Investigates the elements of a folder and adds them to a new zip entry
    private static void packMoreFiles(ZipOutputStream zos, File[] listOfFiles, int pathStartingIndex) throws  IOException {
        for (File currentFile : listOfFiles) {
            if (currentFile.isDirectory()) {
                if (currentFile.list() != null && currentFile.list().length > 0) {
                    packMoreFiles(zos, currentFile.listFiles(), pathStartingIndex);
                } else {
                    addEmptyFolder(zos, currentFile.getAbsolutePath().substring(pathStartingIndex));
                }
            } else {
                addFile(zos, currentFile, currentFile.getAbsolutePath().substring(pathStartingIndex));
            }
        }
    }

    private static void addEmptyFolder(ZipOutputStream zos, String targetName) throws IOException {
        zos.putNextEntry(new ZipEntry(targetName + File.separator));
        zos.closeEntry();
    }

    private static void addFile(ZipOutputStream zos, File sourceFile, String targetFilePath) throws IOException {
        zos.putNextEntry(new ZipEntry(targetFilePath));
        FileInputStream fis = new FileInputStream(sourceFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }
        zos.closeEntry();
    }
}
