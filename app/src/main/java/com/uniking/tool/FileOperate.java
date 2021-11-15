package com.uniking.tool;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by uniking on 17-11-7.
 */

public class FileOperate {
    public static byte[] repairByteLength(byte[] buf, int length)
    {
        if(buf.length <= length)
            return buf;

        byte[] newBuf = new byte[length];
        for(int i=0; i<length; ++i)
            newBuf[i] = buf[i];

        return newBuf;
    }

    public static boolean copyFileFromAssets(Context context, String srcFileName, String desFileName)
    {
        boolean bRtn = false;
        try
        {
            InputStream src = null;
            src = context.getAssets().open(srcFileName);

            do{
                File des = new File(desFileName);
                if (des.exists()) {
                    bRtn = true;
                    break;
                }

                OutputStream sOut = new FileOutputStream(desFileName);

                byte[] tmp = new byte[1024 * 1024];
                int c = 0;
                while ((c = src.read(tmp)) > 0)
                    sOut.write(repairByteLength(tmp, c));

                bRtn = true;
            } while (false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return bRtn;
    }

    public static void copyFile(String srcFileName, String desFileName, long size)
    {
        try {
            do {
                File des = new File(desFileName);
                if (des.exists()) {
                    break;
                }

                File inputFile = new File(srcFileName);
                if(!inputFile.exists()){
                    break;
                }

                FileInputStream fis = null;
                fis = new FileInputStream(inputFile);
                OutputStream sOut = new FileOutputStream(desFileName);

                byte[] buffer = new byte[1024*1024*5];//500k
                int len = 0;
                long writeLen = 0;
                while((len=fis.read(buffer)) != -1){
                    writeLen += len;
                    if(writeLen < size)
                        sOut.write(buffer, 0, len);
                    else if(writeLen == size)
                    {
                        sOut.write(buffer, 0, len);
                        break;
                    }
                    else
                    {//writeLen > size
                        sOut.write(buffer, 0, len - (int)(writeLen -size));
                        break;
                    }
                }

            }while(false);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void copyFile(String srcFileName, String desFileName)
    {
        long size = 0;
        try {
            do {
                File des = new File(desFileName);
                if (des.exists()) {
                    if(!des.delete())
                        break;
                }

                File inputFile = new File(srcFileName);
                if(!inputFile.exists()){
                    break;
                }
                size = inputFile.length();

                FileInputStream fis = null;
                fis = new FileInputStream(inputFile);
                OutputStream sOut = new FileOutputStream(desFileName);

                byte[] buffer = new byte[1024*1024*5];//500k
                int len = 0;
                long writeLen = 0;
                while((len=fis.read(buffer)) != -1){
                    writeLen += len;
                    if(writeLen < size)
                        sOut.write(buffer, 0, len);
                    else if(writeLen == size)
                    {
                        sOut.write(buffer, 0, len);
                        break;
                    }
                    else
                    {//writeLen > size
                        sOut.write(buffer, 0, len - (int)(writeLen -size));
                        break;
                    }
                }

            }while(false);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String readFromAssets(Context app, String srcFileName)
    {
        StringBuffer bRtn = new StringBuffer();
        try
        {
            InputStream src = null;
            src = app.getAssets().open(srcFileName);

            byte[] buf  = new byte[1024];
            while(true) {
                int l = src.read(buf);
                if(l >= 0)
                    bRtn.append(new String(repairByteLength(buf, l)));
                else
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return bRtn.toString();
    }

    public static void updateFile(String fileName, String content)
    {
        try {
            OutputStream sOut = new FileOutputStream(fileName);
            sOut.write(content.getBytes());
            sOut.flush();
            sOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateFile(String fileName, byte[] content)
    {
        try {
            OutputStream sOut = new FileOutputStream(fileName);
            sOut.write(content);
            sOut.flush();
            sOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] subArray(byte[] a, int len)
    {
        if(len>= a.length)
            return a;

        byte[] resA = new byte[len];
        System.arraycopy(a, 0, resA, 0, len);

        return resA;
    }

    public static String readFileExclusive(String srcFileName)
    {
        StringBuffer strBuf = new StringBuffer();
        try {
            do {
                File inputFile = new File(srcFileName);
                if(!inputFile.exists()){
                    break;
                }

                FileInputStream fis = null;
                fis = new FileInputStream(inputFile);

                byte[] buffer = new byte[1024*1024*5];//500k
                int len = 0;
                while((len=fis.read(buffer)) != -1){
                    strBuf.append(new String(subArray(buffer, len)));
                }

                fis.close();
                inputFile = null;
            }while(false);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strBuf.toString();
    }

    public static byte[] readBinaryFile(String srcFileName)
    {
        byte[] content = new byte[0];

        try {
            do {
                File inputFile = new File(srcFileName);
                if(!inputFile.exists()){
                    break;
                }

                content = new byte[(int)inputFile.length()];
                FileInputStream fis = null;
                fis = new FileInputStream(inputFile);

                while(fis.read(content) != -1){
                }

                fis.close();
                inputFile = null;
            }while(false);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return content;
    }

    public static void deleteAllFiles(File root, List<String> ignore) {
        if(ignore == null)
        {
            ignore = new ArrayList<>();
        }

        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f, ignore);
                    try {
                        if(!ignore.contains(f.getAbsolutePath()))
                            f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f, ignore);
                        try {
                            if(!ignore.contains(f.getAbsolutePath()))
                                f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    /**
     * 将流转换成字符串
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String readInputSream(InputStream in) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        in.close();

        return baos.toString();
    }

    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     * @param _type 查询类型，比如mp3什么的
     */
    public static ArrayList<String> getAllFiles(String dirPath, String _type) {
        File f = new File(dirPath);
        if (!f.exists()) {//判断路径是否存在
            return null;
        }

        File[] files = f.listFiles();

        if(files==null){//判断权限
            return null;
        }

        ArrayList<String> fileList = new ArrayList();
        for (File _file : files) {//遍历目录
            if(_file.isFile() &&
                    (_type == null ? true:_file.getName().endsWith(_type))
                    ){
                //String _name=_file.getName();
                String filePath = _file.getAbsolutePath();//获取文件路径
                //int end=_file.getName().lastIndexOf('.');
                //String fileName = _file.getName().substring(0,end);//获取文件名
                fileList.add(filePath);
            } else if(_file.isDirectory()){//查询子目录
                getAllFiles(_file.getAbsolutePath(), _type);
            } else{
            }
        }
        return fileList;
    }

    /*
    目录尾部没有/
     */
    public static void moveFiles(String srcDir, String desDir)
    {
        File ss = new File(srcDir);
        if(!ss.exists())
            return;

        File dd = new File(desDir);
        if(!dd.exists())
            dd.mkdirs();

        if(srcDir.endsWith("/"))
            srcDir = srcDir.substring(0, srcDir.length()-1);
        if(desDir.endsWith("/"))
            desDir = desDir.substring(0, desDir.length()-1);

        ArrayList<String> fileList = getAllFiles(srcDir, null);
        for (String file:fileList)
        {
            copyFile(file, desDir+file.substring(srcDir.length()));
        }

        List<String> ignore = new ArrayList<>();
        deleteAllFiles(new File(srcDir), ignore);
    }

    public static int getFileCount(String dir)
    {
        ArrayList<String> fileList = getAllFiles(dir, null);
        return fileList.size();
    }

    public static void appandFile(String fileName, String content){
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            ;//e.printStackTrace();
        }
    }

    public static long fileTimeDiff(String filepath) {
        File file = new File(filepath);
        long m = file.lastModified();
        Date date = new Date();
        return (date.getTime()-m)/(1000 * 60 * 60 * 24);
    }

    public static void clearFileDiff(String dir, long timediff){
        ArrayList<String> fileList = getAllFiles(dir, null);
        for (String one: fileList ) {
            if(fileTimeDiff(one) >= timediff){
                File file = new File(one);
                if(!file.isDirectory())
                    file.delete();
            }
        }
    }
}
