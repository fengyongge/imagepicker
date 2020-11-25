package com.zzti.fengyongge.imagepicker.ImageloderListener;

import android.content.Context;
import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载接口
 * @author fengyongge
 * GitHub:https://github.com/fengyongge/imagepicker
 */
public interface ImageloderListener {

    void loadImageUrl(String url, ImageView imageView);

    void loadImageFile(File file, ImageView imageView);

    void asyncDownloadImage(Context context, String url, String savePath, String saveFileName, ImageDownloadListener listener);

}
