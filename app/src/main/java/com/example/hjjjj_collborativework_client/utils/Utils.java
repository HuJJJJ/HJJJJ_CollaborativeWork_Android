package com.example.hjjjj_collborativework_client.utils;

import android.content.Context;
import android.provider.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String getDeviceNo(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    ///获取文件名
    public static String splitFileName(String path) {
        int lastIndex = path.lastIndexOf("/");
        if (lastIndex != -1) {
            return path.substring(lastIndex + 1);
        }
        return null;
    }

    public static long getFileTotalBlocks(File file, long blockSize) {
        if (file == null) {
            return 0;
        }
        long length = file.length();
        return (long) Math.ceil(length / (double) blockSize);
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) (value & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[3] = (byte) ((value >> 24) & 0xFF);
        return src;
    }

    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset] & 0xFF)
                | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000)
                | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }

    public static byte[] intToUnsignedByteArray(int number) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((number >> 24) & 0xff);
        bytes[1] = (byte) ((number >> 16) & 0xff);
        bytes[2] = (byte) ((number >> 8) & 0xff);
        bytes[3] = (byte) (number & 0xff);
        return bytes;
    }

    /**
     * 合并某个文件夹下的所有文件
     *
     * @param dirpath  文件夹路径
     * @param savePath 最终文件路径
     **/
    public static void MergeFile(String dirpath, String savePath) throws IOException {

        //需要合并的文件list
        List<String> fileList = GetAllFile(dirpath);
        fileList = ListUtils.ByNumberSort(fileList);

        //最终文件
        File outputfile = new File(savePath);
        FileOutputStream fos = new FileOutputStream(outputfile);

        //缓冲区
        byte[] buffer = new byte[4 * 1024 * 1024];
        int bytesRead;

        for (String file : fileList) {

            FileInputStream fis = new FileInputStream(file);
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fis.close();
        }
        fos.flush();
        fos.close();
    }

    /**
     * 获取某个文件夹下的所有文件
     *
     * @param dirpath 文件夹路径
     **/
    public static List<String> GetAllFile(String dirpath) {
        try {
            File dir = new File(dirpath);
            List<String> allFileList = new ArrayList<>();
            // 判断文件夹是否存在
            if (!dir.exists()) {
                System.out.println("目录不存在");
                return null;
            }
            // 获取文件列表
            File[] fileList = dir.listFiles();
            assert fileList != null;
            for (File file : fileList) {
                if (file.isDirectory()) {
                    // 递归处理文件夹
                    // 如果不想统计子文件夹则可以将下一行注释掉
                    //getAllFile(path);
                } else {
                    // 如果是文件则将其加入到文件数组中
                    allFileList.add(file.getPath());
                }
            }
            return allFileList;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 删除文件夹和文件夹下的所有文件
     **/
    public static Boolean deleteFile(File file) {
        System.gc();

        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件是否存在以及文件路径是否正确");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFile(f);
            } else {
                //文件删除
                boolean res = f.delete();
                //打印文件名
                System.out.println("文件名：" + f.getName());
            }
        }
        //文件夹删除
        file.delete();
        System.out.println("目录名：" + file.getName());
        return true;
    }

    /// <summary>
    /// 计算进度条
    /// </summary>
    /// <param name="PassCount"></param>
    /// <param name="allCount"></param>
    /// <returns></returns>
    public static double ExecPercent(double PassCount, double allCount)
    {
        double num = 0;
        if (allCount > 0)
        {
            num = ChinaRound((double)Math.round(PassCount / allCount * 100), 0);
        }
        return num;
    }
    private static double ChinaRound(double value, int decimals)
    {
        if (value < 0)
        {
            return Math.round(value + 5 / Math.pow(10, decimals + 1));
        }
        else
        {
            return Math.round(value);
        }
    }


}
