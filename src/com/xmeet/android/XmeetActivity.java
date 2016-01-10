package com.xmeet.android;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import com.xmeet.android.XmeetParser.XmeetListener;
import com.xmeet.android.XmeetVoiceButton.AudioFinishRecorderListener;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class XmeetActivity extends Activity {

	private XmeetHanlder mClient 	= null;
	
	private TextView mTitle 		= null;
	private ListView mListView 		= null;
	private EditText mMessageEdit 	= null;
	private TextView mSendButton 	= null;
	private TextView mMessageType	= null;
	private TextView mUserView 		= null;
	
	private XmeetVoiceButton mMessageVoice	= null;
	
//	private XmeetDialog loadingDialog 	= null;
	private ProgressBar mProgress = null;
	
	private XmeetAdapter mAdapter 	= null;
	
	private final static String XNEST_ID =  "912ec803b2ce49e4a541068d495ab570";

	private String mXnestId = null;
	private String mNickName = null;
	
	private boolean mIsVoice = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		
		XmeetRootView view = new XmeetRootView(this);
		setContentView(view);
		
		initConfig();

		initViews();
		
		SocketThread thread= new SocketThread();
		thread.start();
	}

	private void initConfig() {
		Intent intent = getIntent();
		Bundle bd = intent.getExtras();
		
		if(bd == null)
			return;

		setXnestId(bd.getString("nest"));
		setNickName(bd.getString("nick"));
	}
	
	private class SocketThread extends Thread{
		@Override
		public void run() {
			Looper.prepare();
			initialize();
			super.run();
		}
	}
	
	private void setXnestId(String id) {
		if (id == null || id == "" ||  id.isEmpty())
			return;
		mXnestId = id;
	}
	
	private void setNickName(String name) {
		if (name == null || name == "" ||  name.isEmpty())
			return;
		mNickName = name;
	}
	
	private String buildUri() {
		String nick = null;
		if (mNickName != null && mNickName != "") {
			nick = mNickName;
		} else {
			nick = XmeetUtil.getUserFromPrefrence(this);
		}
		String name = nick;
		try {
			name = URLEncoder.encode(nick, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ("ws://meet.xpro.im:8080/xgate/websocket/") //
				+ (mXnestId == null ? XNEST_ID : mXnestId) 
				+ "?nickname=" 
				+ name;
	}
	
	private void showVoice() {
		mMessageVoice.setVisibility(View.VISIBLE);
		mMessageEdit.setVisibility(View.INVISIBLE);
		mMessageType.setBackgroundResource(XmeetResource.getIdByName(this, "drawable", "xmeet_message_write"));
		hiddenKeyboard();
	}
	
	private void showWrite() {
		mMessageVoice.setVisibility(View.INVISIBLE);
		mMessageEdit.setVisibility(View.VISIBLE);
		mMessageType.setBackgroundResource(XmeetResource.getIdByName(this, "drawable", "xmeet_message_voice"));
		shwoKeyboard();
	}
	
	private void hiddenKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(mMessageEdit.getWindowToken(), 0);
		}				
		
		mMessageEdit.clearFocus();
	}
	
	private void shwoKeyboard() {
		mMessageEdit.setFocusable(true);
		mMessageEdit.setFocusableInTouchMode(true);
		mMessageEdit.requestFocus();
		
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(mMessageEdit, 0);
	}
	
	private void initViews() {
		createDialog();
		mTitle = (TextView) findViewById( XmeetUtil.xmeet_xnest_name);
		mListView = (ListView) findViewById(XmeetUtil.xmeet_listview);
		mMessageEdit = (EditText) findViewById(XmeetUtil.xmeet_message_text);
		mSendButton = (TextView) findViewById(XmeetUtil.xmeet_message_send);
		mUserView = (TextView) findViewById(XmeetUtil.xmeet_user_name);
		mProgress = (ProgressBar) findViewById(XmeetUtil.xmeet_progress);
		
		mMessageVoice = (XmeetVoiceButton) findViewById(XmeetUtil.xmeet_message_voice);
		mMessageType = (TextView) findViewById(XmeetUtil.xmeet_message_type);
		
		findViewById(XmeetUtil.xmeet_back_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				if (mClient != null) {
					mClient.removeListener();
					mClient.close();
				}
			}
		});
		
		mSendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				sendMessage();
			}
		});
		
		mMessageEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND ||
						(event != null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
					sendMessage();
				}
				return false;
			}
		});
		
		mUserView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showRenameAlert();
			}
		});
		
		mMessageType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mIsVoice = !mIsVoice;
				if ( mIsVoice )
					showVoice() ;
				else
					showWrite();
			}
		});
		
		mMessageVoice.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {
			
			@Override
			public void onFinish(float seconds, String filePath) {
				sendVoice(filePath);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				XmeetMessage message = (XmeetMessage) mAdapter.getItem(arg2);
				if (message != null && message.payload.startsWith("audio:")) {
					XmeetVoicePlaying.getInstance().play(message.payload.replace("audio:", ""));
				}
			}
		});

		showVoice();
	}
	
	private void sendVoice(String file) {
		if (mClient == null) 
			return;
		
		byte[] message = XmeetUtil.File2byte(file);
		if (message != null) {
			mClient.send(message);
		}
		
	}
	
	private void sendMessage() {
		if (mClient == null) 
			return;
		
		String message = mMessageEdit.getText().toString();
		if (message != null && 
			message != "" && 
			!message.isEmpty()) {
			mClient.send(message);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mMessageEdit.setText("");
				}
			});
		}
	}
	
	private void initialize() {
		try {
			showLoading();
			
			mClient = new XmeetHanlder(new URI(buildUri()), this);
			mClient.addListener(mListener);
			mClient.connectBlocking();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			showToast(e.getReason().toString());
			hiddenLoading();
		} catch (InterruptedException e) {
			e.printStackTrace();
			showToast(e.toString());
			hiddenLoading();
		}
	}
	
	private void showToast(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				Toast.makeText(XmeetActivity.this, message, Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	
	private XmeetListener mListener = new XmeetListener(){

		@Override
		public void onOpen() {
			
		}
		
		@Override
		public void onClose() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mTitle.setText("未连接");
					showToast("连接xmeet失败，请检查网络连接之后再试");
					finish();
					}
				});
		}

		@Override
		public void onJoin(XmeetMessage message) {
			addMessage(message);
		}

		@Override
		public void onLeave(XmeetMessage message) {
			addMessage(message);
		}

		@Override
		public void onMessage(XmeetMessage message) {
			addMessage(message);
		}

		@Override
		public void onChangeName(XmeetMessage message) {
			addMessage(message);
			XmeetUtil.setUserFromPrefrence(XmeetActivity.this, message.nickName);
		}

		@Override
		public void onHistory(List<XmeetMessage> messages) {
			addMessage(messages);
			hiddenLoading();
		}

		@Override
		public void onError(String errmsg) {
			
		}

	};
	
	private void addMessage(final XmeetMessage message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mAdapter == null) {
					mAdapter = new XmeetAdapter(XmeetActivity.this, message);
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.addMessage(message);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mAdapter.getCount() - 1);
				}
			}
		});
		
	}
	
	private void addMessage(final List<XmeetMessage> messages) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mAdapter == null) {
					mAdapter = new XmeetAdapter(XmeetActivity.this, messages);
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.addMessage(messages);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mAdapter.getCount() - 1);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmeet, menu);
		return true;
	}
	
//	private String getUserFromPrefrence() {
//		SharedPreferences sh = getSharedPreferences("xmeet_prefrence", Context.MODE_PRIVATE);
//		return sh.getString("nickname", "");
//	}
//
//	private void setUserFromPrefrence(String nickname) {
//		SharedPreferences sh = getSharedPreferences("xmeet_prefrence", Context.MODE_PRIVATE);
//		Editor dt = sh.edit();
//		dt.putString("nickname", nickname);
//		dt.commit();
//	}
	
	private void showRenameAlert() {		
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(new XmeetRenameView(this));
		
		final EditText text = (EditText) dialog.findViewById(XmeetUtil.xmeet_rename_edit);
		dialog.findViewById(XmeetUtil.xmeet_rename_cancle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.findViewById(XmeetUtil.xmeet_rename_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				String message = text.getText().toString();
				if (message != null && 
					message != "" && 
					!message.isEmpty()) {
					mClient.send("@changename:" + message);
				}
			}
		});
		
		dialog.show();
	}
	
	private void showLoading() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				loadingDialog.show();
				mProgress.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void hiddenLoading() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				loadingDialog.dismiss();
				mProgress.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	private void createDialog() {
//		loadingDialog = new XmeetDialog(this);
	}
	
}
