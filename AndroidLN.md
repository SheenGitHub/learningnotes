# Android学习笔记 #
Google Android 架构

[http://gityuan.com/android/](http://gityuan.com/android/)

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh6vij2gtqj20k70gok31.jpg)

## Android架构 ##

Android系统启动过程由上图从下往上的一个过程：Loader -> Kernel -> Native -> Framework -> App

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh6vkzrzm7j21ay16p43l.jpg)

### Loader层 ###
Boot Loader：这是启动Android系统之前的引导程序，主要是检查RAM，初始化硬件参数等功能。

### Kernel层 ###
idle进程, 系统初始化过程Kernel由无到有开创的第一个进程

kthreadd进程是所有内核进程的鼻祖

### Native层 ###
init进程是所有用户进程的鼻祖

Zygote是所有Java进程的父进程

### Framework层 ###
System Server是Zygote孵化的第一个进程

### App层 ###
Zygote进程孵化出的第一个App进程是Launcher，这是用户看到的桌面App

## 通信方式 ##
IPC （Inter-Process Communication）进程间通信

Linux管道、消息队列、共享内存、套接字、信号量、信号，Android额外还有 Binder IPC，Zygote进程采用Socket机制

system server、 media server 以及上层App之间更多采用Binder IPC, 进程主线程和工作线程之间采用Handler机制

### Binder ###
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fh7lx9sez1j20pl0cgjs1.jpg)
### Socket ###
### Handler ###
Handler消息机制是由一组MessageQueue、Message、Looper、Hanlder共同组成

那handler消息机制能否用于进程间通信？答案是不能，Handler只能用于共享内存地址空间的两个线程间通信，即同进程的两个线程间通信。

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fh7mku0s8nj20qg08qq3f.jpg)

## Binder ##
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fh7or8bqjhj20hv0b70t0.jpg)

> 每个Android的进程，只能运行在自己进程所拥有的虚拟地址空间。对应一个4GB的虚拟地址空间，其中3GB是用户空间，1GB是内核空间，当然内核空间的大小是可以通过参数配置调整的。对于用户空间，不同进程之间彼此是不能共享的，而内核空间却是可共享的。Client进程向Server进程通信，恰恰是利用进程间可共享的内核内存空间来完成底层通信工作的，Client端与Server端进程往往采用ioctl等方法跟内核空间的驱动进行交互。

图中Client/Server/ServiceManage之间的相互通信都是基于Binder机制。

- 注册服务(addService)：Server进程要先注册Service到ServiceManager。该过程：Server是客户端，ServiceManager是服务端。
- 获取服务(getService)：Client进程使用某个Service前，须先向ServiceManager中获取相应的Service。该过程：Client是客户端，ServiceManager是服务端。
- 使用服务：Client根据得到的Service信息建立与Service所在的Server进程通信的通路，然后就可以直接与Service交互。该过程：client是客户端，server是服务端。

> 图中的Client,Server,Service Manager之间交互都是虚线表示，是由于它们彼此之间不是直接交互的，而是都通过与Binder驱动进行交互的，从而实现IPC通信方式。其中Binder驱动位于内核空间，Client,Server,Service Manager位于用户空间。Binder驱动和Service Manager可以看做是Android平台的基础架构，而Client和Server是Android的应用层，开发人员只需自定义实现client、Server端，借助Android的基本平台架构便可以直接进行IPC通信。

### C/S模式 ###
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fh7p6hukm6j20a107qq37.jpg)

> client端：BpBinder.transact()来发送事务请求；
> server端：BBinder.onTransact()会接收到相应事务

### Binder 驱动 ###
> Binder驱动是Android专用的，但底层的驱动架构与Linux驱动一样。binder驱动在以misc设备进行注册，作为虚拟字符设备，没有直接操作硬件，只是对设备内存的处理。主要是驱动设备的初始化(binder_init)，打开 (binder_open)，映射(binder_mmap)，数据操作(binder_ioctl)。

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fh7pb7noc1j20af06rdfy.jpg)

#### 系统调用 ####

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fh7pebmbi3j209s09iwew.jpg)

## Activity启动 ##
![](https://raw.githubusercontent.com/sucese/android-open-source-project-analysis/master/art/app/component/activity_start_flow.png)

## Android View ##
[深入理解 Android 之 View 的绘制流程](http://www.cnblogs.com/jycboy/p/6219915.html)

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fhmxf08aodj20ve0g0thk.jpg)

### Window ###
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ce6e958a4d7049eea68425b4383052bf~tplv-k3u1fbpfcp-zoom-1.image)
这个抽象类包含了三个核心组件：

- WindowManager.LayoutParams: 窗口的布局参数；
- Callback: 窗口的回调接口，通常由Activity实现；
- ViewTree: 窗口所承载的控件树。

Android中Window的具体实现（也是唯一实现）——PhoneWindow

我们平时调用**setContentView()方法**设置Activity的用户界面时，实际上就完成了对所关联的PhoneWindow的ViewTree的设置。

我们还可以通过Activity类的**requestWindowFeature()方法**来定制Activity关联PhoneWindow的外观，这个方法实际上做的是把我们所请求的窗口外观特性存储到了PhoneWindow的mFeatures成员中，在窗口绘制阶段生成外观模板时，会根据mFeatures的值绘制特定外观。

### 三个阶段 ###
View的整个绘制流程可以分为以下三个阶段：

- measure: 判断是否需要重新计算View的大小，需要的话则计算；
- layout: 判断是否需要重新计算View的位置，需要的话则计算；
- draw: 判断是否需要重新绘制View，需要的话则重绘制。


这三个子阶段可以用下图来描述：

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fhmy9i4kn2j20lj05sdi8.jpg)

#### MeasureSpec ####
MeasureSpec是一个32位整数，由SpecMode和SpecSize两部分组成，其中，高2位为SpecMode，低30位为SpecSize。SpecMode为测量模式，SpecSize为相应测量模式下的测量尺寸。View（包括普通View和ViewGroup）的SpecMode由本View的LayoutParams结合父View的MeasureSpec生成。
SpecMode的取值可为以下三种：

- EXACTLY: 对子View提出了一个确切的建议尺寸（SpecSize）；
- AT_MOST: 子View的大小不得超过SpecSize；
- UNSPECIFIED: 对子View的尺寸不作限制，通常用于系统内部。

// 若mPrivateFlags中包含PFLAG_FORCE_LAYOUT标记，则强制重新布局
// 比如调用View.requestLayout()会在mPrivateFlags中加入此标记

从measure()方法的源码中我们可以知道，只有以下两种情况之一，才会进行实际的测量工作：

- forceLayout为true：这表示强制重新布局，可以通过View.requestLayout()来实现；
- needsLayout为true，这需要specChanged为true（表示本次传入的MeasureSpec与上次传入的不同），并且以下三个条件之一成立：
    - sAlwaysRemeasureExactly为true: 该变量默认为false；
    - isSpecExactly为false: 若父View对子View提出了精确的宽高约束，则该变量为true，否则为false
    - matchesSpecSize为false: 表示父View的宽高尺寸要求与上次测量的结果不同

ParentMeasureSpec的SpecMode为EXACTLY时，表示父View对子View指定了确切的宽高限制。此时根据子View的LayoutParams的不同，分以下三种情况：

- 具体大小（childDimension）：这种情况下令子View的SpecSize为childDimension，即子View在LayoutParams指定的具体大小值；令子View的SpecMode为EXACTLY，即这种情况下若该子View为容器View，它也有能力给其子View指定确切的宽高限制（子View只能在这个宽高范围内），若为普通View，它的最终测量大小就为childDimension。
- match_parent：此时表示子View想和父View一样大。这种情况下得到的子View的SpecMode与上种情况相同，只不过SpecSize为size，即父View的剩余可用大小。
- wrap_content: 这表示了子View想自己决定自己的尺寸（根据其内容的大小动态决定）。这种情况下子View的确切测量大小只能在其本身的onMeasure()方法中计算得出，父View此时无从知晓。所以暂时将子View的SpecSize设为size（父View的剩余大小）；令子View的SpecMode为AT_MOST，表示了若子View为ViewGroup，它没有能力给其子View指定确切的宽高限制，毕竟它本身的测量宽高还悬而未定。

当ParentMeasureSpec的SpecMode为AT_MOST时，我们也可以根据子View的LayoutParams的不同来分三种情况讨论：

- 具体大小：这时令子View的SpecSize为childDimension，SpecMode为EXACTLY。
- match_parent：表示子View想和父View一样大，故令子View的SpecSize为size，但是由于父View本身的测量宽高还无从确定，所以只是暂时令子View的测量结果为父View目前的可用大小。这时令子View的SpecMode为AT_MOST。
- wrap_content：表示子View想自己决定大小（根据其内容动态确定）。然而这时父View还无法确定其自身的测量宽高，所以暂时令子View的SpecSize为size，SpecMode为AT_MOST。
- 从上面的分析我们可以得到一个通用的结论，当子View的测量结果能够确定时，子View的SpecMode就为EXACTLY；当子View的测量结果还不能确定（只是暂时设为某个值）时，子View的SpecMode为AT_MOST。

## Android Design ##
A dp is equal to one physical pixel on a screen with a density of 160. To calculate dp:

> dp = (width in pixels * 160) / screen density

Navigation Drawer 

Floating Action Button

NavigationBar

sideNav

## RxJava ##
### Subject ###

- AsyncSubject	不论订阅发生在什么时候，只会发射最后一个数据
- BehaviorSubject	发送订阅之前一个数据和订阅之后的全部数据
- ReplaySubject	不论订阅发生在什么时候，都发射全部数据
- PublishSubject	发送订阅之后全部数据

#### lift ####
lift() 过程，有点像一种代理机制，通过事件拦截和处理实现事件序列的变换。

精简掉细节的话，也可以这么说：在 Observable 执行了 lift(Operator) 方法之后，会返回一个新的 Observable，这个新的 Observable 会像一个代理一样，负责接收原始的 Observable 发出的事件，并在处理后发送给 Subscriber。

# OpenGL ES #
逆时针排列顶点 卷曲顺序(winding order)

#### 裁剪空间 ####
对于给定的位置,它的x、y以及z分量都需要在那个位置-w和w之间

当为屏幕创建三维的幻想，OpenGL会把每个gl_Position的x、y和z分量都除以它的w分量
