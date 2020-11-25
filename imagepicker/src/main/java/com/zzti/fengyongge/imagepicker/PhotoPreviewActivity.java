package com.zzti.fengyongge.imagepicker;

import android.os.Bundle;
import com.zzti.fengyongge.imagepicker.control.PhotoSelectorDomain;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import java.util.List;

/**
 * describe 图片预览
 * @author fengyongge(fengyongge98@gmail.com)
 * @date 2016/5/24
 * GitHub:https://github.com/fengyongge/imagepicker
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

	protected void init(Bundle extras) {
		if (extras == null){
			return;
		}
		isSave = extras.getBoolean(ImagePickerInstance.IS_SAVE,false);
		if (extras.containsKey(ImagePickerInstance.PHOTOS)) {
			this.photos = (List<PhotoModel>) extras.getSerializable(ImagePickerInstance.PHOTOS);
			this.current = extras.getInt(ImagePickerInstance.POSITION, 0);
			if(isSave){
				bindData(true);
			}else{
				bindData(false);
			}
			updatePercent(current,photos.size());
		} else if (extras.containsKey("album")) {
			String albumName = extras.getString("album");
			this.current = extras.getInt("position");
			if (albumName!=null && albumName.equals(PhotoSelectorActivity.RECCENT_PHOTO)) {
				photoSelectorDomain.getReccent(this);
			} else {
				photoSelectorDomain.getAlbum(albumName, this);
			}
		}
	}


	/**
	 * 图库图片预览
	 * @param photos
	 */
	@Override
	public void onPhotoLoaded(List<PhotoModel> photos) {
		this.photos = photos;
		//绑定数据，更新展示张数，不可少
		bindData(false);
		updatePercent(current,photos.size());
	}


}
