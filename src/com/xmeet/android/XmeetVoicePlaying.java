package com.xmeet.android;

import java.io.IOException;

import android.media.MediaPlayer;

public class XmeetVoicePlaying {
	
	private static XmeetVoicePlaying mInstance;
	private MediaPlayer mPlayer = new MediaPlayer();
	
	public static XmeetVoicePlaying getInstance() {
		if (mInstance == null) {
			synchronized (XmeetVoicePlaying.class) {
				if (mInstance == null) { 
					mInstance = new XmeetVoicePlaying();
				}
			}
		}
		return mInstance;
	}
	
	public void play(String file) {
		stop();
		
		try {
			mPlayer.reset();
			mPlayer.setDataSource(file);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		if (mPlayer.isPlaying()) {
			mPlayer.stop();
		}
	}
	
}
