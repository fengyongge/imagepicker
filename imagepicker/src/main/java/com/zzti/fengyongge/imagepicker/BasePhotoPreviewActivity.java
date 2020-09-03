package com.zzti.fengyongge.imagepicker;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.zzti.fengongge.imagepicker.R;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.AnimationUtils;
import com.zzti.fengyongge.imagepicker.view.PhotoPreview;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fengyongge on 2016/5/24
 */
public class BasePhotoPreviewActivity extends AppCompatActivity{
	protected List<PhotoModel> photos = new ArrayList<>();
	protected int current;
	private Boolean is_save;
	private ViewPager mViewPager;
	private RelativeLayout rlTopTitle;
	private ImageView ivBack;
	private TextView tvPercent;
	protected boolean isUp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photopreview);
		initView();
		initOnclick();
	}

	void initView(){
		rlTopTitle = findViewById(R.id.rlTopTitle);
		ivBack = findViewById(R.id.ivBack);
		tvPercent =findViewById(R.id.tvPercent);
		mViewPager =findViewById(R.id.viewPager);
		overridePendingTransition(R.anim.activity_alpha_action_in, 0);
	}

	void initOnclick(){

		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				current = position;
				updatePercent(current,photos.size());
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 绑定数据，更新界面
	 */
	protected void bindData(Boolean is_save) {
		this.is_save = is_save;
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(current);
	}

	/**
	 * 展示当前张数
	 */
	protected void updatePercent(int current,int totleNum) {
		tvPercent.setText((current+1) + "/" + totleNum);
	}


	/**
	 * 装载图片
	 */
	private PagerAdapter mPagerAdapter = new PagerAdapter() {
		@Override
		public int getCount() {
			if (photos == null) {
				return 0;
			} else {
				return photos.size();
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container, final int position) {
			PhotoPreview photoPreview = new PhotoPreview(BasePhotoPreviewActivity.this);
			container.addView(photoPreview);
			photoPreview.loadImage(photos.get(position),is_save);
//			photoPreview.setOnClickListener(photoItemClickListener);
			return photoPreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	};


	/** 图片点击事件回调 */
	private OnClickListener photoItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isUp) {
				new AnimationUtils(getApplicationContext(), R.anim.translate_up)
						.setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(rlTopTitle);
				isUp = true;
			} else {
				new AnimationUtils(getApplicationContext(), R.anim.translate_down_current)
						.setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(rlTopTitle);
				isUp = false;
			}
		}
	};


}
