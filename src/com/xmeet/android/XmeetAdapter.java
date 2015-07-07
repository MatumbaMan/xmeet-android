package com.xmeet.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class XmeetAdapter extends BaseAdapter {
	
	private Context mContext;
	private ViewHolder mHolder1 ;
	private ViewHolder mHolder2 ;
	
	private List<XmeetMessage> mData = new ArrayList<XmeetMessage>();

	public XmeetAdapter(Context context, XmeetMessage message) {
		mContext = context;
		mData.add(message);
	}
	
	public XmeetAdapter(Context context, List<XmeetMessage> list) {
		mContext = context;
		mData = list;
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
		return null;
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
					contentView = LayoutInflater.from(mContext).inflate(XmeetResource.getIdByName(mContext, "layout", "xmeet_message_item_left"), null);
					
					mHolder1.time = (TextView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_time"));
					mHolder1.head = (ImageView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_head"));
					mHolder1.payload = (TextView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_payload"));
					mHolder1.name = (TextView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_username"));
					contentView.setTag(mHolder1);
					break;
				case 1:
				case 2:
					mHolder2 = new ViewHolder();
					contentView = LayoutInflater.from(mContext).inflate( XmeetResource.getIdByName(mContext, "layout", "xmeet_message_item_right"), null);
					
					mHolder2.time = (TextView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_time"));
					mHolder2.head = (ImageView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_head"));
					mHolder2.payload = (TextView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_payload"));
					mHolder2.name = (TextView) contentView.findViewById(XmeetResource.getIdByName(mContext, "id", "xmeet_message_username"));
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
			mHolder1.payload.setText(message.payload);
			mHolder1.name.setText(message.nickName);
			
			mHolder1.payload.setVisibility(View.VISIBLE);
			mHolder1.head.setVisibility(View.VISIBLE);
			mHolder1.name.setVisibility(View.VISIBLE);
			break;
		case 1:
			mHolder2.time.setText(message.sendTime);
			mHolder2.payload.setText(message.payload);
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
