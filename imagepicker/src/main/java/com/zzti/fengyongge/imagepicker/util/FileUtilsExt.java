package com.zzti.fengyongge.imagepicker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 保存文件
 *
 * @author fengyongge
 * GitHub:https://github.com/fengyongge/imagepicker
 */
public final class FileUtilsExt {
    /**
     * 创建需要保存的文件
     *
     * @param isUseExternalFilesDir 是否使用getExternalFilesDir,false为保存在sdcard根目录下
     * @param fileName              保存文件名
     * @param folderName            保存在sdcard根目录下的文件夹名（isUseExternalFilesDir=false时需要）
     */

    public static File savaFileUtils(
            Context context,
            Boolean isUseExternalFilesDir,
            String fileName,
            String folderName) {
        String filePath = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            filePath = context.getExternalFilesDir(folderName).getAbsolutePath();
        } else {
            if (isUseExternalFilesDir) {
                filePath =  context.getExternalFilesDir(folderName).getAbsolutePath();
            } else {
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }
        if (isUseExternalFilesDir) {
            return new File(filePath, fileName);
        } else {
            File file = new File(filePath, folderName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return new File(file, fileName);
        }
    }


    /**
     * bitmap保存到File
     */
    public static void saveBitmap2File(Context context, Bitmap bitmap, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通知系统图库更新
        NotifyUtils.refreshSystemPic(context, file);
    }
}


