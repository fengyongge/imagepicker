package com.zzti.fengyongge.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * describe
 * 对外API
 * @author fengyongge(fengyongge98@gmail.com)
 * @version
 * @date 2020/7/26
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
     * @param context
     * @param tempList 浏览图片集合，注意！必须封装成imagepicker的bean，url支持网络或者本地
     * @param positon  角标
     * @param isSave 是否支持保存
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
     * @param context
     * @param limit  选择图片张数
     * @param isShowCamera 是否支持拍照
     * @param requestCode
     */
    public void photoSelect(Context context, int limit, boolean isShowCamera,int requestCode) {
        Intent intent = new Intent(context, PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(LIMIT, limit);
        intent.putExtra(IS_SHOW_CAMERA, isShowCamera);
        CommonUtils.launchActivityForResult((Activity) context, intent, requestCode);
    }

}
