package com.xmeet.android;

import com.xmeet.android.XmeetVoiceRecorder.AudioStateListener;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class XmeetVoiceButton extends Button {
	

	private final int STATE_NORMAL = 0;
	private final int STATE_RECORDING = 1;
	private final int STATE_CANCLE = 2;
	
	private int mCurrentState = STATE_NORMAL;
	
	private XmeetVoiceDialog mDialog = null;
	private XmeetVoiceRecorder mAudioManager = null;
	
	private boolean isRecording = false;
	private float mTime;
	private boolean mReady;
	
	private static final int DISTANCE_Y_CANCEL = 50;
	
	
	public XmeetVoiceButton(Context context) {
		this(context, null);
	}
	
	public XmeetVoiceButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mDialog = new XmeetVoiceDialog(context);
		
		mAudioManager = XmeetVoiceRecorder.getInstance();
		mAudioManager.setOnAudioStateListener(new AudioStateListener() {
			
			@Override
			public void wellPrepared() {
				mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
			}
		});
		
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}
	
	 /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }
 
    private AudioFinishRecorderListener mAaudioFinishRecorderListener;
 
    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mAaudioFinishRecorderListener = listener;
    }
	
	
	private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_VOICE_CHANGED = 0x111;
    private static final int MSG_DIALOG_DIMISS = 0x112;
	/*获取音量大小线程*/
	private Runnable mGetVoiceLevelRunnable = new Runnable() {
		public void run() {
			while(isRecording) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	};
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:
				mDialog.showRecording();
				isRecording = true;
				//线程开启
				new Thread(mGetVoiceLevelRunnable).start();
				break;
			case MSG_VOICE_CHANGED:
				mDialog.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
				break;
			case MSG_DIALOG_DIMISS:
				mDialog.dismissRecording();
				break;
			}
		};
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		switch (action) {
        case MotionEvent.ACTION_DOWN:
            changeState(STATE_RECORDING);
            break;
        case MotionEvent.ACTION_MOVE:
 
            if (isRecording) {
                // 如果想要取消，根据x,y的坐标看是否需要取消
                if (wantToCancle(x, y)) {
                    changeState(STATE_CANCLE);
                } else {
                    changeState(STATE_RECORDING);
                }
            }
 
            break;
        case MotionEvent.ACTION_UP:
            if (!mReady) {
                reset();
                return super.onTouchEvent(event);
            }
            if (!isRecording || mTime < 0.6f) {
                mDialog.tooShort();
                mAudioManager.cancel();
                mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);// 延迟显示对话框
            } else if (mCurrentState == STATE_RECORDING) { // 正在录音的时候，结束
            	mDialog.dismissRecording();
                mAudioManager.release();
 
                if (mAaudioFinishRecorderListener != null) {
                    mAaudioFinishRecorderListener.onFinish(mTime,mAudioManager.getCurrentFilePath());
                }
 
            } else if (mCurrentState == STATE_CANCLE) { // 想要取消
            	mDialog.dismissRecording();
                mAudioManager.cancel();
            }
            reset();
            break;
        }
		return super.onTouchEvent(event);
	}
	
	/**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }
	
	private boolean wantToCancle(int x, int y) {
		if (x < 0 || x > getWidth()) { // 超过按钮的宽度
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
 
        return false;
	}

	private void changeState(int state) {
		if (mCurrentState != state) {
			mCurrentState = state;
			switch (state) {
            case STATE_NORMAL:
                setText("按住发送");
                break;
 
            case STATE_RECORDING:
                setText("手指上滑，取消发送");
                if (isRecording) {
                    mDialog.recording();
                }
                break;
 
            case STATE_CANCLE:
                setText("松开手指，取消发送");
                mDialog.wantToCancel();
                break;
            }
		}
	}
}
