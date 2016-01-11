package com.xmeet.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.xmeet.android.XmeetParser.XmeetListener;

public class XmeetVoiceDownloader {

	private static XmeetVoiceDownloader mInstance;
	private final static String DIR_NAME = android.os.Environment.getExternalStorageDirectory() + "/xmeet/remote/";
	private XmeetListener mListener = null;
	private XmeetMessage mMessage = null;
	private String filePath = null;
	private String fileUrl = null;
	
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
        fileUrl = message.payload;
        filePath = getFileName(fileUrl);
        mMessage.payload = "audio:" + filePath;
        
        new FileDownloadThread().start();
	}
	
	private String getFileName(String urlStr) {
		urlStr = urlStr.replace("http://meet.xpro.im:8080/", "");
		String[] paths = urlStr.split("/");
		if (paths.length <= 1)
			return DIR_NAME + urlStr + ".amr";
		
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
			downloadFile();
//			String urlStr = mMessage.payload;  
//            String fileName = getFileName(urlStr); 
//            mMessage.payload = "audio:" + fileName;
//            OutputStream output=null;  
//            try {  
//                URL url=new URL(urlStr);  
//                HttpURLConnection conn=(HttpURLConnection)url.openConnection();  
//                //取得inputStream，并将流中的信息写入SDCard  
//                  
//                File file = new File(fileName);  
//                InputStream input = conn.getInputStream();  
//                if(file.exists()){  
//                    System.out.println("exits");  
//                } else {  
//                    file.createNewFile();//新建文件  
//                    output=new FileOutputStream(file);  
//                    //读取大文件  
//                    byte[] buffer=new byte[4*1024];  
//                    while(input.read(buffer)!=-1){  
//                        output.write(buffer);  
//                    }  
//                    output.flush();  
//                }  
//            } catch (MalformedURLException e) {  
//                e.printStackTrace();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }finally{  
//                try {  
//                        output.close();  
//                        System.out.println("success");  
//                        mListener.onMessage(mMessage);
//                    } catch (IOException e) {  
//                        System.out.println("fail");  
//                        e.printStackTrace();  
//                    }  
//            }  
		}
	}
	
	
	public void downloadFile() {
		HttpClient client = new DefaultHttpClient();
		
		HttpGet request = new HttpGet(fileUrl);
		HttpEntity entity = null;
		try {
			HttpResponse response = client.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			entity = response.getEntity();

			if (responseCode == HttpStatus.SC_OK || responseCode == HttpStatus.SC_PARTIAL_CONTENT) {
				String tempfile = filePath + ".tmp";
				if (writeToFile(entity, tempfile) )
					moveFile(tempfile, filePath);
			} else if (responseCode == HttpStatus.SC_NOT_FOUND) {
				System.out.println("传输失败，该文件不存在");
			} else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
				System.out.println("传输失败，会话过期");
			} else {
				System.out.println("传输失败，未知异常");
			}
		} catch (InterruptedIOException e) {
			System.out.println(e.toString());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				request.abort();
			} catch (Exception e) {
				System.out.println( e.getMessage() );
			}
		}
	}
	
	private final static int BUFFER_SIZE = 1024 * 4;
	boolean writeToFile(HttpEntity entity, String path) {
		if (entity == null) {
			System.out.println("传输失败，文件内容读取错误");
			return false;
		}
		
		File file = new File(path);
		InputStream input = null;
		RandomAccessFile accessFile = null;
		try {
			input = entity.getContent();
			file.createNewFile();//新建文件
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.seek(file.length());;
			
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = -1;
			
			while ((len = input.read(buffer)) != -1) {
				accessFile.write(buffer, 0, len);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (Exception e) {
					System.out.println( e.getMessage() );
				}
			}
		}
		return true;
	}

	boolean moveFile(String srcPath, String destPath) {
		File src = new File(srcPath);
		File dest = new File(destPath);
		try {
			dest.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}//新建文件
		if (dest.exists()) {
			src.renameTo(dest);
		}
		if (mListener != null)
			mListener.onMessage(mMessage);
		return true;
	}

}
