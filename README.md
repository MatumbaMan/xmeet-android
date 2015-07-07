# xmeet-android
xmeet project for android

使用方法：
1.将xmeet-android-0.1.0.jar和Java-WebSocket-1.3.1-SNAPSHOT.jar添加到libs目录下，并在Java Build Path中添加。
2.将layout目录中的文件拷贝到工程中的res/layout目录下（注意请不要有重名文件）
3.将drawable目录下文件拷贝到工程中的res/drawable-hdpi目录下（同样注意命名问题）
4.在res/values/styles.xml文件中（style.xml不存在请创建），加入如下style：
	<style name="loading_dialog" parent="android:style/Theme.Dialog">  
        <item name="android:windowFrame">@null</item>
        <item name="android:windowNoTitle">true</item>   
        <item name="android:windowBackground">@color/transparent_background</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowContentOverlay">@null</item>  
        <item name="android:backgroundDimEnabled">false</item>
        <item name="android:windowIsTranslucent">false</item>
        <item name="android:background">#00000000</item>
    </style> 
5.在res/values/color.xml文件中（color.xml不存在请创建），加入如下color：
	<color name="transparent_background">#00000000</color>
6.在AndroidManifest.xml中注册XmeetActivity，代码如下：
	<activity
        android:name="com.xmeet.android.XmeetActivity"
        android:label="@string/app_name" 
        android:screenOrientation="portrait">
    </activity>
（注：xmeet基于网络操作，请打开网络权限！）
7.使用xmeet的方法，首先实例化一个XmeetView，
	XmeetView view = new XmeetView();
然后打开xmeet，
	view.openXmeet(this);
大功告成。
当然，你还可以通过XmeetView提供的方法设置hostIp,昵称,聊天室id等，
	view.setGlobalDefaultHostId("");
	view.setNickName("");
	view.setXnestId("");
	如果你不清楚怎么设置，也可已不设置，使用默认
	
	了解更多请关注：<http://meet.xpro.im/>
