package com.ecs.sign.base.common.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

/**
 * 作者：RedKeyset on 2019/11/14 16:36
 * 邮箱：redkeyset@aliyun.com
 */
public class FileUtils {
    private static String Tag = "FileUtils";

    /**
     * 创建文件夹
     */
    public static String createFolder(String folder) {
        String path = null;
        //获取SD卡的路径
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.e(Tag, "获取SD卡的路径:" + path);
        }

        File Folder = new File(path, folder);

        if (!Folder.exists())//判断文件夹是否存在，不存在则创建文件夹，已经存在则跳过
        {
            Folder.mkdir();//创建文件夹
            //两种方式判断文件夹是否创建成功
            //Folder.isDirectory()返回True表示文件路径是对的，即文件创建成功，false则相反
            boolean isFilemaked1 = Folder.isDirectory();
            //Folder.mkdirs()返回true即文件创建成功，false则相反
            boolean isFilemaked2 = Folder.mkdirs();

            if (isFilemaked1 || isFilemaked2) {
                Log.e(Tag, "创建文件夹成功");
                return path + "/" + folder;
            } else {
                Log.e(Tag, "创建文件夹失败");
                return null;
            }

        } else {
            Log.e(Tag, "文件夹已存在");
            return path + "/" + folder;
        }
    }


    /**
     * 清空文件夹中 所有文件
     * ！慎用
     *
     * @param fileAbsolutePath
     * @return
     */
    public static Vector<String> clearFolder(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                Log.e("eee", "文件名 ： " + filename);
                subFile[iFileLength].delete();
                Log.e("eee", "删除 文件名 ： " + filename);
            }
        }
        return vecFile;
    }


    /**
     * 计算百分比
     *
     * @param num   被除数
     * @param total 除数 总数
     * @param scale 精确小数点几位
     * @return
     */
    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num) + "%";
    }


    /**
     * 获取单个文件的MD5值
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    //字符串、json 写入文件
    public static void writeStringToFile(String json, String filePath) {
        File txt = new File(filePath);
        if (!txt.exists()) {
            try {
                txt.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] bytes = json.getBytes(); //新加的
        int b = json.length(); //改
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txt);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
