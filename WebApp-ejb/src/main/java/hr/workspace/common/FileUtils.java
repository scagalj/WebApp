/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.workspace.common;

import hr.workspace.models.Attachment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Stjepan
 */
public class FileUtils {

    private final static String DOCUMENT_STORAGE_PATH = "/opt/attachments/documents/";
    private final static String IMAGE_STORAGE_PATH = "/opt/attachments/images/";

    public FileUtils() {
    }

    public static Boolean saveFileToDisk(Attachment att, String path) {
        File file = new File(path + att.getInternalName());
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(att.getData());
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Boolean saveFileToDisk(Attachment att) {
        return saveFileToDisk(att, DOCUMENT_STORAGE_PATH);
    }
    public static Boolean saveImageToDisk(Attachment att) {
        return saveFileToDisk(att, IMAGE_STORAGE_PATH);
    }

    public static Attachment loadFileFromDisk(Attachment att, String path) {
        try {
            byte[] content = Files.readAllBytes(Paths.get(path + att.getInternalName()));
            att.setData(content);
            return att;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Attachment loadFileFromDisk(Attachment att) {
        return loadFileFromDisk(att, DOCUMENT_STORAGE_PATH);
    }
    public static Attachment loadImageFromDisk(Attachment att) {
        return loadFileFromDisk(att, IMAGE_STORAGE_PATH);
        
    }

    public static File fetchFileFromDisk(Attachment att, String path) {
        try {
            File file = new File(path + att.getInternalName());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean deleteFileFromDisk(Attachment att, String path) {
         try {
            File file = fetchFileFromDisk(att, path);
            boolean isDeleted = file.delete();
            return isDeleted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Boolean deleteFileFromDisk(Attachment att) {
        return deleteFileFromDisk(att, DOCUMENT_STORAGE_PATH);
    }
    
    public static Boolean deleteImageFromDisk(Attachment att) {
        return deleteFileFromDisk(att, IMAGE_STORAGE_PATH);
        
    }

}
