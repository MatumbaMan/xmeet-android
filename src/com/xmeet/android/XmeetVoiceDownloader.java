package com.xmeet.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.xmeet.android.XmeetParser.XmeetListener;

public class XmeetVoiceDownloader {

	private static XmeetVoiceDownloader mInstance;
	private final static String DIR_NAME = android.os.Environment.getExternalStorageDirectory() + "/xmeet/remote/";
	private XmeetListener mListener = null;
	private XmeetMessage mMessage = null;
	
	public static XmeetVoiceDownloader getInstance() {
		if (mInstance == null) {
			synchronized (XmeetVoiceDownloader.class) {
				if (mInstance == null) { 
					mInstance = new XmeetVoiceDownloader();
				}
			}
		}
		return mInstance;
	}
	
	public void downloadFile(XmeetListener listener, XmeetMessage message ) {
		mListener = listener;
		mMessage = message;
		
		File dir = new File(DIR_NAME);
        if (!dir.exists()) {
        	dir.mkdirs();
        }
        new FileDownloadThread().start();
	}
	
	private String getFileName(String urlStr) {
		urlStr = urlStr.replace("http://meet.xpro.im:8080/", "");
		String[] paths = urlStr.split("/");
		if (paths.length <= 1)
			return urlStr;
		
		String filePath = DIR_NAME;
		for (int i = 0; i < paths.length - 1; i++) {
			filePath += paths[i];
			new File(filePath).mkdir();
		}
		return filePath + "/" + paths[paths.length - 1] + ".amr";
	}
	
	private class FileDownloadThread extends Thread {
		@Override
		public void run() {
			String urlStr = mMessage.payload;  
            String fileName = getFileName(urlStr); 
            mMessage.payload = "audio:" + fileName;
            OutputStream output=null;  
            try {  
                URL url=new URL(urlStr);  
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();  
                //取得inputStream，并将流中的信息写入SDCard  
                  
                File file = new File(fileName);  
                InputStream input = conn.getInputStream();  
                if(file.exists()){  
                    System.out.println("exits");  
                } else {  
                    file.createNewFile();//新建文件  
                    output=new FileOutputStream(file);  
                    //读取大文件  
                    byte[] buffer=new byte[4*1024];  
                    while(input.read(buffer)!=-1){  
                        output.write(buffer);  
                    }  
                    output.flush();  
                }  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }finally{  
                try {  
                        output.close();  
                        System.out.println("success");  
                        mListener.onMessage(mMessage);
                    } catch (IOException e) {  
                        System.out.println("fail");  
                        e.printStackTrace();  
                    }  
            }  
		}
	}
}
