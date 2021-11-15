package com.uniking.tool;

/**
 * Created by wzl on 11/17/20.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * A helper class for compression-related operations
 */
public class ZipUtil {
    static final int BUFFER_SIZE = 2048;
    static final String TAG = "ZipUtil";

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                finally {
                    origin.close();
                }
            }
        }
        finally {
            out.close();
        }
    }

    public static void unzip(String zipFile, String location) throws IOException {
        try {
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + "/" + ze.getName();

                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    }
                    else {
                        File newFile = new File(path);
                        File fileParent = newFile.getParentFile();
                        if(!fileParent.exists()){
                            fileParent.mkdirs();
                        }
                        if(!newFile.exists()){
                            newFile.createNewFile();
                        }
                        FileOutputStream fout = new FileOutputStream(path, false);
                        try {
                            byte[] buffer = new byte[10240];
                            for (int c = zin.read(buffer); c != -1; c = zin.read(buffer)) {
                                fout.write(buffer, 0, c);
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }

    public static void unzip(String zipFile, String fileName, String location) throws IOException {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {

                    if(ze.getName().endsWith("zip"))
                        Log.i(TAG, ze.getName());

                    if(ze.getName().equals(fileName)){
                        FileOutputStream fout = new FileOutputStream(location, false);
                        try {
                            byte[] buffer = new byte[10240];
                            for (int c = zin.read(buffer); c != -1; c = zin.read(buffer)) {
                                fout.write(buffer, 0, c);
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }
}