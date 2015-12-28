package com.xmeet.android;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class XmeetVoice {
	
	private MediaRecorder mRecorder = null;
	
	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(android.os.Environment.getExternalStorageDirectory() + "/" + name);
			
			try {
				mRecorder.prepare();
				mRecorder.start();
			} catch (IllegalStateException e) {
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}
	
	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}
}
