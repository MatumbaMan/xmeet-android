package com.xmeet.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class XmeetView  {
	
	private String mXnestId = null;
	private String mNickName = null;
	
	public void openXmeet(Activity arg0) {
		String packgeName = arg0.getApplicationInfo().packageName;
		setXnestId(packgeName);
		
		Intent intent = new Intent();
		Bundle budle = new Bundle();
		budle.putString("nest", mXnestId);
		budle.putString("nick", mNickName);
		intent.putExtras(budle);
		
		
		intent.setClassName(arg0.getApplicationContext(), "com.xmeet.android.XmeetActivity");
//		intent.setClass(arg0.getApplicationContext(), XmeetActivity.class);
		arg0.startActivity(intent);
	}
	
	private void setXnestId(String id) {
		mXnestId = XmeetUtil.stringToMD5(id);
	}
	
	public void setNickName(String name) {
		mNickName = name;
	}
}
