package com.xmeet.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class XmeetUtil {
	public static int item_left = 0;
	public static int item_right = 1;
	
	public static int xmeet_title_layout = 7001;
	public static int xmeet_back_button = xmeet_title_layout + 1;
	public static int xmeet_xnest_name = xmeet_back_button + 1;
	public static int xmeet_user_name = xmeet_xnest_name + 1;
	public static int xmeet_progress = xmeet_user_name + 1;
	
	public static int xmeet_message_layout = 8001;
	public static int xmeet_message_send = xmeet_message_layout + 1;
	public static int xmeet_message_text = xmeet_message_send + 1;
	
	public static int xmeet_message_type = xmeet_message_text + 1;
	public static int xmeet_message_voice = xmeet_message_type + 1;
	
	public static int xmeet_voice_icon = xmeet_message_voice + 1;
	public static int xmeet_voice_voice = xmeet_voice_icon + 1;
	public static int xmeet_voice_label = xmeet_voice_voice + 1;
	
	public static int xmeet_listview = 9001;
	
	public static int xmeet_message_time = xmeet_listview + 1;
	public static int xmeet_message_head = xmeet_message_time + 1;
	public static int xmeet_message_username = xmeet_message_head + 1;
	public static int xmeet_message_payload = xmeet_message_username + 1;
	
	public static int xmeet_rename_edit = xmeet_message_payload + 1;
	public static int xmeet_rename_ok = xmeet_rename_edit + 1;
	public static int xmeet_rename_cancle = xmeet_rename_ok + 1;
	
	public static int xmeet_loading_image = xmeet_rename_cancle + 1;
	
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
	
	public static String stringToMD5(String string) {  
	    byte[] hash;  
	  
	    try {  
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));  
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	        return null;  
	    } catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	        return null;  
	    }  
	  
	    StringBuilder hex = new StringBuilder(hash.length * 2);  
	    for (byte b : hash) {  
	        if ((b & 0xFF) < 0x10)  
	            hex.append("0");  
	        hex.append(Integer.toHexString(b & 0xFF));  
	    }  
	  
	    return hex.toString();  
	} 
	
	public static String getRandomName() {
		String name ="";
		for(int i = 0; i < 3; i++)
		{
			name = name + (char) (Math.random ()*26+'A');
		}
		return "Android" + name;
	}
	
	public static String getUserFromPrefrence(Context context) {
		SharedPreferences sh = context.getSharedPreferences("xmeet_prefrence", Context.MODE_PRIVATE);
		return sh.getString("nickname", "");
	}

	public static void setUserFromPrefrence(Context context, String nickname) {
		SharedPreferences sh = context.getSharedPreferences("xmeet_prefrence", Context.MODE_PRIVATE);
		Editor dt = sh.edit();
		dt.putString("nickname", nickname);
		dt.commit();
	}
	
	public static byte[] File2byte(String filePath) {  
        byte[] buffer = null;  
        try  
        {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] header = new byte[]{0,0,0,0,0,0,0,1};
            bos.write(header);
            
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
}
