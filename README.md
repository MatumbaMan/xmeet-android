###使用方法：
打开根目录下的USE文件夹 <br>
1.将xmeet-android-1.0.0.jar添加到libs目录下，并在Java Build Path中添加。 <br>
2.将drawable目录下文件拷贝到工程中的res/drawable-hdpi目录下（注意请不要有重名文件） <br>
3.在values/style.xml中添加如下代码<br>
```JAVA
<style name="xmeet_loading_dialog" parent="android:style/Theme.Dialog">  
        <item name="android:windowFrame">@null</item>  <!--边框--> 
        <item name="android:windowNoTitle">true</item>   
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsFloating">true</item>  <!--是否浮现在activity之上-->
        <item name="android:windowContentOverlay">@null</item>  
        <item name="android:backgroundDimEnabled">false</item>模糊 
        <item name="android:windowIsTranslucent">false</item>半透明
        <item name="android:background">#00000000</item>
    </style> 
```
4.在AndroidManifest.xml中加入如下代码
```JAVA
<activity
            android:name="com.xmeet.android.XmeetActivity"
            android:label="@string/app_name" 
            android:screenOrientation="portrait">
        </activity>
```
（注：xmeet基于网络操作等，请打开网络、语音和文件读取权限！） <br>
5.使用xmeet的方法，首先实例化一个XmeetView， <br>
```Java
	XmeetView view = new XmeetView();
```
然后打开xmeet， <br>
```Java
	view.openXmeet(this);
```
大功告成。 <br>
你还可以通过XmeetView提供的方法设置昵称， <br>
```Java
	view.setNickName(""); 
```
[了解更多](http://meet.xpro.im)