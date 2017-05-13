# Swift - iOS 笔记 #

## swift常识 ##
> Swift里不会自动给变量赋初始值，也就是说变量不会有默认值，所以要求使用变量之前必须要对其初始化。如果在使用变量之前不进行初始化就会报错

Optional其实是个enum，里面有None和Some两种类型。其实所谓的nil就是Optional.None, 非nil就是Optional.Some, 然后会通过Some(T)包装（wrap）原始值，这也是为什么在使用Optional的时候要拆包（从enum里取出来原始值）的原因, 也是PlayGround会把Optional值显示为类似{Some "hello world"}的原因
    
    enum Optional<T> : LogicValue, Reflectable { 
	    case None 
	    case Some(T) 
	    init() 
	    init(_ some: T) 
	     
	    /// Allow use in a Boolean context. 
	    func getLogicValue() -> Bool 
	     
	    /// Haskell's fmap, which was mis-named 
	    func map<U>(f: (T) -> U) -> U? 
	    func getMirror() -> Mirror 
    } 

### ? 使用的两种场景 ###

- 1.声明Optional值变量
- 2.用在对Optional值操作中，用来判断是否能响应后面的操作

这里的!表示“我确定这里的的strValue一定是非nil的，尽情调用吧” ，比如这种情况:

    if strValue { 
    	let hashValue = strValue!.hashValue 
    } 

{}里的strValue一定是非nil的，所以就能直接加上!，强制拆包(unwrap)并执行后面的操作。 当然如果不加判断，strValue不小心为nil的话，就会出错，crash掉。

### ! 大概也有两种使用场景 ###
 
- 1.强制对Optional值进行拆包(unwrap)
- 2.声明Implicitly Unwrapped Optionals值，一般用于类中的属性

补充：
Forced cast from 'Data?' to 'NSData' only unwraps and bridges;did you mean to use '!' with 'as'?
Fix-it Replace "as!" with "! as"!!

- ! as 拆包 再转换类型
- as! 强制类型转换

[AppDelegate作用](http://stackoverflow.com/questions/652460/what-is-the-appdelegate-for-and-how-do-i-know-when-to-use-it)
> A delegate object is an object that gets notified when the object to which it is connected reaches certain events or states. In this case, the Application Delegate is an object which receives notifications when the UIApplication object reaches certain states. In many respects, it is a specialized one-to-one Observer pattern.

**如何在Xcode中添加ViewController并且关联swift文件**
> File=>New=>File 选择Cocoa Touch Class ,Subclass选择UIViewController，建立swift文件
> 从控件中拖出一个ViewController,点击下图中第一个图标，在indentity inspector的customer Class中选择创建的Swift类
> 
> ![icon](http://ww1.sinaimg.cn/thumbnail/48ceb85dgy1fet9rsa0eaj204j025jr8.jpg)

URLSessionConfiguration
>An NSURLSessionConfiguration object defines the behavior and policies to use when uploading and downloading data using an URLSession object. When uploading or downloading data, creating a configuration object is always the first step you must take. You use this object to configure the timeout values, caching policies, connection requirements, and other types of information that you intend to use with your URLSession object.

URLSessionConfiguration.default
>Returns a newly created default session configuration object.

[UIScreen类](http://www.jianshu.com/p/cf7f7dddb60b)
>UIScreen对象定义了基于硬件显示的相关属性。iOS设备有一个主屏幕和零个或多个附带屏幕。tvOS设备有一个主屏幕，就是设备相连接的电视。使用这个类来获取每一个设备显示屏幕的对象。每一个屏幕对象定义了相关的边界矩形和其它有趣的属性，例如它的亮度。

 **tintColor**

 > tintColor 是 iOS7.0 引入的一个 UIView 的属性. 再来强调一下, tintColor 是 UIView 的属性!!! 虽然没有说三遍, 但这一点非常重要! 这就意味着, 所有的 UI 控件都有 tintColor 这个属性! 

- 继承
- 重写
- 传播
 
navigationController

pushViewController

[TableViewController](https://developer.apple.com/library/content/referencelibrary/GettingStarted/DevelopiOSAppsSwift/CreateATableView.html)

Xcode [iOS代码调试](http://blog.csdn.net/u012701023/article/details/47778789)

LogMessage

**NSString 和 String的差别**

[原文](http://www.hangge.com/blog/cache/detail_798.html)

> 能使用String类型就尽量使用String类型，原因如下：
> 
> （1）现在Cocoa里所有的API都能接受和返回String类型，所以也没必要特地转换下。
> 
> （2）Swift的String是struct，而NSString类是NSObject，所以String更符合字符串“不变”这一特性。同时，在不触及NSString特有操作和动态特性的时候，使用String的方法，在性能上也会有所提升。
> 
> （3）由于String实现了像CollectionType这样的接口，所以有些Swift的语法特性只有String才能使用，而NSString却没有。
> 比如for...in的枚举遍历所有字符
> 
%B会打印断点的名字，%H会打印断点的调用次数，@@中间可以输入表达式。

### 函数 ###
> 每个函数有个函数名，用来描述函数执行的任务。要使用一个函数时，你用函数名“调用”，并传给它匹配的输入值（称
> 作实参，arguments）。一个函数的实参必须与函数参数表里参数的顺序一致。

**函数返回值**

> **没有定义返回类型的函数会返回特
> 殊的值，叫 Void 。它其实是一个空的元组（tuple），没有任何元素，可以写成 ()**
> 
> **返回值可以被忽略，但定义了有返回值的函数必须返回一个值，如果在函数定义底部没有返回任何值，这
> 将导致编译错误（compile-time error）。**
> 
> **你可以用元组（tuple）类型让多个值作为一个复合值从函数中返回。**


所有的函数都给它们的参数定义了 参数名（parameter name） ：
但是，这些参数名仅在函数体中使用，不能在函数调用时使用。这种类型的参数名被称作 局部参数名（local parameter
name） ，因为它们只能在函数体中使用。

**外部参数名（External Parameter Names）**

调用函数时，给每个参数命名是非常有用的，因为这些参数名可以指出各个实参的用途是什么。
> 注意： 当其他人在第一次读你的代码，函数参数的意图显得不明显时，考虑使用外部参数名。如果函数参数名的
> 意图是很明显的，那就不需要定义外部参数名了。

**简写外部参数名（Shorthand External Parameter Names）**

如果你需要提供外部参数名，但是局部参数名已经定义好了，那么你不需要写两次参数名。相反，只写一次参数名，并
用 井号（#） 作为前缀就可以了。这告诉 Swift 使用这个参数名作为局部和外部参数名。

**默认值**

你可以在函数体中为每个参数定义 默认值 。当默认值被定义后，调用这个函数时可以忽略这个参数。

> 注意： 将带有默认值的参数放在函数参数列表的最后。这样可以保证在函数调用时，非默认参数的顺序是一致
> 的，同时使得相同的函数在不同情况下调用时显得更为清晰。

**默认值参数的外部参数名** （External Names for Parameters with Default
Values）
> 为了使定义外部参数名更加简单，当你未给带默认值的参数提供外部参数名时，Swift 会自动提供外部名字。此时外部
> 参数名与局部名字是一样的，就像你已经在局部参数名前写了 井号（#） 一样。

注意： 你可以使用 下划线（_） 作为默认值参数的外部参数名，这样可以在调用时不用提供外部参数名。但是给带
默认值的参数命名总是更加合适的。

**可变参数**
> 注意： 一个函数至多能有一个可变参数，而且它必须是参数表中最后的一个。这样做是为了避免函数调用时出现
> 歧义。

注意： 对变量参数所进行的修改在函数调用结束后便消失了，并且对于函数体外是不可见的。变量参数仅仅存在
于函数调用的生命周期中。

**输入输出参数（In-Out Parameters）**
> 变量参数，正如上面所述，仅仅能在函数体内被更改。如果你想要一个函数可以修改参数的值，并且想要在这些修改在
> 函数调用结束后仍然存在，那么就应该把这个参数定义为输入输出参数（In-Out Parameters）。
> 输入输出参数是函数对函数体外产生影响的另一种方式。

#### 函数类型（Function Types） ####
每个函数都有种特定的函数类型，由函数的参数类型和返回类型组成。

#### 嵌套函数（Nested Functions） ####
可以把函数定义在别的函数
体中，称作嵌套函数（nested functions）。

默认情况下，嵌套函数是对外界不可见的，但是可以被他们封闭函数（enclosing function）来调用。一个封闭函数也可
以返回它的某一个嵌套函数，使得这个函数可以在其他域中被使用。

    func chooseStepFunction(backwards: Bool) -(Int) -Int {
	    func stepForward(input: Int) -Int { return input + 1 }
	    func stepBackward(input: Int) -Int { return input - 1 }
	    return backwards ? stepBackward : stepForward
    }
    var currentValue = -4
    let moveNearerToZero = chooseStepFunction(currentValue < 0)
    // moveNearerToZero now refers to the nested stepForward() function
    while currentValue != 0 {
	    println("\(currentValue)... ")
	    currentValue = moveNearerToZero(currentValue)
    }
    println("zero!")
    // -4...
    // -3...
    // -2...
    // -1...
    // zero!
    
### 闭包（Closures） ###
> 闭包是自包含的函数代码块，可以在代码中被传递和使用

闭包采取如下三种形式之一：

- 全局函数是一个有名字但不会捕获任何值的闭包
- 嵌套函数是一个有名字并可以捕获其封闭函数域内值的闭包
- 闭包表达式是一个利用轻量级语法所写的可以捕获其上下文中变量或常量值的匿名闭包

Swift 的闭包表达式拥有简洁的风格，并鼓励在常见场景中进行语法优化，主要优化如下：

- 利用上下文推断参数和返回值类型
- 隐式返回单表达式闭包，即单表达式闭包可以省略 return 关键字
- 参数名称缩写
- 尾随（Trailing）闭包语法

#### 闭包表达式 ####
闭包表达式是一种利用简洁语法构建内联闭包的方式。 闭包表达式提供了一些语法优化，使得撰写闭包变得简单明了。

    { (parameters) -> returnType in
    	statements
    }

根据上下文推断类型

    reversed = sorted(names, { s1, s2 in return s1 > s2 } )

单表达式闭包隐式返回

    reversed = sorted(names, { s1, s2 in s1 > s2 } )

参数名称缩写

    reversed = sorted(names, { $0 > $1 } )

运算符函数

    reversed = sorted(names, >)


尾随闭包（Trailing Closures）

    func someFunctionThatTakesAClosure(closure: () -> ()) {
    	// 函数体部分
    }
    	// 以下是不使用尾随闭包进行函数调用
    someFunctionThatTakesAClosure({
    	// 闭包主体部分
    })
   	 // 以下是使用尾随闭包进行函数调用
    someFunctionThatTakesAClosure() {
    	// 闭包主体部分
    }

#### 捕获值（Capturing Values） ####
> 注意： Swift 会决定捕获引用还是拷贝值。 您不需要标注 amount 或者 runningTotal 来声明在嵌入的 incrementor 函
> 数中的使用方式。 Swift 同时也处理 runingTotal 变量的内存管理操作，如果不再被 incrementor 函数使用，则会被
> 清除。

强引用环

> 注意： 如果您将闭包赋值给一个类实例的属性，并且该闭包通过指向该实例或其成员来捕获了该实例，您将创建
> 一个在闭包和实例间的强引用环。 Swift 使用捕获列表来打破这种强引用环

**闭包是引用类型（Closures Are Reference Types）**

无论您将函数/闭包赋值给一个常量还是变量，您实际上都是将常量/变量的值设置为对应函数/闭包的引用。 上面的例子
中， incrementByTen 指向闭包的引用是一个常量，而并非闭包内容本身。

这也意味着如果您将闭包赋值给了两个不同的常量/变量，两个值都会指向同一个闭包：

    let alsoIncrementByTen = incrementByTen
    alsoIncrementByTen()
    // 返回的值为50



#### 逃逸闭包（Escaping Closure） ####

首先你需要理解什么是逃逸闭包。它的定义非常简单而且易于理解。

> 如果一个闭包被作为一个参数传递给一个函数，并且在函数return之后才被唤起执行，那么这个闭包是逃逸闭包。并且这个闭包的参数是可以“逃出”这个函数体外的。

#### @escaping的含义 ####

这个提醒我们去理解@escaping属性的含义。因为在swift3中闭包默认是不可逃逸的，逃逸闭包需要像这样被标记。@escaping属性让我们可以那样做。

我们通过@escaping属性标记闭包，编译错误就消失了。

**重点**

这里几点关于创建默认不可逃逸闭包的好处： 最明显的好处就是编译器优化你的代码的性能和能力。如果编译器知道这个闭包是不可逃逸的，它可以关注内存管理的关键细节。

而且你可以在不可逃逸闭包里放心的使用self关键字，因为这个闭包总是在函数return之前执行，你不需要去使用一个弱引用去引用self.这对你而言是一个非常nice的功能。

Nil 和 Null
问题描述:
TableView顶部的TextField被NavigationItem遮挡住
> nil用来给对象赋值（Objective-C中的任何对象都属于id类型），NULL则给任何指针赋值，NULL和nil不能互换，nil用于类指针赋值（在Objective-C中类是一个对象，是类的meta-class的实例）， 而NSNull则用于集合操作，虽然它们表示的都是空值，但使用的场合完全不同。

> //json返回的null值 EduExper = "<null>";if JSON["EduExper"] is NSNull{    print("yes")}else{    print("no")}


### 枚举 ###
> 原始值和相关值是不相同的。当你开始在你的代码中定义枚举的时候原始值是被预先填充的值，像上述三个
> ASCII 码。对于一个特定的枚举成员，它的原始值始终是相同的。相关值是当你在创建一个基于枚举成员的新常量或变
> 量时才会被设置，并且每次当你这么做得时候，它的值可以是不同的。

**相关值（Associated Values）**

    enum Barcode {
    case UPCA(Int, Int, Int)
    case QRCode(String)
    }

使用 

    switch productBarcode {
	    case .UPCA(let numberSystem, let identifier, let check):
	    	println("UPC-A with value of \(numberSystem), \(identifier), \(check).")
	    case .QRCode(let productCode):
	    	println("QR code with value of \(productCode).")
    }
    // 输出 "QR code with value of ABCDEFGHIJKLMNOP.”

### swift 访问权限 ###

#### fileprivate ####
在swift 3中，新增加了一个 fileprivate来显式的表明，这个元素的访问权限为文件内私有。过去的private对应现在的fileprivate。现在的private则是真正的私有，离开了这个类或者结构体的作用域外面就无法访问。

> 在原有的swift中的 private其实并不是真正的私有，如果一个变量定义为private，在同一个文件中的其他类依然是可以访问到的。这个场景在使用extension的时候很明显。

#### open ####
open则是弥补public语义上的不足。<br>
现在的pubic有两层含义：

- 这个元素可以在其他作用域被访问
- 这个元素可以在其他作用域被继承或者override


 final的问题在于在标记之后，在任何地方都不能override。而对于lib的设计者而言，希望得到的是在module内可以被override，在被import到其他地方后其他用户使用的时候不能被override。

这就是 open产生的初衷。通过open和public标记区别一个元素在其他module中是只能被访问还是可以被override。

open > public > interal > fileprivate > private

### dynamic 关键字 ###

Swift中也有dynamic关键字，它可以用于修饰变量或函数，它的意思也与OC完全不同。它告诉编译器使用动态分发而不是静态分发。OC区别于其他语言的一个特点在于它的动态性，任何方法调用实际上都是消息分发，而Swift则尽可能做到__静态分发__。

因此，标记为dynamic的变量/函数会隐式的加上@objc关键字，它会使用OC的runtime机制。



Objective-C 中的所谓函数调用，其实是发送消息。<br>
> 比如 [obj domething:arg1];<br>
> 会被编译成objc_msgSend(obj, @selector(something:), arg1);<br>
> 从字符串"domething:"动态查找到对应的方法地址。


Objective-C 的消息发送是完全动态的，动态特性对于构建UI库很有用。（用静态的语言比如 C++, C 之类去做 UI 库会难得多）。

但是 Swift 不同，Swift 中的函数可以是静态调用，静态调用会更快。

**当函数是静态调用的时候，就不能从字符串查找到对应的方法地址了。**

这样 Swift 跟 Objective-C 交互时，Objective-C 动态查找方法地址，就有可能找不到 Swift 中定义的方法。<br>
**这样就需要在 Swift 中添加一个提示关键字，告诉编译器这个方法是可能被动态调用的，需要将其添加到查找表中。这个就是关键字 dynamic 的作用。**<br>
你假如去掉 dynamic 关键字，animationDidStop:finished: 就不会被调用了。

另一个方法是在 Swift 中直接或间接继承 NSObject，比如class AnimationDelegate : NSObject
当继承 NSObject 时，就认为是 Objective-C 的类，就按照 Objective-C 的方式处理。注意就算是继承 NSObject，但将方法标记成私有 private，也是不能动态找到方法地址的。

### 特殊的字面量 ###

在开发或调试过程中如果能用好下面这四个字面量，将会起到事半功倍的效果：

- __FILE__：当前代码在那个文件中
- __FUNCTION__：当前代码在该文件的那个函数中
- __LINE__：当前代码在该文件的第多少行
- __COLUMN__：当前代码在改行的多少列

### OC 和 Swift 兼容###
Apple采取的做法是允许我们在同一个项目中同时使用Swift和Objective-C来进行开发。其实一个项目中的Objective-C文件和Swift文件是处于两个不同世界中的，为了让它们能相互联通，我们需要添加一些桥梁。

首先通过添加{product-module-name}-Bridging-Header.h文件，并在其中填写想要使用的头文件名称，我们就可以很容易地在Swift中使用Objective-C代码了。Xcode为了简化这个设定，甚至在Swift项目中第一次导入Objective-C文件时会主动弹框进行询问是否要自动创建这个文件，可以说是非常方便。

### guard ###

[使用guard的正确姿势](http://www.open-open.com/lib/view/open1455424030323.html)

- 可以用 guard ：在验证入口条件时
- 可以用 guard ：在成功路径上提前退出
- 可以用 guard ：在可选值解包时（拍扁 if let..else 金字塔）
- 可以用 guard ： return 和 throw 中
- 可以用 guard ：日志、崩溃和断言中

Code Example 

    func taskFromJSONResponse(jsonData: NSData) throws -> Task {
    	guard let json = decodeJSON(jsonData) as? [String: AnyObject] else {
    	throw ParsingError.InvalidJSON
    }
    
    guard let id = json["id"] as? Int,
      let name = json["name"] as? String,
      let userId = json["user_id"] as? Int,
      let position = json["pos"] as? Double
    else {
    	throw ParsingError.MissingData
    }
    
    	return Task(id: id, name: name, userId: userId, position: position)
    }
## 网络 ##

[网络-NSURL](http://blog.csdn.net/abcd2686529828/article/details/51332484)

**NSURLConnection**

（1）NSURL：请求地址

（2）NSURLRequest：封装一个请求，保存发给服务器的全部数据，包括一个NSURL对象，请求方法、请求头、请求体....

（3）NSMutableURLRequest：NSURLRequest的子类

（4）NSURLConnection：负责发送请求，建立客户端和服务器的连接。发送NSURLRequest的数据给服务器，并收集来自服务器的响应数据 

**使用NSURLConnection发送请求的步骤很简单**

（1）创建一个NSURL对象，设置请求路径（设置请求路径）

（2）传入NSURL创建一个NSURLRequest对象，设置请求头和请求体（创建请求对象）

（3）使用NSURLConnection发送NSURLRequest（发送请求）

![Example](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ff0y458jxuj20jr06cmxg.jpg)

[如何使用HTTPRequest 在Swift中](http://stackoverflow.com/questions/24016142/how-to-make-an-http-request-in-swift)

[URL Session](http://blog.csdn.net/majiakun1/article/details/38133433)

**三种工作模式**

- 默认会话模式（default）：工作模式类似于原来的NSURLConnection，使用的是基于磁盘缓存的持久化策略，使用用户keychain中保存的证书进行认证授权。

- 瞬时会话模式（ephemeral）：该模式不使用磁盘保存任何数据。所有和会话相关的caches，证书，cookies等都被保存在RAM中，因此当程序使会话无效，这些缓存的数据就会被自动清空。

- 后台会话模式（background）：该模式在后台完成上传和下载，在创建Configuration对象的时候需要提供一个NSString类型的ID用于标识完成工作的后台会话。

**获取NSURLSession类对象有几种方式**

- 第一种方式是使用静态的sharedSession方法，该类使用共享的会话，该会话使用全局的Cache，Cookie和证书。
- 第二种方式是通过sessionWithConfiguration:方法创建对象，也就是创建对应配置的会话，与NSURLSessionConfiguration合作使用。
- 第三种方式是通过sessionWithConfiguration:delegate:delegateQueue方法创建对象，二三两种方式可以创建一个新会话并定制其会话类型。该方式中指定了session的委托和委托所处的队列。当不再需要连接时，可以调用Session的invalidateAndCancel直接关闭，或者调用finishTasksAndInvalidate等待当前Task结束后关闭。这时Delegate会收到URLSession:didBecomeInvalidWithError:这个事件。Delegate收到这个事件之后会被解引用。

**NSURLSessionTask是一个抽象子类,有三个子类**
- NSURLSessionDataTask
- NSURLSessionUploadTask
- NSURLSessionDownloadTask

![继承关系图](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ff11h1x70ij20sg0dn40t.jpg)

**DispatchSemaphore**
>信号量是一种计数器，如果方法中设置了一个信号量，当它大于0，方法会按正常模式往下执行，如果等于0则会阻塞住，直到它大于0时，就会往下执行了

**JSONSerialization**

JSONSerialization能将JSON转换成Foundation对象，也能将Foundation对象转换成JSON，但转换成JSON的对象必须具有如下属性：

- 1，顶层对象必须是Array或者Dictionary
- 2，所有的对象必须是String、Number、Array、Dictionary、Null的实例
- 3，所有Dictionary的key必须是String类型
- 4，数字对象不能是非数值或无穷

*注意：尽量使用JSONSerialization.isValidJSONObject先判断能否转换成功。*

## iOS中的多线程技术 ###
- pthread
- NSThread
- GCD
- NSOperation

Swift3.0 中的 Thread 类
其实就是 NSThread

**线程的创建**
 
    
    let thread1 = Thread(target: self, selector: #selector(longOperation), object: "thread1") thread1.start()


**通过 Thread 直接分离（创建）一个线程**

使用这种方式创建的线程，线程是直接启动的

      Thread.detachNewThreadSelector(#selector(longOperation), toTarget: self, with: "newThread")
    
**使用 NSObject 的方法直接在后台线程执行某方法**

(实际上也是创建一个新的线程对象，并启动线程)

    self.performSelector(inBackground: #selector(longOperation), with: "newThread")

**GCD**

[原文](http://www.jianshu.com/p/f042432e2d7d)

>Grand Central Dispatch (GCD)是Apple开发的一个多核编程的较新的解决方法。它主要用于优化应用程序以支持多核处理器以及其他对称多处理系统。它是一个在线程池模式的基础上执行的并行任务。在Mac OS X 10.6雪豹中首次推出，也可在IOS 4及以上版本使用。


**常见队列：**

为了方便 GCD 使用，苹果默认提供了主队列和全局队列。 我们不需要创建只需要获取。

> 主队列 (串行)
> let mainQueue = DispatchQueue.main
> **
> 全局队列 (并发)
> let globalQueue = DispatchQueue.global()
> 
> **同步不开异步开，串行开1条，并行开多条。**
> 
> 单纯这么简单的看是有误解的， 在我的评论中就可以看到。
> 
> 具体的意思是：
> 队列中的任务同步执行，队列就不具有开启线程的能力， 队列中的任务异步执行，队列就具有开启线程的能力。
> （同步和异步执行决定的是否有开启线程的能力）
> 
> 如果队列具 有开启线程的能力 (队列任务异步执行) 且队列是 串行队列 ，那么将会 开启 1 条线程 。
> 如果队列具 有开启线程的能力 (队列任务异步执行) 且队列是 并发队列 ，那么将会 开启 多 条线程 。开启线程的条数由 GCD 决定。
> ( 串行和并发决定的是开几条线程 )

### 全局队列 ###

- **全局 ( 并发 ) 队列异步执行 ：**

并发队列异步（不阻塞当前线程）执行（队列就具有开启线程的能力）， 队列会开启多条线程。

- **全局 ( 并发 ) 队列同步执行 ：**

并发队列同步（阻塞当前线程）执行（队列就不具有开启线程的能力）， 队列不会开启线程（代码都在主线程中执行）。

###主队列###

- **主队列(串行)异步执行**

主队列异步（不会阻塞当前线程）执行（队列就具有开启线程的能力）， 队列会开启线程（开启的线程就是主线程）。

- **主队列(串行)同步执行**

执行的效果就俩字 死锁

主线程同步，在 Swift 中，编译阶段就报错，在 oc 中是在运行的时候才能发现。体现的主要是界面的 “假死”。

###自定义队列###

- **自定义（串行）队列异步执行**

由于只异步执行只开了1 条线程， 可只。 默认创建的队列是 串行队列

- **自定义（串行）队列同步执行**

串行队列同步执行，没有开启线程。 代码在主线程中执行。

- **自定义（并发）队列异步执行**

（参考全局队列）

- **自定义（并发）队列同步执行**

###NSOperation###

[原文](http://www.cocoachina.com/ios/20160201/15179.html)

> NSOperation在ios4后也基于GCD实现，但是相对于GCD来说可控性更强，并且可以加入操作依赖。
> NSOperation是一个抽象的基类，表示一个独立的计算单元，可以为子类提供有用且线程安全的建立状态，优先级，依赖和取消等操作。系统已经给我们封装了NSBlockOperation和NSInvocationOperation这两个实体类。使用起来也非常简单，不过我们更多的使用是自己继承并定制自己的操作。

**状态**

> NSOperation提供了ready cancelled executing finished这几个状态变化，我们的开发也是必须处理自己关心的其中的状态。这些状态都是基于keypath的KVO通知决定，所以在你手动改变自己关心的状态时，请别忘了手动发送通知。这里面每个属性都是相互独立的，同时只可能有一个状态是YES。finished这个状态在操作完成后请及时设置为YES，因为NSOperationQueue所管理的队列中，只有isFinished为YES时才将其移除队列，这点在内存管理和避免死锁很关键。

**支持依赖**

通过NSOperation类里的方法addDependency(op:NSOperation)支持依赖。当你需要开始一个依赖于其它操作执行的操作，你会需要NSOperation

![结构图](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ff28xtpzcij20fz09ywfu.jpg)

**优先级**

    public enum NSOperationQueuePriority : Int {
	    case VeryLow
	    case Low
	    case Normal
	    case High
	    case VeryHigh
    }

##图片##

### UIViewContentMode ###

![Mode](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ff9ckw0vkyj20c90jpn4q.jpg)


### UIView的CALayer ###
> UIView之所以能显示在屏幕上，完全是因为它内部的一个图层，在创建UIView对象时，UIView内部会自动创建一个图层(即CALayer对象)，通过UIView的layer属性可以访问这个层
> 当UIView需要显示到屏幕上时，会调用drawRect:方法进行绘图，并且会将所有内容绘制在自己的图层上，绘图完毕后，系统会将图层拷贝到屏幕上，于是就完成了UIView的显示。因此，通过操作这个CALayer对象，可以很方便地调整UIView的一些界面属性，比如：阴影、圆角大小、边框宽度和颜色等。


**总结：UIView本身不具备显示的功能，拥有显示功能的是它内部的图层。**

> UIView实现了UIResponder 可以响应事件
> 
> CALayer不能响应事件

## UIImageJPEGRepresentation ###
UIImageJPEGRepresentation方法在耗时上比较少 <br>而UIImagePNGRepresentation耗时操作时间比较长<br>
通过调用UIImageJPEGRepresentation(UIImage* image, 0.5)读取数据时,返回的数据大小只有11KB多,大大压缩了图片的数据量 ,而且从视角角度看,图片的质量并没有明显的降低.因此,在读取图片数据内容时,建议优先使用UIImageJPEGRepresentation


## 数据存储 ##
**NSCoder**


> 需要将一些自己定义的类型存储在plist比如说图片,这个时候可以利用NSCoding协议,将数据地以类似档案的形式存储到plist文件中，然后从plist的文件中读取数据，使用协议的时候这个时候就会用到了NSCoder，如果对存档和解压没有概念的话，可以简单的理解为数据的序列化与反序列化。

**NSData**

- NSData 和它的可变长子类 NSMutableData 是字节缓冲区的对象化封装。我们可以获得简单缓冲区，并进行一些转换操作。通常我们并不会直接创建字节数据，而是从其他类型的内容转换成字节数据。
- 所谓简单缓冲区，就是缓冲区内只包含数据，无内嵌指针

**NSCache**

> NSCache简介：NSCache是苹果官方提供的缓存类，用法与NSMutableDictionary的用法很相似，在AFNetworking和SDWebImage中，使用它来管理缓存。
> 
> NSCache在系统内存很低时，会自动释放一些对象（出自苹果官方文档，不过在模拟器中模拟内存警告时，不会做缓存的清理动作） 为了确保接收到内存警告时能够真正释放内存，最好调用一下removeAllObjects方法。
> 
> NScache是线程安全的，在多线程操作中，不需要对Cache加锁。
> 
> NScache的key只是做强引用，不需要实现NScopying协议。

## iOS前缀缩写 ##
> NS NextStep 
> 
> CF Core Foundation 

> CA Core Animation QuartzCore.framework 
> 
> UI User interface UIKit.framework 
> 
> AL AssetsLibrary.framework 
> 
> MP MediaPlayer.framework 
> 
> =================== 
> 
> 其它相关 
> 
> .caf Core Audio Format 
> 
> .aiff Audio Interchange File Format


## 动画 ##
**CATransition 用法**

    1.#define定义的常量   
     kCATransitionFade   交叉淡化过渡   
     kCATransitionMoveIn 新视图移到旧视图上面   
     kCATransitionPush   新视图把旧视图推出去   
     kCATransitionReveal 将旧视图移开,显示下面的新视图   
    
     2.用字符串表示   
     pageCurl向上翻一页   
     pageUnCurl  向下翻一页   
     rippleEffect滴水效果   
     suckEffect  收缩效果，如一块布被抽走   
     cube立方体效果   
     oglFlip 上下翻转效果

**CAAnimation类**

    CAAnimation类，是一个抽象类。遵循CAMediaTiming协议和CAAction协议！
    
    CAAnimation is an abstract animation class. It provides the basic support for the CAMediaTiming and CAActionprotocols.
    
    CAMediaTiming协议
    可以调整时间,包括持续时间,速度,重复次数。
    CAAction协议
    可以通过响应动作的方式来显示动画。

**CAAnimation有很多派生类**

- CATransition 提供渐变效果:(推拉push效果,消退fade效果,揭开reveal效果)。
- CAAnimationGroup 允许多个动画同时播放。
- CABasicAnimation 提供了对单一动画的实现。
- CAKeyframeAnimation 关键桢动画,可以定义行动路线。
- CAConstraint 约束类,在布局管理器类中用它来设置属性。
- CAConstraintLayoutManager 约束布局管理器,是用来将多个CALayer进行布局的.各个CALayer是通过名称来区分,而布局属性是通过CAConstraint来设置的。
- CATransaction 事务类,可以对多个layer的属性同时进行修改.它分隐式事务,和显式事务。
      
**CAAnimation属性**

    delegate
    @property(retain) id delegate
    为CAAnimation设置代理。默认为nil。
    注意：一个CAAnimation实例，不能设置delegate为self。会引起循环引用。
    
    removedOnCompletion
    @property(getter=isRemovedOnCompletion) BOOL removedOnCompletion
    设置是否动画完成后，动画效果从设置的layer上移除。默认为YES。
    
    timingFunction
    @property(retain) CAMediaTimingFunction *timingFunction
    设置动画的“时机”效果。就是动画自身的“节奏”：比如：开始快，结束时变慢；开始慢，结束时变快；匀速；等，在动画过程中的“时机”效果。
    
    animation
    + (id)animation
    创建并返回一个CAAnimation实例。
    defaultValueForKey
    + (id)defaultValueForKey:(NSString *)key
    根据属性key，返回相应的属性值。

## Json解析重构 ##

**Either<A, B>类型**

来对代码进行重构，以使其能进行错误处理。这可以使代码在正确的情况下返回用户对象，而在出错时返回一个错误对象。在Swift当中可以使用如下方法来实现Either<A, B> ：

    enum Either<A, B> {  
      case Left(A)  
      case Right(B)  
    } 

## 函数式编程 ##
[Functor，Applicative 和 Monad](http://www.jdon.com/idea/functor-monad.html)
###Functor函子###
Optional.Some(2).map { $0 + 3 }<br/>
// => .Some(5)

    func map<U>(f: T -> U) -> U? {
      swifth self {
      case .Some(let x): return f(x)
      case .None: return .None
    }

## 高级运算符 ##

> Swift 中算术运算符默认是不会溢出的。溢出行为都会作为错误被捕获。为了允许溢出行为，可以使用 Swift 中另一套默认支持的溢出运算符，比如溢出加法运算符（&+）。所有这些溢出运算符都是以 & 符号开始的。

**运算符函数**

类和结构体可以为现有的操作符提供自定义的实现，这通常被称为运算符重载(overloading)。<br/>

    struct Vector2D {
    	var x = 0.0, y = 0.0
    }
    func + (left: Vector2D, right: Vector2D) -> Vector2D {
    	return Vector2D(x: left.x + right.x, y: left.y + right.y)
    }

**前缀和后缀运算符**

要实现前缀或者后缀运算符，需要在声明运算符函数的时候在 func 关键字之前指定 prefix 或者 postfix 限定符：

    prefix func - (vector: Vector2D) -> Vector2D {
    	return Vector2D(x: -vector.x, y: -vector.y)
    }

**复合赋值运算符**<br>
需要把运算符的左参数设置成 inout 类型，因为这个参数的值会在运算符函数内直接被修改。

    func += (inout left: Vector2D, right: Vector2D) {
    	left = left + right
    }

因为加法运算在之前已经定义过了，所以在这里无需重新定义。

**等价操作符**

> 自定义的类和结构体没有对等价操作符(equivalence operators)进行默认实现，等价操作符通常被称为『相等』操作符（==）与『不等』操作符（!=）。对于自定义类型，Swift 无法判断其是否『相等』，因为『相等』的含义取决于这些自定义类型在你的代码中所扮演的角色。

    func == (left: Vector2D, right: Vector2D) -> Bool {
    	return (left.x == right.x) && (left.y == right.y)
    }
    func != (left: Vector2D, right: Vector2D) -> Bool {
    	return !(left == right)
    }

**自定义运算符**

新的运算符要在全局作用域内，使用 operator 关键字进行声明，同时还要指定 prefix、infix 或者 postfix 限定符：

    prefix func +++ (inout vector: Vector2D) -> Vector2D {
	    vector += vector
	    return vector
    }

#### 自定义中缀运算符的优先级和结合性 ####
自定义的中缀(infix)运算符也可以指定优先级(precedence)和结合性(associativity)。优先级和结合性中详细阐述了这两个特性是如何对中缀运算符的运算产生影响的。

- 结合性（associativity）可取的值有 left，right 和 none。当左结合运算符跟其他相同优先级的左结合运算符写在一起时，会跟左边的操作数进行结合。同理，当右结合运算符跟其他相同优先级的右结合运算符写在一起时，会跟右边的操作数进行结合。而非结合运算符不能跟其他相同优先级的运算符写在一起。

- 结合性（associativity）的默认值是 none，优先级（precedence）如果没有指定，则默认为 100。

    infix operator +- { associativity left precedence 140 }

    func +- (left: Vector2D, right: Vector2D) -> Vector2D {

    	return Vector2D(x: left.x + right.x, y: left.y - right.y)

    }

**map 中缀表达式**

    我们可以甚至定义一个中缀操作符号，Swift的map的中缀操作符是<^>，而Haskell的fmap是<$>。:
    
    Haskell直接写为：
    
    getPostTitle <$> (findPost 1)
    
    Swift代码如下：
    
    infix operator <^> { associativity left }
    
    func <^><T, U>(f: T -> U, a: T?) -> U? {
      return a.map(f)
    }
    getPostTitle <^> findPost(1)

Swift将一个函数应用到另外一个函数的代码如下：

    typealias IntFunction = Int -> Int
    
    func map(f: IntFunction, _ g: IntFunction) -> IntFunction {
      return { x in f(g(x)) }
    }
    
    let foo = map({ $0 + 2 }, { $0 + 3 })
    foo(10)
    // => 15

#### 自定义操作符  ####

**swift2**

    infix operator =~ {
	    associativity none
	    precedence 130
    }

**swift3**

    // 自定义操作符 别名类型
    infix operator >>> : ATPrecedence
    precedencegroup ATPrecedence {
	    associativity: left
	    higherThan: AdditionPrecedence
	    lowerThan: MultiplicationPrecedence
    }

## iOS页面传值问题 ##
- 一、协议
- 二、闭包
- 三、使用NSNotificationCenterv

A 代码

    import UIKit
    class AViewController: UIViewController, passValueDelegate // 1、遵守协议
    {
    @IBOutlet weak var textLabel: UILabel!
    @IBOutlet weak var nameTextField: UITextField!
    
    override func viewDidLoad() {
    super.viewDidLoad()
    self.nameTextField.clearButtonMode = .WhileEditing
    }
    
    @IBAction func buttonAction(sender: AnyObject) {
    let bVC = BViewController()
    
    // 取到闭包传递的值
    bVC.returnNameValue { (name) -> Void in
    self.nameTextField.text = name
    }
    
    // 2、设置代理
    bVC.delegate = self
    
    self.presentViewController(bVC, animated: true, completion: nil)
    
    // 发出广播
    NSNotificationCenter.defaultCenter().postNotificationName("name", object: self.nameTextField.text)
    }
    
    // 3、重写协议方法，取到传值
    func passValue(text: String) {
    textLabel.text = text
    }
    
    
    }

B代码

    import UIKit
    
    protocol passValueDelegate {
    func passValue(var text: String)
    }
    
    typealias nameValue = (String)->Void
    
    class BViewController: UIViewController {
    // 1、声明属性
    var delegate: passValueDelegate?
    
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var bNameTextField: UITextField!
    var nameText: nameValue?
    
    override func viewDidLoad() {
    super.viewDidLoad()
    self.bNameTextField.clearButtonMode = .WhileEditing
    self.textField.clearButtonMode = .WhileEditing
    
    // 注册为观察者，接收信息
    NSNotificationCenter.defaultCenter().addObserver(self, selector: "printName:", name: "name", object: nil)
    }
    
    @IBAction func buttonActive(sender: AnyObject) {
    // 2、调用代理方法，把值传输出去
    self.delegate?.passValue(textField.text!)
    
    // 使用闭包传值出去
    self.nameText!(self.bNameTextField.text!)
    
    self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func printName(notifaction: NSNotification) {
    print(notifaction.object)
    self.bNameTextField.text = notifaction.object as? String
    NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    // 闭包方法
    func returnNameValue(name: nameValue) {
    self.nameText = name
    }
    }


- 利用NSUserDefult双向传值


> 原理：NSUserDefaults是系统对plist文件封装好的一个类，我们可以通过这个类对文件进行读写，在在HomeDirectory/Library/preferences/XXX.userdefaults.plist下。利用NSUserDefaults的特性，将某一个界面要传出的值存入到plist文件中，然后再另一个界面从plist文件读出来，就完成了传值操作

**小于5行的并且不重用的函数直接用内联的方式写在代码块中**

### Print("Hello World!") ###
[你真的不懂 print（"Hellow World!"）](http://www.jianshu.com/p/abb55919c453)


- print("hello, world")和print(123)的执行原理
- Streamable和OutputStreamType协议
- CustomStringConvertible和CustomDebugStringConvertible协议
- 为什么字符串的初始化函数中可以传入任何类型的参数
- print和debugPrint函数的区别


print code

    // 简化版print函数，通过terminator = "\n"可知print函数默认会换行
    public func print(items: Any..., separator: String = " ", terminator: String = "\n") {
    var output = _Stdout()
    _print(items, separator: separator, terminator: terminator, toStream: &output)
    }
    
    // 完整版print函数，参数中多了一个outPut参数
    public func print<Target: OutputStreamType>(items: Any...,separator: String = " ",
      terminator: String = "\n",
      inout toStream output: Target) {
      _print(items, separator: separator, terminator: terminator, toStream: &output)
    }
    
核心代码

    internal func _print<Target: OutputStreamType>(
      items: [Any],
      separator: String = " ",
      terminator: String = "\n",
      inout toStream output: Target
    ) {
      var prefix = ""
      for item in items {
    output.write(prefix)// 每两个元素之间用separator分隔开
    _print_unlocked(item, &output)// 这句话其实是核心
    prefix = separator
      }
      output.write(terminator)// 终止符，通常是"\n"
    }

### TopLayoutGuide ###
[TopLayoutGuide](https://blog.kyleduo.com/2014/10/22/ios_learning_autolayout_toplayoutguide/)

> topLayoutGuide属性表示不希望被透明的状态栏或导航栏遮挡的内容范围的最高位置。这个属性的值是它的length属性的值（topLayoutGuide.length），这个值可能由当前的ViewController或这个ViewController所属的NavigationController或TabBarController决定，有如下情况：
> 
>- 一个独立的ViewController，不包含于任何其他的ViewController。如果状态栏可见，topLayoutGuide表示状态栏的底部，否则表示这个ViewController的上边缘。
>- 包含于其他ViewController的ViewController不对这个属性起决定作用，而是由容器ViewController决定这个属性的含义：
> 
>     - 如果导航栏（Navigation Bar）可见，topLayoutGuide表示导航栏的底部。
>     - 如果状态栏可见，topLayoutGuide表示状态栏的底部。
>     - 如果都不可见，表示ViewController的上边缘。
> 这部分还比较好理解，总之是屏幕上方任何遮挡内容的栏的最底部。

### Swift指针 ###
[Swift 中的指针使用](https://onevcat.com/2015/01/swift-pointer/)

在 Swift 中，指针都使用一个特殊的类型来表示，那就是 UnsafePointer<T>。遵循了 Cocoa 的一贯不可变原则，UnsafePointer<T> 也是不可变的。当然对应地，它还有一个可变变体，UnsafeMutablePointer<T>。绝大部分时间里，C 中的指针都会被以这两种类型引入到 Swift 中：C 中 const 修饰的指针对应 UnsafePointer (最常见的应该就是 C 字符串的 const char * 了)，而其他可变的指针则对应 UnsafeMutablePointer。除此之外，Swift 中存在表示一组连续数据指针的 UnsafeBufferPointer<T>，表示非完整结构的不透明指针 COpaquePointer 等等。另外你可能已经注意到了，能够确定指向内容的指针类型都是泛型的 struct，我们可以通过这个泛型来对指针指向的类型进行约束以提供一定安全性。

对于一个 UnsafePointer<T> 类型，我们可以通过 memory 属性对其进行取值，如果这个指针是可变的 UnsafeMutablePointer<T> 类型，我们还可以通过 memory 对它进行赋值。比如我们想要写一个利用指针直接操作内存的计数器的话，可以这么做：

    func incrementor(ptr: UnsafeMutablePointer<Int>) {
    	ptr.memory += 1
    }
    
    var a = 10
    incrementor(&a)
    
    a  // 11


与这种做法类似的是使用 Swift 的 inout 关键字。我们在将变量传入 inout 参数的函数时，同样也使用 & 符号表示地址。不过区别是在函数体内部我们不需要处理指针类型，而是可以对参数直接进行操作。

    func incrementor1(inout num: Int) {
    	num += 1
    }
    
    var b = 10
    incrementor1(&b)
    
    b  // 11


**虽然 & 在参数传递时表示的意义和 C 中一样，是某个“变量的地址”，但是在 Swift 中我们没有办法直接通过这个符号获取一个 UnsafePointer 的实例。**