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

# TensorFlow Lite #
[https://tensorflow.google.cn/lite/guide](https://tensorflow.google.cn/lite/guide)

TensorFlow Lite is a set of tools to help developers run TensorFlow models on mobile, embedded, and IoT devices. It enables on-device machine learning inference with low latency and a small binary size.

TensorFlow Lite consists of two main components:

- The TensorFlow Lite interpreter, which runs specially optimized models on many different hardware types, including mobile phones, embedded Linux devices, and microcontrollers.
- The TensorFlow Lite converter, which converts TensorFlow models into an efficient form for use by the interpreter, and can introduce optimizations to improve binary size and performance.

#### Machine learning at the edge ####

TensorFlow Lite is designed to make it easy to perform machine learning on devices, "at the edge" of the network, instead of sending data back and forth from a server. For developers, performing machine learning on-device can help improve:

- Latency: there's no round-trip to a server
- Privacy: no data needs to leave the device
- Connectivity: an Internet connection isn't required
- Power consumption: network connections are power hungry

#### Key features ####

- Interpreter tuned for on-device ML, supporting a set of core operators that are optimized for on-device applications, and with a small binary size.
- Diverse platform support, covering Android and iOS devices, embedded Linux, and microcontrollers, making use of platform APIs for accelerated inference.
- APIs for multiple languages including Java, Swift, Objective-C, C++, and Python.
- High performance, with hardware acceleration on supported devices, device-optimized kernels, and pre-fused activations and biases.
- Model optimization tools, including quantization, that can reduce size and increase performance of models without sacrificing accuracy.
- Efficient model format, using a FlatBuffer that is optimized for small size and portability.
- Pre-trained models for common machine learning tasks that can be customized to your application.

## TensorFlow Lite Android 图像分类 ##

### 获取相机输入 ### 
CameraActivity.java中定义了获取相机输入的函数，还可以获取用户UI偏好。
### 分类器 ###
Classfier包括了处理相机输入和运行推断的主要复杂逻辑。

Classfier实现了静态方法create，根据提供的模型初始化合适的子类

#### 载入模型并创建解释器 ####
执行推断需要载入一个模型文件并初始化解释器，在Classifier的构造器执行这个过程并载入类型标签列表。设备类型和线程数等信息通过Interpreter.Options实例出入构造函数来配置解释器
	
	protected Classifier(Activity activity, Device device, int numThreads) throws
	    IOException {
	  tfliteModel = FileUtil.loadMappedFile(activity, getModelPath());
	  switch (device) {
	    case NNAPI:
	      nnApiDelegate = new NnApiDelegate();
	      tfliteOptions.addDelegate(nnApiDelegate);
	      break;
	    case GPU:
	      gpuDelegate = new GpuDelegate();
	      tfliteOptions.addDelegate(gpuDelegate);
	      break;
	    case CPU:
	      break;
	  }
	  tfliteOptions.setNumThreads(numThreads);
	  tflite = new Interpreter(tfliteModel, tfliteOptions);
	  labels = FileUtil.loadLabels(activity, getLabelPath());

FileUtil.loadMappedFile 执行预载入和模型文件的内存映射，加快了装载时间减少了内存中的脏数据页，并返回一个包含模型的MappedByteBuffer

MappedByteBuffer和Interperter.Options对象一起被传给Interpreter的构造函数。

#### 位图图片的预处理 ####
Classfier的构造函数中，将相机的位图图片转换成TensorImage格式，以便更高效处理和预处理。

	/** Loads input image, and applys preprocessing. */
	private TensorImage loadImage(final Bitmap bitmap, int sensorOrientation) {
	  // Loads bitmap into a TensorImage.
	  image.load(bitmap);
	
	  // Creates processor for the TensorImage.
	  int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
	  int numRoration = sensorOrientation / 90;
	  ImageProcessor imageProcessor =
	      new ImageProcessor.Builder()
	          .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
	          .add(new ResizeOp(imageSizeX, imageSizeY, ResizeMethod.BILINEAR))
	          .add(new Rot90Op(numRoration))
	          .add(getPreprocessNormalizeOp())
	          .build();
	  return imageProcessor.process(inputImageBuffer);
	}

#### 分配输出对象 ####
初始化TensorBuffer作为模型的输出
	
	/** Output probability TensorBuffer. */
	private final TensorBuffer outputProbabilityBuffer;
	
	//...
	// Get the array size for the output buffer from the TensorFlow Lite model file
	int probabilityTensorIndex = 0;
	int[] probabilityShape =
	    tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, 1001}
	DataType probabilityDataType =
	    tflite.getOutputTensor(probabilityTensorIndex).dataType();
	
	// Creates the output tensor and its processor.
	outputProbabilityBuffer =
	    TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
	
	// Creates the post processor for the output probability.
	probabilityProcessor =
	    new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

#### 运行推断 ####
	tflite.run(inputImageBuffer.getBuffer(),
	    outputProbabilityBuffer.getBuffer().rewind());

#### 识别图像 ####
不同于直接调用run函数，recognizeImage方法接收一个位图和传感器方向，运行推断，并返回Recognition实例，各自对应一个标签，该方法返回的数目由MAX_RESULTS限制，默认是3。

使用处理后正规化方法，置信度被转换成成0到1之间表示用给定类别表征图片。

	Map<String, Float> labeledProbability =
	    new TensorLabel(labels,
	        probabilityProcessor.process(outputProbabilityBuffer))
	        .getMapWithFloatValue();

优先队列被用于排序

	/** Gets the top-k results. */
	private static List<Recognition> getTopKProbability(
	    Map<String, Float> labelProb) {
	  // Find the best classifications.
	  PriorityQueue<Recognition> pq =
	      new PriorityQueue<>(
	          MAX_RESULTS,
	          new Comparator<Recognition>() {
	            @Override
	            public int compare(Recognition lhs, Recognition rhs) {
	              // Intentionally reversed to put high confidence at the head of
	              // the queue.
	              return Float.compare(rhs.getConfidence(), lhs.getConfidence());
	            }
	          });
	
	  for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
	    pq.add(new Recognition("" + entry.getKey(), entry.getKey(),
	               entry.getValue(), null));
	  }
	
	  final ArrayList<Recognition> recognitions = new ArrayList<>();
	  int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
	  for (int i = 0; i < recognitionsSize; ++i) {
	    recognitions.add(pq.poll());
	  }
	  return recognitions;
	}
# Q&A #
#### Could not resolve com.android.tools.build:gradle:3.0.1 ####
配置代理地址，地址为127.0.0.1不是0.0.0.0 

Gradle不支持socks协议，本地安装privoxy，将socks协议转成Http协议，勾选Http Proxy中勾选Http，端口从socks的1080换成privoxy的8118端口

*不适用代理*

关闭setting中的Http Proxy，删除用户文件夹下.gradle文件夹中gradle.properties中的代理设置(**配置中的代理设置在编译的时候是有效的**)

#### AndroidStudio中出现Failed to resolve:com.android.support:appcompat-v7报错 ####
#### aapt.v2.Aapt2Exception: Android resource linking failed ####
#### error: resource android:attr/colorError not found. ####
**调低compiledSdkVersion 和targetSdkVersion与sdk工具版本相同，调整appcompat-v[N]:[sdk-tool-version]+**

#### Failed to notify build listener ####
是gradle5.0和android studio3.2不兼容的问题，解决方案是升级android studio到3.3x

#### Resources$NotFoundException: String resource ID #0x0 ####
setText() 参数为int型，系统去查找资源而不是直接设置文字

#### Execution failed for task ':app:transformClassesAndResourcesWithR8ForRelease'.
> com.android.tools.r8.CompilationFailedException: Compilation failed to complete ####

   xxx
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

#### AndroidStudio识别不了手机 ####
安装相应品牌手机android USB驱动
 
#### 安卓保持屏幕常亮 ####
FLAG_KEEP_SCREEN_ON

	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);