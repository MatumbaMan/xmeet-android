package com.xmeet.android;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

class XmeetRenameView extends LinearLayout {

	private TextView title;
	private TextView message;
	private EditText edit;
	
	private LinearLayout buttonLayout;
	private TextView left;
	private TextView right;
	
	private int margin = 0;
	
	public XmeetRenameView(Context context) {
		super(context);
		
		margin = XmeetUtil.dip2px(context, 5);
		
		setOrientation(LinearLayout.VERTICAL);
		setMinimumWidth(XmeetUtil.dip2px(context, 250));
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setPadding(margin, margin, margin, margin);
		setLayoutParams(param);
		
	    int strokeColor = Color.parseColor("#2aa4dc");//边框颜色
	    int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色

	    GradientDrawable gd = new GradientDrawable();//创建drawable
	    gd.setColor(fillColor);
	    gd.setStroke(XmeetUtil.dip2px(context, 0.5f), strokeColor);
	    setBackground(gd);

		setTitle(context);
		setMessage(context);
		setEdit(context);
		setButton(context);
	}
	
	private void setTitle(Context context) {
		title = new TextView(context);
		title.setTextColor(Color.parseColor("#2aa4dc"));
		title.setText("昵称修改");
		title.setTextSize(19.f);
		
		LayoutParams titleParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParam.gravity = Gravity.CENTER;
		titleParam.setMargins(margin, margin, margin, margin);
		title.setLayoutParams(titleParam);
		
		this.addView(title);
	}

	private void setMessage(Context context) {
		message = new TextView(context);
		message.setTextColor(Color.parseColor("#2aa4dc"));
		message.setText("请输入新的名字");
		message.setTextSize(16.f);
		
		LayoutParams backParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		backParam.gravity = Gravity.CENTER;
		backParam.setMargins(margin, margin, margin, margin);
		message.setLayoutParams(backParam);
		
		this.addView(message);
	}

	private void setEdit(Context context) {
		edit = new EditText(context);
		
		LayoutParams backParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		edit.setLayoutParams(backParam);
		
		edit.setId(XmeetUtil.xmeet_rename_edit);
		this.addView(edit);
	}

	private void setButton(Context context) {
		buttonLayout = new LinearLayout(context);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		buttonLayout.setLayoutParams(layoutParam);
		
		int strokeColor = Color.parseColor("#2aa4dc");//边框颜色
	    int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色

	    GradientDrawable gd = new GradientDrawable();//创建drawable
	    gd.setColor(fillColor);
	    gd.setStroke(XmeetUtil.dip2px(context, 0.5f), strokeColor);
		
		left = new TextView(context);
		left.setText("确定");
		left.setTextSize(19.f);
		left.setTextColor(Color.parseColor("#2aa4dc"));
		left.setBackground(gd);
		left.setGravity(Gravity.CENTER);
		left.setPadding(margin, margin, margin, margin);
		
		LayoutParams leftParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
		leftParam.setMargins(margin, margin, margin, margin);
		left.setLayoutParams(leftParam);
		
		left.setId(XmeetUtil.xmeet_rename_ok);
		buttonLayout.addView(left);
		
		right = new TextView(context);
		right.setText("取消");
		right.setTextSize(19.f);
		right.setTextColor(Color.parseColor("#2aa4dc"));
		right.setBackground(gd);
		right.setGravity(Gravity.CENTER);
		right.setPadding(margin, margin, margin, margin);
		
		LayoutParams rightParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
		rightParam.setMargins(margin, margin, margin, margin);
		right.setLayoutParams(rightParam);
		
		right.setId(XmeetUtil.xmeet_rename_cancle);
		buttonLayout.addView(right);
		
		
		this.addView(buttonLayout);
	}
}
