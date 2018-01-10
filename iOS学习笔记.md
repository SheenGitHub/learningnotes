# Swift - iOS 笔记 #

### Self ###
[指向自身的方法](https://cnbin.github.io/blog/2016/02/24/swift-jie-kou-he-lei-fang-fa-zhong-de-self/)
## 加载文件的方式 ##
led path = Bundle.main.path(forResource:"PROJECT_NAME.plist",ofType:nil)
let config = NSArray(contentsOfFile:path)
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

- as! 强行拆包，若类型不对，系统直接崩溃
- as？ 拆包，拆包结果为Optional，若类型不对，返回nil

## 构造器 ##
### Memberwise Initializers for Structure Types ###
> Structure types automatically receive a memberwise initializer if they do not define any of their own custom initializers. 

### Initializer Delegation for Value Types ###
> Initializers can call other initializers to perform part of an instance’s initialization. This process, known as initializer delegation, avoids duplicating code across multiple initializers.

**Classes do not have a default memberwise initializer**

**Value types (structures and enumerations) do not support inheritance**

### Designated Initializers and Convenience Initializers ###
#### Designated initializers ####
> Designated initializers are the primary initializers for a class. A designated initializer fully initializes all properties introduced by that class and calls an appropriate superclass initializer to continue the initialization process up the superclass chain.


#### Convenience initializers ####
> Convenience initializers are secondary, supporting initializers for a class. You can define a convenience initializer to call a designated initializer from the same class as the convenience initializer with some of the designated initializer’s parameters set to default values. 

### Initializer Delegation for Class Types ###

To simplify the relationships between designated and convenience initializers, Swift applies the following three rules for delegation calls between initializers:

- Rule 1
	-  A designated initializer must call a designated initializer from its immediate superclass.
- Rule 2
	- A convenience initializer must call another initializer from the same class. 
- Rule 3
	- A convenience initializer must ultimately call a designated initializer.

A simple way to remember this is:

- Designated initializers must always delegate up.
- Convenience initializers must always delegate across.

These rules are illustrated in the figure below:

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgkkh442mcj20ow0bx74k.jpg)

Swift’s compiler performs four helpful safety-checks to make sure that two-phase initialization is completed without error:

- Safety check 1
	- A designated initializer must ensure that all of the properties introduced by its class are initialized before it delegates up to a superclass initializer.

	- As mentioned above, the memory for an object is only considered fully initialized once the initial state of all of its stored properties is known. In order for this rule to be satisfied, a designated initializer must make sure that all of its own properties are initialized before it hands off up the chain.

- Safety check 2
	- A designated initializer must delegate up to a superclass initializer before assigning a value to an inherited property. If it doesn’t, the new value the designated initializer assigns will be overwritten by the superclass as part of its own initialization.

- Safety check 3
	- A convenience initializer must delegate to another initializer before assigning a value to any property (including properties defined by the same class). If it doesn’t, the new value the convenience initializer assigns will be overwritten by its own class’s designated initializer.

- Safety check 4
	- An initializer cannot call any instance methods, read the values of any instance properties, or refer to self as a value until after the first phase of initialization is complete.

The class instance is not fully valid until the first phase ends. Properties can only be accessed, and methods can only be called, once the class instance is known to be valid at the end of the first phase.

Here’s how two-phase initialization plays out, based on the four safety checks above:

**Phase 1**

- A designated or convenience initializer is called on a class.
- Memory for a new instance of that class is allocated. The memory is not yet initialized.
- A designated initializer for that class confirms that all stored properties introduced by that class have a value. The memory for these stored properties is now initialized.
- The designated initializer hands off to a superclass initializer to perform the same task for its own stored properties.
- This continues up the class inheritance chain until the top of the chain is reached.
- Once the top of the chain is reached, and the final class in the chain has ensured that all of its stored properties have a value, the instance’s memory is considered to be fully initialized, and phase 1 is complete.


**Phase 2**

- Working back down from the top of the chain, each designated initializer in the chain has the option to customize the instance further. Initializers are now able to access self and can modify its properties, call its instance methods, and so on.
- Finally, any convenience initializers in the chain have the option to customize the instance and to work with self.

Here’s how phase 1 looks for an initialization call for a hypothetical subclass and superclass:

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgklcem51aj20ox0bx0su.jpg)

Here’s how phase 2 looks for the same initialization call:

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgkld1891cj20ow0bxq31.jpg)

### Two-Phase Initialization ###
> Class initialization in Swift is a two-phase process. In the first phase, each stored property is assigned an initial value by the class that introduced it. Once the initial state for every stored property has been determined, the second phase begins, and each class is given the opportunity to customize its stored properties further before the new instance is considered ready for use.

**Swift subclasses do not inherit their superclass initializers by default**


> When you write a subclass initializer that matches a superclass designated initializer, you are effectively providing an override of that designated initializer. Therefore, you must write the override modifier before the subclass’s initializer definition. 

Conversely, if you write a subclass initializer that matches a superclass convenience initializer, **that superclass convenience initializer can never be called directly by your subclass**

### Automatic Initializer Inheritance ###
- Rule 1
	- If your subclass doesn’t define any designated initializers, it automatically inherits all of its superclass designated initializers.

- Rule 2
	- If your subclass provides an implementation of all of its superclass designated initializers—either by inheriting them as per rule 1, or by providing a custom implementation as part of its definition—then it automatically inherits all of the superclass convenience initializers.

### Failable Initializer ###
 Alternatively, you can define a failable initializer that creates an implicitly unwrapped optional instance of the appropriate type

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


### Error Handling ###
- Propagating Errors Using Throwing Functions
- Handling Errors Using Do-Catch
- Converting Errors to Optional Values
- Disabling Error Propagation

Convert 

    func fetchData() -> Data? {
	    if let data = try? fetchDataFromDisk() { return data }
	    if let data = try? fetchDataFromServer() { return data }
	    return nil
    }

#### Specify Cleanup Action ####

    func processFile(filename: String) throws {
	    if exists(filename) {
		    let file = open(filename)
		    defer {
		    close(file)
	    }
	    while let line = try file.readline() {
	    	
	    }
	    	// close(file) is called here, at the end of the scope.
	    }
    }

### Type Casting ###
#### Type Casting for Any and AnyObject ####

Swift provides two special types for working with nonspecific types:

- Any can represent an instance of any type at all, including function types.
- AnyObject can represent an instance of any class type.

### Swift中的指针 ###
#### UnsafeMutablePointer ####
    let ptr: UnsafeMutablePointer<Int> = ...
    // ptr.pointee == 23
    // ptr[0] == 23


#### 类型转换 ####

    // Imported from C
    func strlen(_ __s: UnsafePointer<Int8>!) -> UInt
    
    let length = uint8Pointer.withMemoryRebound(to: Int8.self, capacity: 8) {
    return strlen($0)
    }
    // length == 7


    let uint64Pointer = UnsafeMutableRawPointer(uint64Pointer)
      .bindMemory(to: UInt64.self, capacity: 1)


#### 内存管理带来的危害 ####

    var collectionPtr: UnsafeMutableBufferPointer<Int>?
    
    func duplicateElements(inArray: UnsafeMutableBufferPointer<Int>) {
    for i in 0..<inArray.count {
    inArray[i] *= 2
    }
    }
    
    repeat {
    var collection = [1, 2, 3]
    collection.withUnsafeMutableBufferPointer({ collectionPtr = $0 })
    } while false
    
    duplicateElements(inArray: collectionPtr!) // Crash due to EXC_BAD_ACCESS

在这里， collection 在一个 block 中被创建，同时在 block 结束后引用被释放。我们有意的在调用 collection 后将引用保存在了 collectoinPtr 中，然后在原始的 collection 不在存在后继续调用，所以程序在调用 duplicateElements(inArray:) 后崩溃了，如果我们想要使用指针来快速创建变量，我们需要确定这些变量能够在我们需要使用它们的时候可用。注意ARC将在每个变量离开他的作用于的时候为每个变量添加 release 方法，如果这个变量没有被强引用的话，他就会被释放。


### Int、Int8、Int16、Int32和 Int64介绍 ###


- Int16, 等于short, 占2个字节. -32768 32767
- Int32, 等于int, 占4个字节. -2147483648 2147483647
- Int64, 等于long, 占8个字节. -9223372036854775808 9223372036854775807
- Int8 ，占用1个字节

**还有需要特别说明的是在 Xcode 中Int的默认值是64bit 的，而 Int64也是64bit** 
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

>
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

### 主队列 ###

- **主队列(串行)异步执行**

主队列异步（不会阻塞当前线程）执行（队列就具有开启线程的能力）， 队列会开启线程（开启的线程就是主线程）。

- **主队列(串行)同步执行**

执行的效果就俩字 死锁

主线程同步，在 Swift 中，编译阶段就报错，在 oc 中是在运行的时候才能发现。体现的主要是界面的 “假死”。

### 自定义队列 ###

- **自定义（串行）队列异步执行**

由于只异步执行只开了1 条线程， 可只。 默认创建的队列是 串行队列

- **自定义（串行）队列同步执行**

串行队列同步执行，没有开启线程。 代码在主线程中执行。

- **自定义（并发）队列异步执行**

（参考全局队列）

- **自定义（并发）队列同步执行**

### NSOperation ###

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


## 图层 CAAnimation ##

View 和Layer 

> view的bounds 和 layer的bounds是不同的，
> 默认view的bounds的origin是{0,0}，layer的bounds是layer的frame的origin在上层view当中的相对坐标

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcdetpgeuj212w0qrdli.jpg)

**视图的frame，bounds和center属性仅仅是存取方法，当操纵视图的frame，实际上是在改变位于视图下方CALayer的frame，不能够独立于图层之外改变视图的frame**

**记住当对图层做变换的时候，比如旋转或者缩放，frame实际上代表了覆盖在图层旋转之后的整个轴对齐的矩形区域，也就是说frame的宽高可能和bounds的宽高不再一致了**

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcdqrrt3yj214j0s4wmh.jpg)

### 锚点 ###

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcdzkqu6ij215219h7dv.jpg)


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
- CAConstraintLayoutManager 约束布局管理器,是用来将多个CA
- 进行布局的.各个CALayer是通过名称来区分,而布局属性是通过CAConstraint来设置的。
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

CABasicAnimation:CAPropertyAnimation

open var fromValue:Any?
open var toValue:Any?
open var byValue:Any?

**contentsScale**

如果contentsScale设置为1.0，将会以每个点1个像素绘制图片，如果设置为2.0，则会以每个点2个像素绘制图片，这就是我们熟知的Retina屏幕。

**maskToBounds**

UIView有一个叫做clipsToBounds的属性可以用来决定是否显示超出边界的内容，CALayer对应的属性叫做masksToBounds

**contentsRect**

默认的contentsRect是{0, 0, 1, 1}，这意味着整个寄宿图默认都是可见的，如果我们指定一个小一点的矩形，图片就会被裁剪

涉及到图片是如何显示和拉伸的,layer和view的大小不会改变，但是内部内容的区域得以选择

> 事实上给contentsRect设置一个负数的原点或是大于{1, 1}的尺寸也是可以的。这种情况下，最外面的像素会被拉伸以填充剩下的区域。

**contentsCenter**
被拉伸的区域

### Frame 和 bounds 的区别 ###

[frame和bounds](http://blog.csdn.net/mad1989/article/details/8711697)

**bounds影响了子视图的坐标系**

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgblvcxlrmj20dc0ejmyj.jpg)

### 圆角 ###
> CALayer有一个叫做conrnerRadius的属性控制着图层角的曲率，默认情况下，这个曲率值只影响背景颜色而不影响背景图片或是子图层。不过，如果把masksToBounds设置成YES的话，图层里面的所有东西都会被截取

### ScrollView ###
[ScrollView Guide](https://www.appcoda.com/uiscrollview-introduction/)
### GradientLayer ###
View 自带 bounds layer属性<br>
layer中加入gradient

addSublayer() 将layer加在最上层，index是在 layer.count-1，会遮盖整个屏幕

insertSubLayer(,at:0)将layer加在最底层

UIColor，CGColor，CIColor三者的区别和联系

[UIColor，CGColor，CIColor三者的区别和联系](http://www.cnblogs.com/smileEvday/archive/2012/06/05/UIColor_CIColor_CGColor.html)

Animated gradient layers in Swift

[ColorGradient动态变化](https://oktapodi.github.io/2017/04/26/working-with-gradient-layers-in-swift.html)

CALayer.drawAsynchronously

> drawsAsynchronously. This is a property on CALayer that specifies whether or not the CPU work necessary to draw the layer should be performed on a background thread. If this is set to true, the layer will look exactly the same as usual, but the CPU computation necessary to draw it will be performed on a background thread. You should set this to true if you have a view in your app that’s redrawn a lot, such as a map view or a table view.

CALayer.shouldRasterize
> This is a property on CALayer that specifies whether or not the layer should be rasterized. When this property is true, the layer is drawn once. Whenever it is animated, it isn’t redrawn and the bitmap information from the first draw is recycled. This should be set to true if you have a view in your app that doesn’t need to be redrawn frequently. Note that when setting shouldRasterize, a layer’s appearance may change on Retina devices. This is because layers have what’s called a rasterization scale, which is the scale to rasterize the layer with. To prevent this from happening, set the layer’s rasterizationScale to UIScreen.mainScreen().scale, so that the layer is rasterized on the same scale that the screen is drawn.

### 各种CALayer ###
[各种Layer](https://www.raywenderlich.com/90488/calayer-in-ios-with-swift-10-examples)

CALayer
CAScrollLayer
CATextLayer
AVPlayerLayer
CAGradientLayer
CAReplicatorLayer
CATiledLayer
CAShapeLayer
CATransformLayer
CAEmitterLayer

[Bezier Paths](https://www.appcoda.com/bezier-paths-introduction/)


### Core Animation in the iOS drawing hierarchy ###


![Core Animation](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ffxgy9wt7ij20sg0i7gmc.jpg)

UIColor

UIColor(hue:,saturation:,brightness:,alpha)

Layer的阴影倒转

> layer.geometryFlipped = false

**initwithFrame 不调用**

> Cocoa controls implement the NSCoding protocol for unarchiving from a nib. Instead of initializing the object using initWithFrame: and then setting the attributes, the initWithCoder: method takes responsibility for setting up the control when it's loaded using the serialized attributes configured by Interface Builder. This works pretty much the same way any object is serialized using NSCoding.
> 
> It's a little bit different if you stick a custom NSView subclass in a nib that doesn't implement NSCoding, in that case initWithFrame: will be called. In both cases awakeFromNib will be called after the object is loaded, and is usually a pretty good place to perform additional initialization in your subclasses.

awakeFromNib 从storyboard中启动的时候会被调用，storyboard中数据被序列化在配置文件中，storyboard直接从配置中加载数据

**drawLayer inContext 不调用**

在外部的someView.layer.setNeedDisplay()<br>

若View中重写display方法，drawlayerincontext方法将不被调用

实现CALayerDelegate时，需要调用 CALayer.setNeedDisplay

Layer的bounds决定它在父视图中的定位


**动画在viewdidLoad中无效果，在viewWillAppear和viewdidappear中有效果**

#### Animation文章 ####
[iOS - CALayer 绘图层](http://www.cnblogs.com/QianChia/p/6348043.html)

[Beizer Path](https://www.appcoda.com/bezier-paths-introduction/)

[使用案例](http://www.cocoachina.com/ios/20160711/17007.html)

[动画的角色](http://www.jianshu.com/p/88ab3415a3fe)

Animation 继承结构
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ffyv4kwkuhj20fr08n78p.jpg)

### 手势 ###
[UITouch 和 UIEvent](http://www.jianshu.com/p/25b3467f1554)

一次完整的触摸过程，会经历3个状态：

- 触摸开始：(void)touchesBegan:(NSSet*)touches withEvent:(UIEvent*)event
- 触摸移动：(void)touchesMoved:(NSSet*)touches withEvent:(UIEvent*)event
- 触摸结束：(void)touchesEnded:(NSSet*)touches withEvent:(UIEvent*)event
- 触摸取消（可能会经历）：(void)touchesCancelled:(NSSet*)touches withEvent:(UIEvent*)event

### 图片 ###

**图片放缩**

[图像缩放的双线性内插值算法的原理解析](http://blog.sina.com.cn/s/blog_ab584cac0101h0xy.html)

对应的三种模式

- kCAFilterLinear
- kCAFilterNearest
- kCAFilterTrilinear

对于一个目的像素，设置坐标通过反向变换得到的浮点坐标为(i+u,j+v) (其中i、j均为浮点坐标的整数部分，u、v为浮点坐标的小数部分，是取值[0,1)区间的浮点数)，则这个像素得值 f(i+u,j+v) 可由原图像中坐标为 (i,j)、(i+1,j)、(i,j+1)、(i+1,j+1)所对应的周围四个像素的值决定，即：

　　`f(i+u,j+v) = (1-u)(1-v)f(i,j) + (1-u)vf(i,j+1) + u(1-v)f(i+1,j) + uvf(i+1,j+1) `                         

其中f(i,j)表示源图像(i,j)处的的像素值，以此类推。

#### 组透明 ####


![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcmjt51sfj20xy0gcwin.jpg)

这是由透明度的混合叠加造成的，当你显示一个50%透明度的图层时，图层的每个像素都会一半显示自己的颜色，另一半显示图层下面的颜色。这是正常的透明度的表现。但是如果图层包含一个同样显示50%透明的子图层时，你所看到的视图，50%来自子视图，25%来了图层本身的颜色，另外的25%则来自背景色

你可以设置CALayer的一个叫做shouldRasterize属性来实现组透明的效果，如果它被设置为YES，在应用透明度之前，图层及其子图层都会被整合成一个整体的图片，这样就没有透明度混合的问题了

    //enable rasterization for the translucent button
      button2.layer.shouldRasterize = YES;
      button2.layer.rasterizationScale = [UIScreen mainScreen].scale;

#### 仿射转换 ####

    CGAffineTransform.identity
    
    CGAffineTransform.scale
    
    self.layerView.layer.affineTransform = transform

#### 坐标转换 ####

[二维三维矩阵变换](http://blog.csdn.net/csxiaoshui/article/details/65446125)

     #define DEGREES_TO_RADIANS(x) ((x)/180.0*M_PI)

**以下变换矩阵都是右乘矩阵，针对行变换，针对列变换需要转置**

#### 平移矩阵 ####

 1     0    0 <br>
 0     1    0   即平移变换矩阵。 <br>
 dx   dy    1 <br>

#### 放缩矩阵 ####

sx    0    0 <br>
 0    sy    0  即为缩放矩阵。 <br>
 0     0     1 <br>
#### 旋转矩阵 ####
cosa   sina  0<br>
 -sina  cosa  0  为旋转变换矩阵。<br>
   0       0     1 <br>

#### 三维变换 ####
**绕任意轴的三维旋转可以使用类似于绕任意点的二维旋转一样，将旋转分解为一些列基本的旋转**

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcp0kil87j20br0ak0ug.jpg)

#### P点绕向量u旋转θ角，得到点Q，已知P点的坐标和向量u，如何求Q点的坐标。 ####
 
我们可以把向量u进行一些旋转，让它与z轴重合，之后旋转P到Q就作了一次绕Z轴的三维基本旋转，之后我们再执行反向的旋转，将向量u变回到它原来的方向，也就是说需要进行的操作如下： 

- 1. 将旋转轴u绕x轴旋转至xoz平面 
- 2. 将旋转轴u绕y轴旋转至于z轴重合 
- 3. 绕z轴旋转θ角 
- 4. 执行步骤2的逆过程 
- 5. 执行步骤1的逆过程 


(u,v,w)对应向量(a,b,c)

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcouuebdsj20jl04xq31.jpg)

如果向量是经过单位化的（单位向量），那么有a^2+b^2+c^2=1

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgcovf2yagj20fp0443yj.jpg)

### 透视投影 ###
CATransform3D.m34 默认为0

### 灭点 ###

> 当在透视角度绘图的时候，远离相机视角的物体将会变小变远，当远离到一个极限距离，它们可能就缩成了一个点，于是所有的物体最后都汇聚消失在同一个点。

Core Animation定义了这个点位于变换图层的anchorPoint（通常位于图层中心，但也有例外，见第三章）。这就是说，当图层发生变换时，这个点永远位于图层变换之前anchorPoint的位置。

#### 使用CATransform3DRotate之后，view消失一半 ####
	
Indeed this seems to mess with the z-index. This UIImageView is part of a UIView that is partially "behind" another UIView, but after rotation, the myUIImageView is always drawn on top

The solution to this was to set the zPosition property of all my layers appropriately. Thanks is due to @Brad Larson, who suggested this solution in a comment here. It seems that, when you start using CATransform3D, the normal zindex view hierarchy established by addsubview is thrown out the window.

**是因为灭点的原因**

#### UIViewContentMode ####

![Mode](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ff9ckw0vkyj20c90jpn4q.jpg)

Aspect 保持图片比例尺不变，Fit保持图片在View范围内,Fill尽可能的扩充

**View.contentMode 和 view.layer.contentsGravity 相当**

**UIViewContentMode.scaleAspectFit 和 kCAGravityResizeAspect 相当** 

**UIView.contentScaleFactor 和 layer.contetnScale相当**

> contentsScale属性其实属于支持高分辨率（又称Hi-DPI或Retina）屏幕机制的一部分。它用来判断在绘制图层的时候应该为寄宿图创建的空间大小，和需要显示的图片的拉伸度（假设并没有设置contentsGravity属性）


## 隐式动画 ##

### 事务 ###
> 事务实际上是Core Animation用来包含一系列属性动画集合的机制，任何用指定事务去改变可以做动画的图层属性都不会立刻发生变化，而是当事务一旦提交的时候开始用一个动画过渡到新值。

**任何可以做动画的图层属性都会被添加到栈顶的事务**

> Core Animation在每个run loop周期中自动开始一次新的事务（run loop是iOS负责收集用户输入，处理定时器或者网络事件并且重新绘制屏幕的东西），即使你不显式的用[CATransaction begin]开始一次事务，任何在一次run loop循环中属性的改变都会被集中起来，然后做一次0.25秒的动画。


我们把改变属性时CALayer自动应用的动画称作行为，当CALayer的属性被修改时候，它会调用-actionForKey:方法，传递属性的名称。剩下的操作都在CALayer的头文件中有详细的说明，实质上是如下几步：

- 图层首先检测它**是否有委托**，并且是否实现CALayerDelegate协议指定的-actionForLayer:forKey方法。如果有，直接调用并返回结果。
- 如果没有委托，或者委托没有实现-actionForLayer:forKey方法，图层接着检查包含属性名称对应行为**映射的actions字典**。
- 如果actions字典没有包含对应的属性，那么图层接着在它的**style字典**接着搜索属性名。
- 最后，如果在style里面也找不到对应的行为，那么图层将会直接调用定义了每个属性的**标准行为**的-defaultActionForKey:方法。

所以一轮完整的搜索结束之后，-actionForKey:要么返回空（这种情况下将不会有动画发生），要么是CAAction协议对应的对象，最后CALayer拿这个结果去对先前和当前的值做动画。

于是这就解释了UIKit是如何禁用隐式动画的：每个UIView对它关联的图层都扮演了一个委托，并且提供了-actionForLayer:forKey的实现方法。当不在一个动画块的实现中，UIView对所有图层行为返回nil，但是在动画block范围之内，它就返回了一个非空值。

- UIView关联的图层禁用了隐式动画，对这种图层做动画的唯一办法就是使用UIView的动画函数（而不是依赖CATransaction），或者继承UIView，并覆盖-actionForLayer:forKey:方法，或者直接创建一个显式动画（具体细节见第八章）。
- 对于单独存在的图层，我们可以通过实现图层的-actionForLayer:forKey:委托方法，或者提供一个actions字典来控制隐式动画。

### 模型与呈现 ###

每个图层属性的显示值都被存储在一个叫做**呈现图层**的独立图层当中，他可以通过-presentationLayer方法来访问。这个呈现图层实际上是模型图层的复制，但是**它的属性值代表了在任何指定时刻当前外观效果**。换句话说，你可以通过呈现图层的值来获取当前屏幕上真正显示出来的值。

呈现树通过图层树中所有图层的呈现图层所形成。注意呈现图层仅仅当图层首次被提交（就是首次第一次在屏幕上显示）的时候创建，所以在那之前调用-presentationLayer将会返回nil。
你可能注意到有一个叫做–modelLayer的方法。在呈现图层上调用–modelLayer将会返回它正在呈现所依赖的CALayer。通常在一个图层上调用-modelLayer会返回–self（实际上我们已经创建的原始图层就是一种数据模型）。

**Swift中分别是 CALayer.presentation() 和 CALayer.model() **

### UIView的CALayer ###
> UIView之所以能显示在屏幕上，完全是因为它内部的一个图层，在创建UIView对象时，UIView内部会自动创建一个图层(即CALayer对象)，通过UIView的layer属性可以访问这个层
> 当UIView需要显示到屏幕上时，会调用drawRect:方法进行绘图，并且会将所有内容绘制在自己的图层上，绘图完毕后，系统会将图层拷贝到屏幕上，于是就完成了UIView的显示。因此，通过操作这个CALayer对象，可以很方便地调整UIView的一些界面属性，比如：阴影、圆角大小、边框宽度和颜色等。

### 过渡 ###

CATransition有一个type和subtype来标识变换效果。type属性是一个NSString类型，可以被设置成如下类型：

- kCATransitionFade 
- kCATransitionMoveIn 
- kCATransitionPush 
- kCATransitionReveal

后面三种过渡类型都有一个默认的动画方向，它们都从左侧滑入，但是你可以通过subtype来控制它们的方向，提供了如下四种类型：

- kCATransitionFromRight 
- kCATransitionFromLeft 
- kCATransitionFromTop 
- kCATransitionFromBottom

### CAMediaTiming ###
duration和repeatCount默认都是0

autoreverses的属性 autoreverses的属性

#### 相对时间 ####
beginTime指定了动画开始之前的的延迟时间

speed是一个时间的倍数，默认1.0，减少它会减慢图层/动画的时间

timeOffset和beginTime类似，增加timeOffset只是让动画快进到某一点

#### 层级关系时间 ####
每个动画和图层在时间上都有它自己的层级概念，相对于它的父亲来测量

对CALayer或者CAGroupAnimation调整duration和repeatCount/repeatDuration属性并不会影响到子动画。但是beginTime，timeOffset和speed属性将会影响到子动画。

### 动画速度 ###
#### 线性步调 ####
    velocity = change / time

上面的等式假设了速度在整个动画过程中都是恒定不变的（就如同第八章“显式动画”的情况），对于这种恒定速度的动画我们称之为“线性步调”，而且从技术的角度而言这也是实现动画最简单的方式，**但也是完全不真实的一种效果。**

- kCAMediaTimingFunctionLinear 
- kCAMediaTimingFunctionEaseIn 
- kCAMediaTimingFunctionEaseOut 
- kCAMediaTimingFunctionEaseInEaseOut
- kCAMediaTimingFunctionDefault

#### 缓冲函数 ####

### 事件响应 ###

#### 响应者对象UIResponder ####

[touchesBegan 触摸事件](http://www.jianshu.com/p/b615df568d0e)

[触摸事件处理](http://www.cnblogs.com/spiritstudio/archive/2011/05/26/2059352.html)

在iOS中不是任何对象都能处理事件，只有继承了UIResponder的对象才能接收并处理事件。我们称之为“响应者对象”
UIApplication、UIViewController、UIView都继承自UIResponder，因此它们都是响应者对象，都能够接收并处理事件

在屏幕中移动View代码：

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
    	let touch = touches.first
    	moveOrigin = touch?.location(in: self.view)
    }
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
    	let touch = touches.first
    	let moveCurrent = touch?.location(in: self.view)
    	var frame = self.cutView.frame
    	frame.origin.x += (moveCurrent?.x)! - (moveOrigin?.x)!
    	frame.origin.y += (moveCurrent?.y)! - (moveOrigin?.y)!
    	self.cutView.frame = frame
    	moveOrigin = moveCurrent //更新moveOrigin的点，避免重复增加
    }

### 事件传递 ###
[iOS事件传递和响应](http://www.jianshu.com/p/437728c73660)

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgdkrz0nhzj20jh0angny.jpg)

#### UIView不接收触摸事件的三种情况 ####

> 1. 不接收用户交互
> userInteractionEnabled = NO
> 
> 2. 隐藏
> hidden = YES
> 
> 3. 透明
> alpha = 0.0 ~ 0.01 , 设置父控件的alpha值,则子控件的alpha也响应的改变
> 
> 提示：UIImageView的userInteractionEnabled默认就是NO，
> 因此UIImageView以及它的子控件默认是不能接收触摸事件的

hitTest的底层实现:

    // 因为所有的视图类都是继承BaseView
    - (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event
    {
    	//NSLog(@"%@--hitTest",[self class]);
    	//return [super hitTest:point withEvent:event];
    
    
    	// 1.判断当前控件能否接收事件
    	if (self.userInteractionEnabled == NO || self.hidden == YES || self.alpha <= 0.01) return nil;
    
    	// 2. 判断点在不在当前控件
    	if ([self pointInside:point withEvent:event] == NO) return nil;
    
    	// 3.从后往前遍历自己的子控件
    	NSInteger count = self.subviews.count;
    
    	for (NSInteger i = count - 1; i >= 0; i--) {
    	UIView *childView = self.subviews[i];
    
    	// 把当前控件上的坐标系转换成子控件上的坐标系
     	CGPoint childP = [self convertPoint:point toView:childView];
    
       	UIView *fitView = [childView hitTest:childP withEvent:event];
    
    
    	if (fitView) { // 寻找到最合适的view
    		return fitView;
    	}
    
    
    }
    
    // 循环结束,表示没有比自己更合适的view
    return self;
    
    }


**那么事件是怎么响应(处理)的呢?**

- 1.用户点击屏幕产生的一个触摸事件,经过一系列的传递过程后,会找到最合适的视图控件来处理这个事件
- 2.找到最合适的视图控件后,就会调用控件的touches方法来做具体的事件处理
  	- a. touchesBegin..
  	- b. touchesMoved..等等
- 3.如果我们不重写上面的touches方法,那么这些touches方法的默认****做法是将事件顺着响应者链条向上传递,将事件交给上一个响应者进行处理

**要求父视图继续处理事件**

    - (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
    {
    	// 处理事件
    	NSLog(@"%s", __func__);
    
    	// 继续传递向上传递  super->父类
    	[super touchesBegan:touches withEvent:event];
    }

### First Responder ###
当编辑框获得焦点时，会自动设置其自身为first Responder，反之亦然

**第一响应对象**

> 在应用的响应对象里，会有一个成为第一响应对象。
> 
> 第一响应对象和其他响应对象之间有什么区别？对于普通的触摸事件没什么区别。就算我把一个按钮设置成第一响应对象，当我点击其他按钮时，还是会响应其他按钮，而不会优先响应第一响应对象。
> 
> 第一响应对象的区别在于负责处理那些和屏幕位置无关的事件，例如摇动。
> 
> 苹果官方文档的说法是：第一响应对象是窗口中，应用程序认为最适合处理事件的对象。
> 
> 一个班只能有一个班长，应用的响应对象中，只能有一个响应对象成为第一响应对象。

**总结：UIView本身不具备显示的功能，拥有显示功能的是它内部的图层。**

> UIView实现了UIResponder 可以响应事件
> 
> CALayer不能响应事件

## UIImageJPEGRepresentation ###
UIImageJPEGRepresentation方法在耗时上比较少 <br>而UIImagePNGRepresentation耗时操作时间比较长<br>
通过调用UIImageJPEGRepresentation(UIImage* image, 0.5)读取数据时,返回的数据大小只有11KB多,大大压缩了图片的数据量 ,而且从视角角度看,图片的质量并没有明显的降低.因此,在读取图片数据内容时,建议优先使用UIImageJPEGRepresentation

## ViewController生命周期 ##
[UIViewController 生命周期](http://www.jianshu.com/p/9d3d95e1ef5a)

Xcode 提供了纯代码和 Storyboard（Xib 同理）两种布局 UI 的方式

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgdwwiml7hj21ii150n2v.jpg)

### loadView() ###

- loadView() 即加载控制器管理的 view。
- **不能直接手动调用该方法；当 view 被请求却为 nil 时，该方法加载并创建 view。**
- 若控制器有关联的 Nib 文件，该方法会从 Nib 文件中加载 view；如果没有，则创建空白 UIView 对象。
- **如果使用 Interface Builder 创建 view，则务必不要重写该方法。**
- **可以使用该方法手动创建视图，且需要将根视图分配为 view；自定义实现不应该再调用父类的该方法。**
- 执行其他初始化操作，建议放在 viewDidLoad() 中。

### viewDidLoad() ###
- view 被加载到内存后调用 viewDidLoad()。
- **重写该方法需要首先调用父类该方法。**
- 该方法中可以额外初始化控件，例如添加子控件，添加约束。
- **该方法被调用意味着控制器有可能（并非一定）在未来会显示。**
- 在控制器生命周期中，该方法只会被调用一次。

### viewWillAppear(_:) ###
- **该方法在控制器 view 即将添加到视图层次时以及展示 view 时所有动画配置前被调用。**
- **重写该方法需要首先调用父类该方法。**
- 该方法中可以进行操作即将显示的 view，例如改变状态栏的取向，类型。
- 该方法被调用意味着控制器将一定会显示。
- 在控制器生命周期中，该方法可能会被多次调用。

### viewWillLayoutSubviews() ###
- 该方法在通知控制器将要布局 view 的子控件时调用。
- **每当视图的 bounds 改变，view 将调整其子控件位置。**
- 该方法可重写以在 view 布局子控件前做出改变。
- 该方法的默认实现为空。
- **该方法调用时，AutoLayout 未起作用。**
- 在控制器生命周期中，该方法可能会被多次调用。
- 
> 若 loadView() 没有加载 view，viewDidLoad() 会一直调用 loadView() 加载 view，因此构成了死循环，程序即卡死。

#### viewWillLayoutSubviews和layoutSubviews的区别 ####
[viewWillLayoutSubviews和layoutSubviews的区别](https://stackoverflow.com/questions/39606077/difference-between-layoutsubviews-and-viewwilllayoutsubviews)

> viewWillLayoutSubviews is called when view controller's view's bounds changed (usually happens when view loaded, or rotation changed, or if it's a child view controller, and its view was changed by the parent view controoler), but before it's subview's bounds or position changes. You can override this method to make some changes to subview's bounds or position before the view layouts them.
> 
> layoutSubviews, from Apple's documentation:
> 
> You should override this method only if the autoresizing and constraint-based behaviors of the subviews do not offer the behavior you want
> This method gets called when a layout update happens, either by changing the view's bounds explicitly or call setNeedsLayout or layoutIfNeeded on the view to force a layout update. Please remember that it will be called automatically by the OS, and you should never call it directly. It's quite rare that you need to override this method, cause usually the autoresizing or constraint will do the job for you.

## 贝塞尔曲线 ##
理论基础

[Bernstein polynomial](https://en.wikipedia.org/wiki/Bernstein_polynomial)

[贝塞尔曲线](http://blog.csdn.net/cdnight/article/details/48468653)
### 三控制点贝塞尔曲线 ###
DF:DE = AD:AB = BE:BC

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgq8swlhh6j20go0b4t8l.jpg)

让选取的点 D 在第一条线段上从起点 A 移动到终点 B，找出所有的贝塞尔曲线上的点 F

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fgq8szhllnj20go0b4jr9.jpg)

### 贝塞尔曲线Demo ###
[动态展示 - from github](http://myst729.github.io/bezier-curve/)


### Animation 坐标系 ###
transform.rotation.y 的坐标系是以手机平面为xOy面，z轴方向垂直屏幕向外


## Json解析重构 ##

**Either<A, B>类型**

来对代码进行重构，以使其能进行错误处理。这可以使代码在正确的情况下返回用户对象，而在出错时返回一个错误对象。在Swift当中可以使用如下方法来实现Either<A, B> ：

    enum Either<A, B> {  
      case Left(A)  
      case Right(B)  
    } 

## 函数式编程 ##
[Functor，Applicative 和 Monad](http://www.jdon.com/idea/functor-monad.html)
### Functor函子 ###
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

**以上代码在swift3中无法运行，swift3中memory已经重命名为pointee， += 号不可以使用**

与这种做法类似的是使用 Swift 的 inout 关键字。我们在将变量传入 inout 参数的函数时，同样也使用 & 符号表示地址。不过区别是在函数体内部我们不需要处理指针类型，而是可以对参数直接进行操作。

    func incrementor1(inout num: Int) {
    	num += 1
    }
    
    var b = 10
    incrementor1(&b)
    
    b  // 11


**虽然 & 在参数传递时表示的意义和 C 中一样，是某个“变量的地址”，但是在 Swift 中我们没有办法直接通过这个符号获取一个 UnsafePointer 的实例。**

### 指针初始化和内存管理 ###
在 Swift 中不能直接取到现有对象的地址，我们还是可以创建新的 UnsafeMutablePointer 对象。与 Swift 中其他对象的自动内存管理不同，对于指针的管理，是需要我们手动进行内存的申请和释放的。一个 UnsafeMutablePointer 的内存有三种可能状态：

- 内存没有被分配，这意味着这是一个 null 指针，或者是之前已经释放过
- 内存进行了分配，但是值还没有被初始化
- 内存进行了分配，并且值已经被初始化

UnsafeMutablePointer 的初始化方法 (init) 完成的都是从其他类型转换到 UnsafeMutablePointer 的工作。我们如果想要新建一个指针，需要做的是使用 alloc: 这个类方法。

    var intPtr = UnsafeMutablePointer<Int>.alloc(1)
    intPtr.initialize(10)
    intPtr.destroy()
    intPtr.dealloc(1)
    intPtr = nil


> 注意其实在这里对于 Int 这样的在 C 中映射为 int 的 “平凡值” 来说，destroy 并不是必要的，因为这些值被分配在常量段上。但是对于像类的对象或者结构体实例来说，如果不保证初始化和摧毁配对的话，是会出现内存泄露的。所以没有特殊考虑的话，不论内存中到底是什么，保证 initialize: 和 destroy 配对会是一个好习惯。

对于一般的接受 const 数组的 C API，其要求的类型为 UnsafePointer，而非 const 的数组则对应 UnsafeMutablePointer

## UIApplication ##
[UIApplication 介绍](http://www.jianshu.com/p/f23bda05ca8e)

> iOS程序启动后创建的第一个对象就是UIApplication对象,一个UIApplication对象就代表一个应用程序.
> 
> 每个应用都有自己的UIApplication对象,且是单例.如果试图在程序中新建一个UIApplication对象,那么将提示报错
> 
> 通过[UIApplication sharedApplication]可以获得这个单例对象.利用UIApplication对象，能进行一些应用级别的操作.

**UIApplication的一个主要工作是处理用户事件,它会起一个队列,把所有用户事件都放入队列,逐个处理,在处理的时候,它会发送当前事件到一个合适的处理事件的目标控件**

- UIApplication对象是应用程序的象征;
- 每一个应用都有自己的UIApplication对象，而且是单例的;
- 通过[UIApplication sharedApplication]可以获得这个单例对象;
- 一个iOS程序启动后创建的第一个对象就是UIApplication对象;
- 利用UIApplication对象，能进行一些应用级别的操作.

### 程序启动UIApplicationMain ###

UIApplication的基类是UIResponder，和4.2以前生成的工程是不同的，以前是继承自NSObject。

main函数中执行了一个UIApplicationMain这个函数

    int UIApplicationMain(int argc, char argv[], NSString principalClassName, NSString *delegateClassName);

argc、argv:

直接传递给UIApplicationMain进行相关处理即可

**principalClassName：**

指定应用程序类名（app的象征），该类必须是UIApplication(或子类)。如果为nil,则用UIApplication类作为默认值

**delegateClassName：**

指定应用程序的代理类，该类必须遵守UIApplicationDelegate协议

- 1.UIApplicationMain函数会根据principalClassName创建UIApplication对象
- 2.根据delegateClassName创建一个delegate对象 ，并将该delegate对象赋值给UIApplication对象中的delegate属性.
- 3.接着会建立应用程序的Main Runloop（事件循环）,进行事件的处理(首先会在程序完毕后调用delegate对象的application:didFinishLaunchingWithOptions:方法)
- 4.程序正常退出时UIApplicationMain函数才返回

![Application生命周期](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ffm60yrd47j20yx14ktdd.jpg)

**关于Xcode7后.dylib库不见了而变更为.tbd**

> For those who are curious, the .tbd files are new "text-based stub libraries", that provide a much more compact version of the stub libraries for use in the SDK, and help to significantly reduce its download size.

## CoreLocation ##
获取locations位置，**异步**

    - (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
    {
	    CLLocation *currLocation = [locations lastObject];
	    NSLog(@"经度=%f 纬度=%f 高度=%f", currLocation.coordinate.latitude, currLocation.coordinate.longitude, currLocation.altitude);
    }

**代理方法返回的 locations 信息**

当位置管理器，获取到位置后，调用 locationManager:didUpdateLocations:方法，返回的类型为 CLLocation 的位置信息数组，以下为数组包含的属性

- 1.coordinate : 当前位置的坐标
- latitude : 纬度
- longitude : 经度
- 2.altitude : 海拔，高度
- 3.horizontalAccuracy : 纬度和经度的精度
- 4.verticalAccuracy : 垂直精度(获取不到海拔时为负数)
- 5.course : 行进方向(真北)
- 6.speed : 以米/秒为单位的速度
- 7.description : 位置描述信息

**定位授权**

开启locationmanager
  
[_locationManager startUpdatingLocation];

关闭locationmanager

[_locationManager stopUpdatingLocation];

（1）始终允许访问位置信息

- (void)requestAlwaysAuthorization;

（2）使用应用程序期间允许访问位置数据

- (void)requestWhenInUseAuthorization;
## OC ##
**oc中几种属性特质:**

- 1、nonatomic：非原子性访问，可以多线程并发访问，oc中大多数都申明nonatomic属性。
- 2、atomic：默认值，原子性访问，单线程访问，表示如果有多个线程同时调用
- ter的话，不会出现某一个线程执行setter全部语句之前，另一个线程开始执行setter情况，安全性高于nonatomic，性能低于nonatomic，但atomic 并不代表线程安全，只是说对同一对象的set和get的操作是顺序执行的。
- 3、assign：默认值，直接赋值，主要是对基本数据类型使用：NSInteger，CGFloat 和C语言的 int double float char …
- 4、retain：先release旧的对象，新对象的计数加1，并返回地址给引用者，主要对NSObject与其子类中使用。
- 5、copy： 创建一个新对象，将旧对象的值赋值给新对象，release旧对象。copy与retain的区别为：retain是指针拷贝，copy是内容拷贝，其主要对字符串NSString使用。
- 6、readonly：不能修改，只能读取，不生成setter方法。
- 7、readwrite：默认值，可读写。
- 8、strong：强引用，ARC模式下与retain同作用。
- 9、week：弱引用，ARC模式下与assign同作用。

**loadView 和 viewDidLoad**

- loadView和viewDidLoad的区别就是，调用loadView时view还没有生成，调用viewDidLoad时，view已经生成了.
- 当调用loadview时,view为空, -> 调用viewdidload控制器仍然没有自己的view,此时再次调用loadview方法让控制器生成一个黑色的view.
- 注意点: 此时如果调用了loadview当时没有给viewController指定一个view的话,不能在viewdidload方法中用self.view = 某个view,此时如果调用view的set或者get方法都会使程序进入无限死循环中.看代码


## 随机数 ##
arc4random() 产生 UInt32 范围的数值，产生 0~1 随机数可使用 CGFloat(arc4random())/CGFloat(UInt32.max)

### CGFloat ###

    #if defined(__LP64__) && __LP64__
    # define CGFLOAT_TYPE double
    # define CGFLOAT_IS_DOUBLE 1
    # define CGFLOAT_MIN DBL_MIN
    # define CGFLOAT_MAX DBL_MAX
    #else
    # define CGFLOAT_TYPE float
    # define CGFLOAT_IS_DOUBLE 0
    # define CGFLOAT_MIN FLT_MIN
    # define CGFLOAT_MAX FLT_MAX
    #endif

这段话的意思就是,64位系统下,CGFLOAT是double类型,32位系统下是float类型.CGFloat能够保证你的代码在64位系统下也不容易出错,所以你的代码应该尽量使用CGFloat.尽管他可能造成一些多余的消耗.不过能保证安全


## SuperMap ##
### 组件 ###

#### RMMapContents ####
> The cartographic and data components of a map.  Do not retain.
> 
> There is exactly one RMMapContents instance for each RMMapView instance.

使用
> contents:RMMapContents = (RMMapView)mapView.contents
> 

# Using Swift with Cocoa and Objective-C #

## Understanding the Swift Import Process ##

- Remap certain Objective-C types to their equivalents in Swift, like id to Any
- Remap certain Objective-C core types to their alternatives in Swift, like NSString to String
- Remap certain Objective-C concepts to matching concepts in Swift, like pointers to optionals

### Key-Value Observing Implementation Details ###

Automatic key-value observing is implemented using a technique called isa-swizzling.

The isa pointer, as the name suggests, points to the object's class which maintains a dispatch table. This dispatch table essentially contains pointers to the methods the class implements, among other data.

When an observer is registered for an attribute of an object the isa pointer of the observed object is modified, pointing to an intermediate class rather than at the true class. As a result the value of the isa pointer does not necessarily reflect the actual class of the instance.

You should never rely on the isa pointer to determine class membership. Instead, you should use the class method to determine the class of an object instance.

#### 变量声明 ####

[IOS OC声明变量在@interface括号中与使用@property的区别](http://blog.csdn.net/shenjie12345678/article/details/39052659)


[java 堆栈的区别](http://blog.csdn.net/chengyingzhilian/article/details/7781858)

#### 存在栈中的数据可以共享 ####
    int a = 3; 　
    
    int b = 3; 


> 编译器先处理int a = 3;首先它会在栈中创建一个变量为a的引用，然后查找栈中是否有3这个值，如果没找到，就将3存放进来，然后将a指向3。接着处理int b = 3;在创建完b的引用变量后，因为在栈中已经有3这个值，便将b直接指向3。这样，就出现了a与b同时均指向3的情况。这时，如果再令a=4;那么编译器会重新搜索栈中是否有4值，如果没有，则将4存放进来，并令a指向4;如果已经有了，则直接将a指向这个地址。因此a值的改变不会影响到b的值。要注意这种数据的共享与两个对象的引用同时指向一个对象的这种共享是不同的，因为这种情况a的修改并不会影响到b, 它是由编译器完成的，它有利于节省空间。而一个对象引用变量修改了这个对象的内部状态，会影响到另一个对象引用变量 

# 问题 #
## iOS中 Debug 和 Release 的区别和使用 ##
- Debug : 调试版本,主要是让程序员使用,在调试的过程中调用 Debug 会启动更多的服务来监控错误,运行速度相对较慢,而且比较耗能.
- Release : 发布版本,主要是让用户使用, 在使用的过程中会去掉那些繁琐的监控服务,运行速度相对较快,而且比较节约内存.

在程序调试的过程中, Xcode 默认的情况是 Debug ,如果想修改成 Release 情景下测试.

> edit Scheme ---info --->Build Configuration 选择 Release 进行测试;

### 模态视图跳转 ###

> 需求：A视图控制器中presentB视图控制器，B视图控制器再presentC视图控制器。最后从C视图控制器直接返回到A视图控制器。

[模态视图跳转](http://www.jianshu.com/p/e2572e83071e)

> 谁污染，谁治理！

    // C视图控制器触发dismiss方法前添加这么一段代码
    	UIViewController *rootVC = self.presentingViewController;
    	// rootVC.view.alpha = 0; 
    	while (rootVC.presentingViewController) {
    		rootVC = rootVC.presentingViewController;
    	}
    	[rootVC dismissViewControllerAnimated:YES completion:nil];

[模态试图(presentViewController)显示UINavigationBar](http://www.cnblogs.com/mohe/archive/2013/05/02/3054681.html)

### NavigationBar ###
> 使用UINavigationController來控制push/popover時，所有view controllers上的navigationBar都是同一個，但navigationItem則是每個view controller個別擁有。除了rootViewController以外，控制器會自動在navigationBar的左側顯示一個< Back的按鍵。

Swift 手势检测

- 滑动 UISwipeGestureRecognizer
- 边缘滑动 UIScreenEdgePanGestureRecognizer
- 轻点手势 UITapGestureRecognizer 
- 捏合手势 UIPinchGestureRecognizer
- 旋转手势 UIRotationGestureRecognizer
- 拖动手势 UIPanGestureRecognizer
- 长按 UILongPressGestureRecognizer


UICollectionView中长按会触发 collectionViewdisSelectItemAtindexPath,需要对此做处理


### NSArray ###

有一个不可变的NSArray引用并不意味着数组就不能在你眼皮子底下被改变

    let a = NSMutableArray(array: [1,2,3])
    
    // 我不想让b能被改变
    let b: NSArray = a
    
    // 但其实b还是可以被改变 —— 通过a
    a.insertObject(4, atIndex:3)
    
    // 现在b也含有4了
    print(b)

正确的做法是在创建b的时候把a拷贝一份：

    let a = NSMutableArray(array: [1,2,3])
    
    // 我不想让b能被改变
    let b: NSArray = a.copy() as! NSArray
    
    a.insertObject(4, atIndex:3)
    print(b)  // b现在依然还是[1,2,3]

**Swift中，数组的可变性由var和let关键字来决定**

## CharacterSet ##

CharacterSet是一个结构体，CharacterSet.urlHostAllowed等预制类型包含了所有不需要被转码的字符，反过来说就是指明了需要被转码的字符。

CharacterSet类中提供了一些常用的URL转码的类型:

* CharacterSet.urlHostAllowed: 被转义的字符有  "#%/<>?@\^`\{\|\}
* CharacterSet.urlPathAllowed: 被转义的字符有  "#%;<>?[\]^`\{\|\}
* CharacterSet.urlUserAllowed: 被转义的字符有   #%/<>?@\^`\{\|\}
* CharacterSet.urlQueryAllowed: 被转义的字符有  "#%<>[\]^`\{\|\}
* CharacterSet.urlPasswordAllowed 被转义的字符有 "#%/:<>?@[\]^`\{\|\}

# Android #
## Fragment ##
生命周期

![生命周期](http://ww1.sinaimg.cn/mw690/48ceb85dgy1ffvebor5wkj20ad0j0ac4.jpg)

**字体大小**
- A14:(9.283203125, 16.70703125)
- A15:(9.873046875, 17.900390625)
- A16:(10.46875, 19.09375)
- A17:(11.06494140625, 20.287109375)
- A18:(11.6982421875, 21.48046875)
- A19:(12.3203125, 22.673828125)
- a14:(7.57421875, 16.70703125)
- a15:(8.0419921875, 17.900390625)
- a16:(8.515625, 19.09375)
- a17:(8.98974609375, 20.287109375)
- a18:(9.5009765625, 21.48046875)
- a19:(10.0009765625, 22.673828125)
- 你14:(14.28, 16.70703125)
- 你15:(15.3, 17.900390625)
- 你16:(16.32, 19.09375)
- 你17:(17.323, 20.287109375)
- 你18:(18.342, 21.48046875)
- 你19:(19.361, 22.673828125)



#iPhone 屏幕尺寸#
[dpi-ppi-designer-need-know](http://www.uisdc.com/dpi-ppi-designer-need-know)

- px就是表示pixel，像素，是屏幕上显示数据的最基本的点；
- pt就是point，是印刷行业常用单位，等于1/72英寸。

iphone 6  2.3* 4.1(4.7) 750* 1334  ppi 326

- 设备	屏幕尺寸	分辨率（pt）	Reader	分辨率（px）	渲染后	PPI
- iPhone 3GS	3.5吋	320x480	@1x	320x480		163
- iPhone 4/4s	3.5吋	320x480	@2x	640x960		330
- iPhone 5/5s/5c	4.0吋	320x568	@2x	640x1136		326
- iPhone 6	4.7吋	375x667	@2x	750x1334		326
- iPhone 6Plus	5.5吋	414x736	@3x	1242x2208	1080x1920	401
- iPhone 6s	4.7吋	375x667	@2x	750x1334		326
- iPhone 6sPlus	5.5吋	414x736	@3x	1242x2208	1080x1920	401
- iPhone 7	4.7吋	375x667	@2x	750x1334		326
- iPhone 7Plus	5.5吋	414x736	@3x	1242x2208	1080x1920	401
> 所以光看屏幕的分辨率对于设计师来说是不具备多少实际意义的，通过分辨率计算得出的像素密度（PPI）才是设计师要关心的问题，我们通过屏幕分辨率和屏幕尺寸就能计算出屏幕的像素密度的。

## 控件默认尺寸 ##
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fglpsmb6vkj20c80efmyh.jpg)


约束可以理解为，两个界面元素之间必须满足的基于约束布局系统的关系。每个约束都是以下方式的线性表达式:

### NSLayoutConstraint ###

item1.attribute1 = multiplier × item2.attribute2 + constant
## 输入键盘控制 ##
设置 UITextFieldDelegate代理

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        UIView.animate(withDuration: 0.4){
            self.view.frame.origin.y = 0.0
        }
        return true
    }
    func textFieldDidBeginEditing(_ textField: UITextField) {
        UIView.animate(withDuration: 0.4){
            self.view.frame.origin.y = -165.0
        }
    }

消息通知

    NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHiden), name: .UIKeyboardWillHide, object: nil
    NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardDidAppear), name: .UIKeyboardDidShow, object: nil)

## touchesBegan中区分单击和双击 ##
    
	-(void)singleTap{
    	NSLog(@"Tap 1 time");
    }
    -(void)doubleTap{
    	NSLog(@"Tap 2 time");
    }
    - (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    	UITouch *touch = [touches anyObject];
    	NSTimeInterval delaytime = 0.4;//自己根据需要调整
    	switch (touch.tapCount) {
    	case 1:
    	[self performSelector:@selector(singleTap) withObject:nil afterDelay:delaytime];
    	break;
    	case 2:{
    	[NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(singleTap) object:nil];
    	[self performSelector:@selector(doubleTap) withObject:nil afterDelay:delaytime];
    }
    	break;
    	default:
    	break;
    	}
    }

## Associated Objects ##
[Associated Objects](http://nshipster.com/associated-objects/)

### 为一个类extension一个属性 ###
> extensions may not contain stored properties

[Swift extensions *can* add stored properties](https://medium.com/@ttikitu/swift-extensions-can-add-stored-properties-92db66bce6cd)

# iOS架构模式 #
[iOS架构模式](http://www.jianshu.com/p/87ac2f075a5b)

我们来看一下一个好的框架应该有的特征：

- 平衡并且严格的划分实体之间的角色和责任（Balanced distribution of responsibilities among entities with strict roles）
- 可测试通常作为第一优先级
- 便于使用并且维护成本低

## MVC ##
> **对于 iOS 开发者来说，给控制器减轻负担已经成为一个重要的话题**
### Expectation ###
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh1v0arqg3j20m8074t9j.jpg)
### Reality ###
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh1v1k3owlj20go06fjrv.jpg)
> MVC 架构可以在视图控制器中进行（各层次的）拼接组装
> 
> View 和 Controller 之间的交互在单元测试中是无法被真正测试的。
> 
> 就开发速度而言，Cocoa MVC 是最好的架构模式。

## MVP ##
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh1v3uuoa5j20yg0aj0uz.jpg)

我们来看一下 MVP 的特点：

- 划分（distribution）--大部分的任务都被划分到 Presenter 和 Model 中，而 View不太灵活（例子中的 Model 也不太灵活）
- 可测试性--非常出色，我们可以通过 View 来测试大部分的业务逻辑
- 易用性--在我们那个不切实际的小例子里，MVP 的理念是非常清晰的，但是代码量是 MVC 模式的两倍

> MVP 在 iOS 中使用意味着非常好的可测试和非常多的代码
## MVVP ##
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh1vwsolu1j212u0bsab7.jpg)

它和 MVP 非常像：

- MVVM 把 View Controller 作为 View
- View 和 Model 之间没有紧耦合

我们再对 MVVM 的几个特征进行一下评估：

- 划分（distribution）--也许在我们的小例子中表现得不是太清楚，但是实际上 MVVM 的 View 比 MVP 的 View 要做的时期要多一些，因为，通过设置绑定，第一个 View 由 ViewModel 来更新状态，然而第二个只需要将所有事件传递到 Presenter 就行了，不需要更新它的状态
- 可测试性-ViewModel 并不持有 View，这让我们可以轻松的对它进行测试，View 也可以进行测试，但是它是依赖于 UIKit 的，你可能会忽略它
- 易用性--在我们的例子里，MVVM 的代码量跟 MVP 差不多，但是在实际的 app 中，需要把事件从 View 传递到 Presenter ，并且要手动的更新 View，如果使用绑定的话，MVVM 将会瘦身不少


> MVVM 非常具有吸引力，因为它结合了上述几种框架方法的好

## VIPER ##
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fh1worgovaj218e0jm40b.jpg)

在 VIPER 中对于职责的划分提出了另一种方式，这次我们有五层：

- Interactor--包括和数据相关的业务逻辑（Entities）或者网络请求，比如创建entities 类的对象或者把它们从服务器中抓取出来。为了达到这些目的你会用到通常会被看做外部依赖而不被看做 VIPER 单元的一些服务（Services）和管理者（Managers）
- Presenter--包括 UI 相关（UIKit 之外）的一些业务逻辑，调用 Interactor 中的一些方法
- Entities--纯粹的数据对象，并非是数据访问层，数据访问是 Interactor 层的任务
- Router--负责 VIPER 模块之间的切换

## UITableView MVC ##
[整洁的 Table View 代码](https://objccn.io/issue-1-2/)

**然而，这两个 delegate 方法的实现又基于了 view controller 知晓 cell 实现的具体细节**

## View Controller 容器 ##
[View Controller 容器](https://objccn.io/issue-1-4/)

[更轻量的 View Controllers](https://objccn.io/issue-1-1/)  

**延伸阅读**

> UIWindow 作为一个应用程序的根视图（root view），是旋转和初始布局消息等事件产生的来源。

# OC #

#### 对象操作 ####

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1flaqkiypydj20h105kq52.jpg)

# C++ #

### 虚函数表 ###

[虚函数表](https://www.cnblogs.com/hushpa/p/5707475.html)

    #include <iostream>
    
    using namespace std;
    
    class Base {
    public:
    	virtual void f() {cout<<"base::f"<<endl;}
    	virtual void g() {cout<<"base::g"<<endl;}
    	virtual void h() {cout<<"base::h"<<endl;}
    };
    
    class Derive : public Base{
    public:
    	void g() {cout<<"derive::g"<<endl;}
    };
    
    //可以稍后再看
    int main () {
    	cout<<"size of Base: "<<sizeof(Base)<<endl;
    
    	typedef void(*Func)(void);
    	Base b;
    	Base *d = new Derive();
    
    	long* pvptr = (long*)d;
    	long* vptr = (long*)*pvptr;
    	Func f = (Func)vptr[0];
    	Func g = (Func)vptr[1];
    	Func h = (Func)vptr[2];
    
    	f();
    	g();
    	h();
    
    	return 0;
    }


包含虚函数的类才会有虚函数表， 同属于一个类的对象共享虚函数表， 但是有各自的_vptr.
    
虚函数表实质是一个指针数组，里面存的是虚函数的函数指针。

Base中虚函数表结构：

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fmdu2iecp0j20cm05zjra.jpg)

Derive中虚函数表结构：

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fmdu2zpdcaj20cz067wee.jpg)

#### 多继承 ####
![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fmdu4m8mqyj207u05cmyf.jpg)

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fmdu4yp6gyj20bo04t3zw.jpg)