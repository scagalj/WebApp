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

    private final static String STORAGE_PATH = "/opt/attachments/";

    public FileUtils() {
    }

    public static Boolean saveFileToDisk(Attachment att) {
        File file = new File(STORAGE_PATH + att.getInternalName());
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(att.getData());
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Attachment loadFileFromDisk(Attachment att) {
        try {

            byte[] content = Files.readAllBytes(Paths.get(STORAGE_PATH + att.getInternalName()));
            att.setData(content);
            return att;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File fetchFileFromDisk(Attachment att) {
        try {
            File file = new File(STORAGE_PATH + att.getInternalName());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean deleteFileFromDisk(Attachment att) {
        try {
            File file = fetchFileFromDisk(att);
            boolean isDeleted = file.delete();
            return isDeleted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
