package com.zzti.fengyongge.imagepicker.util;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * 通知系统相册更新
 * @author fengyongge
 * GitHub:https://github.com/fengyongge/imagepicker
 */
final class NotifyUtils {

        public static void refreshSystemPic(Context context, File destFile) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                insertPicInAndroidQ(context, destFile);
            } else {
                ContentValues value = new ContentValues();
                value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                value.put(MediaStore.Images.Media.DATA, destFile.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);
                Uri contentUri = FileProviderUtil.getFileUri(context,
                        destFile, context.getPackageName() + ".fileprovider");
                context.sendBroadcast(
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
            }
        }


        /**
         * Android Q以后向系统相册插入图片
         */
        @RequiresApi(Build.VERSION_CODES.Q)
        private static void insertPicInAndroidQ(Context context,  File insertFile) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DESCRIPTION, insertFile.getName());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, insertFile.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.TITLE, "Image.jpg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");

            Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver resolver = context.getContentResolver();
            Uri insertUri = resolver.insert(external, values);
            BufferedInputStream inputStream = null;
            OutputStream os = null;
            try {
                try {
                    inputStream = new BufferedInputStream(new FileInputStream(insertFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (insertUri != null) {
                    os = resolver.openOutputStream(insertUri);
                }
                if (os != null) {
                    int read;
                    byte[] buffer = new byte[1024 * 4];
                    while ((read = inputStream.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    os.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
