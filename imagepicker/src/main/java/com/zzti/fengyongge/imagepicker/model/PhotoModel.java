
package com.zzti.fengyongge.imagepicker.model;
import java.io.Serializable;

/**
 * Created by fengyongge on 2016/5/24
 */
public class PhotoModel implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 网络图片url：http://gank.io/images/f0c192e3e335400db8a709a07a891b2e 或者 https://gank.io/images/f0c192e3e335400db8a709a07a891b2e
	 * 本地图片uri：/storage/emulated/0/imagePicker/1599114947548.jpg
	 */
	private String originalPath;
	/**
	 * 是否选中
	 */
	private boolean isChecked = false;

	public PhotoModel(String originalPath, boolean isChecked) {
		super();
		this.originalPath = originalPath;
		this.isChecked = isChecked;
	}

	public PhotoModel(String originalPath) {
		this.originalPath = originalPath;
	}

	public PhotoModel() {
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}

