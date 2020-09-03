package com.zzti.fengyongge.imagepickersample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzti.fengongge.imagepickersample.R;
import com.zzti.fengyongge.imagepicker.ImagePickerInstance;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;
import com.zzti.fengyongge.imagepicker.util.FileUtils;
import com.zzti.fengyongge.imagepickersample.model.UploadGoodsBean;
import com.zzti.fengyongge.imagepickersample.utils.Config;
import com.zzti.fengyongge.imagepickersample.utils.SizeUtils;
import com.zzti.fengyongge.imagepickersample.view.MyGridView;

import java.util.ArrayList;
import java.util.List;


public class AddPicActivity extends AppCompatActivity {

    /** 展示的图片集合 */
    private ArrayList<UploadGoodsBean> imageList = new ArrayList<UploadGoodsBean>();
    private int screen_widthOffset;
    private MyGridView myGridView;
    private GridImgAdapter gridImgsAdapter;
    private int requestCodeNum = 0;

    /** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pic);

        Config.ScreenMap = Config.getScreenSize(this, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screen_widthOffset = (display.getWidth() - (3 * SizeUtils.dp2px(2))) / 3;
        myGridView = (MyGridView) findViewById(R.id.my_goods_GV);
        gridImgsAdapter = new GridImgAdapter();
        myGridView.setAdapter(gridImgsAdapter);
        imageList.add(null);
        gridImgsAdapter.notifyDataSetChanged();
    }


    class GridImgAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AddPicActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if (convertView != null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddPicActivity.this).inflate(R.layout.activity_addstory_img_item, null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);

            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (imageList.get(position) == null) {
                holder.delete_IV.setVisibility(View.GONE);

                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, holder.add_IB);

                holder.add_IB.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        int limit = 9 - (imageList.size() - 1);
                        ImagePickerInstance.getInstance().photoSelect(AddPicActivity.this,limit,true,requestCodeNum);
                    }
                });

            } else {

                ImageLoader.getInstance().displayImage("file://" + imageList.get(position).getUrl(), holder.add_IB);

                holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;

                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = imageList.remove(position).getUrl();
                        for (int i = 0; i < imageList.size(); i++) {
                            if (imageList.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            imageList.add(null);
                        }
                        FileUtils.DeleteFolder(img_url);
                        gridImgsAdapter.notifyDataSetChanged();
                    }
                });

                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            List<PhotoModel> tempList = new ArrayList<PhotoModel>();
                            for (int i = 0; i < imageList.size(); i++) {
                                if(imageList.get(i)!=null){
                                    PhotoModel photoModel = new PhotoModel();
                                    photoModel.setOriginalPath(imageList.get(i).getUrl());
                                    tempList.add(photoModel);
                                }
                            }
                        ImagePickerInstance.getInstance().photoPreview(AddPicActivity.this,tempList,position,false);

                    }
                });

            }
            return convertView;
        }

        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    if (imageList.size() > 0) {
                        imageList.remove(imageList.size() - 1);
                    }

                    for (int i = 0; i < paths.size(); i++) {
                        imageList.add(new UploadGoodsBean(paths.get(i), false));
                        //上传参数
                    }
                    if (imageList.size() < 9) {
                        imageList.add(null);
                    }
                    gridImgsAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}