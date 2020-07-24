package com.zzti.fengyongge.imagepicker;

import android.os.Bundle;

import com.zzti.fengyongge.imagepicker.control.PhotoSelectorDomain;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.LogUtils;
import com.zzti.fengyongge.imagepicker.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fengyongge on 2016/5/24
 * 图片预览
 */
public class PhotoPreviewActivity extends BasePhotoPreviewActivity implements PhotoSelectorActivity.OnLocalReccentListener {
	private PhotoSelectorDomain photoSelectorDomain;
	private boolean isSave;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());
		init(getIntent().getExtras());
	}

	@SuppressWarnings("unchecked")
	protected void init(Bundle extras) {
		if (extras == null){
			return;
		}
		isSave = extras.getBoolean("isSave",false);
		if (extras.containsKey("photos")) {
			this.photos = (List<PhotoModel>) extras.getSerializable("photos");
			this.current = extras.getInt("position", 0);
			// 是否保存（一般保存网络图片，本地图片只能查看）
			if(isSave){
				bindData(true);
				updatePercent();
			}else{
				bindData(false);
			}
		} else if (extras.containsKey("album")) {
			String albumName = extras.getString("album");
			this.current = extras.getInt("position");
			if (!StringUtils.isNull(albumName) && albumName.equals(PhotoSelectorActivity.RECCENT_PHOTO)) {
				photoSelectorDomain.getReccent(this);
			} else {
				photoSelectorDomain.getAlbum(albumName, this);
			}
		}
	}


	/** 点击图片预览 */
	@Override
	public void onPhotoLoaded(List<PhotoModel> photos) {
		this.photos = photos;
		updatePercent();
		bindData(false);
	}


}
