package com.xmeet.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

class XmeetDialog extends Dialog {

	public XmeetDialog(Context context) {
		super(context, XmeetResource.getIdByName(context, "style", "xmeet_loading_dialog"));
		
		View v = new XmeetLoadingView(context);
		setContentView(v);
		
		ImageView imageView = (ImageView) v.findViewById(XmeetUtil.xmeet_loading_image);
		AnimationDrawable animationDrawable = (AnimationDrawable)imageView.getBackground();  
        animationDrawable.start();   

        setCancelable(false);
	}

}
