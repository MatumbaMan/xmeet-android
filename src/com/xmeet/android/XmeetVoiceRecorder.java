package com.xmeet.android;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.os.Environment;

public class XmeetVoiceRecorder {
	
	private static XmeetVoiceRecorder mInstance;
	
	private MediaRecorder mRecorder = null;
	private boolean isPrepare;
	private final static String DIR_NAME = android.os.Environment.getExternalStorageDirectory() + "/xmeet/local/";
	private String mCurrentFilePath;

	public static XmeetVoiceRecorder getInstance() {
		if (mInstance == null) {
			synchronized (XmeetVoiceRecorder.class) {
				if (mInstance == null) { 
					mInstance = new XmeetVoiceRecorder();
				}
			}
		}
		return mInstance;
	}
	
	public interface AudioStateListener {
		void wellPrepared();
	}
	
	public AudioStateListener mAudioStateListener;
	
	public void setOnAudioStateListener(AudioStateListener listener) {
		mAudioStateListener = listener;
	}
	
	public void prepareAudio() {
		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		
		try {
            isPrepare = false;
            File dir = new File(DIR_NAME);
            if (!dir.exists()) {
            	dir.mkdirs();
            }
            String fileName = DIR_NAME + generateFileName();
            File file = new File(fileName);
 
            mCurrentFilePath = file.getAbsolutePath();
 
            mRecorder = new MediaRecorder();
            // 设置输出文件
            mRecorder.setOutputFile(mCurrentFilePath);
            // 设置MediaRecorder的音频源为麦克风
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 设置音频编码
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
 
            // 准备录音
            mRecorder.prepare();
            // 开始
            mRecorder.start();
            // 准备结束
            isPrepare = true;
            if (mAudioStateListener != null) {
                mAudioStateListener.wellPrepared();
            }
 
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private String generateFileName() {
		return UUID.randomUUID().toString() + ".amr";
	}
	
	public int getVoiceLevel(int maxlevel) {
		if (isPrepare) {
			try {
				return maxlevel * mRecorder.getMaxAmplitude() /32768 + 1;
			} catch (Exception e) {
				
			}
		}
		return 1;
	}
	
	public void release() {
		mRecorder.reset();
		mRecorder = null;
	}
	
	public void cancel() {
		release();
		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			file.delete();
			mCurrentFilePath = null;
		}
	}
	
	public String getCurrentFilePath() {
		return mCurrentFilePath;
	}
}
