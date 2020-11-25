package com.zzti.fengyongge.imagepicker.ImageloderListener;

import android.content.Context;
import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载对外API
 * @author fengyongge
 * GitHub:https://github.com/fengyongge/imagepicker
 */
public final class Imageloder {

    private ImageloderListener imageloderListener = new ImageloderImpl();

    private Imageloder(){

    }

    private static class SingleInstance{
        private static final Imageloder INSTANCE = new Imageloder();
    }

    public static Imageloder getInstance(){
        return SingleInstance.INSTANCE;
    }


    public void loadImageUrl(String url, ImageView imageView){
        imageloderListener.loadImageUrl(url, imageView);
    }


    public void loadImageFile(File file, ImageView imageView){
        imageloderListener.loadImageFile(file, imageView);
    }


    public void asyncDownloadImage(Context context, String url, String savePath, String saveFileName, ImageDownloadListener listener){
        imageloderListener.asyncDownloadImage(context, url, savePath, saveFileName, listener);
    }


}
