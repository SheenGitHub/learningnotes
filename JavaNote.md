# 并发（Concurrent) #

## Java 多线程设计模式 ##

**wait set 线程的休息室**

执行wait()的线程就会进入this的wait set，就变成了线程在this上wait

欲执行wait()方法，线程需获取锁，当线程进入wait set时，已经释放了锁

notify()方法，可从wait set里抓一个线程，唤醒这个线程

欲执行notify()方法，必须调用实例的锁，才能执行

notify唤醒的线程并不是在notify的一瞬间就重新开始执行，因为在notify的那一刻，执行notify的线程还握着锁不放，（notify仅通知，不释放锁？）

（notify()或者notifyAll()方法并不是真正释放锁，必须等到synchronized方法或者语法块执行完才真正释放锁）

被唤醒的线程便开始去获取刚才进入wait事释放掉的锁，这个锁是在刚才执行notifyAll方法的线程手里

所有实例都会有wait set 所以 wait notify notifyAll 才会是Object类的方法

可容一个以上线程使用而不危及安全性的类成为thread-safe的类

多线程程序的评量标注

- 安全性——不损坏对象
- 生存性——进行必要的处理
- 复用性——可再利用类
- 性能——能快速、大量进行处理

### Single Threaded Execution Pattern ###
**能通过这座桥的，只有一个人**

Single Threaded Execution 视点放在运行的线程 

（Critical Section,Critical Region 临界区） 视点放在执行的范围

race condition 有race condition的情况下，很难预测各字段的值

synchronized方法，保证同时只有一个线程可以执行它


锁定需要花时间，线程冲突时必须等待

sychronized 方法和synchronized块，无论return或是异常，都会确实解除锁定，所以如果是成对的lock(),unlock(),就要使用finally

所有可由多个线程共享，并会访问字段的方法，都应该设置成synchronized加以保护

### Immutable ###
**想破坏它也没办法**

StringBuffer类是mutable，进行写修改时需要使用synchronized加以保护，影响速度

### Guard Suspension ###
**要等我准备好哦**

施加条件，加以防卫

线程真正等待的是实例状态的变化，等待警戒条件的变化

precondition 警戒条件就是“目的操作”的事前条件

GuardObject 只要警戒条件满足，就会执行，不满足，等待

guardedMethod， stateChangingMethod

Guarded Suspension Pattern 是附加条件的 Single Thread Execution Pattern，也就是有条件的 synchronized

共通特征：

- 有循环的存在
- 有条件的测试
- 因为某种原因在“等待”


guard suspension/guard wait/busy wait/ spin lock/ polling

### Balking ###

**不需要的话，就算了吧**

GuardedObject guardMethod 和 stateChangingMethod

使用条件

警戒条件只有一次成立时

不想等待警戒条件成立时

不需要刻意去执行的时候

synchronized 没有timeout，也不能中断

### Producer-Consumer ###
**我来做，你来用**

Pipe Pattern

throws InterruptedException 读成 "这个方法可以取消"

Channel担任传递Data参与者中继站、通道的角色

当对wait中的线程调用interrupt时，会先重新获取锁，再抛出InterruptedExecution。获取锁之前，无法抛出InterruptedException

interrupted这个方法只会改变线程的中断状态而已，sleep,wait,join这些方法会不断检查中断状态的值，而自己抛出InterruptedException

Thread.interrupted 方法会检查线程的中断状态并清除之

Thread.interrupted方法检查的对象是现在的线程

interrupt 方法 与 interrupted方法

interrupt 是将线程切换到中断状态的方法

interrupted是检查并清除中断状态的方法

stop方法， Thread类的stop方法，就算正在执行线程临界区的内容，也可以结束掉线程。以安全性的观点来看，这是有危险性的。

### Read-Write Lock ###
**大家想看就看吧，不过看的时候不能写**

### Thread-Per-Message ### 
**这个工作交给你了**

委托消息的一端和执行消息的一端是不同的线程

request是期待的操作开始处理的触发器，但不会等待执行结束

### Worker Thread ###
**等到工作来，来了就工作**

Background Thread 背景线程， 

管理工人线程的地方 Thread Pool

Worker的参与者数量,可以机动变化：

最先从某一个定量的Worker参与者开始
当工作量增加时，增加Worker参与者
不过增加太多会用完内存，所以到达某个上限要停止增加
相反地，工作减少时，（也就是待命的Worker参与者增加时）,就结束掉一些Worker参与者；

Worker Thread Pattern 与 Thread-Per-Message中将invocation与execution分离。这也是Command Pattern的主题

**提高响应性**

如果invocation与execution无法分离，当execution花很长时间时，invocation的操作也会受到牵累。

Worker Thread Pattern 相对于 Thread-Per-Message Pattern节省了启动线程所需的时间。

Swing Event-dispatching thread 只有一条

Worker参与者只有一条，可以省略共享互斥的问题

### Future ###
**先给您这张提货单**

参与Future的线程，会在时候再去获取执行结果

使用Future Pattern时，通过“稍后再设置真正的处理结果”，做到异步方法调用的“返回值”

**不让人等待的Future参与者**

getContent()方法不使用GuardedSuspension，而使用 Balking Pattern， “如果还没好，就马上退出”

**Open Call Pattern**

Host类没有字段（也就是根本没有状态）,所以就算被多条线程访问，也不会破坏安全性。可以说是Immutable Pattern的特殊形式，也是Open Call Pattern的特殊形式

> Open Call Pattern是当Host具有状态时，只对状态更新的部分加以防御的Pattern。

若同步执行需要花一些时间的操作，会是程序响应性降低，但是如果异步地执行，却无法在第一时刻得知结果

### Two-Phase Termination ###
**快把玩具收拾好**
先从作业中进入终止处理中，再真正结束掉线程

主要考虑的关键因素

- 安全地结束（安全性）
- 一定会进行终止处理（生命性）
- 收到终止请求后，要尽快开始终止处理（响应性）

**只检查表示只不够周全**
想要结束掉线程时，线程可能正在sleep，这样shutdownRequested标识为true，线程也不会结束。也可能在wait也不会退出。

只是用中断状态也是不够的

wait sleep join 一抛出InterruptedException后，线程就不是中断的了，不适用标识，记录会被抹掉


### Thread-Specific Storage ###
**每个线程的保管箱**

#### Actor-based 与 Task-based ####

#### Actior-based ####
代表线程的实例，会拥有进行工作所需的信息，这样可降低线程之间需要传递的信息。每个线程使用其他线程传来的信息进行处理，改变自己的状态。

#### Task-based ####
不将信息放在线程里，而是把信息放在线程之间传递的实例里。并不是只有数据，包括用来执行工作的方法也放在这个实例里。Worker Thread Pattern


## 常见问题 ##
- 安全性问题
- 活跃性问题
- 性能问题

## 基础 ##
共享的和可变的状态

*可变变量的同步*

1. 不在线程间共享状态变量
2. 将状态变量修改为不可变的
3. 访问状态变量时使用同步

当类中只包含自己的状态时，线程安全才是有意思的

**无状态对象一定是线程安全的**

### 竞态条件(Race Condition) ###
先检查后执行

基于一种**可能失效的观察结果**来做判断或执行某个计算

“读取-修改-写入” 称为 复合操作: 一组必须以原子方式执行的操作以确保线程安全性

尽可能地使用线程安全对象来管理状态类的状态

#### 同步代码块 ####

两部分：一个作为锁的对象引用，一个作为这个锁保护的代码块

每个Java对象都可以用作一个实现同步的锁，这些锁被称为内置锁(Intrinsic Lock)或监视锁(Monitor Lock)

Java的内置锁相当于一种互斥体，只能有一个线程获得这种锁

重入的“粒度”是“线程”，而不是“调用”，内置锁是可重入的

> pthread的互斥体默认的获取操作是以“调用”为粒度的

静态方法以Class对象作为锁

**锁保护**

对于可能被多个线程同时访问的可变状态变量，在访问它时都需要持有同一个锁

#### 可见性 ####
volatile变量不会作重排序，不会被缓存在寄存器或者队其他寄存器不可见的地方，因此读取volatile类型的变量总会返回最新写入的值

**典型用法**:检查某个状态标记以判断是否退出循环

**使用原则**:

1. 对变量的写入操作不依赖变量的当前值
2. 该变量不会与其他状态变量一起纳入不变性条件中
3. 访问变量是不需要枷锁

**发布**

使对象在当前作用域之外的代码中使用。

> 对象被不适当的发布造成未考虑的并发问题

**封闭线程**

Swing ： 可视化组件和数据模型对象

JDBC： Connection对象

*Ad-hoc线程封闭*

维护线程封闭性的职责完全由程序实现来承担，通常将子系统实现为一个单线程子系统

*栈封闭*

只有局部变量才能访问对象：**任何方法都无法获得对基本类型的引用**

在线程内部(Within-Thread)上限文中使用非线程安全的对象，那么该对象仍然是线程安全的

*ThreadLocal*

可以将ThreadLocal<T> 视为包含 Map<Thread,T>对象，其中保存了特定于该线程的值，**但ThreadLocal的实现并非如此**

**不变性**

不可变对象一定是线程安全的

**安全发布**

尚未被完全创建的对象被发布，对象是最新的，但是状态的值是失效的(final 可避免)

没有足够的同步，在多个线程间共享数据将会发生一些非常奇怪的问题

Java内存模型为不可变对象提供了一种特殊的初始换安全性保证。即使发布不可变对象的引用时没有使用同步，也仍然可以安全地访问该对象

不可变性需求:状态不可变，所有域都是final，正确的构造过程

安全的发布方式:

1. 静态初始化函数中初始化一个对象引用
1. 将对象的引用保存到volatile类型的域或者AtomicReferance对象中
1. 将对象的引用保存到某个正确构造对象的final类型域中
1. 将对象的引用保存到一个由锁保护的域中

对象的发布需求取决于它的可见性:

1. 不可变对象可以听过任意机制来发布
1. 事实不可变对象必须通过安全方式来发布
1. 可变对象必须通过安全方式来发布，并且必须是线程安全的或者由某个锁保护起来

**策略总结**

1. 线程封闭:对象封闭在县城中
1. 只读共享:不可变对象和事实不可变对象
1. 线程安全共享:多个线程可以通过对象的公有接口来访问而不需要进一步的同步
1. 保护对象:被保护的对象只能通过持有特定的锁来访问

> 只对外发布不可变对象和基本数据类型变量

#### 对象组合 ####
设计线程安全类的三要素:

- 找出构成对象那个状态的所以变量
- 找出约束状态变量的不变性条件
- 建立对象状态的并发访问管理策略

要保证类的线程安全性，就要确保它的不变性不会再并发访问的情况下被破坏，对象与变量都有一个状态空间，即所有可能的值。

状态空间越小，就越容易判断线程的状态。final类型的域越多，就越能简化对象可能状态的分析过程。

**实例封闭**

能够访问被封装对象的所有路径都是已知的

Java类库中多线程封闭的实例：将非线程安全的类转化为线程安全的类

Collections.synchronizedList及其类似方法

Java监视器模式：将对象的所有可变状态都封装起来，并由对象自己的内置锁来保护

使用私有锁对象，使客户代码无法得到锁

**私有构造函数捕获模式**

	private SafePoint(int[] a){ this(a[0],a[1]);}
	
	public SafePoint(SafePoint p){this(p.get());}
	
	public synchronized int[] get(){
		return new int[]{x,y};
	}

**添加一个新的源自操作**

1. 修改原始的类
 
	修改原始的类需要了解代码中的同步策略，与原有的设计一致
 
2. 扩展这个类，继承

	“扩展”方法比直接添加代码到类中更加策略，因为现在的同步策略被分布到多个单独维护的源代码文件中，若基类改变同步策略并选择了不同的锁来保护它的状态改变，那么子类被破坏

3. 将扩展代码放入一个辅助类中
	
	同上

4. 组合
	
	相对安全

迭代时容器被修改抛出CurrentModificationException(Fast-fail)

**在迭代期间需要对容器加锁**



 并发容器 
-

**ConcurrentHashMap**

分段锁(Locking Striping)

弱一致性(Weakly Consistent),容忍并发的修改，一些需要在整个Map上进行计算的方法，例如size和isEmpty这些方法的语义被略微减弱了以反映容器的并发特性

非独占锁，因此无法客户端加锁。

**CopyOnWriteArrayList**

"写入时复制"容器,不会抛出ConcurrentModificationException

阻塞队列
-

提供可阻塞的put和take方法,以及支持定时的offer和poll方法

简化了生产者-消费者设计的实现过程

put的阻塞减少了内存的消耗，是的消费者有时间赶上工作进度

offer如果数据不能被添加到队列中，返回一个失败状态

生产者-消费者的行为通过共享工作队列耦合在一起，需要构建资源管理机制

LinkedBlockingQueue，ArrayBlockingQueue， PriorityBlockingQueue(非FIFO顺序)

SynchronousQueue 不维护存储空间，维护一组线程(等待元素的加入和移出)

生产者-消费者模式：

生产者和消费者可以并发地执行，如果一个是I/O密集型，另一个是CPU密集型，那么并发的吞吐量高于串行执行的吞吐量。如果并行度不同，那么它们紧密耦合在一起把整体并行度降为二者中更小的并行度

对象池利用串行线程封闭，将对象“借给”一个请求线程。安全地在线程间传递所有权

双端队列

适合工作密取(Work Stealing)

同步工具类
-

根据自身的状态来协调线程的控制流

**闭锁**

CountDownLatch 

闭锁状态包含一个计数器，await方法等待计数器到达零

FutureTask(Future, 表示一种可生成结果的计算)

Callable 可生成结果的Runnable，可处以三种状态(Waiting to run, Running, Completed)

**信号量**

控制同时访问某个特定资源的操作数量

**栅栏**

CyclicBarrier类似于闭锁

所有线程必须同时到达栅栏位置，才能继续执行。闭锁用于等待事件，栅栏用于等待其他线程

如果对await的调用超时，或者await阻塞的线程被中断，那么栅栏被认为是打破了，所有线程终止，抛出BrokenBarrierException。 如果成功通过栅栏，await为每个线程返回一个唯一的到达索引号

Runtime.getRuntime().availableProcessors()获得处理器数

Exchanger是一种两方的(Two-party)栅栏，各方在栅栏位置交换数据

结果缓存
-

当缓存的是Future而不是值时，将导致缓存污染(Cache Pollution)问题：如果某个计算被取消活失败，那么结果将指明计算过程被取消或失败，futureTask抛出CanncellationException或ExecutionException，如果检测到RuntimeException，那么要移除Future
简单的缓存可能会将性能瓶颈转变成可伸缩性瓶颈

总结
-

- 可变状态是至关重要的。可变状态越少，越确保线程安全
- 尽量将域声明为final。除非它是可变的
- 不可变对象一定是线程安全的
- 封装有助于管理复杂性
- 用锁来保护每个可变变量
- 当保护同一个不变性条件中的所有变量时，要使用同一个锁
- 在执行复合操作期间，要持有锁
- 如果从多个线程中访问同一个可变变量时没有同步机制，那么程序会出现问题
- 不要故作聪明地推断不需要使用同步
- 在设计过程中考虑线程安全，或者在文档中明确指出它不是线程安全的
- 将同步策略文档化

Executor
-

解决每任务一线程的问题

**执行策略**

考虑执行任务中的 " What、Where、When、How"的问题

线程池
-

newFixedThreadPool,newCachedThreadPool,new SingleThreadPool, newScheduledThreadPool

只有当大量相互独立且同构的任务可以并发进行处理时，才能将程序的工作负载分配到多个任务重带来的真正性能提升。

CompletionService
-

	List<ImageInfo> info = scanForImageInfo(source);
	CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
	for(final ImageInfo imageInfo:info){
		completionService.submit(new Callable<ImageData>(){
			public ImageData call(){
				return ImageInfo.downloadImage();
			}
		});
	}
	
	try{
		for(int t = 0; n = info.size(); t < n; t++){
			Future<ImageData> f = completionService.take();
			ImageData imageData = f.get();
			renderImage(imageData);
		}
	}catch(InterruptedException e){
		Thread.currentThread().interrupt();
	}catch(ExecutionException e){
		throw launderThrowable(e.getCause());
	}

对照组

	final<ImageInfo> imageInfos = scanForImageInfo(source);
	Callable<List<ImageData>> task = new Callable<List<ImageData>>(){
		public List<ImageData> call(){
			List<ImageData> result = new ArrayList<ImageData>();
			for(ImageInfo imageInfo : imageInfos){
				result.add(imageInfo.downloadImage());
			}
			return result;
		}
	}
	
	Future<List<ImageData>> future = executor.submit(task);
	try{
		List<ImageData> imageData = future.get();
		for(ImageData data: imageData){
			renderImage(data);
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
		}catch(ExecutionException e){
			throw launderThrowable(e.getCause());
		}
	}

ExecutorCompletionService中使用QueueingFuture，当future完成时将其加入BlockingQueue(ExecutorCompletionService中使用LinkedBlockingQueue)

	private class QueueingFuture extends FutureTask<Void> {
        QueueingFuture(RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }
        protected void done() { completionQueue.add(task); }
        private final Future<V> task;
    }

为每一幅图像的下载都创建一个独立任务，并在线程池中执行它们，从而将串行的下载过程装换为并行的过程,减少下载所有图像的总时间，此外通过CompletionService中获取结果以及每张图片在下载完成后立刻显示出来，能使用户获得一个更加动态和更加响应性的用户界面

多个ExecutorCompletionService可以共享一个Executor，CompletionService相当于一组计算的句柄，与Future作为单个计算的句柄非常类似

**限时操作**

限时的future.get()

	V get(long timeout, TimeUnit unit)

Executors中的invokeAll()

		public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
            return e.invokeAll(tasks);
        }
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                             long timeout, TimeUnit unit)
任务取消
-

**Java没有提供任何机制来安全地终止线程**

停止一个线程的最佳方法：条件变量 或 条件变量+中断

但他提供了中断，这是一种协作机制，能够使一个线程终止另一个线程的当前工作，中断会使共享的数据处于不一致的状态

> **一个行为良好的软件和勉强运行的软件之间的最主要区别是，行为良好的软件能很完善地处理失败、关闭和取消等过程。**

设置"请求已取消"标志，使用volatile来保存取消状态

> **一个可取消的任务必须拥有取消策略(Cancellation Policy),在这个策略中将详细地定义取消操作的"How","When",以及"What",即其他代码如何("How")请求取消任务，任务在何时(When)检查是否已经请求了取消，以及在响应取消请求时应该执行哪些("What")操作。**

> Java的API或语言规范中，没有将中断与任何取消语义关联起来，在取消之外的其他操作中使用中断都是不合适的

每个线程中都有一个boolean类型的中断状态。当中断线程时，这个线程的中断状态将被设置为true。

	public class Thread{
		public void interrupt(){}//中断目标线程
		public boolean isInterrupted(){}//返回当前线程的中断状态
		public static boolean interrupted(){}//清楚当前线程的中断状态，返回它之前的值，这也是清楚中断状态的唯一方法
	}

阻塞库方法，例如Thread.sleep和Object.wait等，都会检查线程何时中断，并在发现中断时提前返回。它们在响应终端执行的操作包括:清楚中断状态，抛出InterruptedException,标识阻塞操作由于中断而提前结束。JVM不能保证检测中断的速度，但实际响应还是很快的。

当线程在非阻塞状态下中断时，它的中断状态将被设置，然后根据将被取消的操作来检查中断状态以判断发生了中断。----如果不出发InterruptedException，那么中断状态将一直保持，知道明确地清楚中断状态

> 调用interrupted并不意味着立即停止目标线程正在进行的工作，只是传递了请求中断的消息

有些方法，例如wait，sleep，join将严格处理这种请求，当它们收到中断请求或者执行时发现某个已经被设置好的中断状态时，将抛出一个异常。

如果调用interrupted时返回了true，那么除非你像屏蔽这个中断，否则必须对它进行处理——可以抛出InterruptedException或者再起调用interrupt来回复中断状态。

大多数可阻塞的库函数都只是抛出InterruptedException作为中断响应:尽快退出执行流程，把中断信息传递给调用者，从而使调用栈中的上层代码可以采取进一步的操作。

在捕获InterruptedException之后恢复中断状态

Thread.currentThread().interrupt();

通过推迟中断请求的处理，能制定更加灵活的中断策略


### 响应中断 ###
两种处理中断的策略

1. 传递异常
1. 恢复中断状态

对于一些不支持取消单仍可以调用中断阻塞方法的操作，必须在循环中调用这些方法，并在发现中断后重新尝试

IO操作无法响应中断，只有通过直接close来抛出异常

## 停止基于线程的服务 ##
对于持有线程的服务，只要服务的存在时间大于创建线程的方法的存在时间，那么就应该提供声明周期方法。

### shutdownNow的局限性 ###
需要保存执行被中断的线程

### 非正常终止的线程处理 ###
典型的线程池处理结构

	public void run(){
		Throwable thrown = null;
		try{
			while(!isInterrupted())
				runTask(getTaskFromWorkQueue());
		}catch(Throwable e){
			thrown = e;
		}finally{
			threadExited(this, thrown);
		}
	}
	//当线程抛出一个未检查的异常时，整个应用程序都可能受到影响

只有execute提交的任务，才能把它抛出的异常交给异常处理器，而通过submit提交的任务，无论抛出的未检查异常还是已检查异常，都会被认为是任务返回状态的一部分

JVM关闭
-

当最后一个非守护进程关闭时，或调用System.exit(),或是特定于平台的关闭(SIGINT信号或Ctrl-C),也可调用Rumtime.halt或在操作系统中杀死JVM进程(如SIGKILL)

### 关闭钩子 ###
通过Runtime.addShutdownHook注册的但尚未开始的线程

关闭钩子不应该依赖那些可能被应用程序或其他关闭钩子关闭的服务

注册关闭钩子来停止日记服务

	public void start(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				try{ LogService.this.stop();}
				catch(InterruptedException ignored){}
			}
		})
	}

### 守护进程 ###
尽量少地使用守护进程，很少操作能够在不进行清理的情况下被安全地抛弃

**避免使用终结器**

## 线程池 ##
### 线程饥饿死锁 ###
	public class ThreadDeadLock{
		ExecutorService exec = Executors.newSingleThreadExecutor();
		
		public class RenderPageTask implements Callable<String>{
			public String call throws Exception{
				Future<String> header, footer;
				header = exec.submit(new LoadFileTask("header.html"));
				footer = exec.submit(new LoadFileTask("footer.html"));
				String page = renderBody();
				//发生死锁，由于任务在等待子任务的结果	
				return header.get() + page + footer.get();
			}
		}
	}

ThreadPoolExecutor的通用构造函数

	public ThreadPoolExecutor(int corePoolSize,
							int maximumPoolSize,
							long keepAliveTime,//空闲时间超过存活时间将被回收
							TimeUnit unit,
							BlockingQueue<Runnable> workdQueue,
							ThreadFactory threadFactory,
							RejectedExecutionHandler handler){}

> 回收空闲线程会产生额外的延迟，因为当需求增加时，必须创建新的线程来满足需求

newFixedThreadPool工厂方法将线程池的基本大小和最大大小设置为参数中指定的值，而且创建的线程不会超时。

newCachedThreadPool工厂方法将线程池的最大大小设置为Integer.MAX_VALUE,而降基本大小设置为零，并且设置超时时间为1分钟。

如果无限制地创建线程，将导致不稳定，并通过采用固定大小的线程池来解决这个问题(newCachedThreadPool可能产生这个问题，突然大量的登录服务线程)

ThreadPoolExecutor允许提供一个BlockingQueue来保存等待执行的任务。基本的任务排队方法有3中:无界队列、有界队列和同步移交(Synchronous Handoff)

newFixedThreadPool和newSingleThreadExecutor默认使用一个无界的LinkedBlockingQueue，如果所有工作者线程都处于忙碌，那么任务在队列中等候

一种更稳妥的资源管理策略是是有有界队列，当队列填满后，有许多饱和策略。

对于非常大或者无界的线程池，可以通过使用SynchronousQueue来避免任务排队，以及直接将生产者交给工作者线程。SynchronousQueu不是一个真正的队列，而是线程之间进行移交的机制。如果没有线程咋等待，且线程数大于当前最大值，这个任务会被拒绝。只有工作线程时无界的或者可以拒绝任务时，SynchronousQueue才有实际价值。

newCachedThreadPool在接收网络请求的服务器应用中，如果不进行限制，很容易发生过载问题

#### 饱和策略 ####
不同的饱和策略:AbortPolicy,CallerRunsPolicy,DiscardPolicy,DiscardOldestPolicy

中止策略是默认的饱和测录，该策略将会抛出未检查的RejectedExecutionException，调用者可以捕获这个异常，根据需求编写自己的处理代码。。

当新提交的任务无法保存到队列中等待执行时，抛弃策略将会悄悄抛弃改任务。 抛弃最旧策略会抛弃下一个将被执行的任务，然后重新尝试提交新任务。（优先队列会抛弃优先级最高的任务，两者不要同时使用）

调用者运行策略，将某些任务退回到调用者，降低新任务的流量，调用一个execute的线程执行该任务。若持续过载，同样会抛弃请求。

**使用Semaphore来控制任务的提交速率**

	public class BoundedExecutor {
	    private final Executor exec;
	    private final Semaphore semaphore;
	
	    public BoundedExecutor(Executor exec, Semaphore semaphore) {
	        this.exec = exec;
	        this.semaphore = semaphore;
	    }
	
	    public void submitTask(final Runnable command) throws InterruptedException{
	        semaphore.acquire();
	        try {
	            exec.execute(() -> {
	                try {
	                    command.run();
	                } finally {
	                    semaphore.release();
	                }
	            });
	        } catch (RejectedExecutionException e) {
	            semaphore.release();
	        }
	    }
	}

**串行版本适合深度优先遍历，并行版本适合广度优先遍历**
## GUI ##

#### GUI为什么都是单线程 ####
存在难处理的竞态和死锁

用户的动作是从顶层到底层，系统的响应是从底层到底层，加锁容易出现死锁

## 死锁 ##
**Java应用程序无法从死锁中恢复过来**

安全性与活跃性之间存在某种制衡

过渡地使用加锁，可能导致顺序死锁(Lock-Ordering Deadlock),使用线程池和信号量来限制对资源的使用，可能导致资源死锁(Resource Deadlock)
哲学家问题；相互请求，形成抱死问题(Deadly Embrace)

数据库检测到一组事务发生死锁，将选择一个牺牲者并放弃这个事务。Java并没有数据库服务器那么强大。

### 锁顺序锁死 ###
如果所有线程以固定的顺序来获得锁，那么就不会出现顺序死锁问题

解决嵌套的锁获取操作，必须定义锁的顺序，并在整个应用程序中按照这个顺序来获取锁

	private static final Object tieLock = new Object();
	
	int fromHash = System.identityHashCode(fromAcct);
	int toHash = System.identityHashCode(toAcct);
	
	if(fromHash < toHash){
		synchronized(fromAcct){
			Synchronized(toAcct){
				new Helper().transfer();
			}
		}
	}else if(fromHash > toHash){
		synchronized(toAcct){
			Synchronized(fromAcct){
				new Helper().transfer();
			}
		}
	}else{
		synchronized(tieLock){//极少数两个对象有相同的散列值
			synchronized(toAcct){
				Synchronized(fromAcct){
					new Helper().transfer();
				}
			}
		}
	}

如果持有锁时，调用外部方法，将出现活跃性问题。在外部方法中可能获得其他锁，或者阻塞时间过长，导致其他线程无法及时获得当前被持有的锁。

#### 开放调用 ####
如果调用某个方法的时候不需要持有锁，那么这种方法被称为开发调用(Open Call)

采用开放调用的方式类似于采用封装机制提供的线程安全分析。使用锁对象的同步代码块而不是同步方法。

#### 资源死锁 ####
**线程饥饿死**

有界线程池/资源池与相互依赖的任务不能一起使用。(在Executor执行的线程中get子任务的Future)

## 死锁的避免与诊断 ##
### 两阶段策略 ###
1. 首先找出什么地方将获取多个锁(使这个集合尽量小)
2. 然后对这些实例进行全局分析，从而确保它们在整个程序中获得锁的顺序保持一致

使用shutdownRequest方法将shutdownRequest设置为true，还会调用interrupt方法，使线程就算在sleep和wait的状态下，也能结束掉

**定时锁**，只在同时获得两个锁时才有效

**活锁**，不会阻塞线程，但是会不断重复执行相同的操作，而且总会失败，比如事务的回滚

## 性能 ##
与单线程相比，使用多个线程总会引入一些额外的性能开销，包括:线程之间的协调(加锁，触发信号以及内存同步)，增加上下文切换，线程的创建和销毁，线程的调度。设置开销会超过由于提高吞吐量、响应性和计算能力所带来的的性能提升。

性能的指标:服务时间、延时时间、吞吐量、效率、可伸缩性以及容量

**可伸缩性**:当增加计算资源时(例如CPU、内存、存储容量或IO带宽)，程序的吞吐量或者处理能力相应地增加

> 大多数提高单线程程序性能的技术，往往都会破坏可伸缩性

**Amdahl定律**，并行部分的比率越高，加速比越大

上下文切换:如果可运行的线程数大于CPU的数量，那么操作系统会将某个正在运行的线程调度出来

上下文切换的开销相当于:5000~10000个时钟周期，也就是几微米

### 内存同步 ###
内存栅栏(Memory Barrier):内存栅栏可以刷新缓存，使缓存无效，刷新硬件的写缓存，以及停止执行管道；内存栅栏中大多数是不能被重排序的。

区分竞争的同步和无竞争的同步非常重要

synchronized机制针对无竞争的同步进行了优化，volatile通常是非竞争的。一个"快速通道"(Fast-Path)的非竞争同步消耗20~250个时钟周期。现代JVM会优化去掉一些不会发生竞争的锁。一个完备的JVM能通过逸出分析(Escape Analysis)来找出不会发布到堆本地的对象。

竞争的同步可能需要操作系统的接入。在锁上竞争失败的线程肯定会阻塞。JVM在实现阻塞行为时，可以采用自旋等待(Spin=Waiting,通过循环不断地尝试获取锁，直到成功)或者通过操作系统挂起被阻塞的线程。大多数JVM将线程挂起。

## 减少锁竞争 ##
影响因素:锁的请求频率和每次持有该锁的事件

缩小锁的范围，减小锁的粒度

### 段分锁 ###
ConcurrentHashMap

劣势:要获得多个锁实现独占访问将会更加困难并且开销更高

当ConcurrentHashMap需要扩展映射范围，以及重新计算键值的散列值要分布到更大的筒集合中时，就需要获取分段锁集合中所有的锁，ConcurrentHashMap为每个分段都维护一个独立的计数。

热点域，非独占锁(读写锁)

## 并发测试 ##
并发测试大致分两类:安全性测试和活跃性测试

### 活跃性测试 ###
- 吞吐量
- 响应性
- 可伸缩性

#### 测试设计 ####
执行与测试串行类时的相同的分析————找出需要检查的不变性和后验条件

## 可重入锁 ##
Lock提供了一种无条件、可轮询、定时的以及可中断的锁获取操作，所以加锁和解锁都是显示的

	public interface Lock{
		void lock();
		void lockInterruptibly() throws InterruptedException;
		boolean tryLock();
		boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;
		void unlock();
		Condition newCondition(); 
	}

ReentrantLock实现了Lock接口，并提供了synchronized相同的互斥性和内存可见性

### 优点 ###
无法中断一个正在等待获取锁的线程，或无法在请求一个锁时无限时等待下去。内置锁必须在获取该锁时在锁的代码块内释放，与异步操作处理实现良好的交互，无法实现非阻塞的加锁规则。

### 定时锁与轮询锁 ###
tryLock：避免死锁发生。

在无法获取两个锁时，选择释放现有的锁

### 可中断的锁获取操作 ###
lockInterruptibly方法能够在获取锁的同时保持对中断的响应

非块结构的加锁
# JavaScript #

## 严格模式 ##
**"use strict"**

未经声明的变量赋值在严格模式下回导致抛出ReferenceError(尝试未证实)

八进制字面量在严格模式下是无效的(Chrome:Uncaught SyntaxError: Octal literals are not allowed in strict mode.)

不允许使用with

不要把函数或参数命名为 eval 或arguments， 不能出现两个同名参数

对arguments赋值会导致语法错误

访问arguments.callee会导致错误

不能为函数的caller赋值

未指定环境对象而调用函数，this值不会转型为window，除非把函数添加到某个对象或调用apply或call，否则this值将是undefined

外部访问不到eval()中创建的任何变量或函数

给eval赋值也会导致错误

writable属性为false的属性，给它赋值将会抛出异常

尝试写入只指定了get特然函数的属性会抛出异常

只指定了setter函数的属性也不能读

超时函数全局执行，this在非严格模式下指向window， 严格模式下是undefined
## Number ##
NaN

任何数值除以非数值都会返回NaN

- 任何涉及NaN的操作都会返回NaN
- NaN与任何值都不相等，包括本身


补码
反码加1

按位非
负值减1

## String ##
String是基本数据类型，是值类型，不是引用类型，复制时拷贝一份新的副本


### Java class -version ###

    major  minor Java platform version 
    45   3   1.0
    45   3   1.1
    46   0   1.2
    47   0   1.3
    48   0   1.4
    49   0   1.5
    50   0   1.6
    51   0   1.7
    52   0   1.8

# Spring #
## IoC, Inversion of Control ##
`@ComonentScan` 扫描所在的包

`@Compent` `@Bean` `@Service` `@Value` 标注可注入的装配的类和值

`@Autowired` `Resource` 自动装配可注入的类
## Spring MVC ##
`@RestController` `@ResponseBody` 和 `@Controller`的结合

`@RequestMapping` 根据请求类型分类成 `@GetMapping` 和 `@PostMapping`

### RestTemplate ###
默认装配

	@Autowired
	private RestTemplateBuilder builder;

	@Bean
	public RestTemplate restTemplate(){
		return builder.build();
	}
## MyBatis ##

### 测试 ###
在测试中正常使用Mapper接口即可

    @Resource
    private UserDao userDao;
#### yml文件 ####
"Yet Another Markup Language"（仍是一种置标语言）

    spring:
      profiles:
        active: dev
      datasource:
	    driver-class-name: com.mysql.jdbc.Driver
	    url: jdbc:mysql://localhost:3306/wxauth
	    username: root
	    password: 123456
    
    mybatis:
      mapper-locations: classpath:mybatis/mapper/*.xml
      type-aliases-package: com.sheen.mapper

### Mapper ###
`@MapperScan("com.sheen.dao")` 扫描Mapper接口的包


在对应的包中建立接口，MyBatis根据配置文件为接口生成响应的代理类

使用xml配置 

	<mapper namespace="com.sheen.dao.UserDao">
	    <resultMap id="BaseResultMap" type="com.sheen.model.User">
	        <result column="ID" property="id" jdbcType="CLOB"/>
	        <result column="NAME" property="name" jdbcType="CLOB"/>
	        <result column="EMAIL" property="email" jdbcType="CLOB"/>
	    </resultMap>
	    <select id="getUserById" parameterType="java.lang.String" resultMap="BaseResultMap">
	      select ID,NAME,EMAIL FROM user WHERE ID= #{id,jdbcType=CLOB}
	    </select>
	</mapper>

**SQL中的Text类型对应CLOB**

使用Provider标注

	@UpdateProvider(type = ContactProvider.class, method = "updateContactByMessage")
	void updateContactByMessage(ReceivedMessageModel model);

	public String updateContactByMessage(ReceivedMessageModel model) {
        BEGIN();
        UPDATE(TABLENAME);

        Optional.ofNullable(model.getDepartment()).ifPresent((value)->{
            SET("GZBM=#{department}");
        });

        WHERE("YHID=#{userID}");
        return SQL();
    }

标注

   `#{field}` 对应于函数的参数 param中的param.field

	@Param("title") 标注参数的别名 可以使用#{title.field} 

##XML解析##
### 如何嵌套解析XML ###
使用JAXB，嵌套元素使用

    @XmlElementWrapper

根元素使用

    @XmlRootElement(name ="xml")

元素使用

    @XmlElement

CDATA元素转换使用

    @XmlJavaTypeAdapter(value= AdapterCDATA.class)

**在转换时去掉getter方法，去除条目的重复**

#### 阻止JAXB的转义 ####

    m.setProperty("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler",
    
    new CharacterEscapeHandler(){
        @Override
        public void escape(char[] ch, int start,
           int length, boolean isAttVal,
           Writer writer) throws IOException
        {
            writer.write(ch, start, length);
        }
        });

#### XML 文档结构必须从头至尾包含在同一个实体内 ####
XML 格式不对

### POST中的查询路径 ###
在请求路径query中的参数中使用 

    @RequestParam("timestamp") String timestamp

### maven 打包时，程序包com.sun.xml.internal.bind.v2.xxxx不存在 ###
因为使用了jdk内部的类库，在pom中加入以下对jre中类库的引用

    <plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-compiler-plugin</artifactId>
		<configuration>
			<source>1.8</source>
			<target>1.8</target>
			<encoding>UTF-8</encoding>
			<compilerArguments>
				<bootclasspath>${java.home}/lib/rt.jar;${java.home}/lib/jce.jar</bootclasspath>
			</compilerArguments>
		</configuration>
	</plugin>

### Ambiguous mapping ###
在RequestMapping中使用了Method = GET ，导致正常post可以获得GET的请求，出现歧义

## Reactor-Flux ##
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqnpsbjq4cj212i0pkdwp.jpg)
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqnpwsjbolj20v00i6n36.jpg)

# 面向对象设计原则 #
## 单一职责原色 ##
SRP,Single Responsibility Priciple

> 一个类应该仅有一个引起它变化的原因

职责的定义：变化的原因(a reason for change)

软件的首要技术失明————管理复杂度，而找出容易变化的区域，隔离变化，就是一种很好的管理复杂度的启发方法

### 分层 ###
1. UserManageService

    	public interface UserManageService {  
      
		    public ResultMsg create(Integer tokenId, String sign, User user);  
		      
		    public ResultMsg delete(Integer tokenId, String sign, Integer userId);  
		      
		    public ResultMsg update(Integer tokenId, String sign, User user);  
		      
		    public ResultMsg get(Integer tokenId, String sign, Integer userId);  
      
    	}  

2. UserDao接口

	    public interface UserDao{  
	      
		    public boolean add(User user);  
		      
		    public boolean delete( Integer userId);  
		      
		    public boolean update(User user);  
		      
		    public User get(Integer userId);  
	      
	    } 

3. Service的实现

		@Service("userManageService")  
  
		public class UserManageServiceImpl implements UserManageService {  
		  
			@Resource(name="UserDaoImpl")  
			  
			private UserDao<User> userDao;  
			  
			……  
			  
			@Override  
			  
			public ResultMsg create(Integer tokenId, String sign, User user) {  
			  
				…..  
				  
				//处理完业务后，判断是否需要调用UserDao接口的相应实现  
				  
				userDao.add(user);  
				  
				……  
			  
			 }  
			  
			 …..  
		  
		}

## 开放-封闭原则 ##
OCP, Open-Closed Principle

软件实体(类，模块，函数等)应该是可以扩展的，但是不可修改的

> 由于模块依赖一个固定的抽象体，所以它对于更改可以是封闭的。
> 
> 同时，通过这个抽象体派生，也可以扩展次模块的行为
> 
> 使用接口抽象，创建多个实现，彼此多态地替换
> 
> 使用抽象接口来调解客户端对原始类的访问，可以通过同一接口访问新类实现新功能

### 刺激变化 ###
因为变化发生（发现）越早、越快就越有利；刺激变化的一些方法：

      1、首先编写测试。

      2、使用很短的迭代周期进行开发。

      3、经常把开发特性展示给涉众。

      4、首先开发最重要的特性。

      5、尽早地、经常性地发布软件。尽可能频繁地把软件展示给客户和使用人员，得到他们的反馈。

**仅仅对程序中出现频繁变化的那些部分作出抽象，而拒绝不成熟的抽象**

## 里氏替换原则 ##
LSP, Liskov Substitution Priciple

派生类型必须能够替换掉它们的基类型

> 这里需要如下的替换性质：若对类型S的每一个对象O1,都存在一个类型T的对象O2，使得在所有针对T编写的程序P中，用O1替换O2后，程序P的行为功能不变，则S是T的子类型 ———— Barbara Liskov

Andy Hunt和Dave Thomas总结

> 派生类必须能通过基类的接口而被使用，且使用者无须了解两者之间的差异

 **除非派生类是一个"更特殊"的类，否则不应该从基类继承**

- 基于契约设计（Design By Contract，DBC） 
- 前置后置条件  

**子类必须接受父类可以接受的一切。父类的用户不应被所使用的子类的输出扰乱**

## 接口隔离原则 ##
ISP, Interface Segregation Principle

不应该强迫用户依赖于它们不用的方法

适应适配器分离转换接口

> 使用依赖性反转原则添加了Job类和其客户端之间的接口层。（适配器）
> 
> 创建了一个装订作业的Staple接口和一个打印作业的Print接口，并分别由Staple类和Print类使用，用来调用Job类的相关方法。
> 
> 这样就分离了Job类到两个类型的接口中。


 ISP与SRP的区别是：

- ISP主要针对接口，SRP主要针对类； 
-  ISP主要针对客户、SRP主要针对职责；
-  ISP是以客户需求/类型来分离接口的，

ISP与SRP的联系是：

- 一个类可能需要实现多个接口，这会产生职责耦合；
- 但分离多个接口就是一种解耦表现，一个接口相关职责的变化一般不会引起其他接口的变化。

## 依赖倒置原则 ##
DIP,Dependency Inversion Principle

a. 高层模块不应该依赖于底层模块，两者应该依赖于抽象

b. 抽象不应该依赖于细节；细节应该依赖于抽象

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqpv8w79k5j20qp06r74p.jpg)

### 实现 ###
#### 基础：依赖于抽象 ####

> 程序中所有的依赖关系都应该终止于抽象类或者接口中；
> 而不应该依赖于具体类。

（1）、类中的所有成员变量必须是接口或抽象，不应该持有一个指向具体类的引用或指针。

 即所有具体类只能通过接口或抽象类连接。

 （3）、任何类都不应该从具体类派生。

 （4）、任何方法都不应该覆写它的任何基类中已经实现的方法。（里氏替换原则）

 （5）、任何变量实例化都需要实现创建模式（如：工厂方法/模式），或使用依赖注入框架（如：Spring IOC）。

#### 核心：依赖倒置 ####
由客户类来声明它们需要的服务接口，改变实现抽象接口的类就不会影响到客户了

#### DIP在系统架构中的应用 ####
1、数据存储

 传统：

 业务层在不同数据类型的CURD地方，直接调用不同数据系统（Mysql/Mongo/Rides缓存/本地文件系统）的相关接口，把数据保存到相应系统。

 DIP：

 抽象数据存储层（DAO），DAO模块实现不同数据系统的接口；

 业务层调用DAO层接口传入不同类型数据，DAO层适配到相应数据系统。

 2、消息传输

 通常：

 上层通过调用下层接口，发消息给下层。

 一般情况上层需要同步等待下层处理后的响应。

 DIP：

 通过消息队列（Message Queue）解耦，实现异步传输；

 上层调用MQ的发送消息接口，消息发送到MQ，下层调用MQ获取消息的接口进行消费；

 这时上/下层都可以轻松进行扩展。

 注意：如果上层实时关注下层的处理结果，MQ就不适用。

 3、配置/服务注册中心

 传统：

 下层服务配置保存在配置文件或数据库，改动一些配置或扩展下层服务时可能影响到上层服务；

 如：

 上层服务依赖下层服务的IP、RPC服务地址等等。

 DIP：

 独立出来配置/服务注册中心；

 当下层服务配置改变时，更新到配置心中；配置中心把更新推送到上层服务。

 如：

 针对IP，可以通过DNS，上层使用的是域名，下层服务IP更新时，只需在DNS改动记录指向新的IP。

 针对RPC服务地址，通过服务注册中心（如：ZooKeeper）来发布；上层通过注册中心接口订阅所需服务，当下层通过注册中心接口扩展新注册服务或更新服务地址时，上层服务会收到订阅的服务更新情况。

## 迪米特法则 ##
LOD, Law of Demeter
最少知道原则

只与直接的朋友通信(Only talk to your immediate friends)

1. 从被依赖者的角度，只应该暴露应该暴露的方法
2. 从依赖者的角度来说，只依赖应该依赖的对象

## 组合/聚集复用原则 ##
CARP, Composite/Aggregate Reuse Principle

合成复用原则是指尽量使用合成/聚合，而不是使用继承

 继承复用通过扩展一个已有对象的实现来得到新的功能，基类明显地捕获共同的属性和方法，而子类通过增加新的属性和方法来扩展父类的实现。继承是类型的复用。













