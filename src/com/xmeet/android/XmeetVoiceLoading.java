package com.xmeet.android;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XmeetVoiceLoading extends LinearLayout{
	
	private LinearLayout mView = null;
	
	private ImageView mIcon = null;
	private ImageView mVoice = null;
	private TextView  mLabel = null;
	
	public XmeetVoiceLoading(Context context) {
		super(context);
		setView(context);
		setImage(context);
		setText(context);
	}
	
	private void setView(Context context) {
		mView = new LinearLayout(context);
		mView.setBackgroundColor(Color.parseColor("#e02aa4dc"));
		mView.setAlpha(100);
		mView.setOrientation(LinearLayout.VERTICAL);
		
		LayoutParams layoutParam = new LayoutParams(XmeetUtil.dip2px(context, 180), XmeetUtil.dip2px(context, 200));
		mView.setLayoutParams(layoutParam);
		
		this.addView(mView);
	}
	
	private void setImage(Context context) {
		LinearLayout imageLayout = new LinearLayout(context);
	
		LayoutParams imageParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageParam.gravity = Gravity.CENTER;
		imageLayout.setLayoutParams(imageParam);
		
		mIcon = new ImageView(context);
		mIcon.setBackgroundResource(XmeetResource.getIdByName(context, "drawable", "xmeet_voice_hint"));
		
		LayoutParams iconParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iconParam.setMargins(XmeetUtil.dip2px(context, 10), XmeetUtil.dip2px(context, 40), XmeetUtil.dip2px(context, 10), XmeetUtil.dip2px(context, 10));
		mIcon.setLayoutParams(iconParam);
		mIcon.setId(XmeetUtil.xmeet_voice_icon);
		imageLayout.addView(mIcon);
		
		mVoice = new ImageView(context);
		mVoice.setBackgroundResource(XmeetResource.getIdByName(context, "drawable", "xmeet_amp7"));
		
		LayoutParams voiceParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		voiceParam.setMargins(XmeetUtil.dip2px(context, 10), XmeetUtil.dip2px(context, 40), XmeetUtil.dip2px(context, 10), XmeetUtil.dip2px(context, 10));
		voiceParam.gravity = Gravity.BOTTOM;
		mVoice.setLayoutParams(voiceParam);
		mVoice.setId(XmeetUtil.xmeet_voice_voice);
		imageLayout.addView(mVoice);
		
		mView.addView(imageLayout);
	}
	
	private void setText(Context context) {
		mLabel = new TextView(context);
		mLabel.setText("手指上滑，取消发送");
		mLabel.setTextColor(Color.parseColor("#ffffff"));
		
		LayoutParams labelParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		labelParam.gravity = Gravity.CENTER;
		mLabel.setLayoutParams(labelParam);
		mLabel.setId(XmeetUtil.xmeet_voice_label);
		
		mView.addView(mLabel);
	}
	
	//显示录音的对话框
	public void showRecordingDialog() {
		
	}
	//显示状态
	//显示取消对话框
	//显示时间过短
	//取消
	//音量
}
