package com.zzti.fengyongge.imagepickersample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;


public class MyGridView extends GridView {

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	 @Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	  
		 int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
					MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);

			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();

	     }


}
