package com.zzti.fengyongge.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengyongge on 2020/7/26
 */
public class ImagePickerInstance {

    public static final String PHOTOS = "photos";
    public static final String POSITION = "position";
    public static final String IS_SAVE = "isSave";
    public static final String LIMIT = "limit";
    public static final String IS_SHOW_CAMERA = "isShowCamera";
    private static ImagePickerInstance mInstance;

    private ImagePickerInstance() {

    }

    public static ImagePickerInstance getInstance() {
        if (mInstance == null) {
            synchronized (ImagePickerInstance.class) {
                if (mInstance == null) {
                    mInstance = new ImagePickerInstance();
                }
            }
        }
        return mInstance;
    }

    /**
     * 对外开放的图片预览方法
     */
    public void photoPreview(Context context, List<PhotoModel> tempList, int positon, boolean isSave) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTOS, (ArrayList<PhotoModel>) tempList);
        bundle.putInt(POSITION, positon);
        bundle.putBoolean(IS_SAVE, isSave);
        CommonUtils.launchActivity(context, PhotoPreviewActivity.class, bundle);
    }

    /**
     * 对外图库选择图片,或者拍照选择图片方法
     */
    public void photoSelect(Context context, int limit, boolean isShowCamera,int requestCode) {
        Intent intent = new Intent(context, PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(LIMIT, limit);
        intent.putExtra(IS_SHOW_CAMERA, isShowCamera);
        CommonUtils.launchActivityForResult((Activity) context, intent, requestCode);
    }

}
