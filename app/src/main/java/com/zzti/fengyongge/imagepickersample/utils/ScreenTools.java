package com.zzti.fengyongge.imagepickersample.utils;

import android.content.Context;

public class ScreenTools {

    private static ScreenTools mScreenTools;
    private Context context;

    private ScreenTools(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ScreenTools instance(Context context) {
        if (mScreenTools == null){
            mScreenTools = new ScreenTools(context);
        }
        return mScreenTools;
    }

    public float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public int getScreenWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


}
