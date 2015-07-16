###使用方法：
打开根目录下的USE文件夹 <br>
1.将xmeet-android-1.0.0.jar添加到libs目录下，并在Java Build Path中添加。 <br>
2.将drawable目录下文件拷贝到工程中的res/drawable-hdpi目录下（注意请不要有重名文件） <br>
3.在AndroidManifest.xml中加入如下代码
```JAVA
<activity
            android:name="com.xmeet.android.XmeetActivity"
            android:label="@string/app_name" 
            android:screenOrientation="portrait">
        </activity>
```
（注：xmeet基于网络操作，请打开网络权限！） <br>
4.使用xmeet的方法，首先实例化一个XmeetView， <br>
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