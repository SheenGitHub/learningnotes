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














