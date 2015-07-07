package com.xmeet.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class XmeetView  {
	
	private String mHostId = null;
	private String mXnestId = null;
	private String mNickName = null;
	
	public void openXmeet(Activity arg0) {
		Intent intent = new Intent();
		Bundle budle = new Bundle();
		budle.putString("host", mHostId);
		budle.putString("nest", mXnestId);
		budle.putString("nick", mNickName);
		intent.putExtras(budle);
		
		intent.setClassName(arg0.getApplicationContext(), "com.xmeet.android.XmeetActivity");
		arg0.startActivity(intent);
	}
	
	public void setGlobalDefaultHostId(String host) {
		mHostId = host;
	}
	
	public void setXnestId(String id) {
		mXnestId = id;
	}
	
	public void setNickName(String name) {
		mNickName = name;
	}
}
