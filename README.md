# AnymoRefreshView
Just a refresh demo<br>
一个简单的弹性下拉刷新控件，还未用于实际项目中，欢迎大家一起帮助完善此控件。

![](https://i.imgur.com/gFObFlZ.gif)

# 使用方法 #
## 第一步 ##
Project.gradle中添加<br>

allprojects {<br>
&nbsp;&nbsp;&nbsp;&nbsp;repositories {<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;maven { url 'https://jitpack.io' }<br>
&nbsp;&nbsp;&nbsp;&nbsp;}<br>
}<br>
app.gradle中添加<br>

dependencies {<br>
&nbsp;&nbsp;&nbsp;&nbsp;implementation 'com.github.fengmo94:AnymoRefreshView:V0.9'<br>
}
## 第二步 ##
布局添加,PullRefreashLayout内部必须包含一个控件，否则无效果，会报错：<br> 
 MaxheaderView header最大宽度<br> 
Radius 用的半径<br>
RadiusRound 圆环的宽度<br>
StartAngle 圆环开始旋转的位置<br>

<com.example.libflexiblerefreshview.PullRefreashLayout<br>
&nbsp;&nbsp;&nbsp;&nbsp;android:id="@+id/prl"<br>
&nbsp;&nbsp;&nbsp;&nbsp;android:layout_width="match_parent"<br>
&nbsp;&nbsp;&nbsp;&nbsp;android:layout_height="match_parent"<br>
&nbsp;&nbsp;&nbsp;&nbsp;app:MaxheaderView="300"<br>
&nbsp;&nbsp;&nbsp;&nbsp;app:Radius="100"<br>
&nbsp;&nbsp;&nbsp;&nbsp;app:RadiusRound="110"<br>
&nbsp;&nbsp;&nbsp;&nbsp;app:StartAngle="-90"><br>
&nbsp;&nbsp;&nbsp;&nbsp;<ListView<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;android:id="@+id/lv"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;android:layout_width="match_parent"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;android:layout_height="match_parent"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;android:background="#ffffffff"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;android:dividerHeight="0dp"/><br>
</com.example.libflexiblerefreshview.PullRefreashLayout>
