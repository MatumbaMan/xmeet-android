package com.xmeet.android;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class XmeetLoadingView extends LinearLayout {

	private ImageView imageView;
	private TextView textView;
	
	public XmeetLoadingView(Context context) {
		super(context);
		setBackgroundColor(Color.parseColor("#00000000"));
		setOrientation(LinearLayout.VERTICAL);
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(param);
		
		setImageView(context);
		setTextView(context);
	}
	
	private void setTextView(Context context) {
		textView = new TextView(context);
		textView.setText("loading...");
		textView.setTextSize(16.f);
		
		LayoutParams titleParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParam.gravity = Gravity.CENTER;
		textView.setLayoutParams(titleParam);
		
		this.addView(textView);
	}

	private void setImageView(Context context) {
		imageView = new ImageView(context);
		imageView.setBackgroundResource(R.drawable.xmeet_loading_animation);
		
		LayoutParams titleParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParam.gravity = Gravity.CENTER;
		imageView.setLayoutParams(titleParam);
		
		imageView.setId(XmeetUtil.xmeet_loading_image);
		this.addView(imageView);
	}
}
