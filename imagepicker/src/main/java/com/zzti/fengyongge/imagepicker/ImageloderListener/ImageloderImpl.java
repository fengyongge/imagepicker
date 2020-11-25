package com.zzti.fengyongge.imagepicker.ImageloderListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zzti.fengyongge.imagepicker.R;
import com.zzti.fengyongge.imagepicker.util.FileUtilsExt;

import java.io.File;

/**
 * 图片加载实现方法
 * @author fengyongge
 * GitHub:https://github.com/fengyongge/imagepicker
 */
public final class ImageloderImpl implements ImageloderListener {

    public RequestOptions getRequestOptions() {
        RequestOptions options =new RequestOptions();
        //优先级设置
        options.priority(Priority.HIGH)
                //设置占位图
                .placeholder(R.drawable.ic_picture_loading)
                //设置错误图片
                .error(R.drawable.ic_picture_loading)
                //url为null
                .fallback(R.drawable.ic_picture_loadfailed)
                //指定图片大小
//                .override(width, height)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }


    @Override
    public void loadImageUrl(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(getRequestOptions())
                .into(imageView);
    }

    @Override
    public void loadImageFile(File file, ImageView imageView) {
        Glide.with(imageView.getContext()).load(file)
                .apply(getRequestOptions())
                .into(imageView);
    }

    @Override
    public void asyncDownloadImage(final Context context, String url, final String savePath, final String saveFileName, final ImageDownloadListener listener) {
        Glide.with(context)
                .asBitmap()
                .apply(getRequestOptions())
                .load(url)
                .into(new CustomTarget<Bitmap>(){
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        File destFile = FileUtilsExt.savaFileUtils(context, true, savePath,saveFileName);
                        FileUtilsExt.saveBitmap2File(context,resource,destFile);
                        listener.onDownloadSuccess();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        listener.onDownloadFail();
                    }
                });
    }
}
