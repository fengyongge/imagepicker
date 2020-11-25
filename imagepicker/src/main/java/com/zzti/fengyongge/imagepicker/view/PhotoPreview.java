package com.zzti.fengyongge.imagepicker.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.zzti.fengyongge.imagepicker.ImageloderListener.ImageDownloadListener;
import com.zzti.fengyongge.imagepicker.ImageloderListener.Imageloder;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
import com.zzti.fengyongge.imagepicker.R;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class PhotoPreview extends LinearLayout implements OnClickListener,EasyPermissions.PermissionCallbacks {
	private ProgressBar pbLoading;
	private ImageView ivContent;
	private OnClickListener l;
	private View save_bt;
	private String downloadeUrl;
	private Context cxt;
	private View inflate;
	private static final int WRITE_EXTERNAL_STORAGE = 123;
	private static final int RC_SETTINGS_SCREEN = 125;

	public PhotoPreview(Context context) {
		super(context);
		this.cxt = context;
		inflate = LayoutInflater.from(context).inflate(
				R.layout.view_photopreview, this, true);

		pbLoading = (ProgressBar) findViewById(R.id.pb_loading_vpp);
		ivContent = (ImageView) findViewById(R.id.iv_content_vpp);
		save_bt = findViewById(R.id.save_bt);
		save_bt.setOnClickListener(this);
		ivContent.setOnClickListener(this);
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}


	public void loadImage(PhotoModel photoModel, Boolean is_save) {
		if (is_save) {
			save_bt.setVisibility(View.VISIBLE);
		} else {
			save_bt.setVisibility(View.GONE);
		}

		//根据是否是网络图片，调用不同展示
		if(photoModel.getOriginalPath().contains("http")||photoModel.getOriginalPath().contains("https")){
			downloadeUrl = photoModel.getOriginalPath();
			Imageloder.getInstance().loadImageUrl(photoModel.getOriginalPath(),ivContent);
		}else{
			Imageloder.getInstance().loadImageUrl("file://" + photoModel.getOriginalPath(),ivContent);
		}

	}



	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_content_vpp && l != null){
			l.onClick(ivContent);
		}else if(v.getId() == R.id.save_bt){
			writeTask();
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
		switch (requestCode) {
			case WRITE_EXTERNAL_STORAGE:
				downloade();
				break;
		}
	}

	@Override
	public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
		new AppSettingsDialog.Builder((Activity) cxt)
				.setTitle(cxt.getString(R.string.title_settings_dialog))
				.setPositiveButton(cxt.getString(R.string.setting))
				.setNegativeButton(cxt.getString(R.string.cancel))
				.setRequestCode(RC_SETTINGS_SCREEN)
				.build()
				.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	/**
	 * 6.0动态申请获取相册权限
	 */
	@AfterPermissionGranted(WRITE_EXTERNAL_STORAGE)
	public void writeTask() {
		if (EasyPermissions.hasPermissions(cxt, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			// Have permission, do the thing!
			downloade();
		} else {
			EasyPermissions.requestPermissions((Activity) cxt, cxt.getString(R.string.write),
                    WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
	}

	void downloade(){
		Imageloder.getInstance().asyncDownloadImage(cxt, downloadeUrl, System.currentTimeMillis() + ".jpg", "imagepicker", new ImageDownloadListener() {
			@Override
			public void onDownloadSuccess() {
				Toast.makeText(cxt, "保存成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDownloadFail() {

			}
		});	Imageloder.getInstance().asyncDownloadImage(cxt, downloadeUrl, System.currentTimeMillis() + ".jpg", "imagepicker", new ImageDownloadListener() {
			@Override
			public void onDownloadSuccess() {
				Toast.makeText(cxt, "保存成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDownloadFail() {

			}
		});
	}
}
