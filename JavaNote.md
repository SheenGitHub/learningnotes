# Lang #
## 注解 ##
### Java内置注解 ### 
- @Override 
- @Deprecated 
- @SuppressWarnings
### 元注解 ###
- @Target (CONSTRUCTOR,FIELD,LOCAL_VARIABLE,METHOD,PACKAGE,PARAMETER,TYPE)
- @Retention (SOURCE 被编译器丢弃,CLASS 被VM丢弃,RUNTIME 可用反射读取)
- @Documented 包含在Javadoc中
- @Inherited 允许子类继承父类的注解

### 注解的元素 ###
注解的元素看起来就像是接口的方法

### 注解处理器 ###
继承 AbstractProcessor 接口 

	public synchronized void init(ProcessingEnvironment processingEncironment)

使用ProcessingEnvironment获取一些实用类以及获取选项参数

- Elements getElementUtils()	返回实现Elements接口的对象，用于操作元素的工具类。
- Filer getFiler()	返回实现Filer接口的对象，用于创建文件、类和辅助文件。
- Messager getMessager()	返回实现Messager接口的对象，用于报告错误信息、警告提醒。
- Map<String,String> getOptions()	返回指定的参数选项
- Types getTypeUtils()	返回实现Types接口的对象，用于操作类型的工具类。

#### 元素 ####
Element元素是一个接口，表示一个程序元素，比如包、类或者方法。

asType()可以获得元素的TypeMirror

- ExecutableElement 方法
- PackageElement 包package描述
- TypeElement 类或接口
- TypeParameterElement 方法的形式化参数
- VariableElement 变量

element.getKind() == ElementKind.CLASS

PACKAGE,ENUM,CLASS,ANNOTATION_TYPE,INTERFACE,ENUM_CONSTANT,FIELD,PARAMETER,LOCAL_VARIABLE,METHOD,CONSTRUCTOR,TYPE_PARAMETER


尽量避免instanceof 判断, interface 和 class混淆

在注解处理过程中，我们扫描所有的Java源文件。源代码的每一个部分都是一个特定类型的Element

Element代表的是源代码。TypeElement代表的是源代码中的类型元素，例如类。然而，TypeElement并不包含类本身的信息。你可以从TypeElement中获取类的名字，但是你获取不到类的信息，例如它的父类。这种信息需要通过TypeMirror获取。你可以通过调用elements.asType()获取元素的TypeMirror
#### 类型 ####
TypeMirror是一个接口，表示 Java 编程语言中的类型。这些类型包括基本类型、声明类型（类和接口类型）、数组类型、类型变量和 null 类型。还可以表示通配符类型参数、executable 的签名和返回类型，以及对应于包和关键字 void 的伪类型。

DeclaredType.asElement()可以转化为Element

- ArrayType 数组
- DeclaredType 类、接口声明
- ErrorType 无法建模的类、接口
- ExecutableType 方法
- NoType 伪类型
- NullType null类型
- PrimitiveType 基本类型
- ReferenceType 引用类型
- TypeVariable 类型变量
- WildcardType 通配符参数

TypeMirror.getKind() == TypeKind.BOOLEAN

BOOLEAN,INT,LONG,FLOAT,DOUBLE,VOID,NULL,ARRAY,PACKAGE,EXECUTABLE

#### 创建文件 ####
Filer接口支持通过注解处理器创建新文件。可以创建三种文件类型：源文件、类文件和辅助资源文件
#### 打印错误信息 ####
Messager接口提供注解处理器用来报告错误消息、警告和其他通知的方式.
#### 处理过程 ####
[原文](http://hannesdorfmann.com/annotation-processing/annotationprocessing101)

[翻译](https://blog.csdn.net/qfanmingyiq/article/details/75432854)

**因为注解处理是在编译Java源代码之前**

> 注解处理过程是一个有序的循环过程

process方法提供了两个参数，第一个是我们请求处理注解类型的集合（也就是我们通过重写getSupportedAnnotationTypes方法所指定的注解类型），第二个是有关当前和上一次 循环的信息的环境。返回值表示这些注解是否由此 Processor 声明，如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们；如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们

**规则**

我们先规定如下一些规则：

- 只有类可以被@Factory注解，因为接口或者抽象类并不能用new操作实例化；
- 被@Factory注解的类，必须至少提供一个公开的默认构造器（即没有参数的构造函数）。否者我们没法实例化一个对象。
- 被@Factory注解的类必须直接或者间接的继承于type()指定的类型；
- 具有相同的type的注解类，将被聚合在一起生成一个工厂类。这个生成的类使用Factory后缀，例如type = Meal.class，将生成MealFactory工厂类；
- id只能是String类型，并且在同一个type组中必须唯一。

**一个注解处理器任然是一个Java程序，所以我们需要使用面向对象编程、接口、设计模式，以及任何你将在其他普通Java程序中使用的技巧**
## enum ##
Enum是Java提供给编译器的一个用于继承的类。枚举量的实现其实是public static final T 类型的未初始化变量，之后，会在静态代码中对枚举量进行初始化。所以，如果用枚举去实现一个单例，这样的加载时间其实有点类似于饿汉模式，并没有起到lazy-loading的作用。
## 内部类 ##
### 外部类可以访问内部类private变量 ###
Java规范里确实规定了外部类可以访问内部类的private/protected变量，就像访问自己的private/protected变量一样.........实际上，编译器实现的时候是这样的：

Outer类和Inner类不再是嵌套结构，而是变为一个包中的两个类，然后，对于private变量的访问，编译器会生成一个accessor函数
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

竞争的同步可能需要操作系统的接入。在锁上竞争失败的线程肯定会阻塞。JVM在实现阻塞行为时，可以采用自旋等待(Spin-Waiting,通过循环不断地尝试获取锁，直到成功)或者通过操作系统挂起被阻塞的线程。大多数JVM将线程挂起。

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

锁的实现方式越好，将需要越少的系统调用和上下文切换，并且在共享内存总线上的内存同步通信量也越少

### 公平锁 ###
在公平的锁中，如果有另一个线程持有这个锁或者有其他线程在队列中等待这个锁，那么新发出的请求线程将会被排入队列中。在非公平的锁中，只有当锁被某个线程持有时，新发出请求的线程才会被放入队列中。

非公平锁的性能高于公平锁的性能的一个原因是：在恢复一个被挂起的线程与该线程真正开始运行之间存在严重的延迟。

#### 内置锁的有点 ####
自动释放，不用unlock

在线程转储中能给出在那些调用帧中获得了哪些锁，并且能够检测和识别发生死锁的线程。JVM并不知道哪些线程持有ReentrantLock。

## 读写锁 ##
#### 读写锁的可选实现 ####
- 释放优先
- 读线程插队
- 重入性
- 降级
- 升级

等待时间最常的线程将优先获得锁


> **基于状态的管理**

## 依赖状态性的管理 ##
提前条件不满足的情况下失败~等待前提变为真

	acquire lock on object state
	while(precondition does not hold){
		release lock
		wait until precondition might hold
		optionally fail if interrupted or timeout expires
		reacquire lock
	}
	perform action
		release

### 忙等待或自旋等待 ###
在抛出异常的时候，循环等待

> 使用wait和notiryAll来实现状态管理

## 条件队列 ##
条件谓词是使某个操作成为条件依赖的前提条件

三元关系：加锁、wait方法和一个条件谓词

### 状态依赖方法的的形式 ###
每当从wait中唤醒的时候，必须再次检测条件谓词

	void stateDependentMethod() throws InterruptedException{
		synchronized(lock){
			while(!conditionPredicate())
		}
	}

**单一的通知很容易导致信号丢失 notifyAll相对notify更加安全**

> 假设线程A在条件队列上等待条件谓词PA，同时线程B在同一条件队列上等待谓词PB。现在，假设PB变为真，兵线线程C执行一个notify：JVM从它拥有的众多线程中选择一个并唤醒。如果选择了线程A，那么它被唤醒，并且看到PA尚未变成真，因此继续等待。同时线程B本可以执行，却并没有唤醒。这不是严格意义上的“丢失信号”，而更像是一种“被劫持的”信号，但导致的问题是相同的：线程正在等待一个已经（或者本应该）发生过的信号。

#### 只有满足以下两个条件，才能使用单一的notify而不是notifyAll: ####

- 所有等待线程的类型都相同
- 单进单出:每次只能唤醒一个线程来执行

> notifyAll虽然可能比notify更低效，但是却更容易确保类的行为时正确的。容易造成大量的上下文切换以及锁获取操作

## Condition ##
Condition对Object类进行了扩展，wait，notify和notifyAll方法对应分别是await，signal和signalAll

	@ThreadSafe
	public class ConditionBoundedBuffer<T>{
		protected final Lock lock = new ReentrantLock();
		private final Condition notFull = lock.newCondition();
		private final Condition notEmpty = lock.newCondition();
		@GuardedBy("lock")
		private final T[] items = (T[])new Object[BUFFER_SIZE];
		@GuardedBy("lock") private int tail,head,count;
		
		public void put(T x) throws InterruptedException{
			lock.lock();
			try{
				while(count == items.length)
					notFull.await();
				items[tail] = x;
				if(++tail == items.length)
					tail = 0;
				++count;
				notEmpty.signal();
				}finally{
					lock.unlock();	
				}
			}
		}
		
		public T take() throws InterruptedException{
			lock.lock();
			try{
				while(count == 0)
					notEmpty.await();
				T x = item[head];
				if (++head == items.length)
					head = 0;
				--count;
				notFull.signal();
				return x;
				}finally{
					lock.unlock();
				}
		}
	}

## Synchronizer ##
AbstractQueuedSynchronizer是用于构建锁和同步器的框架

ReentrantLock,Semaphore,CountDownLatch,ReentrantReadWriteLock,SynchronousQueue,FutureTask

AQS负责管理同步类中的状态，它管理一个整数状态信息，可以通过getState,setState以及compareAndSetState等protected类型方法来进行操作。

ReentrantLock把它用来表示所有线程已经获得该锁的次数，Semaphore用他来表示剩余的许可数量，FutureTask用它来表示任务的状态(尚未开始、正在运行、已完成以及已取消)。同步器类中还可以自己管理一些额外的消息状态。

	final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

	public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

## AbstractQueuedSynchronizer ##
	
     /* @return a negative value on failure; zero if acquisition in shared
     *         mode succeeded but no subsequent shared-mode acquire can
     *         succeed; and a positive value if acquisition in shared
     *         mode succeeded and subsequent shared-mode acquires might
     *         also succeed, in which case a subsequent waiting thread
     *         must check availability. (Support for three different
     *         return values enables this method to be used in contexts
     *         where acquires only sometimes act exclusively.)  Upon
     *         success, this object has been acquired.
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

     /* @return {@code true} if this release of shared mode may permit a
     *         waiting acquire (shared or exclusive) to succeed; and
     *         {@code false} otherwise
     */
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

acquireSharedInterruptibly 处理失败方式

tryAcquire、tryRelease和isHeldExclusively 独占的方式

### ReentryLock 中的AQS ###
非公平锁

		final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }

### Semaphore 中的AQS ###

		final int nonfairTryAcquireShared(int acquires) {
            for (;;) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                    compareAndSetState(available, remaining))
                    return remaining;
            }
        }

        protected final boolean tryReleaseShared(int releases) {
            for (;;) {
                int current = getState();
                int next = current + releases;
                if (next < current) // overflow
                    throw new Error("Maximum permit count exceeded");
                if (compareAndSetState(current, next))
                    return true;
            }
        }

### Future 中的AQS ###
Future.get的语义非常类似于闭锁的语义————如果发生了某个事件(由FutureTask表示的任务执行完成或被取消)

## 非阻塞算法 ##
非阻塞方法的设计和实现相对复杂，但在可伸缩性和活跃性上拥有巨大的优势

非阻塞算法在可以使多个线程在竞争相同的数据的时候不会发生阻塞，因此它能在粒度更细的层次上进行协调，并且极大地减少调度开销，而且非阻塞算法中不存在死锁和活跃性问题。同时不受单线程失败的影响

### 锁的劣势 ###
锁定协议可以确保持有守护变量的锁，都能采用独占的方式访问这些变量，并且对变量的任何修改对随后获得这个锁的其他线程都是可见的。

如果多个线程同时请求锁，那么JVM就必需借助操作系统的功能，一些线程将被挂起并且在稍后恢复运行。当线程恢复执行时，必须等待其他线程执行完它们的时间片以后才能被调度执行。在挂起和恢复线程等过程中存在着很大的开销，并且通常存在着长时间的中断。

volatile是一种更轻量级的同步机制，可以保证可见性，但是不能构建原子的复合操作。如果一个线程在持有锁的情况下被延迟执行(例如缺页错误、调度延迟等)，那么需要这个锁的线程都无法执行下去。独占锁是一种悲观技术。

乐观的方法，通过借助冲突检查机制来判断在更新的过程中，是否存在来自其他线程的干扰。

原子读写，比较和交换(Compare-and-Swap)或者关联加载/条件存储(Load-Linked/Store-Condition)

**CAS在处理器级别完成**

CAS包括3个操作数————需要读写的内存位置V、进行比较的值A和拟写入的新值B。仅当位置V的值等于A时，才会更新V的值。CAS是一项乐观的技术。如果另一个线程在最近一次检查后更新了该变量，那么CAS能检测到这个错误。

一个线程在竞争CAS失败的时候不会阻塞，可以决定是否重新尝试（任存在活锁风险）

在竞争不高时，基于CAS的计数器在性能上远超过基于锁的计数器执行CAS不需要执行JVM代码、系统调用或线程调度操作。

主要缺点：使得调用者处理竞争问题(通过重试、回退、放弃)，而在锁中能自动处理竞争问题。

在支持CAS的平台上，运行时把它们编译为响应的机器指令。在不支持CAS指令，JVM将使用自旋锁。原子变量类中使用了JVM支持的高效的CAS操作

如果在数值范围内使用volatile引用或者多个原子整数，那么将出现不安全的“先检查再运行”操作

非阻塞算法：一个线程的是失败或挂起不会导致其他线程也失败或挂起

无锁(Lock-Free)算法：如果算法的每个步骤都存在某个线程能够执行下去

如果将CAS用于协调线程之间的操作，并且能够正确地实现，那么它即使一种无阻塞算法，又是一种无锁算法

非阻塞链接队列中的插入算法(Michael and Scott)，正是ConcurrentLinkedQueue中使用的算法

### ABA问题 ###
AtomicStampedReference支持两个变量上执行原子的条件更新，在“对象-引用”二元组上加上“版本号”，从而避免ABA问题。
AtomicMarkableReference跟新一个“对象引用-布尔值”二元组，标记“已删除的节点”

# Java内存模型 #

aVariable = 3；

如果缺少同步，那么将会有许多因素使得线程无法立即甚至永远，看到另一个线程的操作结果。在编译器中生成的指令顺序和源代码中的顺序不同。另外编译器还会把变量保存在寄存器中而不是内存。处理器可以采用乱序或并行的方式来执行指令；缓存可能会改变写入变量提交到主内存的顺序；保存在处理器本地的缓存中值，对其他处理器不可见。

JVM维护一种类似串行的语义:只要程序的最终结果与在严格串行环境中执行的结果相同。

重排序提高了计算的性能，同时包括流水线额超标量执行单元，动态指令调度，猜测执行以及完备的多级缓存。

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
## Spring容器 ##
Spring容器使用DI管理构成应用的组件，它会创建相互协作的组件之间的管理

Spring自带多个容器实现，可以分为两种不同的类型

### BeanFactory ###
最简单的容器，提供基本的DI支持
### ApplicationContext ###
基于BeanFactory构建，提供应用框架级别的服务(属性文件解析以及发布应用事件给监听者)

#### 类型 ####
- AnnotationConfigApplicationContext
- AnnotaiionConfigWebApplicationContext
- ClassPathXmlApplicationContext
- FileSystemXmlApplicationContext
- XmlWebApplicationVontext
#### 获取ApplicationContext的几种方式 ####
- 在初始化时保存ApplicationContext对象
- 通过Spring提供的工具类获取ApplicationContext对象(WebApplicationContextUtils)
- 继承抽象类ApplicationObjectSupport
- 继承抽象类WebApplicationObjectSupport
- 实现接口ApplicationContextAware
- 不依赖与Servlet和注入的方式，ContextLoader.getCurrentWebApplicationContext

### ServletContext ###
Servlet容器在启动时会加载Web应用，并为每个Web应用创建唯一的ServletContext对象

在ServletContext中可以存放共享数据，有4个读取或者设置共享数据的方法

#### ServletContextListener ####
> 在 Servlet API 中有一个 ServletContextListener 接口，它能够监听 ServletContext 对象的生命周期，实际上就是监听 Web 应用的生命周期。当Servlet 容器启动或终止Web 应用时，会触发ServletContextEvent 事件，该事件由ServletContextListener 来处理。在 ServletContextListener 接口中定义了处理ServletContextEvent 事件的两个方法

需要注册listener-class，在xml中或使用WebListener(注意注册@Component)

	@WebListener
	@Component
	public class GlobalContextListener implements ServletContextListener {
	
	    @Override
	    public void contextInitialized(ServletContextEvent sce) {
	        System.out.println("启动了");
	    }
	
	    @Override
	    public void contextDestroyed(ServletContextEvent sce) {
	        System.out.println("结束了");
	    }
	}

## 生命周期 ##
![](http://ww1.sinaimg.cn/mw690/48ceb85dly1fx6j9xso8dj22bc1eawog.jpg)

1. Spring对bean进行实例化
2. Spring将值和bean的引用注入到bean对应的属性中
3. 如果Bean实现BeanNameAware，将bean的ID传递，从而知道自己的名字
4. 如果Bean实现了BeanFactoryAware接口，传入BeanFactory，从而知道自己的Factory
5. 如果是实现了ApplicationContext，Spring将出入上下文，从而保留上下文
6. 如果实现了BeanPostProcessor接口，将调用postProcessBeforeInitialization方法
7. 如果Bean实现了InitializingBean接口，Spring将调用它的postProcessBeforeInitialization方法
8. 调用自定义的初始化方法init-method方法
9. 如果实现了BeanPostProcessor接口，将调用postProcessAfterInitialization方法
10. Bean可以使用了
11. 关闭容器
12. 调用DisposableBean接口的destroy方法
13. 调用自定义的销毁方法destroy-method
### 自定义创建和销毁阶段调用方法 ###
1. 注解方式 @PostContruct, @PreDestroy
2. 实现 InitilizingBean,DisposableBean接口
3. Java Config， 配置@Bean(initMethod="start", destroyMehtod="destroy")
4. XML中配置 init-method="" destroy-method=""

#### BeanNameAware ####
让Bean获取自己在BeanFactory配置中的名字（根据情况是id或者name）
#### BeanFactoryAware ####
让Bean获取配置他们的BeanFactory的引用

> 实际上非常不推荐这样做，因为结果是进一步加大Bean与Spring的耦合

**补充说明**

factory.preInstantiateSingletons()方法立即实例化所有的Bean实例,方法本身的目的是让Spring立即处理工厂中所有Bean的定义，并且将这些Bean全部实例化。因为Spring默认实例化Bean的情况下，采用的是lazy机制

#### ApplicationContextAware ####
> 在Web应用中，Spring容器通常采用声明式方式配置产生：开发者只要在web.xml中配置一个Listener，该Listener将会负责初始化Spring容器，MVC框架可以直接调用Spring容器中的Bean，无需访问Spring容器本身。在这种情况下，容器中的Bean处于容器管理下，无需主动访问容器，只需接受容器的依赖注入即可。

在方法

	void setApplicationContext(ApplicationContext applicationContext) 

中设置applicationContext

#### BeanPostProcessor简介 ####
BeanPostProcessor的实现类注册到Spring IOC容器后，对于该Spring IOC容器所创建的每个bean实例在初始化方法(afterPropertiesSet和任意声明的init方法)调用前，将会调用BeanPostProcessor中的postProcessBeforeInitializaition方法。而bean初始化方法调用完成后，会调用BeanPostProcessor中的postProcessAfterInitialization方法。
	
	--> Spring IOC容器实例化Bean
	--> 调用BeanPostProcessor的postProcessBeforeInitialization方法
	--> 调用bean实例的初始化方法
	--> 调用BeanPostProcessor的postProcessAfterInitialization方法

> Spring容器通过BeanPostProcessor给了我们一个机会对Spring管理的bean进行再加工。比如：我们可以修改bean的属性，可以给bean生成一个动态代理实例等等。一些Spring AOP的底层处理也是通过实现BeanPostProcessor来执行代理包装逻辑的。

> The BeanPostProcessor interface defines callback methods that you can implement to provide your own (or override the container's default) instantiation logic, dependency-resolution logic, and so forth. If you want to implement some custom logic after the Spring container finishes instantiating, configuring, and initializing a bean, you can plug in one or more BeanPostProcessor implementations.

## InitializingBean和DisposableBean ##
> **The difference to the @PostConstruct, InitializingBean and custom init method is that these are defined on the bean itself. Their ordering can be found in the Combining lifecycle mechanisms section of the spring documentation.**


ApplicationContextAware,BeanPostProcessor接口的实现在扫描全局，而这几个注解和接口在bean自身；
## IoC, Inversion of Control ##
`@ComonentScan` 扫描所在的包

`@Compent` `@Bean` `@Service` `@Value` 标注可注入的装配的类和值

`@Autowired` `Resource` 自动装配可注入的类
## Spring MVC ##
`@RestController` `@ResponseBody` 和 `@Controller`的结合

`@RequestMapping` 根据请求类型分类成 `@GetMapping` 和 `@PostMapping`

### @RequestParam，@PathParam，@PathVariable等注解区别 ###
@PathVariable

这个注解能够识别URL里面的一个模板

	@RequestMapping("/hello/{id}")
	    public String getDetails(@PathVariable(value="id") String id,
	    @RequestParam(value="param1", required=true) String param1,
	    @RequestParam(value="param2", required=false) String param2){
	.......
	}


### @ModelAttribute ###

> 在控制器的处理器方法参数上添加 @ModelAttribute 注释可以访问模型中的属性，如果不存在这个模型，则会自动将其实例化，产生一个新的模型。 模型属性还覆盖了来自 HTTP Servlet 请求参数的名称与字段名称匹配的值，也就是请求参数如果和模型类中的域变量一致，则会自动将这些请求参数绑定到这个模型对象，这被称为数据绑定，从而避免了解析和转换每个请求参数和表单字段这样的代码。

[ModelAttribut](https://blog.csdn.net/lovesomnus/article/details/78873089)
#### 应用在方法上 ####
1. 使用@ModelAttribute 注解无返回值的方法

被@ModelAttribute注解的方法会在IController每个方法执行之前都执行

2. 使用@ModelAttribute注解带有返回值的方法

返回值对象被默认放到隐含的Model中
#### 应用在方法的参数上 ####
1. 使用@ModelAttribute注解的参数，意思是从前面的Model中提取对应名称的属性
2. 从Form表单或URL参数中获取
#### 应用咋方法上，并且发放也使用了@RequestMapping #### 

> 在控制器的处理器方法参数上添加 @ModelAttribute 注释可以访问模型中的属性，如果不存在这个模型，则会自动将其实例化，产生一个新的模型。 模型属性还覆盖了来自 HTTP Servlet 请求参数的名称与字段名称匹配的值，也就是请求参数如果和模型类中的域变量一致，则会自动将这些请求参数绑定到这个模型对象，这被称为数据绑定，从而避免了解析和转换每个请求参数和表单字段这样的代码。
> 


	@PostMapping("/componies/{componyId}/departments/{departmentId}/edit")
	public String processSubmit(@ModelAttribute Department department) { }

这个处理器方法中的 department 参数会被从以下几个来源进行匹配绑定：

- 已经定义过的模型方法（带有 @ModelAttribute 的方法，后面解释）
- HTTP Session 中和字段名匹配的会话方法（带有 @SessionAttribute 的方法，和模型方法类似，只是作用域不同）
- 经过 URL 转换器解析过的路径变量
- 该模型类的默认构造方法
- 调用具有与 Servlet 请求参数匹配的参数的 “主构造函数”; 参数名称通过 JavaBeans @ConstructorProperties 或通过字节码中的运行时保留参数名称确定。

### ModelMap ###
ModelMap对象主要用于传递控制方法处理数据到结果页面，也就是说我们把结果页面上需要的数据放到ModelMap对象中即可

public String xxxxmethod(String someparam,ModelMap model) 

可以将someparam存入model供具体页面使用

### Spring中Model,ModelMap以及ModelAndView之间的区别 ###
[https://blog.csdn.net/zhangxing52077/article/details/75193948](https://blog.csdn.net/zhangxing52077/article/details/75193948)

①Model

Model是一个接口，它的实现类为ExtendedModelMap，继承ModelMap类

②ModelMap

ModelMap继承LinkedHashMap，spring框架自动创建实例并作为controller的入参，用户无需自己创建

③ModelAndView

顾名思义，ModelAndView指模型和视图的集合，既包含模型 又包含视图；ModelAndView的实例是开发者自己手动创建的，这也是和ModelMap主要不同点之一；

当然还可以这样设置跳转地址

	ModelAndView view = new ModelAndView();
	view.setViewName("test");
### RequestMapping中的produces ###
@RequestMapping(value = "/produces", produces = "application/json")：表示将功能处理方法将生产json格式的数据，此时根据请求头中的Accept进行匹配，如请求头“Accept:application/json”时即可匹配;
## 日志 ##
默认情况下，Spring Boot会用Logback来记录日志，并用INFO级别输出到控制台

[https://blog.csdn.net/inke88/article/details/75007649](https://blog.csdn.net/inke88/article/details/75007649)
## 定时器 ##
- java.util.Timer类
- Quartz 或者 elastic-job
- @Scheduled

@Scheduled 注释方法 @EnableSchduling 注释类

### cron ###
[cron](https://www.cnblogs.com/linjiqin/archive/2013/07/08/3178452.html)

Seconds Minutes Hours DayofMonth Month DayofWeek Year或 

Seconds Minutes Hours DayofMonth Month DayofWeek

\* 表示在任意秒/分/时

/ 步长

, 枚举

- 时间范围

> 只是说是 fixedRate 任务两次执行时间间隔是任务的开始点，而 fixedDelay 的间隔是前次任务的结束与下次任务的开始。

### 开始执行一次 ###

## 网络类 ##
### RequestContextHolder ###
获取request和response

RequestContextHolder顾名思义,持有上下文的Request容器

	
	        //两个方法在没有使用JSF的项目中是没有区别的
	        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
	//                                            RequestContextHolder.getRequestAttributes();
	        //从session里面获取对应的值
	        String str = (String) requestAttributes.getAttribute("name",RequestAttributes.SCOPE_SESSION);
	
	        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
	        HttpServletResponse response = ((ServletRequestAttributes)requestAttributes).getResponse();


RequestContextHolder这个类,里面有两个ThreadLocal保存当前线程下的request

[https://blog.csdn.net/u012706811/article/details/53432032](https://blog.csdn.net/u012706811/article/details/53432032)

	//得到存储进去的request
	    private static final ThreadLocal<RequestAttributes> requestAttributesHolder =
	            new NamedThreadLocal<RequestAttributes>("Request attributes");
	    //可被子线程继承的request
	    private static final ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder =
	            new NamedInheritableThreadLocal<RequestAttributes>("Request context");

FrameworkServlet查看到该类重写了service(),doGet(),doPost()…等方法,这些实现里面都有一个预处理方法processRequest(request, response);


#### Environment类 ####

Environment是Spring核心框架中定义的一个接口

这个接口对应用程序运行环境的两个关键方面进行了建模 :

profile 

- 一个profile是一组Bean定义(Bean definition)的逻辑分组(logical group)。
- 这个分组，也就是这个profile，被赋予一个命名，就是这个profile的名字。
- 只有当一个profile处于active状态时，它对应的逻辑上组织在一起的这些Bean定义才会被注册到容器中。
- Bean添加到profile可以通过XML定义方式或者annotation注解方式。
- Environment对于profile所扮演的角色是用来指定哪些profile是当前活跃的/缺省活跃的。

property 属性 

- 一个应用的属性有很多来源: 属性文件(properties files),JVM系统属性，系统环境变量，JNDI，servlet上下文参数，临时属性对象等。
- Environment对于property所扮演的角色是提供给使用者一个方便的服务接口用于配置属性源
- 从属性源中获取属性

### 拦截器/过滤器HandlerInterceptorAdapter的使用 ###
preHandle在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制等处理； 

postHandle在业务处理器处理请求执行完成后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView； 

afterCompletion在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面），可以根据ex是否为null判断是否发生了异常，进行日志记录

### MultipartFile文件保存 ###
	MultipartFile
	void transferTo(File dest)

#### 在application.yml中配置全局参数 ####

#### @ConfigurationProperties  ####
为被注释的类自动注入property中的数据

#### @ConditionalOnProperty ####
这个注解能够控制某个configuration是否生效。具体操作是通过其两个属性name以及havingValue来实现的，其中name用来从application.properties中读取某个属性值，如果该值为空，则返回false;如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。如果返回值为false，则该configuration不生效；为true则生效
#### 类型不匹配拒绝 ####
Field error in object 'user' on field 'age': rejected value [陈意]; codes typeMismatch

### RestTemplate ###
默认装配

	@Autowired
	private RestTemplateBuilder builder;

	@Bean
	public RestTemplate restTemplate(){
		return builder.build();
	}

## AOP ##
切点的织入

	@Aspect
	@Component
	public class PermissionAspect{
		//创建切点
		@Pointcut("execution (* com.sheen.pc.**.controller.TaskController.*(..))"
		            + "||execution (* com.sheen.pc.**.controller.ProcessController.*(..))")
		public void controllerAspect(){
		}	
		
		//在切点的操作
		@Before("controllerAspect()")
	    public void doBefore(JoinPoint joinPoint) throws Exception {
	        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
	                .getRequest();
		}
	}

## 应用 ##
### MethodInterceptor方法拦截器 ###

## MyBatis ##

#### 查询中有中文，结果为空 ####
yml中设置字符集 url: jdbc:mysql://localhost:3306/pc?useUnicode=true&characterEncoding=UTF-8
### 测试 ###
在测试中正常使用Mapper接口即可

    @Resource
    private UserDao userDao;

#### 数据库字段与数据类字段的对应 使用@Results ####

@Results是以@Result为元素的数组，@Result表示单条属性-字段的映射关系

**@ResultMap**

从SQL查询结果集到JavaBean或POJO实体的过程。

  1. 通过JDBC查询得到ResultSet对象

  2. 遍历ResultSet对象并将每行数据暂存到HashMap实例中，以结果集的字段名或字段别名为键，以字段值为值

  3. 根据ResultMap标签的type属性通过反射实例化领域模型

  4. 根据ResultMap标签的type属性和id、result等标签信息将HashMap中的键值对，填充到领域模型实例中并返回

实例

	@SelectProvider(type = TestSqlProvider.class, method = "getAllSql")  
    @Options(useCache = true, flushCache = false, timeout = 10000)  
    @Results(value = {  
            @Result(id = true, property = "id", column = "test_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),  
            @Result(property = "testText", column = "test_text", javaType = String.class, jdbcType = JdbcType.VARCHAR) })  
    public List<TestBean> getAll(); 

动态映射

	<select id="getStundent" resultMap="rm">
	  SELECT ID, Name, JuniorHighSchool, SeniorHighSchool, during
	    FROM TStudent
	</select>
	<resultMap id="rm" type="EStudent">
	  // 若不加这句，则当将juniorHighSchool赋予给seniorHighSchool属性时，juniorHighSchool属性将为null
	  <result column="juniorHighSchool" property="juniorHighSchool"/>
	
	  <discriminator column="during" javaType="_int">
	    // 形式1：通过resultType设置动态映射信息
	    <case value="4" resultType="EStudent">
	      <result column="juniorHighSchool" property="seniorHighSchool"/>
	    </case>
	
	   // 形式2: 通过resultMap设置动态映射信息
	   <case value="5" resultMap="dynamicRM"/>
	   <case value="6" resultMap="dynamicRM"/>
	  </discriminator>
	</resultMap>
	<resultMap id="dynamicRM" type="EStudent">
	  <result column="juniorHighSchool" property="seniorHighSchool"/>
	</resultMap>
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

## IDEA springboot 热部署 ##
[SpringBoot配置devtools实现热部署](https://www.cnblogs.com/lspz/p/6832358.html)
devtools的原理

> 深层原理是使用了两个ClassLoader，一个Classloader加载那些不会改变的类（第三方Jar包），另一个ClassLoader加载会更改的类，称为restart ClassLoader,这样在有代码更改的时候，原来的restart ClassLoader 被丢弃，重新创建一个restart ClassLoader，由于需要加载的类相比较少，所以实现了较快的重启时间
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

# JVM #
## GC ##
Garbage Collection
[Minor GC, Major GC, Full GC的区别](http://www.importnew.com/15820.html)
### Minor GC ###
从年轻代空间（包括 Eden 和 Survivor 区域）回收内存被称为 Minor GC

Eden 和 Survivor 区不存在内存碎片。写指针总是停留在所使用内存池的顶部

从永久代到年轻代的引用被当成 GC roots，从年轻代到永久代的引用在标记阶段被直接忽略掉
### Major GC VS FullGC ###
- Major GC 是清理老年代。
- Full GC 是清理整个堆空间—包括年轻代和老年代。

## Heap ##
### Eden ###
新来的对象要进入Eden，经过一次Minor GC，进入到 Survior
### Survivor ###
#### 没有Survivor的情况 ####
**增加老年代空间**

- 更多存活对象才能填满老年代。降低Full GC频率	
- 随着老年代空间加大，一旦发生Full GC，执行所需要的时间更长

**减少老年代空间**	

- Full GC所需时间减少	
- 老年代很快被存活对象填满，Full GC频率增加

#### 两个Survivor区 ####
Survivor的存在意义，就是减少被送到老年代的对象，进而减少Full GC的发生，Survivor的预筛选保证，只有经历16次Minor GC还能在新生代中存活的对象，才会被送到老年代

**设置两个Survivor区最大的好处就是解决了碎片化**

> 刚刚新建的对象在Eden中，一旦Eden满了，触发一次Minor GC，Eden中的存活对象就会被移动到Survivor区。这样继续循环下去，下一次Eden满了的时候，问题来了，此时进行Minor GC，Eden和Survivor各有一些存活对象，如果此时把Eden区的存活对象硬放到Survivor区，很明显这两部分对象所占有的内存是不连续的，也就导致了内存碎片化

> 应该建立两块Survivor区，刚刚新建的对象在Eden中，经历一次Minor GC，Eden中的存活对象就会被移动到第一块survivor space S0，Eden被清空；等Eden区再满了，就再触发一次Minor GC，Eden和S0中的存活对象又会被复制送入第二块survivor space S1

**如果对象的复制次数达到16次，该对象就会被送到老年代中**

> 永远有一个survivor space是空的，另一个非空的survivor space无碎片

### old ###
在年轻代中经历了N次垃圾回收后仍然存活的对象，就会被放到年老代中。
### 持久代 ###
用于存放静态文件，如今Java类、方法等

**JDK8中已经把持久代（PermGen Space） 干掉了，取而代之的元空间（Metaspace）。Metaspace占用的是本地内存，不再占用虚拟机内存。**
### 堆外内存 ###
底层访问，越过JVM（例如Socket）

适合分配次数少，读写操作很频繁的场景

因为DirectByteBuffer是通过虚引用(Phantom Reference)来实现堆外内存的释放

其实虚引用主要被用来 跟踪对象被垃圾回收的状态，通过查看引用队列中是否包含对象所对应的虚引用来判断它是否 即将被垃圾回收，从而采取行动

优点： 提升了IO效率（避免了数据从用户态向内核态的拷贝）；减少了GC次数（节约了大量的堆内内存）。 
　　
缺点：分配和回收堆外内存比分配和回收堆内存耗时；（解决方案：通过对象池避免频繁地创建和销毁堆外内存）

## JIT ##
[深入浅出JIT编译器](https://www.ibm.com/developerworks/cn/java/j-lo-just-in-time/)

当 JIT 编译启用时（默认是启用的），JVM 读入.class 文件解释后，将其发给 JIT 编译器。JIT 编译器将字节码编译成本机机器代码

如果一段代码频繁的调用方法，或是一个循环，也就是这段代码被多次执行，那么编译就非常值得

> Java 本身是一种半编译半解释执行的语言。Hot Spot VM 采用了 JIT compile 技术，将运行频率很高的字节码直接编译为机器指令执行以提高性能，所以当字节码被 JIT 编译为机器码的时候，要说它是编译执行的也可以

### 编译模式 ###
> JVM Server 模式与 client 模式启动，最主要的差别在于：-server 模式启动时，速度较慢，但是一旦运行起来后，性能将会有很大的提升。原因是：当虚拟机运行在-client 模式的时候，使用的是一个代号为 C1 的轻量级编译器，而-server 模式启动的虚拟机采用相对重量级代号为 C2 的编译器。C2 比 C1 编译器编译的相对彻底，服务起来之后，性能更高。

#### 优化代码缓存 ####
当 JVM 编译代码时，它会将汇编指令集保存在代码缓存。代码缓存具有固定的大小，并且一旦它被填满，JVM 则不能再编译更多的代码

 –XX:ReservedCodeCacheSize=Nflag（N 就是之前提到的默认大小）来最大化代码缓存大小

> 例如 Intel 系列机器，client 编译器模式下代码缓存大小起始于 160KB，server 编译器模式下代码缓存大小则起始于 2496KB

标准编译是被-XX:CompileThreshold=Nflag 的值所触发。Client 编译器模式下，N 默认的值 1500，而 Server 编译器模式下，N 默认的值则是 10000

如果 PrintCompilation 被启用，每次一个方法（或循环）被编译，JVM 都会打印出刚刚编译过的相关信息

# 测试 #
## AB测试 ##
将Web或App界面或流程的两个或多个版本，在同一时间维度，分别让两个或多个属性或组成成分相同（相似）的访客群组访问，收集各群组的用户体验数据和业务数据，最后分析评估出最好版本正式采用
### AB测试的基本步骤 ###
1.设定项目目标即AB测试的目标

2.设计优化的迭代开发方案，完成新模块的开发

3.确定实施的版本以及每个线上测试版本的分流比例

4.按照分流比例开放线上流量进行测试

5.收集实验数据进行有效性和效果判断

6.根据试验结果确定发布新版本、调整分流比例继续测试或者在试验效果未达成的情况下继续优化迭代方案重新开发上线试验

> AB测试强调的是同一时间维度对相似属性分组用户的测试，时间的统一性有效的规避了因为时间、季节等因素带来的影响，而属性的相似性则使得地域、性别、年龄等等其他因素对效果统计的影响降至最低

**A/B测试用于验证用户体验、市场推广等是否正确，而一般的工程测试主要验证软硬件是否符合设计预期，因此AB测试与一般的工程测试分属于不同的领域**
# 虚拟机 #
认识技术运作的本质，是自己思考“程序这样写好不好”的基础和前提

## Java内存区域 ##

### 常用虚拟机参数 ###
-XX:PermSize -XX:MaxPermSize 方法区大小

-Xms -Xmx 堆大小

-Xss 线程栈大小

-XX:MaxDirectMemorySize 直接内存容量，默认与Xmx指定的一样

-XX:+PrintGCDetails 记录 GC 运行时的详细数据信息，包括新生成对象的占用内存大小以及耗费时间等

-verbose:class以及-XX:+TraceClassLoading、-XX:+TraceClassUnLoading查看类加载和卸载信息

-XX:SurvivorRatio、-XX:PretenureSizeThreshold、-XX:HandlePromotionFailure Serial收集器可用的所有控制参数

-XX:+UseConcMarkSweepG 默认选择ParNew

-XX:+UserParNewGC 强制使用ParNew

-XX:ParrallelGThreads 限制垃圾收集的线程数

-XX:MaxGCPauseMillis 控制最大垃圾收集停顿时间

-XX:GCTimeRatio 直接设置吞吐量大小
### 运行时的数据区域 ###
Java虚拟机在执行Java程序的过程中会把它锁管理的内存划分成若干个不同的数据区域。有的区域随着虚拟机进程的启动而存在，有些区域则依赖用户线程的启动和结束而建立和销毁。

运行时数据区

- 方法区 Method Area
- 虚拟机栈 VM Stack
- 本地方法栈 Native Method Stack
- 堆 Heap
- 程序计数器 Program Counter Register

另外

- 执行引擎
- 本地库接口
- 本地方法库

#### 线程计数器 ####
Java虚拟机的多线程是通过线程轮流切换并分配处理器执行时间的方式来实现的。为了线程切换胡能恢复到正确的执行位置，每条线程都需要一个独立的程序计数器，各条线程之间计数器互不影响，独立存储，这类区域为“线程私有”的内存

如果线程正在执行一个Java方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址。如果正在执行的是Native方法，这个计数器值则是Undefined,此内存区域是唯一一个在Java虚拟机贵方中没有规定任何OutOfMemoryError情况的区域

#### Java虚拟机栈 ####
Java Virtual Machine Stacks

也是线程私有的，它的生命周期和线程相同，虚拟机栈描述的是Java方法执行的内存模型：每个方法在执行的同时都会创建一个栈帧(Stack Frame)用于存储局部变量表、操作数栈、动态数栈、方法出口等信息。每一个方法从调用直至执行完成的过程，就对应着一个栈帧在虚拟机总入栈到出栈的过程。-Xss

通常人们所指的栈内存就是指虚拟机栈，或者说是虚拟机栈中的局部变量表部分

线程请求的栈深度大于虚拟机所允许的深入，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展，如果扩展时无法申请到足够的内存，就会抛出OutOfMemoryError异常。

#### 本地方法栈 ####
Native Method Stack 

本地方法栈为虚拟机使用到的Native方法服务。有的虚拟机(譬如Sun HotSpot)就直接把本地方法栈和虚拟机栈合二为一。本地方法栈区域也会抛出 StackOverflowError和OutOfMemoryError异常。

#### Java堆 ####
Java 堆是一块被所有新城共享的内存区域。Java虚拟机规范的描述是：所有的对象实例以及数组都要在堆上分配。但是随着JIT编译器与逃逸分析技术逐渐成熟，栈上分配、标量替换优化技术将会导致一些微妙的变化发生。

所谓垃圾收集器的主要区域，很多时候被称为GC堆（Garbage Collected Heap）。由于现在收集器基本都采用分代收集算法，所以Java堆还可以细分为：新生代和老年代;再细致一点有Eden控件，From Survivor空间、To Survivor空间等。从内存分配的角度看，线程共享的Java堆中可能划分出多个线程私有的分配缓冲区（Thread Local Allocation Buffer， TLAB）。

Java堆可以处于物理上不连续的内存空间，只要逻辑上是连续的即可。主流虚拟机的堆都是可扩展的（-Xms和-Xms）如果堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出OutOfMemoryError异常。

#### 方法区 ####
Method Area

和Java堆一样，是个各个线程共享的内存区域，用于存储已被虚拟机加载的类信息、常量、静态变量、JIT编译器编译后的代码等数据。虽然Java虚拟机把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫Non-Heap（非堆）

在HotSpot上，人们更愿意把方法区成为“永久代”（PermanentGeneration），本质上两者并不等价，仅仅因为HotSpot设计团队把GC分代收集扩展至方法区。省去专门为方法区编写内存管理代码。

#### 运行时常量池 ####
Runtime Constant Pool

运行时常量池是方法区的一部分，Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池（Constant Pool Table），用于存放编译器生成的各种字面量和符号引用，这部分内容将在类加载后进入方法区的运行时常量池中存放。

Java并不要求常量一定在编译器才能产生，并非与置入Class文件中常量池的内容才能进入方法区运行时常量池，运行期间也可以将新的常量放入池中。比如String类的intern()方法

#### 直接内存 ####
Direct Memory

JDK中加入NIO，它可以使用Native函数库直接分配堆外内存，然后通过一个存储在Java堆中的DirectByteBuffer对象作为这块内存的引用进行操作，这样鞥你在一些场景中提高性能，因为避免了在Java堆和Native堆中来回复制数据。

虽然直接内存的分配不受Java堆大小的限制，但是仍受本地总内存（RAM以及SWAP区或分页文件）大小已经处理器寻址控件的限制，仍然可能抛出OutOfMemoryError异常。

### HotSpot虚拟机对象 ###
#### 对象的创建 ####
虚拟机遇到一条New指令时，首先去检查这个指令的参数能否在常量池中定位到一个类的符号引用，并且检查这个符号引用代表的类是否已被加载、解析和初始化过。如果没有，那必须先执行相应的类加载过程。

在类加载检查通过后，接下来虚拟机降为新生对象分配内存。内存分配完成后，虚拟机将分配到的内存空间都初始化为零值。接下来，虚拟机对对象进行必要的设置，例如对象是哪个类的实例、如何才能找到类的元数据信息、对象的哈希码、对象的GC分代年龄等信息。这些信息存放在对象的对象头中（Object Header）。

从虚拟机的角度来说，一个新的对象就这样产生了。但从Java层序的视角来看，对象的创建才刚刚开始<init>方法还没有执行，所有的字段都还为零。一般来说，由invokespecial指令决定，执行一个new指令之后接着执行<init>方法，把对象按照程序员的意愿进行初始化，这样一个真正可用的对象才算完全产生出来。

#### 对象的内存布局 ####
在HotSpot虚拟机中，对象在内存中的存储布局分为三个区域：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。

**对象头包括两部分信息：**

第一个部分用于存储对象自身的运行时数据，如哈希码（HashCode）、GC分代年龄
锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等，这部分数据被称为“Mark Word”。考虑到虚拟机的控件效率，Mark Word被设计成一个非固定的数据结构，它会根据对象的状态来复用自己的存储空间。

对象头的另一部分是类型指针，即对此昂指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。

**实例数据部分**是对象真正存储的有效信息，也是程序代码所定义的各种类型的字段内容。无论是父类继承袭来的，还是在子类中定义的都需要记录下俩。这部分的存储顺序会受虚拟机分配策略参数（FieldsAllocationStyle）和字段在Java源码中定义顺序的影响。相同的宽度的字段总是被分配在一起，父类中定义的变量会出现在子类之前。如果CompactFields为true（默认），那么子类中较窄的变量也可能会插入到父类变量的空隙之中。

**对齐填充**并不是必然存在的，仅仅是满足HotSpotVM自动内存管理系统要求对象起始地址必须是8字节的整数倍。

#### 对象的访问定位 ####
主流的访问方式有使用句柄和直接指针。

**句柄访问** Java堆中会划出一块内存来作为句柄池，reference中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自的具体地址信息。

**直接指针访问** Java堆对象的布局必须考虑访问数据类型的相关信息，而reference中存储的直接就是对象地址。

句柄的好处就是reference中存储的是稳定的句柄地址，对象呗移动只会改变句柄中实例数据指针。reference不需要修改。直接指针访问方式的最大好处是速度更快。HotSpot使用直接指针， 对于整个软件开发来说，句柄访问的情况更加常见。


#### 虚拟机栈和本地方法栈溢出 ####

- 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常
- 如果虚拟机在扩展栈时无法申请到足够的内存空间，将抛出OutOfMemoryError异常

如果建立多线程过程导致内存溢出，在不能减少线程数或者更换64位虚拟机的情况下，就只能通过减少最大堆和减少栈容量来换取更多的线程，这是通过“减少内存”的手段来解决内存溢出的方式。

#### 方法区和运行时常量池 ####
String.intern是一个Native方法，将String对象添加到常量池中，并返回String对象的引用，通过-XX:PermSize和-XX:MaxPermSize限制方法区大小，从而间接限制其中常量池的容量。

> JDK 1.7开始逐步去除永久代，不会有影响

#### 直接内存溢出 ####
如果发现OOM之后Dump文件很小，而程序中又直接或间接使用了NIO，就要检查是否是DirectMemory导致的内存溢出。

## 垃圾收集器与内存分配策略 ##
程序计数器、虚拟机栈、本地方法栈3个区域随线程而生，随线程而灭；线程中的栈帧随着方法的进入和退出而有条不紊地执行着出栈和入栈操作。每一个栈帧中分配多少内存在类结构确定下来的时候就已知的。这几个区域的内存分配和回收都具备确定性，这几个区域不需要过多考虑回收的问题，因为方法或线程结束，内存自然就随着回收。Java堆和方法区则不一样，一个接口的多个类实现需要的内存可能不一样，一个方法的多个分支需要的内存也可能不一样，只有程序处于运行期间才可能知道会创建哪些对象，这部分的分配和回收都是动态的，垃圾收集器所关注的是这部分内存。

### 对象已死吗 ###
垃圾收集器在对堆进行回收前，第一件事情就是要确定这些对象是否还“存活”

#### 引用计数 ####
难以解决循环引用的问题

#### 可达性分析算法 ####
Reachability Analysis

通过一系列成为“GC Roots”的对象为起始点，从这些节点开始向下搜索，搜索所走过的路径成为引用链（Reference Chain）,当一个对象到GC Roots没有任何引用链相连的时候，则证明此对象是不可用的。

可作为GCRoots的对象包括

- 虚拟机栈中引用的对象
- 方法区类中静态属性引用的对象
- 方法区中常量引用的对象
- 本地方法栈中JNI引用的对象

#### 引用 ####

- 强引用 Strong Reference
- 软引用 Soft Reference
- 弱引用 Weak Reference
- 虚引用 Phantom Reference

#### 生存还是死亡 ####
要真正宣告一个对象死亡，至少要经历两次标记过程：如果对象在进行可达性分析后并没有发现与GC Roots相连接的引用链，那么它将被第一次标记并进行一次筛选，筛选的条件是此对象有必要执行finalize方法。如果对象没有finalize()方法，或者finalize方法已经被虚拟机调用过，虚拟机将这两种情况都视为“没有必要执行”。

如果对象呗判定有必要执行finalize方法，那么这个对象将被被放置在一个叫F-Queue的队列中，并在稍后由一个虚拟机自动建立、低优先级的Finalizer线程去执行它。这里所谓的“执行”是指虚拟机自动建立、低优先级的Finalizer线程去执行它。如果一个对象在finalize方法中执行缓慢或者发生了死循环，很可能会导致F-Queue队列中其他对象永久处于等待， 甚至导致整个内存回收系统崩溃。finalize方法使对象逃脱死亡命运的最后一次机会，稍后GC将对F-Queue中的对象进行第二次小规模的标记。如果对象要在finalize中成功拯救自己，只要重新与引用链上任何一个对象建立联系即可

任何一个对象的finalize方法只会被系统自动调用一次，如果对象面临下一次回收，它的finalize方法不会被再次执行。

#### 回收方法区 ####
永久代的垃圾回收主要回收两部分内容：废弃常量和无用的类

无用的类：

- 该类的所有实例都已经被回收。
- 加载该类的ClassLoader已经被回收。
- 该类对应的java.lang.Class对象没有任何地方被引用，无法通过反射访问该类的方法

> 大量使用反射、动态代理、CGLib等Bytecode框架、动态生成JSP以及OSGi这类频繁自定义ClassLoader的场景都需要虚拟机具备类卸载功能，以保证永久代不会溢出

### 垃圾回收算法 ###
#### 标记-清楚算法 ####
首先标记所有需要回收的对象，在标记完成后统一回收所有被标记的对象。

后续的算法都是基于这种思路，并对其不足之处进行改进而得到的

不足之处：

1. 效率问题，标记和清楚两个过程的效率不高；
2. 空间问题，标记清楚之后会产生大量不连续的碎片。碎片太多会导致需要较大对象时，无法赵安东足够连续内存而不得不提前触发一次垃圾收集动作。

#### 复制算法 ####
将内存分为大小相等的两块，每次只使用其中一块。当一块内存用完，就讲还活着的对象复制到另一块上面，然后把已经使用过的内存空间一次性清空。 **这种算法的代价是将内存缩小为原来的一半。**

新生代对象98%是“朝生夕死”，所以不需要按照1:1的比例饿，而是将内存分为一块较大的Eden空间和两块较小的Survivor空间，每次使用Eden和其中一块Survivor，将Eden和Survivor中还存活的对象一次性地复制到另一个块Survivor空间上，最后清理Eden和刚才用过的Survivor空间。HotSpot虚拟机默认Eden和Survivor大小比例为8:1，也就是每次新生代中可用内存空间为整个新生代容量的90%，只有10%的内存会被浪费，当Survivor空间不够用时，需要依赖其他内存（老生代）

#### 标记-整理算法 ####
复制手机算法在对象存活率较高时既要进行较多的复制操作，效率将会变低。更加关键的是如果不想浪费50%的控件就需要有额外的空间进行分配担保。以应对被使用的内存中所有对象100%存活的极端情况，所以在老年代中一般不能直接选用这种算法。

不是直接对可回收对象进行清理，而是让所有存活的对象都向一端移动，然后直接清理掉端边界以外的内存。

#### 分代收集算法 ####
根据对象存活的周期将不同的内存划分成几块，一般是把java对象堆分为新生代和老生代。新生代中每次收集都发现大批对象死去，就选用复制算法。老生代中因为对象存活率高，没有额外的控件对它进行分配担保，就使用“标记清理”或者“标记整理”算法来进行回收

### HotSpot的算法实现 ###
#### 枚举根节点 ####
可达性分析对时间的敏感，导致进行GC时必须停顿所有Java执行线程（“Stop-The-World”）的其中一个重要原因，即使在号称不会发生停顿的CMS收集器中，枚举根节点时也必须进行停顿。

准确式内存管理（Exact Memory Management，也叫Non-Conservative/Accurate Memory Management），即虚拟机可以知道内存某个位置的数据具体是什么类型

在HotSpot的实现中，是使用一组成为OopMap的数据结构来达到这个目的，在类加载完成的时候，HotSpot就把对象内什么偏移量上是什么类型的数据计算出来，在JIT编译过程汇总，也会在特定的位置记录下栈和寄存器中哪些位置是引用。这样GC在扫描时就可以直接得知这些信息了。

#### 安全点 ####
只有在特定的位置记录了这些信息，这些位置被称为安全点（Safepoint），即程序执行时并非所有地方都能停顿下来开始GC，只有到达安全点时才能暂停。“长时间执行”是指令序复用，比如方法调用、循环跳转、异常转移等。所以具有这些功能的指令才会产生Safepoint。

如何让所有的线程都跑到最近的安全点再停下来，有两种方案可供选择：抢先式中断（Preemptive Suspension）和主动式中断（Voluntary Suspension），其中抢先式中断不需要要线程执行代码主动去配合，当GC发生时，首先把所有线程全部中断，如果发现有线程中断的地点不在安全带你上，就恢复线程，让它跑到安全点上。机会没有虚拟机采用抢先式中断来暂停线程从而响应GC事件。

主动式中断的思想是当GC需要中断线程的时候，不直接对线程操作，仅仅简单地设置一个标志，各个线程去主动轮询这个标志，发现中断标志位真时就自己中断挂起，轮询标志的地方和安全点是重合的，另外再加上创建对象需要分配内存的地方。

#### 安全区域 ####
Safe Region

当程序“不执行”，例如线程处于Sleep状态或者Blocked状态，这时候线程无法响应JVM的中断请求，“走”到安全的地方去中断挂起，JVM显然也不太可能等待线程重新被分配CPU时间。

安全区域是指一端代码之中，引用关系不会发生变化，在这个区域中的任意地方开始GC都是安全的，Safe Region可以看做是被扩展的Safepoint。

当线程执行到Safe Region中的代码时，首先表示自己已经进入了Safe Region，那样，当这段时间里JVM要发起GC时，就不用管识别自己为Safe Region状态的线程了。当线程离开Safe Region时，它要检查系统是否已经完成根节点枚举，如果完成，那线程就继续执行，否则，它就必须等待知道收到可以来开Safe Region的信号为止。

### 垃圾回收器 ###
#### Serial收集器 ####
进行垃圾手机时，必需暂停其他所有的工作线程，知道它手机结束。

依然是运行在Client模式下的默认新生代收集器，简单而高效

#### ParNew手机器 ####
就是Serial收集器的多线程版本，是许多运行在Server模式下的虚拟机的首选，除了Serial收集器之外，只有它能与CMS收集器配合工作。

#### Parallel Scavenge收集器 ####
它也是使用复制算法的收集器，又是并行的多线程收集器。

Parrallel Scavenge收集器的目的则是达到一个可控制的吞吐量（Throughput）。

吞吐量= 用户运行代码时间/(运行用户代码时间+垃圾收集时间)

停顿时间越短越适合与用户交互的程序，良好的响应速度能提升用户体验，而高吞吐量则可以高效率地利用CPU时间，尽快完成程序的运算任务，主要适合在后台运算而不要太多交互的任务。

Parrel Scavenge收集器提供两个参数控制吞吐量。

MaxGCPauseMillis 允许一个大于0的毫秒数，GC停顿时间短是以牺牲吞吐量和新生代空间换取的。把新生代调小会导致垃圾手机发生得更频繁。导致吞吐量下降。

GCTimeRatio 参数是一个大于0且小于100的整数。也就是垃圾手机时间的占比，相当于吞吐量的倒数。**吞吐量优先**收集器

+UserAdaptiveSizePolicy 打开这个参数，就不需要手工指定新生代的大小（-Xmn）、Eden与Survivor区的比例（-XX:SurvivorRatio）、晋升老年代对象年龄(-XX:PretenureSizeThreshold)等细节参数了，虚拟机会根据当前系统的运行情况手机性能检控信息，动态调整这些参数以提供最合适的停顿时间和最大的吞吐量。这种调节方式成为GC自适应调解策略（GC Ergonomics），自适应调解策略也是与ParNew收集器的一个重要区别

#### Serial Old ####
一个单线程收集器，手机老年代的版本

#### Parrallel Old ####
Parallel Scavenge收集器的老年代版本，使用多线程和“标记-整理”算法。Parallel Scavenge无法与CMS收集器配合工作

在注重吞吐量以及CPU资源敏感的场合，可以优先考虑Parallel Scavenge加Parallel Old收集器

#### CMS收集器 ####
Concurrent Mark Sweep

是一种以获取最短停顿时间为目标的收集器。

四个步骤

- 初始标记 CMS initial mark
- 并发标记 CMS concurrent mark
- 重新标记 CMS remark
- 并发清除 CMS concurrent sweep

初始标记、重新标记这两部仍然需要“Stop The World”。初始标记只是仅仅标记一下GC Roots能直接关联到的对象，速度很快，并发标记就是进行GC Roots Tracing的过程。而重新标记阶段则是为了修正并发标记期间因为用户程序继续运作而导致标记变动的那一部分兑现高的标记记录。这个阶段的停顿时间一般会比初始标记阶段稍长一些，但远比并发标记的事件短。

整个过程中耗时最长的并发标记和并发清楚过程收集器线程都可以与用户线程一起工作。总体而言，CMS收集器的内存回收过程是与用户线程并发执行的。

**明显缺点**

- 对CPU资源非常敏感
- 无法处理浮动垃圾（Floating Garbage）
- 基于“标记-清楚”，大量的控件碎片

-XX:CMSInitiatingOccupancyFraction 触发老年代的回收

要是CMS运行期间预留的内存无法满足程序需要，就会出现一次“Concurrent Mode Failure”失败，这时虚拟机会启动后备员：临时启用Serial Old收集器来重新进行老年代的垃圾回收，这样的停顿时间就很长了。-XX：CMSInitiatingOccupancyFraction设置得太高容易导致大量“Concurrent Mode Failure”失败，性能反而降低

CMS收集器提供一个 -XX：+UseCMSCompactAtFullCollection开关，默认是开启的，在顶不住要进行FullGC的时候开启内存碎片的合并整理过程，内存整理的过程是无法并发的，空间碎片问题没有了，但停顿时间不得不变长， -XX:CMSFullGCsBeforeCompaction，执行了多少次不压缩的FullGC后，跟着来一次带压缩的（默认值0）

#### G1收集器 ####
Garba First

特点：

- 并行与并发 ： 充分利用多CPU
- 多核环境的硬件优势来缩短Stop-The-World停顿。
- 分代收集：不需要其他收集器配合就能管理整个GC堆。采用不同的方式去处理新创建和已经存活了一段时间、熬过多次GC的旧对象
- 空间整合：基于“标记-整理”，从局部开始看是基于复制，不会产生内存空间碎片
- 可预测的停顿：G1除了追求低停顿外、还能建立可预测的停顿时间模型，能让一个指定长度为M毫秒的时间片段内，消耗在垃圾收集时间上不超过N毫秒，这机会已经是实时Java（RTSJ）的收集器特征了。

建立可预测的停顿模型：因为它可以有计划地避免在整个Java堆中国进行全区域的垃圾收集。G1跟踪Region里面的垃圾堆积的价值大小（回收所获得的空间大小以及回收所需时间的经验值）维护一个优先列表，没根据允许的手机时间，**优先回收价值最大的Region**。

这种使用Region划分内存空间以及优先级的区域回收方式，保证G1收集器在优先的时间内可以获取尽可能的收集效率。

G1收集器中，Region之间对象引用以及其他收集器中的新生代和老年代之间的对象引用，虚拟机都是使用RememberSet来避免全局扫描的。G1中每个Region都有一个预制对应的Remembered Set，虚拟机发现程序在对Reference类型的诗句进行操作时，会产生一个Write Barrier暂停中断写操作，检查Reference引用的对象是否处于不同的Region之中，如果是，便通过CardTable把相关引用信息记录到被引用对象所属的Region的RememberedSet之中。当进行内存回收时，在GC根节点的枚举范围中加入RememberedSet即可保证不对全局扫描也不会有遗漏。

如果不计算维护Remembered Set操作，G1的操作步骤：

- 初始标记 Initial Marking
- 并发标记 Concurrent Marking
- 最终标记 Final Marking
- 筛选回收 Live Data Counting and Evacuation

最终标记极端为了修正并发标记期间因为用户程序继续运作而导致标记产生变动的那一部分标记记录，虚拟机将这段时间对象变化记录在线程Remembered Set Logs里面，并在最终标记阶段需要把Remembered Set Logs的数据合并到Remembered Set中。这阶段需要挺短线程，但是可并发执行。筛选回收阶段停顿用户线程将大幅提高收集效率。

### 内存分配与回收策略 ###
#### 对象优先在Eden分配 ####
当Eden区没有足够空间的时候，虚拟机将发起一次MinorGC

MinorGC时，选择在新生代腾出足够的空间给新的分配请求
#### 大对象直接进入老年代 ####
需要大量连续空间的Java对象，最典型的大对象就是那种很长的字符串以及数组。（避免写“短命大对象”）

-XX：PretenureSizeThreshold 大于这个值的对象直接在老生代分配，避免在Eden区及两个Survivor区发生大量的内存复制。PS收集器不认识

#### 长期存活的对象将进入老年代 ####
-XX:MaxTenuringThreshold 进入老年代的年龄阈值

#### 动态对象年龄判定 ####
虚拟机并不是永远要求对象必须到达MaxTenuringThreshold才能晋级老年代，如果在Survivor空间中相同年龄所有对象大小大于Survivor空间的一般，年龄大于或等于改年龄的对象可以直接进入老年代。
#### 空间担保分配 ####
在发生MinorGC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代说有空间对象总空间，如果条件成立，那么MinorGC可以确保是安全的，如果不成立，则虚拟机可以查看HandlePromotionFailure设置值是否允许担保失败。如果允许，那么可以继续检查老年代最大可用的连续空间是否大于历次晋级到老年代对象的凭据大小，如果大于，将尝试进行一次MinorGC，尽管这次MinorGC是有风险的；如果小于，或者HandlePromotionFailure设置不允许冒险，那么这时要改为进行一次Full GC。

## 虚拟机性能监控与故障处理工具 ##
### JDK命令行工具 ###
- jps JVM process Status Tool,显示指定系统内所有的HotSpot虚拟机进程
- jstat JVM Statics Monitoring Too，用于手机HotSpot虚拟机各个方面的运行数据
- jinfo Configuration Info Java 显示虚拟机配置信息
- jmap Memory Map for Java， 生成虚拟机的内存转储快照
- jhat JVM Heap Dump Browser，用于分析heapdump文件，它会建立一个HTTP/HTML服务器，让用户可以在浏览器上分析结果
- jstack Stack Trace for Java 显示虚拟机的线程快照
- hidis jit生成代码反汇编

代码调试在Java虚拟机中会遇到很大的困难，因为大量执行代码是通过JIT编译器动态生成到CodeBuffer中的，没有很简单的手段来处理这种混合模式的调试

HSDIS的作用是让HotSpot的 -XX:+PrintAssembly指令调用它来把动态生成的本地代码还原为汇编代码输出，同时还生成大量非常有价值的注视，这样我们就可以通过输出的代码来分析问题。如果使用Product版的HotSpot，那还要额外加入一个-XX:+UnlockDiagnosticVMOptions参数

java -XX:+PrintAssembly -Xcomp -XX:CompileCommand=dontinline,*Bar.sum -XX:CompileCommand=compileonly,*Bar.sum test.Bar

-XComp是让虚拟机以编译模式执行这段代码，这样代码可以“偷懒”,不需要执行足够次数来预热就能触发JIT编译。两个-XX:CompileCommand意思是让编译器不要内联并且只编译sum， -XX:PrintAssembly就是输出反汇编内容。

Integer.valueOf()方法基于减少对象创建次数和节省内存的考虑，[-128,127]之间的数字会被缓存，当valueOf方法传入参数在这个范围之中，将直接返回缓存的对象。可能会在引用中造成死锁。

### JDK可视化工具 ###
#### JConsole ####
Java监视与管理控制台，是一种基于JMX的可视化监视、管理工具。它管理部分的功能是针对JMX MBean进行管理。
#### VisualVM ####
配置中心迁移至https://visualvm.github.io/index.html 

https://visualvm.github.io/archive/uc/8u40/updates.xml.gz

**BTrace动态日志跟踪**

它的作用在不停止程序运行的前提下，通过HotSpot虚拟机的HotSwap技术动态加入原本不存在的调试代码。

## 案例分析 ##
### 高性能硬件上的程序部署方案 ###
在高性能硬件上部署程序，主要有两种方式：

- 通过64位JDK来使用大内存。
- 使用若干个32位虚拟机建立逻辑集群来利用硬件资源

对于用户交互性强、对停顿时间敏感的系统，可以给Java虚拟机分配超大堆的前提是有把握把应用程序的Full GC频率控制得足够低，至少要低到不会影响用户使用，譬如十几小时乃至一天才出现一次FullGC，这样可以通过深夜执行定时任务的方式触发Full GC甚至重启应用服务器来保持内存可用空间在一个稳定的水平。

控制Full GC频率的关键是看应用中绝大多数对象能否符合“朝生夕灭”的原则，即大多数对象的生存时间不应太长，尤其不能有成批量的、长时间生存时间的大对象产生，这样才能保障老年代空间的稳定。

#### 使用64位JDK管理超大堆内存需要面临的问题 ####
- 内存回收导致的长时间停顿
- 64位JDK的性能测试普遍低于32位JDK
- 需要保证程序足够稳定，因为这种应用要是产生堆溢出几乎无法产生堆转储快照，哪怕产生了也几乎无法进行分析。
- 相同程序64位JDK消耗的内存一般比32位JDK大，这是由于指针膨胀，以及数据类型对齐补白等因素导致的

#### 使用若干32位虚拟机建立逻辑集群来利用硬盘资源 ####
在一台物理机器上启动多个应用服务进程，每个服务器进程分配不同端口，然后再前端搭建一个负载均衡器，以反向代理的方式来分配访问请求。

**缺点**

- 尽量避免节点竞争全局资源，典型的是磁盘竞争
- 很难高效利用某些资源池，譬如连接池
- 各个节点仍然不可避免受32位的内存限制
- 大量使用本地缓存的应用中，在逻辑集群中会造成比较大的内存浪费，因为每个逻辑点点上都有一份缓存，可考虑集中式缓存

**文档服务的主要压力集中在磁盘和内存访问，CPU资源敏感度较低，因而改为CMS收集器进行垃圾回收**

### 集群间同步导致的内存溢出 ###
JBOSSCache

### 堆外内存导致的溢出错误 ###
虚拟机虽然会对Direct Memory进行回收，但是Direct Memory却不能像新生代、老年代那样，发现控件不足就通知收集器进行垃圾回收，它只能等待老年代满后Full GC，然后“顺便”帮它清理掉内存的废弃对象。否则只能一直等到抛出内存溢出异常，先catch掉，再在catch块里“大喊”一声：“System.gc()”,要是虚拟机还是不听(譬如打开了-XX:+DisableExplicitGC开关)，那就只能眼睁睁地看着堆中还有许多空闲内存，自己却不得不抛出内存溢出异常。

大量NIO操作的应用和框架需要使用Direct Memory内存

除了java堆和永久代之外，会占用较多内存的区域还有

- Direct Memory: 可通过-XX:MaxDirectMemorySize调整大小，内存不足时抛出OutOfMemoryError或者OutOfMemoryError:Direct buffer memory 
- Socket缓存区：每个Socket链接都需要Receiver和Send两个缓存区，分别占大约37KB和25KB内存，连接多的话这块内存占用也比较客观。如果无法分配，可能会抛出IOException:Too many open files异常
- JNI代码:如果代码中使用JNI调用本地库，那本地库使用的内存也不在堆中。
- 虚拟机和GC：虚拟机、GC的代码执行也要消耗一定的内存

### 外部命令导致系统缓慢 ###
每个用户请求的处理都需要执行一个外部shell脚本来获得系统的一些信息。执行这个shell脚本是通过Java的Runtime.getRuntime().exec()方法来调用的。虚拟机执行这个命令的过程是：首先克隆一个和当前虚拟机拥有一样环境变量的进程，再去调用这个新进程去执行外部命令，然后再退出。因此产生了大量的fork系统调用。

去掉这个Shell脚本的执行，改用Java的API去获取这些信息

### 服务器JVM进程崩溃 ###
由于使用了大量异步调用Web服务，并且两遍服务速度不对等，事件越长就累积了越多Web服务没有调用完成，导致在等待的线程和Socket链接越来越多，最终在超过虚拟机的承受能力后使得虚拟机进程崩溃。解决办法：将一部调用改为生产者/消费者模式的消息队列实现后，系统恢复正常。

### 不恰当数据结构导致内存占用过大 ###

# 汇编指令 #
## 8086寄存器 ##
- AH&AL=AX(accumulator)：累加寄存器，常用于运算;在乘除等指令中指定用来存放操作数，另外,所有的I/O指令都使用这一寄存器与外界设备传送数据。
- BH&BL=BX(base)：基址寄存器，常用于地址索引
- CH&CL=CX(count)：计数寄存器，常用于计数；常用于保存计算值，如在移位指令,循环(loop)和串处理指令中用作隐含的计数器.
- DH&DL=DX(data)：数据寄存器，常用于数据传递。
- SP（Stack Pointer）：堆栈指针，与SS配合使用，可指向目前的堆栈位置
- BP（Base Pointer）：基址指针寄存器，可用作
- SS的一个相对基址位置
- SI（Source Index）：源变址寄存器，可用来存放相对于DS段之源变址指针
- DI（Destination Index）：目的变址寄存器，可用来存放相对于ES 段之目的变址指针
- 指令指针IP是一个16位专用寄存器，它指向当前需要取出的指令字节，当BIU从内存中取出一个指令字节后，IP就自动加
- 标志性寄存器FR，在FR中有意义的有9位，其中6位是状态位，3位是控制位。标志寄存器（Flags Register,FR）又称程序状态字(Program Status Word,PSW)
- CS（Code Segment）：代码段寄存器
- DS（Data Segment）：数据段寄存器
- SS（Stack Segment）：堆栈段寄存器
- ES（Extra Segment）：附加段寄存器。

## x86-64的寄存器 ##
### 64位通用寄存器（整数寄存器） ###
R8、R9、R10、R11、R12、R13、R14、R15
 可作为8位（R8B~ R15B）、16位（R8W~ R15W）或 32位寄存器（R8D~R15D）使用

所有GPRs都从32位扩充到64位

-  ①8个32位通用寄存器 EAX、EBX、ECX、EDX、EBP、 ESP、ESI、EDI
-   对应扩展寄存器分别为 RAX、RBX、 RCX、RDX、RBP、RSP、RSI、RDI
-  ② 新增了EBP、ESP、ESI和EDI 的低8位寄存器：BPL、SPL 、SIL和DIL
-  ③可兼容使用原AH、BH、CH和DH寄存器 （使原来IA-32中的每个通用寄存器都可以是8位、16位、 32位和64位，如：SIL、SI、ESI、RSI）

## 常用指令 ##
POP 和PUSH通常可以用来交换两个寄存器的值，也可以用来保护寄存器的值

交换ax与cx的值：push ax；push cx；pop ax； pop cx

保护寄存器：push ax；push cx；….中间有很多执行的代码…pop cx;pop ax;

