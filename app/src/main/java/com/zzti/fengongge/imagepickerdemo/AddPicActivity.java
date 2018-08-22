package com.zzti.fengongge.imagepickerdemo;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzti.fengongge.imagepickerdemo.model.UploadGoodsBean;
import com.zzti.fengongge.imagepickerdemo.utils.Config;
import com.zzti.fengongge.imagepickerdemo.utils.DbTOPxUtils;
import com.zzti.fengongge.imagepickerdemo.view.MyGridView;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;
import com.zzti.fengyongge.imagepicker.util.FileUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyongge
 * @Description
 */
public class AddPicActivity extends AppCompatActivity  {

    private List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
    private ArrayList<UploadGoodsBean> img_uri = new ArrayList<UploadGoodsBean>();
    private int screen_widthOffset;
    private MyGridView my_imgs_GV;
    GridImgAdapter gridImgsAdapter;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_pic);

            Config.ScreenMap = Config.getScreenSize(this, this);
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            screen_widthOffset = (display.getWidth() - (3* DbTOPxUtils.dip2px(this, 2)))/3;

            my_imgs_GV = (MyGridView) findViewById(R.id.my_goods_GV);
            gridImgsAdapter = new GridImgAdapter();
            my_imgs_GV.setAdapter(gridImgsAdapter);
            img_uri.add(null);
            gridImgsAdapter.notifyDataSetChanged();
        }


    class GridImgAdapter extends BaseAdapter implements ListAdapter {
            @Override
            public int getCount() {
                return img_uri.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(AddPicActivity.this).inflate(R.layout.activity_addstory_img_item, null);

                ViewHolder holder;
                
                if(convertView!=null){
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(AddPicActivity.this).inflate(R.layout.activity_addstory_img_item,null);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
                holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
                
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
                convertView.setLayoutParams(param);
                if (img_uri.get(position) == null) {
                    holder.delete_IV.setVisibility(View.GONE);
                    
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, holder.add_IB);

                    holder.add_IB.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(AddPicActivity.this, PhotoSelectorActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("limit", 9 - (img_uri.size() - 1));
                            startActivityForResult(intent, 0);
                        }
                    });

                } else {
                    ImageLoader.getInstance().displayImage("file://" + img_uri.get(position).getUrl(), holder.add_IB);

                    holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                        private boolean is_addNull;
                        @Override
                        public void onClick(View arg0) {
                            is_addNull = true;
                            String img_url = img_uri.remove(position).getUrl();
                            for (int i = 0; i < img_uri.size(); i++) {
                                if (img_uri.get(i) == null) {
                                    is_addNull = false;
                                    continue;
                                }
                            }
                            if (is_addNull) {
                                img_uri.add(null);
                            }

						    FileUtils.DeleteFolder(img_url);//删除在emulate/0文件夹生成的图片

                            gridImgsAdapter.notifyDataSetChanged();
                        }
                    });

                    holder.add_IB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("photos",(Serializable)single_photos);
                            bundle.putInt("position", position);
                            bundle.putBoolean("isSave",false);
                            CommonUtils.launchActivity(AddPicActivity.this, PhotoPreviewActivity.class, bundle);
                            single_photos.clear();
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
                        if (img_uri.size() > 0) {
                            img_uri.remove(img_uri.size() - 1);
                        }

                        for (int i = 0; i < paths.size(); i++) {
                            img_uri.add(new UploadGoodsBean(paths.get(i), false));
                            //上传参数
                        }
                        for (int i = 0; i < paths.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(paths.get(i));
                            photoModel.setChecked(true);
                            single_photos.add(photoModel);
                        }
                        if (img_uri.size() < 9) {
                            img_uri.add(null);
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