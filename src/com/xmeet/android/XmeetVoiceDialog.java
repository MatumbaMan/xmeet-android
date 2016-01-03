package com.xmeet.android;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class XmeetVoiceDialog  extends Dialog{
	private Context mContext = null;
	
	private ImageView mIcon = null;
	private ImageView mVoice = null;
	private TextView mLable = null;
	
	public XmeetVoiceDialog(Context context) {
		super(context, XmeetResource.getIdByName(context, "style", "xmeet_loading_dialog"));
		
		
		
		mContext = context;
		
	}
	
	public void showRecording() {
		View v = new XmeetVoiceLoading(mContext);
		setContentView(v);
		
		mIcon = (ImageView) v.findViewById(XmeetUtil.xmeet_voice_icon);
		mVoice = (ImageView) v.findViewById(XmeetUtil.xmeet_voice_voice);
		mLable = (TextView) v.findViewById(XmeetUtil.xmeet_voice_label);
		
//		if (!this.isShowing()) {
			this.show();
//		}
	}
	
	public void recording() {
		if (this.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setBackgroundResource(XmeetResource.getIdByName(mContext, "drawable", "xmeet_voice_hint"));
			mVoice.setBackgroundResource(XmeetResource.getIdByName(mContext, "drawable", "xmeet_amp1"));
			mLable.setText("手指上滑，取消发送");
		}
	}
	
	public void wantToCancel() {
		if (this.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setBackgroundResource(XmeetResource.getIdByName(mContext, "drawable", "xmeet_voice_cancel"));
			mLable.setText("松开手指，取消发送");
		}
	}
	
	public void tooShort() {
		if (this.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setBackgroundResource(XmeetResource.getIdByName(mContext, "drawable", "xmeet_voice_short"));
			mLable.setText("录音时间过短");
		}
	}
	
	public void dismissRecording() {
		if (this.isShowing()) {
			this.dismiss();
		}
	}
	
	public void updateVoiceLevel(int level) {
		if (this.isShowing()) {
			int resId = XmeetResource.getIdByName(mContext, "drawable", "xmeet_amp" + level);
			mVoice.setBackgroundResource(resId);
		}
	}

}
