安卓知识基础
![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gepofa3xuij22911xugpc.jpg)
安卓进阶知识
![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gepohcuf3xj22y721mgpo.jpg)
# UI #

## View ##
View.post()

[https://www.jianshu.com/p/85fc4decc947](https://www.jianshu.com/p/85fc4decc947)

*提问*

Q1: 为什么 View.post() 的操作是可以对 UI 进行操作的呢，即使是在子线程中调用 View.post()？

Q2：网上都说 View.post() 中的操作执行时，View 的宽高已经计算完毕，所以经常看见在 Activity 的 onCreate() 里调用 View.post() 来解决获取 View 宽高为0的问题，为什么可以这样做呢？

Q3：用 View.postDelay() 有可能导致内存泄漏么？


*总结*

1. View.post(Runnable) 内部会自动分两种情况处理，当 View 还没 attachedToWindow 时，会先将这些 Runnable 操作缓存下来；否则就直接通过 mAttachInfo.mHandler 将这些 Runnable 操作 post 到主线程的 MessageQueue 中等待执行。
1. 如果 View.post(Runnable) 的 Runnable 操作被缓存下来了，那么这些操作将会在 dispatchAttachedToWindow() 被回调时，通过 mAttachInfo.mHandler.post() 发送到主线程的 MessageQueue 中等待执行。
1. mAttachInfo 是 ViewRootImpl 的成员变量，在构造函数中初始化，Activity View 树里所有的子 View 中的 mAttachInfo 都是 ViewRootImpl.mAttachInfo 的引用。
1. mAttachInfo.mHandler 也是 ViewRootImpl 中的成员变量，在声明时就初始化了，所以这个 mHandler 绑定的是主线程的 Looper，所以 View.post() 的操作都会发送到主线程中执行，那么也就支持 UI 操作了。
1. dispatchAttachedToWindow() 被调用的时机是在 ViewRootImol 的 performTraversals() 中，该方法会进行 View 树的测量、布局、绘制三大流程的操作。
1. Handler 消息机制通常情况下是一个 Message 执行完后才去取下一个 Message 来执行（异步 Message 还没接触），所以 View.post(Runnable) 中的 Runnable 操作肯定会在 performMeaure() 之后才执行，所以此时可以获取到 View 的宽高。
1. 使用 View.post()，还是有可能会造成内存泄漏的，Handler 会造成内存泄漏的原因是由于内部类持有外部的引用，如果任务是延迟的，就会造成外部类无法被回收。而根据我们的分析，mAttachInfo.mHandler 只是 ViewRootImpl 一个内部类的实例，所以使用不当还是有可能会造成内存泄漏的。

### 动画绘制 ###

在 onCreate 里 setContentView() 的时候，是将我们自己写的布局文件添加到以 DecorView 为根布局的一个 ViewGroup 里，也就是说 DevorView 才是 View 树的根布局

在 onResume() 执行完后，WindowManager 将会执行 addView()，然后在这里面会去创建一个 ViewRootImpl 对象，接着将 DecorView 跟 ViewRootImpl 对象绑定起来，并且将 DecorView 的 mParent 设置成 ViewRootImpl，而 ViewRootImpl 是实现了 ViewParent 接口的，所以虽然 ViewRootImpl 没有继承 View 或 ViewGroup，但它确实是 DecorView 的 parent。

View 树里面不管哪个 View 发起了布局请求、绘制请求，统统最终都会走到 ViewRootImpl 里的 scheduleTraversals()，然后在最近的一个屏幕刷新信号到了的时候再通过 ViewRootImpl 的 performTraversals() 从根布局 DecorView 开始依次遍历 View 树去执行测量、布局、绘制三大操作。

**这也是为什么一直要求页面布局层次不能太深，因为每一次的页面刷新都会先走到 ViewRootImpl 里，然后再层层遍历到具体发生改变的 View 里去执行相应的布局或绘制操作。**


**当调用了 View.startAniamtion() 之后，动画并没有马上就被执行，这个方法只是做了一些变量初始化操作，接着将 View 和 Animation 绑定起来，然后调用重绘请求操作，内部层层寻找 mParent，最终走到 ViewRootImpl 的 scheduleTraversals 里发起一个遍历 View 树的请求，这个请求会在最近的一个屏幕刷新信号到来的时候被执行，调用 performTraversals 从根布局 DecorView 开始遍历 View 树。**

### 动画真正执行的地方 ###
动画其实真正执行的地方应该是在 ViewRootImpl 发起的遍历 View 树的这个过程中。测量、布局、绘制，View 显示到屏幕上的三个基本操作都是由 ViewRootImpl 的 performTraversals() 来控制，而作为 View 树最顶端的 parent，要控制这颗 Veiw 树的三个基本操作，只能通过层层遍历。所以，测量、布局、绘制三个基本操作的执行都会是一次遍历操作。

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gewsn8ssx9j20im0fjjrf.jpg)

如果当前 View 需要绘制，就会去调用自己的 onDraw()，然后如果有子 View，就会调用dispatchDraw() 将绘制事件通知给子 View。ViewGroup 重写了 dispatchDraw()，调用了 drawChild()，而 drawChild() 调用了子 View 的 draw(Canvas, ViewGroup, long)，而这个方法又会去调用到 draw(Canvas) 方法，所以这样就达到了遍历的效果。整个流程就像上上图中画的那样。

*动画真正执行的地方应该也就是在 applyLegacyAnimation()*

	boolen draw(Canvas canvas, View parent, long drawingTime){
		boolean more = false;
		...
		Transform transformToApply = null;
		
		final Animation a = getAnimation();
		if(a != null) {
			more = applyLegacyAnimation(parent, drawingTime, a, ascalingRequired);
			...
		}
		...
		return more
	}

在android-22版本及之前叫drawAnimation

已经能确定 applyTransformation() 是什么时候回调的，动画是什么时候才真正开始执行的。那么 Q1 总算是搞定了，Q2 也基本能理清了。因为我们清楚， applyTransformation() 最终是在绘制流程中的 draw() 过程中执行到的，那么显然在每一帧的屏幕刷新信号来的时候，遍历 View 树是为了重新计算屏幕数据，也就是所谓的 View 的刷新，而动画只是在这个过程中顺便执行的。

当动画如果还没执行完，就会再调用 invalidate() 方法，层层通知到 ViewRootImpl 再次发起一次遍历请求，当下一帧屏幕刷新信号来的时候，再通过 performTraversals() 遍历 View 树绘制时，该 View 的 draw 收到通知被调用时，会再次去调用 applyLegacyAnimation() 方法去执行动画相关操作，包括调用 getTransformation() 计算动画进度，调用 applyTransformation() 应用动画。

#### 动画执行过程 ####
1. 首先，当调用了 View.startAnimation() 时动画并没有马上就执行，而是通过 invalidate() 层层通知到 ViewRootImpl 发起一次遍历 View 树的请求，而这次请求会等到接收到最近一帧到了的信号时才去发起遍历 View 树绘制操作。
1. 从 DecorView 开始遍历，绘制流程在遍历时会调用到 View 的 draw() 方法，当该方法被调用时，如果 View 有绑定动画，那么会去调用applyLegacyAnimation()，这个方法是专门用来处理动画相关逻辑的。
1. 在 applyLegacyAnimation() 这个方法里，如果动画还没有执行过初始化，先调用动画的初始化方法 initialized()，同时调用 onAnimationStart() 通知动画开始了，然后调用 getTransformation() 来根据当前时间计算动画进度，紧接着调用 applyTransformation() 并传入动画进度来应用动画。
1. getTransformation() 这个方法有返回值，如果动画还没结束会返回 true，动画已经结束或者被取消了返回 false。所以 applyLegacyAnimation() 会根据 getTransformation() 的返回值来决定是否通知 ViewRootImpl 再发起一次遍历请求，返回值是 true 表示动画没结束，那么就去通知 ViewRootImpl 再次发起一次遍历请求。然后当下一帧到来时，再从 DecorView 开始遍历 View 树绘制，重复上面的步骤，这样直到动画结束。
1. 有一点需要注意，动画是在每一帧的绘制流程里被执行，所以动画并不是单独执行的，也就是说，如果这一帧里有一些 View 需要重绘，那么这些工作同样是在这一帧里的这次遍历 View 树的过程中完成的。每一帧只会发起一次 perfromTraversals() 操作。

**View 动画区别于属性动画的就是 View 动画并不会对这个 View 的属性值做修改**

### ValueAnimator ###

[属性动画 ValueAnimator 运行原理全解析 待续](https://www.jianshu.com/p/46f48f1b98a9)


> Choreographer 内部有几个队列，上面方法的第一个参数 CALLBACK_ANIMATION 就是用于区分这些队列的，而每个队列里可以存放 FrameCallback 对象，也可以存放 Runnable 对象。Animation 动画原理上就是通过 ViewRootImpl 生成一个 doTraversal() 的 Runnable 对象（其实也就是遍历 View 树的工作）存放到 Choreographer 的队列里的。而这些队列里的工作，都是用于在接收到屏幕刷新信号时取出来执行的。但有一个关键点，Choreographer 要能够接收到屏幕刷新信号的事件，是需要先调用 Choreographer 的 scheduleVsyncLocked() 方法来向底层注册监听下一个屏幕刷新信号事件的。

动画是一个持续的过程，也就是说，每一帧都应该处理一个动画进度，直到动画结束。既然这样，我们就需要在动画结束之前的每一个屏幕刷新信号都能够接收到，所以在每一帧里都需要再去向底层注册监听下一个屏幕刷新信号事件。

当接收到屏幕刷新信号时，mFrameCallback 的 doFrame() 会被回调，该方法内部做了两件事，一是去处理当前帧的动画，二则是根据列表的大小是否不为 0 来决定继续向底层注册监听下一个屏幕刷新信号事件，如此反复，直至列表大小为 0。

*处理动画第一帧的工作问题：*

参考 Animation 动画的原理，第一帧的工作通常都是为了记录动画第一帧的时间戳，因为后续的每一帧里都需要根据当前时间以及动画第一帧的时间还有一个动画持续时长来计算当前帧动画所处的进度

#### 补间动画和属性动画 ####
补间动画仅仅是对 View 在视觉效果上做了移动、缩放、旋转和淡入淡出的效果，其实并没有真正改变 View 的属性

属性动画有两个非常重要的类：ValueAnimator 类 & ObjectAnimator 类，二者的区别在于：
ValueAnimator 类是先改变值，然后 手动赋值 给对象的属性从而实现动画；是 间接 对对象属性进行操作；而 ValueAnimator 类本质上是一种 改变值 的操作机制。

ObjectAnimator 类是先改变值，然后 自动赋值 给对象的属性从而实现动画；是 直接 对对象属性进行操作；可以理解为：ObjectAnimator 更加智能、自动化程度更高。

#### Path的FillType ####
	public enum FillType {
	
	    WINDING         (0),
	
	    EVEN_ODD        (1),
	
	    INVERSE_WINDING (2),
	
	    INVERSE_EVEN_ODD(3);
	
	}

*WINDING*


> 非零环绕原则，从任意一点发射一条线，默认值是0，遇到顺时针交点则+1，遇到逆时针交点则-1，最终如果不等于0，则认为这个点是图形内部的点，则需要绘制颜色；反之，如果这个值是0，则认为这个点不在图形内部，则不需要绘制颜色。

*EVEN_ODD*

> 奇偶原则。从任意一点射出一条线，与图形的交线是奇数，则认为这个点在图形内部，需要绘制颜色；反之如果是偶数，则认为这个点在图形外部，不需要绘制颜色。

#### 属性动画 ####
[https://www.jianshu.com/p/46f48f1b98a9](https://www.jianshu.com/p/46f48f1b98a9)

	//1.ValueAnimator用法  
	ValueAnimator animator = ValueAnimator.ofInt(500);
	animator.setDuration(1000);
	animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	        @Override
	        public void onAnimationUpdate(ValueAnimator animation) {
	               int value = (int) animation.getAnimatedValue();
	               mView.setX(value);  
	         }
	 });
	animator.start();
	
	//2.ObjectAnimator用法
	ObjectAnimator animator = ObjectAnimator.ofInt(mView, "X", 500).setDuration(1000).start();

> Android 每隔 16.6ms 会刷新一次屏幕，也就是每过 16.6ms 底层会发出一个屏幕刷新信号，当我们的 app 接收到这个屏幕刷新信号时，就会去计算屏幕数据，也就是我们常说的测量、布局、绘制三大流程。这整个过程关键的一点是，app 需要先向底层注册监听下一个屏幕刷新信号事件，这样当底层发出刷新信号时，才可以找到上层 app 并回调它的方法来通知事件到达了，app 才可以接着去做计算屏幕数据之类的工作。
> 
> 而注册监听以及提供回调接口供底层调用的这些工作就都是由 Choreographer 来负责，Animation 动画的原理是通过当前 View 树的 ViewRootImpl 的 scheduleTraversals() 方法来实现，这个方法的内部逻辑会走到 Choreographer 里去完成注册监听下一个屏幕刷新信号以及接收到事件之后的工作。
> 
> 需要跟屏幕刷新信号打交道的话，归根结底最后都是通过 Choreographer 这个类。

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gexsb45o3sj20oc086mxe.jpg)

Choreographer 内部有几个队列，上面方法的第一个参数 CALLBACK_ANIMATION 就是用于区分这些队列的，而每个队列里可以存放 FrameCallback 对象，也可以存放 Runnable 对象。Animation 动画原理上就是通过 ViewRootImpl 生成一个 doTraversal() 的 Runnable 对象（其实也就是遍历 View 树的工作）存放到 Choreographer 的队列里的。而这些队列里的工作，都是用于在接收到屏幕刷新信号时取出来执行的。但有一个关键点，Choreographer 要能够接收到屏幕刷新信号的事件，是需要先调用 Choreographer 的 scheduleVsyncLocked() 方法来向底层注册监听下一个屏幕刷新信号事件的。

当 ValueAnimator 调用了 start() 方法之后，首先会对一些变量进行初始化工作并通知动画开始了，然后 ValueAnimator 实现了 AnimationFrameCallback 接口，并通过 AnimationHander 将自身 this 作为参数传到 mAnimationCallbacks 列表里缓存起来。而 AnimationHandler 在 mAnimationCallbacks 列表大小为 0 时会通过内部类 MyFrameCallbackProvider 将一个 mFrameCallback 工作缓存到 Choreographer 的待执行队列里，并向底层注册监听下一个屏幕刷新信号事件。

#### 这里为什么要对第一帧时间 mStartTime 进行修正呢 ####
这跟属性动画通过 Choreographer 的实现原理有关。我们知道，屏幕的刷新信号事件都是由 Choreographer 负责，它内部有多个队列，这些队列里存放的工作都是用于在接收到信号时取出来处理。那么，这些队列有什么区别呢？

其实也就是执行的先后顺序的区别，按照执行的先后顺序，我们假设这些队列的命名为：1队列 > 2队列 > 3队列。我们本篇分析的属性动画，AnimationHandler 封装的 mFrameCallback 工作就是放到 1队列里的；而之前分析的 Animation 动画，它通过 ViewRootImpl 封装的 doTraversal() 工作是放到 2队列里的；而上面刚过完的修正动画第一帧时间的 Runnable 工作则是放到 3队列里的。

也就是说，当接收到屏幕刷新信号后，属性动画会最先被处理。然后是去计算当前屏幕数据，也就是测量、布局、绘制三大流程。但是这样会有一个问题，如果页面太过复杂，绘制当前界面时花费了太多的时间，那么等到下一个屏幕刷新信号时，属性动画根据之前记录的第一帧时间戳计算动画进度时，会发现丢了开头的好几帧，明明动画没还执行过。所以，这就是为什么需要对动画第一帧时间进行修正。

#### ValueAnimator start()之后的流程 ####

1. ValueAnimator 属性动画调用了 start() 之后，会先去进行一些初始化工作，包括变量的初始化、通知动画开始事件；
1. 然后通过 AnimationHandler 将其自身 this 添加到 mAnimationCallbacks 队列里，AnimationHandller 是一个单例类，为所有的属性动画服务，列表里存放着所有正在进行或准备开始的属性动画；
1. 如果当前存在要运行的动画，那么 AnimationHandler 会去通过 Choreographer 向底层注册监听下一个屏幕刷新信号，当接收到信号时，它的 mFrameCallback 会开始进行工作，工作的内容包括遍历列表来分别处理每个属性动画在当前帧的行为，处理完列表中的所有动画后，如果列表还不为 0，那么它又会通过 Choreographer 再去向底层注册监听下一个屏幕刷新信号事件，如此反复，直至所有的动画都结束。
1. AnimationHandler 遍历列表处理动画是在 doAnimationFrame() 中进行，而具体每个动画的处理逻辑则是在各自，也就是 ValueAnimator 的 doAnimationFrame() 中进行，各个动画如果处理完自身的工作后发现动画已经结束了，那么会将其在列表中的引用赋值为空，AnimationHandler 最后会去将列表中所有为 null 的都移除掉，来清理资源。
1. 每个动画 ValueAnimator 在处理自身的动画行为时，首先，如果当前是动画的第一帧，那么会根据是否有"跳过片头"（setCurrentPlayTime()）来记录当前动画第一帧的时间 mStartTime 应该是什么。
1. 第一帧的动画其实也就是记录 mStartTime 的时间以及一些变量的初始化而已，动画进度仍然是 0，所以下一帧才是动画开始的关键，但由于属性动画的处理工作是在绘制界面之前的，那么有可能因为绘制耗时，而导致 mStartTime 记录的第一帧时间与第二帧之间隔得太久，造成丢了开头的多帧，所以如果是这种情况下，会进行 mStartTime 的修正。
1. 修正的具体做法则是当绘制工作完成后，此时，再根据当前时间与 mStartTime 记录的时间做比较，然后进行修正。
1. 如果是在动画过程中的某一帧才出现绘制耗时现象，那么，只能表示无能为力了，丢帧是避免不了的了，想要解决就得自己去分析下为什么绘制会耗时；而如果是在第一帧是出现绘制耗时，那么，系统还是可以帮忙补救一下，修正下 mStartTime 来达到避免丢帧。

#### 根据当前时间计算并实现当前帧的动画动作 animateBasedOntime() ####
当前帧的动画进度计算完毕之后，就是需要应用到动画效果上面了，所以 animateValue() 方法的意义就是类似于 Animation 动画中的 applyTransformation()

*内部是如何根据拿到的 0-1 区间的进度值转换成我们指定区间的数值*

	void calculateValue(float fraction){
		Object value = mKeyframes.getvalue(fraction);
		mAnimatedValue = mConvert == null ? value: mConverter.convert(value);
	}

我们在使用 ValueAnimator 时，注册了动画进度回调，然后在回调里取当前的值时其实也就是取到上面那个 mAnimatedValue 变量的值，而这个变量的值是通过 mKeyframes.getValue() 计算出来的

mKeyframes 实例化的这些地方，ofInt()，onFloat() 就是我们创建属性动画时相似的方法名， 其实 ValueAnimator.ofInt() 内部会根据相应的方法来创建 mKeyframes 对象，也就是说，在实例化属性动画时，这些 mKeyframes 也顺便被实例化了。

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geyvtg25loj20sq0yf75t.jpg)

1. 接收到屏幕刷新信号后，AnimationHandler 会去遍历列表，将所有待执行的属性动画都取出来去计算当前帧的动画行为。
1. 每个动画在处理当前帧的动画逻辑时，首先会先根据当前时间和动画第一帧时间以及动画的持续时长来初步计算出当前帧时动画所处的进度，然后会将这个进度值等价转换到 0-1 区间之内。
1. 接着，插值器会将这个经过初步计算之后的进度值根据设定的规则计算出实际的动画进度值，取值也是在 0-1 区间内。
1. 计算出当前帧动画的实际进度之后，会将这个进度值交给关键帧机制，来换算出我们需要的值，比如 ValueAnimator.ofInt(0, 100) 表示我们需要的值变化范围是从 0-100，那么插值器计算出的进度值是 0-1 之间的，接下去就需要借助关键帧机制来映射到 0-100 之间。
1. 关键帧的数量是由 ValueAnimator.ofInt(0, 1, 2, 3) 参数的数量来决定的，比如这个就有四个关键帧，第一帧和最后一帧是必须的，所以最少会有两个关键帧，如果参数只有一个，那么第一帧默认为 0，最后一帧就是参数的值。当调用了这个 ofInt() 方法时，关键帧组也就被创建了。
1. 当只有两个关键帧时，映射的规则是，如果没有设置估值器，那么就等比例映射，比如动画进度为 0.5，需要的值变化区间是 0-100，那么等比例映射后的值就是 50，那么我们在 onAnimationUpdate 的回调中通过 animation.getAnimatedValue() 获取到的值 50 就是这么来的。
1. 如果有设置估值器，那么就按估值器的规则来进行映射。
1. 当关键帧超过两个时，需要先找到当前动画进度是落于哪两个关键帧之间，然后将这个进度值先映射到这两个关键帧之间的取值，接着就可以将这两个关键帧看成是第一帧和最后一帧，那么就可以按照只有两个关键帧的情况下的映射规则来进行计算了。
1. 而进度值映射到两个关键帧之间的取值，这就需要知道每个关键帧在整个关键帧组中的位置信息，或者说权重。而这个位置信息是在创建每个关键帧时就传进来的。onInt() 的规则是所有关键帧按等比例来分配权重，比如有三个关键帧，第一帧是 0，那么第二帧就是 0.5， 最后一帧 1。
#### View.animator() ####

	animatePropertyBy(int constantName, float startValue, float byValue){
	    ...
	    mView.removeCallbacks(mAnimationStarter);
	    mView.postOnAnimation(mAnimationStarter);
	}

postOnAnimation()  传进去的 Runnable 并不会被马上执行，而是要等到下一个屏幕刷新信号来的时候才会被取出来执行。

view.animate().scaleX()这样使用时，就算不主动调用start(),其实内部自动会安排一个Runnable，最迟在下一个屏幕刷新信号来的时候，就会自动去调用startAnimation()来启动动画

但如果主动调用了start() ，内部就需要先将安排好的 Runnable 操作取消掉，然后直接调用 startAnimation()来启动动画。
 
	private void startAnimation(){
	if(mRTBackend != null && mRTBackend.startAnimation(this)){
		return;
	}
	...
	
	//创建一个0.0-1.0变化的ValueAnimator
	ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
	
	//将当前 mPendingAnimations 里保存的一系列动画全部取出来，作为同一组一起执行一起结束的动画
	ArrayList<NameValuesHolder> nameValueList = (ArrayList<NameValuesHolder>) mPendingAnimations.clone();
	
	//创建一个新的PropertyBundle 来保存这一组动画，以ValueAnimator作为key来区分
	mAnimationMap.put(animator, new PropertyBundle(propetyMask, nameValueList));
	
	//提供动画开始前,结束后的操作回调
	if(mPendingSetupAction != null){
		mAnimatorSetupMap.put(animator, mPendingSetupAction);
		mPendingSetupAction = null;
	}
	...
	
	//对ValueAnimator进行Listener、StartDelay、Duration、Interpolator的设置
	animator.addUpdateListener(mAnimatorEventListener);
	animator.addListener(mAnimatorEventListener);
	...
	
	//启用ValueAnimator.start();
	animator.start();
	}

> ValueAnimator 内部其实并没有进行任何 ui 操作，它只是提供了一种机制，可以根据设定的几个数值，如 0-100，内部自己在每一帧内，根据当前时间，第一帧的时间，持续时长，以及插值器规则，估值器规则来计算出在当前帧内动画的进度并映射到设定的数值区间，如 0-100 区间内映射之后的数值应该是多少。
> 
> 既然 ValueAnimator 并没有进行任何 ui 操作，那么要用它来实现动画效果，只能自己在 ValueAnimator 提供的每一帧的回调里（AnimatorUpdateListener），自己取得 ValueAnimator 计算出的数值，来自行应用到需要进行动画效果的那个 View 上。

整理信息

### 自定义View ###
1. 自定义View：继承View
2. 基于现有组件：继承View的派生类
3. 组合的方式：自定义控件中包含了其他的组件

#### 自定义样式属性 ####
	<resources>
	    <declare-styleable name="MenuItemLayout">
	        <attr name="title_text" format="string" />
	        <attr name="hint_text" format="string" />
	        <attr name="icon_reference" format="reference" />
	        <attr name="icon_uri" format="string" />
	        <attr name="jump_url" format="string" />
	        <attr name="divide_line_style" format="integer" />
	    </declare-styleable>
	</resources>

*读取自定义属性*

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItemLayout);
		setTitleText(a.getString(R.styleable.MenuItemLayout_title_text));
		setHintText(a.getString(R.styleable.MenuItemLayout_hint_text));
		setIconImgId(a.getResourceId(R.styleable.MenuItemLayout_icon_reference, 10000));
		setJumpUrl(a.getString(R.styleable.MenuItemLayout_jump_url));

#### 绘制流程 ####
![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gfdrn8jemvj20fc0heaay.jpg)

*测量View大小(onMeasure)*

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int widthsize  MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
	    int widthmode  MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式
	    
	    int heightsize  MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
	    int heightmode  MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
	}

widthMeasureSpec 和 heightMeasureSpec 这两个 int 类型的参数， 毫无疑问他们是和宽高相关的， 但它们其实不是宽和高， 而是由宽、高和各自方向上对应的测量模式来合成的一个值

	模式	        二进制数值	描述
	UNSPECIFIED	 00	        默认值，父控件没有给子view任何限制，子View可以设置为任意大小。
	EXACTLY	     01	        表示父控件已经确切的指定了子View的大小。
	AT_MOST	     10	        表示子View具体大小没有尺寸限制，但是存在上限，上限一般为父View大小。

如果对View的宽高进行修改了，不要调用 super.onMeasure( widthMeasureSpec, heightMeasureSpec); 要调用 setMeasuredDimension( widthsize, heightsize); 这个函数。

### Android硬件加速 ###
[原Android文档翻译 https://www.jianshu.com/p/601a21b00475](https://www.jianshu.com/p/601a21b00475)

[https://developer.android.com/guide/topics/graphics/hardware-accel](https://developer.android.com/guide/topics/graphics/hardware-accel)

### Android drawing models ###
[https://developer.android.google.cn/guide/topics/graphics/hardware-accel](https://developer.android.google.cn/guide/topics/graphics/hardware-accel)
#### Software-based drawing model ####
views are drawn with the following two steps:

1. Invalidate the hierarchy
1. Draw the hierarchy

> The invalidation messages are propagated all the way up the view hierarchy to compute the regions of the screen that need to be redrawn (the dirty region). The Android system then draws any view in the hierarchy that intersects with the dirty region

#### Hardware accelerated drawing model ####
Instead of executing the drawing commands immediately, the Android system records them inside display lists, which contain the output of the view hierarchy’s drawing code. 

Another optimization is that the Android system only needs to record and update display lists for views marked dirty by an invalidate() call. Views that have not been invalidated can be redrawn simply by re-issuing the previously recorded display list.

1. Invalidate the hierarchy
1. Record and update display lists
1. Draw the display lists 

To ensure that the Android system records a view’s display list, you must call invalidate() 和脏区重叠的区域不保证执行绘制，绘制必须执行明确执行invalidate

Using display lists also benefits animation performance because setting specific properties, such as alpha or rotation, does not require invalidating the targeted view (it is done automatically) 透明通道，旋转角度不用需要invalidate制动执行

If your application is affected by any of these missing features or limitations, you can turn off hardware acceleration for just the affected portion of your application by calling **setLayerType(View.LAYER_TYPE_SOFTWARE, null)**

 Calling the setter for any of these properties results in optimal invalidation and no redrawing of the targeted view:某些属性的修改不需要整个View重绘

- alpha: Changes the layer's opacity
- x, y, translationX, translationY: Changes the layer's position
- scaleX, scaleY: Changes the layer's size
- rotation, rotationX, rotationY: Changes the layer's orientation in 3D space
- pivotX, pivotY: Changes the layer's transformations origin

	view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
	ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", 180);
	animator.addListener(new AnimatorListenerAdapter() {
	    @Override
	    public void onAnimationEnd(Animator animation) {
	        view.setLayerType(View.LAYER_TYPE_NONE, null);
	    }
	});
	animator.start();

动画结束之后关闭硬件layer加速，硬件加速消耗内存

*建议*

Reduce the number of views in your application
Avoid overdraw 
Don't create render objects in draw methods
Don't modify shapes too often
Don't modify bitmaps too often
Use alpha with care  alpha值设置尽量使用硬件Layer


### Camera ###
#### CameraX ####
这个 CameraView 类是一个 ViewGroup，本质上包含了一个 TextureView 来显示 camera 流，以及配置这个组件的一些属性

- scaleType—给捕获的流设置缩放类型。可以使 CENTER_CROP 或者 CENTER_INSIDE
- quality—设置捕获的媒体的质量。可以是 MAX，HIGH，MEDIUM 或者 LOW
- pinchToZoomEnabled—一个布尔值，控制用户是否能够在 CameraView 内使用手指缩放视图
- captureMode—设置捕获模式。可以是 IMAGE，VIDEO 或者 FIXED
- lensFacing—设置镜头。可以是 FRONT，BACK 或者 NONE
- flashMode—设置闪光灯模式。可以是 FRONT，BACK 或者 NONE

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gezuccvvifj20hs07mgll.jpg)

	TextureView viewFinder = findViewById(R.id.view_finder);
        viewFinder.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                updateTransform();
            }
        });

        viewFinder.post(new Runnable() {
            @Override
            public void run() {
                startCamera();
            }
        });

更新相机预览：主要是给TextureView设置一个旋转的矩阵变化，防止预览方向不对

	private void updateTransform() {
        Matrix matrix = new Matrix();
        // Compute the center of the view finder
        float centerX = viewFinder.getWidth() / 2f;
        float centerY = viewFinder.getHeight() / 2f;

        float[] rotations = {0,90,180,270};
        // Correct preview output to account for display rotation
        float rotationDegrees = rotations[viewFinder.getDisplay().getRotation()];

        matrix.postRotate(-rotationDegrees, centerX, centerY);

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix);
    }

启动相机：创建PreviewConfig和Preview这两个对象，可以设置预览图像的尺寸和比例，在OnPreviewOutputUpdateListener回调中用setSurfaceTexture方法，将相机图像输出到TextureView。最后用CameraX.bindToLifecycle方法将相机与当前页面的生命周期绑定。

	private void startCamera() {
        // 1. preview
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(new Rational(1, 1))
                .setTargetResolution(new Size(640,640))
                .build();

        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) viewFinder.getParent();
                parent.removeView(viewFinder);
                parent.addView(viewFinder, 0);

                viewFinder.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });

        CameraX.bindToLifecycle(this, preview);

CameraX 可以绑定对Camera的各种使用

	bindToLifecycle(LifecycleOwner lifecycleOwner, UseCase... useCases)
#### Camera2 ####
[https://www.jianshu.com/p/b9d994f2b381](https://www.jianshu.com/p/b9d994f2b381)
通过CameraManager能查询到本设备上有多少available的Camera设备

每个CameraDevice设备提供一系列参数来描述当前Camera设备，通过getCameraCharacteristics获取

从相机设备上获取一个或多个image，首先必须创建一个CameraCaptureSession并输出到一个或多个目标Surface上。每个Surface必须预先设置合适的尺寸。目标Surface可以背一系列类(SurfaceView,SurfaceTexture,ImageReader..)持有。

相机设备要获取Image，需要创建一个定义了相机参数的CaptureRequest,CameraDevice有一个工厂方法区创建一个request builder。

一旦request被创建出来，它可以被一个active状态的session拿去得到一个Image或多个Image，也就是说session通过request去得到一张或者多张图

API使用大体如下:

1. 通过context.getSystemService(Contxt.CAMERA_SERVICE)获取CameraManager.
1. 通过CameraManager.open()方法在回调中得到CameraDevice.
1. 通过CameraDevice.createCaptureSession()在回调中获取CameraCaptureSession.
1. 构建CaptureRequest,有三种模式可选 预览/拍照/录像.
1. 通过CameraCaptureSession发送CaptureRequest, capture表示只发一次请求，setRepeatingRequest表示不断发送请求
1. 拍照数据可以在ImageReader.OnImageAvailableListener回调中获取，CaptureCallback中则可获取拍照实际获取的参数和Camera当前状态

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gezzldhmzkj20p00cxdfw.jpg)


*ImageReader获得预览数据*

Image类允许应用通过一个或多个ByteBuffers直接访问Image的像素数据， ByteBuffer包含在Image.Plane类中，同时包含了这些像素数据的配置信息。因为是作为提供raw数据使用的，Image不像Bitmap类可以直接填充到UI上使用。

因为Image的生产消费是跟硬件直接挂钩的，所以为了效率起见，Image如果不被使用了应该尽快的被销毁掉。比如说，当我们使用ImageReader从不用的媒体来源获取到Image的时候，如果Image的数量到达了maxImages，不关闭之前老的Image，新的Image就不会继续生产。

	...
	          //构造一个ImageReader的实例，设置宽高，输出格式，缓存max数量
	           mImageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(),
	                            ImageFormat.JPEG, 2);
	           mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mCameraHandler);
	...

	private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
	        @Override
	        public void onOpened(CameraDevice camera) {
	            ...
	                mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
	                mPreviewBuilder.addTarget(previewSurface);
					//把ImageReader的surface添加给CaptureRequest.Builder，使预览surface和ImageReader同时收到数据回调。
	                mPreviewBuilder.addTarget(mImageReader.getSurface());
	                mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()), mStateCallBack, mCameraHandler);
	            ...
	        }
	}

	private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
	        @Override
	        public void onImageAvailable(ImageReader reader) {
	            Image image = reader.acquireNextImage();
	           //因为是ImageFormat.JPEG格式，所以 image.getPlanes()返回的数组只有一个，也就是第0个。
	            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
	            byte[] bytes = new byte[byteBuffer.remaining()];
	            byteBuffer.get(bytes);
				//ImageFormat.JPEG格式直接转化为Bitmap格式。
	            Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				//因为摄像机数据默认是横的，所以需要旋转90度。
	            Bitmap newBitmap = BitmapUtil.rotateBitmap(temp, 90);
				//抛出去展示或存储。
	            mOnGetBitmapInterface.getABitmap(newBitmap);
				//一定需要close，否则不会收到新的Image回调。
		            image.close();
	        }
	    };

camera2格式设置为YUV_420_888时ImageReader会得到三个Plane，分别对应y,u,v，每个Plane都有自己的规格，两个Plane重要的参数

*getRowStride*

getRowStride是每一行数据相隔的间隔。getRowStride并不一定等于camera预览的宽度

*getPixelStride*

表示相邻的相同YUV数据间隔的距离。
Y分量应该都是1，表示Y都是紧密挨着的
UV分量可能是1，也可能是2
1、 UV分量是1：表示UV跟Y一样，两个U之间没有间隔，也就是YU12(也叫I420：YYYYYYYYUUVV)或者YV12(YYYYYYYYVVUU)
2、UV分量是2：表示每两个UV之间间隔一个，也就是NV12(YYYYYYYYUVUV)或者NV21(YYYYYYYYVUVU)

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gf12h6vzmij20i209rq2v.jpg)


	private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
	        @Override
	        public void onImageAvailable(ImageReader reader) {
	            Image image = reader.acquireLatestImage();
	            if (image == null) {
	                return;
	            }
	                try {
	                    int w = image.getWidth(), h = image.getHeight();
	                    // size是宽乘高的1.5倍 可以通过ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888)得到
	                    int i420Size = w * h * 3 / 2;
	
	                    Image.Plane[] planes = image.getPlanes();
	                    //remaining0 = rowStride*(h-1)+w => 27632= 192*143+176 Y分量byte数组的size
	                    int remaining0 = planes[0].getBuffer().remaining();
	                    int remaining1 = planes[1].getBuffer().remaining();
	                    //remaining2 = rowStride*(h/2-1)+w-1 =>  13807=  192*71+176-1 V分量byte数组的size
	                    int remaining2 = planes[2].getBuffer().remaining();
	                    //获取pixelStride，可能跟width相等，可能不相等
	                    int pixelStride = planes[2].getPixelStride();
	                    int rowOffest = planes[2].getRowStride();
	                    byte[] nv21 = new byte[i420Size];
	                    //分别准备三个数组接收YUV分量。
	                    byte[] yRawSrcBytes = new byte[remaining0];
	                    byte[] uRawSrcBytes = new byte[remaining1];
	                    byte[] vRawSrcBytes = new byte[remaining2];
	                    planes[0].getBuffer().get(yRawSrcBytes);
	                    planes[1].getBuffer().get(uRawSrcBytes);
	                    planes[2].getBuffer().get(vRawSrcBytes);
	                    if (pixelStride == width) {
	                        //两者相等，说明每个YUV块紧密相连，可以直接拷贝
	                        System.arraycopy(yRawSrcBytes, 0, nv21, 0, rowOffest * h);
	                        System.arraycopy(vRawSrcBytes, 0, nv21, rowOffest * h, rowOffest * h / 2 - 1);
	                    } else {
	                        //根据每个分量的size先生成byte数组
	                        byte[] ySrcBytes = new byte[w * h];
	                        byte[] uSrcBytes = new byte[w * h / 2 - 1];
	                        byte[] vSrcBytes = new byte[w * h / 2 - 1];
	                        for (int row = 0; row < h; row++) {
	                            //源数组每隔 rowOffest 个bytes 拷贝 w 个bytes到目标数组
	                            System.arraycopy(yRawSrcBytes, rowOffest * row, ySrcBytes, w * row, w);
	                            //y执行两次，uv执行一次
	                            if (row % 2 == 0) {
	                                //最后一行需要减一
	                                if (row == h - 2) {
	                                    System.arraycopy(vRawSrcBytes, rowOffest * row / 2, vSrcBytes, w * row / 2, w - 1);
	                                } else {
	                                    System.arraycopy(vRawSrcBytes, rowOffest * row / 2, vSrcBytes, w * row / 2, w);
	                                }
	                            }
	                        }
	                        //yuv拷贝到一个数组里面
	                        System.arraycopy(ySrcBytes, 0, nv21, 0, w * h);
	                        System.arraycopy(vSrcBytes, 0, nv21, w * h, w * h / 2 - 1);
	                    }
	                    //这里使用了YuvImage，接收NV21的数据，得到一个Bitmap
	                    Bitmap bitmap = BitmapUtil.getBitmapImageFromYUV(nv21, width, height);
	
	                    if (mOnGetBitmapInterface != null) {
	                        mOnGetBitmapInterface.getABitmap(bitmap);
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    LogUtil.d(e.toString());
	                }
	            image.close();
	        }
	    };
	
	//BitmapUtil.java
	    public static Bitmap getBitmapImageFromYUV(byte[] data, int width, int height) {
	        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        yuvimage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
	        byte[] jdata = baos.toByteArray();
	        BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
	        bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
	        Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length, bitmapFatoryOptions);
	        return bmp;
	    }

> 数据字节少1是因为我们数据从下标0开始取，直到最后取到的有效自然数序列为奇数（即下标0、2、4.....2n对应于自然数1、3、5.....2n+1）,同时这个值每行中有效范围一定小于width，所以取到的有效个数一定是width/pixelStride*height/pixelStride = 一个偶数，此时有效序列在奇数位上，所以最后一位一定是无效的。系统取值时，拿到UV数据前移一字节得到U数据，此时数据有效范围为width*height/pixelStride-1；拿到UV数据直接取V数据，最后取到的有效范围也是width/pixelStride*height/pixelStride-1，所以以此推测这就是为什么UV数据总是比Y少1字节的原因，虽然少了但U或V的数据实则都是正常的偶数位。在YUV420这个格式下Y = 4U = 4V 是一定成立的。
### TextView ###
#### Compound Drawable ####
我们可以用 LinearLayout 里面嵌套 ImageView 和 TextView 实现，也可以只用一个带 Drawable 的 TextView 做到。
#### 阴影效果 ####
shadowDx，shadowDy，shadowRadius 的值的单位是 px，而非 dp。为了让阴影完全显示，记得设置合适的 padding
#### Span 中各种标志的含义 ####
- SPAN_INCLUSIVE_INCLUSIVE
- SPAN_INCLUSIVE_EXCLUSIVE
- SPAN_EXCLUSIVE_INCLUSIVE
- SPAN_EXCLUSIVE_EXCLUSIVE

这里的 inclusive 和 exclusive 并非指的是 start 和 end 对应的字符，而是指，在 start 之前或 end 之后字符增加时，新增的字符是否应用 span 样式

**不要大量使用这种自定义字体，因为自定义字体会消耗更多的性能**

### Drawable ###
*BitmapDrawable*

*ShapeDrawable*

*LayerDrawable*

LayerDrawable可以说是android开发中用得比较多的，将BitmapDrawable, ShapeDrawable叠加生成layerDrawable其效果图就是上面这样的。
LayerDrawable对应的XML标签是<layer-list>，它表示一种层次化的Drawable集合，通过将不同的Drawable放置在不同的层上面从而达到一种叠加后的效果
LayerList里面可以包含多个item

*StateListDrawable*

常见状态

- android:state_selected
- android:state_pressed
- android:state_focused
- android:state_checkable
- android:state_checked
- android:state_enable

*LevelListDrawable*

手动设置类型来来显示不同drawable

*TransitionDrawable*

*InsetDrawable*
### 画图 ###
裁剪圆

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        mPath = new Path();

        mPath.addCircle(mBitmap.getWidth() / 2, mBitmap.getHeight() / 2, mBitmap.getWidth() / 2, Path.Direction.CCW);
        canvas.clipPath(mPath);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
    }
### XML BitMap ###
XML Bitmap 是一个用XML定义的文件放在资源目录，定义的对象是图片，为bitmap定义别名，这个文件可以给bitmap定义一些额外的属性。例如：抖动

	<?xml version="1.0" encoding="utf-8"?>
	<bitmap
	  xmlns:android="http://schemas.android.com/apk/res/android"
	  android:src="@[package:]drawable/drawable_resource"
	  android:antialias=["true" | "false"]
	  android:dither=["true" | "false"]
	  android:filter=["true" | "false"]
	  android:gravity=["top" | "bottom" | "left" | "right" | "center_vertical" |
	           "fill_vertical" | "center_horizontal" | "fill_horizontal" |
	           "center" | "fill" | "clip_vertical" | "clip_horizontal"]
	  android:tileMode=["disabled" | "clamp" | "repeat" | "mirror"] />


### ViewStub ###
### LayoutManager ###
## Handler ##
#### Handler Class Should be Static or Leaks Occur ####
I wrote that debugging code because of a couple of memory leaks I 
found in the Android codebase. Like you said, a Message has a 
reference to the Handler which, when it's inner and non-static, has a 
reference to the outer this (an Activity for instance.) If the Message 
lives in the queue for a long time, which happens fairly easily when 
posting a delayed message for instance, you keep a reference to the 
Activity and "leak" all the views and resources. It gets even worse 
when you obtain a Message and don't post it right away but keep it 
somewhere (for instance in a static structure) for later use.


1. Android App启动的时候，Android Framework 为主线程创建一个Looper对象，这个Looper对象将贯穿这个App的整个生命周期，它实现了一个消息队列（Message  Queue），并且开启一个循环来处理Message对象。而Framework的主要事件都包含着内部Message对象，当这些事件被触发的时候，Message对象会被加到消息队列中执行。
1. 当一个Handler被实例化时（如上面那样），它将和主线程Looper对象的消息队列相关联，被推到消息队列中的Message对象将持有一个Handler的引用以便于当Looper处理到这个Message的时候，Framework执行Handler的handleMessage(Message)方法。
1. 在 Java 语言中，非静态匿名内部类将持有一个对外部类的隐式引用，而静态内部类则不会

合适的处理方式

	public class SampleActivity extends Activity {
	         
	  /**
	   * Instances of static inner classes do not hold an implicit
	   * reference to their outer class.
	   */
	  private static class MyHandler extends Handler {
	    private final WeakReference<SampleActivity> mActivity;
	         
	    public MyHandler(SampleActivity activity) {
	      mActivity = new WeakReference<SampleActivity>(activity);
	    }
	         
	    @Override
	    public void handleMessage(Message msg) {
	      SampleActivity activity = mActivity.get();
	      if (activity != null) {
	        // ...
	      }
	    }
	  }
	         
	  private final MyHandler mHandler = new MyHandler(this);
	         
	  /**
	   * Instances of anonymous classes do not hold an implicit
	   * reference to their outer class when they are "static".
	   */
	  private static final Runnable sRunnable = new Runnable() {
	      @Override
	      public void run() { }
	  };
	         
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	         
	    // Post a message and delay its execution for 10 minutes.
	    mHandler.postDelayed(sRunnable, 60 * 10 * 1000);
	             
	    // Go back to the previous Activity.
	    finish();
	  }
	}

> 如果内部类的生命周期和Activity的生命周期不一致（比如上面那种，Activity finish()之后要等10分钟，内部类的实例才会执行），则在Activity中要避免使用非静态的内部类，这种情况，就使用一个静态内部类，同时持有一个对Activity的WeakReference

*在主线程向子线程发消息*

		Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                //初始化Looper
                Looper.prepare();
                //在子线程内部初始化handler即可，发送消息的代码可在主线程任意地方发送
                handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                      //所有的事情处理完成后要退出looper，即终止Looper循环
                        //这两个方法都可以，有关这两个方法的区别自行寻找答案
                        handler.getLooper().quit();
                        handler.getLooper().quitSafely();
                    }
                };
              
                //启动Looper循环
                Looper.loop();
            }
        };
        thread.start();

Can't create handler inside thread " + Thread.currentThread()+ " that has not called Looper.prepare()

**handleMessage（）的执行线程就是handler初始化时所在的线程，那么答案真的是这样的吗？
答案是否定的！！！**

#### Handler机制中最重要的四个对象 ####

- Handler：负责发送消息及处理消息
- Looper：复制不断的从消息队列中取出消息，并且给发送本条消息的Handler
- MessageQueue：负责存储消息
- Message:消息本身，负责携带数据


#### Handler构造方法 ####
	public Handler() {
	        this(null, false);
	    }
	
	//两个参数的构造方法
	public Handler(Callback callback, boolean async) {
	        mLooper = Looper.myLooper();
	        if (mLooper == null) {
	            throw new RuntimeException(
	                "Can't create handler inside thread that has not called Looper.prepare()");
	        }
	        mQueue = mLooper.mQueue;
	        mCallback = callback;
	        mAsynchronous = async;
	    }

Handler的构造方法中会验证Looper，如果Looper为空，那么会抛出空指针异常

将自己的一个全局消息队列对象（mQueue）指向了Looper中的消息队列

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geyzjp6qv4j20xc0nbgm3.jpg)

#### Looper的构造函数 ####

	//Looper暴露出的静态初始化方法
	//这个方法会调用下面的私有静态方法
	  public static void prepare() {
	        prepare(true);
	    }
	//Looper私有的静态方法
	   private static void prepare(boolean quitAllowed) {
	        if (sThreadLocal.get() != null) {
	            throw new RuntimeException("Only one Looper may be created per thread");
	        }
	        sThreadLocal.set(new Looper(quitAllowed));
	    }
	//私有的构造方法，禁止外界调用
	  private Looper(boolean quitAllowed) {
	        mQueue = new MessageQueue(quitAllowed);
	        mThread = Thread.currentThread();
	    }
	
	//从sThreadLocal中获取一个Looper
	 public static @Nullable Looper myLooper() {
	        return sThreadLocal.get();
	    }

- 我们只能通过Looper.prepare()方法去初始化一个Looper
- Looper.prepare(boolean)方法的逻辑是一个线程中只能有一个Looper对象，否则在第二次尝试初始化Looper的时候，就会抛出异常。
- 以线程为单位存储Looper的主要逻辑是通过ThreadLocal实现的
- 私有的构造方法，禁止外界任意new出一个Looper

每个线程只有一个Looper

只有跟MessageQueue同一个包下才可以实例化MessageQueue，换句话说，我们用户是无法直接new一个MessageQueue对象出来的。而因为Looper在一个线程中只能有一个，从而导致MessageQueue也只能有一个
## 布局 ##
### LayoutParams ###
LayoutParams 的作用是：子控件告诉父控件，自己要如何布局

> LayoutParams 是 ViewGroup 的一个内部类，这是一个基类，例如 FrameLayout、LinearLayout 等等，内部都有自己的 LayoutParams

	LayoutParams(int width, int height)
	WRAP_CONTENT = -2
	MATCH_PARENT = -1
	FILL_PARENT = -1

传入的参数为实际的宽高 dp值

		LinearLayout ll = new LinearLayout(getContext());
        // ll的父容器是MainActivity中的FrameLayout
        ll.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundResource(android.R.color.holo_blue_bright);

        TextView tv = new TextView(getContext());
        // tv的父容器是LinearLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(160, 160);
        tv.setLayoutParams(layoutParams);// ①
        tv.setBackgroundResource(android.R.color.holo_red_dark);
        tv.setText(getText(R.string.tv));

LinearLayout 中添加子元素时，未设置LayoutParams时生成布局默认值

	public void addView(View child, int index) {
	    if (child == null) {
	        throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
	    }
	    LayoutParams params = child.getLayoutParams();
	    if (params == null) {
	        params = generateDefaultLayoutParams();
	        if (params == null) {
	            throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
	        }
	    }
	    addView(child, index, params);
	}

方向为默认是横向

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
	    if (mOrientation == HORIZONTAL) {
	        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    } else if (mOrientation == VERTICAL) {
	        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    }
	    return null;
	}

setLayoutParams 方法

	public void setLayoutParams(ViewGroup.LayoutParams params) {
	    if (params == null) {
	        throw new NullPointerException("Layout parameters cannot be null");
	    }
	    mLayoutParams = params;
	    resolveLayoutParams();// 根据已解析的布局方向解析布局参数
	    if (mParent instanceof ViewGroup) {
	        ((ViewGroup) mParent).onSetLayoutParams(this, params);
	    }
	    requestLayout();
	}

关键的最后一行 requestLayout() ，这个方法简单来说，就是重新执行 onMeasure() 和 onLayout()，而 onDraw() 需要适情况而定

- 在 LayoutParams 赋值后，如果确定还没有完成 View 的绘制，可以省略 setLayoutParams() ，在后面绘制期间，会取到前面的赋值，并使之生效。
- 如果已经完成了 View 的绘制，那么必须要调用 setLayoutParams() ，重新进行绘制。
- 不确定的情况下就 setLayoutParams() ，反正不会出问题。

对元素setWidth，setHeight并不一定是真实的值，只有在LayoutParas中设置的才是真实值

ConstraintLayout.LayoutParams 中设置对齐的参数

		public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0F;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int topToTop = -1;
        public int topToBottom = -1;
        public int bottomToTop = -1;
        public int bottomToBottom = -1;
        public int baselineToBaseline = -1;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public float circleAngle = 0.0F;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int endToStart = -1;
        public int endToEnd = -1;
        public int goneLeftMargin = -1;
        public int goneTopMargin = -1;
        public int goneRightMargin = -1;
        public int goneBottomMargin = -1;
        public int goneStartMargin = -1;
        public int goneEndMargin = -1;
        public float horizontalBias = 0.5F;
        public float verticalBias = 0.5F;
        public String dimensionRatio = null;
        float dimensionRatioValue = 0.0F;
        int dimensionRatioSide = 1;
        public float horizontalWeight = -1.0F;
        public float verticalWeight = -1.0F;
        public int horizontalChainStyle = 0;
        public int verticalChainStyle = 0;
        public int matchConstraintDefaultWidth = 0;
        public int matchConstraintDefaultHeight = 0;
        public int matchConstraintMinWidth = 0;
        public int matchConstraintMinHeight = 0;
        public int matchConstraintMaxWidth = 0;
        public int matchConstraintMaxHeight = 0;
        public float matchConstraintPercentWidth = 1.0F;
        public float matchConstraintPercentHeight = 1.0F;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int orientation = -1;
        public boolean constrainedWidth = false;
        public boolean constrainedHeight = false;
        boolean horizontalDimensionFixed = true;
        boolean verticalDimensionFixed = true;
        boolean needsBaseline = false;
        boolean isGuideline = false;
        boolean isHelper = false;
        boolean isInPlaceholder = false;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        int resolveGoneLeftMargin = -1;
        int resolveGoneRightMargin = -1;
        float resolvedHorizontalBias = 0.5F;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
### FrameLayout ###
默认从区域的左上角(可以通过layout_gravity控制)，帧布局的大小由控件中最大的子控件决定

前景图像，永远不会被覆盖

- android:foreground
- android:foregroundGravity

### CoordinatorLayout ###

### Presentation ###
presentation是一种特殊的对话框，主要用于在另外一块屏幕上显示内容。值得注意的是在创建presentation前必须把presentation与它的目标屏幕相关联。所以在使用它之前必须为它选择一个Display

Presentation的context与包含它的Activity的context是不相同的

当Presentation附属的display被移除的话，Presentation就会自动被取消。

选择display的方法主要有两种

- *一是利用MediaRouter* 
- *二是利用DisplayManager*

获取display服务
![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gepqazt1e4j210c0cvmxg.jpg)

Activity中

	@Override
    public Object getSystemService(@ServiceName @NonNull String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException(
                    "System services not available to Activities before onCreate()");
        }

        if (WINDOW_SERVICE.equals(name)) {
            return mWindowManager;
        } else if (SEARCH_SERVICE.equals(name)) {
            ensureSearchManager();
            return mSearchManager;
        }
        return super.getSystemService(name);
    }

由于Activity.java继承自ContextThemeWrapper.java，所以就会调用父类的getSystemService函数

	@Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return mInflater;
        }
        return getBaseContext().getSystemService(name);
    }

在创建Activity的时候ActivityThread调用ContextImpl的createActivityContext函数创建一个ContextImpl对象，然后通过Activity的attach函数将该contex对象设置到ContextWrapper中。所以getBaseContext函数获取的也就是一个ContextImpl对象，之后会调用该对象的getSystemService函数。最后就会调用到SystemServiceRegistry的getSystemService函数获取服务

	public static Object getSystemService(ContextImpl ctx, String name) {
        ServiceFetcher<?> fetcher = SYSTEM_SERVICE_FETCHERS.get(name);
        return fetcher != null ? fetcher.getService(ctx) : null;
    }

SystemServiceRegistry对象配置在了preloaded-classes文件中，当系统启动的时候在ZygoteInit的preload函数中进行加载该文件，对该文件中的对象进行预加载初始化。在SystemServiceRegistry对象的静态代码块中调用registerService函数将系统中的服务注册在SYSTEM_SERVICE_NAMES和SYSTEM_SERVICE_FETCHERS中

	registerService(Context.DISPLAY_SERVICE, DisplayManager.class,
                new CachedServiceFetcher<DisplayManager>() {
            @Override
            public DisplayManager createService(ContextImpl ctx) {
                return new DisplayManager(ctx.getOuterContext());
            }});

将Context.DISPLAY_SERVICE，DisplayManager.class以及CachedServiceFetcher对象类型为DisplayManager传入registerService函数。

	/**
	     * Statically registers a system service with the context.
	     * This method must be called during static initialization only.
     */
    private static <T> void registerService(String serviceName, Class<T> serviceClass,
            ServiceFetcher<T> serviceFetcher) {
        SYSTEM_SERVICE_NAMES.put(serviceClass, serviceName);
        SYSTEM_SERVICE_FETCHERS.put(serviceName, serviceFetcher);
    }

在DisplayManager的构造函数中将传入的context对象记录在mContext成员变量中，并且创建DisplayManagerGlobal对象，mGlobal对象的创建属于单例模式。

	   /**
	     * Gets an instance of the display manager global singleton.
	     *
	     * @return The display manager instance, may be null early in system startup
	     * before the display manager has been fully initialized.
	     */
    public static DisplayManagerGlobal getInstance() {
        synchronized (DisplayManagerGlobal.class) {
            if (sInstance == null) {
                IBinder b = ServiceManager.getService(Context.DISPLAY_SERVICE);
                if (b != null) {
                    sInstance = new DisplayManagerGlobal(IDisplayManager.Stub.asInterface(b));
                }
            }
            return sInstance;
        }
    }


### PreferenceActivity ###
PreferenceActivity其实和普通的Activity本质上来说区别不大，只不过多了些自动去读取SharedPrefrences的值来更新界面和其他一些逻辑

通过addPreferencesFromResource（xml资源id）加载静态xml资源文件 或者 完全通过代码构造对象再动态添加


## 属性 ##
### Padding ###
TextView设置背景图 setBackgroundResource, padding在背景内

ImageView设置src图, setImageResource, padding在图片外

TextView设置背景图，会导致padding变化，需要注意

### clipChildren ###
- clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
- clipToPadding用来定义ViewGroup是否允许在padding中绘制。默认情况下，cliptopadding被设置为ture， 也就是把padding中的值都进行裁切了。

### XLIFF ###
一个String中有多个需要替换的变量，可以在xml中定义如下变量：
<string name="info">your name is <xliff:g id="NAME">%1$s</xliff:g>, and your age is<xliff:g id="AGE">%2$s</xliff:g> </string>

	TextView tv = (TextView) findViewById(R.id.textView);
	String info = getResources().getString(R.string.info,"jnhoodlum","22"); tv.setText(info);

在String里要增加XLIFF的 xmlns：
<resources xmlns:android="http://schemas.android.com/apk/res/android"xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
### View的平移、缩放、旋转以及位置 ###
*View的相关变量*

//View的内容相对于View在水平方向上的偏移量，以像素为单位
//当mScrollX为正数时，内容相对于View从右向左移动，反之则从左向右移动
protected in mScrollX;

//View的内容相对于View在垂直方向上的偏移量，以像素为单位
//当mScrollY为正数时，内容相对于View从下向上移动，反之则从上向下移动
protected int mScrollY

> //获取内容偏移量mScrollX的值
> int android.view.View.getScrollX()
>  
> //获取内容偏移量mScrollY的值
> int android.view.View.getScrollY()
>  
> //设置内容偏移量mScrollX的值，此方法引发View重新调整内容的位置并重绘
> //相当于调用scrollTo(value,mScrollY)
> void android.view.View.setScrollX(int value)
>  
> //设置内容偏移量mScrollY的值，此方法引发View重新调整内容的位置并重绘
> //相当于调用scrollTo(mScrollX,value)
> void android.view.View.setScrollY(int value)
>  
> //将View的内容移动到参数所指定的位置中，此时mScrollX=x，mScrollY=y。
> //此方法引发View重新调整内容的位置并重绘
> void android.view.View.scrollTo(int x, int y)
>  
> //设置View内容移动的增量，相当于调用scrollTo(mScroll+x, mScroll+y);
> //此方法引发View重新调整内容的位置并重绘
> 
> void android.view.View.scrollBy(int x, int y)


#### 关于scrollTo()方法中参数值为正，却向左移动，参数值为负，却向右移动(这地方确实很怪)的一些理解 ####

	/**
	     * Move the scrolled position of your view. This will cause a call to
	     * {@link #onScrollChanged(int, int, int, int)} and the view will be
	     * invalidated.
	     * @param x the amount of pixels to scroll by horizontally<pre name="code" class="java">    /**
	     * Set the scrolled position of your view. This will cause a call to
	     * {@link #onScrollChanged(int, int, int, int)} and the view will be
	     * invalidated.
	     * @param x the x position to scroll to
	     * @param y the y position to scroll to
	     */
	    public void scrollTo(int x, int y) {
	        if (mScrollX != x || mScrollY != y) {
	            int oldX = mScrollX;
	            int oldY = mScrollY;
	            mScrollX = x;
	            mScrollY = y;
	            invalidateParentCaches();
	            onScrollChanged(mScrollX, mScrollY, oldX, oldY);
	            if (!awakenScrollBars()) {
	                postInvalidateOnAnimation();
	            }
	        }
	    }

如果要改变整个View在屏幕中的位置，可以使用下列API：

- offsetLeftAndRight(int offset) // 用于左右移动
- offsetTopAndBottom(int offset) // 用于上下移动

获取控件在屏幕中的绝对位置

- getLocalVisibleRect()
- getGlobalVisibleRect()
- getLocationOnScreen()
- getLocationInWindow()

这些API来获取控件在Parent中的相对位置

- getLeft(), 
- getTop(), 
- getBottom(), 
- getRight()

#### 移动View的相关变量和方法 ####

#### Android坐标系 ####
![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomgfbpm4j20a206pdfs.jpg)

    //此成员变量包含了View的平移、缩放、旋转、锚点等属性
    //平移相关的是mTransformationInfo.mTranslationX和mTransformationInfo.mTranslationY
    TransformationInfo mTransformationInfo;

Android旋转采用左手定则

View的平移、缩放、旋转、锚点信息都存放在View的成员变量mTransformationInfo中，

而mTransformationInfo还包含了一个矩阵变量mMatrix（可以通过View.getMatrix()获取），

这个矩阵作用很大，因为平移、绽放、旋转操作，都可以转化为对矩阵的数学运算……

	//此方法用于获取View在水平方向的缩放比例。
	public float android.view.View.getScaleX()
	 
	 
	//此方法用于获取View在垂直方向的缩放比例。
	public float android.view.View.getScaleY()
	 
	 
	//设置View在水平方向的缩放比例
	//scaleX=1，表示是原来的大小
	//scaleX<1，表示缩小，如scale=0.5f，表示宽度是原来的0.5倍
	//scaleX>1，表示放大，如scale=2.0f，表示宽度是原来的2.0倍
	public void android.view.View.setScaleX(float scaleX)
	 
	 
	//设置View在垂直方向的缩放比例
	//scaleY=1，表示是原来的大小
	//scaleY<1，表示缩小，如scale=0.5f，表示高度是原来的0.5倍
	//scaleY>1，表示放大，如scale=2.0f，表示高度是原来的2.0倍
	public void android.view.View.setScaleY(float scaleY)
	 
	 
	//设置锚点的X坐标值，以像素为单位。默认是View的中心。
	public void android.view.View.setPivotX(float pivotX)
	 
	 
	//设置锚点的Y坐标值，以像素为单位。默认是View的中心。
	public void android.view.View.setPivotX(float pivotX)

#### 设置旋转、缩放中心 ####
	
	//设置View旋转中心点的X坐标。
	public void android.view.View.setPivotX(float pivotX)
	 
	//设置View旋转中心点的Y坐标。
	public void android.view.View.setPivotX(float pivotX)
	
	//设置摄像机的与旋转目标在Z轴上距离
	//该方法只能用于setRotationX和setRoationY，对setRotation无影响
	void android.view.View.setCameraDistance(float distance)


#### Android Matrix ####
Android Matrix 本质上是一个如下图的矩阵

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomk2szchj20ah01vgle.jpg)

Matrix提供了如下几个操作

缩放（Scale）
对应 MSCALE_X 与 MSCALE_Y

位移（Translate）
对应 MTRANS_X 与 MTRANS_Y

错切（Skew）
对应 MSKEW_X 与 MSKEW_Y

旋转（Rotate）
旋转没有专门的数值来计算，Matrix 会通过计算缩放与错切来处理旋转

*数学原理*

矩阵乘法规则

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geommcoy75j20jp02oq2u.jpg)

新建一个 Matrix 后其矩阵为默认状态，其值如下

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomn35c7nj205x0420si.jpg)

默认状态下的数据都是初始值，即不做任何变换处理，所有坐标保持原样

*缩放（Scale）*

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomnsiv1yj209a044jr7.jpg)

Matrix 中用于缩放操作的方法有如下两个：

	void setScale(float sx, float sy);
	void setScale(float sx, float sy, float px, float py);

第二个重载方法多了两个参数 px、py，这两个参数用来描述缩放的枢轴点

初始化一个矩阵之后调用缩放方法：

	Matrix matrix = new Matrix()
	matrix.setScale(0.5F, 0.5F, 300F, 300F);

缩放 0.5 倍，枢轴点为 300，调用该方法后矩阵变换为：

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomqlencsj206c0420sj.jpg)

*位移（Translate）*

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomry4d9uj2099044glf.jpg)

设置位移操作的只有一个方法：

void setTranslate(float dx, float dy);

*错切（Skew）*

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomt3ft9zj20lo0bvjrp.jpg)

	matrix.setSkew(0.3F, 0.3F);

错切公式如下：

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomv5akzmj205502k3ya.jpg)

矩阵描述：

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomvdtiwtj209a044dfn.jpg)

错切操作的方法：

	void setSkew(float kx, float ky);
	void setSkew(float kx, float ky, float px, float py);

*旋转（Rotate）*

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomvwcxpoj20xc0irjrp.jpg)

旋转的公式

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomw7b0tmj208h02kq2r.jpg)

矩阵描述：

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geomwn4npkj20by044wec.jpg)

用于控制旋转的方法：

	void setRotate(float degrees);
	void setRotate(float degrees, float px, float py);

*Matrix 复合变换*

	//scale
	boolean preScale(float sx, float sy);
	boolean preScale(float sx, float sy, float px, float py);
	boolean postScale(float sx, float sy);
	boolean postScale(float sx, float sy, float px, float py);
	
	//translate
	boolean preTranslate(float dx, float dy);
	boolean postTranslate(float dx, float dy);
	
	//skew
	boolean preSkew(float kx, float ky);
	boolean preSkew(float kx, float ky, float px, float py);
	boolean postSkew(float kx, float ky);
	boolean postSkew(float kx, float ky, float px, float py);
	
	//rotate
	boolean preRotate(float degrees);
	boolean preRotate(float degrees, float px, float py);
	boolean postRotate(float degrees);
	boolean postRotate(float degrees, float px, float py);

*前乘（pre）*

相当于矩阵右乘

*后乘（post）*

相当于矩阵左乘


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

### SurfaceView及TextureView ###
#### SurfaceView是什么？ ####

它继承自类View，因此它本质上是一个View。但与普通View不同的是，它有自己的Surface。有自己的Surface，在WMS中有对应的WindowState，在SurfaceFlinger中有Layer(通常只有最顶层的DecorView，也就是根结点视图，才是对WMS可见的)

App端它仍在View hierachy中，但在Server端（WMS和SF）中，它与宿主窗口是分离的

- 这样的好处是对这个Surface的渲染可以放到单独线程去做，渲染时可以有自己的GL context。
- 但它也有缺点，因为这个Surface不在View hierachy中，它的显示也不受View的属性控制，所以不能进行平移，缩放等变换，也不能放在其它ViewGroup中，一些View中的特性也无法使用

*SurfaceView中双缓冲*

SurfaceView在更新视图时用到了两张Canvas，一张frontCanvas和一张backCanvas，每次实际显示的是frontCanvas，backCanvas存储的是上一次更改前的视图，当使用lockCanvas（）获取画布时，得到的实际上是backCanvas而不是正在显示的frontCanvas，之后你在获取到的backCanvas上绘制新视图，再unlockCanvasAndPost（canvas）此视图，那么上传的这张canvas将替换原来的frontCanvas作为新的frontCanvas，原来的frontCanvas将切换到后台作为backCanvas。

#### TextureView是什么 ####
 它可以将内容流直接投影到View中，它可以将内容流直接投影到View中，可以用于实现Live preview等功能。和SurfaceView不同，它不会在WMS中单独创建窗口，而是作为View hierachy中的一个普通View，因此可以和其它普通View一样进行移动，旋转，缩放，动画等变化。

- 优点：支持移动、旋转、缩放等动画，支持截图
- 缺点：必须在硬件加速的窗口中使用，占用内存比SurfaceView高，在5.0以前在主线程渲染，5.0以后有单独的渲染线程。

#### 播放器应该选择谁？ ####

从性能和安全性角度出发，使用播放器优先选SurfaceView。

1、在android 7.0上系统surfaceview的性能比TextureView更有优势，支持对象的内容位置和包含的应用内容同步更新，平移、缩放不会产生黑边。 在7.0以下系统如果使用场景有动画效果，可以选择性使用TextureView

2、由于失效(invalidation)和缓冲的特性，TextureView增加了额外1~3帧的延迟显示画面更新

3、TextureView总是使用GL合成，而SurfaceView可以使用硬件overlay后端，可以占用更少的内存带宽，消耗更少的能量

4、TextureView的内部缓冲队列导致比SurfaceView使用更多的内存

5、SurfaceView： 内部自己持有surface，surface 创建、销毁、大小改变时系统来处理的，通过surfaceHolder 的callback回调通知。当画布创建好时，可以将surface绑定到MediaPlayer中。SurfaceView如果为用户可见的时候，创建SurfaceView的SurfaceHolder用于显示视频流解析的帧图片，如果发现SurfaceView变为用户不可见的时候，则立即销毁SurfaceView的SurfaceHolder，以达到节约系统资源的目的

### ViewPager ###
ViewPager是android扩展包v4包中的类，这个类可以让用户左右切换当前的view

- 1）ViewPager类直接继承了ViewGroup类，所有它是一个容器类，可以在其中添加其他的view类。
- 2）ViewPager类需要一个PagerAdapter适配器类给它提供数据。
- 3）ViewPager经常和Fragment一起使用，并且提供了专门的FragmentPagerAdapter和FragmentStatePagerAdapter类供Fragment中的ViewPager使用。

PagerAdapter是基类适配器是一个通用的ViewPager适配器，相比PagerAdapter，FragmentPagerAdapter和FragmentStatePagerAdapter更专注于每一页是Fragment的情况，而这两个子类适配器使用情况也是有区别的。FragmentPagerAdapter适用于页面比较少的情况，FragmentStatePagerAdapter适用于页面比较多的情况

使用Fragment+PagerAdapter，而不是View+PagerAdapter，我一直以来都只有一个原因，对于复杂的布局，那就是Fragment有相对独立的生命周期，一切有迹可循，将代码从Activity中抽离，简化Activity的逻辑。

#### ViewPager2接口变更 ####
- FragmentStateAdapter << == FragmentStatePagerAdapter、FragmentPagerAdapter
- 是的，不用纠结了，统一使用FragmentStateAdapter
- RecyclerView.Adapter << == PagerAdapter
- 你没有看错，就是RecycleView，还有熟悉的ViewHolder、onBindViewHodler()
- FragmentStateAdapter#createFragment << == PagerAdapter#getItem
- 事实证明，以前的方法名称是过去出现问题的根源。使得不少新手在getItem()与instantiateItem()之间打交道时吃过亏。
- getItemCount << == getCount
- ViewPage2#registerOnPageChangeCallback << == addPageChangeListener
- 用抽象类替代接口，可以不用一次性override三个接口方法了，想用哪个就重载哪个

# 组件 #
## Activity ##


### 启动过程 ###
ViewRootImpl 是实现了 ViewParent 接口的，所以虽然 ViewRootImpl 没有继承 View 或 ViewGroup

### 生命周期 ###
7个回调函数覆盖Activity生命周期的每个环节

#### 3个生存期 ####
- 完整生存期:在onCreate方法和onDestroy方法之间经历的就是完成的生存期,onCreate中完成各种初始化操作，onDestroy中完成释放内存的操作
- 可见生存期:onStart方法和onStop方法之间经历的就是可见生存期，onStart中对资源进行加载，onStop中对资源进行释放
- 前台生存期:onResume方法和onPause方法之间
### 启动模式 ###
- standard:每启动一个新的Activity，它就会在返回栈中入栈，并处于栈顶位置
- singleTop:启动Activity时发现栈顶已是该Activity，就不会创建新的Activity实例
- singleTask:检查栈中是否存在该Activity的实例，若发现，则把该Activity之上的所有Activity全部出栈
- singleInstance:会有一个单独的栈来管理这个Activity，用来解决多个应用共享这个Activity的问题
## Fragment ##
### 生命周期 ###
- 运行状态
- 暂停状态
- 停止状态 当Activity进入停止状态或者调用Fragment的remove或者replace但在事务提交前调用了addToBackStack();总之对用户不可见
- 销毁状态 当Activity被销毁或者调用remove或replace，但没有调用addToBackStack方法

#### 回调方法 ####
- onAttach 当Fragment和Activity建立关联时
- onCreate 
- onCreateView 为Fragment创建视图时
- onActivityCreated 确保Fragment关联的Activity已经创建完毕时
- onStart
- onResume
- onPause
- onStop
- onDestroyView 当与Fragment关联的视图被移除时
- onDestroy
- onDetach 当Fragment和Activity解除关联时

#### fragment.setMenuVisibility setUserVisibleHint ####
Fragment真正意义上的onResume和onPause

	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
        }
    }

*FragmentPagerAdapter核心代码*

	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object
                + " v=" + ((Fragment)object).getView());
        mCurTransaction.detach((Fragment)object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

*使用例子*

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(mContainer, buttonView.getId());
            mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
            mFragmentPagerAdapter.finishUpdate(mContainer);
        } 
    }

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case R.id.radio_button1:
                return new Fragment1();
            case R.id.radio_button2:
                return new Fragment2();
            case R.id.radio_button3:
                return new Fragment3();
            case R.id.radio_button4:
                return new Fragment4();
            case R.id.radio_button0:
            default:
                return new Fragment0();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

*源码*

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

推荐直接使用ViewPager

*懒加载*

我们切换Fragment页面时，Adapter会调用fragement.setUserVisibleHint(boolean isVisibleToUser)来标志ViewPager的当前显示页面。而我们用getUserVisibleHint()来判断并执行懒加载。

懒加载的三个判断：

1. 是否为当前页面（是否可见）
1. 是否已经加载过了
1. 视图是否初始化完成（setUserVisibleHint()的调用在onCreateView之前！）

		var hasLoad = false
	
	    var isViewInitiated = false
	
	    fun loadData() {
	        if (userVisibleHint && !hasLoad && isViewInitiated) {
	            // load
	            hasLoad = true
	        }
	    }
	
	    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
	        super.setUserVisibleHint(isVisibleToUser)
	        loadData()
	    }
	
	    override fun onActivityCreated(savedInstanceState: Bundle?) {
	        super.onActivityCreated(savedInstanceState)
	        isViewInitiated = true
	        loadData()
	   }
	      override fun onDestroyView() {
	        super.onDestroyView()
	         isViewInitiated = false // 注意考虑View被销毁，而Fragment对象还在
	    }

BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT之下用的是Lifecycle

	 public Object instantiateItem(@NonNull ViewGroup container, int position) {
       
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        ...
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            // 看这里
            if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                mCurTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
            } else {
                fragment.setUserVisibleHint(false);
            }
        }
        return fragment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
               // 看这里
                if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                    if (mCurTransaction == null) {
                        mCurTransaction = mFragmentManager.beginTransaction();
                    }
                    mCurTransaction.setMaxLifecycle(mCurrentPrimaryItem, Lifecycle.State.STARTED);
                } else {
                    mCurrentPrimaryItem.setUserVisibleHint(false);
                }
            }
            fragment.setMenuVisibility(true);
            // 看这里
            if (mBehavior == BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                if (mCurTransaction == null) {
                    mCurTransaction = mFragmentManager.beginTransaction();
                }
                mCurTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
            } else {
                fragment.setUserVisibleHint(true);
            }

            mCurrentPrimaryItem = fragment;
        }
    }

STARTED这个值意味着什么呢？

- 新建的Fragment生命周期在执行onActivityCreated()之后会继续执行onStrat() ，但不会执行onResume()！
- 而原先处于RESUME状态的则会执行onPause().

改写懒加载方法

	var hasLoad = false

    fun loadData() {
        if (hasLoad) {
            // load
            hasLoad = true
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
## Broadcast ##
- 标准广播(normal broadcast)
- 有序广播(ordered broadcast) 可以被拦截(abortBroadcast)

Android 8.0之后所有隐式广播都不允许使用静态注册的方式来接收。隐式广播指的是没有具体指定发送给哪个广播的应用。少数特殊的系统广播仍然允许使用静态注册的方式。
## Content Provider ##
用于访问其他程序中的数据，通过uri来访问其他应用
### 数据存储 ###
- 文件存储
- SharedPreferences存储
- Sqlite数据库存储

### 运行时权限 ###
- 普通权限
- 危险权限
#### 权限组 ####

一旦用户同意了某个权限之后，同组的其他权限也会被系统自动授权
## Service ##

启动远程服务中的包名时setPackage使用的是远程项目根目录的包名，并非到服务所在的具体的路径，因为aidl引入时，使用了相同的根目录包名，故此可以识别
### AIDL ###
> 定向Tag表示在跨进程通信中数据的流向，用于标注方法的参数值，分为 in、out、inout 三种。其中 in 表示数据只能由客户端流向服务端， out 表示数据只能由服务端流向客户端，而 inout 则表示数据可在服务端与客户端之间双向流通。
#### AIDL refusing to generate code from aidl file defining parcelable ####
在aidl文件中缺少了对aidl中定义的类型的引用import

### Binder是如何出现的 ###
Android团队想要实现进程之间的通信，需要解决以下几个问题：

1. 如何知道客户端需要调用哪个进程以及该进程中的函数
1. 客户端如何将函数形参发送给远程进程中的函数，以及如何将远程进程函数计算结果返回客户端
1. 如何去屏蔽底层通信细节，让实现客户端调用远程函数就像调用本地函数一样

定义一个类，解决问题

1. 每个需要远程通信的类唯一标识就可以通过包名+类名的字符串，然后在类里面给每个函数编号即可对函数唯一编码。
1. 定义一个可打包的接口Parcelable，这个接口提供2个重要函数，分别是将对象中的属性写入到数组和从数组中的数据还原对象，每个可以发送到远程函数作为形参的对象只需实现Parcelable对象即可
1. 屏蔽进程之间的通信细节，帮用户发送远程请求并将拿到返回结果提交给用户

服务端想要实现被跨进程访问，就必须继承Binder类

#### Binder机制 ####
客户端将请求数据发送给Binder驱动并同时被挂起，Binder驱动从线程池中去取指定服务线程，并执行客户端指定的函数，将结果返回给Binder驱动，驱动将唤醒被挂起的线程并将结果返回给客户端

ServiceManager管理服务的注册和请求
#### Binder驱动实现原理 ####
客户端所持有的Binder引用并不是实际的远程Binder对象，引用在Binder驱动中还要做一次映射,客户端调用远程对象函数时，把数据写入Parcel，在调用Binder引用的transact函数时，transact函数会把参数、标识符(表示远程对象及其函数)等数据放入Client的共享内容,Binder驱动从Client共享内存中读取数据，并根据这些数据找到对应的远程进程的共享内存，把数据拷贝到远程进程的共享内存中，并通知远程进程执行onTransact函数。远程函数执行完成后，将得到的写入自己的共享内存中，Binder再将远程进程的共享内存通过映射拷贝到客户端的共享内存。

	//获取WindowManager服务引用
	WindowManager wm = (WindowManager)getSystemService(getApplication().WINDOW_SERVICE);  
	//布局参数layoutParams相关设置略...
	View view=LayoutInflater.from(getApplication()).inflate(R.layout.float_layout, null);  
	//添加view
	wm.addView(view, layoutParams);

在getSystemService内部就是向ServiceManager查询标识符为getApplication().WINDOW_SERVICE的远程对象的引用。得到这个引用之后，调用addView时，真正的实现在代理引用里面，代理把参数到包到Parcel对象中，然后调用transact函数，触发Binder驱动的一系列调用过程。
 
## Context ##
> Interface to global information about an application environment. This is an abstract class whose implementation is provided by the Android system. It allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc
> 
> 提供应用环境全局信息的接口，并且这个接口是由抽象类实现的，它的执行被android系统所提供，允许我们获取以应用为特征的资源和类型，同时启动应用级的操作，如启动Activity，broadcasting和接收intent。

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gf4rmsc157j20fh0cp3yf.jpg)

	Context数量 = Activity数量 + Service数量 + 1

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gf4rpdex8rj20el0ak0sl.jpg)

**Application是属于系统组件，系统组件的实例是要由系统来去创建的，如果这里我们自己去new一个MyApplication的实例，它就只是一个普通的Java对象而已，而不具备任何Context的能力**

	/** 
	 * Common implementation of Context API, which provides the base 
	 * context object for Activity and other application components. 
	 */  
	class ContextImpl extends Context{  
	    //所有Application程序公用一个mPackageInfo对象  
	    /*package*/ ActivityThread.PackageInfo mPackageInfo;  
	      
	    @Override  
	    public Object getSystemService(String name){  
	        ...  
	        else if (ACTIVITY_SERVICE.equals(name)) {  
	            return getActivityManager();  
	        }   
	        else if (INPUT_METHOD_SERVICE.equals(name)) {  
	            return InputMethodManager.getInstance(this);  
	        }  
	    }   
	    @Override  
	    public void startActivity(Intent intent) {  
	        ...  
	        //开始启动一个Activity  
	        mMainThread.getInstrumentation().execStartActivity(  
	            getOuterContext(), mMainThread.getApplicationThread(), null, null, intent, -1);  
	    }  
	}  

Context的类型有两种，一种是Activity-Context，另一种是Application-Activity，这两种的区别就在于它们的生命周期不一样，一个是随着Activity的销毁而销毁，另一个是伴随整个Application

Application-Context的生命周期是整个应用，所以，对于它的使用必须慎重，大部分情况下都要避免使用它，因为它会导致内存泄露的问题。

## ViewModel ##
[https://mp.weixin.qq.com/s/pomNsh-nrbXTXmg4nkmEbw](https://mp.weixin.qq.com/s/pomNsh-nrbXTXmg4nkmEbw)

> ViewModel的生命周期要比Activity长一点。因为配置更改导致的Activity销毁，ViewModel不会跟着销毁，只有Activity正常finish(比如用户点击back退出，或者退到后台杀掉App等)，ViewModel才会销毁。

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geyu37qh37j20ei0f33yg.jpg)

mViewModelStore是用来存储ViewModel的对象,onCreate方法可能会多次回调，我们在onCreate方法初始化ViewModel的，但是不可能每次onCreate回调都创建新的ViewModel对象，所以需要一个东西来存储我们之前创建过的ViewModel，这个就是ViewModelStore的作用。同时，ViewModel生命周期比Activity的长也是因为这个类

Activity因为资源限制被系统杀掉之后会重新恢复之前的状态，这里的on

NonConfigurationInstances是一个Wrapper，用来包装一下因为不受配置更改而影响的数据，比如Activity中的Fragment，旋转屏幕导致Activity重新创建，此时的Activity跟之前的不是同一个对象，但Fragment确实同一个，这就是通过NonConfigurationInstance实现的

	ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing,
            int configChanges, boolean getNonConfigInstance, String reason) {
        ActivityClientRecord r = mActivities.get(token);
       
            if (!r.stopped) {
                callActivityOnStop(r, false /* saveState */, "destroy");
            }
            if (getNonConfigInstance) {
                ...
                    r.lastNonConfigurationInstances
                            = r.activity.retainNonConfigurationInstances();
                ...
            }
            
                mInstrumentation.callActivityOnDestroy(r.activity);
                ...
            
            r.setState(ON_DESTROY);
        }
        return r;
    }

Activity的onStop和onDestroy之间，会回调retainNonConfigurationInstances方法，同时记录到ActivityClientRecord中去，这里retainNonConfigurationInstances方法返回的对象就是我们之前看到的NonConfigurationInstances对象

NonConfiguration对象的回复，可以从performLaunchActivity方法中找到答案。

	activity.attach(appContext, this, getInstrumentation(), r.token,
	                        r.ident, app, r.intent, r.activityInfo, title, r.parent,
	                        r.embeddedID, r.lastNonConfigurationInstances, config,
	                        r.referrer, r.voiceInteractor, window, r.configCallback);

#### onSaveInstanceState和ViewModel的区别 ####
onSaveInstanceState方法能保存的数据的场景

1. 由于资源限制，Activity被系统杀掉；
1. 配置更改

onSaveInstanceState方法只能保存少量简单的数据，大量和复杂数据最后不要放在onSaveInstanceState方法保存，因为onSAveInstanceState方法通过Bundle保存数据，如果数据的量太大或者太复杂，会阻塞UI线程，从而影响Activity生命周期的执行

ViewModel

1. ViewModel能保存的数据的场景：配置更改，不支持由于Activity重建的场景
1. ViewModel支持大量和复杂的数据，比如RecyclerView的data

#### SavedStateHandle的使用 ####
首先定义一个构造方法带SavedStateHandle参数的ViewModel

	class MyViewModelWithSaveStateHandle(private val saveStateHandle:SavedStateHandle) :  ViewModel(){
		//需要资源限制导致重建的场景下保存的数据
		//用LiveData暴露，不能让外部直接通过LiveData去修改内部的值
		val mAgeLiveData:LiveData<Int> = saveStateHandle.getLiveData(KEY_AGE)
		
		//普通的数据
		val mNameLiveData = MutableLiveData<String>()
		
		fun setAge(age:Int){
			saveStateHandle.set(KEY_AGE, age)
		}
		
		companion object {
			const val KEY_ARE = "key_age"
		}
	}

其次，通过AbstractSavedStateViewModelFactory或者SavedStateViewModelFactory创建ViewModel对象

	class main:AppcompatActivity(){
		override fun onCreate(saveInstanceState: Bundle){
			super.onCreate(savedInstanceState)
			setContentView(R.layout.activity_main)
			val viewModel = ViewModelProvider(
			this,
			saveStateViewModelFactory(application, this)
			)[MyViewModelWithSaveStateHandle::class.java]
		}
	}

#### SavedState components ####

1. SavedStateRegistryOwner:一个接口，有一个getSavedStateRegistry方法，作用是提供SavedStateRegistry对象。该接口主要实现类有Activity和Fragment。
1. SavedStateRegistryController:SavedStateRegistry的控制类，主要有两个方法：performRestore方法的作用恢复数据；performSave方法主要保存数据。Activity和Fragment直接操作类就是该类。
1. SavedStateRegistry: 主要是从UI控制器(Activity或者Fragment等)恢复数据，或者需要保存的数据写入UI控制器的Bundle里面；外部可以通过registerSavedStateProvider方法注册SavedStateProvider，这样SavedStateRegistry在保存数据会SavedStateProvider提供的数据。SavedStateRegistryController主要操作类就是该类。
1. SavedStateProvider: 主要是提供保存和恢复的数据。该接口只有一个saveState方法，主要的作用将需要保存的数据用Bundle包装起来。

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1geyu5r4amrj20hs0c9q2z.jpg)

*总结*

- ViewModel 和 onSaveInstaceState方法区别在于：ViewModel只能保存因为配置更改导致重建的数据，但是它能保存大量和复杂的数据；onSaveInstaceState能保存配置更改导致重建和资源限制导致重建的数据，但是它只能保存少量简单的数据。ViewModel使用SavedStateHandle能够保存资源限制导致重建的数据。
- ViewModel的生命周期之所以比Activity的生命周期生命周期，主要重建之后的Activity用的是之前的ViewStore。ViewModelStore保存的是之前的ViewModel，而ViewStore在配置更改导致重建不会清空已有ViewModel。
# Android 设备的CPU类型 (ABIs) #
- armeabiv-v7a: 第7代及以上的 ARM 处理器。2011年12月以后的生产的大部分Android设备都使用它.
- arm64-v8a: 第8代、64位ARM处理器，很少设备，三星 Galaxy S6是其中之一。
- armeabi: 第5代、第6代的ARM处理器，早期的手机用的比较多。
- x86: 平板、模拟器用得比较多。
- x86_64: 64位的平板。

**应用程序二进制接口（Application Binary Interface）**定义了二进制文件（尤其是.so文件）如何运行在相应的系统平台上，从使用的指令集，内存对齐到可用的系统函数库。在Android系统上，每一个CPU架构对应一个ABI：armeabi，armeabi-v7a，x86，mips，arm64-v8a，mips64，x86_64。

Android应用支持的ABI取决于APK中位于lib/ABI目录中的.so文件，其中ABI可能是上面说过的七种ABI中的一种。

例如ARM64和x86设备也可以同时运行armeabi-v7a和armeabi的二进制包。但最好是针对特定平台提供相应平台的二进制包，这种情况下运行时就少了一个模拟层（例如x86设备上模拟arm的虚拟层），从而得到更好的性能（归功于最近的架构更新，例如硬件fpu，更多的寄存器，更好的向量化等）


**你应该尽可能的提供专为每个ABI优化过的.so文件，但要么全部支持，要么都不支持：你不应该混合着使用。你应该为每个ABI目录提供对应的.so文件。**

作为一个经验法则，当只有一个.so文件时，静态编译C++运行时是没问题的，否则当存在多个.so文件时，应该让所有的.so文件都动态链接相同的C++运行时。

我们往往很容易对.so文件应该放在或者生成到哪里感到困惑，下面是一个总结：

- Android Studio工程放在jniLibs/ABI目录中（当然也可以通过在build.gradle文件中的设置jniLibs.srcDir属性自己指定）
- Eclipse工程放在libs/ABI目录中（这也是ndk-build命令默认生成.so文件的目录）
- AAR压缩包中位于jni/ABI目录中（.so文件会自动包含到引用AAR压缩包的APK中）
- 最终APK文件中的lib/ABI目录中
- 通过PackageManager安装后，在小于Android 5.0的系统中，.so文件位于app的nativeLibraryPath目录中；在大于等于Android 5.0的系统中，.so文件位于app的nativeLibraryRootDir/CPU_ARCH目录中。

*只提供armeabi架构的.so文件而忽略其他ABIs的*

x86设备能够很好的运行ARM类型函数库，但并不保证100%不发生crash，特别是对旧设备。64位设备（arm64-v8a, x86_64, mips64）能够运行32位的函数库，但是以32位模式运行，在64位平台上运行32位版本的ART和Android组件，将丢失专为64位优化过的性能（ART，webview，media等等）

可以选择在应用市场上传指定ABI版本的APK，生成不同ABI版本的APK可以在build.gradle中如下配置：

	android {
	    ... 
	    splits {
	        abi {
	            enable true
	            reset()
	            include 'x86', 'x86_64', 'armeabi-v7a', 'arm64-v8a' //select ABIs to build APKs for
	            universalApk true //generate an additional APK that contains all the ABIs
	        }
	    }
	
	    // map for the version code
	    project.ext.versionCodes = ['armeabi': 1, 'armeabi-v7a': 2, 'arm64-v8a': 3, 'mips': 5, 'mips64': 6, 'x86': 8, 'x86_64': 9]
	
	    android.applicationVariants.all { variant ->
	        // assign different version code for each output
	        variant.outputs.each { output ->
	            output.versionCodeOverride =
	                    project.ext.versionCodes.get(output.getFilter(com.android.build.OutputFile.ABI), 0) * 1000000 + android.defaultConfig.versionCode
	        }
	    }
	 }

Google 并不要求我们支持所有的 64 位架构，但是对于已经支持的每种原生 32 位架构，就必须包含对应的 64 位架构。

例如：

- 对于 ARM 架构，有 armeabi-v7a（32位） 就必须 arm64-v8a（64位）。
- 对于 x86 架构，有 x86（32位） 就必须有 x86_64（64位）

前面也提到，我们的项目中，可能会引入一些第三方库，导致在不明确的情况下，引入了一些预期之外的 ABIs 库。

通常我们的做法是在 Gradle 中增加 abiFilters 过滤，来确保不会在打包输出的 APK 中存在预期之外的 ABIs 目录和 so 库。

    ndk {
        //设置支持的SO库架构
        abiFilters 'armeabi-v7a' 
    }

#### 微信的安装包在只编译了armeabi，没有x86，arm64-v8a是如何运行在各种处理器的手机上的？ ####
arm64-v8a是可以向下兼容的，但前提是你的项目里面没有arm64-v8a的文件夹，如果你有两个文件夹armeabi和arm64-v8a，两个文件夹，armeabi里面有a.so 和 b.so,arm64-v8a里面只有a.so，那么arm64-v8a的手机在用到b的时候发现有arm64-v8a的文件夹，发现里面没有b.so，就报错了，所以这个时候删掉arm64-v8a文件夹，这个时候手机发现没有适配arm64-v8a，就会直接去找armeabi的so库，所以要么你别加arm64-v8a,要么armeabi里面有的so库，arm64-v8a里面也必须有

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

# 语言 #
## 泛型 ##
*PECS原则*
 
extend只能读，super只能写，producer用extends，consumer用super

	java.util.Collections.copy(List<? super T> dest, List<? extends T> src)

### 类型擦除 ###

> Java的泛型是伪泛型，这是因为Java在编译期间，所有的泛型信息都会被擦掉

在代码中定义List<Object>和List<String>等类型，在编译后都会变成List，JVM看到的只是List，而由泛型附加的类型信息对JVM是看不到的

*通过反射添加其它类型元素*



	ArrayList<Integer> list = new ArrayList<Integer>();

    list.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer

    list.getClass().getMethod("add", Object.class).invoke(list, "asd");

在程序中定义了一个ArrayList泛型类型实例化为Integer对象，如果直接调用add()方法，那么只能存储整数数据，不过当我们利用反射调用add()方法的时候，却可以存储字符串，这说明了Integer泛型实例在编译之后被擦除掉了，只保留了原始类型。

#### 原始类型 ####

> 原始类型 就是擦除去了泛型信息，最后在字节码中的类型变量的真正类型，无论何时定义一个泛型，相应的原始类型都会被自动提供，类型变量擦除，并使用其限定类型（无限定的变量用Object）替换

	class Pair<T> {  
	    private T value;  
	    public T getValue() {  
	        return value;  
	    }  
	    public void setValue(T  value) {  
	        this.value = value;  
	    }  
	} 

Pair的原始类型为:

	class Pair {  
	    private Object value;  
	    public Object getValue() {  
	        return value;  
	    }  
	    public void setValue(Object  value) {  
	        this.value = value;  
	    }  
	}

Pair这样声明的话
 
public class Pair<T extends Comparable{}

那么原始类型就是Comparable

要区分原始类型和泛型变量的类型。

- 在调用泛型方法时，可以指定泛型，也可以不指定泛型。
- 在不指定泛型的情况下，泛型变量的类型为该方法中的几种类型的同一父类的最小

		public class Test {  
		    public static void main(String[] args) {  
		
		        /**不指定泛型的时候*/  
		        int i = Test.add(1, 2); //这两个参数都是Integer，所以T为Integer类型  
		        Number f = Test.add(1, 1.2); //这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Number  
		        Object o = Test.add(1, "asd"); //这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Object  
		
		        /**指定泛型的时候*/  
		        int a = Test.<Integer>add(1, 2); //指定了Integer，所以只能为Integer类型或者其子类  
		        int b = Test.<Integer>add(1, 2.2); //编译错误，指定了Integer，不能为Float  
		        Number c = Test.<Number>add(1, 2.2); //指定为Number，所以可以为Integer和Float  
		    }  
		
		    //这是一个简单的泛型方法  
		    public static <T> T add(T x,T y){  
		        return y;  
		    }  
		}

**因为类型擦除的问题，所以所有的泛型类型变量最后都会被替换为原始类型**

#### 类型擦除与多态的冲突和解决方法 ####
JVM采用了一个特殊的方法，来完成这项功能，那就是桥方法

	class com.tao.test.DateInter extends com.tao.test.Pair<java.util.Date> {  
	  com.tao.test.DateInter();  
	    Code:  
	       0: aload_0  
	       1: invokespecial #8                  // Method com/tao/test/Pair."<init>":()V  
	       4: return  
	
	  public void setValue(java.util.Date);  //我们重写的setValue方法  
	    Code:  
	       0: aload_0  
	       1: aload_1  
	       2: invokespecial #16                 // Method com/tao/test/Pair.setValue:(Ljava/lang/Object;)V  
	       5: return  
	
	  public java.util.Date getValue();    //我们重写的getValue方法  
	    Code:  
	       0: aload_0  
	       1: invokespecial #23                 // Method com/tao/test/Pair.getValue:()Ljava/lang/Object;  
	       4: checkcast     #26                 // class java/util/Date  
	       7: areturn  
	
	  public java.lang.Object getValue();     //编译时由编译器生成的巧方法  
	    Code:  
	       0: aload_0  
	       1: invokevirtual #28                 // Method getValue:()Ljava/util/Date 去调用我们重写的getValue方法;  
	       4: areturn  
	
	  public void setValue(java.lang.Object);   //编译时由编译器生成的巧方法  
	    Code:  
	       0: aload_0  
	       1: aload_1  
	       2: checkcast     #26                 // class java/util/Date  
	       5: invokevirtual #30                 // Method setValue:(Ljava/util/Date; 去调用我们重写的setValue方法)V  
	       8: return  
	}

子类中的桥方法Object getValue()和Date getValue()是同时存在的，可是如果是常规的两个方法，他们的方法签名是一样的，也就是说虚拟机根本不能分别这两个方法。如果是我们自己编写Java代码，这样的代码是无法通过编译器的检查的，但是虚拟机却是允许这样做的，因为虚拟机通过参数类型和返回类型来确定一个方法，所以编译器为了实现泛型的多态允许自己做这个看起来“不合法”的事情，然后交给虚拟器去区别

#### 泛型在静态方法和静态类中的问题 ####
泛型类中的静态方法和静态变量不可以使用泛型类所声明的泛型类型参数

	public class Test2<T> {    
	    public static T one;   //编译错误    
	    public static  T show(T one){ //编译错误    
	        return null;    
	    }    
	}

因为泛型类中的泛型参数的实例化是在定义对象的时候指定的，而静态变量和静态方法不需要使用对象来调用。对象都没有创建，如何确定这个泛型参数是何种类型，所以当然是错误的。

	public class Test2<T> {    
	
	    public static <T >T show(T one){ //这是正确的    
	        return null;    
	    }    
	}

在泛型方法中使用的T是自己在方法中定义的 T，而不是泛型类中的T。


## Java ##
## Kotlin ##
### 协程 ###
协程（Coroutines）是一种比线程更加轻量级的存在，正如一个进程可以拥有多个线程一样，一个线程可以拥有多个协程。

> Generator函数是ES6对协程的不完全实现。Generator被称为“半协程”，意思是只有Generator函数的调用者，才能将程序的执行权还给Genertor函数。若是完全执行的协程，则任何函数都可以让暂停的协程继续执行。

**协程不是进程也不是线程，而是一个特殊的函数，这个函数可以在某个地方挂起，并且可以重新在挂起处外继续运行。所以说，协程与进程、线程相比并不是一个维度的概念**

如果是多核CPU，多个进程或一个进程内的多个线程是可以并行运行的，**但是一个线程内协程却绝对是串行的，无论CPU有多少个核。毕竟协程虽然是一个特殊的函数，但仍然是一个函数。**一个线程内可以运行多个函数，但这些函数都是串行运行的。当一个协程运行时，其它协程必须挂起。

*上下文切换*

- 进程的切换者是操作系统，切换时机是根据操作系统自己的切换策略，用户是无感知的。进程的切换内容包括页全局目录、内核栈、硬件上下文，切换内容保存在内存中。进程切换过程是由“用户态到内核态到用户态”的方式，切换效率低。
- 线程的切换者是操作系统，切换时机是根据操作系统自己的切换策略，用户无感知。线程的切换内容包括内核栈和硬件上下文。线程切换内容保存在内核栈中。线程切换过程是由“用户态到内核态到用户态”， 切换效率中等。
- 协程的切换者是用户（编程者或应用程序），切换时机是用户自己的程序所决定的。协程的切换内容是硬件上下文，切换内存保存在用户自己的变量（用户栈或堆）中。协程的切换过程只有用户态，即没有陷入内核态，因此切换效率高。

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

#### 代码不停产生GC ####
检查循环语句是否出现差错导致不能停下来

#### Presentation无法使用Fragment ####
Presentation实际上是一个Dialog，所以里面无法弹出Dialog、PopupWindow等依赖于Activity的小窗口，同样也无法使用Fragment

#### The specified child already has a parent ####

#### Task 'wrapper' not found in project ':xxxxxxxx' ####
#### design editor is unavailable until after a success sync ####
close project,重新import项目

# 散列 #
## 散列函数 ##
让键的各个部分均参与散列函数的计算

单个字符以Unicode将单个字符变成整数，浮点数以其32位二进制表示，组合键用霍纳法则简化，类似各个点位表示二进制的点位，Java中以31为基

Java中散列使用基于红黑树的拉链法

# 图像 #
H.265标准围绕着现有的视频编码标准H.264，保留原来的某些技术，同时对一些相关的技术加以改进。

新技术使用先进的技术用以改善码流、编码质量、延时和算法复杂度之间的关系，达到最优化设置。具体的研究内容包括：提高压缩效率、提高鲁棒性和错误恢复能力、减少实时的时延、减少信道获取时间和随机接入时延、降低复杂度等。H264由于算法优化，可以低于1Mbps的速度实现标清数字图像传送；H265则可以实现利用1~2Mbps的传输速度传送720P（分辨率1280*720）普通高清音视频传送。

#### 什么是H.264 ####
> H.264，同时也是MPEG-4第十部分，是由ITU-T视频编码专家组（VCEG）和ISO/IEC动态图像专家组（MPEG）联合组成的联合视频组（JVT，Joint Video Team）提出的高度压缩数字视频编解码器标准。这个标准通常被称之为H.264/AVC（或者AVC/H.264或者H.264/MPEG-4AVC或MPEG-4/H.264 AVC）而明确的说明它两方面的开发者。
> 
> H.264最大的优势是具有很高的数据压缩比率，在同等图像质量的条件下，H.264的压缩比是MPEG-2的2倍以上，是MPEG-4的1.5～2倍。举个例子，原始文件的大小如果为88GB，采用MPEG-2压缩标准压缩后变成3.5GB，压缩比为25∶1，而采用H.264压缩标准压缩后变为879MB，从88GB到879MB，H.264的压缩比达到惊人的102∶1。低码率（Low Bit Rate）对H.264的高的压缩比起到了重要的作用，和MPEG-2和MPEG-4ASP等压缩技术相比，H.264压缩技术将大大节省用户的下载时间和数据流量收费。尤其值得一提的是，H.264在具有高压缩比的同时还拥有高质量流畅的图像，正因为如此，经过H.264压缩的视频数据，在网络传输过程中所需要的带宽更少，也更加经济。

#### H.265与H.264有何不同 ####
> H.265/HEVC的编码架构大致上和H.264/AVC的架构相似，也主要包含：帧内预测(intra prediction)、帧间预测(inter prediction)、转换(transform)、量化(quantization)、去区块滤波器(deblocking filter)、熵编码(entropy coding)等模块。但在HEVC编码架构中，整体被分为了三个基本单位，分別是：编码单位(coding unit，CU)、预测单位(predict unit，PU)和转换单位(transform unit，TU)。
> 比起H.264/AVC，H.265/HEVC提供了更多不同的工具来降低码率，以编码单位来说， 最小的8x8到最大的64x64。信息量不多的区域(颜色变化不明显，比如车体的红色部分和地面的灰色部分)划分的宏块较大，编码后的码字较少，而细节多的地方(轮胎)划分的宏块就相应的小和多一些，编码后的码字较多，这样就相当于对图像进行了有重点的编码，从而降低了整体的码率，编码效率就相应提高了。同时，H.265的帧内预测模式支持33种方向(H.264只支持8种)，并且提供了更好的运动补偿处理和矢量预测方法。

## 颜色空间 ##
### HSV ###
*色调H*

用角度度量，取值范围为0°～360°，从红色开始按逆时针方向计算，红色为0°，绿色为120°,蓝色为240°。它们的补色是：黄色为60°，青色为180°,紫色为300°；

*饱和度S*

饱和度S表示颜色接近光谱色的程度。一种颜色，可以看成是某种光谱色与白色混合的结果。其中光谱色所占的比例愈大，颜色接近光谱色的程度就愈高，颜色的饱和度也就愈高。饱和度高，颜色则深而艳。光谱色的白光成分为0，饱和度达到最高。通常取值范围为0%～100%，值越大，颜色越饱和。

*明度V*

明度表示颜色明亮的程度，对于光源色，明度值与发光体的光亮度有关；对于物体色，此值和物体的透射比或反射比有关。通常取值范围为0%（黑）到100%（白）。

RGB和CMY颜色模型都是面向硬件的，而HSV（Hue Saturation Value）颜色模型是面向用户的。

HSV模型的三维表示从RGB立方体演化而来。设想从RGB沿立方体对角线的白色顶点向黑色顶点观察，就可以看到立方体的六边形外形。六边形边界表示色彩，水平轴表示纯度，明度沿垂直轴测量。

### YUV格式 ###
YUV，分为三个分量，“Y”表示明亮度（Luminance或Luma），也就是灰度值；而“U”和“V” 表示的则是色度（Chrominance或Chroma），作用是描述影像色彩及饱和度，用于指定像素的颜色。

>     与我们熟知的RGB类似，YUV也是一种颜色编码方法，主要用于电视系统以及模拟视频领域，它将亮度信息（Y）与色彩信息（UV）分离，没有UV信息一样可以显示完整的图像，只不过是黑白的，这样的设计很好地解决了彩色电视机与黑白电视的兼容问题。并且，YUV不像RGB那样要求三个独立的视频信号同时传输，所以用YUV方式传送占用极少的频宽。

YUV 表示三个分量， Y 表示 亮度（Luminance），即灰度值，UV表示色度（Chrominance），描述图像色彩和饱和度，指定颜色。YUV格式有YUV444、 YUV422 和 YUV420 三种，差别在于：

- YUV444： 每个Y分量对应一组UV分量
- YUV422：每两个Y分量共用一组UV分量
- YUV420：每四个Y分量共用一组UV分量

#### YUV的 planar和packed的差别？ ####
这是yuv格式的两大类

planar格式：连续存储所有像素点Y，然后是所有像素点U，接着是V

packed格式：所有像素点的YUV信息连续交错存储

#### YUV,YCbCr,YPbPr写法的含义 ####
它们分别代表在不同领域时使用的名称，总的大类都是一致的。主流上所说的YUV即是YCbCr

- YCbCr：其中Y是指亮度分量，Cb指蓝色色度分量，而Cr指红色色度分量
- YPbPr：他和YCbCr的区别在于YCbCr是数字系统的标识，YPbPr是模拟系统的标识

#### YUV中stride跨距的含义 ####
跨距的由来，因为CPU存储和读取必须是2的密次方，故而很多分辨率的yuv格式通常会有一个stride，比如某个720*536的YUV420SP视频，它的stride是768，那么中间48就是跨距。通常如果自己去解析，可以通过偏移裁取，如果采用第三方库，一般都会有传入跨距的值

#### YUV420 ####

- YV12和YU12都属于YUV420p，**其中Y\U\V分别对应一个plane**，区别在于UV的位置对调
- NV12和NV21,其中NV12就是我们Android常见的YUV420SP，他们不像上一个YV12，有3个plane，**而是由Y和UV分别两个Plane组成，UV交替排列，U在前的是NV12，V在前为NV21.**
- I420：或表示为IYUV,数码摄像机专用表示法.

- 一般来说，直接采集到的视频数据是RGB24的格式，RGB24一帧的大小size=width×heigth×3 Byte，RGB32的size=width×heigth×4 Byte，如果是I420（即YUV标准格式4：2：0）的数据量是 size=width×heigth×1.5 Byte。 在采集到RGB24数据后，需要对这个格式的数据进行第一次压缩。即将图像的颜色空间由RGB24转化为IYUV。因为，X264在进行编码的时候需要标准的YUV（4：2：0）。但是这里需要注意的是，虽然YV12也是（4：2：0），但是YV12和I420的却是不同的，在存储空间上面有些区别。如下：
- YV12 ： 亮度（行×列） + V（行×列/4) + U（行×列/4）
- I420 ： 亮度（行×列） +U（行×列/4) + V（行×列/4）

可以看出，YV12和I420基本上是一样的，就是UV的顺序不同。（摘自百度百科I420）

*通常编码时查看支持列表有它都可以传入，那我们来看看它可替代的这些format的含义：*

- COLOR_FormatYUV411PackedPlanar： YUV411，每4个连续的Y分量公用一个UV分量，并且Y分量和UV分量打包到同一个平面，用的不多。
- COLOR_FormatYUV420Planar：YUV420P,每2x2像素公用一个UV空间，Y分量空间–>U分量平面–>V分量平面
- COLOR_FormatYUV420PackedPlanar：YUV420 packet每2X2像素公用一个UV分量，并且将YUV打包到一个平面
- COLOR_FormatYUV420SemiPlanar:YUV420SP,即上述的NV12
- COLOR_FormatYUV420PackedSemiPlanar：Y分量空间–>V分量平面–>U分量平面，与COLOR_FormatYUV420Planar uv相反

# Java #
## 单例模式 ##
### 饿汉式 ###
	public class Singleton {
	
	    private static Singleton instance = new Singleton();
	
	    private Singleton() {
	    }
	
	    public static Singleton getInstance() {
	        return instance;
	    }
	
	}
### 懒汉式 ###
	public class Singleton {
	    
	    private static Singleton instance;
	
	    private Singleton() {
	    }
	
	    public static Singleton getInstance() {
	        if (instance == null) {
	            instance = new Singleton();
	        }
	        return instance;
	    }
	    
	}
### 双重校验 ###
	public class SingletonSafe {
	
	    private static volatile SingletonSafe singleton;
	
	    private SingletonSafe() {
	    }
	
	    public static SingletonSafe getSingleton() {
	        if (singleton == null) {
	            synchronized (SingletonSafe.class) {
	                if (singleton == null) {
	                    singleton = new SingletonSafe();
	                }
	            }
	        }
	        return singleton;
	    }
	}

### 静态内部类 ###
	public class Singleton {
	
	    private static class SingletonHolder {
	        private static Singleton instance = new Singleton();
	    }
	
	    private Singleton() {
	        
	    }
	
	    public static Singleton getInstance() {
	        return SingletonHolder.instance;
	    }
	}

### 单例模式 ###
	public enum Singleton {
	
	    INSTANCE;
	
	    public void doSomething() {
	        System.out.println("doSomething");
	    }
	
	}

### 反射攻击 ###
	public static void main(String[] args) throws Exception {
	    Singleton singleton = Singleton.getInstance();
	    Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
	    constructor.setAccessible(true);
	    Singleton newSingleton = constructor.newInstance();
	    System.out.println(singleton == newSingleton);
	}
### 反序列化攻击 ###
	public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        byte[] serialize = SerializationUtils.serialize(instance);
        Singleton newInstance = SerializationUtils.deserialize(serialize);
        System.out.println(instance == newInstance);
    }