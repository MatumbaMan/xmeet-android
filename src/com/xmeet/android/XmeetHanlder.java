package com.xmeet.android;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import android.content.Context;

import com.xmeet.android.XmeetParser.XmeetListener;

class XmeetHanlder extends WebSocketClient {
	
	private XmeetParser mParser = null;

	public XmeetHanlder(URI serverUri, Context context) {
		super(serverUri);
		mParser = new XmeetParser(context);
	}
	
	public void addListener(XmeetListener listener) {
		mParser.addListener(listener);
	}
	
	public void removeListener() {
		mParser.removeListener();
	}

	@Override
	public void onClose(int arg0, String arg1, boolean remote) {
		mParser.onClose();
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace(); 
		mParser.onError(ex.toString());
	}

	@Override
	public void onMessage(String message) {
		mParser.parseMessage(message);
	}
	
	@Override
	public void onMessage(ByteBuffer bytes) {
		String name = "sdfsdf";
		name = name.toUpperCase();
		super.onMessage(bytes);
	}
	
	@Override
	public void onOpen(ServerHandshake arg0) {
		mParser.onOpen();
	}

	
	
}
