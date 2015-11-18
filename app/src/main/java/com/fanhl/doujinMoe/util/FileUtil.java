package com.fanhl.doujinMoe.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by fanhl on 15/11/17.
 */
public class FileUtil {
    public static final String TAG = FileUtil.class.getSimpleName();

    /**
     * 仅用于读取小文件(json文件)
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) return null;

        InputStream ins = null;
        try {
            ins = new FileInputStream(file);
            byte b[] = new byte[(int) file.length()];
            ins.read(b);

            return new String(b);
        } catch (Exception e) {
            Log.d(TAG, "读取文件失败.");
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }

    /**
     * 将文本(json小文本)
     *
     * @param file
     * @param text
     * @return
     */
    static boolean writeFile(File file, String text) {
        return writeFile(file, text.getBytes());
    }

    private static boolean writeFile(File file, byte[] bytes) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "保存文件失败.");
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
