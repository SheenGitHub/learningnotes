# UI #
## 布局 ##
### FrameLayout ###
默认从区域的左上角(可以通过layout_gravity控制)，帧布局的大小由控件中最大的子控件决定

前景图像，永远不会被覆盖

- android:foreground
- android:foregroundGravity

### CoordinatorLayout ###

## 属性 ##
### Padding ###
TextView设置背景图 setBackgroundResource, padding在背景内

ImageView设置src图, setImageResource, padding在图片外

TextView设置背景图，会导致padding变化，需要注意
### RecyclerView ###
#### 自动换行的RecyclerView ####
重写LayoutManager

	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
	        detachAndScrapAttachedViews(recycler);
	        int sumWidth = getWidth();
	
	        int curLineWidth = 0, curLineTop = 0;
	        int lastLineMaxHeight = 0;
	        for (int i = 0; i < getItemCount(); i++) {
	            View view = recycler.getViewForPosition(i);
	            addView(view);
	            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
	            measureChildWithMargins(view, 0, 0);
	
	            int width = getDecoratedMeasuredWidth(view);
	            int height = getDecoratedMeasuredHeight(view);
	
	            curLineWidth += ( width+ lp.leftMargin);
	            if (curLineWidth <= sumWidth ) {
	                layoutDecorated(view, curLineWidth - width, curLineTop+lp.topMargin, curLineWidth, curLineTop + height + lp.topMargin);
	                lastLineMaxHeight = Math.max(lastLineMaxHeight, height+lp.topMargin);
	            } else {
	                curLineWidth = width + lp.leftMargin;
	                if (lastLineMaxHeight == 0) {
	                    lastLineMaxHeight = height + lp.topMargin;
	                }
	                curLineTop += lastLineMaxHeight;
	                layoutDecorated(view, 0 + lp.leftMargin, curLineTop + lp.topMargin, curLineWidth, curLineTop + height + lp.topMargin);
	                lastLineMaxHeight = height + lp.topMargin;
	            }
	        }
	    }

# Android Studio #

# Gradle #
## Android 配置Gradle ##
#### 安卓的gradle插件 ####

	buildscript {
	    repositories {
	        // Gradle 4.1 and higher include support for Google's Maven repo using
	        // the google() method. And you need to include this repo to download
	        // Android Gradle plugin 3.0.0 or higher.
	        google()
	        ...
	    }
	    dependencies {
	        classpath 'com.android.tools.build:gradle:3.2.1'
	    }
	}

#### 设置Gradle版本  ####

> You can specify the Gradle version in either the File Project Structure Project menu in Android Studio, or by editing the Gradle distribution reference in the gradle/wrapper/gradle-wrapper.properties file. The following example sets the Gradle version to 4.6 in the gradle-wrapper.properties file.

使用gradle wrapper时，如此设置gradle的版本

#### 使用本地Gradle ####

Setting->Build,Execution,Deployment->Gradle->Use local gradle distribution

## 地图 ##
### AMap ###
获取屏幕四角坐标


	//获取当前缩放等级(未开启定位图层,在fragment中oncreatview生命周期中无法获取到,可以在Onresume中获取) 
	float zoom = mAMap.getCameraPosition().zoom;    
	 
	 
	 
	VisibleRegion visibleRegion = mAMap.getProjection().getVisibleRegion();
	LatLng farLeft = visibleRegion.farLeft;     //可视区域的左上角。
	LatLng farRight = visibleRegion.farRight;   //可视区域的右上角。
	LatLng nearLeft = visibleRegion.nearLeft;   //可视区域的左下角。
	LatLng nearRight = visibleRegion.nearRight; //可视区域的右下角。
	LatLngBounds latLngBounds = visibleRegion.latLngBounds;   //由可视区域的四个顶点形成的经纬度范围
	LatLng southwest = latLngBounds.southwest;      //西南角坐标
	LatLng northeast = latLngBounds.northeast;      //东北角坐标
	 
	 
	//返回一个从地图位置转换来的屏幕位置--经纬度坐标转屏幕坐标
	Point point = mAMap.getProjection().toScreenLocation(latLng);
	int x=point.x;    //x轴坐标
	int y=point.y;  
#### 发布问题 ####
实际发布版本，第一次获取的当前定位是(0.0, 0.0),第一次将定位移到当前位置执行完，是无法获取屏幕范围坐标的

#### 不能resolve高德的依赖 ####
估计是翻墙的原因，privoxy状态不稳定的时候就会报错
# Q&A #
#### Could not resolve com.android.tools.build:gradle:3.0.1 ####
配置代理地址，地址为127.0.0.1不是0.0.0.0 

Gradle不支持socks协议，本地安装privoxy，将socks协议转成Http协议，勾选Http Proxy中勾选Http，端口从socks的1080换成privoxy的8118端口

#### AndroidStudio中出现Failed to resolve:com.android.support:appcompat-v7报错 ####
调低compiledSdkVersion 和targetSdkVersion与sdk工具版本相同，调整appcompat-v[N]:[sdk-tool-version]+

#### Resources$NotFoundException: String resource ID #0x0 ####
setText() 参数为int型，系统去查找资源而不是直接设置文字

#### Android 3.2 Read Time Out ####
项目的jdk使用本地的jdk，而不是自带的openjdk

#### Android Studio 测试debug.keystore ####
用来生成可以key

C:\Users\admin\.android文件夹下

#### 生成高德发布版SHA1码 ####
keytool -list -v -keystore [keystore路径]

生成发布版的keystore文件(.jks后缀)

Build->Generate Singed Bundle/APK->CREATENEW  填写相应信息

#### 导入AAR包 ####
将 aar文件放入模块的libs文件夹下，模块的build.gradle中加入

	repositories {
	    flatDir {
	        dirs 'libs'
	    }
	}

dependencies 中加入

	implementation(name:'[包名]',ext:'aar')

#### ButterKinfe问题 ####
在moudle的gradle中

	implementation 'com.jakewharton:butterknife:8.4.0'
	annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'

添加插件

	apply plugin: 'com.jakewharton.butterknife'

在项目的gradle中

	dependencies {
	    classpath 'com.android.tools.build:gradle:3.0.0'
	    classpath 'com.jakewharton:butterknife-gradle-plugin:8.4.0'
	}

**升级3.0之后的问题，暂时只能降至8.4版本**

#### Could not get unknown property 'packageForR' for task ':app:processDebugResour ####

删除module中的apply plugin

#### @和?区别以及?attr/**与@style/**等的区别 ####
- 使用@表示使用固定的style，而不会跟随Theme改变，这style可以在对应的style.xml中找到。
- 使用？表示从Theme中查找引用的资源名，这个google叫预定义样式，用在多主题时的场景，属性值会随着主题而改变。（？需要和attr配合使用）

#### 自定义button的大小 ####
控件默认设置了minHeight,minWidth,设置为0之后就可以设置自己要的参数

#### AAPT2 error: check logs for details ####
查看build下 ab/按钮，查看具体报错信息

### 通过异步网络请求的添加的List中存在null的项目导致的问题 ###
使用synchronized obj加锁 或者使用synchronizedList 

#### 监听器不起作用 ####
可能监听事件在其他地方重新设置了监听器setXXXListener