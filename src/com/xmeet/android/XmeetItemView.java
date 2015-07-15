package com.xmeet.android;

import com.xmeet.android.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

class XmeetItemView extends LinearLayout {

private TextView			timeMessage;
	
	private RelativeLayout	mesageLayout;
	private ImageView		headImage;
	private TextView			nameText;
	private TextView			messageText;
	private int type;
	
	public XmeetItemView(Context context, int type) {
		super(context);
		this.type = type;
		int padding = XmeetUtil.dip2px(context, 6);
		setOrientation(LinearLayout.VERTICAL);
		setPadding(padding, padding, padding, padding);
		
		setTime(context);
		setMessage(context);
	}

	private void setTime(Context context) {
		timeMessage = new TextView(context);
		
		GradientDrawable gd = new GradientDrawable();
	    gd.setColor(Color.parseColor("#D1D1D1"));
	    gd.setCornerRadius(XmeetUtil.dip2px(context, 9));
	    timeMessage.setBackground(gd);
	    
	    int padding = XmeetUtil.dip2px(context, 5);
	    timeMessage.setPadding(padding, 0, padding, 0);
		
		LayoutParams backParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		backParam.gravity = Gravity.CENTER_HORIZONTAL;
		timeMessage.setLayoutParams(backParam);
		
		timeMessage.setId(XmeetUtil.xmeet_message_time);
		this.addView(timeMessage);
	}
	
	private void setMessage(Context context) {
		mesageLayout = new RelativeLayout(context);
		LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParam.setMargins(0, XmeetUtil.dip2px(context, 10), 0, 0);
		mesageLayout.setLayoutParams(layoutParam);
		
		headImage = new ImageView(context);
		headImage.setBackgroundResource(R.drawable.xmeet_user_default);
		android.widget.RelativeLayout.LayoutParams headParam = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		if (type == XmeetUtil.item_left)
			headParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		else
			headParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		headParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		headImage.setLayoutParams(headParam);
		headImage.setId(XmeetUtil.xmeet_message_head);
		mesageLayout.addView(headImage);
		
		nameText = new TextView(context);
		nameText.setGravity(Gravity.CENTER);
		nameText.setTextSize(10.f);
		android.widget.RelativeLayout.LayoutParams nameParam = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		nameParam.addRule(RelativeLayout.BELOW, XmeetUtil.xmeet_message_head);
		nameParam.addRule(RelativeLayout.ALIGN_LEFT, XmeetUtil.xmeet_message_head);
		nameParam.addRule(RelativeLayout.ALIGN_RIGHT, XmeetUtil.xmeet_message_head);
		nameText.setLayoutParams(nameParam);
		nameText.setId(XmeetUtil.xmeet_message_username);
		mesageLayout.addView(nameText);
		
		messageText = new TextView(context);
		messageText.setTextSize(16.0f);
		android.widget.RelativeLayout.LayoutParams payloadParam = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		if (type == XmeetUtil.item_left) {
			messageText.setBackgroundResource(R.drawable.xmeet_message_other);
			payloadParam.addRule(RelativeLayout.RIGHT_OF, XmeetUtil.xmeet_message_username);
			payloadParam.setMargins(XmeetUtil.dip2px(context, 5), 0, XmeetUtil.dip2px(context, 30), 0);
		} else {
			messageText.setBackgroundResource(R.drawable.xmeet_message_mine);
			payloadParam.addRule(RelativeLayout.LEFT_OF, XmeetUtil.xmeet_message_username);
			payloadParam.setMargins(XmeetUtil.dip2px(context, 30), 0, XmeetUtil.dip2px(context, 5), 0);
		}
		
		messageText.setLayoutParams(payloadParam);
		messageText.setId(XmeetUtil.xmeet_message_payload);
		mesageLayout.addView(messageText);
		
		this.addView(mesageLayout);
	}

}
