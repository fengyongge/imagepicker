package com.zzti.fengyongge.imagepicker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 压缩方法
 * @author fengyongge
 * GitHub:https://github.com/fengyongge/imagepicker
 */
public final class CompressUtils {

    /**
     * 根据采样率压缩图片
     * @param srcPath
     * @return
     */
    public static Bitmap compressBitmapDecodeFile(String srcPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(srcPath,options);
        options.inJustDecodeBounds = true;
        //现在主流手机分辨率
        int reqHeight = 1920;
        int reqWidth= 1080;
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,options);
        return compressByQuality(bitmap,100);
    }


    /**
     * 计算图片采样率
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int inSampleSize = 1;
        final int width = options.outWidth;
        final int height = options.outHeight;
        //获取宽高最小比例作为inSampleSize值
        if(width > reqWidth || height > reqHeight){
            final int widthRatio = Math.round((float)width/(float)reqWidth);
            final int heightRatio = Math.round((float)height/(float)reqHeight);
            inSampleSize = widthRatio > heightRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }


    /**
     * 根据质量压缩图片
     * @param image
     * @param quality
     * @return
     */
    public static Bitmap compressByQuality(Bitmap image,int quality) {
        Bitmap c_bitmap = null ;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            int options = 100;
            //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            while ( baos.toByteArray().length / 1024>100) {
                //重置baos即清空baos
                baos.reset();
                //这里压缩options%，把压缩后的数据存放到baos中
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
                //每次都减少10
                options -= 10;
            }
            //把压缩后的数据baos存放到ByteArrayInputStream中
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            c_bitmap = BitmapFactory.decodeStream(isBm, null, null);
            return c_bitmap;
        } catch (Exception e) {
            return c_bitmap;
        }
    }

    /**
     * 根据图片尺寸
     * @param bitmap
     * @param size
     */
    public static void compressBySize(Bitmap bitmap,int size) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / size, bitmap.getHeight() / size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / size, bitmap.getHeight() / size);
        canvas.drawBitmap(bitmap, null, rect, null);
        compressByQuality(result, 100);
    }


    /**
     * 复制图片
     */
    public static String getCropImagePath(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            File takeImageFile = FileUtils.getCreatFilePath();
            FileUtils.writeImage(bitmap, takeImageFile.getAbsolutePath(), 100);
            return takeImageFile.getAbsolutePath();
        }
    }

}
