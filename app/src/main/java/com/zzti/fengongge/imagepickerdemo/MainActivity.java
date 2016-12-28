package com.zzti.fengongge.imagepickerdemo;

import android.app.Activity;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;
import com.zzti.fengongge.imagepickerdemo.model.UploadGoodsBean;
import com.zzti.fengongge.imagepickerdemo.util.Config;
import com.zzti.fengongge.imagepickerdemo.util.DbTOPxUtil;
import com.zzti.fengongge.imagepickerdemo.view.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
	private LayoutInflater inflater;
	private ImageView add_IB;
	private MyGridView my_imgs_GV;
	private int screen_widthOffset;
	private ArrayList<UploadGoodsBean> img_uri = new ArrayList<UploadGoodsBean>();
	private List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
	GridImgAdapter gridImgsAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(
                defaultOptions).build();
        ImageLoader.getInstance().init(config);
		Config.ScreenMap = Config.getScreenSize(this, this);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screen_widthOffset = (display.getWidth() - (3* DbTOPxUtil.dip2px(this, 2)))/4;
		inflater = LayoutInflater.from(this);
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
			convertView = inflater.inflate(R.layout.activity_addstory_img_item, null);
			add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
			ImageView delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
			convertView.setLayoutParams(param);
			if (img_uri.get(position) == null) {
				delete_IV.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, add_IB);
				add_IB.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(MainActivity.this, PhotoSelectorActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						intent.putExtra("limit", 9 - (img_uri.size() - 1));
						startActivityForResult(intent, 0);
					}
				});

			} else {
				ImageLoader.getInstance().displayImage("file://" + img_uri.get(position).getUrl(), add_IB);
				delete_IV.setOnClickListener(new View.OnClickListener() {
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
//						FileUtils.DeleteFolder(img_url);
						gridImgsAdapter.notifyDataSetChanged();
					}
				});

				add_IB.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("photos",(Serializable)single_photos);
						bundle.putInt("position", position);
						bundle.putString("save","save");
						CommonUtils.launchActivity(MainActivity.this, PhotoPreviewActivity.class, bundle);
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
