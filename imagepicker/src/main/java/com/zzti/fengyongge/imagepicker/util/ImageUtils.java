package com.zzti.fengyongge.imagepicker.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author fengyongge
 */
public final class ImageUtils {


    /**
     * imageloder下载图片
     * @param context
     * @param mImageUrl
     */
    public static void downloadImage(final Context context, String mImageUrl) {
        ImageLoader.getInstance().loadImage(mImageUrl,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        File image_file;

                        try {
                            image_file = new File(Environment.getExternalStorageDirectory(), "imagepicker");
                            if (!image_file.exists()) {
                                image_file.mkdirs();
                            }else{

                                if (Environment.getExternalStorageState().equals(
                                        Environment.MEDIA_MOUNTED)) {
                                    image_file = new File(
                                            Environment.getExternalStorageDirectory()+"/imagepicker",
                                            (Math.round((Math.random() * 9 + 1) * 100000)) + ".jpg");

                                } else {

                                    image_file = new File(
                                            "/data/data/com.zzti.fsuper/imagepicker", (Math.round((Math.random() * 9 + 1) * 100000)) + ".jpg");
                                    if (!image_file.exists()) {
                                        image_file.mkdirs();
                                    }
                                }

                            }

                            FileOutputStream out = new FileOutputStream(image_file);

                            if (loadedImage != null) {
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            }
                            out.flush();
                            out.close();

                            loadedImage.recycle();
                            loadedImage = null;
                            FileUtils.updateGallery(context, image_file);

                        } catch (Exception e) {
                            LogUtils.log("保存失败："+e.getMessage());
                        }

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
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

    /**
     * 根据路径，生成bitmip
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0){
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        newOpts.inJustDecodeBounds = false;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


    /**
     * 压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        Bitmap c_bitmap = null ;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            c_bitmap = BitmapFactory.decodeStream(isBm, null, null);
            return c_bitmap;
        } catch (Exception e) {
            return c_bitmap;
        }

    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
