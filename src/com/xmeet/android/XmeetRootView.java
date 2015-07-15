package com.xmeet.android;

import com.xmeet.android.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

class XmeetRootView extends RelativeLayout {
	private RelativeLayout 	topLayout;
	private TextView			backButton;
	private TextView			nestName;
	private TextView			userName;
	
	private RelativeLayout 	bottomLayout;
	private TextView			sendButton;
	private EditText			messageText;
	
	private TextView			xmeetText;
	private ProgressBar		progress;
	private ListView			messageList;
	
	public XmeetRootView(Context context) {
		super(context);
		
		setTopView(context);
		setBottomView(context);
		setXmeetText(context);
		setListView(context);
		setProgress(context);
	}

	private void setTopView(Context context) {
		topLayout = new RelativeLayout(context);
		topLayout.setBackgroundColor(Color.parseColor("#394346"));
		topLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, XmeetUtil.dip2px(context, 45)));
		
		backButton = new TextView(context);
		backButton.setText(" 返回");
		backButton.setTextColor(Color.parseColor("#ffffff"));
		backButton.setTextSize(18.f);
		Drawable drawable= getResources().getDrawable(R.drawable.xmeet_back_arrow);   
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
		backButton.setCompoundDrawables(drawable,null,null,null);  
		
		LayoutParams backParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		backParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		backParam.addRule(RelativeLayout.CENTER_VERTICAL);
		backParam.setMarginStart(XmeetUtil.dip2px(context, 15));
		backButton.setLayoutParams(backParam);
		backButton.setId(XmeetUtil.xmeet_back_button);
		topLayout.addView(backButton);
		
		nestName = new TextView(context);
		nestName.setText("我的聊天室");
		nestName.setTextColor(Color.parseColor("#ffffff"));
		nestName.setTextSize(18.f);
		
		LayoutParams nestParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		nestParam.addRule(RelativeLayout.CENTER_IN_PARENT);
		nestName.setLayoutParams(nestParam);
		nestName.setId(XmeetUtil.xmeet_xnest_name);
		topLayout.addView(nestName);
		
		userName = new TextView(context);
		userName.setBackgroundResource(R.drawable.xmeet_title_user);
		
		LayoutParams userParam = new LayoutParams(XmeetUtil.dip2px(context, 25), XmeetUtil.dip2px(context, 25));
		userParam.addRule(RelativeLayout.CENTER_VERTICAL);
		userParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		userParam.setMarginEnd(XmeetUtil.dip2px(context, 15));
		userName.setLayoutParams(userParam);
		userName.setId(XmeetUtil.xmeet_user_name);
		topLayout.addView(userName);
		
		topLayout.setId(XmeetUtil.xmeet_title_layout);
		this.addView(topLayout);
	}
	
	private void setBottomView(Context context) {
		bottomLayout = new RelativeLayout(context);
		bottomLayout.setBackgroundColor(Color.parseColor("#394346"));
		
		bottomLayout.setFocusable(true);
		bottomLayout.setFocusableInTouchMode(true);
		
		LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, XmeetUtil.dip2px(context, 45));
		layoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
		bottomLayout.setLayoutParams(layoutParam);
		
		sendButton = new TextView(context);
		sendButton.setBackgroundResource(R.drawable.xmeet_message_send);
		
		LayoutParams sendParam = new LayoutParams(XmeetUtil.dip2px(context, 28), XmeetUtil.dip2px(context, 28));
		sendParam.addRule(RelativeLayout.CENTER_VERTICAL);
		sendParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		sendParam.setMarginEnd(XmeetUtil.dip2px(context, 10));
		sendButton.setLayoutParams(sendParam);
		sendButton.setId(XmeetUtil.xmeet_message_send);
		bottomLayout.addView(sendButton);
		
		messageText = new EditText(context);
		messageText.setHint("请输入...");
		messageText.setLines(1);
		
		LayoutParams messageParam = new LayoutParams(LayoutParams.MATCH_PARENT, XmeetUtil.dip2px(context, 40));
		messageParam.addRule(RelativeLayout.CENTER_VERTICAL);
		messageParam.setMarginEnd(XmeetUtil.dip2px(context, 48));
		messageParam.setMarginStart(XmeetUtil.dip2px(context, 10));
		messageText.setLayoutParams(messageParam);
		messageText.setId(XmeetUtil.xmeet_message_edit);
		bottomLayout.addView(messageText);

		bottomLayout.setId(XmeetUtil.xmeet_message_layout);
		this.addView(bottomLayout);
	}
	
	private void setProgress(Context context) {
		progress = new ProgressBar(context);
		LayoutParams messageParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		messageParam.addRule(RelativeLayout.CENTER_IN_PARENT);
		progress.setLayoutParams(messageParam);
		progress.setVisibility(View.INVISIBLE);
		progress.setId(XmeetUtil.xmeet_progress);
		this.addView(progress);
	}
	
	private void setXmeetText(Context context) {
		xmeetText = new TextView(context);
		xmeetText.setText("xmeet");
		xmeetText.setTextColor(Color.parseColor("#33b5e5"));
		xmeetText.setTextSize(80.0f);
		xmeetText.getPaint().setFakeBoldText(true);
		xmeetText.setGravity(Gravity.CENTER);
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW, XmeetUtil.xmeet_title_layout);
		lp.addRule(RelativeLayout.ABOVE, XmeetUtil.xmeet_message_layout);
		
		xmeetText.setLayoutParams(lp);
		
		this.addView(xmeetText);
	}
	
	private void setListView(Context context) {
		messageList = new ListView(context);
		messageList.setBackgroundColor(Color.parseColor("#e0ECECEC"));
		messageList.setDivider(null);
//		messageList.setSelector(Color.TRANSPARENT);
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW, XmeetUtil.xmeet_title_layout);
		lp.addRule(RelativeLayout.ABOVE, XmeetUtil.xmeet_message_layout);
		
		messageList.setLayoutParams(lp);
		
		messageList.setId(XmeetUtil.xmeet_listview);
		this.addView(messageList);
	}
}
