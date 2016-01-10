package com.xmeet.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

class XmeetAdapter extends BaseAdapter {
	
	private Context mContext;
	private ViewHolder mHolder1 ;
	private ViewHolder mHolder2 ;
	
	private int mMinItemWidth; //最小的item宽度
    private int mMaxItemWidth; //最大的item宽度
	
	private List<XmeetMessage> mData = new ArrayList<XmeetMessage>();

	public XmeetAdapter(Context context, XmeetMessage message) {
		mContext = context;
		mData.add(message);
		init(context);
	}
	
	public XmeetAdapter(Context context, List<XmeetMessage> list) {
		mContext = context;
		mData = list;
		init(context);
	}
	
	private void init(Context context) {
		//获取屏幕的宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);//(int) (outMetrics.widthPixels * 0.15f);
	}
	
	public void addMessage(XmeetMessage message) {
		mData.add(message);
	}
	
	public void addMessage(List<XmeetMessage> messages) {
		for (XmeetMessage message : messages) 
			mData.add(message);
	}
	
	private class ViewHolder {
		TextView time;
		ImageView head;
		TextView payload;
		TextView name;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	@Override
	public int getItemViewType(int position) {
		return mData.get(position).type;
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		

		XmeetMessage message = mData.get(position);
		int type = getItemViewType(position);
		
		if (contentView == null) {

			switch (type) {
				case 0:
					mHolder1 = new ViewHolder();
					contentView = new XmeetItemView(mContext, XmeetUtil.item_left);
					
					mHolder1.time = (TextView) contentView.findViewById(XmeetUtil.xmeet_message_time);
					mHolder1.head = (ImageView) contentView.findViewById(XmeetUtil.xmeet_message_head);
					mHolder1.payload = (TextView) contentView.findViewById(XmeetUtil.xmeet_message_payload);
					mHolder1.name = (TextView) contentView.findViewById(XmeetUtil.xmeet_message_username);
					contentView.setTag(mHolder1);
					break;
				case 1:
				case 2:
					mHolder2 = new ViewHolder();
					contentView = new XmeetItemView(mContext, XmeetUtil.item_right);
					
					mHolder2.time = (TextView) contentView.findViewById(XmeetUtil.xmeet_message_time);
					mHolder2.head = (ImageView) contentView.findViewById(XmeetUtil.xmeet_message_head);
					mHolder2.payload = (TextView) contentView.findViewById(XmeetUtil.xmeet_message_payload);
					mHolder2.name = (TextView) contentView.findViewById(XmeetUtil.xmeet_message_username);
					contentView.setTag(mHolder2);

					break;
				default:
					break;
			}
			
			
		} else {
			switch (type) {
			case 0:
				mHolder1 = (ViewHolder) contentView.getTag();
				break;
			case 1:
			case 2:
				mHolder2 = (ViewHolder) contentView.getTag();
				break;
				default:
					break;
			}
			
		}
		
		switch (type) {
		case 0:
			mHolder1.time.setText(message.sendTime);
//			mHolder1.payload.setText(message.payload);
			if (message.payload.startsWith("audio:")) {
				mHolder1.payload.setText("");
				mHolder1.payload.setCompoundDrawablesWithIntrinsicBounds(0, 0, XmeetResource.getIdByName(mContext, "drawable", "xmeet_voice_playing_other"), 0);
				LayoutParams lp = (LayoutParams) mHolder1.payload.getLayoutParams();
				lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60.0f) * XmeetUtil.getAmrDuration(message.payload.replace("audio:", "")));

			} else {
				mHolder1.payload.setText(message.payload);
			}
			mHolder1.name.setText(message.nickName);
			
			mHolder1.payload.setVisibility(View.VISIBLE);
			mHolder1.head.setVisibility(View.VISIBLE);
			mHolder1.name.setVisibility(View.VISIBLE);
			break;
		case 1:
			mHolder2.time.setText(message.sendTime);
			if (message.payload.startsWith("audio:")) {
				mHolder2.payload.setText("");
				mHolder2.payload.setCompoundDrawablesWithIntrinsicBounds(0, 0, XmeetResource.getIdByName(mContext, "drawable", "xmeet_voice_playing_mine"), 0);
				LayoutParams lp = (LayoutParams) mHolder2.payload.getLayoutParams();
				lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60.0f) * XmeetUtil.getAmrDuration(message.payload.replace("audio:", "")));
			} else {
				mHolder2.payload.setText(message.payload);
			}
			mHolder2.name.setText(message.nickName);
			
			mHolder2.payload.setVisibility(View.VISIBLE);
			mHolder2.head.setVisibility(View.VISIBLE);
			mHolder2.name.setVisibility(View.VISIBLE);
			break;
		case 2:
			mHolder2.time.setText(message.payload);
			mHolder2.payload.setVisibility(View.GONE);
			mHolder2.head.setVisibility(View.GONE);
			mHolder2.name.setVisibility(View.GONE);
			break;
			default:
				break;
		}
		
		
		return contentView;
	}
}
