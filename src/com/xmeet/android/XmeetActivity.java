package com.xmeet.android;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.java_websocket.drafts.Draft_17;

import com.xmeet.android.XmeetParser.XmeetListener;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class XmeetActivity extends Activity {

	private XmeetHanlder mClient = null;
	
	private TextView mTitle = null;
	private ListView mListView = null;
	private EditText mMessageEdit = null;
	private TextView mSendButton = null;
	private TextView mUserView = null;
	private Dialog loadingDialog = null;
	
	private XmeetAdapter mAdapter = null;
	
	private final static String XNEST_ID =  "14009e12d791e664fc0175aecb31d833";
	private final static String NICK_NAME = XmeetUtil.getRandomName();
	
//	private String mHostId = null;
	private String mXnestId = null;
	private String mNickName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		
		setContentView(XmeetResource.getIdByName(getApplicationContext(), "layout", "activity_xmeet"));
		
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

//		setGlobalDefaultHostId(bd.getString("host"));
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
	
	private String buildUrl() {
		String nick = null;
		if (mNickName != null && mNickName != "") {
			nick = mNickName;
		} else {
			nick = getUserFromPrefrence();
		}
		
		return ("ws://meet.xpro.im:8080/xgate/websocket/") //
				+ (mXnestId == null ? XNEST_ID : mXnestId) 
				+ "?nickname=" 
				+ nick;
	}
	
	private void initViews() {
		createDialog();
		mTitle = (TextView) findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_xnest_name"));
		mListView = (ListView) findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_listview"));
		mMessageEdit = (EditText) findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_message_edit"));
		mSendButton = (TextView) findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_message_send"));
		mUserView = (TextView) findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_user_name"));
		
		findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_back_button")).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				mClient.removeListener();
				mClient.close();
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
		
//		mMessageEdit.setOnKeyListener(new OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
//				if (arg1 == KeyEvent.KEYCODE_ENTER) {
//					sendMessage();
//				}
//				return false;
//			}
//		});
		mUserView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showRenameAlert();
			}
		});

	}
	
	private void sendMessage() {
		String message = mMessageEdit.getText().toString();
		if (message != null && message != "" && !message.isEmpty()) {
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
			mClient = new XmeetHanlder(new URI(buildUrl()), new Draft_17());
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
			setUserFromPrefrence(message.nickName);
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
	
	private String getUserFromPrefrence() {
		SharedPreferences sh = getSharedPreferences("xmeet_prefrence", Context.MODE_PRIVATE);
		return sh.getString("nickname", NICK_NAME);
	}

	private void setUserFromPrefrence(String nickname) {
		SharedPreferences sh = getSharedPreferences("xmeet_prefrence", Context.MODE_PRIVATE);
		Editor dt = sh.edit();
		dt.putString("nickname", nickname);
		dt.commit();
	}
	
	private void showRenameAlert() {		
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.xmeet_rename_alert);
		
		final EditText text = (EditText) dialog.findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_rename_edit"));
		dialog.findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_rename_cancle")).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_rename_ok")).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				String message = text.getText().toString();
				if (message != null && message != "" && !message.isEmpty()) {
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
				loadingDialog.show();
			}
		});
	}
	
	private void hiddenLoading() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadingDialog.dismiss();
			}
		});
	}
	
	private void createDialog() {
		loadingDialog = new Dialog(this, XmeetResource.getIdByName(getApplicationContext(), "style", "loading_dialog"));// 创建自定义样式dialog  
		View v = LayoutInflater.from(this).inflate(XmeetResource.getIdByName(getApplicationContext(), "layout", "xmeet_loading"), null);
		
		loadingDialog.setContentView(v);
		
		ImageView imageView = (ImageView) v.findViewById(XmeetResource.getIdByName(getApplicationContext(), "id", "xmeet_loading_image"));
		AnimationDrawable animationDrawable = (AnimationDrawable)imageView.getBackground();  
        
        //开始或者继续动画播放  
        animationDrawable.start();   

        loadingDialog.setCancelable(false);
	}
}
