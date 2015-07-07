package com.xmeet.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class XmeetParser {
	
	private XmeetListener mListener;
	private XmeetMembers mMembers;
	
	private String mId;
	
	public XmeetParser() {
		mMembers = new XmeetMembers();
	}
	
	public interface XmeetListener {
		void onOpen();
		void onClose();
		void onJoin(XmeetMessage message);
		void onLeave(XmeetMessage message);
		void onMessage(XmeetMessage message);
		void onChangeName(XmeetMessage message);
		void onHistory(List<XmeetMessage> messages);
		
		void onError(String errmsg);
	}
	
	public void addListener(XmeetListener listener) {
		mListener = listener;
	}
	
	public void removeListener() {
		mListener = null;
	}
	
	public void onClose() {
		if (mListener != null)
			mListener.onClose();
	}
	
	public void onOpen() {
		if (mListener != null)
			mListener.onOpen();
	}
	
	public void onError(String errmsg) {
		if (mListener != null)
			mListener.onError(errmsg);
	}

	public void parseMessage(String message) {
		
		try {
			JSONObject object = new JSONObject(message);
			
			String type = object.getString("type");
			
			if (type.equals("members")) {
				parseMembers(object);
			} else if (type.equals("member_count")) {
				
			} else if (type.equals("normal")) {
				parseNormal(object);
			} else if (type.equals("join")) {
				parseJoin(object);
			} else if (type.equals("leave")) {
				parseLeave(object);
			} else if (type.equals("self")) {
				parseSelf(object);
			} else if (type.equals("changename")) {
				parseChangeName(object);
			} else if (type.equals("history")) {
				parseHisgory(object);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("General:" + e.toString());
		}
		
	}

	private void parseSelf(JSONObject object) {
		try {
			
			mId = object.getString("payload");	
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("Self:" + e.toString());
		}
	}
	
	private void parseMembers(JSONObject object) {
		try {
			JSONArray payload = object.getJSONArray("payload");
			
			for (int i = 0; i < payload.length(); i++) {
				
				JSONObject info = payload.getJSONObject(i);
				
				XmeetUserInfo user 	= new XmeetUserInfo();
				user.id 			= info.getString("pid");
				user.nickname 		= info.getString("nickname");
				
				if (mId.equals(user.id))
					user.isSelf = true;
				
				mMembers.addMember(user);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("Members:" + e.toString());
		}
	}
	
	private void parseJoin(JSONObject object) {
		try {
			
			XmeetUserInfo user 	= new XmeetUserInfo();
			
			user.id = object.getString("from");
			user.nickname = object.getString("payload");
			
			mMembers.addMember(user);
			
			XmeetMessage message = new XmeetMessage();
			message.payload = user.nickname + " 加入了聊天室";
			message.type = 2;
			
			if (mListener != null)
				mListener.onJoin(message);
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("Join:" + e.toString());
		}
	}
	
	private void parseLeave(JSONObject object) {
		try {
			String id = object.getString("from");
			String nickname = mMembers.removeMember(id);
			XmeetMessage message = new XmeetMessage();
			message.payload = nickname + " 离开了聊天室";
			message.type = 2;
			
			if (mListener != null)
				mListener.onLeave(message);
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("Leave:" + e.toString());
		}
	}
	
	private void parseNormal(JSONObject object) {
		try {
			String id = object.getString("from");
			String payload = object.getString("payload");
			String send_time = object.getString("send_time");
			
			XmeetUserInfo user = mMembers.queryMember(id);
			String nickname = user.nickname == null ? "" : user.nickname;
			
			XmeetMessage message = new XmeetMessage();
			message.nickName = nickname;
			message.payload = payload;
			message.sendTime = send_time;
			message.type = (user.isSelf ? 1 : 0);
			
			if (mListener != null)
				mListener.onMessage(message);
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("Normal:" + e.toString());
		}
	}
	
	private void parseChangeName(JSONObject object) {
		try {
			
			String id = object.getString("from");
			String newname = object.getString("payload");	
			
			XmeetUserInfo user = mMembers.queryMember(id);
			String nickname = user.nickname == null ? "" : user.nickname;
			
			String old = nickname;
			mMembers.setNewName(newname, id);
			
			XmeetMessage message = new XmeetMessage();
			message.payload = old + " 使用了新名字 " + newname;
			message.type = 2;
			message.nickName = newname;
			
			if (mListener != null)
				mListener.onChangeName(message);	
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("Changename:" + e.toString());
		}
	}
	
	private void parseHisgory(JSONObject object) {
		
		List<XmeetMessage> list = new ArrayList<XmeetMessage>();
		try {
			Thread.sleep(1000);
			JSONArray payload = object.getJSONArray("payload");
			
			for (int i = 0; i < payload.length(); i ++) {
				JSONObject info = payload.getJSONObject(i);
				
				XmeetMessage message = new XmeetMessage();
				
				XmeetUserInfo user 	= mMembers.queryMember(info.getString("from"));
				String nickname = user.nickname == null ? "" : user.nickname;
				message.nickName	= nickname;
				message.payload		= info.getString("payload");
				message.sendTime	= info.getString("send_time");
				
				if (mId.equals(user.id))
					user.isSelf = true;
				
				list.add(message);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			onError("History:" + e.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
			onError("History:" + e.toString());
		} finally {
			XmeetMessage message = new XmeetMessage();
			message.payload		= "------------以上是历史消息---------------";
			message.type		= 2;

			list.add(message);
			
			if (mListener != null)
				mListener.onHistory(list);
		}
	}
}
