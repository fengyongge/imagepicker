package com.zzti.fengyongge.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zzti.fengongge.imagepicker.R;
import com.zzti.fengyongge.imagepicker.adapter.AlbumAdapter;
import com.zzti.fengyongge.imagepicker.adapter.PhotoSelectorAdapter;
import com.zzti.fengyongge.imagepicker.control.PhotoSelectorDomain;
import com.zzti.fengyongge.imagepicker.model.AlbumModel;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.AnimationUtils;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;
import com.zzti.fengyongge.imagepicker.util.FileUtils;
import com.zzti.fengyongge.imagepicker.util.ImageUtils;
import com.zzti.fengyongge.imagepicker.util.StringUtils;
import com.zzti.fengyongge.imagepicker.view.SelectPhotoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by fengyongge on 2016/5/24
 * 图片选择
 */
public class PhotoSelectorActivity extends Activity implements SelectPhotoItem.onItemClickListener, SelectPhotoItem.onPhotoItemCheckedListener,
		OnItemClickListener, OnClickListener {
	public static final String RECCENT_PHOTO = "最近照片";
	private List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
	private GridView gvPhotos;
	private ListView lvAblum;
	private TextView btnOk;
	private TextView tvAlbum, tvPreview, tvTitle;
	private PhotoSelectorDomain photoSelectorDomain;
	private PhotoSelectorAdapter photoAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout layoutAlbum;
	public static ArrayList<PhotoModel> selected = new ArrayList<PhotoModel>();;
	private ArrayList<String> img_path = new ArrayList<String>();
	private String path_name;
	private int limit;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("photos", img_path);
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_photoselector);
		limit = getIntent().getIntExtra("limit", 0);
		DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() //
				.considerExifParams(true) // 调整图片方向
				.resetViewBeforeLoading(true) // 载入之前重置ImageView
				.showImageOnLoading(R.drawable.ic_picture_loading) // 载入时图片设置为黑色
				.showImageOnFail(R.drawable.ic_picture_loadfailed) // 加载失败时显示的图片
				.delayBeforeLoading(0) // 载入之前的延迟时间
				.build(); //
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultDisplayImageOptions).memoryCacheExtraOptions(480, 800)
				.threadPoolSize(5).build();
		ImageLoader.getInstance().init(config);
		photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());
		tvTitle = (TextView) findViewById(R.id.tv_title_lh);
		gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
		lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
		btnOk = (TextView) findViewById(R.id.btn_right_lh);
		tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
		tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
		layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
		btnOk.setOnClickListener(this);
		tvAlbum.setOnClickListener(this);
		tvPreview.setOnClickListener(this);
		photoAdapter = new PhotoSelectorAdapter(getApplicationContext(), new ArrayList<PhotoModel>(),
				CommonUtils.getWidthPixels(this), this, this, this,limit);
		gvPhotos.setAdapter(photoAdapter);
		albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
		lvAblum.setAdapter(albumAdapter);
		lvAblum.setOnItemClickListener(this);
		findViewById(R.id.bv_back_lh).setOnClickListener(this); // 返回
		photoSelectorDomain.getReccent(reccentListener); // 更新最近照片
		photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_right_lh)
			ok(); // 选完照片
		else if (v.getId() == R.id.tv_album_ar)
			album();
		else if (v.getId() == R.id.tv_preview_ar)
			priview();
		else if (v.getId() == R.id.tv_camera_vc)
			catchPicture();
		else if (v.getId() == R.id.bv_back_lh)
			finish();
	}

	/** 拍照 */
	private void catchPicture() {
		path_name = "image"+(Math.round((Math.random()*9+1)*100000))+".jpg";
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(),path_name)));
		startActivityForResult(intent, 2);
	}


	private String cropImage(Bitmap cropImage){
		if (cropImage == null) {
			return null;
		}else {
			String path_name = FileUtils.LOCAL_PATH+"/"+System.currentTimeMillis()+".jpg";
			FileUtils.writeImage(cropImage, path_name, 100);
			return path_name;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2 ) {
			if (resultCode == -1) {
				File takefile = new File(Environment.getExternalStorageDirectory(), path_name);
				PhotoModel photoModel = new PhotoModel(takefile.getAbsolutePath());
				selected.clear();
				selected.add(photoModel);
				ok();
			}
		}
	}


	/**
	 * 完成
	 */
	private void ok() {
		if (selected.isEmpty()) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			img_path.clear();
			new Thread() {
				private String cropImage;
				@Override
				public void run() {
					for (int i = 0; i < selected.size(); i++) {
						//防止拍照图片角度发生变化(三星)
						int degree = ImageUtils.getBitmapDegree(selected.get(i).getOriginalPath());
						if (degree == 0) {
							cropImage = cropImage(ImageUtils.getimage(selected.get(i).getOriginalPath()));
						} else {
							cropImage = cropImage(ImageUtils.rotateBitmapByDegree(ImageUtils.getimage(selected.get(i).getOriginalPath()), degree));
						}
						if (StringUtils.isNotEmpty(cropImage)) {
							img_path.add(cropImage);
						}
					}
					handler.sendEmptyMessage(0);
				}
			}.start();
		}
	}

	/** 预览照片 */
	private void priview() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("photos", selected);
		CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	private void album() {
		if (layoutAlbum.getVisibility() == View.GONE) {
			popAlbum();
		} else {
			hideAlbum();
		}
	}

	/** 弹出相册列表 */
	private void popAlbum() {
		layoutAlbum.setVisibility(View.VISIBLE);
		new AnimationUtils(getApplicationContext(), R.anim.translate_up_current).setLinearInterpolator().startAnimation(
				layoutAlbum);
	}

	/** 隐藏相册列表 */
	private void hideAlbum() {
		new AnimationUtils(getApplicationContext(), R.anim.translate_down).setLinearInterpolator().startAnimation(
				layoutAlbum);
		layoutAlbum.setVisibility(View.GONE);
	}

	/** 清空选中的图片 */
	private void reset() {
		selected.clear();
		tvPreview.setText("预览");
		tvPreview.setEnabled(false);
	}

	@Override
	/** 点击查看照片 */
	public void onItemClick(int position) {
		Bundle bundle = new Bundle();
		if (tvAlbum.getText().toString().equals(RECCENT_PHOTO)){
			bundle.putInt("position", position - 1);
		}else{
			bundle.putInt("position", position);
		}
		bundle.putString("album", tvAlbum.getText().toString());
		CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}



	@Override
	/** 照片选中状态改变之后 */
	public void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			selected.add(photoModel);
			tvPreview.setEnabled(true);
		} else {
			selected.remove(photoModel);
		}
		tvPreview.setText("预览(" + selected.size() + ")");  //修改预览数量

		if (selected.isEmpty()) {
			tvPreview.setEnabled(false);
			tvPreview.setText("预览");
		}
	}

	@Override
	public void onBackPressed() {
		if (layoutAlbum.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else
			super.onBackPressed();
	}

	@Override
	/** 相册列表点击事件 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
		for (int i = 0; i < parent.getCount(); i++) {
			AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
			if (i == position){
				album.setCheck(true);
			}
			else{
				album.setCheck(false);
			}
		}
		albumAdapter.notifyDataSetChanged();
		hideAlbum();
		tvAlbum.setText(current.getName());
		tvTitle.setText(current.getName());

		// 更新照片列表
		if (current.getName().equals(RECCENT_PHOTO))
			photoSelectorDomain.getReccent(reccentListener);
		else
			photoSelectorDomain.getAlbum(current.getName(), reccentListener); // 获取选中相册的照片
	}

	/** 获取本地图库照片回调 */
	public interface OnLocalReccentListener {
		public void onPhotoLoaded(List<PhotoModel> photos);
	}

	/** 获取本地相册信息回调 */
	public interface OnLocalAlbumListener {
		public void onAlbumLoaded(List<AlbumModel> albums);
	}

	private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
		@Override
		public void onAlbumLoaded(List<AlbumModel> albums) {
			albumAdapter.update(albums);
		}
	};
	private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
		@Override
		public void onPhotoLoaded(List<PhotoModel> photos) {
			if (tvAlbum.getText().equals(RECCENT_PHOTO))
			photos.add(0, new PhotoModel());
			single_photos.clear();
			single_photos.addAll(photos);
			photoAdapter.update(photos);
			gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
			reset();
		}
	};



}
