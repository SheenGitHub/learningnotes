# Lang #
## 关键字 ##
### strictfp ###
strictfp 关键字可应用于类、接口或方法。使用 strictfp 关键字声明一个方法时，该方法中所有的float和double表达式都严格遵守FP-strict的限制,符合IEEE-754规范。当对一个类或接口使用 strictfp 关键字时，该类中的所有代码，包括嵌套类型中的初始设定值和代码，都将严格地进行计算。严格约束意味着所有表达式的结果都必须是 IEEE 754 算法对操作数预期的结果，以单精度和双精度格式表示。
　　如果你想让你的浮点运算更加精确，而且不会因为不同的硬件平台所执行的结果不一致的话，可以用关键字strictfp
## 泛型 ##
### 擦除 ###

> 为什么我们往ArrayList<StringarrayList=new ArrayList<String>();所创建的数组列表arrayList中，不能使用add方法添加整形呢？不是说泛型变量Integer会在编译时候擦除变为原始类型Object吗，为什么不能存别的类型呢？既然类型擦除了，如何保证我们只能使用泛型变量限定的类型呢？
> 
> java是如何解决这个问题的呢？java编译器是通过先检查代码中泛型的类型，然后再进行类型擦除，在进行编译的。
指定泛型方法

	Test2.<Integer>add(1, 2)

### 协变与逆变 ###
逆变与协变用来描述类型转换（type transformation）后的继承关系，其定义：如果A、B表示类型，f(⋅)表示类型转换，≤表示继承关系（比如，A≤B表示A是由B派生出来的子类）

- f(⋅)是逆变（contravariant）的，当A≤B时有f(B)≤f(A)成立；
- f(⋅)是协变（covariant）的，当A≤B时有f(A)≤f(B)成立；
- f(⋅)是不变（invariant）的，当A≤B时上述两个式子均不成立，即f(A)与f(B)相互之间没有继承关系。

#### PECS ####
producer-extends, consumer-super（PECS）

从数据流来看，extends是限制数据来源的（生产者），而super是限制数据流入的（消费者）。

#### 自限定的类型 ####
	class SelfBounded<T extends SelfBounded<T>> {

 自限定类型的本质就是：基类用子类代替其参数。这意味着泛型基类变成了一种其所有子类的公共功能模版，但是在所产生的类中将使用确切类型而不是基类型。因此，Subtype中，传递给set()的参数和从get() 返回的类型都确切是Subtype。

#### 自限定与协变 ####
自限定类型的价值在于它们可以产生协变参数类型——方法参数类型会随子类而变化。其实自限定还可以产生协变返回类型，但是这并不重要，**因为JDK1.5引入了协变返回类型**

在非泛型代码中，参数类型不能随子类型发生变化。方法只能重载不能重写

	interface SelfBoundSetter<T extends SelfBoundSetter<T>> {
	    void set(T args);
	}
	
	interface Setter extends SelfBoundSetter<Setter> {}
	
	public class SelfBoundAndCovariantArguments {
	    void testA(Setter s1, Setter s2, SelfBoundSetter sbs) {
	        s1.set(s2);
	        s1.set(sbs);  // 编译错误
	    }
	}

#### 捕获转换 ####
捕获转换只有在这样的情况下可以工作：即在方法内部，你需要使用确切的类型。
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
复制收集算法在对象存活率较高时既要进行较多的复制操作，效率将会变低。更加关键的是如果不想浪费50%的控件就需要有额外的空间进行分配担保。以应对被使用的内存中所有对象100%存活的极端情况，所以在老年代中一般不能直接选用这种算法。

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
进行垃圾收集时，必需暂停其他所有的工作线程，知道它收集结束。

依然是运行在Client模式下的默认新生代收集器，简单而高效

#### ParNew收集器 ####
就是Serial收集器的多线程版本，是许多运行在Server模式下的虚拟机的首选，除了Serial收集器之外，只有它能与CMS收集器配合工作。

#### Parallel Scavenge收集器 ####
它也是使用复制算法的收集器，又是并行的多线程收集器。

Parrallel Scavenge收集器的目的则是达到一个可控制的吞吐量（Throughput）。

吞吐量= 用户运行代码时间/(运行用户代码时间+垃圾收集时间)

停顿时间越短越适合与用户交互的程序，良好的响应速度能提升用户体验，而高吞吐量则可以高效率地利用CPU时间，尽快完成程序的运算任务，主要适合在后台运算而不要太多交互的任务。

Parrel Scavenge收集器提供两个参数控制吞吐量。

MaxGCPauseMillis 允许一个大于0的毫秒数，GC停顿时间短是以牺牲吞吐量和新生代空间换取的。把新生代调小会导致垃圾收集发生得更频繁。导致吞吐量下降。

GCTimeRatio 参数是一个大于0且小于100的整数。也就是垃圾收集时间的占比，相当于吞吐量的倒数。**吞吐量优先**收集器

+UserAdaptiveSizePolicy 打开这个参数，就不需要手工指定新生代的大小（-Xmn）、Eden与Survivor区的比例（-XX:SurvivorRatio）、晋升老年代对象年龄(-XX:PretenureSizeThreshold)等细节参数了，虚拟机会根据当前系统的运行情况收集性能检控信息，动态调整这些参数以提供最合适的停顿时间和最大的吞吐量。这种调节方式成为GC自适应调解策略（GC Ergonomics），自适应调解策略也是与ParNew收集器的一个重要区别

#### Serial Old ####
一个单线程收集器，收集老年代的版本

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

建立可预测的停顿模型：因为它可以有计划地避免在整个Java堆中国进行全区域的垃圾收集。G1跟踪Region里面的垃圾堆积的价值大小（回收所获得的空间大小以及回收所需时间的经验值）维护一个优先列表，没根据允许的收集时间，**优先回收价值最大的Region**。

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
- jstat JVM Statics Monitoring Too，用于收集HotSpot虚拟机各个方面的运行数据
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
加载约80MB的数据文件到内存进行数据分析，这些数据会在内存形成100万个HashMap<Long, Long>Entry, 800M的Eden控件很快被填满从而引发GC，但Minor GC之后，新生代中绝大部分对象依然是存活的。ParNew收集器使用的是复制算法，把这些对象引用复制到Survivor并维持这些对象引用的正确就成为一个沉重的负担，因而导致GC暂停时间明显变长。 如果不修改程序，仅从GC调优的角度去解决这个问题，可以考虑将Survivor控件去掉(加入参数-XX:SurvivorRatio=65536、-XX:MaxTenuringThreshold=0或者-XX:+AlwaysTenure),让新生代中存活的对象在第一次MinorGC后立刻进入老年代，等到Major GC的时候再清理它们。

这里的根本问题是用HashMap<Long,Long>结构来存储数据文件控件效率太低。

HashMap<Long,Long>结构中，只有Key和Value所存放的两个长整型数据是有效数据,共16B(2*8B)。这两个长整型数据包装成java.lang.Long对象之后，就分别具有8B的MarkWord、8B的Klass指针，在加8B存储数据的long值。在这两个对象组成Map.Entry之后，又多了16B的对象头，然后8B的next字段和4B的int型hash字段，为了对齐，还必须添加4B的空白填充。最后还有HashMap中对这个Entry的8B的引用，这样增加两个长整型数字，实际耗费的内存为 (Long(24B)*2)+Entry(32B)+HashMap Ref(8B)=88B,空间效率为16B/88B=18%。

程序在最小化时，组员管理中显示的占用内存大幅度减小，但是虚拟内存则没有变化，程序最小化时它的工作内存被自动交换到磁盘的页面文件之中，这样发生GC时就可能因为恢复页面的操作而导致不正常的GC停顿。

### Eclipse调优 ###
方法区大小 调整

通过-Xverify:none禁止字节码验证过程也可以

CompileTime 

java的运行期编进行编译需要消耗程序正常的运行时间，就是上面所说的“编译时间” 

虚拟机提供一个参数-Xint禁止编译器运作，强制虚拟机对字节码采用纯解释方式执行。当虚拟机运行在-client模式的时候，使用的是一个代号为C1的轻量级编译器，另外还有一个代号为C2的相对重量的编译器能提供更多的优化措施，如果使用-server模式的虚拟机启动Eclipse将会使用到C2编译器。这时VisualGC中可以看到启动过程中使用了更多的事件进行代码编译。

在程序运行一段时间后，热点方法被不断编译，新的热点方法数量也总会下降，但是垃圾收集则是随着程序运行而不断运作的，所以它对性能的影响才显得尤为重要。

每一次发生垃圾收集动作，所有线程都必须跑到一个安全点，然后挂起线程等待垃圾回收。这样频繁的GC就会导致很多没有必要的安全点检测、线程挂起及恢复操作。很有必要使用-Xmn参数调整新生代的大小

### 由Windows虚拟内存导致的长时间停顿 ###
使用-XX:PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -Xloggc : gclog.log

## 类文件结构 ##
代码编译的寄过从本地机器码转变为字节码，是存储格式发展的一小步，确实编程语言发展的一大步。

### 无关性基础 ###
实现语言无关性的基础仍然是虚拟机和字节码存储格式。Java虚拟机不和包括Java在内的任何语言绑定，它只与“Class文件”这种特定的二进制文件格式所关联，Class文件中包含了Java虚拟机指令集和符号表以及若干其他辅助信息。虚拟机并不关心Class的来源是何种语言。

Java语言中的各种变量、关键字和运算符号的语义最终都是由多条字节码命令组合而成的，因此字节码命令所能提供的语义描述能力肯定会比Java语言本身更加强大。因此，有一些Java语言本身无法有效支持的语言特性不代表字节码本身无法有效支持，这也为其他语言实现一些有别于Java的语言特性提供了基础。

**任何一个Class文件都对应着唯一一个类或接口的定义信息，但反过来，类或接口并不一定得定义在一个文件里。**

Class文件是一组以8位字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑地排列在Class文件之中，中间没有任何分隔符，这使得整个Class文件中存储的内容几乎全部是程序运行的必要数据，没有空隙存在。遇到占用八位字节以上空间的数据项时，则会按照高位在前的方式分割成若干个8位字节进行存储。

Class文件格式采用一种类似C语言结构体的伪结构来存储数据，这种伪结构只有两种数据结构：无符号数和表。

#### 魔数与Class文件的版本 ####
每个Class文件的头四个字节被称为魔数(Magic Number),它唯一作用是确定这个文件是否为一个能被虚拟机接受的Class文件。

Class文件的魔数的获得很有“浪漫气息”，值为:0xCAFEBABE(咖啡宝贝？),这个魔数值在Java还成为Oak语言的时候就已经确定下来了。

紧接着魔数的4个存储字节是Class文件的版本号：第5和第6个字节是次版本号，第7和第8个字节是主版本号，Java的版本号是从45开始的。高版本的JDK能向下兼容以前版本的Class文件，但不能运行以后版本的Class文件，即使文件格式未发生任何变化，虚拟机也必须拒绝执行超过其版本号的的Class文件。

JDK1.1能支持的版本号为45.0~45.65535,JDK1.7的版本可生成的Class文件主版本号最大值为51.0。

#### 常量池 ####
常量池可以理解为Class文件之中的资源仓库，它是Class文件结构与其他项目关联最多的数据类型，也是占用Class文件控件最大的数据项目之一，同时它还是Class文件中第一个出现的表类型数据结构项目。

由于常量池中常量的数量是不固定的，所以在常量池的入口需要放置一项u2类型的数据，代表常量池容量计数值(constant_pool_count)。与Java语言习惯不一样，这个容器计数是从1开始而不是从0开始。0x0016，即十进制22，代表常量池中有21项常量，索引范围是1~21。

设计者将第0项常量空出来是有特殊考虑的，这样做的目的在于满足后面某些指向常量池的索引值的数据再特定情况下需要表达不引用任何一个常量池项目的含义。这种情况就可以把索引位置置为0来表示。

常量池主要存放两大类常量：字面量和符号引用

字面量比较接近Java语言层面的常量概念，如文本字符串、声明为final的常量值。而符号引用则属于编译原理方面的概念，包含类和接口的全限定名(Full Qualified Name)、字段名称和描述符(Descriptior)、方法的名称和描述符。

Java代码在进行Javac编译的时候，并不像C和C++那样有“连接”这一步骤，而是在虚拟机加载Class的时候进行动态连接。在Class文件中不会保留各个方法、字段的最终内存布局信息，因此这些字段、方法的符号引用不经过运行期装换的话无法得到真正的内存入口地址，也就无法直接被虚拟机使用。当虚拟机运行时，需要从常量池获得对应的符号引用，再在类创建时或运行时解析、翻译到具体的内存地址之中。

常量池中的每一项常量都是一个表，在JDk1.7之前共有11种各不相同的表结构，在JDK1.7中，为了更好地支持动态语言调用，又额外增加了3种(CONTANT_MethodHandle_info、CONSTANT_MethodType_info和CONSTANT_InvokeDynamic_info)。这14种表都有一个共同的特点，就是表开始的第一位是一个u1类型的标志位(tag)

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzhrrk8hwyj20p20e70zp.jpg)

#### 无符号数 ####
无符号数属于基本的数据结构，以u1、u2、u4、u8来分别代表1个字节、2个字节、4个字节、8个字节的无符号数。无符号数可以用描述数字、索引引用、数量值或者按照UTF-8编码构成字符串值。

表示有多个无符号数或者其他表作为数据项都构成的符合数据类型，所有表都习惯性地以"_info"结尾。表用于描述有层次关系的符合结构数据，整个Class文件本质上就是一张表。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzhoze9qpcj20pa0flwjn.jpg)

无论是无符号数还是表，当需要描述同一类型但数量不定的多个数据时，经常会使用一个前置的容量计数器加若干个连续的数据项的形式，这时称这一些列连续的某一类型的数据为某一类型的集合。

CONSTANT\_Class\_info u1 tag 1 u2 name_index 1

name_index是一个索引值，指向常量池中一个CONSTANT_Utf8_info常量 这个代表这个类(或者接口)的全限定名 

CONSTANT\_Utf8\_info u1 tag 1 u2 length 1 u1 bytes length 

使用javap -verbose TestClass查看输出Class文件

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzhstdlhrsj20ip0jnjyi.jpg)

#### 访问标志 ####
常量池结束之后，紧接着的两个字节代表访问标志(access_flag),这个标志用于识别一些类或者接口层次的访问信息，包括：这个Class是类还是接口；是否定义为public类型；是否定义为abstract；是类的话，是否被声明为final等。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzhuh2m47zj20p50bigre.jpg)
#### 类索引、父索引与接口索引集合 ####
类索引(this_class)和父类索引(super_class)都是一个u2类型的数据，而接口索引集合(interfaces)是一组u2类型的数据的集合，Class文件中由这三项数据来确定这个类的继承关系。类索引用于确定这个类的全限定名，父类索引用于确定这个类的父类全限定名。除了java.lang.Object外，所有Java类的父类索引都不为0。

#### 字段表集合 ####
字段表(field_info)用于描述接口或者类中声明的变量。字段包括类级别变量以及实例级变量。Java中描述一个字段包含的信息有:字段的作用域(public、private、protected修饰符)、是实例变量还是类变量(static修饰符)、可变性(final)、并发可见性(volatile修饰符，是否强制从主内存读写)、是否被可序列化(transient修饰符)、字段数据类型(基本类型、对象、数组)、字段名称。以上信息，各个修饰符都是布尔值，要么有某个修饰符，要么没有，很适合用标志位来表示。而字段名叫什么、字段被定义为什么数据类型这些是无法固定的，只能引用常量池中的常量来描述。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzhv9oektuj20pk0h3wmt.jpg)

限定全名和简单名称很好理解，仅仅是把类全名中的"."替换成"/"而已，为了使多个连续限定全名之间不会产生混淆，在使用时最后一般会加一个";"表示全限定名结束。简单名称是指没有类型和参数修饰的方法或者字段名称inc()和m字段的简单名称分别是"inc"和"m"。

描述符的作用是用来描述字段的数据类型、方法列表(包括数量、类型以及顺序)和返回值。 根据描述符规则，基本数据类型(byte、char、double、float、int、long、short、boolean)以及代表无返回值的void类型都用一个大写字母来表示，而对象则用字符L加对象的全限定名来表示

对于数组类型，每一未读将使用一个前置的"["字符来描述，如java.lang.String[][]类型的二维数组将被记录为"[[Ljava/lang/String"

用描述符来描述方法时，按照先参数列表，后返回值的顺序描述，参数列表按照参数的严格书序放在一组小括号"()"之内，如方法void inc的描述符为"()V",方法java.lang.String.toString()的描述符为"()Ljava/lang/String;",方法 int indexOf(char[] source,int sourceOffset,int sourceCount,char[] target, int targetOffset, int targetCount, int fromIndex)的描述符"([CII[CIII)I"。

字段表集合中不会列出从超类或者父类接口中继承而来的字段，但有可能列出原本Java代码之中不存在的字段，譬如内部类为了保持对外部类的访问性，会自动添加指向外部类实例的字段。另外，在Java语言中字段是无法重载的，两个字段你的数据类型、修饰符不管是否相同，都必须使用不同的名称，但对于字节码来讲，如果两个字段的描述符不一致，那么字段重名就是合法的。
![](http://ww1.sinaimg.cn/large/48ceb85dly1fzhvkogd4ej20p006utba.jpg)

#### 方法表集合 ####
Class文件存储格式对方法的描述u字段的描述机会采用了完全一致的方式

方法表的访问标志中没有ACC_VOLATILE标志和ACC_TRANSIENT标志，与之相对的，synchronized、native、strictfp和abstract关键字可以修饰方法，所以方法表的访问标志中增加了ACC_SYNCHRONIZED、ACC_NATIVE、ACC_STRICTFP和ACC_ABSTRACT标志。

方法里的代码，经过编译器编译成字节码指令后，存放在属性表集合中一个名叫"Code"的属性里面，属性表是Class文件中最具有扩展性的数据项目。

与字段表集合相对应的，如果父类方法再子类中没有被重写(Override)，方法表集合中就不会出现来自父类的方法信息。但同样的，有可能出现由编译器自动添加的方法，最典型的便是类构造器<clinit>方法和实例构造器<init>方法。在Java语言中，要重载(Overload)一个方法，除了要与原方法具有相同的简单名称之外，还必须要求拥有一个与原方法不同的特征签名。特征签名就是一个方法中各个参数在常量池中的字段符号引用的集合，也就是因为返回值不会包含在特征签名中，因此Java语言里是无法仅仅依靠放回值的不同对一个已有的方法进行重载的。但在Class文件格式中，特征签名的范围更大一些，只要描述符不是完全一致的两个方法也可以共存。也就是说，如果两个方法有相同的名称和特征签名，但返回值不同，那么也是可以合法共存于一个Class文件中的。

#### 属性表集合 ####
属性表集合不要求各个属性表有严格顺序，而且只要不与已有属性重复，任何人实现的编译器都可以向属性表中写入自己定义的属性信息，Java虚拟机运行时会忽略掉它不认识的属性。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzimjnlqurj20oq07h77p.jpg)
![](http://ww1.sinaimg.cn/large/48ceb85dly1fzimjujd7uj20or0pan9v.jpg)
![](http://ww1.sinaimg.cn/large/48ceb85dly1fzimjzka2bj20oo069whz.jpg)

*属性表结构*

对于每个属性，它的名称需要从常量池中引用一个CONSTANT_Utf8_info常量来表示，而属性值的结构则完全是自定义的，只需要通过一个u4长度属性去说明属性值所占用的位数即可。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzis5j4ay8j20p3050dhc.jpg)


*Code表结构*

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzis66v4zpj20og0ai42k.jpg)

attribute_name_index是一项指向CONSTANT_Utf8_info型常量，常量值固定为"Code"，它代表该属性的属性名称, attribute_length知识了属性值的长度。 max_stack代表了操作数栈(Operand Stacks)深度的最大值。在方法执行的任意时刻，操作数栈都不会超过这个深度。虚拟机运行的时候需要根据这个值来分配栈帧(Stack Frame)中的操作栈深度。max_locals代表了局部变量表所需的存储空间。在这里，max_locals的单位是Slot，Slot是虚拟机为局部变量分配内存所使用的最小单位。对于byte、char、float、int、short、boolean和returnAddress等长度不超过32位的数据类型，每个局部变量占1个Slot，而double和long这种64位数据类型则需要两个Slot来存放。方法参数（包括实例方法中的隐藏参数“this”）、显示处理器的参数（Exception Handler Parameter，就是try-catch语句中catch所定义的异常）、方法体中定义的局部变量都需要局部变量表来存放。

并不是方法中用到了多少个局部变量，就把这些局部变量所占Slot之和作为max_locals的值，原因是局部变量表所占的Slot可以被其他变量所使用，Javac编译器会根据变量的作用域来分配Slot给各个变量使用，然后计算max_locals的大小。

code_length和code用来存储Java源程序编译后生成的字节码指令。code_length代表字节码长度，code是用于存储字节码指令的一系列字节流，每个指令就是一个u1类型的单字节，当虚拟机读到code中的一个字节码时，就可以对应找出这个字节码代表是什么指令。并且可以知道这条指令后面是否需要跟随参数，以及参数应当如何理解，一共可以表达256条指令。

关于code_length，虽然它是一个u4类型的长度，但虚拟机规范中明确限制了一个方法不允许超过65535条字节码指令，即它实际上只使用了u2的长度。如果超过这个限制，Javac编译器也会拒绝编译。

翻译"2A B7 00 0A B1"的过程为：

1. 读入2A，查得0x2A对应的指令为aload_0,这个指令的含义是将第0个Slot中卫reference类型的本地变量推送到操作数栈
2. 读入B7，查表得0xB7对应的指令时invokespecial,这条指令的作用是以栈顶的reference所指向的对象作为方法接收者，调用此对象的实例构造器、private方法或者它的父方法。这个方法有一个u2类型的参数说明具体调用哪一个方法，它指向常量池CONSTANT_Methodref_info类型常量，即此方法的方法符号引用
3. 读入00 0A,这是invokespecial的参数，查常量池0x000A对应的常量为实例构造函数<init>的符号引用
4. 读入B1，查表得0xB1对应的指令为return，含义是返回此方法，并且返回值为void。这条指令执行后，当前方法结束。

字节码指令之后的是这个方法的异常处理表集合。这些字段的含义为：如果当字节码在第start_pc行到第end_pc行(不包含)之间出现了catch_type或者其子类的异常(catch_type为指向一个CONSTANT_Class_info型常量的索引)，则转到第handler_pc行继续进行处理。当catch_type的值为0时，代表任意异常情况都需要转向handler_pc处进行处理。

字节码中的“行”是一种形象的描述，指的是字节码相对于方法体开始的偏移量，而不是Java源码的行号。JDK1.4.2之前的Javac采用jsr和ret指令来实现finally语句，但1.4.2之后已经改为编译器自动在每段可能的分支路径之后都将finally语句块的内容荣誉生成一遍来实现finally语义。JDK1.7中，已经完全禁止Class文件中出现jsr和ret指令，如果遇到这两条指令，虚拟机会在类加载的字节码校验阶段抛出异常。

*Exception属性*

Exceptions属性的作用是列举出方法栈宏可能抛出的受查异常(Checked Exception)，也就是方法描述时在throws关键字后面列举的异常。

*LineNumberTable属性*

LineNumberTable用于描述Java源码行号与字节码行号(偏移量)之间的对应关系，默认会生成在Class文件中，可以在Javac中分别是用-g:none或-g:lines选项来取消或要求生成这项信息。

line_number_table是一个数量为line_number_table_length、类型为line_number_info的集合，line_number_info表包括start_pc和line_number两个u2类型的数据项，前置是字节码行号，后者是Java源码行号。

*LocalVariableTable属性*

LocalVariableTable 属性用于描述栈帧中局部变量表中的变量与Java源代码中定义的变量之间的关系，也不是必须的属性，但默认生成在Class文件之中，可以在Javac中分别使用-g:none或-g:vars选项来取消或者要求生成这项信息。如果没有生成这项属性，最大的麻烦就是其他人引用这个方法时，所有的参数名称都会消失，IDE将会使用诸如arg0、arg1之类的占位符代替原有的参数名，这对程序运行没有影响，但会对代码编写带来较大不便。而且在调试期间无法根据参数名从上下文中获得参数值。

local_variable_info代表了一个栈帧与源码中的局部变量的关联。

在JDK中引入泛型之后，增加加了一个LocalVariableTypeTable，仅仅是把记录的字段描述符的descriptor_index替换成了字段的特征签名(Signature),对于非泛型类型来说，描述符和特征签名能描述的信息是基本一致的，但是泛型引入之后，由于描述符中泛型的参数化类型被擦除，描述符就不能准确地描述泛型类型了，因此出现了LocalVariableTypeTable

*SourceFile属性*

关闭，抛出异常时，对战中将不会显示出错代码所属的文件名(Unknown Source ?)

*ConstantValue属性*

ConstantValue 属性的作用是通知虚拟机自动为静态变量赋值，只有被static关键字修饰的变量(类变量)才可以使用这项属性。 对于非static类型的变量(也就是实例变量)的赋值是在实例构造器<init>方法中进行的；而对于类变量，则有两种方式可以选择：在类构造器<clinit>方法中或者使用ConstantValue属性。JDK1.7编译器的选择是如果同时使用final和static来修饰一个变量，并且这个常量的数据类型为基本类型或者String，就生成ConstantValue来进行初始化，如果这个变量没有被final修饰或者并非基本类型，就会在<clinit>方法中进行初始化。

*InnerClasses属性*

记录内部类和宿主类之间的关联，如果一个类定义了内部类，那编译器会为它以及它锁包含的内类生成InnerClasses属性。

*Deprecated及Synthetic属性* 

都属于标志类型的布尔属性，只存在有和没有的区别，没有属性值的概念

Synthetic属性代表此字段或者方法并不是由Java源代码直接产生的，而是由编译器自动添加的，典型的就是 Bridge Method。所有非用户代码产生的类、方法及字段都应当至少设置Synthetic属性和ACC_SYNTHETIC标志中的一项，唯一的例外是实例构造器<init>方法和类构造器<clinit>方法

*StackMapTable属性*

JDK1.6发布增加到Class文件规范中，是一个复杂的变长属性，位于Code属性的属性表中，这个属性会在虚拟机类加载的字节码验证阶段被新类型检查器使用，目的在于代替以前比较消耗性能的基于数据流分析的类型退到验证器。省略了运行期通过数据流分析去确认字节码行为逻辑性的步骤，而是在编译阶段将一些列的验证类型直接记录在Class文件之中，通过检查这些验证类型代替类型推导的过程，从而大幅提升字节码验证的性能。

一个方法的Code属性最多只能有一个StackMapTable属性，否则将抛出ClassFormatError 异常。

*Signature属性*

使用擦除的缺点就是运行期无法像C#等有真泛型支持的语言那样，将泛型类型域用户定义的普通类型同等对待，例如运行期做反射时无法获得泛型信息。Signature属性就是为了弥补这个缺陷而增设的，现在Java的反射API能够获取泛型类型，最终的数据来源也就是这个属性。

如果当前的Signature属性是类文件的属性，则这个结构表示类签名，如果当前的Signature属性是方法表的属性，则这个结构表示方法类型的签名，如果当前Signature属性表字段表的属性，那么结构表示字段类型签名。

*BootstrapMethods属性*

这个属性用于保存invokedynamic指令引用的引导方法限定符。如果某个类文件结构的常量池中曾经出现过CONSTANT_InvokeDynamic_info类型的常量，那么这个类文件的属性表中必须存在一个明确的BootstrapMethods属性，即使多个，并且最多页只有一个。

## 字节码指令 ##
Java虚拟机的指令由一个字节长度的、代表着某种特定操作含义的数字(成为操作码Opcode)以及跟随其后的零至多个代表此操作所需参数(操作数Operands)而构成。由于Java虚拟机采用面向操作数栈而不是寄存器的架构，所以大多数指令不包含操作数，只有一个操作码。

由于限制了Java虚拟机操作码的长度为一个字节(0~255),这意味着指令集的操作码总数不可能超过256条；又由于Class问价格式放弃了编译后代码的操作数对齐，这就意味着虚拟机处理那些超过一个字节数据的时候，不得不在运行时从字节中重新构建出具体数据的结构，如果要讲一个16位长度的无符号整数使用两个无符号字节存储起来(将它们命名为byte1和byte2)，那它们的值应该是这样的：

	(byte1<<8)|byte2

### 字节码与数据类型 ###
Java虚拟机的指令集中，大多数的指令都包含了器操作数对应的数据类型信息。例如，iload指令用于从局部变量表中加载int型的数据到操作数栈中，而fload指令加载的则是float类型的数据。而两条指令的操作在虚拟机内部可能会是由同一段代码来实现的，但在Class文件中它们必须拥有各自队里的操作码。

#### 助记符 ####
- i int
- l long
- s short
- b byte
- c char
- f float
- d double
- a reference

**并非每一种数据类型和每一种操作都有对应的指令，有一些指令可以在必要的时候用来将一些不支持的类型转换为可被支持的类型。**

大部分指令都没有支持整数类型byte、char、short，设置没有任何指令支持boolean类型。编译器会在编译期或运行将byte和short类型的数据带符号扩展为相应的int型类型数据，将boolean和char类型数据零位扩展为相应的int类型数据。与之类似，在处理Boolean、byte、short和char类型的数组时，也会转换为使用相应int类型的字节码来处理。因此，大多数对boolean、byte、short和char类型数据的操作，实际上都是使用相应int类型作为运算类型。

### 加载和存储指令 ###
加载和存储指令用于将数据再栈帧中的局部变量表和操作数栈之间来回传输。

- 将一个变量加载到操作数栈 Tload、Tload_<n>
- 将一个数值从操作数栈存储到局部变量表 Tstore Tstore_2
- 将一个常量加载到操作数栈 bipush、sipush、ldc、ldc_w、ldc_w、ldc2_w、aconst_null、iconst_m1,Tconst_<T>

存储数据的操作数栈和局部变量表主要是由加载和存储指令进行操作，除此之外，还有少量指令，如访问对象的字段或数据元素的指令也会向操作栈传输数据。

iload_0 和操作数为0 的iload指令语义完全一致

### 运算指令 ###
运算或算数指令用于对两个操作数栈上的值进行某种特定运算，并把结果重新存入到操作栈顶。大体上算数指令可以分为两种：对整形数据进行运算的指令与对浮点型数据进行运算的指令。

- 加法指令:iadd、ladd、fadd、dadd
- 减法指令:isub、lsub、fsub、dsub
- 乘法指令:imul、lmul、fmul、dmul
- 除法指令:idiv、ldiv、fdiv、ddiv
- 求余指令:irem、lrem、frem、drem
- 取反指令:ineg、lneg、fneg、dneg
- 位移指令:ishl、ishr、iushr、lshl、lshr、lushr
- 按位或指令:ior、lor
- 按位与指令:iand、land
- 按位异或指令:ixor、lxor
- 局部变量自增指令:iinc
- 比较指令:dcmpg、dcmpl、fcmpg、fcmpl、lcmp

Java虚拟机的指令集直接支持了《Java语言规范》中描述的各种对整数及浮点数操作的语义。数据运算可能会导致溢出。其实Java虚拟机规范没有明确定义过整形数据溢出的具体运算结果，仅规定了处理整形数据时，只有除法指令(idiv和ldiv)以及求余指令(irem和lrem)中当出现除数为零时会导致虚拟机抛出(ArithmeticException)，其余任何整数运算场景都应该抛出运行时溢出。

Java虚拟机规范要求虚拟机实现处理浮点数时，必须严格遵循IEEE754规范中所规定的行为和限制。也就是说，Java虚拟机必须完全支持IEEE754中定义的非正规浮点数值(Denormalized Floating-Point Numbers)和逐级下溢(Gradual Underflow)的运算规则。这些特征将会使某些数值算法处理起来变得相对容易一些。

Java虚拟机要求在进行浮点数运算时，所有的运算结果都必须舍入到适当的精度，非精度的结果必须舍入为可表示的最接近的精确值，如果有两种可表示的形式与该值一样接近，将优先选择最低有效位为0的。这种舍入模式也是IEEE754规范中的默认舍入模式，成为最接近数舍入模式。

把浮点数转换为整数时，Java虚拟机使用IEEE754标准中的向零舍入模式。这种模式的舍入结果会导致数字被截断，所有小数部分的有效字节都会被丢弃。向零舍入模式将在目标数值类型中选择一个最接近但是不大于原值的数字来作为最精确的舍入结果。

Java虚拟机在处理浮点数运算时，不会抛出任何运行时异常（IEEE754的浮点异常时一种运算信号），当一个操作产生溢出时，将会使用有符号的无穷大来表示，如果某个操作结果没有明确的数学定义的话，将会使用NaN值来表示。所有NaN值作为操作数的算数操作，结果都会返回NaN。

在对long类型数值进行比较时，虚拟机采用带符号的比较方式，而对浮点数值进行比较时(dcmpg、dcmpl、fcmpg、fcmpl)，虚拟机会采用IEEE754规范锁定义的无法号比较(Nonsignaling Comparisons)方式。

### 类型转换指令 ###
Java虚拟机直接支持一下数值类型的宽化类型转换(widening Numeric Conversions，即小范围类型向大范围类型的安全转换)

- int类型到long、float或者double类型
- long类型到float、double类型
- float类型到double类型

相对的，处理窄化类型转换(Narrowing Numeric Conversions)时，必须显式地使用转换指令来完成，这些转换指令包括：i2b、i2c、i2s,l2i、f2i、f2l、d2i、d2l和d2f。窄化类型转换可能导致转换结果产生不同的正负号、不同数量级的情况，转换过程可能会导致数值的精度丢失。

将一个浮点值窄化转换为整数类型T(T限于int或long类型之一)的时候，将遵循以下转换规则：

- 如果浮点值是NaN，那转换结果就是int或long类型的0
- 如果浮点值不是无穷大的话，浮点值使用IEEE754的向零舍入模式取整，获得整数值v，如果v在目标类型T(int或long)的表示范围，那转换结果解释v
- 否则，将根据v的符号，转换为T所能表示的最大或者最小正数。

### 对象创建与访问指令 ###
Java虚拟机对类实例和数组的创建使用了不同的字节码指令。

- 创建类实例的指令：new
- 创建数组的指令：newarray、anewarray、multianewarray
- 访问类字段和实例变量的指令:getfield、putfield、getstatic、putstatic
- 把一个数组元素加载到操作数栈的指令：baload、caload、saload、iaload、laload、faload、daload、aaload
- 把一个操作栈的值存储到数组元素中的指令：Tastore
- 去数组长度的指令:arraylength
- 检查类实例类型的指令:instanceof、checkcast

### 操作数管理指令 ###
- 将操作数栈的栈顶一个或两个元素出栈：pop、pop2
- 复制栈顶一个或两个数值并复制值或双份的复制值重新压入栈顶：dup、dup2、dup_x1、dup_x2、dup2_x2
- 将站顶端的两个数值互换swap

### 控制转移指令 ###
控制转移指令可以让Java虚拟机有条件或无条件地从指定的位置指令而不是控制转移指令的下一条继续执行程序，从概念模型上理解，可以认为控制转移指令就是有条件或无条件地修改PC寄存器的值。

条件分支:ifeq、iflt、ifne、ifgt、ifge、ifnull、ifnonnull、ificmpeq、if_icmpeq、if_icmpne、if_icmplt、if_icmple、if_icmpge、if_acmpeq和if_acmpne。
复合条件分支:tableswitch、lookupswitch
无条件分支:goto、goto_w、jsr、jsr_w、ret

Java虚拟机中有专门的指令集来处理int和reference类型的条件分支比较操作，为了可以无需明显标识一个实体值是否null，也有专门的指令来检测null值。

比较运算指令会返回一个整型值到操作数栈中，随后再执行int类型的条件分支比较操作来完成整个分支跳转。

### 方法调用和返回指令 ###
- invokevirtual指令用于调用对象的实例方法，根据对象的实际类型进行分派(虚方法分派)
- invokeinterface指令用于调用接口方法，它会在运行时搜索一个实现这个接口方法的对象，找出合适的方法进行调用
- invokespecial指令用于调用一些需要特殊处理的实例方法，包括实例初始化方法、私有方法和父类方法
- invokestatic指令用于调用类方法
- invokedynamicd指令用于在运行时动态解析出调用点限定符所以引用的方法，并执行该方法，前面4条调用指令的分派逻辑都固化在Java虚拟机内部，而invokedynamic指令的分派逻辑是由用户所设定的引导方法决定的。

方法调用指令与数据类型无关，而方法返回指令时根据返回值的类型区分的，包括ireturn(当返回值是boolean、byte、char、short和int类型时所使用)、lreturn、freturn、dreturn和areturn，另外一条return指令提供声明为void的方法、实力初始化方法以及类和接口的类初始化方法使用。

### 异常处理指令 ###
在Java程序中显式抛出异常操作都有athrow指令来实现

异常处理不是由字节码来实现的，而是由异常表来完成的

### 同步指令 ###
Java虚拟机可以支持方法级的同步和方法内部一段指令序列的同步，而这两种结构都使用管程(Monitor)来支持的。

方法级的同步是隐式的，即无需使用字节码来控制，它实现在方法和返回操作之中。虚拟机可以从方法常量池的方法表结构中的ACC_SYNCHRONIZED访问标志得知一个方法是否声明为同步方法。当方法调用时，调用指令将会检查ACC_SYNCHRONIZED访问标志是否被设置了，如果设置了，执行线程就要求先成功持有管程，然后才能执行方法，最后当方法完成时释放管程。在方法执行期间，执行线程持有了管程，其他任何线程都无法获得同一个管程，如果一个同步方法执行期间抛出了异常，并且在方法内部无法处理此异常，那么这个同步方法所持有的管程将在异常抛到同步方法之外时自动释放。

同步一段指令序列通常是由Java语言中的synchronized语句块来表示的，Java虚拟机的指令集中有monitorenter和monitorexit两条指令来支持synchronized关键字的语义，正确实现synchronized关键字需要Java编译器与Java虚拟机两者共同协作支持。

为了保证在方法异常完成时monitorenter和monitorexit指令依然可以正确配对执行，编译器会自动产生一个异常处理器，这个异常处理器声明可以处理的所有的异常，它的目的就是来执行monitorexit指令。

### 公有设计和私有实现 ###
虚拟机实现的方式主要有以下两种：

- 将输入的Java虚拟机代码在加载或执行时翻译成另外一种虚拟机的指令集
- 将输入的Java虚拟机代码在加载或执行翻译成宿主机CPU的本地指令集(即JIT代码生成技术)

### 加载类的过程 ###


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
- ES（Extra Segment）：附加段寄存器

## 虚拟机类加载机制 ##
虚拟机把描述类的数据从Class文件加载到内存，并对数据进行校验、转换解析和初始化，最终形成可以背虚拟机直接使用的Java类型，这就是虚拟机的类加载机制。

在Java语言中，类型的加载、连接和初始化过程都是在程序运行期间完成的，这种策略虽然会令类加载时稍微增加一些性能开销，但是会为Java应用程序提供高度的灵活性，Java里天生可以动态扩展的语言特性就是依赖运行期动态加载和动态连接这个特点。例如，如果编写一个面向接口的应用程序，可以等到运行时再指定其实际的实现类；用户可以通过Java预定义和自定义类加载器，让一个本地应用可以在运行时从网络或其他地方加载一个二进制流作为程序代码的一部分。从出的Applet、JSP到相对复杂的OSGi技术，都使用了Java语言运行期类加载的特性。

### 类加载的时机 ###
类从被加载到虚拟机内存中开始，到卸载出内存为止，它的整个生命周期包括：加载(Loading)、验证(Verification)、准备(Preparation)、解析(Resolution)、初始化(Initialization)、使用(Using)和卸载7个阶段。其中验证、准备、解析3个部分统称为连接(Linking)。

加载、验证、准备、初始化和卸载这5个阶段的顺序是确定的，类的加载过程必须按照这种顺序按部就班地开始，而解析阶段则不一定：它在某些情况下可以在初始化阶段之后再开始，这就是为了支持Java语言的运行时绑定(也成为运行时绑定或晚期绑定)。并且不一定会按部就班地“进行”或“完成”，强调这点是因为这些阶段通常是相互交叉混合式进行的，通常在一个阶段执行的过程中调用、激活另外一个阶段。

Java虚拟机规范中并没有强制约束什么情况下开始类加载过程。

但是对于初始化阶段，虚拟机规范则是严格规定了有且只有5种情况必须立即对类进行“初始化”(而加载、验证、准备自然需要在此之前开始)：

- 遇到new、getstatic、putstatic或invokestatic这4条字节码指令时，如果类没有进行过初始化，则需要先触发其初始化。
- 使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没有进行过初始化，则需要先触发其初始化。
- 当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化
- 当虚拟机启动时，用户需要指定一个要执行的主类(包含main()方法的那个类)，虚拟机会先初始化这个主类。
- 当使用JDK1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后解析结果REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄,并且这个方法句柄所对应的类没有进行过胡世华，则需要先触发其初始化。

这5种场景中的行为称为对一个类进行主动引用。除此之外，所有引用类的方法都不会触发初始化，称为被动引用。

**通过子类引用父类中定义的静态字段，只会触发父类的初始化，而不会触发子类的初始化。**


> 至于是否需要触发子类的加载和验证，在虚拟机规范中并未明确规定，这点取决于虚拟机的具体实现。对于 HotSpot虚拟机来说，可通过-XX:+TraceClassLoading参数观察到此操作会导致子类的加载。

	public class NotInitialization {
		public static void main(String[] args){
			SuperClass[] sca = new SuperClass[10];
		}
	}

运行之后没有输出 "SuperClass init!", 说明没有触发SuperClass的初始化阶段，但是这段代码触发了另外一个名叫"[LSuperClass"的类的初始化阶段，对于用户代码来说，这不是一个合法的类名称，它是一个由虚拟机自动生成、直接继承与java.lang.Object的子类，创建动作由字节码newarray触发。这个类代表了一个元素类型为SuperClass的一维数组，数组中应有的属性和方法都实现在这个类里。

Java语言中对数组的访问比C/C++相对安全的原因是因为这个类封装了数组元素的访问方法，而C/C++直接翻译为堆数组指针的移动。

对其他类的静态变量的引用不会触发其他类的初始化，其实在编译阶段，通过常量传播优化，已经将常量存储到自身类的常量池中，之后的引用都会转化为自身类对自身常量池的引用，也就是说两个雷编译成Class之后就不存在任何联系了。

接口加载过程与类加载过程稍有一些不同，针对接口需要做一些特殊说明：接口也有初始化过程，这点与类是一致的。接口中不能使用static{}语句块，但编译器仍然会为接口生成<clinit>()类构造器，用于初始化接口中所定义的成员变量。接口与类真正的区别是以上第3中，在初始化一个接口时，并不会要求其父几口全部完成初始化，只有真正用到父接口的时候才会初始化。

### 类加载的过程 ###
加载、验证、准备、解析和初始化

#### 加载 ####
加载是“类加载”(Class Loading)过程的一个阶段，在加载阶段：

- 通过一个类的全限定名来获取定义此类的二进制字节流
- 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
- 在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口

“通过一个类的全限定名来获取定义此类的二进制字节流”这条，它没有指明二进制字节流要从一个Class文件中获取，准确地说是根本没有指明要从哪里获取，怎样获取，这为加载建立了一个广阔的舞台，许多重要的Java技术都建立在这一基础之上，例如：

- 从ZIP包中读取，这很常见，最终成为JAR、EAR、WAR格式的基础
- 从网络中获取，典型的应用是Applet
- 运行时获取，这种场景使用最多的就是动态带领技术，在java.lang.reflect.Proxy中，就是用了ProxyGenerator.generateProxyClass来为特定接口生成形式为"*$Proxy"的代理类二进制字节流。
- 由其他文件生成，典型的场景是JSP应用，即由JSP文件生成对应的Class类
- 从数据库读取，这种场景相对少见写，例如有些中间件服务器(如SAP Netweaver)可以选择把程序安装到数据库中来完成程序代码在集群间的分法。

开发人员可以通过自定义的类加载器去控制字节流的获取方式。**数组类是由Java虚拟机直接创建**

数组类的创建规则

- 如果数组的类型组件是引用类型，那么就递归采用定义的加载过程去加载这个组件类型，数组C将在加载改组件类型的类加载器的类名称空间上被标识
- 如果数组的类型组件不是引用类型，Java虚拟机会将数组C标记为与引导类加载器关联
- 数组类的可见性与它的组件类型的可见性一致。如果组件类型不是引用类型，那数组类的可预见性默认为public

加载阶段完成后，虚拟机外部的二进制字节流就按照虚拟机所需要的格式存储在方法区之中，方法区中的数据格式由虚拟机实现自行决定。Hotspot虚拟机中的Class对象比较特殊，虽然它是对象，但是存放在方法区里面，这个对象将作为程序访问方法区中这些类型数据的外部接口

加载与连接阶段的部分内容（如一部分字节码文件格式验证动作）是交叉进行的，加载阶段尚未完成，连接阶段可能已经开始。

#### 验证 ####
验证时连接阶段的第一步，这一阶段的目的是为了确保Class文件的字节流包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全。

Java语言本身是相对安全的语言，使用纯粹的Java代码无法所做到访问数组边界以外的数据、将一个对象转型为它未曾实现的类型、跳转到不存在的代码行之类的事情，如果这样做了，编译器将拒绝编译。但是Class文件并不一定要求用Java源码编译而来，可以使用任何途径产生，甚至可以用十六进制编辑器直接来产生Class文件。在字节码语言层面上，上述Java代码无法做到的事情都是可以实现的，至少语义上是可以表达出来的。虚拟机如果不检查输入的字节流，对其完全信任的话，很可能会因为载入了有害的字节流而导致系统崩溃，锁验证时虚拟机对自身保护的一项重要工作。

验证阶段是否严谨，直接决定了Java虚拟机能否承受恶意代码的攻击，从执行性能的角度上，验证工作的工作量在虚拟机的类加载子系统中又占了相当大的部分。如果验证到数据的字节流不符合Class文件格式的约束，虚拟机就应抛出java.lang.verifyError异常或器子类异常。

验证阶段大致上会完成下面4个阶段的检验动作：文件格式验证、元数据验证、字节码验证、符号引用验证。

*文件格式验证*

第一阶段要验证字节流是否符合Class文件格式的规范，并且能被当前版本的虚拟机处理。验证点：

- 是否以魔数0xCAFEBABE开头
- 主、次版本号是否在当前虚拟机处理范围之内
- 常量池的常量中是否有不被支持的常量类型(检查常量tag标志)
- 指向常量的各种索引值中是否有被指向不存在的常量或不符合类型的常量
- CONSTANT_Utf8_info型的常量中是否有不符合UTF8编码的数据。
- Class文件中各个部分及文件本身是否有被删除的或附加的其他信息

这阶段的验证时基于二进制字节流进行的，只有通过这个阶段的验证后，字节流才会进入内存的方法区进行存储，后面的3个阶段全部是基于方法区的存储结构进行的，不再直接操作字节流。

*元数据验证*

第二阶段是对字节码描述的信息进行语义分析，以保证其描述的信息符合**Java语言规范**的要求

- 这个类是否有父亲(除了java.lang.Object之外，所有类都应当有父亲)。
- 这个类的父亲是否继承了不被允许被继承的类(被final修饰的类)
- 这个类如果不是抽象类，是否实现了其父类或接口之中要求实现的所有方法。
- 类中额字段、方法是否与父类产生矛盾(例如覆盖了父类的final字段，或者出现不符合的方法重载，例如方法参数都一直，但返回类型却不同等)
- ...........

*字节码验证*

第三阶段是整个验证过程中最复杂的一个阶段，主要目的是通过数据流和控制流分析，确定语义是合法的、符合逻辑的。在第二阶段对元数据信息中的数据类型做完校验后，这个额极端将对类的方法体进行校验分析，保证被校验类的方法在运行时不会做出危害虚拟机安全的事件：

- 保证任意时刻操作数栈的数据类型与指令代码序列都能配合工作，例如不会出现类似：在操作栈放置了一个int类型的数据，使用时却按long类型来加载如本地变量表中
- 保证跳转指令不会跳转到方法体以外的字节码指令上
- 保证方法体中的类型转换是有效的，例如把一个子类型对象赋值给父类数据类型，这是安全，反之就是危险和不合法的
- .........

如果一个方法通过了字节码验证，也不能说明其一定是安全的，这里涉及到“停机问题”：通过程序去校验程序逻辑是无法做到绝对准确的

为例避免过多的时间消耗在字节码验证阶段，在JDK1.6之后的Javac编译器和Java虚拟机中进行了一项优化，给方法体的Code属性的属性表增加一项名为“StackMapTable”的属性，这项属性描述了方法体所有的基本块(Basic Block,按照控制流拆分的代码块)开始时本地变量表和操作栈应有的状态，在字节码验证期间，就不需要根据程序推导这些状态的合法性，只需要检查StackMapTable属性中的记录是否合法即可。HotSpot虚拟机提供-XX:UseSplitVerifier选项来关闭这项优化，或者使用参数-XX:+FailOverToOldVerifier要求在类型校验社保的时候退回到旧的类型推导方式进行校验。而在主版本号大于50的Class文件，使用类型检查来完成数据流分析校验则是唯一的选择。

*符号引用校验*

最后一个阶段发生将虚拟机将符号引用转化为直接引用的时候，这个转换动作将会哎链接的第三阶段解析过程中发生，符号引用解析可以看所是对类自身以外(常量池中的各种符号引用)的信息进行匹配性校验，通常校验：

- 符号引用中通过字符串描述的全限定名是否能找到对应的类
- 在指定类总是否存在符合方法的字段描述符以及简单名称所描述的方法和字段
- 符号引用中的类、字段、方法的访问性(private、protected、public、default)是否可被当前类访问
- ..........

符号引用验证的目的是确保解析动作能正常执行，如果无法通过符号引用验证，那么将会抛出一个java.lang.IncompatibleClassChangeError异常的子类

#### 准备 ####
准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，这些变量所使用的内存将会在方法区中进行分配，这个阶段中有两个容易产生混淆的概念需要强调一下，首先，这时候进行内存分配的仅包括类变量，而不包括实例变量，实例变量会在对象实例化时随着对象一起分配在Java堆中。其实，这里所说的初始值“通常情况”下是数据类型的零值。value被复制的putstatic指令时在程序被编译后，存放于类构造器<clinit>()方法之中，所以把value复制会在初始化阶段才会执行。

如果字段属性表存在ConstantValue属性，那么准备阶段value就会被初始化为ConstantValue属性所指定的值。

#### 解析 ####
解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程。符号引用再Class文件中以CONSTANT_Class_info、CONSTANT_Fieldref_info、CONSTANT_Methodref_info等类型的常量出现。

- 符号引用(Symbolic References):符号引用以一组符号来描述所引用的目标，符号可以是任何形式的字面量，只要使用时能无歧义地地位到目标即可。符号引用与虚拟机实现的内存布局无关，引用的目标并不一定已经加载到内存中。各种虚拟机实现的内存布局可以各不相同，但是它们能接受的符号引用必须都是一致的，因为符号引用的字面量形式明确定义在Java虚拟机规范的Class文件格式中。
- 直接引用(Direct References):直接引用可以是直接指向目标的指针、相对偏移量或是一个能间接定位到目标的句柄。直接引用时和虚拟机实现的内存布局相关的，同一个符号引用再不同虚拟机实例上翻译出来的直接引用一般不会相同。如果有了直接引用，那引用的目标必定已经在内存中存在。

虚拟机规范之中并未规定解析阶段发生的具体时间，只要求在执行anewarray、checkcast、getfield、getstatic、instanceof、invokedynamic、invokeinterface、invokespecial、invokestatic、invokevirtual、ldc、ldc_w、multianewarray、new、putfield和putstatic这16个用于操作符号应用的字节码指令之前，先对它们所使用的符号引用进行解析。所以虚拟机实现跨越根据需要来判断到底是在类被加载器加载时就对常量池中的符号引用进行解析，还是等到一个符号引用将要被使用前采取解析它。虚拟机可以第一次解析的寄过进行缓存(在运行时常量池中记录直接引用，并把常量表示为已解析状态)从避免解析动作重复进行。

对于invokeddynamic指令，上面的规则不成立，invokedynamic指令的目的本来就是用于动态语言支持(目前仅适用Java语言不会生成这条字节码指令),它所对应的引用称为“动态调用点限定符”(Dynamic Call Site Specifier),必须等到程序实际运行到这条指令的时候，解析动作才能进行。相对的，其余可出发解析的指令都是“静态”的，可以在刚刚完成加载阶段，还没有开始执行代码时就进行解析。

解析动作针对于7类符号引用进行：

- 类或接口 CONSTANT_Class_info
- 字段 CONSTANT_Fieldref_info
- 类方法 CONSTANT_Methodref_info
- 接口方法 CONSTANT_InterfaceMethodref_info
- 方法类型 CONSTANT_MethodType_info
- 方法句柄 CONSTANT_MethodHandle_info
- 方法调用点限定符 CONSTANT_MethodHandle_info

*类或接口的解析*

当前代码所处的类为D，如果要把一个从未解析过的符号引用N解析为一个类或接口C的直接引用，这个过程需要3个步骤：

1. 如果C不是一个数组类型，那么虚拟机会将代表N的全限定名传递给D的类加载器去加载这个类C。在加载过程中，由于元数据验证、字节码验证的需要，又可能触发其他相关类的加载动作，例如加载这个类类的父类或实现的接口。一旦这个加载过程出现了任何异常，解析过程就是宣告失败。
2. 如果C是一个数组类型，并且数组的元素类型为对象，也就是N的描述符会是类似“[Ljava/lang/Iteger”的形式，那将会按照第一点的规则加载数组元素类型。加载完而后由虚拟机生成一个代表此数组未读和元素的数组对象。
3. 如果上面的步骤没有出现任何异常，那么C在虚拟机中实际已经成为一个有效的类或接口了，但在解析完成之前还要进行符号引用验证，确认D是否具备对C的访问权限。如果发现不具备访问权限，将抛出java.lang.IllegalAccessError异常

*字段解析*

要解析一个未被解析过的字段符号引用，首先会对字段表内class_index项中索引的CONSTANT_Class_info符号进行解析。如果解析成功，那么将这个字段或接口用C表示，虚拟机按如下步骤搜索：

1. 如果C本身就包含了简单名称和字段描述符都与目标相匹配的字段，则放回这个字段的直接引用，查找结束。
2. 否则，如果C中实现了接口，那么将会按照继承关系从下往上搜索各个接口和它的父接口。
3. 否则，如果不是Object类，就按照继承关系从下往上递归搜索其父类。
4. 否则，查找失败，抛出java.lang.NoSuchFieldError异常。

如果查找成功返回了引用，将会对这个字段进行权限验证，如果发现不具备对字段的访问权限，将抛出java.lang.IllegalAccessError异常。

如果一个同名字段同时出现在C的接口和父类中，那编译器将可能拒绝编译。"The field Sub.A is ambiguous"

**类方法解析和接口方法解析是分开来的，与字段解析类似**

#### 初始化 ####
初始化是类加载过程的最后一步，除了在加载阶段用户应用程序可以通过自定义类加载器参与之外，其余动作完全是由虚拟机主导和控制。到了初始化阶段，才真正开始执行类中定义的Java程序代码(或者说是字节码)。

在准备阶段变量已经赋值过一次系统要求的初始值，而在初始化阶段，则根据程序员通过程序制定的主观计划去执行类构造器<clinit>()方法的过程。

- <clinit>()方法时由编译器自动收集类中的所有类变量的赋值动作和静态语句块(static{}块)中的语句合并产生的，编译器收集的顺序是由语句在源文件中出现的顺序所决定的，静态语句块中只能访问到定义在静态语句块之前的变量，定义在它之后的变量，**在前面的静态语句块可以复制，但不能访问**。
- <clinit>()方法与类构造函数(<init>方法)不同，它不需要显示地调用父类构造器，虚拟机会保证子类的<clinit>()方法执行之前，父类的<clinit>()方法已经执行完毕，因此在虚拟机中第一个被执行的<clinit>()方法肯定是java.lang.Object
- 由于父类的<clinit>()会先执行，这就意味着父类中定义的静态语句块要优先于子类的变量赋值。
- <clinit>()方法对于类或接口来说并不是必须的，如果一个类中没有静态语句块，也没有对变量的赋值操作，那么编译器可以不为这个类生成<clinit>()方法。
- 接口中不能使用静态语句块，但仍然有变量的赋值操作，因此接口会生成<clinit>()方法，但接口与类不同的是，执行接口的<clinit>()方法不需要先执行父接口的<clinit>()方法。只有当父接口中定义的变量使用的时候，父接口才会初始化。另外实现类的初始化时，也一样不会执行接口的<clinit>()方法。
- 虚拟机保证一个类的<clinit>()方法在多线程环境中被正确地加锁、同步，如果多线程同时去初始化一个类，那么只有一个线程去执行这个类的<clinit>方法，其他线程都需要阻塞等待，知道活动线程执行<clinit>()方法完毕。如果一个类的初始化方法有耗时很久的操作，就可能造成多个进程阻塞。

### 类加载器 ###
虚拟机设计团队把类加载阶段中的“通过一个类的全限定名来获取描述此类的二进制字节流”这个动作放到Java虚拟机外部去实现，以便让应用程序自己决定如何去获取所需要的类。实现这个动作的模块成为“类加载器”

它最初是为了满足Java Applet的需求而别开发出来的，虽然Java Applet基本上已经没用了，但是类加载器却在类层次划分、OSGi、热部署、代码加密等领域大放异彩，成为Java技术体系中的一块重要的基石。

#### 类与类加载器 ####
类加载器虽然只用于实现类的加载动作，但它在Java程序中起到的作用却远远不限于类加载阶段。对于任何一个类，都需要由加载它的类加载器和这个类本身一同确立其在Java虚拟机中的唯一性，每一个类加载器，都需要拥有一个独立的类命名空间。**比较两个类是否相等，只有这两个类是由同一个类加载器加载的前提下才有意义。**

这里的相等包括Class对象的equals()方法、isAssignableFrom()方法、isInstance()方法的返回结果，也包括使用instanceof关键字做对象所属关系判定等情况。

#### 双亲委派模型 ####
从Java虚拟机的角度来讲，只存在两种不同的类加载器：一种是启动类加载器(Bootstrap ClassLoader),这个类加载器使用C++语言实现，是虚拟机自身的一部分；另一种就是所有其他的类加载器，这些类加载器都由Java语言实现，独立于虚拟机外部，并且全部继承自抽象类java.lang.ClassLoader

从Java开发人员的角度来看，类加载器还可以划分得更细致一些，绝大部分Java程序都会使用到以下3种系统提供的类加载器。

- 启动类加载器(Bootstrap ClassLoader):前面已经介绍过，这个类加载器负责将存在<JAVA_HOME>\lib目录汇总，或者被-Xbootclasspath参数所指定的路径中，并且是被虚拟机识别的(仅按照文件名识别，如rt.jar,名字不符合的类库即使被放在lib目录中也不会被加载)类库加载到虚拟机内存中。启动类加载器无法被Java程序直接引用，用户再编写自定义类加载器时，如果需要把加载请求委托给引导类加载器，那直接使用null代替即可。
- 扩展类加载器(Extension ClassLoader):这个加载器由sun.misc.Launcher$ExtClassLoader实现，它负责加载<JAVA_HOME>\lib\ext目录中的，或者被java.ext.dirs系统变量所指定的路径中的所有类库，开发者可用直接使用扩展类加载器。
- 应用程序类加载器(Application ClassLoader):这个类加载器由sun.misc.Launcher$AppClassLoader实现。由于这个类加载器是ClassLoader中的getSystemClassLoader()方法的返回值，所以一般也称它为系统类加载器。它负责加载用户类路径(ClassPath)上所指定的类库，系统开发者可用直接使用这个类加载器。如果应用程序中没有定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzpjoupnl3j20ak0b7q4r.jpg)

双亲委派模式要求除了顶层的启动类加载器外，其余的类加载器都应当有自己的父类加载器。这里类加载器之间的父子关系一般都不会以继承的关系来实现，而是都使用组合关系来实现父类加载的代码。

双亲委派模式并不是一个强制性约束模型，而是Java设计者推荐给开发者的一种类加载器实现方式。

双亲委派模式的工作过程是:如果一个类加载器都到了类加载的请求，它首先不会自己去尝试加载这个类，而是把这个请求委派给父类加载器去完成，每一个层次的类加载器都是如果，因此所有的加载请求最终都应该换送到顶层的启动类加载器中，只有当父加载器反馈自己无法完成这个加载请求时，子加载器才会尝试自己去加载。

使用双亲委派模式来组织类加载器之间的关系，**有一个显而易见的好处就是Java类随着它的类加载器一起具备了一种带有优先级的层次顺序。**

即使自定义了自己的类加载器，强行用defineClass()方法区加载一个以"java.lang"开头的类也不会成功。如果尝试这样做的话，将会受到一个由虚拟机自己抛出的"java.lang.SecurityException:Prohibited package name:java.lang" 异常

#### 破坏双亲委派模式 ####

JDK1.2之后的java.lang.ClassLoader添加了一个新的protected方法findClass(),用户去继承java.lang.ClassLoader的唯一目的就是重写loadClass(),因为虚拟机在进行加载的时候会调用加载器的私有方法loadClassInternal(),而这个方法的唯一逻辑就是去调用自己的loadClass()。JDK之后已不再提倡用户去覆盖loadClass()方法，而应该把自己的类加载逻辑写到findClass方法中。这样，在loadClass的逻辑里如果父类加载失败，则会调用自己的findClass()方法来完成加载，这样就可以保证新写出来的类加载器是符合双亲委派规则的。

如果基础类又要调用回用户的代码，例如JNDI服务，它的代码由启动类加载器去加载，但JNDI的目的是对资源进行集中管理和查找，它需要调用由独立厂商实现并部署在应用程序的ClassPath下的JNDI接口提供者(SPI,Service Provider Interface)的代码，但启动类加载不可能“认识”这些代码。为了解决这个问题，Java设计团队加入一个不太优雅的设计:线程上下文类加载器(Thread Context ClassLoader)。这个类加载器可以通过java.lang.Thread类的setContextClassLoader()方法进行设置，如果创建线程时还未设置，它将会从父线程中继承一个，如果在应用程序的全局范围内都没有设置过的话，那这个类加载器默认就是应用程序类加载器。JNDI服务使用这个线程上下文加载去加载所需的SPI代码，也就是父类加载器请求子类加载器去完成类加载的动作。实际上已经违背了双亲委派模型的一般性原则，Java中所有涉及SPI的架子动作基本上都采用方式，例如JNDI、JDBC、JCE、JAXB、JBI等。

第三次“被破坏”是由于用户对程序动态性的追求而导致，动态性是指:代码热替换(HotSwap)、模块热部署(Hot Deployment)。

Sun所提出的JSR-294、JSR-277规范在与JCP组织的模块化规范之争中落败给JSR-291(即OSGi R4.2),虽然Sun不敢失去Java模块化的主导权，独立在发展Jigsaw项目，但OSGi已经成为了业界事实上的Java模块化标准，OSGi实现模块化热部署的关键是它自定义的类加载机制的实现。每一个模块程序(OSGi中成为Bundle)都有一个自己的类加载器，当需要更换一个Bundle时，就把Bundle连同类加载一起换掉以实现代码的热替换。

在OSGi环境中，类加载器不再是双亲委派模式中的树状结构，而是进一步发展为更加复杂的网状结构，当收到类加载请求时，OSGi将按照下面的顺序进行类搜索:

1. 将以java.*开头的类委派给父类加载器加载。
2. 否则,将委派列表名单内的类委派给父类加载器加载。
3. 否则，将Import列表中的类委派给Export这个类的Bundle的类加载器加载。
4. 否则，查找当前Bundle的ClassPath，使用自己的类加载器加载
5. 否则，查找类是否在自己的Fragment Bundle中，如果在，则委派给Fragment Bundle的类加载器加载。
6. 否则，查找Dynamic Import列表的Bundle，委派给对应Bundle的类加载器加载。
7. 否则，类查找失败。

以上的查找顺序只有开头两点仍然符合双亲委派规则，其余的类查找都是在平级的类加载器中进行的。

正如OSGi中的类加载器并不符合传统的双亲委派的类加载器，并且业界对其为了实现热部署而带来额外的搞复杂度还是存在不少争议，但Java程序员中基本有一个共识:OSGi对类加载器的使用时很值得学习的，弄懂了OSGi的实现，就可以算是掌握了类加载器的精髓。

## 虚拟机字节码执行引擎 ##
执行引擎是Java虚拟机最核心的组成部分之一。“虚拟机”是相对于“物理机”的概念，这两种机器都有代码执行能力，其区别是物理机的执行引擎是建立在处理器、硬件、指令集和操作系统层面上，而虚拟机的执行引擎则是由自己实现的，因此可以自行制定指令集与执行引擎的结构体系，并且能够执行哪些不被硬件直接支持的指令集格式。

在Java虚拟机规范中制定了虚拟机字节码执行引擎的概念模型，这个概念模型成为各种虚拟机执行引擎的统一外观(Facade)。在不同的虚拟机实现里，执行引擎在执行Java代码的时候可能有解释执行(通过解释器执行)和编译执行(通过即时编译器产生本地代码)两种选择，也可能两者兼容，设置还可能包含几个不同级别的编译器执行引擎。所有的Java虚拟机的执行引擎都是一致的：输入的是字节码，处理过程是字节码解析的等效过程，输出的是执行结果。

### 运行时栈帧结构 ###
栈帧(Stack Frame)是用于支持虚拟机进行方法调用和方法执行的数据结构，它是虚拟机运行时数据区中的虚拟机栈(Virtual Machine Stack)的栈元素。栈帧存储了方法的局部变量表、操作数栈、动态连接和方法返回地址等信息。每一个方法从调用开始至执行完成的过程，都对应着一个栈帧在虚拟机里面从入栈到出栈的过程。

每一个栈帧都包含额局部变量表、操作数栈、动态连接、方法返回地址和一些额外的附加信息。在编译程序代码的时候，詹振中需要多大的局部变量表，多深的操作数栈都已经完全确定了，并且写入到方法表的Code属性之中。因此一个栈帧需要分配多少内存，不会受到程序运行期变量的影响，仅仅取决于具体的虚拟机实现。

一个线程中的方法调用链可能会很长，很多方法都同时处于执行状态。对于执行引擎来说，在活动县城中，只有位于栈顶的栈帧才是有效的，成为当前栈帧(Current Stack Frame),与这个栈帧相关联的方法成为当前方法。执行引擎运行的所有字节码指令都只针对于当前栈帧进行操作。

**栈帧结构**

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzpq7bimwgj20ho0h3gor.jpg)

#### 局部变量表 ####
局部变量表(Local Variable Table)是一组变量值存储空间，用于存放参数和方法内部定义的局部变量。在Java程序便以为class文件时，就在方法的Code属性的max_locals数据项中确定了该方法所需要分配的局部变量表的最大容量。

局部变量表的容量以变量槽(Variable Slot)为最小单位，Slot的长度可以随着处理器、操作系统或虚拟机的不同而发生变化。

reference类型表示对一个对象实例的引用，虚拟机贵方既没有说明它的长度，也没有明确指出这种引用应有怎样的结构。但至少，从此引用中直接或间接地查找到对象在java堆中的数据存放的起始地址索引，二是此引用中直接或间接地查找到对象所属数据类型在方法区中的存储的类型信息，否则无法实现Java语言规范中定义的语法约束。

returnAddress类型已经很少见了，它是未jsr、jsr_w和ret服务的，指向一条字节码的地址。古老的Java虚拟机曾经使用这几条指令来实现异常处理，现在已经由异常表代替。

对于64位的数据类型，虚拟机以高位对齐的方式为其分配两个连续的Slot空间。由于局部变量表建立在线程的堆栈上，是私有的数据，无论读写两个连续的Slot是否为原子操作，都不会引起数据安全问题。

虚拟机通过索引定位的方式使用局部变量表。索引值的范围是从0开始至局部变量表最大的Slot数量。对于相邻的共同存放一个64位数据的两个Slot，不允许采用任何方式单独访问其中一个，遇到这种情况，虚拟机应该在类加载的校验阶段抛出异常。

在方法执行时，虚拟机是使用局部变量表完成参数值到参数变量列表传递过程的。对于实例方法，局部变量表的第0位索引的Slot默认是用于传递方法所属对象实例的引用，在方法中可以通过关键字“this”来访问这个隐含的参数，其余参数占用从1开始的局部变量Slot，参数表分配完毕后，再根据方法体内部定义的变量顺序和作用域分配其余的Slot。

为了尽可能节省栈帧控件，局部变量表中的Slot是可以重用的，方法体中定义的变量，其作用域并不一定会覆盖整个方法体，如果当前字节码PC计数器的值已经超出了某个变量的作用域，那么这个变量对应的Slot就可以交给其他变量使用。某些情况下，Slot的复用会直接影响到垃圾收集行为。

局部变量能否被回收的根本原因：局部变量表中的Slot是否还存有关于placeholder数组对象的引用。

**类变量即使即使在初始化阶段没有赋值也没有关系，类变量在准备阶段已经赋了默认值，但是一个局部变量定义了当没有赋初始值是不能使用的。**

#### 操作数栈 ####
操作数栈(Operand Stack)也成为操作数栈，它是一个LIFO栈。操作数栈的最大深度也是在编译的时候写入Code属性的max_stacks数据项。操作数栈的每一个元素可以是任意的Java数据类型。64位数据类型所占的栈容量为2。

当一个方法刚开始执行的时候，这个操作方法的操作数栈时空的，在方法的执行过程中，会有各种字节码指令往操作数栈中写入和提取内容，也就是出栈/入栈操作。

操作数栈中的元素的数据类型必须与字节码指令的序列严格匹配，在编译程序代码的时候，编译器要验证保证这一点，在类校验阶段的数据流分析中还要再一次验证这一点。

#### 动态连接 ####
每个栈帧都包含一个指向运行时常量池中该栈帧所属方法的引用，持有这个引用时为了支持调用过程中的动态连接(Dynamic Linking)。Class文件的常量池中存有大量的符号引用，字节码的方法调用指令就以常量池中指向方法的符号引用作为参数。这些符号引用一部分会在类加载阶段或者第一次使用的时候就转化为直接引用，这种转化成为静态解析。另外一部分会在每一次运行期间转化为直接引用，这部分成为动态连接。

#### 方法返回地址 ####
正常返回(Normal Method Invocation Completion)：执行引擎遇到任意一个方法返回的字节码指令，这时候可能会有返回值传递给上层的方法调用者，是否有返回值和返回值的类型将根据遇到何种方法返回指令来决定。

异常返回(Abrupt Method Invocation Completion)：且异常没有在方法体内得到处理。无论是Java虚拟机内部产生的异常，还是代码中使用athrow字节码指令产生的异常，只要在本方法的异常表中没有搜索到匹配的异常处理器，就会导致方法退出。

无论采用何种退出方式，在方法退出之后，都需要返回到方法被调用的位置。程序才能继续执行。方法放回时可能需要在詹振中保存一些信息，来帮助它恢复到它的上层方法的执行状态。一般来说，方法正常退出时，调用者的PC计数器的值可以作为返回地址，而异常退出时，返回地址是要通过异常处理表来确定的。

方法退出的过程实际上就等同于把当前栈帧出栈，因此退出时可能执行的操作有：恢复上层方法的局部变量表和操作数栈，把返回值压入调用者的桢栈的操作数栈中，调整PC计数器的值以指向方法调用指令后面的一条指令等。

#### 附加信息 ####
虚拟机规范允许具体的虚拟机实现增加一些规范没有描述的信息到栈帧之中，例如与调试相关的信息，这部分信息完全取决于具体的虚拟机实现。

### 方法调用 ###
方法调用并不等同于方法执行，方法调用阶段唯一的任务就是确定被调用的方法的版本(即调用哪一个方法)，暂时还不涉及方法内部的具体运行过程。在程序运行时，进行方法调用时最普遍、最频繁的操作，Class文件的编译过程中不包含传统编译中的连接步骤，一切方法调用再Class文件里存储的都只是符号引用，而不是方法在实际运行时内存布局汇总的入口地址。

#### 解析 ####
在类加载的解析阶段，会将其中一部分符号引用转化为直接引用，这种解析能成立的前提是：方法在程序真正运行之前就有一个可确定的调用版本，并且这个方法的调用版本在运行期是不可改变的。

在Java语言中符合“编译器，运行期不可变”这个要求的方法，主要包括静态方法和私有方法，前者与类型直接关联，后者在外部不可被访问，这两种方法各自的特点决定了它们都不可能通过继承或别的方式重写其他版本。因此它们都适合在类加载阶段进行解析。

与之对应的是，在Java虚拟机里提供了5条方法调用字节码指令，分别是

- invokestatic:调用静态方法
- invokespecial:调用实例构造器<init>方法、私有方法和父类方法
- invokevirtual:调用所有的虚方法
- invokeinterface:调用接口方法，会在运行时再确定一个实现此接口的对象
- invokedynamic:先在运行时动态解析出调用点限定符所引用的方法，再执行该方法，在此之前的4条调用指令，分派逻辑是固化在Java虚拟机内部的，而invokedynamic指令的分派逻辑是由用户所审定的引导方法决定的。

**只要能被invokestatic和invokespecial指令调用的方法，都可以在解析阶段确定唯一的调用版本，符合这个条件的有静态方法、私有方法、实例构造器、父类方法4类**，它们在类加载的时候就会把符号引用解析为该方法的直接引用。这些方法成为非虚方法，与之相反，其他方法成为虚方法(除去final方法)

**final方法时一种非虚方法**

解析调用时一个静态的过程，在编译期间就完全确定，在类装载的解析阶段就会把涉及的符号引用全部转变为可确定的直接引用，不会延迟到运行期再去完成。而分派(Dispatch)调用则可能是静态的也可能是动态的，根据分派依据的总宗数量可分为单分派和多分派。这两种分派又可以两两组合就构成了静态但分派、静态多分派、动态但分派、动态多分派。

#### 派分 ####
*静态派分(Method Overload Resolution)*

	Human man = new Man();

把 Human成为变量的静态类型(Static)，或者叫外观类型(Apparent Type)，后面的 Man则称为变量的实际类型(Actual Type), 静态类型和实际类型在程序都可以发生一些变化，区别是静态类型的变化仅仅在使用时发生，变量本身的静态类型不会被改变，并且最终的静态类型是在编译器可知；而实际类型变化的结果是在运行期才可以确定的，编译器在编译程序的时候并不知道一个对象的实际类型是什么。

![](http://ww1.sinaimg.cn/large/48ceb85dly1fzpvzjortlj207m052gm4.jpg)

虚拟机在**重载时**是通过参数的静态类型而不是实际类型作为判断依据的。并且静态类型是编译器可知的。因此，在编译阶段，Javac编译器会根据参数的静态类型决定使用哪个重载版本。所以选择了调用目标后会把这个方法的符号引用写到main()方法里的两条invokevirtual指令的参数中。

**所有依赖静态类型来定位方法执行版本的派分动作成为静态派分。静态拍飞的典型应用是方法重载**。Java编译器虽然能确定方法的重载版本，DNA很多版本的并非“唯一的”，往往只能确定一个“更加适合的”版本。

char类型可以的转换

char->int->long->float->double 顺序转型

char->Character->Serializable 实现了接口的转型

Character还实现了java.lang.Comparable<Character>,如果同时出现两个参数分别为Serializable和Comparable<Character>的重载方法，那么它们在此时的优先级是一样的，编译器无法确定要自动转型成哪种类型，会提示类型模糊，拒绝编译。变长参数的重载优先级是最低的。有些在单个参数中能成立的自动转型，如char转型成int，在变长参数中是不成立的。

**无论对重载的认识有多么深刻，一个合格的程序员都不应该在实际应用中写出如此极端的重载代码。**

*动态派分*

它和多态性的另一个重要体现————重写(Override)有着很密切的关联。

同一个类性的invokevirtual指令最终执行的目标方法并不相同，原因就需要从invokevirtual指令的多态查找过程开始说起，invokevirtual指令的运行时解析过程大致分为以下步骤：

1. 找到操作数栈顶的第一个元素指向的对象的实例类型，记作C。
2. 如果类型C中查找到与变量中的描述符和简单名称都相符的方法，则进行访问权限校验，如果通过则返回这个方法的直接引用，查找过程结束；如果不通过，则返回java.lang.IllegalAccessError异常。
3. 否则，按照继承关系从下往上依次对C的各个父类进行第二步的搜索和验证过程。
4. 如果始终没有找到合适的方法，则抛出java.lang.AbstractMethodError异常。

由于invokevirtual指令执行的第一步就是在运行期确定接受者的实际类型，所以两次 调用用中的invokevirtual指令把常量池中的类方法符号引用解析到了不同的直接引用上，这个就是Java语言中方法重写的本质。这种在运行期根据是积累性确定方法执行版本的派分过程称为动态派生。

*单派分与多派分*

方法的接受者与方法的参数统称为方法的宗量。根据派分基于多少种宗量，可以将派分划分为单派分和多派分。单派分是一个宗量对目标方法进行选择，多派分则是根据多于一个宗量对目标方法进行选择。

编译器选择的过程，也就是静态派分的过程。运行阶段虚拟机的选择，也就是动态派分的过程。

静态类型的重载方法，是根据两个宗量进行选择，所以Java语言的静态类型是属于多派分类型

运行时，虚拟机不会关心虚拟机的参数类型，参数的静态类型、实际类型都不会对方法产生影响，唯一可以影响虚拟机选择的因素只有方法的接受者的实际类型是Father还是Son。只有一个宗量作为选择依据，所以Java语言的动态派分属于单派分类型。

Java语言目前是一门静态多派分、动态但派分的语言。

(参数重载多派分？，继承结果，具体对象唯一，多派分？)

JDK1.7中实现的JSR-292里面已经开始提供对动态于洋的支持，JDK1.7中新增的invokedynamic指令也成为最复杂的一条方法指令。

*虚拟机动态派分的实现*

动态派分是非常频繁的动作，动态派分的按本选择过程需要运行时再数据中搜索合适的目标方法，因此虚拟机的实际实现中基于性能的考虑，大部分实现都不会真正进行如此频繁的搜索，最“稳定优化”的手段就是为类在方法区中一个虚方法表(Virtual Method Table,与此对应的，在invokeinterface执行时也会用到接口方法表————Interface Method Table,简称itable)，使用虚方法表索引来代替元数据查找以提高性能。

虚方法表中存放着各种方法的实际入口地址。如果某个方法在子类中宏没有被重写，那么子类的虚方法表里面的地址入口和父类相同的方法的地址入口是一致的，都指向父类的实现入口。如果子类中重写了这个方法，子类方法表中的地址将会替换为指向子类实现版本的入口地址。

为了程序实现上的方便，具有相同签名的方法，在父类、子类的虚方法表中都应当具有一样的索引序号，这样，当类型变换时，近需要更换查找的方法表，就可以从不同的虚方法表中按索引去除所需的入口地址。

方法表一般在类加载的连接阶段进行初始化，准备了类的变量初始值后，虚拟机会把该类的方法表也初始化完毕。

除了使用方法表之外，在条件允许的情况下，还是还会使用内联缓存(Inline Cache)和基于“类型继承关系分析”(Class Hierarchy Analysis,CHA)技术的守护内联(Guarded Inlining)两种非稳定的“激进优化”手段来获得更高的性能。

#### 动态类型语言的支持 ####

## 案例分析 ##
### Tomcat ###
主流的Java Web服务器，如Tomcat、Jetty、WebLogic、WebSphere等都实现了自己定义的类加载器(一般都不止一个)。一个功能健全的Web服务器，要解决如下几个问题：

- 部署在同一个服务器上的两个Web应用程序所使用的Java类库可以实现相互隔离。两个不同的应用程序可能会依赖同一个第三方库的不同版本，不能要求一个类库在一个服务器中只有一份，服务器应当保证两个应用程序的类库可以独立使用。
- 部署在同一个服务器的两个应用层序所使用的Java类库可以互相共享
- 服务器需要尽可能地保证自身的安全不受部署的Web应用程序影响。
- 支持JSP应用的Web服务器,大多数支持HotSwap功能

由于上述问题，在部署Web应用使，单独的一个ClassPath就无法满足需求了，所以各种Web服务器都“不约而同”地提供了好几个ClassPath路径供用户存放第三方类库。每一个目录都有一个相应的自定义类加载器去加载放置在里面的Java类库。

*Tomcat目录结构*

/common :类库可被Tomcat和所有的Web应用程序共同使用。
/server :类库可被Tomcat使用，对所有的Web应用程序共同使用
/shared :类库可被所有的Web应用程序共同使用，但对Tomcat自己不可见
/WebApp/WEB-INF：类库仅仅可以被此Web应用程序使用，对Tomcat和其他Web应用程序都不可见

为了支持这套目录结构，并对目录里的类库进行加载和隔离，Tomcat自定义了多个类加载器。

![](http://ww1.sinaimg.cn/mw690/48ceb85dly1g04ld0umnwj20fl0nan15.jpg)

从委派关系中可以看出CommonClassLoader能加载的类都可以背CatalinaClassLoader和SharedClassLoader使用，而CatalinaClassLoader和SharedClassLoader自己能加载的类则与对方相互隔离。WebAppClassLoader可以使用SharedClassLoader加载到的类，但是各个WebAppClassLoader实例之间相互隔离。而JasperLoader的加载范围仅仅是这个JSP文件所编译出来的那一个类，它的出现就是为了被抛弃：当服务器检测到JSP文件被修改时，就是替换到当前JasperLoader的实例，并通过再建立一个新的Jsp类加载器来实现JSP文件的HotSwap功能。

学习JEE规范，去看JBOSS源码；学习类加载器，去看OSGi源码。

*OSGi(Open Service Gateway Initiative)*

**简单例子**

Bundle A:声明发布packageA，依赖java.*的包
Bundle B:声明依赖了PackageA和packageC,同时也依赖java.*的包
Bundle C:声明发布了packageC，依赖了packageA。

类加载的规则：

1. 以java.*开头的类，委派给父类加载器加载
1. 否则，委派列表名单内的类，委派给父类加载器加载
1. 否则，Import列表中的类，委派给Export这个类的Bundle的类加载器加载。
1. 否则，查找当前Bundle的Classpath，使用自己的类加载器加载
1. 否则，查找是否在自己的Fragment Bundle中，如果是，则委派给Fragment Bundle的类加载器加载。
1. 否则，查找Dynamic Import列表的Bundle，委派给对应Bundle的类加载器加载，
1. 否则，类查找失败。

*字节码生成技术与动态代理的实现*

*Retrotranslator:跨越JDK版本*

一般来说，以“做项目”为主的软件公司比较容易更新技术，在下一个项目中换一个技术框架、升级到最新的JDK版本，设置把Java换成C#、C++来开发程序都是有可能的。但是当公司发展壮大，技术有所积累，逐渐成为一个以“做产品”为主的软件公司后，自主选择技术的权力就会丧失掉，因为之前所积累的代码和技术都是用真金白银换来的，一个稳健的团队也不会随意地改变底层的技术。

Java逆向移植工具(Java Backporting Tools)应运而生。

JDK每次升级新增的功能大致可以分为以下4类：

- 在编译器层面做的改进。如及自动装箱、变长参数、泛型
- 对Java API的代码增强
- 需要在字节码中进行支持的改动。 动态语言支持等
- 虚拟机内部的改进 Java内存模式、CMS收集器之类的改动

Retrotranslator只能模拟前两类。对于编译阶段进行的处理改进，Retrotranslator则使用ASM框架直接对字节码进行处理。

## 早期(编译期)优化 ##

三类编译器

- 前端编译器: Javac, Eclipse JDT中的增量式编译器(ECJ)
- JIT编译器: HotSpot VM的C1、C2编译器
- AOT编译器: GNU Compiler for the Java(GCJ)、Excelsior JET

Javac这类编译器对代码的运行效率几乎没有任何优化措施

许多新生的Java语法特性，都是靠编译器的“语法糖”来实现的，而不是依赖虚拟机的底层改进来支持。


### Javac编译器 ###
Javac编译器本身就是一个由Java语言编写的程序。

编译过程大致可以分为3个过程

- 解析与填充符号表过程
- 插入式注解器的注解处理过程
- 分析与字节码生成过程

#### 解析与填充符号表 ####
词法、语法分析
#### 注解处理器 ####

### Java 语法糖 ###
#### 泛型与类型擦除 ####
它的本质是参数化类型(Parametersized Type)的应用，也就是说所操作的数据类型被指定为一个参数。

C#里泛型无论在程序源码中、编译后的IL中(Intermediate Language,中间语言,这时候泛型是一个站位符)，或是运行期CLR中，都是切实存在的,List<Integer>与 List<String>就是两个不同的类型，它们在系统运行期生成，有自己的虚方法表和类型数据，这种实现称为类型膨胀，基于这种方法实现的泛型称为真实泛型。

Java语言中的泛型则不一样， 它只在程序源码中存在，在编译后的字节代码中，就已经替换为原来的原生类型(Raw Type,也成为 裸类型)了，并且在相应的地方插入了轻质类型转换，因此，对于运行期Java语言来说,ArrayList<Integer>和ArrayList<String>就是同一个类，所以泛型技术实际上是Java语言的一颗语法糖,Java语言中的泛型实现方法成为类型擦除，基于这种方法实现的泛型称为伪泛型。

方法重载要求方法具有不同的特征签名，而返回值并不包含在方法的特征签名之中，所以返回值不参与重载选择，但是在Class文件格式之中，只要描述符不是完全一致的两个方法就可以共存。就是说，两个方法如果有相同的名称和特征签名，但返回值不同，那它们也是可以合法共存于一个Class文件中的。

#### 自动装箱 ####
== equals 不会处理自动装箱
#### 条件编译 ####
使用条件为常量的if语句在编译阶段就会被运行。
## 晚期(运行期)优化 ##
#### 解释器与编译器 ####
许多主流商用虚拟机，都同时包含解释器与编译器。解释器与编译器两者各有优势:当程序需要迅速启动和执行的时候，解释器可以首先发挥作用，省去编译时间，立即执行。当程序运行后，随着时间的推移，编译器逐渐发挥作用，把越来越多的代码编译成本地代码之后，可以获取更高的执行效率。当程序运行环境中内存资源限制较大，可以使用解释执行节约内存，反之可以使用编译执行来提升效率。同时，解释器还可以作为编译器激进优化时的一个逃生门，让编译器根据概率选择一些大多数时候都能提升运行速度的优化手段，当激进优化的假设不成立，如加载了新类后类型继承结构出现变化、出现“罕见陷阱”时可以通过逆优化退回到解释状态继续执行，因此，在整个虚拟机执行架构中，解释器与编译器经常配合工作。
![](http://ww1.sinaimg.cn/large/48ceb85dly1g05xmg8te1j20ij076ac9.jpg)

虚拟机中可以使用参数 -Xint强制虚拟机运行于解释模式，也可以使用-Xcomp强制虚拟机运行于编译模式

分层编译根据编译器编译、优化的规模与耗时，划分出不同的编译层次，其中包括：

第0层，程序解释执行，解释器不开启性能监控(Profiling),可出发第1层编译。
第1层，也称为C1,将字节码编译为本地代码，进行简单可靠的优化，如有必要将加入性能监控的逻辑。
第2层(或2层以上),也称为C2编译，也是将字节码编译为本地代码，但是会启动一些编译耗时较长的优化，设置会根据性能监控信息进行一些不可靠的激进优化。

两类“热点代码”

- 被多次调用的方法
- 被多次执行的循环体

后一直情况，尽管编译动作是由循环体所触发的，但编译器依然会以整个方法(而不是单独的循环体)作为编译对象。这种编译方式因为编译发生在方法执行过程之中，因此形象地被称为栈上替换(On Stack Replacement,简称OSR编译，即方法栈帧还在栈上，方法就被替换了)

热点探测(Hot Spot Detection)主要有两种判定的方式：

基于采样的热点探测(Sample Based Hot Spot Detection):采用这种方法的虚拟机会周期性地检查各个线程的栈顶，如果发现某个(某些)方法经常出现在栈顶，那这个方法就是“热点方法”。基于采样的热点探测的好处是实现简单、高效，还可以很容易地获取方法调用关系(将调用堆栈展开即可),缺点是很难精确地确认一个方法的热度，容易因为受到线程阻塞或别的外界因素的影响而扰乱热点探测。

基于计算器的热点探测(Counter Based Hot Spot Detection):采用这种方法的虚拟机会为每个方法(设置是代码块)建立计数器，统计方法的执行次数，如果执行次数超过一定的阈值就认为它是“热点方法”。这种统计方法实现起来麻烦一些，需要为每个方法建立和维护计数器，而不能直接获取方法的调用关系，但是它的统计结果相对来说更加精确和严谨。

在HotSpot虚拟机中使用的是第二种——基于计数器的热点探测方法，因此它为每个方法准备了两类计数器:方法计数器(Invocation Counter)和回边计数器(Back Edge Counter)。这两个计数器都有一个确定的阈值，当计数器超过阈值溢出了，就会触发JIT编译。

方法计数器：默认阈值在Client模式下是1500次，在Server模式下是10000次，这个阈值可以通过-XX:CompileThreshold来人为设定。

如果不做任何设置，方法调用计数器统计的并不是方法被调用的绝对次数，而是一个相对执行频率，即一段时间之内方法被调用的次数。当超过一定的时间限度，如果方法的调用次数仍不足以让它提交给即时编译器编译，那这个方法的调用计数器就会被减少一半，这个过程称为方法调用计数器热度的衰减(Counter Decay),而这段时间被称为此方法统计的半衰期(Counter Half Life Time)。进行热度衰减的动作是在虚拟机进行垃圾收集时顺便进行的，可以使用虚拟机参数-XX:-UseCounterDecay来关闭热度衰减，让方法计数器统计方法的绝对调用次数。

汇编计数器是统计一个方法中循环体代码执行的次数。在字节码中遇到控制流向后跳转的指令成为“回边”(Back Edge)。显然建立回边计数器统计的目的就是为了触发OSR编译。

关于回边计数器的阈值，需要设置-XX:OnStackReplacePercentage来间接调整回边计数器的阈值。

Client模式编译器

在第一个阶段，一个平台独立的前端将字节码构造成一种高级中间代码(High-Level Intermediate Representation,HIR),HIR使用静态单分配(Static Single Assignment,SSA)的形式来代表代码值，这可以使得一些在HIR的构造过程之中和之后进行的优化动作更容易实现。在此之前编译器会在字节码上完成一部分基础优化，如方法内联、常量传播等优化将会在字节码被构造成HIR之前完成

第二个阶段，一个平台相关的后端从HIR中产生低级中间代码(Low-Level Intermediate Representation, LIR), 而在此之前会在HIR上完成一些优化，如控制检查扫描、范围检查消除，以便让HIR达到更高效的代码表示形式。

最后阶段是在平台相关的后端使用线性扫描算法(Linear Scan Register Allocation)在LIR上分配寄存器，并在LIR上所窥孔(Peephole)优化，然后产生机器代码。

![](http://ww1.sinaimg.cn/large/48ceb85dly1g05zln89ifj20id0a47ab.jpg)

Server Compiler会执行所有经典的优化动作，如无用代码消除(Dead Code Elimination)、循环展开(Loop Unrolling)、循环表达式外提(Loop Expression Hoisting)、消除公共子表达式(Common Subexpression Elimination)、常量传播(Constant Propagation)、基本块重排序(Basic Block Reordering)等，还是实施一些与Java语言特性密切相关的优化技术，如范围检查消除(Range Check elimination)、空置检查消除(Null Check Elimination,不过并非所有的空值检查都是依赖编译器优化的，有一些是在代码运行过程中自动优化了)等。另外还可根据解释器或Client Compiler提供的性能监控信息，进行一些不稳定的激进优化，如守护内联(Guarded Inlining)、分支频率预测(Branch Frequency Prediction)等。

Server Compiler的寄存器分配器是一个全局着色分配器，它可以充分利用某些处理器加过(如RISC)上的大寄存器集合。以即时编译的标准看，Server Compiler无疑是比较缓慢的，但是它的编译速度依然远远超过传统的静态优化编译器，而且它相对于Client Compiler编译输出的代码质量有所提供，可以减少本地代码的执行时间，从而抵消额外的编译时间开销。

#### 编译优化 ####
- 方法内联 Method Inlining：去除建立方法调用的成本
- 冗余访问消除 Redundant Loads Elimination
- 复写传播 Copy Propagation
- 无用代码消除 Dead Code Elimination

#### 公共子表达式消除 ####
Common Subexpression Elimination

如果一个表达式E已经计算过了，并且从前的计算到现在E中所有变量的值都没有发生变化，那么E的这次出现就成为公共子表达式。

#### 数组边界检查消除 ####
Array Bounds Checking Elimination

#### 方法内联 ####
如果遇到虚方法，会向CHA(Class Hierarchy Analysis, 类型继承关系分析)，查询此方法在当前程序下是否有多个目标版本可供选择，如果查询结果只有一个版本，那也可以进行内联，不过准则中内联属于激进优化，需要预留一个“逃生门”，成为守护内联如果虚拟机没有加载到会令这个方法的接受者的继承关系发生变化的类，那这个内联优化的代码就可以一直使用下去，如果加载了导致继承关系发生变化的新类，就需要抛弃已经编译的代码，退回到解释状态执行，或者重新进行编译。

如果存在多个版本的目标方法，使用内联缓存(Inline Cache),在未发生方法调用之前，内联缓存状态为空， 当第一次调用发生后，魂村记录下方法接收者的版本信息，并且每次进行方法调用时都直接比较接收者版本，如果以后每次进来的调用的方法的接受者版本都是一样的，那这个内联还可以一直用下去，如果发生了接受者不一致的情况，就说明程序真正使用了了虚方法的多态特性，这时才会取消内联，查找虚方法表进行派分。

#### 逃逸分析 ####
Escape Analysis

当一个对象在方法中被定义后，它可能被外部方法所引用，例如作为调用参数被传递到其他方法中，成为方法逃逸。甚至还有可能被外部线程访问到，譬如复制给类变量或可以到其他线程中访问的实例变量，成为线程逃逸。如果证明一个对象不会逃逸到方法或线程之外则可能对这个变量进行一些高效的优化。

栈上分配 Stack Allocation：大量对象就会随着方法的结束而自动销毁，减少GC压力
同步消除 Synchronization Elimination: 线程同步本身是一个相对耗时的过程，如果逃逸分析能够确定一个变量不会逃逸出线程，无法被其他线程访问，那这个变量的读写就肯定不会有竞争，对这个变量的同步措施可以被消除
标量替换 Scalar Replacement: 如果一个对象不会被外部访问，并且这个对象可以被拆散的话，那程序真正执行的时候可能不创建这个对象，而改为直接创建它的若干个被这个方法使用到的变量成员代替。
## Java内存模型 ##
基于告诉缓存的存储交换很好的解决了处理器与内存的速度矛盾，但是也为计算机系统带来更高的复杂度，因为它引入了一个新的问题：缓存一致性(Cache Coherence)。

Java内存模型规定了所有变量都存储在主内存(Main Memory)。每条线程还有自己的工作内存(Working Memory,可与前面讲的处理器高速缓存类比)，线程的工作内存中保存了被该线程使用到的变量的主内存副本拷贝，线程对变量的所有操作(读取、赋值等)都必须在工作内存中进行，而不能直接读写主内存中的变量。不同的线程之间也无法直接访问对方工作内存中的变量，线程变量值的传递均需要通过主内存来完成。

![](http://ww1.sinaimg.cn/large/48ceb85dly1g062kg5esaj20eg05twfm.jpg)

#### 内存间交互操作 ####
关于主内存与工作内存之间具体的交互协议，即一个变量如何从主内存拷贝到工作内存、如何从工作内存回到主内存之类的操作细节，Java内存模型定义了一下8种操作来完成。虚拟机实现时必须保证下面提及的每一种操作都是原子的、不可再分的。

lock 锁定:作用于主内存的变量，它把一个变量标识为一条线程独占的状态。
unlock 解锁:作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定。
read 读取 :作用于主内存的变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用。
load 载入：作用于工作内存的变量，它把read操作从主内存中得到的变量放入工作内存的变量副本中。
use 使用:作用于工作内存的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用执行变量值的字节码指令时会执行这个操作
assign 复制：作用于工作内存的变量，它把一个从执行引擎接收到的值赋给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作
sotre 存储：作用于工作内存的变量，它把一个工作内存中的一个变量传递传送给主内存，以便随后的write操作使用
write写入：作用于主内存的变量，它把store操作从工作内存得到的变量的值放入主内存的变量中。

**read与load之间、store与write之间可插入其他指令，但必须顺序**

Java内存模型还规定了执行上述8种基本操作时必须满足如下规则：

- 不允许read和load、store和write操作之一单独出现，即不允许一个变量从主内存读取了但工作内存不接受，或者从工作内存发起回写了但主内存不接受的情况出现。
- 不允许一个线程丢弃它的最近的assign操作，即变量在工作内存中改变了之后必须把该变化同步回主内存。
- 不允许一个线程无原因地(没有发生过任何assign操作)把数据从线程的工作内存同步回主内存中。
- 一个新的变量只能咋主内存中“诞生”，不允许在工作内存中直接使用一个未被初始化(load或assign)的变量，换句话说对一个变量实施use、store操作之前，必须先执行过assign和load操作
- 一个变量在同一时刻只允许一条线程对其进行lock操作，但lock操作可以背同一条线程重复执行多次，多次执行lock后，只有执行相同次数的unlock操作，变量才会被解锁。
- 如果对一个变量执行lock操作，那将会清空工作内存中此变量的值，在执行使用这个变量前，需要重新执行或assign操作初始化变量的值。
- 如果一个变量事先没有被lock操作锁定，那就不允许对它执行unlock操作，也不允许去unlock一个呗其他线程锁定住的变量。
- 对一个变量执行unlock操作之前，必须先将此变量同步回主内存中(执行store、write操作)

#### 对volatile型变量的特殊规则 ####
volatile具备两种特性，第一是保证此变量对所有线程的可见性，这里的可见性是指当一条线程修改了这个变量的值，新值对于其他线程来说是可以立即得知的。**Java里的运算并非原子操作，导致volatile变量的运算在并发下是不安全的。**

用字节码来分析并发问题，仍然是不严谨的，因为即使编译出来只有一条字节码指令，也并不意味着这条指令就是一个原子操作。

使用volitale变量的第二个语义是禁止指令重排序优化。

lock addl $0x0,(%esp)操作

相当于对Cache中的变量作了一次 store和write操作，通过这样一个空操作，可让前面的volatile变量的修改对其他CPU立即可见。

lock addl $0x0,(%esp)指令把修改同步到内存时，意味着之前所有的操作都已经执行完成。

如果A优先于B，那么前驱行为P先于Q(防制重排序)

#### 原子性、可见性和有序性 ####
虚拟机提供monitorenter和monitorexit来隐式使用 lock和unlock操作，反映到Java代码中就是synchronized关键字。

Java语言提供了volatiel和synchronized两个关键字来保证线程之间操作的有序性，volatile关键字本身就包含了禁止指令重排序的语义，而synchronized则由“一个变量在同一时刻只允许一个线程对其进行lock操作”这条规则获得，这条规则决定了持有同一个锁的两个同步快只能串行地进入。

#### 先发现行原则 ####
先行发生时Java内存模型中定义的两项操作之间的偏序关系，如果操作A先行发生于操作B，其实就是说发生操作B之前，操作A产生的影响能被B观察到，“影响”包括修改了内存中共享的值、发送了消息、调用了方法。

- 程序次序规则 Program Order Rule
- 管程锁定规则 Monitor Lock Rule
- volatile变量规则 Volatile Variable Rule
- 线程启动规则 Thread Start Rule
- 线程终止规则 Thread Termination Rule
- 线程中断规则 Thread Interruption Rule
- 对象中介规则 Finalizer Rule
- 传递性 Transitivity

无需任何同步手段保障就能成立的先行规则

#### 线程的实现 ####
1. 使用内核线程实现
2. 使用用户线程实现
3. 使用用户线程加轻量级进程混合实现

![](http://ww1.sinaimg.cn/large/48ceb85dly1g06xl11ad3j20ci07rmyb.jpg)

#### 互斥同步 ####
Java中最基本的互斥同步手段就是synchronized关键字，synchronized关键字经过编译之后会在同步块的前后形成monitorenter和monitorexit这两个字节码指令，这两个字节码都需要一个reference类型的参数来指明要锁定和解锁的对象。如果Java程序中的synchronized明确指定了对象参数，那就是这个对象的reference；如果没有明确指定，那就是根据修饰的实例方法还是类方法，去取对应的对象实例或Class对象来作为锁对象。

synchronized同步块对同一条线程来说是可重入的，不会自己把自己锁死，其次，同步块在已经入的线程完成之前，会阻塞后面其他线程的进入。Java的线程时映射到操作系统的原生线程之上，如果要阻塞或唤醒一个线程，都需要操作系统来帮忙完成，这就要从用户态切换到核心态中，因此状态转换需要耗费很多的处理器事件，对于代码简单的同步块，状态转换消耗的事件有可能比用户代码执行的事件还要长。

#### 非阻塞同步 ####
- 测试并设置 (Test-and-Set)
- 获取并交换 (Fetch-and-Increment)
- 交换 (Swap)
- 比较并交换 (Compare-and-Swap)
- 加载链接/条件存储(Load-Linked/Store-Condition)
#### 无同步方案 ####
- 可重入代码
- 线程本地存储

### 锁优化 ###
#### 自旋锁和自适应自旋 ####

让请求锁的线程“稍等一下”，但不会放弃处理器时间，减少线程切换-XX:PreBlockSpin 更改自旋次数

#### 锁消除 ####
锁消除的主要判定来源于逃逸分析

#### 锁粗化 ####
例如append()

#### 轻量级锁 ####
HotSpot虚拟机的对象头(Object Header)分为两部分信息，第一部分用于存储对象自身的运行时数据，包括哈希码、GC年龄分代，官方成为Mark Word，是实现轻量级锁和偏向锁的关键。另一部分用于存储指向方法区对象类型数据的指针，如果是数组对象的话，还会有一个额外的部分用于存储数组对象。

如果同步对象没有被锁定，虚拟机将会在当前线程的栈帧上建立一个名为锁记录(Lock Record)的控件，用于存储当前对象的Mark Word的拷贝。然后虚拟机将使用CAS操作将对象的Mark Word更新为指向Lock Record的指针，如果更新成功，那么这个线程就拥有了该对象的锁，并且对象Mark Word的锁标志位将变为“00”，表示此对象处于轻量级锁定状态。如果轻量级锁不再有效，则膨胀为重量级锁，后面等待的线程也要进入阻塞状态。

轻量级锁能提升同步性能的依据是“对于绝大部分的锁，在整个同步周期内都是不存在竞争的”，这是一个经验数据。如果没有竞争，轻量级锁使用CAS操作避免了互斥量的开销。如果存在锁竞争，轻量级锁会比传统的重量级锁更慢。

#### 偏向锁 ####
假设当前虚拟机启动了偏向锁，那么锁对象第一次被线程获取的时候，虚拟机会把对象头的标志位设为“01”，即偏向模式。同时使用CAS操作把获取到这个锁的线程的ID记录在对象的Mark Word之中。如果持有偏向锁的线程以后每次进入这个锁相关的同步块，那虚拟机都可以不再进行任何同步操作。当有另一个线程尝试获取这个锁时，偏向模式就宣告结束。

如果程序中大多数的锁总是被多个不同的线程访问，那偏向模式就是多余的。



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

# Java Web #
## JSP ##
JSP本身就是一个Servlet


### request ###
#### jsp中的request.getContextPath() ####
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	
	<%
	    String path = request.getContextPath();
	    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>

 <%=request.getContextPath()%>是为了解决相对路径的问题，可返回站点的根路径

**但在JSP文件里,通过request.getContextPath()得到的路径却为空,为什么?**

你在context中没有配置path属性，所以你的工程文件就是在根目录下，相当于path=""

**得到工程文件的实际物理路径，可通过：<%=request.getRealPath("/")%>**

request.getScheme();
返回的协议名称,默认是http

request.getServerName()
返回的是你浏览器中显示的主机名，你自己试一下就知道了

getServerPort()
获取服务器端口号

#### JSP页面设置全局变量 ####
	创建JSP页面common.jsp:
	
	<%@ page language="java" pageEncoding="UTF-8"%>
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<c:set var="cognosPath" value="http://10.103.117.11/cognos10/cgi-bin/cognos.cgi" />
	
	
	
	在需要的页面通过：<%@ include file="/adf/common/jsp/taglibs.jsp"%>引入全局页面；