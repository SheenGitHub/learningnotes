# HTML #
## table ##
## form ##
#### 设计表单 ####
从用户体验(UX)的角度来看，要记住：表单越大，失去用户的风险就越大。保持简单，保持专注:只要求必要的数据。
#### colgroup,col ####
为单独的列设置统一样式
#### thead,tbody,tfoot ####
在长表格的情况下，你可以在每个打印页面上使表格页眉和页脚重复，你也可以让表格的正文部分显示在一个单独的页面上，并通过上下滚动来获得内容。
## Canvas ##
### 图像 ###
drawImage

drawImage(img) 会按照默认的大小进行平铺

drawImage(img, dx, dy, dw, dh) 会将图像完整地绘制到指定位置上，并根据目标区域的大小进行缩放

drawImage(img, sx, sy, sw, sh, dx, dy, dw, dh)会将部分图像绘制到指定的位置上，并根据目标区域的大小进行缩放
## DOM ##
### Node ###
#### nodeType ####

document Node.DOCUMENT_NODE(9)

div Node.ELEMENT_NODE(1)

nodeName是标签名 "DIV", nodeValue始终为null

childNodes  NodeList类数组对象

ownerDocument === document

所有节点都有的一个属性ownerDocument，指向整个文档的文档节点

#### 操作节点 ####
*appendChild*

*insertChild*

*replaceChild*

*removeChild*

*cloneNode(boolean)* 

- true 深拷贝，拷贝子节点和子节点树
- false 浅拷贝，只复制节点本身，不包含子节点

复制后节点副本属于文档所有，并没有为它指定父节点

*normalize*

处理文本节点，删除空白文本，合并连续文本

### Document ###
HTMLDocument的一个实例

documentElement 和 childNodes 属性访问子节点

    document.documentElement === childNodes[0] //对html的引用

*DocumentType* 

- document.doctype 指向 <!DOCTYPE>
- document.title 修改标题
- document.URL
- document.domain **可修改，子域只能改父域，如此可通信**
- document.referrer

#### 查找元素 ####
getElementsByTag 返回的是动态的集合 HTMLCollection,不区分大小写

*HTMLCollection类型* 

都是动态集合

    images.namedItem("myImage") === images["myImage"]

HTMLCollection而言，可以向方括号中传入数值或字符串形式的索引值，在后台，对数值就会调用item(),对字符串索引就会调用namedItem()

- document.anchors 带name特性的< a>元素
- document.forms 文档中所有的<form/>元素
- document.images 文档中所有的<image>元素
- document.links 文档中所有带href的< a>元素

*getElementsByName* 仅HTMLDocument支持


标签的<之后不能跟空格

### body ###
浏览器在遇到body标签才开始呈现内容，head中的js加载会延迟页面加载 

### video ###
- controls : 为网页中的音频显示标准的HTML5控制器。
- autoplay : 使音频自动播放。
- loop : 使音频自动重复播放

可以用 &lt;source&gt; 标签来指定多个文件

[https://developer.mozilla.org/en-US/docs/Learn/HTML/Multimedia_and_embedding/Video_and_audio_content](https://developer.mozilla.org/en-US/docs/Learn/HTML/Multimedia_and_embedding/Video_and_audio_content)

#### 媒体事件 ####
- abort	 在播放被终止时触发,例如, 当播放中的视频重新开始播放时会触发这个事件。
- canplay	在媒体数据已经有足够的数据（至少播放数帧）可供播放时触发。这个事件对应CAN_PLAY的readyState。
- canplaythrough	在媒体的readyState变为CAN_PLAY_THROUGH时触发，表明媒体可以在保持当前的下载速度的情况下不被中断地播放完毕。注意：手动设置currentTime会使得firefox触发一次canplaythrough事件，其他浏览器或许不会如此。
- durationchange	元信息已载入或已改变，表明媒体的长度发生了改变。例如，在媒体已被加载足够的长度从而得知总长度时会触发这个事件。
- emptied	媒体被清空（初始化）时触发。
- ended	播放结束时触发。
- error	在发生错误时触发。元素的error属性会包含更多信息。参阅Error handling获得详细信息。
- loadeddata	媒体的第一帧已经加载完毕。
- loadedmetadata	媒体的元数据已经加载完毕，现在所有的属性包含了它们应有的有效信息。
- loadstart	在媒体开始加载时触发。
- mozaudioavailable	当音频数据缓存并交给音频层处理时
- pause	播放暂停时触发。
- play	在媒体回放被暂停后再次开始时触发。即，在一次暂停事件后恢复媒体回放。
- playing	在媒体开始播放时触发（不论是初次播放、在暂停后恢复、或是在结束后重新开始）。
- progress	告知媒体相关部分的下载进度时周期性地触发。有关媒体当前已下载总计的信息可以在元素的buffered属性中获取到。
- ratechange	在回放速率变化时触发。
- seeked	在跳跃操作完成时触发。
- seeking	在跳跃操作开始时触发。
- stalled	在尝试获取媒体数据，但数据不可用时触发。
- suspend	在媒体资源加载终止时触发，这可能是因为下载已完成或因为其他原因暂停。
- timeupdate	元素的currentTime属性表示的时间已经改变。
- volumechange	在音频音量改变时触发（既可以是volume属性改变，也可以是muted属性改变）.。
- waiting	在一个待执行的操作（如回放）因等待另一个操作（如跳跃或下载）被延迟时触发。
### href使用 ###
[a标签中href=""的几种用法](http://blog.csdn.net/u010297791/article/details/52784879)

### viewport ###
<meta name=”viewport” content=”width=device-width, initial-scale=1, maximum-scale=1″>

- width：控制 viewport 的大小，可以指定的一个值，如果 600，或者特殊的值，如 device-width 为设备的宽度（单位为缩放为 100% 时的 CSS 的像素）。
- height：和 width 相对应，指定高度。
- initial-scale：初始缩放比例，也即是当页面第一次 load 的时候缩放比例。
- maximum-scale：允许用户缩放到的最大比例。
- minimum-scale：允许用户缩放到的最小比例。
- user-scalable：用户是否可以手动缩放
### 查找 ###
form,img,iframe,applet,embed,objec

设置name，创建全局属性，如果是多个，返回HTMLCollection

- getElementById
- getElementsByName
- getElementsByTagName
- getElementsByClassName

查找返回的都是HTMLCollection，并且是动态的

静态副本 Array.prototype.slice.call(nodelist,0)

document.all

#### 节点树的文档 ####
parentNode,childNodes,firstChild,lastChild,
nextSibling,previousSibling
#### 元素树的文档 ####
firstElementChild,lastElementChild,nextElementSibling,
previousElementSibling,childElementCouont;

Text和Comment继承自 CharacterData，被元素树忽略

#### 获取和设置非标准HTML属性 ####
getAttribute();setAttribute();

作为Attr节点的属性 document.body.attributes

#### 元素文本内容 ####
textContent innerHTML 返回纯文本内容

## HTML 5 API ##
### Page Visibility API ###
#### 常用的方法是监听下面三个事件 ####
- pagehide
- beforeunload
- unload

在手机上可能不会触发

- 用户点击了一条系统通知，切换到另一个 App。
- 用户进入任务切换窗口，切换到另一个 App。
- 用户点击了 Home 按钮，切换回主屏幕。
- 操作系统自动切换到另一个 App（比如，收到一个电话）

#### document.visibilityState ####
该属性返回一个字符串，表示页面当前的可见性状态

- hidden：页面彻底不可见。
- visible：页面至少一部分可见。
- prerender：页面即将或正在渲染，处于不可见状态。

#### visibilitychange 事件 ####

	document.addEventListener('visibilitychange', function () {
	  // 用户离开了当前页面
	  if (document.visibilityState === 'hidden') {
	    document.title = '页面不可见';
	  }
	
	  // 用户打开或回到页面
	  if (document.visibilityState === 'visible') {
	    document.title = '页面可见';
	  }
	});
### 离线缓存 ###
> 离线缓存是HTML5新引入的技术，能够让你的Web应用程序指定哪些文件可以缓存在本地，使得你的网络断开时依然可以通过本地的缓存来进行访问浏览。

#### HTML 5 应用程序缓存的好处： ####

离线浏览 – 用户可在应用离线时使用它们。

速度 – 已缓存资源加载得更快。

减少服务器负载 – 浏览器将只从服务器下载更新过或更改过的资源。

浏览器支持：所有主流浏览器均支持应用程序缓存，除了 Internet Explorer。对于移动端来说浏览器不是问题。

#### 使用方法 ####
manifest文件的后缀名必须为.appcache

manifest 文件可分为三个部分：

CACHE MANIFEST – 在此标题下列出的文件将在首次下载后进行缓存

NETWORK – 在此标题下列出的文件需要与服务器的连接，且不会被缓存

FALLBACK – 在此标题下列出的文件规定当页面无法访问时的回退页面（比如 404 页面）

#### 监听事件 ####
updateready
# JavaScript #
## 严格模式 ##
ES6的模块自动采用严格模式

严格模式禁止自动或隐式地创建全局变量

在严格模式的程序中，eval(..) 在运行时有其自己的词法作用域，意味着其
中的声明无法修改所在的作用域。

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

禁止扩展的对象，在非严格模式下，创建属性b 会静默失败。在严格模式下，将会抛出TypeError 错误


## 数据类型 ##
### 7种数据类型 ###
Undefined Null Boolean Number String Object Symbol

数据类型      typeof

- Undefined    undefined
- Null         object
- Number       number
- String       string
- Object       object
- Boolean      boolean
- Function     function
- Symbol       symbol 

### Object ###
> ECMAScript定义 ： 无序属性的集合，其属性可以包含基本值，对象或者函数

#### 数据属性 ####
- [[Configurable]] :能否通过delete删除属性，能否修改属性的特性，能否把属性修改为访问器属性，默认true
- [[Enumerable]]： 能否通过for-in访问器属性，默认true
- [[Writable]]：能否修改属性的值，默认true
- [[Value]]：默认undefined

Object.defineProperty()时 configurable,enumerable,writable都默认为false；

Object类型是所有的实例的基础

#### 属性和方法 ####
*constructor*

保存用于创建当前对象的函数，默认就是 Object()

*hasOwnProperty(propertyName)*

检查给定的属性在当前的对象实例中是否存在

*isPrototypeOf(object)* 

检查传入的对象是否是当前对象的原型

*propertyIsEnumerable*

(检查给定的对象是否能够使用for-in语法)

*toLocalString()*

返回对象的字符串表示，该字符串与执行环境的地区对应

*toString()*

返回对象的字符串表示

*valueOf()*

返回对象的字符串，数值或布尔值

**JavaScript不适用于BOM DOM中的对象,EMCA不负责定义宿主对象**

对象的属性名现在可以有两种类型，一种是原来就有的字符串，另一种就是新增的 Symbol 类型。

定义属性

    let mySymbol = Symbol();
    
    // 第一种写法
    let a = {};
    a[mySymbol] = 'Hello!';
    
    // 第二种写法
    let a = {
      [mySymbol]: 'Hello!'
    };
    
    // 第三种写法
    let a = {};
    Object.defineProperty(a, mySymbol, { value: 'Hello!' });
    
    // 以上写法都得到同样结果
    a[mySymbol] // "Hello!"

> 使用Object.getOwnPropertyNames方法得不到Symbol属性名，需要使用Object.getOwnPropertySymbols方法。
> 
> 另一个新的 API，Reflect.ownKeys方法可以返回所有类型的键名，包括常规键名和 Symbol 键名。

    const foo = bar;
    const baz = {foo};
    等同于
    const baz = {foo:foo};

#### 方法简写 ####
    const o = {
	    method(){
		    return "Hello!";
	    }
    };
    等同于
    const o ={
	    method:function(){
		    return "Hello!";
	    }
    };
#### 属性的赋值器（setter）和取值器（getter） ####
> 如果对象的方法使用了取值函数（getter）和存值函数（setter），则name属性不是在该方法上面，而是该方法的属性的描述对象的get和set属性上面，返回值是方法名前加上get和set
####  ####
属性名表达式如果是一个对象，默认情况下会自动将对象转为字符串[object Object]

#### method name ####
- (new Function()).name//"anonymous"
- doSomething.bind().name//"bound doSomething"
- obj[key1].name//"[description]" Symbol 的描述符
#### Object.is() ####
- Object.is(+0, -0) // false
- Object.is(NaN, NaN) // true

#### Object.assign ####
合并对象，复制可枚举属性

**undefined和null无法转成对象**

**有字符串合入目标对象（以字符数组的形式），数值和布尔值都会被忽略。这是因为只有字符串的包装对象，会产生可枚举属性。**

**Object.assign方法实行的是浅拷贝，而不是深拷贝,拷贝得到对象的引用**

Object.assign只能进行值的复制，如果要复制的值是一个取值函数，那么将求值后再复制。

对非对象属性的拷贝
Object.assign({},obj}

#### 属性的可枚举和遍历 ####
目前，有四个操作会忽略enumerable为false的属性。

- for...in循环：只遍历对象自身的和继承的可枚举的属性。
- Object.keys()：返回对象自身的所有可枚举的属性的键名。
- JSON.stringify()：只串行化对象自身的可枚举的属性。
- Object.assign()： 忽略enumerable为false的属性，只拷贝对象自身的可枚举的属性。

**我们只关心对象自身的属性。所以，尽量不要用for...in循环，而用Object.keys()代替**

*ES6 一共有 5 种方法可以遍历对象的属性*

- for...in 遍历对象自身的和继承的可枚举属性
- Object.key(obj) 自身可枚举属性，不含Symbol
- Object.getOwnPropertyNames(obj) 包括不可枚举属性，不包括Symbol属性
- Object.getOwnPropertySymbols(obj) 自身所有Symbol属性
- Reflect.ownKeys(obj) 自身所有属性

*遍历次序规则*

- 首先遍历所有数值键，按照数值升序排列。
- 其次遍历所有字符串键，按照加入时间升序排列。
- 最后遍历所有 Symbol 键，按照加入时间升序排列。

#### Object.getOwnPropertyDescriptors ####
ES2017引入 Object.getOwnPropertyDescriptors

**引入原因: Object无法正确拷贝get和set属性**

super 指向当前对象的原型对象

super只能在方法中

#### __proto__ ####
> super.foo等同于Object.getPrototypeOf(this).foo（属性）或Object.getPrototypeOf(this).foo.call(this)（方法）

#### Object.create ####
Object.create创建属性默认是不可遍历的

#### Object.entries ####
返回键值对数组

用于将对象转化为Map结构

### Symbol ###
Symbol() 唯一生成 

Symbol.for() 查询生成，

#### 内置的Symbol ####
**Symbol.hasInstance**

foo instanceof Foo在语言内部，实际调用的是Foo[Symbol.hasInstance] (foo)。

static [Symbol.hasInstance] (obj) 相当于重写判断instanceof 

**Symbol.isConcatSpreadable**

默认值是undefined，该对象用于Array.prototype.concat时是否可以展开，数组默认可以展开，数组对象默认不展开

**Symbol.species**

指向一个构造函数

默认等同于

    static get [Symbol.species] (){
        return this;
    }

**Symbol.match**

指向一个函数，同上，当执行str.match(myObject)时，改属性存在，就会调用它

等同于

    searchValue[Symbol.replace] (this,replaceValue)

*一下两个函数同上*

**Symbol.species**

**Symbol.search**

**Symbol.iterator**

指向该对象的默认遍历器

**Symbol.toPrimitive**

指向一个方法，当对象被转为原始类型的值时，会调用这个方法，返回对应原始类型值

被调用时接受一个字符串参数，标识当前的运算模式

- Number:转成数字
- String:转成字符串
- Default:都可以

**Symbol.toStringTag**

指向一个对象，当该对象上调用 Object.prototype.toString时，它的返回值标表示对象的类型

ES6 新增内置对象的Symbol.toStringTag属性值如下。

- JSON[Symbol.toStringTag]：'JSON'
- Math[Symbol.toStringTag]：'Math'
- Module 对象M[Symbol.toStringTag]：'Module'
- ArrayBuffer.prototype[Symbol.toStringTag]：'ArrayBuffer'
- DataView.prototype[Symbol.toStringTag]：'DataView'
- Map.prototype[Symbol.toStringTag]：'Map'
- Promise.prototype[Symbol.toStringTag]：'Promise'
- Set.prototype[Symbol.toStringTag]：'Set'
- %TypedArray%.prototype[Symbol.toStringTag]：'Uint8Array'等
- WeakMap.prototype[Symbol.toStringTag]：'WeakMap'
- WeakSet.prototype[Symbol.toStringTag]：'WeakSet'
- %MapIteratorPrototype%[Symbol.toStringTag]：'Map Iterator'
- %SetIteratorPrototype%[Symbol.toStringTag]：'Set Iterator'
- %StringIteratorPrototype%[Symbol.toStringTag]：'String Iterator'
- Symbol.prototype[Symbol.toStringTag]：'Symbol'
- Generator.prototype[Symbol.toStringTag]：'Generator'
- GeneratorFunction.prototype[Symbol.toStringTag]：'GeneratorFunction'

**Symbol.unscopables**

指向一个对象，当使用with关键字时，哪些属性会被with排除

### 基本包装类型 ###
基本类型不是对象，照理不应该有方法

后台会自动完成流程

1. 创建一个String类型的实例
2. 在实例上调用指定的方法
3. 销毁这个实例

自动创建的基本包装类型的对象，只存在于一行代码执行的瞬间

基本类型和引用类型的区别

typeof 和 instanceof的操作区别
### Null ###
null标识空指针，undefined派生自null

    undefined == null true

### Boolean ###
Bollean()  函数

String 非空为true

Number 非零为true  NaN为false

Object 非null 为true

Undefined n/a 为true   undefined为false

### 未声明变量 ###
对于未声明的变量，只能执行一项操作，即typeof 检测数据类型

### Number ###
浮点数的内存空间是整形的两倍

浮点数值的最高精度是17位小数

> 浮点数值会产生误差的问题，这是基于IEEE754 数值的浮点计算的通病

#### NaN ####
任何数值除以非数值返回NaN，任何NaN的操作返回NaN，NaN与任何值都不相等

    isNaN("blue") true

> isNaN也是用于对象，首先调用的对象的valueOf()方法，如果不能再调用toString()方法，再测试返回值

#### 数制转换 ####
- Number(null) = 0;
- Number(undefined) = NaN;
- Number("") = 0;
- parseInt("") = NaN
- parseFloat("0xA) = 0 只解析十进制
> 一元操作符的操作与Number相同

**Number.isFinite() Number.isNaN(0)只对数值有效**

- Number.isInteger(3.0000000000000002) // true
- Number.isInteger(5E-324) //false Number.MIN_VALUE
- Number.isInteger(5E-325) //true
- Number.EPSILON === Math.pow(2,-52)
- JavaScript数值范围 -2^53-2^53
- Math.clz32 32位二进制形式的前导0个数
- Math.hypot 距离

JavaScript数值存储为64位双精度格式,数值精度做多达53个二进制位(1个隐藏位和52个有效位)，超过这个限度就会被放弃

转化成无符号数

    var a = -18
    a>>>0
    a.toString(2) //11111111111111111111111111101110 18的二进制补码
### 位运算 ###
    >>> 无符号移位
    ~ 取反 负值减一
    & 与运算
    | 或运算
    ^ 异或运算

[感受神奇的异或](https://www.lijinma.com/blog/2014/05/29/amazing-xor/)

，但是如果你在解决问题的时候，你可能会忘记异或的这些特性，所以适当的应用可以让我们加深对异或的理解；

- A ⊕ 1 = A';
- A ⊕ 0 = A;
- A ⊕ A = 0;
- A ⊕ A' = 1;

#### 异或的作用 ####
1. 判断两个值相等

    static inline int ipv6_addr_equal(const struct in6_addr *a1, const struct in6_addr *a2)
    {
    return (((a1->s6_addr32[0] ^ a2->s6_addr32[0]) |
        (a1->s6_addr32[1] ^ a2->s6_addr32[1]) |
        (a1->s6_addr32[2] ^ a2->s6_addr32[2]) |
        (a1->s6_addr32[3] ^ a2->s6_addr32[3])) == 0);
    }

2. 汇编中将变量置零

    xor a，a

3. 翻转特定的位

    10100001 ^ 0100000 = 10000001 //翻转第6位

4. 校验二进制数中1是奇数还是偶数

    1 ^ 0 ^ 1 ^ 0 ^ 0 ^ 0 ^ 0 ^ 1 = 1 粗略地校验

5. 校验与恢复

> A ^ B的结果写到磁盘C；当读取A的数据时，通过B ^ C可以对A的数据做校验，当A盘出错时，通过B ^ C也可以恢复A盘的数据

6. 不使用其他空间，交换两个值

    a = a ^ b;
    b = a ^ b; //a ^ b ^ b = a ^ 0 = a;
    a = a ^ b;

7. 互换二进制数的奇偶位

    `#define N(n) ((n<<1)&(0xAAAA))|((n>>1)&(0x5555))`

8. 一个整型数组里除了N个数字之外，其他的数字都出现了两次，找出这N个数字

      A ^ B ^ C ^ B ^ C ^ D ^ A
    
    = A ^ A ^ B ^ B ^ C ^ C ^ D
    
    = 0 ^ 0 ^ 0 ^ D
    
    = 0 ^ D
    
    = D

#### 题目：一个整型数组里除了两个数字之外，其他的数字都出现了两次。请写程序找出这两个只出现一次的数字？ ####

思路： 

- 第一步：肯定还是像我们上面的解法一样，所有数进行异或，不过最终得到的结果是 a 和 b（假设 a 和 b 是落单的数字）两个值的异或结果 aXORb，没有直接得到 a 和 b 的值；
- 第二步：想办法得到 a 或者 b，假设 aXORb 为 00001001（F肯定不为0），根君 aXORb 的值我们发现，值为1的位（比如从右向左第一位）表示在此位上 a 和 b 的值不同；所以，根据这个特点，我们找出来所有第一位为1的数进行异或，得到的就是 a 或者 b；
- 第三步：aXORb = a ^ b，假设我们已经找到了 a，根据异或特性，我们知道，b = aXORb ^ a；这样我们就可以找出 b；所以我们只需要循环两次；

这样我们的时间复杂度是 O(n)，空间复杂度是 O(1) 代码

#### 布尔操作符 ####
- && 两个对象，返回第二个对象
- || 两个对象，返回第一个对象

#### 乘性操作符 ####
> 先将数字用Number()转化,对象调用其上的valueOf()和toString()

数值除以0，结果是Infinity
#### 加性操作符 ####
 加法

- 两个字符串，拼接；
- 一个字符串，另一个转换为字符串，调用 toString()
- 数值和对象，对象先调用valueOf(),再调用 toString()

- 3 + {valueOf(){return 3}}  = 6
- 3 + {toString(){return "3"}} = "33"
- 3 + {toString(){return 3}} = 6
- 3 + {value:3} = 3[object Object]

 -Infinity + Infinity = NaN

undefined  和 null 调用 toString()转换为 "undefined" 和 "null"

**下面的代码将输出到控制台，为什么？**

下面的代码将输出到控制台，为什么？

console.log(1 +  "2" + "2");
console.log(1 +  +"2" + "2");
console.log(1 +  -"1" + "2");
console.log(+"1" +  "1" + "2");
console.log( "A" - "B" + "2");
console.log( "A" - "B" + 2);

**解决递归超时**

	var list = readHugeList();
	
	var nextListItem = function() {
	    var item = list.pop();
	
	    if (item) {
	        // process the list item...
	        setTimeout( nextListItem, 0);
	    }
	};
	推至事件队列
	var list = readHugeList();
	 
	var nextListItem = function() {
	    var item = list.pop();
	 
	    if (item) {
	        // process the list item...
	        setTimeout( nextListItem, 0);
	    }
	};

**作用域**

通过为每次迭代创建一个唯一的作用域 ，可以使用闭包来防止这个问题，并将该变量的每个唯一值存储在其作用域中

	for (var i = 0; i < 5; i++) {
	    (function(x) {
	        setTimeout(function() { console.log(x); }, x * 1000 );
	    })(i);
	}

**变量提升**

	(function () {
	    try {
	        throw new Error();
	    } catch (x) {
	        var x = 1, y = 2;
	        console.log(x);
	    }
	    console.log(x);
	    console.log(y);
	})();

**变量声明**

	var x = 21;
	var girl = function () {
	    console.log(x);
	    var x = 20;
	};
	girl ();

	未声明的变量，不在全局查找

#### 关系操作符 ####
- 一个是数值，另一个转成数值
- 字符串，比较字符编码值  "Brick" < "alphabet"
- 对象，先调用valueOf,再调用toString()
- 布尔值转数值
- NaN参与比较为false

#### 相等操作符 ####
- 相等和不相等 先转换，再比较
- 全等和不全等， 仅比较不转换

null 和undefined相等

能对null 和undefined作转换

switch转换时是使用全等操作符

### String ###
#### Unicode表示法 ####
- \uxxxx
- \u{xxxx} 超过\uffff
- '\u{1F680}' === '\uD82D\uDE80' 大括号表示法与UTF-16编码等价

**codePointAt()**

返回完整的码位，包含多个编码单元

charCodeAt()只能返回单个编码单元

反向 String.fromCodePoint() 更像是完整版的 String.fromCharCode();

var s = "𠮷"；

判断字符是两个字节还是四个字节

    function is32Bit(c) {
      return c.codePointAt(0) > 0xFFFF;
    }
*repeat*

'x'.repeat(3) //'xxx'

*padStart/padEnd* 

补全字符串

'abc'.padStart(10,'01234567890') // 01234567abc

#### replace ####
	function htmlEscape(text){
		return text.erplace(/[<>"&]/g,function(match, pos, originalText){
			switch(match){
				case '<':
				  return "&lt;";
				case '>':
				  return "&gt;";
				case '&':
				  return "&amp;";
				case '\"':
				  return "&quot;";
			}
		})
	}
### Class ###
Function的语法糖

#### 类的等价实现 ####
	let PersonType2 = (function(){
		"use strict";
		
		const PersonType2 = function(name) {
			if (typof new.target === 'undefined'){
				throw new Error("必须通过关键字new调用构造函数");
			}
		}
		
		Object.defineProperty(PersonType2.prototype, "sayName",{
			value: function(){
				if (typeof new.target !== 'undefined') {
					throw new Error("不可使用关键字new调用该方法");
				}
				console.log(this.name);
			},
			enumerable: false,
			writable: true,
			configurable:true,
		});
		
		return PersonType2;
	})();

#### 继承的等价实现 ####
*寄生组合式继承*

	function Rectangle(length, width) {
		this.length = length;
		this.width  = width;
	}
	
	Rectangel.prototype.getArea = function() {
		return this.length * this.width;
	};
	
	function Square(length) {
		Rectangle.call(this, length, length);
	}
	
	Square.prototype = Object.create(Rectangle.prototype, {
		constructor : {
			value: Square,
			enumerable: true,
			writable: true,
			configurable: true
		}
	});

#### flvjs中的Babel装换ES5实现 ####
	var _createClass = function () { 
		function defineProperties(target, props) { 
			for (var i = 0; i < props.length; i++) { 
				var descriptor = props[i];
				 descriptor.enumerable = descriptor.enumerable || false; 
				 descriptor.configurable = true; 
				 if ("value" in descriptor) descriptor.writable = true; 
				 Object.defineProperty(target, descriptor.key, descriptor); 
			 } 
		 } 
		 
		 return function (Constructor, protoProps, staticProps) { 
			 if (protoProps) defineProperties(Constructor.prototype, protoProps); 
			 if (staticProps) defineProperties(Constructor, staticProps); 
			return Constructor; 
		}; 
	 }(); 

#### Babel中的类保证检查 ####
	function _classCallCheck(instance, Constructor) 
	{
		if (!(instance instanceof Constructor)) {
			throw new TypeError("Cannot call a class as a function");
		}
	}

类的内部所有定义的方法，都是不可枚举的（non-enumerable）

类必须使用new调用，否则会报错。这是它跟普通构造函数的一个主要区别，后者不用new也可以执行。

实例的属性除非显式定义在其本身（即定义在this对象上），否则都是定义在原型上（即定义在class上）

类的所有实例共享一个原型对象

类不存在变量提升（hoist）

hasOwnProperty() 查看对象的属性，而不是prototype上的

> __proto__
#### new.target ####
返回new命令作用于的那个构造函数

#### 继承 ####
> 子类必须在constructor方法中调用super方法，否则新建实例时会报错。这是因为子类没有自己的this对象，而是继承父类的this对象，然后对其进行加工。如果不调用super方法，子类就得不到this对象。

*继承实例*
> ES5 的继承，实质是先创造子类的实例对象this，然后再将父类的方法添加到this上面（Parent.apply(this)）。ES6 的继承机制完全不同，实质是先创造父类的实例对象this（所以必须先调用super方法），然后再用子类的构造函数修改this。

#### Object.getPrototypeOf ####
取得父类
#### super ####
**只有调用super之后，才可以使用this关键字**

constructor中 super虽然代表了父类A的构造函数，但是返回的是子类B的实例，即super内部的this指的是B，因此super()在这里相当于A.prototype.constructor.call(this)。

**super()只能用在子类的构造函数之中**

super作为对象时，在普通方法中，指向父类的原型对象；在静态方法中，*指向父类。*

> 由于super指向父类的原型对象，所以定义在父类实例上的方法或属性，是无法通过super调用的

**由于this指向子类实例，所以如果通过super对某个属性赋值，这时super就是this，赋值的属性会变成子类实例的属性。**

> super在静态方法之中指向父类，在普通方法之中指向父类的原型对象。

    // B 的实例继承 A 的实例
    Object.setPrototypeOf(B.prototype, A.prototype);
    
    // B 继承 A 的静态属性
    Object.setPrototypeOf(B, A);

**一旦发现Object方法不是通过new Object()这种形式调用，ES6 规定Object构造函数会忽略参数**

#### Mixin 多个类 ####
    function mix(...mixins) {
      class Mix {}
    
      for (let mixin of mixins) {
	    copyProperties(Mix, mixin); // 拷贝实例属性
	    copyProperties(Mix.prototype, mixin.prototype); // 拷贝原型属性
      }
    
      return Mix;
    }
    
    function copyProperties(target, source) {
      for (let key of Reflect.ownKeys(source)) {
	    if ( key !== "constructor"
	      && key !== "prototype"
	      && key !== "name"
	    ) {
	      let desc = Object.getOwnPropertyDescriptor(source, key);
	      Object.defineProperty(target, key, desc);
	    }
      }
    }

## 对象 ##
> 在JavaScript 中二进制前三位都为0 的话会被判
> 断为object 类型，null 的二进制表示是全0，自然前三位也是0，所以执行typeof 时会返回“object”

> 对于Object、Array、Function 和RegExp（正则表达式）来说，无论使用文字形式还是构
> 造形式，它们都是对象，不是字面量
> 
> 在对象中，属性名永远都是字符串

### 内置对象 ###
但是在JavaScript 中，它们实际上只是一些内置函数。这些内置函数可以当作构造函数
（由new 产生的函数调用——参见第2 章）来使用，从而可以构造一个对应子类型的新对
象

	var strObject = new String( "I am a string" );
	typeof strObject; // "object"
	strObject instanceof String; // true
	// 检查sub-type 对象
	Object.prototype.toString.call( strObject ); // [object String]

> 从技术角度来说，函数永远不会“属于”一个对象

**是一种对象的引用**

### 数组 ###
如果你试图向数组添加一个属性，但是属性名“看起来”像一个数字，那它会变成
一个数值下标（因此会修改数组的内容而不是添加一个属性）

concat不改变原数组，生成新的数组

splice改变原数组，生成[]

### 深拷贝与浅拷贝 ###
深拷贝会引起循环引用，造成死循环

**Uncaught TypeError:converting circular structure to JSON**

浅拷贝 Object.assign(target,....);

**由于Object.assign(..) 就是使用= 操作符来赋值，所
以源对象属性的一些特性（比如writable）不会被复制到目标对象。**
### 属性描述符 ###
	var myObject = {
		a:2
	};
	var desc = Object.getOwnPropertyDescriptor( myObject, "a" );

	var copy = {}
	Object.defineProperty(desc,"a",desc);

1. Writable
writable 决定是否可以修改属性的值。

2. Configurable
只要属性是可配置的，就可以使用defineProperty(..) 方法来修改属性描述符

3. Enumerable
用户定义的所有的普通属性默认都是enumerable

> 即便属性是configurable:false， 我们还是可以
> 把writable 的状态由true 改为false，但是无法由false 改为true

> 除了无法修改，configurable:false 还会禁止删除这个属性

**不要把delete 看作一个释放内存的工具（就像C/C++ 中那
样），它就是一个删除对象属性的操作**

### 不变性 ###
所有的方法创建的都是浅不变形

1. 对象常量
结合writable:false 和configurable:false 就可以创建一个真正的常量属性

2. 禁止扩展
用Object.preventExtensions 禁止一个对象添加新属性并且保留已有属性

3. 密封
Object.seal， 在一个现有对象上调用
Object.preventExtensions(..) 并把所有现有属性标记为configurable:false 密封之后不仅不能添加新属性，也不能重新配置或者删除任何现有属性

4. 冻结
在一个现有对象上调用
Object.seal(..) 并把所有“数据访问”属性标记为writable:false，这样就无法修改它们
的值
### [[Get]] ###
在语言规范中，myObject.a 在myObject 上实际上是实现了[[Get]] 操作,有点像函数调用：[[Get]] ()
### [[Put]] ###
[[Put]] 算法大致会检查下面这些内容。

1. 属性是否是访问描述符？如果是并且存在setter 就调用setter。
2. 属性的数据描述符中writable 是否是false ？如果是，在非严格模式下静默失败，在严格模式下抛出TypeError 异常。
3. 如果都不是，将该值设置为属性的值。

### Getter和Setter ###
访问描述符

对于访问描述符来说，JavaScript 会忽略它们的value 和
writable 特性，取而代之的是关心set 和get（还有configurable 和enumerable）特性

### 存在性 ###
- in 在对象及原型链中 ,//检查属性名
- hasOwnProperty 只检查对象本身

所有的普通对象都可以通过对于Object.prototype 的委托，除了Object.create(null)

*propertyIsEnumerable*

propertyIsEnumerable(..) 会检查给定的属性名是否直接存在于对象中（而不是在原型链
上）并且满足enumerable:true

#### 生成器 ####
	Object.defineProperty( myObject, Symbol.iterator, {
		enumerable: false,
		writable: false,
		configurable: true,
		value: function() {
			var o = this;
			var idx = 0;
			var ks = Object.keys( o );
			return {
				next: function() {
					return {
						value: o[ks[idx++]],
						done: (idx > ks.length)
					};
				}
			};
		}
	} );

## 类和对象 ##
> 一个类就是一张蓝图。为了获得真正可以交互的对象，我们必须按照类来建造（也可以说
> 实例化）一个东西，这个东西通常被称为实例，有需要的话，我们可以直接在实例上调用
> 方法并访问其所有公有数据属性。
> 这个对象就是类中描述的所有特性的一份副本

### 显式混入 ###
#### minxin ####
	function mixin( sourceObj, targetObj ) {
		for (var key in sourceObj) {
			// 只会在不存在的情况下复制
			if (!(key in targetObj)) {
				targetObj[key] = sourceObj[key];
			}
		}
		return targetObj;
	}
#### 寄生继承 ####
	function Car() {
		// 首先，car 是一个Vehicle
		var car = new Vehicle();
		// 接着我们对car 进行定制
		car.wheels = 4;
		// 保存到Vehicle::drive() 的特殊引用
		var vehDrive = car.drive;
		// 重写Vehicle::drive()
		car.drive = function() {
			vehDrive.call( this );
			console.log(
			"Rolling on all " + this.wheels + " wheels!"
			);
		}
		return car;
	}
### 隐式混入 ###
	var Another = {
		cool: function() {
			// 隐式把Something 混入Another
			Something.cool.call( this );
		}
	};

## 原型 ##
对于默认的[[Get]] 操作来说，如果无法在对象本身找到需要的属性，就会继续访问对象
的[[Prototype]] 链

使用for..in 遍历对象时原理和查找[[Prototype]] 链类似

如果foo 不直接存在于myObject 中而是存
原型链上层时myObject.foo = "bar" 会出现的三种情况。

1. 如果在[[Prototype]] 链上层存在名为foo 的普通数据访问属性（参见第3 章）并且没
有被标记为只读（writable:false），那就会直接在myObject 中添加一个名为foo 的新
属性，它是屏蔽属性。
2. 如果在[[Prototype]] 链上层存在foo，但是它被标记为只读（writable:false），那么
无法修改已有属性或者在myObject 上创建屏蔽属性。如果运行在严格模式下，代码会
抛出一个错误。否则，这条赋值语句会被忽略。总之，不会发生屏蔽。
3. 如果在[[Prototype]] 链上层存在foo 并且它是一个setter（参见第3 章），那就一定会
调用这个setter。foo 不会被添加到（或者说屏蔽于）myObject，也不会重新定义foo 这
个setter。

**只有上层有setter的时候才会设置上层**



> 只读属性会阻止[[Prototype]] 链下层
隐式创建（屏蔽）同名属性。这样做主要是为了模拟类属性的继承，这个限制只在=时，defineProperty不受限制

	var anotherObj = {a:2};
	var myObj = Object.create(anotherObj);
	myObj.a++;
	console.log(anotherObj.a);//2
	console.log(myObj.a);//3

### 原型继承 ###

> JavaScript 和面向类的语言不同，它并没有类来作为对象的抽象模式
> 或者说蓝图。JavaScript 中只有对象

**在JavaScript 中，类无法描述对象的行为，（因为根本就不存在类！）对象直接定义自己的行
为。再说一遍，JavaScript 中只有对象。**

**实例化（或者继承）一个类就意味着“把类的
行为复制到物理对象中”，对于每一个新实例来说都会重复这个过程。
但是在JavaScript 中，并没有类似的复制机制。你不能创建一个类的多个实例，只能创建
多个对象，它们[[Prototype]] 关联的是同一个对象**

new Foo() 这个函数调用实际上并没
有直接创建关联，这个关联只是一个意外的副作用

#### 继承 ####
JavaScript 会在两
个对象之间创建一个关联，这样一个对象就可以通过**委托**访问另一个对象的属性和函数

*差异继承*

只是定义了B 的一些指定
特性，其他没有定义的东西都变成了“洞”。而这些洞（或者说缺少定义的空白处）最终
会被委托行为“填满”。

	function Foo() {
	// ...
	}
	Foo.prototype.constructor === Foo; // true
	var a = new Foo();
	a.constructor === Foo; // true

#### 构造函数 ####
。a.constructor 只是通过默认的[[Prototype]] 委托指向Foo，

修复.constructor 需要很多手动操作。所有这些工作都是源于把“constructor”错误地理
解为“由……构造”，这个误解的代价实在太高了

对象的.constructor 会默认指向一个函数，这个函数可以通过对象的.prototype引用，**constructor 并不表示被构造**

.constructor 并不是一个不可变属性。它是不可枚举，可以任意赋值

**a1.constructor 是一个非常不可靠并且不安全的引用**

#### 原型继承 ####

	function Foo(name) {
		this.name = name;
	}
	Foo.prototype.myName = function() {
		return this.name;
	};
	function Bar(name,label) {
		Foo.call( this, name );
		this.label = label;
	}
	// 我们创建了一个新的Bar.prototype 对象并关联到Foo.prototype
	Bar.prototype = Object.create( Foo.prototype );
	// 注意！现在没有Bar.prototype.constructor 了
	// 如果你需要这个属性的话可能需要手动修复一下它
	Bar.prototype.myLabel = function() {
		return this.label;
	};
	var a = new Bar( "a", "obj a" );
	a.myName(); // "a"
	a.myLabel(); // "obj a"

*两种错误*

	// 和你想要的机制不一样！
	//对Bar.prototype的修改会影响Foo
	Bar.prototype = Foo.prototype;
	// 基本上满足你的需求，但是可能会产生一些副作用 :(
	//来源于函数Foo的副作用
	//使用Foo的原型，而不是Foo的对象
	Bar.prototype = new Foo();

*[[Prototype]]关联*

	// ES6 之前需要抛弃默认的Bar.prototype
	Bar.ptototype = Object.create( Foo.prototype );
	// ES6 开始可以直接修改现有的Bar.prototype
	Object.setPrototypeOf( Bar.prototype, Foo.prototype );

instanceof 操作符的左操作数是一个普通的对象，右操作数是一个函数。instanceof 回答
的问题是：在a 的整条[[Prototype]] 链中是否有指向Foo.prototype 的对象？

> 如果使用内置的.bind(..) 函数来生成一个硬绑定函数（参见第2 章）的话，
> 该函数是没有.prototype 属性的。在这样的函数上使用instanceof 的话，
> 目标函数的.prototype 会代替硬绑定函数的.prototype

第二种判断[[Prototype]] 反射的方法，它更加简洁：

	Foo.prototype.isPrototypeOf( a ); // true

不能跨帧使用,每个帧里都有各自的Function构造函数
#### 对象关联 ####
Object.create(null) 会创建一个拥有空（ 或者说null）[[Prototype]]
链接的对象，这个对象无法进行委托，这些特殊的空[[Prototype]] 对象通常被称作“字典”，它们完全不会受到原
型链的干扰，因此非常适合用来存储数据。

## Prototype ##
JavaScript 中这个机制的本质就是对象之间的关联关系

### 面向委托的设计 ###
来使用委托行为而不是类来思考同样的问题

	Task = {
		setID: function(ID) { this.id = ID; },
		outputID: function() { console.log( this.id ); }
	};
	// 让XYZ 委托Task
	XYZ = Object.create( Task );
	XYZ.prepareTask = function(ID,Label) {
		this.setID( ID );
		this.label = Label;
	};
	XYZ.outputTaskDetails = function() {
		this.outputID();
		console.log( this.label );
	};

这种编码风格称为“对象关联”（OLOO，
objects linked to other objects）

**JavaScript 中就是没有类似“类”的抽象机制**

> 尽量少使用容易被重写的通用方法名，提倡使用更有描述性的方法名

**this的变量也使用原型链来查找**

#### 委托行为意味着某些对象（XYZ）在找不到属性或者方法引用时会把这个请求委托给另一个对象（Task） ####

### 面向对象风格 ###
	// 父类
	function Widget(width,height) {
		this.width = width || 50;
		this.height = height || 50;
		this.$elem = null;
	}
	Widget.prototype.render = function($where){
		if (this.$elem) {
			this.$elem.css( {
				width: this.width + "px",
				height: this.height + "px"
			} ).appendTo( $where );
		}
	};
	// 子类
	function Button(width,height,label) {
		// 调用“super”构造函数
		Widget.call( this, width, height );
		this.label = label || "Default";
		this.$elem = $( "<button>" ).text( this.label );
	}
	// 让Button“继承”Widget
	Button.prototype = Object.create( Widget.prototype );
	// 重写render(..)
	Button.prototype.render = function($where) {
		// “super”调用
		Widget.prototype.render.call( this, $where );
		this.$elem.click( this.onClick.bind( this ) );
	};
	Button.prototype.onClick = function(evt) {
		console.log( "Button '" + this.label + "' clicked!" );
	};
	$( document ).ready( function(){
		var $body = $( document.body );
		var btn1 = new Button( 125, 30, "Hello" );
		var btn2 = new Button( 150, 40, "World" );
		btn1.render( $body );
		btn2.render( $body );
	} );
#### ES6 类风格 ####
	class Widget {
		constructor(width,height) {
			this.width = width || 50;
			this.height = height || 50;
			this.$elem = null;
		}
		render($where){
			if (this.$elem) {
				this.$elem.css( {
					width: this.width + "px",
					height: this.height + "px"
				} ).appendTo( $where );
			}
		}
	}
	class Button extends Widget {
		constructor(width,height,label) {
			super( width, height );
			this.label = label || "Default";
			this.$elem = $( "<button>" ).text( this.label );
		}
		render($where) {
			super( $where );
			this.$elem.click( this.onClick.bind( this ) );
		}
		onClick(evt) {
			le.log( "Button '" + this.label + "' clicked!" );
		}
	}
	$( document ).ready( function(){
		var $body = $( document.body );
		var btn1 = new Button( 125, 30, "Hello" );
		var btn2 = new Button( 150, 40, "World" );
		btn1.render( $body );
		btn2.render( $body );
	} );
#### 委托对象风格 ####
	var Widget = {
		init: function(width,height){
			this.width = width || 50;
			this.height = height || 50;
			this.$elem = null;
		},
		insert: function($where){
			if (this.$elem) {
				this.$elem.css( {
					width: this.width + "px",
					height: this.height + "px"
				} ).appendTo( $where );
			}
		}
	};
	var Button = Object.create( Widget );
	Button.setup = function(width,height,label){
		// 委托调用
		this.init( width, height );
		this.label = label || "Default";
		this.$elem = $( "<button>" ).text( this.label );
	};
	Button.build = function($where) {
		// 委托调用
		this.insert( $where );
		this.$elem.click( this.onClick.bind( this ) );
	};
	Button.onClick = function(evt) {
		console.log( "Button '" + this.label + "' clicked!" );
	};
	$( document ).ready( function(){
		var $body = $( document.body );
		var btn1 = Object.create( Button );
		btn1.setup( 125, 30, "Hello" );
		var btn2 = Object.create( Button );
		btn2.setup( 150, 40, "World" );
		btn1.build( $body );
		btn2.build( $body );
	} );

#### 反词法 ####
匿名函数没有name 标识符，这会导致：

1. 调试栈更难追踪；
2. 自我引用（递归、事件（解除）绑定，等等）更难；
3. 代码（稍微）更难理解。

instanceof 语法会产生语义困惑而且非常不直观

*鸭子类型*

	if (a1.something) {
		a1.something();
	}

#### Right-hand side of instanceof is not callable ####

> This is not what instanceof is for.  instanceof is used to see if an object is an instance of a specific constructor (ex: banned instanceof Array).

#### ES6 class ####
传统面向类的语言中父类和子
类、子类和实例之间其实是复制操作，但是在[[Prototype]] 中并没有复制，相反，它们
之间只有委托关联。

ES6 还解决了什么问题呢？

1. （基本上，下面会详细介绍）不再引用杂乱的.prototype 了。
2. Button 声明时直接“ 继承” 了Widget， 不再需要通过Object.create(..) 来替
换.prototype 对象，也不需要设置.__proto__ 或者Object.setPrototypeOf(..)。
3. 可以通过super(..) 来实现相对多态，这样任何方法都可以引用原型链上层的同名方
法。这可以解决那个问题：构造函数不属于类，所以无法互相引用——
super() 可以完美解决构造函数的问题。
4. class 字面语法不能声明属性（只能声明方法）。看起来这是一种限制，但是它会排除
掉许多不好的情况，如果没有这种限制的话，原型链末端的“实例”可能会意外地获取
其他地方的属性（这些属性隐式被所有“实例”所“共享”）。所以，class 语法实际上
可以帮助你避免犯错。
5. 可以通过extends 很自然地扩展对象（子）类型，甚至是内置的对象（子）类型，比如
Array 或RegExp。没有class ..extends 语法时，想实现这一点是非常困难的，基本上
只有框架的作者才能搞清楚这一点。但是现在可以轻而易举地做到！
## 面向对象 ##
*构造函数*
### 工厂模式 ###
	function createPerson(name,age,job){
		var o = new Object();
		o.name = name;
		o.age = age;
		o.job = job;
		o.sayName = function(){
			console.log(this.name);
		}
		return o;
	}

没有解决识别对象的问题(**怎样知道一个对象的类型**)

### 构造函数模式 ###
	function Person(name,age,job){
		this.name = name;
		this.age = age;
		this.job = job;
		this.sayName =function(){
			console.log(name);
		}
	}

*将构造函数的作用域赋给新对象*

	person1 = new Person();
	person1.constructor == Person
	person1 instanceof Object //true
	person1 instanceof Person //true


**它的实例标识为一种特定的类型**，这正是构造函数模式胜过工厂模式的地方

> 这种方式定义的构造函数是定义在Global对象中的

	Person("Greg",27,"Doctor");
	window.sayName();

不适用new调用函数时，属性和方法都被添加到window对象中

	var o = new Obejct();
	Person.call(o, "Kristen",25,"Doctor");
	o.sayName();

同上，属性和方法被添加到o对象中

**函数执行后，属性和方法被添加到环境对象中**


**SOLVED:函数中定义的函数无法被global直接访问，不提升**

缺点:每一个都要**生成新的函数实例**,会导致不同的作用域链和标识符解析

	function Person(name,age,job){
		this.name = name;
		this.age = age;
		this.job = job;
		this.sayName = new Function("console.log(name);");
	}

解决办法，全局函数,**破坏封装性**

	function Person(name,age,job){
		this.name = name;
		this.age = age;
		this.job = job;
		this.sayName = sayName;
	}

	function(){
		console.log(this.name)
	}

### 原型模式 ###
> 每一个函数都有一个prototype，这个属性是一个指针，指向一个对象，这个对象的用途是包含由特定类型的所有实例共享的属性和方法。

	function Person(){}
	
	Person.prototype.name = "Nicholas";
	Person.prototype.age = 29;
	Person.prototype.job = "Software Engineer";
	Person.prototype.sayName = function(){
		console.log(name);
	}

	var person1 = new Person();
	person1.__proto__ == Person.prototype // true;

对象和原型的联系在与实例和原型对象之间，不在对象和构造函数之间

	person1.__proto__ == Object.getPrototypeOf(person1)

**缺点:省略了为构造函数传递初始化参数这一环节，结果所有实例在默认情况下都取得了相同的值**

Function才有 prototype， 对象内部属性[[Prototype]]即__proto__

函数和函数的原型通过prototype和constructor互指

通过函数生成的对象有一个内部指针([[Prototype]])指向其构造函数的原型，通过isProtoTypeOf可以查看

函数的原型对象最初只包含constructor属性，这个原型的__proto__指向一个对象new Object();

delete obj.prop,可以使得恢复对原型链上prop的访问（属性屏蔽）

重写原型对象切断了现有原型和之前已经存在的对象之间的联系，它们引用的仍然是最初的原型。

### 使用构造函数和原型模式 ###

	function Person(name,age,job){
		this.name = name;
		this.age = age;
		this.job = job;
		this.friends = ["Shelby", "Court"];
	}

	Person.prototype = {
		constructor:Person,
		sayName:function(){
			console.log(this.name);
		}
	}

**EMCAScript中使用岁广泛，认同度最高的创建自定义类型的方法**
### 动态原型模式 ###
有别于：独立的构造函数和原型（如上），

把所有的信息都封装在构造函数中，通过在构造函数中初始化原型，同时保持构造函数和原型的优点

	function Person(name,age,job){
		this.name = name;
		this.age = age;
		this.job = job;
		this.friends = ["Shelby", "Court"];

		if(typeof this.sayName != "function"){
			Person.prototype.sayName = function(){
				console.log(this.name)
			}
		}
	}

**不能使用对象字面量重写原型**
### 寄生构造函数模式 ###
	function Person(name,age,job){
		var o = new Object();
		o.name = name;
		o.age = age;
		o.job = job;
		o.sayName = function(){
			console.log(this.name);
		}
		return o;
	}

重写调用构造函数时的返回值，类似Swift Extention

构造SpecialArray

	function SpecialArray(){
		var values = new Array();
		values.push.apply(values,arguments);
		values.toPipedString = function(){
			return this.join("|");
		}
		return values;
	}

instanceof没有意义

### 稳妥构造函数模式 ###
	function Person(name,age,job){
		var o = new Object();
		
		o.sayName = function(){
			console.log(name);
		}
		return o;
	}

只有sayName可以访问原始数据，提供安全性

instanceof没有意义

*继承*

### 原型链 ###
	function SuperType(){
		this.property = true;
	}
	
	SuperType.prototype.getSuperProperty = function(){
		return this.property;
	};
	
	function SubType(){
		this.subProperty = false;
	}
	
	SubType.prototype = new SuperType();
	
	SubType.prototype.getSubValue = function(){
		return this.subProperty;
	}
	
	var instance = new SubType();
	console.log(instance.getSuperProperty())

缺点:原型属性被所有实例引用，**不能向超类中传递参数**
### 借用构造函数 ###
伪造对象或经典继承

	function SuperType(){
		this.colors = ["red","blue","green"];
	}
	
	function SubType(){
		SuperType.call(this);
	}

在新创建的SubType实例的环境下调用SuperType构造函数。这样就会在SubType的对象上执行SuperType中定义的所有对象的初始化大妈，结果SubType的每个实例就会具有自己colors属性的副本。

缺点：构造函数中的方法**无法复用**，超类的原型中定义的方法对子类型也是不可见的

### 组合继承 ###
伪经典继承

将原型链和借用构造函数的技术组合在一起

使用原型链实现对原型属性和方法的继承，通过构造函数来实现对实例属性的继承

	function SuperType(name){
		this.name = name;
		this.colors = ["red","blue","green"];
	}
	
	SuperType.prototype.sayName = function(){
		console.log(this.name);
	}
	
	function SubType(name,age){
		SuperType.call(this,name);
		this.age = age;
	}
	
	SubType.prototype = new SuperType();
	SubType.prototype.constructor = SubType;
	SubType.prototype.sayAge = function(){
		console.log(this.age);
	}
组合继承的最大不足：**调用了两次超类构造函数**

**最常用的继承模式**

### 原型式继承 ###

	function object(o){
		function F(){}
		F.prototype = o;
		return new F();
	}

ECMAScript中被标准化为 Object.create();

### 寄生式继承 ###

	function createAnother(original){
		var clone = object(original);
		clone.sayHi = function(){
			console.log("hi");
		}
		return clone;
	}

与原型式继承一样由Douglas Crockford推介，在主要考虑对象而不是自定义类型和构造函数的情况下，是一种有用的模式

缺点：**不能做到函数复用**

### 寄生组合式继承 ###
组合继承的最大不足：**调用了两次超类构造函数**

	function inheritPrototype(subType,superType){
		var prototype = object(superType.prototype);
		prototype.constructor = subType;
		subType.prototype = prototype; 
	}

### 私有变量 ###
	function Person(name){
		this.getName = function(){
			returen name;
		}
	
		this.setName = function(){
			name = value;
		}
	}

在构造函数中定义特权方法的缺点，就是每个实例都会创建一组新方法
#### 静态私有变量 ####
	function(){
		var privateVariable = 10; //静态私有变量
		
		function privateFunction(){ //私有方法
			return false;
		}
	
		MyObject = function(){
		};
	
		MyObject.prototype.publicMethod = function(){//特权方法
			privateVariable++;
			return privateFunction();
		}
	}

特权方法，作为一个闭包，总是包含对包含作用域的引用

多**查找作用域链中的一个层次**，就会在**一定程度上影响查找速度**，这正是使用闭包和私有变量的一个明显的不足之处

#### 模块模式 ####
> JavaScript以字面量的方式来创建单例对象

模块模式通过为单例添加私有变量和特权方法使其增强

	var singleton = function(){
		var privateVariable = 10;
	
		function privateFunction(){
			return false;
		}
	
		return {
			publicProperty:true,
			
			publicMethod:function(){
				privateVariable++;
				return privateFunction();
			}
		}
	}();

使用单例来管理应用级的信息

## 类型 ##
> 本规范中的运算法则所操纵的值均有相应的类型。本节中定义了所有可能出现的
> 类型。ECMAScript 类型又进一步细分为语言类型和规范类型。
> ECMAScript 语言中所有的值都有一个对应的语言类型。ECMAScript 语言类型包
> 括Undefined、Null、Boolean、String、Number 和Object。

对语言引擎和开发人员来说，类型是值
的内部特征，它定义了值的行为，以使其区别于其他值。
换句话说，如果语言引擎和开发人员对42（数字）和"42"（字符串）采取不同的处理方
式，那就说明它们是不同的类型，一个是number，一个是string。通

JavaScript 有七种内置类型：

-  空值（null）
-  未定义（undefined）
-  布尔值（ boolean）
-  数字（number）
-  字符串（string）
-  对象（object）
-  符号（symbol，ES6 中新增）

> Function有一个内部属性[[Call]]，该属性使其可以被调用。函数对象的length 属性是其声明的参数的个数

#### null 值检查 ####
	var a = null;
	(!a && typeof a === "object"); // true

### 值和类型 ###
> JavaScript 中的变量是没有类型的，只有值才有。变量可以随时持有任何类型的值。

> JavaScript 不做“类型强制”；也就是说，语言引擎不要求变量总是
> 持有与其初始值同类型的值

**undefined 和undeclared**

	var a;
	a; // undefined
	b; // ReferenceError: b is not defined

**要检查你用到的变量是否已经在宿主程序中定义过**

	// 这样会抛出错误
	if (DEBUG) {
		console.log( "Debugging is starting" );
	}
	// 这样是安全的
	if (typeof DEBUG !== "undefined") {
		console.log( "Debugging is starting" );
	}

	if (window.DEBUG) {
	// ..

### 数组 ###

#### 用代理创建数组 ####
	function toUint32(value){
	    return Math.floor(Math.abs(Number(value)))%Math.pow(2,32);
	}
	
	function isArrayIndex(key){
	    let numbericKey = toUint32(key);
	    return String(numbericKey) == key && numbericKey < (Math.pow(2,32) -1);
	}
	
	class MyArray{
	    constructor(length = 0) {
	        this.length = length;
	        return new Proxy(this,{
	            set(trapTarget, key, value){
	                let currentLength = Reflect.get(trapTarget, 'length');
	                    
	                if(isArrayIndex(key)){
	                    let numbericKey = Number(key);
	                    if(numbericKey >= currentLength) {
	                        Reflect.set(trapTarget, 'length', numbericKey+1);
	                    }
	                }else if(key==='length'){
	                    if(value < currentLength){
	                        for(let index = currentLength-1;index >= value;index--){
	                            Reflect.deleteProperty(trapTarget, index);
	                        }
	                    }
	                }
	                return Reflect.set(trapTarget, key, value);
	            }
	        })
	    }
	}

“稀疏”数组

如果字符串键值能够被强制类型转换为十进制数字的话，它
就会被当作数字索引来处理。

类数组转换

	//空位是empty
	var arr = Array.prototype.slice.call( arguments );
ES6

	//空位是undefined
	var arr = Array.from( arguments );
### 字符串 ###
字符串和数组的确很相似，它们都是类数组，都有length 属性以及indexOf和concat，但并不是一回事

许多数组函数用来处理字符串很方便。虽然字符串没有这些函数，但可以通过“借用”数
组的非变更方法来处理字符串

	var c = Array.prototype.join.call( a, "-" );
	var d = Array.prototype.map.call( a, function(v){
	return v.toUpperCase() + ".";
	} ).join( "" );
### 数字 ###
JavaScript 没有真正意义上的整数

> JavaScript 中的数字类型是基
> 于IEEE 754 标准来实现的，该标准通常也被称为“浮点数”。JavaScript 使用的是“双精
> 度”格式（即64 位二进制）。

	// 无效语法：
	42.toFixed( 3 ); // SyntaxError
	// 下面的语法都有效：
	(42).toFixed( 3 ); // "42.000"
	0.42.toFixed( 3 ); // "0.420"
	42..toFixed( 3 ); // "42.000"

ES6

	0o363; // 243的八进制
	0O363; // 同上
	0b11110011; // 243的二进制
	0B11110011; // 同上

- Number.MIN_VALUE
- Number.MAX_VALIE
- Number.MIN_SAFE_INTEGER
- Number.MAX_SAFE_INTEGER
- Number.EPSILON

over Chrome 67

	BigInt(1234567895n)

虽然整数最大能够达到53 位，但是有些数字操作（如数位操作）只适用于32 位数字

void 并不改变表达式的结果，
只是让表达式不返回值：

如果数学运算（如加法）的结果超出处理范围，则由IEEE 754 规范中的“就
近取整”（round-to-nearest）模式来决定最后的结果

对负零进行字符串化会返回"0",反过来将其从字符串转换为数字，得到的结果是准确的
### 正则表达式 ###
正则类进入使用状态，首先要确定目标私服穿的起始搜索位置。它是字符串的起始字符，或者由正则表达式的lastIndex属性指定(lastIndex只作为exec和test方法的起始搜索位置，当且仅当/g时)，在匹配失败后，此位置在最后一次匹配的起始位置的下一个字符的位置。

如果正则表示式所有的可能路径都没有匹配到，正则表达式会回到起始位置，然后从下一个字符重新尝试。

#### 回溯 ####
当遇到量词和分支时，需要决策下一步如何处理。

贪婪(*)和惰性(*?)版本的正则表达式是等价的，但是它们的匹配过程并不相同。贪婪的量词可能会一直匹配到词尾，然后再向前回溯。

JavaScript没有选择点号匹配换行在内的任意字符。

具体化减少回溯，考虑其它标签的存在

在NFA匹配时候，是根据正则表达式来匹配文本的；而在DFA匹配时候，采用的是用文本来匹配正则表达式的方式

独占模式 不回溯，非捕获组(?:)


### 值和引用 ###
JavaScript 中没有指针，在JavaScript 中变量不可能成为指向
另一个变量的引用。

简单值（即标量基本类型值，scalar primitive）总是通过值复制的方式来赋值/ 传递

复合值（compound value）——对象（包括数组和封装对象，参见第3 章）和函数，则总
是通过**引用复制**的方式来赋值/ 传递

由于引用指向的是值本身而非变量，所以一个引用无法更改另一个引用的指向

	function foo(x) {
		x.push( 4 );
		x; // [1,2,3,4]
		// 然后
		x = [4,5,6];
		x.push( 7 );
		x; // [4,5,6,7]
	}
	var a = [1,2,3];
	foo( a );
	a; // 是[1,2,3,4]，不是[4,5,6,7]
### 內建函数 ###
new String("abc") 创建的是字符串"abc" 的封装对象，而非基本类型值"abc"。

### 内部属性[[Class]] ###
通过Object.prototype.toString(..) 来查看

	Object.prototype.toString.call( [1,2,3] );
	// "[object Array]"
	Object.prototype.toString.call( /regex-literal/i );
	// "[object RegExp]"
	Object.prototype.toString.call( null );
	// "[object Null]"
	Object.prototype.toString.call( undefined );
	// "[object Undefined]"
	Object.prototype.toString.call( "abc" );
	// "[object String]"
	Object.prototype.toString.call( 42 );
	// "[object Number]"
	Object.prototype.toString.call( true );
	// "[object Boolean]"

Object 封装， valueOf 拆封

数组 map 没有任何单元遍历失败， join按length查找，

	var a = Array.apply( null, { length: 3 } );
	a; // [ undefined, undefined, undefined ]

将原型作为默认值
### 强制类型转换 ###
将值从一种类型转换为另一种类型通常称为类型转换（type casting），这是显式的情况；隐
式的情况称为强制类型转换（coercion）

类型转换发生在静态类型语言的编译阶段，而强制类型转换则发生在动态类型语言的运行时（runtime）。

	var a = 42;
	var b = a + ""; // 隐式强制类型转换
	var c = String( a ); // 显式强制类型转换

#### ToString #### 
它负责处理非字符串到字符串的强制类型转换

基本类型值的字符串化规则为：null 转换为"null"，undefined 转换为"undefined"，true
转换为"true"。数字的字符串化则遵循通用规则

对普通对象来说，除非自行定义，否则toString()（Object.prototype.toString()）返回
内部属性[[Class]] 的值

对大多数简单值来说，JSON 字符串化和toString() 的效果基本相同

**不安全的JSON 值。undefined、function、symbol
（ES6+）和包含循环引用**

JSON.stringify(..) 在对象中遇到undefined、function 和symbol 时会自动将其忽略，在
数组中则会返回null（以保证单元位置不变）

	JSON.stringify( undefined ); // undefined
	JSON.stringify( function(){} ); // undefined
	JSON.stringify(
	[1,undefined,function(){},4]
	); // "[1,null,null,4]"
	JSON.stringify(
	{ a:2, b:function(){} }
	); // "{"a":2}"

**定义toJSON() 方法来返回一个安全的JSON 值。**

> 可以向JSON.stringify(..) 传递一个可选参数replacer，它可以是数组或者函数，用
> 来指定对象序列化过程中哪些属性应该被处理
> 
> JSON.string 还有一个可选参数space，用来指定输出的缩进格式。它还可以是字符串，此时最前面的十个字符被用于每一级的缩进

	var a = {
		b:42,
		c:"42",
		d:[1,2,3]
	}
	JSON.stringify(a,(k,v)=>{if(k!=='c') return v;},3);

[1,2]+[3,4] = '1,23,4'

因为数组的valueOf() 操作无法得到简单基本类型值，于是它转而调用toString()。

#### ToNumber ####
**其中true 转换为1，false 转换为0。undefined 转换为NaN，null 转换为0**

*对象*

检查该值是否有valueOf() 方法。
如果有并且返回基本类型值，就使用该值进行强制类型转换。如果没有就使用toString()
的返回值（如果存在）来进行强制类型转换。

如果valueOf() 和toString() 均不返回基本类型值，会产生TypeError 错误。

> 从ES5 开始，使用Object.create(null) 创建的对象[[Prototype]] 属性为null，并且没
> 有valueOf() 和toString() 方法，因此无法进行强制类型转换

#### ToBoolean ####
*假值*

- undefined
- null
- false
- +0、-0 和NaN
- ""

假值对象 document.all


强制转换 + ~~ ~

	0 | -0; // 0
	0 | NaN; // 0
	0 | Infinity; // 0
	0 | -Infinity; // 0

*parseInt*

parseInt 针对字符串，第一个字符决定基数(x,X,0)

08,09会出错，最好指定第二个参数

向parseInt(..) 传递非字符串，此时JavaScript
会将参数强制类型转换为它能够处理的字符串。

parseInt(1/0,19) === 18 | parseInt(Infinity, 19)

19为基数的有效数字是 0-9,a-i;

**parseInt 最大基数是36**; 0-9，a-z 36个字母

	parseInt( 0.000008 ); // 0 ("0" 来自于 "0.000008")
	parseInt( 0.0000008 ); // 8 ("8" 来自于 "8e-7")
	parseInt( false, 16 ); // 250 ("fa" 来自于 "false")
	parseInt( parseInt, 16 ); // 15 ("f" 来自于 "function..")
	parseInt( "0x10" ); // 16
	parseInt( "103", 2 ); // 2

a + "" 会对a 调用valueOf() 方法，然后通过ToString 抽象
操作将返回值转换为字符串。而String(a) 则是直接调用ToString()

	var a = {
	valueOf: function() { return 42; },
	toString: function() { return 4; }
	};
	a + ""; // "42"
	String( a ); // "4"

- 是数字减法运算符，因此a - 0 会将a 强制类型转换为数字

+ 如果+ 的其中一个操作数是字符串（或者通过以上步骤可以得到字符串），
则执行字符串拼接；否则执行数字加法

**|| && 选择运算符**

- a||defaultValue 默认值 //小心假值
- a && foo() 保护运算符

符号不能够被强制类型转换为数字（显式和隐式都会产生错误），但可以被强制类型转换
为布尔值（显式和隐式结果都是true）。

#### toPrimitive ####

*toPrimitive(input,preferedType?)*

    input是输入的值，preferedType是期望转换的类型，他可以是字符串，也可以是数字。

    如果转换的类型是number，会执行以下步骤：

     1. 如果input是原始值，直接返回这个值；

     2. 否则，如果input是对象，调用input.valueOf()，如果结果是原始值，返回结果；

     3. 否则，调用input.toString()。如果结果是原始值，返回结果；

     4. 否则，抛出错误。

     如果转换的类型是String，2和3会交换执行，即先执行toString()方法。

    你也可以省略preferedType，此时，日期会被认为是字符串，而其他的值会被当做Number。

加号操作符会将preferedType看成Number，调用ES内部的toPrimitive(input，Number)方法，得到空字符串

>[]+{}
>"[object Object]"

最终会调用双方的toString()方法，再做字符串加法


>{}+[]
>0

但是空对象加空数组就不一样了，加号运算符的定义是这样的：如果其中一个是字符串，另一个也会被转换为字符串，否则两个运算数都被转换为数字。而同时，javascript有这样的特性，如果{}既可以被认为是代码块，又可以被认为是对象字面量，那么js会把他当做代码块来看待。

#### 宽松相等和严格相等 ####
== 允许在相等比较中进行强制类型转换，而=== 不允许

*字符串和数字之间的相等比较*

ES5 规范11.9.3.4-5 这样定义：

- (1) 如果Type(x) 是数字，Type(y) 是字符串，则返回x == ToNumber(y) 的结果。
- (2) 如果Type(x) 是字符串，Type(y) 是数字，则返回ToNumber(x) == y 的结果。

*其他类型和布尔类型之间的相等比较*

- (1) 如果Type(x) 是布尔类型，则返回ToNumber(x) == y 的结果；
- (2) 如果Type(y) 是布尔类型，则返回x == ToNumber(y) 的结果。

*null 和undefined 之间的相等比较*

ES5 规范11.9.3.2-3 规定：

- (1) 如果x 为null，y 为undefined，则结果为true。
- (2) 如果x 为undefined，y 为null，则结果为true。

*对象和非对象之间的相等比较*

ES5 规范11.9.3.8-9 做如下规定：

- (1) 如果Type(x) 是字符串或数字，Type(y) 是对象，则返回x == ToPrimitive(y) 的结果；
- (2) 如果Type(x) 是对象，Type(y) 是字符串或数字，则返回ToPromitive(x) == y 的结果。
	
		var a = null;
		var b = Object( a ); // 和Object()一样
		a == b; // false
		var c = undefined;
		var d = Object( c ); // 和Object()一样
		c == d; // false
		var e = NaN;
		var f = Object( e ); // 和new Number( e )一样
		e == f; // false

null 和undefined 不能够被封装（boxed），Object(null)
和Object() 均返回一个常规对象

	var i = 2;
	Number.prototype.valueOf = function() {
	return i++;
	};
	var a = new Number( 42 );
	if (a == 2 && a == 3) {
	console.log( "Yep, this happened." );
	}

a.valueOf() 每次调用都产生副作用

- "0" == null; // false
- "0" == undefined; // false
- "0" == false; // true -晕！
- "0" == NaN; // false
- "0" == 0; // true
- "0" == ""; // false
- false == null; // false
- false == undefined; // false
- false == NaN; // false
- false == 0; // true -晕！
- false == ""; // true -晕！
- false == []; // true -晕！
- false == {}; // false
- "" == null; // false
- "" == undefined; // false
- "" == NaN; // false
- "" == 0; // true -晕！
- "" == []; // true -晕！
- "" == {}; // false
- 0 == null; // false
- 0 == undefined; // false
- 0 == NaN; // false
- 0 == []; // true -晕！
- 0 == {}; // false

> 不要有这样的想法，觉得“编程语言应该阻止我们犯错误”。

	[] == ![] // true

    2 == [2]; // true
    "" == [null]; // true

    0 == "\n"; // true

，""、"\n"（或者" " 等其他空格组合）等空字符串被ToNumber 强制类型转换
为0。这样处理总没有问题了吧，不然你要怎么办？



> 类型转换总会出现一些特殊情况，并非只有强制类型转换，任何编程语言都是如此。问题
> 出在我们的臆断（有时或许碰巧猜对了？！），但这并不能成为诟病强制类型转换机制的
> 理由。

*安全运用隐式强制类型转换*

- 如果两边的值中有true 或者false，千万不要使用==。
- 如果两边的值中有[]、"" 或者0，尽量不要使用==。

typeof 总是
返回七个字符串之一（参见第1 章），其中没有空字符串，typeof x == "function" 是100% 安全
的

#### 抽象关系比较 ####
- 比较双方首先调用ToPrimitive，如果结果出现非字符串，就根据ToNumber 规则将双方强制类型转换为数字来进行比较。
- 如果比较双方都是字符串，则按字母顺序来进行比较

		var a = { b: 42 };
		var b = { b: 43 };
		a < b; // false
		a == b; // false
		a > b; // false
		a <= b; // true
		a >= b; // true

> 实际上在比较两个对象的时
> 候，== 和=== 的工作原理是一样的
> 
> 因为根据规范a <= b 被处理为b < a，然后将结果反转,
> 实际上JavaScript 中<= 是
> “不大于”的意思

**相等比较有严格相等，关系比较却没有“严格关系比较”（strict relational comparison）。也
就是说如果要避免a < b 中发生隐式强制类型转换，我们只能确保a 和b 为相同的类型，
除此之外别无他法。**
## 语法 ##
### 语句和表达式 ###
“句子”（sentence）是完整表达某个意思的一组词，由一个或多个“短语”（phrase）组成，
它们之间由标点符号或连接词（and 和or 等）连接起来

> 语句都有一个结果值（statement completion value，undefined 也算）,但我们在代码中是没有办法获得这个结果值的

{ .. } 的结果值是其最后一个语句/ 表达式的结果

**可以使用万恶的eval(..)（又读作“evil”）来获得结果值**

> ++a++ 会产生ReferenceError 错误,，因为++ 无法直接
> 在42 这样的值上产生副作用

a = 42 中的= 运算符看起来没有副作用，实际上它的结果值是42，它的副作用是将42 赋
值给a。

如果变量b 没有在作用域中象var b 这样声明过，则var a = b =
42 不会对变量b 进行声明。

使用 a&&b 代替 if(a) b;

	{
		**foo: bar()**//标签语句
	}

很多开发人员以为这里的{ .. } 只是一个孤立的对象常量，没有赋值。
{ .. } 在这里只是一个普通的代码块

在控制
台中输入{"a":42} 会报错。

> JSON 的确是JavaScript 语法的一个子集，但是JSON 本身并不是合法的JavaScript 语法。

JSON-P（将JSON 数据封装为函数调用，
比如foo({"a":42})）通过将JSON 数据传递给函数来实现对其的访问

> JSON-P 能将JSON 转换为合法的JavaScript 语法。

	[] + {}; // "[object Object]"
	{} + []; // 0 ,{}被当作空代码块

{ .. } 还可以用作函数命名参数（named function argument）的对象解构（object destructuring）

	function foo({ a, b, c }) {
		// 不再需要这样:
		// var a = obj.a, b = obj.b, c = obj.c
		console.log( a, b, c );
	}

**事实上JavaScript 没有else if，但if 和else 只包含单条语句的时候可以省略代码块的**

### 优先级 ###

短路保护

- ， 运算符最低
- &&优先于||
- && 和|| 运算符先于? : 执行

> 如果&& 是右关联的话会被处理为a && (b && c)。但这并不意味着c 会在b
> 之前执行。右关联不是指从右往左执行，而是指从右往左组合。任何时候，
> 不论是组合还是关联，严格的执行顺序都应该是从左到右，a，b，然后c。

因为是右关联，所以它实际上是这样
来处理的：a = (b = (c = 42))
### 自动分号 ###
	var a = 42;
	do {
		// ..
	} while (a) // <-- 这里应该有;
	a;

语法规定do..while 循环后面必须带;，而while 和for 循环后则不需要。大多数开发人员
都不记得这一点，此时ASI 就会自动补上分号。

其他涉及ASI 的情况是break、continue、return 和yield（ES6）等关键字：

	function foo(a) {
		if (!a) return
		a *= 2;
		// ..
	}

由于ASI 会在return 后面自动加上;，所以这里return 语句并不包括第二行的a *= 2。
return 语句的跨度可以是多行，但是其后必须有换行符以外的代码：

> ASI 是一个语法纠错机制。若将换行符当作有意义的字符来对待，就会遇到很多
> 问题。多希望在1995 年5 月的那十天里（ECMAScript 规范制定期间），我让换
> 行符承载了更多的意义。但切勿认为ASI 真的会将换行符当作有意义的字符。 —————— brendan Eich

var 不会执行ASI；一下代码会报错，添加;会正确执行

	var a = 'x'
	/^.$/.test(a);

### 错误 ###
> 在编译阶段发现的代码错误叫作“早期错误”（early error）。语法错误是早期错误的一种
> （如a = ,）这些错误在代码执行之前是无法用try..catch 来捕获的，相反，它们还会导致解析/ 编译失败。

#### 提前使用变量 ####
TDZ(Temporal Dead Zone)

	{
		typeof a; // undefined
		typeof b; // ReferenceError! (TDZ)
		let b;
	}

另一个TDZ 违规的例子是ES6 中的参数默认值

	var b = 3;
	function foo( a = 42, b = a + b + 5 ) {
		// ..
	}
b = a + b + 5 在参数b（= 右边的b，而不是函数外的那个）的TDZ 中访问b，

在ES6 中，如果参数被省略或者值为undefined，则取该参数的默认值

	function foo( a = 42, b = a + 1 ) {
		console.log(
		arguments.length, a, b,
		arguments[0], arguments[1]
		);
	}
	foo(); // 0 42 43 undefined undefined
	foo( 10 ); // 1 10 11 10 undefined
	foo( 10, undefined ); // 2 10 11 10 undefined
	foo( 10, null ); // 2 10 null 10 null

虽然参数a 和b 都有默认值，但是函数不带参数时，arguments 数组为空。

相反，如果向函数传递undefined 值，则arguments 数组中会出现一个值为undefined 的单
元，而不是默认值。

向函数传递参数时，arguments 数组中的对应单元会和命名参数建立关联（linkage）以得
到相同的值。

**finally执行完，函数才会放回值；**

finally中的return会覆盖前面的return；

yield可以看做是return的中间版本，但是yield在generator重新开始时才会执行，所以finally不会再yield之后立刻执行

### 宿主对象 ###
	var a = document.createElement( "div" );
	typeof a; // "object"--正如所料
	Object.prototype.toString.call( a ); // "[object HTMLDivElement]"
	a.tagName; // "DIV"

DOM 元
素。其内部的[[Class]] 值（为"HTMLDivElement"）来自预定义的属性

宿主对象的行为差异有

- 无法访问正常的object 内建方法，如toString();
- 无法写覆盖；
- 包含一些预定义的只读属性；
- 包含无法将this 重载为其他对象的方法；
- 其他……

> 由于浏览器演进的历史遗留问题，在创建带有id 属性
> 的DOM 元素时也会创建同名的全局变量

**这也是尽量不要使用全局变量的一个原因**

#### 在扩展原生方法时需要加入判断条件 ####
	if (!Array.prototype.push) {
		// Netscape 4没有Array.push
		Array.prototype.push = function(item) {
		this[this.length-1] = item;
		};
	}

*shim/polyfill*

在低版本浏览器上引入polyfill


不同的script之间共享global对象，但是全局变量提升的作用域不适用

> 字符串常量中的</script将会被当作结束标签来处理，
> 因此会导致错误。常用的变通方法是：

## 异步 ##
异步是关于现在和将来的时间间隙，而并行是关于能够同时发生的事情

在JavaScript 的特性中， 这种函数顺序的不确定性就是通常所说的竞态条件（racecondition），这种不确定性是在函数（事
件）顺序级别上，而不是多线程情况下的语句顺序级别

单线程事件循环是并发的一种形式
### 协作 ###
	var res = [];
	// response(..)从Ajax调用中取得结果数组
	function response(data) {
		// 一次处理1000个
		var chunk = data.splice( 0, 1000 );
		// 添加到已有的res组
		res = res.concat(
			// 创建一个新的数组把chunk中所有值加倍
			chunk.map( function(val){
				return val * 2;
			} )
		);
		// 还有剩下的需要处理吗？
		if (data.length > 0) {
			// 异步调度下一次批处理
			setTimeout( function(){
				response( data );
			}, 0 );
		}
	}
	// ajax(..)是某个库中提供的某个Ajax函数
	ajax( "http://some.url.1", response );
	ajax( "http://some.url.2", response );
### 任务队列 ###
它是挂在事件循环队列的每个tick 之后的一个队列
### 回调 ###
continuation

限制回调代码被多次执行

	var tracked = false;
	analytics.trackPurchase( purchaseData, function(){
		if (!tracked) {
			tracked = true;
			chargeCreditCard();
			displayThankyouPage();
		}
	} );

> “信任，但要核实。”

## Promise ##
Promise 是一种封装和组合未来值的易于复用的机制
### 完成事件 ###
一个Promise 决议后，这个Promise 上所有的通过
then(..) 注册的回调都会在下一个异步时机点上依次被立即调用

	p.then( function(){
		p.then( function(){
			console.log( "C" );
		} );
		console.log( "A" );
	} );
	p.then( function(){
		console.log( "B" );
	} );
	// A B C

"C" 无法打断或抢占"B"，这是因为Promise 的运作方式。，p1 不是用立即值而是用另一个promise p3 决
议，后者本身决议为值"B"。规定的行为是把p3 展开到p1，但是是异步地展开。所以，在
异步任务队列中，p1 的回调排在p2 的回调之后

，没有任何东西（甚至JavaScript 错误）能阻止Promise 向你通知它的决议（如果它
决议了的话）。如果你对一个Promise 注册了一个完成回调和一个拒绝回调，那么Promise
在决议时总是会调用其中的一个。

	var p3 = new Promise( function(resolve,reject){
		resolve( "B" );
	} );
	var p1 = new Promise( function(resolve,reject){
		resolve( p3 );
	} );
	p2 = new Promise( function(resolve,reject){
		resolve( "A" );
	} );
	p1.then( function(v){
		console.log( v );
	} );
	p2.then( function(v){
		console.log( v );
	} );
	// A B <-- 而不是像你可能认为的B A

### Promise超时模式 ###
	// 用于超时一个Promise的工具
	function timeoutPromise(delay) {
		return new Promise( function(resolve,reject){
			setTimeout( function(){
				reject( "Timeout!" );
			}, delay );
		} );
	}
	// 设置foo()超时
	Promise.race( [
		foo(), // 试着开始foo()
		timeoutPromise( 3000 ) // 给它3秒钟
	] )
	.then(
		function(){
			// foo(..)及时完成！
		},
		function(err){
			// 或者foo()被拒绝，或者只是没能按时完成
			// 查看err来了解是哪种情况
		}
	);

由于Promise 只能被决议一次，所以任何通过then(..) 注册的（每个）回调就只会被调
用一次。

如果你没有用任何值显式决议，那么这个值就是undefined

任何时间点上出现了一个JavaScript 异常错误，比如一个TypeError 或
ReferenceError，那这个异常就会被捕捉，并且会使这个Promise 被拒绝。

	var p = {
	then: function(cb) {
		cb( 42 );
	}
	};
	// 这可以工作，但只是因为幸运而已
	p
	.then(
		function fulfilled(val){
		console.log( val ); // 42
	},
	function rejected(err){
		// 永远不会到达这里
	}
	);

Promise.resolve()会将thenable对象的resolve处理函数的参数解出来

链式流程控制可行的Promise 固有特性:

- 调用Promise 的then(..) 会自动创建一个新的Promise 从调用返回。
- 在完成或拒绝处理函数内部，如果返回一个值或抛出一个异常，新返回的（可链接的）
Promise 就相应地决议。
- 如果完成或拒绝处理函数返回一个Promise，它将会被展开，这样一来，不管它的决议
值是什么，都会成为当前then(..) 返回的链接Promise 的决议值

### 术语：决议、完成以及拒绝 ###
reject不会自动展开

> 前面提到的reject(..) 不会像resolve(..) 一样进行展开。如果向
> reject(..) 传入一个Promise/thenable 值，它会把这个值原封不动地设置为
> 拒绝理由。后续的拒绝处理函数接收到的是你实际传给reject(..) 的那个
> Promise/thenable，而不是其底层的立即值。

try..catch无法跨异步操作工作

#### error-first 回调风格 ####
	function foo(cb) {
		setTimeout( function(){
			try {
				var x = baz.bar();
				cb( null, x ); // 成功！
			}
			catch (err) {
				cb( err );
			}
		}, 100 );
	}
	foo( function(err,val){
		if (err) {
			console.error( err ); // 烦 :(
		}
		else {
			console.log( val );
		}
	} );


> 若向Promise.all([ .. ]) 传入空数组，它会立即完成，但Promise.
> race([ .. ]) 会挂住，且永远不会决议

### Promise 局限性 ###
	// polyfill安全的guard检查
	if (!Promise.wrap) {
		Promise.wrap = function(fn) {
			return function() {
				var args = [].slice.call( arguments );
				return new Promise( function(resolve,reject){
					fn.apply(
						null,
						args.concat( function(err,v){
							if (err) {
								reject( err );
							}
							else {
								resolve( v );
							}
						} )
					);
				} );
			};
		};
	}
#### 顺序错误处理 ####
> 即Promise 链中的错误很容易被
> 无意中默默忽略掉

#### 单一值 ####
> Promise 只能有一个完成值或一个拒绝理由

#### 单决议 ####
> Promise 最本质的一个特征是：Promise 只能被决议一次（完成或拒绝）

	// click(..)把"click"事件绑定到一个DOM元素
	// request(..)是前面定义的支持Promise的Ajax
	var p = new Promise( function(resolve,reject){
		click( "#mybtn", resolve );
	} );
	p.then( function(evt){
		var btnID = evt.currentTarget.id;
		return request( "http://some.url.1/?id=" + btnID );
	} )
	.then( function(text){
		console.log( text );
	} );

如果这个按钮被
点击了第二次的话，promise p 已经决议，因此第二个resolve(..) 调用就会被忽略

#### 无法取消的Promise ####
#### Promise 性能 ####

## 生成器 ##
	// 构造一个迭代器it来控制这个生成器
	var it = foo();
	// 这里启动foo()！
	it.next();
	x; // 2
	bar();
	x; // 3
	it.next(); // x: 3

1. it = foo() 运算并没有执行生成器*foo()，而只是构造了一个迭代器（iterator），这个
迭代器会控制它的执行。后面会介绍迭代器。
2. 第一个it.next() 启动了生成器*foo()，并运行了*foo() 第一行的x++。
3. *foo() 在yield 语句处暂停，在这一点上第一个it.next() 调用结束。此时*foo() 仍
在运行并且是活跃的，但处于暂停状态。
4. 我们查看x 的值，此时为2。
5. 我们调用bar()，它通过x++ 再次递增x。
6. 我们再次查看x 的值，此时为3。
7. 最后的it.next() 调用从暂停处恢复了生成器*foo() 的执行，并运行console.log(..)
语句，这条语句使用当前x 的值3。

**生成器就是一类特殊的函数，可以一次或多次启动和停止，并不一定非得要完成**

#### 迭代消息传递 ####
	function *foo(x) {
		var y = x * (yield);
		return y;
	}
	var it = foo( 6 );
	// 启动foo(..)
	it.next();
	var res = it.next( 7 );
	res.value; // 42

next(arg) 将arg作为被暂停的yield的表达式的结果；

。一般来说，需要的next(..) 调用要
比yield 语句多一个，**为什么会有这个不匹配？**

**因为第一个next(..) 总是启动一个生成器，并运行到第一个yield 处。**

> 消息是双向传递的——yield.. 作为一个
> 表达式可以发出消息响应next(..) 调用，next(..) 也可以向暂停的yield 表达式发送值。

	function *foo(x) {
		var y = x * (yield "Hello"); // <-- yield一个值！
		return y;
	}
	var it = foo( 6 );
	var res = it.next(); // 第一个next()，并不传入任何东西
	res.value; // "Hello"
	res = it.next( 7 ); // 向等待的yield传入7
	res.value; // 42

yield .. 和next(..) 这一对组合起来，在生成器的执行过程中构成了一个双向消息传递系统

**第一个next() 调用（没有参数的）基本上就是在提出一个问题：“生成器*foo(..) 要给我
的下一个值是什么”。谁来回答这个问题呢？第一个yield "hello" 表达式。**

return 语句回答这个问题！(最后一个next)

Object.keys(..) 并不包含来自于[[Prototype]] 链
上的属性，而for..in 则包含

### 生成器迭代器 ###
严格说来，生成器本身并不是iterable，尽管非常类似——当你执行一个生成器，就得到了一个迭代器：

调用it.return(..) 之后，它会立即终止生成器
### 异步迭代生成器 ###
#### 同步错误处理 ####
	try {
		var text = yield foo( 11, 31 );
		console.log( text );
	}
	catch (err) {
		console.error( err );
	}
	
	
	if (err) {
		// 向*main()抛出一个错误
		生成器 ｜ 253
		it.throw( err );
	}

### Promise并发 ###
	function *foo() {
		// 让两个请求"并行"
		var p1 = request( "http://some.url.1" );
		var p2 = request( "http://some.url.2" );
		// 等待两个promise都决议
		var r1 = yield p1;
		var r2 = yield p2;
		var r3 = yield request(
		"http://some.url.3/?v=" + r1 + "," + r2
		);
		console.log( r3 );
	}
	// 使用前面定义的工具run(..)
	run( foo );
### 生成器委托 ###
yield 委托的主要目的是代码组织，以达到与普通函数调用的对称。

从外层的迭代器（it）角度来说，是控制最开始的生成器还是控制委托的那个，没有任何
区别。

### 形实转换程序 ###
	function thunkify(fn) {
		var args = [].slice.call( arguments, 1 );
		return function(cb) {
			args.push( cb );
			return fn.apply( null, args );
		};
	}
	var fooThunk = thunkify( foo, 3, 4 );
	// 将来
	fooThunk( function(sum) {
		console.log( sum ); // 7
	} );

#### thunk+factory方案 ####
	function thunkify(fn) {
		return function() {
			var args = [].slice.call( arguments );
			return function(cb) {
				args.push( cb );
				return fn.apply( null, args );
			};
		};
	}

**用法**

	var whatIsThis = thunkify( foo );
	var fooThunk = whatIsThis( 3, 4 );
	
	// 将来
	fooThunk( function(sum) {
		console.log( sum ); // 7
	} );

#### Uncaught TypeError:CreateListArrayLike called on non-object ####
apply的第二个参数应该是个数组或者类数组
## 变量 ##
### var ###
var 操作符定义的变量将成为该变量的作用域中的局部变量

未经声明的变量赋值在严格模式下会抛出ReferenceError错误

### let ###
let只在命令坐在的代码块内有效

let声明的i在每个for循环轮次都是一个新的变量，

- 1.let生命会提升到块顶部
- 2.从块顶部到变量初始化语句，这块区域叫TDZ(临时死区)
- 3.如果在TDZ内使用该变量，JS就会报错
- 4.TDZ中typeof也会报错

#### 垃圾回收 ####

for 循环头部的let 不仅将i 绑定到了for 循环的块中，事实上它将其重新绑定到了循环
的每一个迭代中，确保使用上一个循环迭代结束时的值重新进行赋值。
下面通过另一种方式来说明每次迭代时进行重新绑定的行为：

	{
		let j;
		for (j=0; j<10; j++) {
		let i = j; // 每个迭代重新绑定！
		console.log( i );
		}
	}

let不会再全局对象内创建属性

	let allen = 'allen'
	window.allen = 'bob'
	allen // 'allen'

#### for循环特别之处，设置变量的那部分是一个父作用域，而循环体内是个单独的子作用域 ####

### 变量提升 ###
var命令会发生**变量提升**现象，在声明之前使用值为undfined，

let命令变量一定要在声明之后使用，否则报错

	foo(); // TypeError
	bar(); // ReferenceError
	var foo = function bar() {
	// ...
	};

这个代码片段经过提升后，实际上会被理解为以下形式：

	var foo;
	foo(); // TypeError
	bar(); // ReferenceError
	foo = function() {
	var bar = ...self...
	// ...
	}

#### Temporal dead zone ####
y作为默认值赋给x前尚未声明，ReferenceError

    function bar(x = y, y = 2) {
      return [x, y];
    }
    
    bar();

    let x = x; //声明x还没有完成，ReferenceError

## this ##

### 为什么会有this ###
> person.sayHi(person)
> 
> 开发者最想要的调用方式是 person.sayHi()，那么问题来了，如果 person.sayHi() 没有实参，person.sayHi 函数是如何接收到 person 的呢？

#### 自身作为参数 ####
> 方法1：依然把第一个参数 self 当做 person，这样形参就会永远比实参多出一个 self
> 
> 方法2：隐藏 self，然后用关键字 this 来访问 self。
> 
> JS 之父选择了方法2，用 this 访问 self。Python 之父选择了方法1，留下 self 作为第一个参数。

JS 之父给高手们准备了无糖的 .call 方法。

person.sayHi.call(person) 就等价于 person.sayHi()，
高手一般用 obj.fn.call(null,1,2,3) 来手动禁用 this。

也就是说 person.sayHi 虽然是 person 的方法，但是是可以调用在任何对象上的。

> **this 的绑定和函数声明的位置没有任何关系，只取决于函数的调用方式**
> 
> 当一个函数被调用时，会创建一个活动记录（有时候也称为执行上下文）。这个记录会包
> 含函数在哪里被调用（调用栈）、函数的调用方法、传入的参数等信息。this 就是记录的
> 其中一个属性，会在函数执行的过程中用到

**this 既不指向函数自身也不指向函数的词法作用域**

### 绑定规则 ###
#### 默认绑定 ####
函数调用时应用this 的默认绑定，this 指向全局对象

> 使用严格模式（strict mode），那么全局对象将无法使用默认绑定，因此this 会绑定
> 到undefined：

#### 隐式绑定 ####
调用位置是否有上下文对象，或者说是否被某个对象拥有或者包
含

*隐式丢失*

var bar = obj.foo;

#### 显式绑定 ####
们不想在对象内部包含函数引用，而想在某个对象上强制调用函数

call,apply

*硬绑定*

	var bar = function() {
		foo.call( obj );
	};

*创建一个包裹函数*

	var bar = function() {
		return foo.apply( obj, arguments );
	};

*辅助绑定函数*

	// 简单的辅助绑定函数
	function bind(fn, obj) {
		return function() {
			return fn.apply( obj, arguments );
		};
	}

ES5 中提供了内置的方法Function.prototype.
bind
#### new绑定 ####
**在JavaScript 中，构造函数只是一些
使用new 操作符时被调用的函数。**

> 实际上并不存在所谓的“构造函数”，只有对于函数的“构造调用”

1. 创建（或者说构造）一个全新的对象。
2. 这个新对象会被执行[[ 原型]] 连接。
3. 这个新对象会绑定到函数调用的this。
4. 如果函数没有返回其他对象，那么new 表达式中的函数调用会自动返回这个新对象。

#### 优先级 ####
- 显式绑定优先级更高；
- 可以看到new 绑定比隐式绑定优先级高
- new优于显示绑定

#### 空绑定 ####
避免this绑定到全局变量

	var ø = Object.create( null );
	// 把数组展开成参数
	foo.apply( ø, [2, 3] ); // a:2, b:3

*间接引用*

	(p.foo = o.foo)(); // 2

应用默认绑定

*软绑定*

	if (!Function.prototype.softBind) {
		Function.prototype.softBind = function(obj) {
			var fn = this;
			// 捕获所有 curried 参数
			var curried = [].slice.call( arguments, 1 );
			var bound = function() {
				return fn.apply(
				(!this || this === (window || global)) ?
				obj : this
				curried.concat.apply( curried, arguments )
				);
			};
			bound.prototype = Object.create( fn.prototype );
			return bound;
		};
	}

#### 词法作用域与箭头函数的this ####
	function foo(){
	    var a = 3;
	    setTimeout(()=>{
	        console.log(this.a);
	    },100);
	}
	
	var obj = {
	    a:2
	};
	
	foo.call(obj);//输出2

箭头函数的this查找类似词法作用域，逐步向外层作用域中的this查询,this绑定到查找的this，this的查找类似于其他标识符，**本质上this也只是一个对象变量，this不是作用域**

所以:上下文环境中包含this，上下文环境和作用域不是一个概念

所以:上下文环境包括函数执行中的各个变量，而变量值的确定是通过词法作用域确定的

> 作用域在函数定义时就已经确定了。而不是在函数调用时确定

**作用域范围内的标识符与this对象的属性不同**

**我的结论:理论上this.a 与 var a定义的a不是一个概念**

- this.a 等同于 [scope].this.a
- var a 等于 [scope].a

>没有全局变量或者全局函数(我也是这么想的)

在浏览器中，JavaScript在顶层作用域中定义的变量被绑定在window对象的属性中，查找顶层作用域的变量时，提供的是window对象的属性。

> window除了扮演ECMAScript定义的Global对象的角色之外，还承担了许多别的任务

全局作用域(scope)中上下文中始终是Window对象
#### IIEF的闭包的this是window ####
执行函数表达式(IIFE,即immediately-invoked function expression)

因为函数执行时，实际是window调用了它，也就是window.函数名();那么，里面的this指向当前调用该函数的对象，就是window



#### 执行js文件没有输出，可能是函数定义未被执行 ####


## 解构赋值 ##
> ES6 允许按照一定模式，从**数组**和**对象**中提取值，对变量进行赋值，这被称为解构（Destructuring）

### 数组解构赋值 ###
具有interator接口的可以使用数组的解构赋值

    function* fibs() {
      let a = 0;
      let b = 1;
      while (true) {
	    yield a;
	    [a, b] = [b, a + b];
	      }
	    }
    
    let [first, second, third, fourth, fifth, sixth] = fibs();
    sixth

#### 数组的拷贝 ####
	[...cloneColors] = colors

### 对象的解构赋值 ###
    let { foo: foo, bar: bar } = { foo: "aaa", bar: "bbb" };

let {p:v} p 模式，v变量

多次解构赋值

    let { loc, loc: { start }, loc: { start: { line }} } = node;

null与undefined不严格相等

    var {x = 3} = {x: null}; 解构有效

避免将{ 放在行首，JS将其解释为代码块

    let x;
    ({x} = {x: 1});

let { log, sin, cos } = Math;

### 字符串的解构赋值 ###
字符串被转换成类似数组的对象

每个数组的对象都有一个length属性

    let {length : len} = 'hello';

### 数值和布尔值的解构赋值 ###
先转为对象

#### 解构赋值的规则是，只要等号右边的值不是对象或数组，就先将其转为对象。由于undefined和null无法转为对象，所以对它们进行解构赋值，都会报错。 ####

### 函数参数的解构赋值 ###
*为函数参数的复制，而不是为x,y赋值*

    function move({x, y} = { x: 0, y: 0 }) {
      return [x, y];
    }

#### 赋值语句的非模式部分才可以使用圆括号 ####

### 解构的用途 ###
- 交换变量的值
- 从函数返回多个值
- 函数参数的定义
- 提取JSON数据
- 函数参数的默认值
- 遍历Map结构
- 输入模块的指定方法

## 模块 ##
1. 必须有外部的封闭函数，该函数必须至少被调用一次（每次调用都会创建一个新的模块
实例）。
2. 封闭函数必须返回至少一个内部函数，这样内部函数才能在私有作用域中形成闭包，并
且可以访问或者修改私有的状态。
#### 导出一个模块中的某个内容 ####

	import {ZipCodeValidator} from './ZipCodeValidator'

#### 将整个模块导入到一个变量，并通过它来访问模块的导出部分 ####

	import * as validator from "./ZipCodeValidator";
	let myValidator = new validator.ZipCodeValidator();

#### 具有副作用的导入模块 ####

设置一些全局状态供其它模块使用

	import "./my-module.js";

#### 默认导出 ####

	export default class ZipCodeValidator；
	
	import validator from "./ZipCodeValidator";
	
	let myValidator = new validator();

> 若要导入一个使用了export =的模块时，必须使用TypeScript提供的特定语法import module = require("module")


## 顶层对象 ##
**顶层对象的属性与全局变量挂钩，被认为是 JavaScript 语言最大的设计败笔之一**

> 顶层对象的属性与全局变量挂钩...带来了几个很大的问题，首先是没法在编译时就报出变量未声明的错误，只有运行时才能知道...；其次，程序员很容易不知不觉地就创建了全局变量...；最后，顶层对象的属性是到处可以读写的，这非常不利于模块化编程。另一方面，...顶层对象是一个有实体含义的对象。

ES6中

	let a = 1;
	console.log(window.a); // undefined

## 异步 ##
异步执行的运行机制如下

（1）所有同步任务都在主线程上执行，形成一个执行栈（execution context stack）。

（2）主线程之外，还存在一个"任务队列"（task queue）。只要异步任务有了运行结果，就在"任务队列"之中放置一个事件。

（3）一旦"执行栈"中的所有同步任务执行完毕，系统就会读取"任务队列"，看看里面有哪些事件。那些对应的异步任务，于是结束等待状态，进入执行栈，开始执行。

（4）主线程不断重复上面的第三步。


## Promise ##
> Promise 对象新建，函数内容就会立刻执行
> 
> Promise 中的return将会传递给 Promise链下一个then中函数的参数
> 
> Promise(function(resolve,reject){/**/}) 中
> 
> resolve函数的调用将会使状态转为完成，并注册了函数调用
> 
> reject函数的调用将会使状态转为拒绝，并注册函数调用，触发后续的catch处理事件
> 
> 注册的函数将会异步调用

## Generator ##

> 生成器函数不立刻执行，直到next()方法被调用
> 
> yield 将会中断函数调用，使函数分段执行
> 
> 返回的生成器内部属性 {value:**,done:true/false}
> 
> next()的参数作为**上一个**yield的返回值，yield默认返回undefined

> Generator 函数可以暂停执行和恢复执行，它还有两个特性，使它可以作为异步编程的完整解决方案：函数体内外的数据交换和错误处理机制

#### 异步封装Case ####

    var fetch = require('node-fetch');
    
    function* gen(){
      var url = 'https://api.github.com/users/github';
      var result = yield fetch(url);
      console.log(result.bio);
    }


#### 调用过程 ####

    var g = gen();
    var result = g.next();
    
    result.value.then(function(data){
      return data.json();
    }).then(function(data){
      g.next(data);
    });



### TypedArray ###

> ArrayBuffer对象代表储存二进制数据的一段内存，它不能直接读写，只能通过视图（TypedArray视图和DataView视图)来读写，视图的作用是以指定格式解读二进制数据

	const buffer = new ArrayBuffer(12);
	
	const x1 = new Int32Array(buffer);
	x1[0] = 1;
	const x2 = new Uint8Array(buffer);
	x2[0]  = 2;
	
	x1[0] // 2
## CPS（Continuation Programming Style） ##
> 我们在学习函数时只了解了一半事实，因为我们基于一个错误的假定：函数只能将结果返回到它的调用端

## Async函数 ##
> async函数的返回值包装成Promise
> 
> await 的返回值是Promise的对象，resolve(data)的data

## Chrome调试 ##

> Snippets：Identifier 'obj' has already been declared

每次当前页面js运行都在给页面的js环境中，故每次定义变量，都会添加到全局js环境中，刷新页面即可



## Proxy ##
代理对象的行为

    var obj = new Proxy({}, {
      get: function (target, key, receiver) {
      console.log(`getting ${key}!`);
      return Reflect.get(target, key, receiver);
      },
      set: function (target, key, value, receiver) {
      console.log(`setting ${key}!`);
      return Reflect.set(target, key, value, receiver);
      }
    });


*没有设置拦截，就直接通向原对象*

直接在对象上调用

    var object = { proxy: new Proxy(target, handler) };


使用proxy实现链式调用

    var pipe = (function () {
      return function (value) {
	    var funcStack = [];
	    var oproxy = new Proxy({} , {
	      get : function (pipeObject, fnName) {
	        if (fnName === 'get') {
	          return funcStack.reduce(function (val, fn) {
	            return fn(val);
	          },value);
	        }
	        funcStack.push(window[fnName]);
	        return oproxy;
	      }
	    });
	
	    return oproxy;
	      }
    }());
    
    var double = n => n * 2;
    var pow= n => n * n;
    var reverseInt = n => n.toString().split("").reverse().join("") | 0;
    
    pipe(3).double.pow.reverseInt.get; // 63

生成dom对象

    const dom = new Proxy({}, {
      get(target, property) {
	    return function(attrs = {}, ...children) {
	      const el = document.createElement(property);
	      for (let prop of Object.keys(attrs)) {
	        el.setAttribute(prop, attrs[prop]);
	      }
	      for (let child of children) {
	        if (typeof child === 'string') {
	          child = document.createTextNode(child);
	        }
	        el.appendChild(child);
	      }
	      return el;
	    }
      }
    });
    
    const el = dom.div({},
      'Hello, my name is ',
      dom.a({href: '//example.com'}, 'Mark'),
      '. I like:',
      dom.ul({},
        dom.li({}, 'The web'),
	    dom.li({}, 'Food'),
	    dom.li({}, '…actually that\'s it')
      )
    );
    
    document.body.appendChild(el);

*如果一个属性不可配置（configurable）和不可写（writable），则该属性不能被代理，通过 Proxy 对象访问该属性会报错。*


*设置私有不可访问属性*
    
    const handler = {
      get (target, key) {
	    invariant(key, 'get');
	    return target[key];
      },
      set (target, key, value) {
	    invariant(key, 'set');
	    target[key] = value;
	    return true;
      }
    };
    function invariant (key, action) {
      if (key[0] === '_') {
	    throw new Error(`Invalid attempt to ${action} private "${key}" property`);
      }
    }
    const target = {};
    const proxy = new Proxy(target, handler);
    proxy._prop
    // Error: Invalid attempt to get private "_prop" property
    proxy._prop = 'c'
    // Error: Invalid attempt to set private "_prop" property
    
*变量p是 Proxy 的实例，当它作为函数调用时（p()），就会被apply方法拦截*

*回收代理权*

    let target = {};
    let handler = {};
    
    let {proxy, revoke} = Proxy.revocable(target, handler);
    
    proxy.foo = 123;
    proxy.foo // 123
    
    revoke();
    proxy.foo // TypeError: Revoked

> 有些原生对象的内部属性，只有通过正确的this才能拿到，所以 Proxy 也无法代理这些原生对象的属性

## Reflect ##
### 设计目的 ###
- 将Object对象的一些明显属于语言内部的方法，放到Reflect对象上
- 修改某些Object方法的返回结果，让其变得更合理
- 让Object操作都变成函数行为
- Reflect对象的方法与Proxy对象的方法一一对应

## Decorator ##
### 修饰类 ###
- 使用函数作为装饰器，装饰的类作为函数的参数
- 使用类作为，装饰的类添加类的行为

## Property Descriptor ##
属性的描述符，可以描述value为值，或者函数，或者属性的getter和setter

### Data descriptor ###
Mandatory properties:

- value

Optional properties:

- configurable
- enumerable
- writable

Sample:

    {
	    value: 5,
	    writable: true
    }

> 在调用Object.
> defineProperty()方法时，如果不指定， configurable， enumerable， writable特性的默认值都是false
### Accessor descriptor ###
Mandatory properties:


- Either get or set or both

Optional properties:

- configurable
- enumerable
- Sample: 
- 
    {
	  	get: function () {
			return 5;
		},
		enumerable: true
	}

### 废除旧的属性的使用 ###

    [
      'json',
      'urlencoded',
      'bodyParser',
      'compress',
      'cookieSession',
      'session',
      'logger',
      'cookieParser',
      'favicon',
      'responseTime',
      'errorHandler',
      'timeout',
      'methodOverride',
      'vhost',
      'csrf',
      'directory',
      'limit',
      'multipart',
      'staticCache',
    ].forEach(function (name) {
      Object.defineProperty(exports, name, {
	    get: function () {
	      throw new Error('Most middleware (like ' + name + ') is no longer bundled with Express and must be installed separately. Please see https://github.com/senchalabs/connect#middleware.');
	    },
	    configurable: true
	      });
    });

## 闭包 ##
**外部函数每次执行返回内部函数的引用会产生一个闭包，这个闭包是不共享的；**

全局上下文只有一个，当JavaScript程序开始执行时就已经创建了全局上下文；而函数执行上下文时在每次调用函数时，就会创建一个新的；

	setTimeout(function a(){console.log('a')},10);
	
	setTimeout("a",10);

字符串生成的函数，无法访问闭包中的值，其中的this是全局对象window；相当于是在全局范围内生成的函数


> 当函数可以记住并访问所在的词法作用域时，就产生了闭包

	function foo() {
		var a = 2;
		function bar() {
			console.log( a ); // 2
		}
		bar();
	}
	foo();

例子

	function foo() {
		var a = 2;
		function bar() {
		console.log( a );
		}
		return bar;
	}
	var baz = foo();
	baz(); // 2 —— 朋友，这就是闭包的效果。

**函数bar() 具有一个涵盖foo() 作用域的闭包**

bar() 依然持有对该作用域的引用，而这个引用就叫作闭包

**闭包的存在阻止了垃圾回收**

> 在foo() 执行后，通常会期待foo() 的整个内部作用域都被销毁，因为我们知道引擎有垃
> 圾回收器用来释放不再使用的内存空间。由于看上去foo() 的内容不会再被使用，所以很
> 自然地会考虑对其进行回收。

**无论通过何种手段将内部函数传递到所在的词法作用域以外，它都会持有对原始定义作用
域的引用，无论在何处执行这个函数都会使用闭包**

### 模块 ###

> 一个具有函数属性的对象本身并不是真正的模块。从方便观察的角度看，一个从函数调用
> 所返回的，只有数据属性而没有闭包函数的对象并不是真正的模块。

#### 模块模式另一个简单但强大的变化用法是，命名将要作为公共API 返回的对象 ####
	var foo = (function CoolModule(id) {
	function change() {
		// 修改公共API
		publicAPI.identify = identify2;
	}
	function identify1() {
		console.log( id );
	}
	function identify2() {
		console.log( id.toUpperCase() );
	}
	var publicAPI = {
		change: change,
		identify: identify1
	};
		ublicAPI;
	})( "foo module" );
	foo.identify(); // foo module
	foo.change();
	foo.identify(); // FOO MODULE

#### 现代的模块机制 ####
模块管理器

	var MyModules = (function Manager(){
	    var modules = []
	    function defines(name,deps,impl){
	       for(var i = 0; i < deps.length;i++){
	           deps[i] = modules[deps[i]];
	       }
	       modules[name]=impl.apply(impl,deps);
	    }
	    function get(name){
	        return modules[name];
	    }
	    return{
	        defines:defines,
	        get:get
	    }
	})();
	
	MyModules.defines("bar",[],function(){
	    function hello(who){
	        return "Let me introcude " + who;
	    }
	
	    return {
	        hello:hello
	    }
	});
	
	MyModules.defines("foo",["bar"],function(bar){
	    var hungry = "hippo";
	    function awesome(){
	        console.log(bar.hello(hungry).toUpperCase());
	    }
	    return {
	        awesome:awesome
	    }
	});
	
	var bar = MyModules.get("bar");
	var foo = MyModules.get("foo");
	
	console.log(bar.hello("hippo"));
	foo.awesome();

**模块模式的两个
特点：为函数定义引入包装函数，并保证它的返回值和模块的API 保持一致。**

**实际上闭包只是一个标准，显然就是关于如何在函数作为值按需传递的词法环境中书写代码的**

当函数可以记住并访问所在的词法作用域，即使函数是在当前词法作用域之外执行，这时就产生了闭包。

> 模块有两个主要特征：
> 
> （1）为创建内部作用域而调用了一个包装函数；
> 
> （2）包装函数的返回
> 值必须至少包括一个对内部函数的引用，这样就会创建涵盖整个包装函数内部作用域的闭
> 包。

#### import函数 ####
ES6 import()返回一个Promise对象

import()加载模块成功之后，这个模块会作为一个对象，当做then方法的参数，因此可以使用对象解构赋值的语法，获取输出接口

    import('./myModule.js')
    .then(({export1, export2}) => {
    // ...·
    });

## 函数 ##
> 由于函数也是对象，它们本身也可以拥有属
> 性

function add(a,b){
    return a+b;
}

add.multiply = function(a,b){
    return a * b;
}

add.join = function(){
    for(let item of this.args){
        console.log(item);
    }
}

add.args = [1,2,3,4,5,6];

console.log(add(3,4));
console.log(add.multiply(3,4));
console.log(add.join());
### IIEF ###
解决undefined 标识符的默认值被错误覆盖导致的异常

	undefined = true; // 给其他代码挖了一个大坑！绝对不要这样做！
	(function IIFE( undefined ) {
	var a;
	if (a === undefined) {
	console.log( "Undefined is safe here!" );
	}
	})();

IIFE 还有一种变化的用途是倒置代码的运行顺序，将需要运行的函数放在第二位，在IIFE
执行之后当作参数传递进去

	var a = 2;
	(function IIFE( def ) {
	def( window );
	})(function def( global ) {
	var a = 3;
	console.log( a ); // 3
	console.log( global.a ); // 2

### 块作用域 ###
#### with ####
块作用域的一
个例子（块作用域的一种形式），用with 从对象中创建出的作用域仅在with 声明中而非外
部作用域中有效。

#### try/catch ####
定try/catch 的catch 分句会创建一个块作
用域，其中声明的变量仅在catch 内部有效。

	try {
	undefined(); // 执行一个非法操作来强制制造一个异常
	}
	catch (err) {
	console.log( err ); // 能够正常执行！
	}
	console.log( err ); // ReferenceError: err not found

**ES6** 

	{
		let a = 2;
		console.log( a ); // 2
	}
	console.log( a ); // ReferenceError

**ES5**

	try{throw 2;}catch(a){
		console.log( a ); // 2
	}
	console.log( a ); // ReferenceError

**Traceur**
	{
		try {
			throw undefined;
		} catch (a) {
			a = 2;
			console.log( a );
		}
	}
	console.log( a );

相当于 let a = 2；

将ES6 代码转换成兼容ES6 之前
的环境
### apply, call, bind ###

apply: 第一个参数为环境对象，第二个参数是参数数组（Array 或 arguments对象）

call: 第一个参数为环境对象，后面的是直接传递参数

bind: 返回绑定环境对象的函数实例

apply和call实际执行了函数，bind只是反回了一个新函数实例

return 返回 undefined；

arguments与对应的命名参数值同步，它们并不是同一内存空间，但是值会同步

arguments的length是由传入的参数个数决定，并非定义的参数个数

**arguments.callee 已经被弃用，不应该再
使用它。**

**ECMAScript中所有的参数都是值传递，不可能通过引用传递参数**

没有重载，参数是有包含0或多个值的数组表示

### 箭头函数 ###
> ES6 中有一个主题用非常重要的方式将this
> 同词法作用域联系起来了

	self = this
self 只是一个可以通过
词法作用域和闭包进行引用的标识符，不关心this 绑定的过程中发生了什么

箭头函数将程序员们经常犯的一个错误给标准化了，
也就是混淆了this 绑定规则和词法作用域规则，**使用bind更好**

> 事实上 =>箭头函数并不绑定 this，arguments，super(ES6)，抑或 new.target(ES6)。
> 
> 这是真的，对于上述的四个（未来可能有更多）地方，箭头函数不会绑定那些局部变量，所有涉及它们的引用，都会沿袭向上查找外层作用域链的方案来处理。

    function foo() {
       setTimeout( () => {
	      console.log("args:", arguments);
       },100);
    }

    foo( 2, 4, 6, 8 );
	// args: [2, 4, 6, 8]

=>箭头函数并没有绑定 arguments，所以它会以 foo() 的 arguments 来取而代之，而 super 和 new.target 也是一样的情况。

所以如果箭头函数直接返回一个对象，必须在对象外面加上括号，否则会报错

	// 报错
	let getTempItem = id => { id: id, name: "Temp" };
	
	// 不报错
	let getTempItem = id => ({ id: id, name: "Temp" });

## 事件触发 ##

[自定义事件](https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Creating_and_triggering_events)

### 事件流程 ###

- document.createEvent()
- Event.initEvent()
- EventTarget.dispatchEvent()
- EventTarget.addEventListener()

### 事件类型 ###
*UI事件*

#### load ####
image，script，link的动态加载在指定src或href之后开始加载

	var image = Image();//加载后将image的src赋给DOM元素

	var script = document.createElement("script");
	script.src = "example.js"
	document.body.append(script);

	var link = document.createElement("link");
	link.type = "text/css";
	link.rel = "stylesheet";
	link.href = "example.css";

#### unload ####
一个页面跳转到另一个页面都会发生一个unload事件；

此时页面加载后存在的对象就不一定存在了

#### resize ####
浏览器窗口被调整到一个新的高度或者宽度时会触发

Firefox会在用户停止调整窗口大小时才出发resize事件；

浏览器窗口最小化或最大化时也会出发resize事件；

#### scroll事件 ####
scroll是在window对象上发生的，混杂模式下可以通过scrollLeft和scrollTop来监控这一变化

*焦点事件*

focus和blur不冒泡， focusout和focusin冒泡

聚焦和失焦元素的事件分别是获得焦点的元素和失去焦点的元素

#### 事件代理 ####
在根元素上设置事件监听，事件最终会冒泡到根元素上，不必每个子元素上都设置监听事件，如此减少了内存消耗

	<p id="testParagraph">
		Some text.
		<input id="testInput" />
	</p>
	
	$('testParagraph').onfocus = handleEventPar;
	$('testInput').onfocus = handleEventInput;

focus事件不冒泡，handleEventPar事件将不会被触发

	$('testParagraph').addEventListener('focus',handleEventPar,true);
	$('testInput').addEventListener('focus',handleEventInput,true);

在捕获阶段设置监听事件，提前截获事件

*鼠标和滚轮事件*

click,dblclick,mousedown,mouseenter,mouseleave,mousemove,mouseout,mouseover,mouseup;

除了mouseenter和mouseleave，所有鼠标事件都会冒泡；

- ClientX,ClientY 鼠标在窗口中的位置
- pageX，pageY 页面点在页面中的位置
- screenX，screenY， 点在电脑屏幕汇总的位置

按键:shiftKey,ctrlKey,altKey,metaKey;

相关元素：mouseover和mouseout事件时，相对于失去或获得光标的元素；DOM relatedTarget, IE:fromElement,toElement

DOM button属性；中|次|主；响应的二进制位

detail：点击次数

mousewheel：wheelDelta 120的倍数

Firefox支持一个 DOMMouseScroll事件滚轮信息保存在detail中，是3的倍数，方向为负

#### 触摸设备 ####
- safari中：不支持dblclick，双击浏览器会放大画面，并无法更改该行为；
- 轻击元素产生mousemove事件，若事件导致内容变化，将不再有其他事件发生；若屏幕没有变化，一次发生mousedown,mouseup和click事件；
- mousemove事件也会出发mouseover和mouseout事件
- 两个手指放在屏幕上且页面随手指移动而滚动时触发mousewheel和scroll事件 

*键盘和鼠标事件*

- keydown:当键盘按下任意键时触发，如果按住不放，会重复触发此事件；
- keypress:当键盘按下字符键时触发，如果按住不放，会重复触发此事件；
- keyup:当键盘释放时触发； div无法添加keyup事件，因为无法获取焦点，可以添加tabindex，
- textInput：将文本显示给用户之间拦截文本，文本插入文本框之前会触发textInput；

tabindex：为负时，无法通过键盘tab来聚焦；为0时，通过键盘tab来聚焦；tab为正时，会根据值的大小一次获得焦点；通过添加tabindex可以获取失焦事件

textInput：inputMethod，表示文本输入到文本框中的方式

复合事件：处理IME的输入序列

*变动事件*

#### 删除节点 ####

DOMNodeRemoved事件，target是被删除的节点，relatedNode包含对目标节点父节点的引用，子节点尚未移除时，parentNode与relatedNode相同；会冒泡；

如果被移除的节点包含子节点，在器所有子节点和这个被移除的节点上会触发DOMNodeRemovedFromDocument事件；target是被移除的子节点或被移除的节点；不会冒泡；

随后触发DOMSubtreeModified，这个事件的target是被移除节点的父节点

#### 插入节点 ####
首先触发DOMNodeInserted事件，target是被插入的节点，relatedNode包括一个对父节点的引用；冒泡的；

紧接着，DOMNodeInsertedIntoDocument事件。不冒泡；

最后，DOMSuntreeModified，触发于新插入节点的父节点；

*HTML5事件*

#### contextmenu ####
自定义右击按钮
#### beforeunload ####
在浏览器卸载页面之前触发，可以通过它取消卸载并继续使用原有页面

	window.onbeforeunload = cb //正常工作
	window.addEventListener('beforeunload',cb)//工作，但不弹框

#### DOMContentLoaded事件 ####
在形成完整的DOM树之后就触发，不理会图像，JavaScript,CSS文件或其他资源是否下载完毕

	setTimeout(function(){
	//action
	},0);

#### readystatechange事件 ####
提供文档与元素加载有关的信息 readystate

#### pageshow和pagehide ####
浏览器的前进后退按钮保存了页面的缓存，event.persisted 是否缓存

#### hashchange ####
URL参数列表及#后面的字符串发生变化时触发；event.oldURL,event.newURL;

*设备事件*

#### orientationchange事件 ####
0肖像；-90向右转；90向左转；
#### MozOrientation事件 ####
检测设备方向的变化；
#### deviceorientation ####
- alpha 围绕z轴变化时，y轴的度数差
- beta 围绕x轴旋转时，z轴的度数差
- gamma 围绕y轴变化时，x轴的度数差
- absolute 是否是绝对值
- compassCalibrated是否校准过

数值按右手定制，逆时针

#### devicemotion事件 ####
设备移动的加速度

- acceleration不考虑重力时，个方向加速度
- accelerationIncludingGravity 考虑z轴重力加速度时个分方向的加速度
- interval 以毫秒表示的事件值，常量；
- rotationRate：表示方向的alpah,beta和伽马属性的对象指

*触摸与手势事件*

#### 触摸事件 ####
- touchstart 当手指触摸屏幕时触发；即使已经有手指放在屏幕上
- touchmove 当手指在屏幕上滑动式连续地触发
- touchend 当手指从屏幕上移开时触发
- touchcancel 当系统停止跟踪触摸时触发

都会冒泡，也可以取消

- touches 当前跟踪的触摸操作的Touch对象的数组
- targetTouches 特定于事件目标的Touch对象的数组
- changTouches 表示自上次触摸以来发生了什么改变的数组

#### 手势事件 ####
- gesturestart 当一个手指已经按在屏幕上而另一个手指有触摸屏幕时触发
- gestrurechange 当触摸屏幕的任何一个手指的位置发生变化时触发
- gestureend 当任何一个手指从屏幕上移开时触发

- roatation：两个手指变化引起的旋转角度
- scale：两个手指尖距离的变化

### 事件委托 ###
在事件处理程序中删除按钮也能组织事件冒泡，目标元素在文档中是事件冒泡的前提
### 模拟事件 ###
document.createEvent(event)

- UIEvents 一般的UI事件，鼠标手事件和键盘事件都继承自UI事件
- MouseEvents 一般化的鼠标事件
- MutationEvents 一般化的变动事件
- HTMLEvent 一般化的HTML事件

target.dispatch(event)
### Passive Event Listeners ###
> Passive Event Listeners是Chrome提出的一个新的浏览器特性：Web开发者通过一个新的属性passive来告诉浏览器，当前页面内注册的事件监听器内部是否会调用preventDefault函数来阻止事件的默认行为，以便浏览器根据这个信息更好地做出决策来优化页面性能。当属性passive的值为true的时候，代表该监听器内部不会调用preventDefault函数来阻止默认滑动行为，Chrome浏览器称这类型的监听器为被动（passive）监听器。目前Chrome主要利用该特性来优化页面的滑动性能，所以Passive Event Listeners特性当前仅支持mousewheel/touch相关事件。

#### Chrome浏览器的一些概念 ####
1. 绘制（Paint）：将绘制操作转换成为图像的过程（比如软件模式下经过光栅化生成位图，硬件模式下经过光栅化生成纹理）。在Chrome中，绘制分为两部分实现：绘制操作记录部分（main-thread side）和绘制实现部分（impl-side）。绘制记录部分将绘制操作记录到SKPicture中，绘制实现部分负责将SKPicture进行光栅化转成图像；
1. 图层（Paint Layer）：在Chrome中，页面的绘制是分层绘制的，页面内容变化的时候，浏览器仅需要重新绘制内容变化的图层，没有变化的图层不需要重新绘制；
1. 合成（Composite）：将绘制好的图层图像混合在一起生成一张最终的图像显示在屏幕上的过程；
1. 渲染（Render）：可以简单认为渲染等价于绘制+合成；
1. UI线程（UI Thread）：浏览器的主线程，负责接收到系统派发给浏览器窗口的事件、资源下载等；
1. 内核线程（Main/Render Thread）：Blink内核及V8引擎运行的线程，如DOM树构建、元素布局、绘制（main-thread side）、JavaScript执行等逻辑在该线程中执行；
1. 合成线程（Compositor Thread）：负责图像合成的线程，如绘制（impl-side），合成等逻辑在该线程中执行。

#### 事件快速处理 ####
在Chrome中，这类可以不经过内核线程就能快速处理的输入事件为手势输入事件（滑动、捏合），手势输入事件是由用户连续的普通输入事件组合产生，如连续的mousewheel/touchmove事件可能会生成GestureScrollBegin/GestureScrollUpdate等手势事件。手势输入事件可以直接在已经渲染好的内容快照上操作，如滑动手势事件，直接对页面已经渲染好的内容快照进行滑动展示即可。由于线程化渲染框架的支持，手势输入事件可以不经过内核线程，直接由合成线程在内容快照上直接处理，所以即使此时内核线程在忙碌，用户的手势输入事件也可以马上得到响应。
## 表单 ##
<form>元素对应JavaScript中的HTMLFormElement类型

- acceptCharset 服务器能够处理的字符集
- action 接受请求URL
- elements 清单中所有空间的集合(HTMLCollection)
- enctype 请求的编码类型
- length 表单中控件的数量
- method 要发送的HTTP请求类型
- name 表单的名称
- reset 将鼠标单中所有域重置为默认值
- submit 提交表单
- target 用于发送请求和响应请求的窗口名称

document.forms 所有表单的集合

有提交按钮时，在响应表单控件拥有焦点的情况下，按回车就会提交表单(textarea 除外)

编程方式提交 form.submit(); 此种方式不会触发submit事件；

表单字段的属性

- disabled 表示当前字段是否被禁用
- form 指向当前字段所有表单的指针
- name 当前字段的名称
- readOnly 表示字段是否只读
- tabIndex 表示当前字段的切换序号
- type 当前字段的类型
- value 当前字段将被提交给服务器的值。对于文件字段来说，这个属性是只读的，包含着文件在计算机中的路径
- files 文件字段中存在 FileList类型

只有表单中包含提交按钮，才有可能触发表单的submit事件

表单支持的事件

blur 失去焦点时触发
change 失去焦点且value值改变；select选项改变时触发
focus 获得焦点时

### 输入过滤 ###
#### 操作剪切板 ####
### HTML验证API ###
- 必填字段
- 输入类型
- 数值范围
- 输入模式
- 有效性验证
- 禁用验证
### 选择框脚本 ###
HTMLSelectElement类型

- add(newOption, relOption) 向控件中插入新option
- multiple 控件中所有option元素
- options 移除给定位置的选项
- remove 移除给定的选项
- selectedIndex 基于0的选中项选项
- size 选择框中可见的行数

选择框的value，未指定则为显示的文本

每一个 option都有一个HTMLOptionElement对象表示

- index：当前选项在options中的索引
- label 单钱选项的标签
- selected 当前选项是否被选中，可设置
- text 选项的文本
- value 选项的值

新增选项 createElement('option')|new Option("Option text","Option value")
### 表单序列化 ###
## 作用域 ##
但是作用域“对象”无法通过JavaScript
代码访问，它存在于JavaScript 引擎内部。

ES5 规定，函数只能在顶层作用域和函数作用域之中声明，不能在块级作用域声明。

ES6 引入了块级作用域，明确允许在块级作用域之中声明函数。ES6 规定，块级作用域之中，函数声明语句的行为类似于let，在块级作用域之外不可引用。

在浏览器的 ES6 环境中，块级作用域内声明的函数，行为类似于var声明的变量。

ES6 的块级作用域允许声明函数的规则，只在使用大括号的情况下成立，如果没有使用大括号，就会报错。

- 允许在块级作用域内声明函数
- 函数声明相当于var会提升到全局作用域或函数作用域的头部
- 函数声明还会提升到所在块级作用域的头部

> ReferenceError 同作用域判别失败相关，而TypeError 则代表作用域判别成功了，但是对
> 结果的操作是非法或不合理的 

当引擎执行LHS 查询时，如果在顶层（全局作用域）中也无法找到目标变量，
全局作用域中就会创建一个具有该名称的变量，并将其返还给引擎,undefined

### 遮蔽效应 ###
作用域查找会在找到第一个匹配的标识符时停止。在多层的嵌套作用域中可以定义同名的
标识符，这叫作“遮蔽效应”（内部的标识符“遮蔽”了外部的标识符）

	window.a


通过这种技术可以访问那些被同名变量所遮蔽的全局变量

词法作用域查找只会查找一级标识符，比如a、b 和c。如果代码中引用了foo.bar.baz，
词法作用域查找只会试图查找foo 标识符，找到这个变量后，对象属性访问规则会分别接
管对bar 和baz 属性的访问。

### 欺骗词法 ###
#### eval ####

> JavaScript 中的eval(..) 函数可以接受一个字符串为参数，并将其中的内容视为好像在书
> 写时就存在于程序中这个位置的代码。换句话说，可以在你写的代码中用程序生成代码并
> 运行，就好像代码是写在那个位置的一样。

#### 动态脚本 ####

JavaScript 中还有其他一些功能效果和eval(..) 很相似。setTimeout(..) 和
setInterval(..) 的第一个参数可以是字符串，字符串的内容可以被解释为一段动态生成的
函数代码。这些功能已经过时且并不被提倡。不要使用它们！

new Function(..) 函数的行为也很类似，最后一个参数可以接受代码字符串，并将其转
化为动态生成的函数（前面的参数是这个新生成的函数的形参）。这种构建函数的语法比
eval(..) 略微安全一些，但也要尽量避免使用。

#### with ####
> with 通常被当作重复引用同一个对象中的多个属性的快捷方式，可以不需要重复引用对象
> 本身。

	function foo(obj) {
	with (obj) {
	a = 2;
	}
	}
	var o1 = {
	a: 3
	};
	var o2 = {
	b: 3
	};
	foo( o1 );
	console.log( o1.a ); // 2
	foo( o2 );
	console.log( o2.a ); // undefined
	console.log( a ); // 2——不好，a 被泄漏到全局作用域上了！
	});


尽管with 块可以将一个对象处理为词法作用域，但是这个块内部正常的var
声明并不会被限制在这个块的作用域中，而是被添加到with 所处的函数作
用域中。

#### 动态作用域 ####
**词法作用域是一套关于引擎如何寻找变量以及会在何处找到变量的规
则**

*如果JavaScript 具有动态作用域*

	function foo() {
		console.log( a ); // 3（不是2 ！）
	}
	function bar() {
		var a = 3;
		foo();
	}
	var a = 2;
	bar();

因为当foo() 无法找到a 的变量引用时，会顺着调用栈在调用foo() 的地
方查找a，而不是在嵌套的词法作用域链中向上查找。

JavaScript只有词法作用域，简单明了。
但是this 机制某种程度上很像动态作用域。

**主要区别：词法作用域是在写代码或者说定义时确定的，而动态作用域是在运行时确定
的。（this 也是！)**
## 异常 ##
	window.onerror = function(message, source, lineno, colno, error) { ... }

- message：错误信息（字符串）。可用于HTML onerror=""处理程序中的event。
- source：发生错误的脚本URL（字符串）
- lineno：发生错误的行号（数字）
- colno：发生错误的列号（数字）
- error：Error对象（对象）
- 若该函数返回true，则阻止执行默认事件处理函数。

	window.addEventListener('error', function(event) { ... })

ErrorEvent 类型的event包含有关事件和错误的所有信息

element.onerror = function(event) { ... }

> 当加载自不同域（协议、域名、端口三者任一不同）的脚本中发生语法(?)错误时，为避免信息泄露，语法错误的细节将不会报告，而代之简单的"Script error."。

结论：如果想通过onerror函数收集不同域的js错误，我们需要做两件事：

1. 相关的js文件上加上Access-Control-Allow-Origin:*的response header
1. 引用相关的js文件时加上crossorigin属性

### 捕获未处理的promise异常 ###

如何捕获那些未处理的Promise错误。由于没有使用catch方法捕获错误，当asyncFunc()函数reject时，抛出的错误则没有被处理

#### unhandledrejection ####
监听unhandledrejection事件，即可捕获到未处理的Promise错误

	window.addEventListener('unhandledrejection', event => ···);

promise: reject的Promise这个事件是PromiseRejectionEvent实例，它有2个最重要的属性：

reason: Promise的reject值
## 性能 ##
浏览器在解析到body标签之前不会渲染页面的任何部分

每个文件必须等到前一个文件下载并执行完成才会开始下载（script下载允许并行）

内嵌脚本放在引用外联样式表的link标签之后会导致页面阻塞去等待样式表的下载；建议不要把内嵌脚本紧跟在link标签后

大型网站和网络应用需要依赖数个JavaScript文件，可以把多个文件合成一个，这样只要引用一个script标签，就可以减少性能消耗

减少JavaScript文件大小并限制HTTP请求数仅仅是创建响应迅速的Web应用的第一步。尽管下载单个较大的JavaScript文件只产生一次HTTP请求，却会锁死浏览器一大段时间。

#### 非阻塞脚本 ####
defer属性指明本元素所含的脚本不会修改DOM；对应的JavaScript文件将在页面解析到script标签时开始下载，但并不会执行，直到DOM加载完成(onload事件被出发前)，无论是内嵌或是外链的都是如此

async加载完成后自动执行，defer需要等待页面完成后执行；

#### 动态脚本 ####
文件在该元素被添加到页面时开始下载；文件的下载不会阻塞页面其他进程。甚至可以将代码放在页面head区域而不影响页面的其它部分

把新建的script标签添加到head标签比添加到body里更保险，尤其是在页面加载过程中执行代码时更是如此；当body中的内容没有完全加载时，IE可能会抛出一个操作已终止的错误信息

使用动态脚本节点下载文件时，返回的代码通常会立刻执行(Firefox和opera会等待其他动态脚本执行完毕)

IE中 script元素提供一个readyState属性 uninitialized loading loaded interactive complete

只有Firefox和Opera保证脚本会按照你指定的顺序执行

#### XMLHttpRequest脚本注入 ####
可以加载JavaScript但不立刻执行；在所有主流浏览器中都能正常工作；


### 函数对象 ###
内部属性[[Scope]]包含一个函数被创建的作用域中对象的集合，这个集合被称为函数的作用域链，它决定哪些数据能被函数访问。

当执行环境被创建时，它的作用域链初始化为当前运行函数的[[Scope]]属性的对象。会创建完成一个活动对象，作为函数运行时的变量对象，包含所有的局部变量，命名变量，参数集合以及this，然后此对象被推入作用域链的最前端。当执行环境被销毁，活动对象也随之销毁。
在函数中读写局部变量总是最快的，而读写全局变量总是最慢的。

如果某个跨作用域的值在函数中被引用一次以上，那么就把它存储到局部变量里。比如用局部变量存储document

执行with语句时，执行环境的作用域链临时被改变了。一个新的变量对象被创建，这个对象被推入作用域链的首位。try-catch也有同样的效果，在catch代码内部，函数所有局部变量将会被放在第二个作用域链对象中。

### 动态作用域 ###
with，try-catch或是eval()函数都被认为是动态作用域，无法通过静态分析检测出来

### 闭包 ###
当函数执行时，一个包含了变量id以及其他数据的活动对象被创建，它成为执行环境作用域链中的第一个对象，而全局对象紧随其后。当闭包被创建时，它的[[Scope]]属性被初始化为这些对象。由于引入了闭包，函数的活动对象的引用仍然存在于闭包的[[Scope]]属性汇总，因此活动对象无法被销毁。这意味着脚本中的闭包与非闭包函数相比，需要更多的内存开销。尤其在IE中有大量非原生的JavaScript对象来实现DOM对象。

当闭包代码执行时，会创建一个执行环境，它的作用域链与属性[[Scope]]中所引用的两个相同的作用域链将会被一起初始化，然后一个活动对象为闭包自身所创建。频繁的跨作用域访问会带来性能问题。可以考虑局部变量。

### 对象成员 ###
JavaScript的对象是基于原型的；原型对象为所有对象实例所共享；

由此，对象可以有两种成员类型：实例成员(own成员)和原型成员

> Object或Array的实例，会自动拥有一个Object实例作为原型
> 
> hasOwnProperty只返回实例成员，in操作符既搜索实例也搜索原型

定义一个函数F，就会存在一个F.prototype， 令 P = F.prototype,P.constructor就是F本身，在P上添加的成员会在用new F产生的对象的原型链上；P的原型属性(内部属性__proto__)是一个Object实例；令 b = new F; b的原型属性(__proto__)就是Book.prototype;不同的F实例共享同一个原型链；他们有各自的实例属性，而其他部分都继承自原型；

搜索实例成员比成员字面量或局部变量读取数据代价更高，再加上遍历原型链带来的开销，这让性能问题更为严重。

成员嵌套越深，读取速度就会越慢
JavaScript的命名空间，比如YUI使用的技术，是导致频繁访问嵌套属性的起因之一。

*把一个对象方法保存在局部变量会导致this无法绑定到window，而this的改变会使JavaScript引擎无法正确解析它的对象成员，进而导致程序出错*

### DOM编程 ###
DOM是一个独立于语言，用于操作XML和HTML文档的程序接口

Chrom中的DOM是使用WebKit中的WebCore库来渲染页面，但JavaScript引擎是他们自己研发的V8，DOM和JavaScript两个相互独立的功能通过接口连接，就像连个岛屿间的收费桥梁，尽量减少访问DOM的次数。(如for循环)

**innerHTML和DOM原生方法的效率对比**

HTML集合，getElementsByNames 以假定实时态存在，与文档保持着连接，会重复执行查询过程，效率很低；

	var alldivs = document.getElementsByTagNames('div');
	for(var i = 0; i< allDivs.length; i++){
		document.body.appendChild(document.createElement('div'));
	}

以上代码会产生死循环；

一个集合的length读取比一个普通数据的length读取要慢很多；

可考虑toArray方法；拷贝元素到数组； 访问DOM员尽量考虑局部变量保存；nextSibling方法更有优势；

区分元素节点的属性和不区分的(区分文本和注释)

- children childNodes
- childElementCount childNodes.length
- firstElementChild firstChild
- lastElementChild lastChild
- nextElementSibling nextSibling
- previousElementSibling previousSibling

document.querySelectorAll返回的不是HTML集合，因此返回的节点不会对应实时的文档接口。

#### 重绘与重排 ####
DOM树 表示页面结构

渲染树 表示DOM节点如何显示

> DOM树中的每一个需要显示的节点在渲染树中至少存在一个对应的节点(隐藏的DOM元素咋渲染树中没有对应的节点)，渲染树中的节点被称为帧(frames)或盒(boxes)，理解成符合CSS模型的定义，理解页面元素为一个具有padding,margin,borders和position的盒子。一旦DOM树和渲染树构建完成，浏览器就开始显示(绘制paint)页面元素

当DOM的变化影响了元素的集合属性(宽和高),浏览器需要重新计算元素的几何属性，浏览器会使渲染树中受影响的部分失效，并重新构造渲染树，这个过程称为重排(reflow),完成重排和，浏览器会重新绘制受影响的部分到屏幕中，该过程重围重绘(repaint)。

并不是所有的DOM变化都会影响几何属性(例如颜色),这种情况只会发生一次重绘，因为元素的布局没有改变。

*重排何时发生*

- 添加或删除可见的DOM元素(把DOM操作放在不可见的元素中)
- 元素位置改变(考虑position定位减少影响)
- 元素尺寸改变
- 内容改变(文本或图片呗不同尺寸的图片替代)
- 页面渲染器初始化
- 浏览器窗口尺寸改变

渲染树变化的排队与刷新，获取布局信息的操作会导致队列刷新

![20180104085724397.png](http://ww1.sinaimg.cn/large/48ceb85dly1g9ez1ev25zg20gx0gqweo.jpg)

- offsetTop,offsetLeft,offsetWidth,offsetHeight
- scrollTop,scrollLeft,scrollWidth,scrollHeight
- clientTop,clientLeft,clientWidth,clientHeight
- getComputedStyle()(currentStyle in IE)

以上属性和方法需要返回最新的布局信息，因此浏览器不得不执行渲染队列中的待处理变化并触发重排已返回正确的值，在修改样式的过程中最好避免使用上面列出的属性，它们都会率先你渲染队列，即使你在获取最近未发生改变的或最新改变无关的布局信息。

*最小化重绘和重排*

为了减少发生次数，应该合并多次对DOM和样式的改变，然后一次处理掉；现在的浏览器为此做了优化

使用cssText一次修改所有样式 el.style.cssText+= '',或者一次性修改class而不是内联的样式，不过class需要检查级联样式

*批量修改DOM*

1. 使元素脱离文档流
2. 对其应用多重改变
3. 把元素带回文档中

三种基本方法可以使DOM脱离文档

- 隐藏元素，应用修改，重新显示
- 使用文档片段(document frament),在当前DOM之外构建一个子树，再把它拷贝会文档
- 将原始文档拷贝到一个脱离文档的节点中，修改副本，完成后再替换原始元素

尽量推荐第二个方案；

*让元素脱离动画流*

一个顶部的动画推移整个余下的部分，会导致昂贵的大规模重排，让用户感到页面一顿一顿。渲染树中需要重新计算的节点越多，情况就越糟糕。

1. 使用绝对位置定位页面上的动画元素，使其脱离文档流。
2. 让元素动起来。当它扩大时，会临时覆盖部分页面。但这只是页面一个小区域的重绘过程，不会产生重排并重绘页面的大部分部分。
3. 当动画结束时恢复定位元素，从而智慧下移一次文档的其他元素。

ie上避免使用:hover

#### 事件委托 ####
使用事件委托，只需给外层元素绑定一个处理器，这样就可以处理在其子元素上触发的所有事件

	//浏览器阻止默认行为并取消冒泡
	if(typeof e.preventDefault === 'function'){
		e.preventDefault();
		e.stopPropagation();
	} else {
		e.returnValue  = false;
		e.cancelBubble = true;
	}

控制条件中非零数会自动转换成true，而零值等同于false

	for(var i=items.length:i--;){
		process(items[i]);
	}

### 代码优化 ###
减少迭代次数 Duff's Device 循环体展开技术

## jquery ##
### jQuery.ajax ###
[API 文档](http://api.jquery.com/jquery.ajax/)

基本流程

    $.ajax({
      url: "test.html",
      context: document.body
    }).done(function() {
      $( this ).addClass( "done" );
    });
    
    dataType: "xml/html/script/json", // expected format for response
      contentType: "application/json", // send as JSON

#### then和done的区别 ####
then可以向后继传值， done结束。

    // 在浏览器环境下运行，而非 node 环境
    cosnt jsPromise = Promise.resolve($.ajax('/whatever.json'))
        jsPromise.then(data => {
        // ...
    })
    
#### val() 和 text() ####
div , a 等没有val()，加入文字需要text()

读取value 使用 attr("value")

## 事件 ##
### 点击事件 ###
对于动态加入的元素使用

	$("body").on('click',".className",()=>{})

jQuery解析事件处理程序的返回值，以便确定是否继续处理这个事件。
通常返回false，它做的工作是:

1. 告诉jquery阻止正在操作对象的默认行为，相当于在event.preventDefault()
2. 告诉jQuery停止该事件触发父元素上的相同事件(冒泡),相当于event.stopPropagation()
3. 结束事件处理程序的执行，如果在这事件之后，被点击的元素还绑定了其他的事件处理程序，则事件队列中的下一事件会被调用(如果不想后续的事件处理程序，可以调用event.preventImmediatePropagation())

#### 跨域 ####
ajax选项
> {xhrFields:{withCredentials:true}}

服务器端header设置

> // 指定允许其他域名访问  
> header('Access-Control-Allow-Origin:*');  
> // 响应类型  
> header('Access-Control-Allow-Methods:POST');  
> // 响应头设置  
> header('Access-Control-Allow-Headers:x-requested-with,content-type');

### .add() ###
往集合中增加元素 类似addClass
### .css() ###
设置css属性
### .addClass() ###
为集合添加css类
### .after() ###
在集合的元素后操作
### .ajaxXXXX ###
可全局使用
$(document).ajaxXXX();
### .attr() ###
checkbox 用prop()获取，html中设置check可以使用 attr()
> To retrieve and change DOM properties such as the checked, selected, or disabled state of form elements, use the .prop() method.

prop()类似

### .hasClass() ###
是否由css类

### .html() ###
设置或获取第一个元素的内部html

### removeXXX ###
- .removeAttr()
- .removeClass()
- .removeProp()

### toggleClass() ###
勾选所有元素的类
### val() ###
设置或获取第一个元素的值
### $.Callback() ###

- add()
- fire() 为回调事件提供参数
- remove()

#### Flag ####
- once 每次仅fire一个callback
- memeory 保留callback
- unique 每个回调仅加入一次
- stopOnFalse 回调返回false即终止

### $.holdReady() ###
true参数锁定， 直到false释放，控制jquery的ready状态
### $.noConflict() ###
释放$变量
### $.ready ###
Promise-like obj 标识ready状态
### $.sub() ###
拷贝一份jquery，修改不影响原来的集
### $.when ###
提供thenable 对象
### $.cssHooks ###
设置css属性的读取和设置方式
### .data ###
读取或设置属性变量

### $.Deferred ###


### eval是魔鬼 ###
eval会干扰作用域链，new Function()不会

> The eval function (and its relatives, Function, setTimeout, and setInterval) provide access to the JavaScript compiler. This is sometimes necessary, but in most cases it indicates the presence of extremely bad coding. The eval function is the most misused feature of JavaScript. ————Douglas Crockford

## 可维护的JavaScript ##
换行两个缩进

使用局部变量，不要使用全局变量，可以传参

使用单全局变量 $,_,

命名空间

	var YourGlobal = {
		namespace:function(ns){
			var parts = ns.split("."),
			object = this,
			i,len;
			
			for(i=0,len=parts.length;i<len;i++){
				if(!object[parts[i]]){
					object[parts[i]]={};
				}
				object = object[part[i]];
			}
			
			return obejct;
		}
	}

#### 应用逻辑隔离 ####
将应用逻辑从所有事件处理程序中抽离出来的做法是一种最佳实践，因为说不定什么时候其他地方就会触发同一段逻辑

测试:测试时需要直接触发功能代码，而不必通过模拟对元素的点击来触发

**有利于重用和测试**
#### 不要分发事件对象 ####
拿到所有需要的数据传给应用逻辑，让事件处理程序成为接触到event对象的唯一的函数

应用逻辑不需要对event产生依赖，进而可以在其他地方使用相同的逻辑，包括写测试代码

IE8之前的DOM对象并非继承自Object

#### 抽离配置数据 ####
	config = {}

#### 抛出错误 ####
对参数作检查，并且抛出错误的具体信息和位置，在被使用的库中，因为库不容易被检查；

不是为了防止错误，而是错误发生时更容易地调试

#### 门面模式 ####
包装之后，在原型上添加方法
# 客户端JavaScript #
Web文档应该少量地使用JavaScript，因为JavaScript真正的角色是增强用户的浏览体验，使信息的获取和传递更容易。用户的体验不应该依赖于JavaScript，但JavaScript可以增强用户体验：

- 创建动画和其他视觉效果，巧妙地引导和帮助用户进行页面导航。
- 对表格的列进行分组，让用户更容易找到所需要的。
- 隐藏某些内容，当用户深入到内容里，再逐步展示详细信息

	a:href="javascript:void window.open('about:blank');"

javascript：表达式的内容作为替换当前的新文档的内容，void 0 可以防止当前页面被覆盖

### 使用模块 ###
不产生全局变量，在默认接口中完成页面模块的初始化，配置和事件监听

关注创建能增值的核心模块，次要的模块可以交给第三方

### FMVC ###
多个层级上采用重复的MVC模式

在服务器端采用MVC模式向客户端提供数据；<br/>
采用MVC的单页应用语序用户查看浏览器端的模型，并与之交互

服务器端的模式是从数据库获取的数据<br/>
而试图是要发送给浏览器的数据表现<br/>
控制器是协调数据管理和同浏览器通信的代码<br/>

在客户端,模型包括从服务器接收到的数据<br/>
视图是用户界面<br/>
控制器是协调客户端数据和界面的逻辑<br/>


### javascript 加载 ###
- 载入文档内容
- 事件驱动

脚本的执行默认是同步和阻塞的(async|defer)

动态加载脚本会实现脚本的异步载入和执行
## window对象 ##
Document对象的location也引用到window的Location对象，Location对象会做相应改变，但document.URL却不会改变

### location ###
#### ID作为全局变量 ####
浏览器厂商可以在任何时候为Window对象加入新属性，这些新属性会破坏此属性名的隐式定义的代码
assign() replace(), reload();
### 打开和关闭窗口 ###
window.open 

参数：新显示文档的URL，新打开窗口的名字，各种属性

iframe的contentWindow引用改窗体的window对象

窗体的window对象用frameElement来获取窗体的iframe元素

iframe底边的白底去除 display:block；或者vertical-align:bottom;
**window其实是全局对象的一个代理**

## 函数防抖与函数节流 ##
### 函数防抖（debounce） ###
> 函数防抖，就是指触发事件后在 n 秒内函数只能执行一次，如果在 n 秒内又触发了事件，则会重新计算函数执行时间。

打个比方，坐公交，司机需要等最后一个人进入才能关门。每次进入一个人，司机就会多等待几秒再关门。

#### 应用场景 ####
连续的事件，只需触发一次回调的场景有：

- 搜索框搜索输入。只需用户最后一次输入完，再发送请求
- 手机号、邮箱验证输入检测
- 窗口大小Resize。只需窗口调整完成后，计算窗口大小。防止重复渲染。

#### 简单实现 ####
	const _.debounce = (func, wait) => {
	  let timer;
	
	  return () => {
	    clearTimeout(timer);
	    timer = setTimeout(func, wait);
	  };
	};

函数防抖在执行目标方法时，会等待一段时间。当又执行相同方法时，若前一个定时任务未执行完，则 clear 掉定时任务，重新定时。
### 函数节流（throttle） ###
> 限制一个函数在一定时间内只能执行一次。

举个例子，乘坐地铁，过闸机时，每个人进入后3秒后门关闭，等待下一个人进入。

#### 应用场景 ####
间隔一段时间执行一次回调的场景有：

- 滚动加载，加载更多或滚到底部监听
- 谷歌搜索框，搜索联想功能
- 高频点击提交，表单重复提交

#### 简单实现 ####
**setTimeout版**

	const _.throttle = (func, wait) => {
	  let timer;
	
	  return () => {
	    if (timer) {
	      return;
	    }
	
	    timer = setTimeout(() => {
	      func();
	      timer = null;
	    }, wait);
	  };
	};

函数节流的目的，是为了限制函数一段时间内只能执行一次。因此，通过使用定时任务，延时方法执行。在延时的时间内，方法若被触发，则直接退出方法。从而，实现函数一段时间内只执行一次。

**时间戳版**

	const throttle = (func, wait) => {
	  let last = 0;
	  return () => {
	    const current_time = +new Date();
	    if (current_time - last > wait) {
	      func.apply(this, arguments);
	      last = +new Date();
	    }
	  };
	};

通过比对上一次执行时间与本次执行时间的时间差与间隔时间的大小关系，来判断是否执行函数。若时间差大于间隔时间，则立刻执行一次函数。并更新上一次执行时间。
# CSS #
**用父类来影响子类元素，大概是CSS影响CSS的一个最强大的功能**

*spa-shell-的好处*

- 表明这些类是由Shell模块(spa/js/spa.shell.js)控制的
- 这能防止和第三方脚本以及我们的其他模块产生名字空间的冲突
- 在调试和查看HTML文档的时候，我们能立即明白哪些元素是由Shell模块生成和控制
## 长度 ##
em:相对长度单位。相对于当前对象内文本的字体尺寸
ex:相对长度单位。相对于字符“x”的高度。通常为字体高度的一半
ch:数字“0”的宽度
rem:相对长度单位。相对于根元素(即html元素)font-size计算值的倍数
vw:相对于视口的宽度。视口被均分为100单位的vw
vh:相对于视口的高度。视口被均分为100单位的vh
vmax:相对于视口的宽度或高度中较大的那个。其中最大的那个被均分为100单位的vmax
vmin:相对于视口的宽度或高度中较小的那个。其中最小的那个被均分为100单位的vmin
cm:厘米（Centimeters）。绝对长度单位
mm:毫米（Millimeters）。绝对长度单位
q:1/4毫米（quarter-millimeters）。绝对长度单位
in:英寸（Inches）。绝对长度单位
pt:点（Points）。绝对长度单位 1in=72pt=2.54cm=96px=6pc
pc:派卡（Picas）。绝对长度单位。相当于我国新四号铅字的尺寸
px:相对长度单位。像素（Pixels）

## css特性支持 ##
语法支持

	@supports(text-shadow: 0 0 0.3em gray){
		h1 {
			color: transparent;
			text-shadow: 0 0 .3em gray;
		}
	}

属性支持
	
	function testProperty(property){
		var root = document.documentElement;
		if (property in root.style){
			root.classList.add(property.toLowerCase());
			return true;
		}
		root.classList.add('no' + propery.toLowerCase());
		return false;
	}

属性值支持

	function testValue(id, value, property){
		var dummy = document.createElement('p');
		dummy.style[property] = value;
	
		if(dummy.style[property]){
			root.classList.add(id);
			return true;
		}
		root.classList.add('no-' + id);
		return false;
	}
## CSS变量 ##
	:root {
	   --color: blue;
	   --COLOR: red;
	}

#### 使用js切换变量 ####
root.style.setProperty("--bg-text", "black");
#### html中使用变量 ####
&lt;html style="--size: 600px"&gt;

## 裁剪 ##
clip-path

	img {
		clip-path:polygon(50% 0, 100% 50%, 50% 100%, 0 50%);
		transition: 1s clip-path;
	}
	
	img:hover {
		clip-path:polygon(0 0, 100% 0, 100% 100%, 0 100%);
	}
## width ##
100% 只与父元素有关，与屏幕无关

position:absolute 的元素的 width x%，与父元素的父元素有关，脱离了父元素的计算，使用relative可以限制absolute是的width的比例相对于父元素

## 前缀 ##
> - -webkit-*   safari 
> - -o-*        Opera
> - -moz-*      Firefox


## direction ##
决定文本方向

direction:ltr|rtl;

### bidi(双向文字)与RTL布局总结 ###
在现代计算机应用中，最常用来处理双向文字的算法是 Unicode 双向算法（Unicode Bidirectional Algorithm），在后面的文章中我们将 Unicode 双向算法简称为 bidi 算法。

我们的web产品中使用的字符都属于unicode字符，而unicode字符的方向属性总共包含三类：强字符、中性字符、弱字符。

- 强字符的方向属性是确定的，与上下文的bidi属性无关，而且强字符在bidi算法中可能会对其前后的中性字符产生影响。大部分的字符都属于强字符，比如拉丁字符、汉字、阿拉伯字符。
- 中性字符的方向性并不确定，受其上下文的bidi属性影响（前后的强字符）。比如大部分的标点符号（“-”，“[]”，"()"等）跟空格。
- 弱字符的方向性是确定的，但不会对其上下文的bidi属性产生影响。比如数字以及跟数字相关的符号。
## float ##
> float元素脱离里定位的标准流，随后的标准流元素将忽略该元素，按前一个标准流元素的位置定位。

#### float 和 absolute都会使元素inline-block化 ####

### float参数 ###
left,right
决定流动的方向

> float元素随后的float元素将跟随在前一个连续的float元素之后
> 
> float元素仍然处于文档流当中，将会被随后的文字包围(区别于position:absolute)

### clear参数 ###
 clear : none | left | right | both

> 决定该侧不允许存在元素，只影响自身，不影响其它元素

## position ##
### static ###
默认值

**div有非static的postion属性，默认宽度为0，div里面有内容，会被内容撑起**

未定位的div宽度时100%；

**同上，div有float:left, float:right等样式，div默认宽度为0，会被内部内容撑起**
### absolute ###
> 脱离文档流，将与其他文档处于不同的层级
> 若父元素不是relative定位，将相对于整个页面定位，默认位置是其在文档流中的定位
> 若父元素是relative定位，将相对于父元素定位

*本质，非static父元素*
> 当给绝对定位元素设置定位值时，该元素会延着DOM树向上查找，直到找到一个具有定位属性的祖先元素，则定位相对于该元素，在该例子中，由于其祖先元素都没有定位属性，则该绝对定位元素会相对于body体进行定位；如果给其父元素加上一个定位属性，则该绝对定位元素会相对于这个父元素；

#### 自动伸缩 ####
当width为auto时，根据left，right自动伸缩，相对于第一个有定位属性的祖先

> **如果 width 值为 auto 此时如果我们设置 left 和 right 都为0，则该元素会填充满其相对的元素**，如果此时我们将宽度设置为固定值，这是绝对定位元素会优先取 left 值作为定位标志

**将margin设为auto，lef和right值相等，绝对定位元素会居中，垂直方向也是一样的(bottom,top)**


    .cover {
	    position: absolute;
	    left: 0;right: 0;top: 0;bottom: 0;
	    background-color: #fff;
	    opacity: .5;filter: alpha(opacity=50);
    }

> 用absolute的left: 0;right: 0;top: 0;bottom: 0;来实现全屏拉伸

> 不设top/right/top/bottom的话absolute会从正常文档流应处的位置开始定位，因此做不到全屏

使用relative显示cover只在父元素部分，而非全屏

*以下情况根本不需要设z-index：*

- 让absolute元素覆盖正常文档流内元素（不用设z-index，自然覆盖）
- 让后一个absolute元素覆盖前一个absolute元素（不用设z-index，只要在HTML端正确设置元素顺序即可）

### relative ###
相对于原来在文档中的位置布局


### fix ###
固定在页面中的位置

### z-index ###
[z-index](https://www.w3cplus.com/css/what-no-one-told-you-about-z-index.html)

> HTML中的每一元素都是在其他元素的前面或者后面。这是众所周知的堆叠顺序（Stacking Order）

> 如果没有涉及z-index和position属性的话，那规则很简单，堆叠顺序就是元素在HTML中出现的顺序。（**负margin特殊**）
> 
> 加上position属性的话，就是所有定位了得元素在没有被定位的元素前面。

z-index只作用在定了位的元素上

z-index会创建一个堆叠的上下文(Stacking Contexts)

定位元素覆盖未定位元素

如果父元素z-index有效，那么子元素无论是否设置z-index都和父元素一致，会在父元素上方

#### 堆叠上下文 ####
> 每一个层都有唯一的根节点。当一个元素创建一个层，那么它的所有子元素都会受到父元素的堆叠顺序影响。意味着如果一个元素位于一个最低位置的层，那你z-index设置得再大，它也不会出现在其它层元素的上面。

*什么情况下会产生新的层：*

- 当一个元素位于HTML文档的最外层（<html>元素）
- 当一个元素被定位了并且拥有一个z-index值（不为auto）
- 当一个元素被设置了opacity，transforms, filters, css-regions, paged media等属性

#### 堆叠顺序 ####
- 层的根元素
- 被定位了的元素，且z-index为负，相同z-index按照HTML元素的书写顺序
- 没有被定位的元素
- 被定位的元素，且z-index为auto
- 被定位了的元素，且z-index为正

z-index为负元素先被绘制，甚至在父元素后面
## 透明度计算 ##
alpha为不透光度 每层每一个色彩通道色度为c,

三个层级的色彩计算

(c1,a1) (c2,a2) (c3,a3)

白光经过第一层的色度为 r1 = 255 * （1-a1）+ c1 * a1;

经过第二层的色度为 r2 = r1 * (1-a2) + c2 * a2;

经过第三层的色度为 r3 = r2 * (1-a3) + c3 * a3;
## flexbox布局 ##
主轴方向块级布局，副轴方向行内布局

### flex参数 ###
display:flex

#### flex:flex-grow flex-shrink flex-basis ####

默认值:0 1 auto;

- flex-grow:拉伸程度 
- flex-shrink:是否收缩 
- flex-basis:自动计算大小

#### flex-direction ####
主轴方向

默认值:row 行方向

值：column,row

#### order ####
源顺序，order大的数字排在后面

#### justify-content ####
在主轴的绘制方式

值：

- flex-start 靠头排列
- flex-end 靠尾排列
- center 居中排列
- space-between 头尾紧靠，间隙相等
- space-around 头尾间隙都相等

#### align-items ####
在侧轴上的对齐方式

值：

- flex-start:顶部从侧轴起始边届对齐
- flex-end:底部在侧轴结束位置对齐
- center:在侧轴居中放置
- baseline:如果行内距与侧轴相同，与flex-start一致，否则与基线对齐
- stretch:如果侧轴大小为auto，使盒边距接近行的尺寸，但遵照'min/max-width/height'限制，相当于完成撑开,默认完全撑开

#### align-self ####
定义flex子项单独的对齐方式，会重写align-items的参数

### 水平居中方案 ###
 在div中设置,可使在主轴上居

> display:flex;
> 
> justify-content:center;

#### Flex IE兼容 ####
flex-basis会忽略 box-sizing:border-box;

## 屏幕大小响应 ##

@media all and(min-width:768px) 适应768px以上的屏幕


## 字体粗细 ##
font-weight:设置粗细，但也与字体有关

font-family:设置字体

## 边框属性 ##
### border ###
全写是 border-width, border-style, border-color
### border-image ###
存在属性时，可以替换border-style

全写是  border-image-source, border-image-slice, border-image-width, border-image-outset,  border-image-repeat

#### border-image-source ####
设置图片源或者颜色样式linear-gradient

如果是none，则或使用border-style来展示border
#### border-image-slice ####
![](https://mdn.mozillademos.org/files/3814/border-image-slice.png)

- 1-4区是角区域，每个部分在最终的边框图像中都只使用一次
- 5-8区是边区域，在最终的边框图像中会重复，拉伸或者有其他的改变，又border-image-repeat来决定
- 9区是中间区域，默认不适用，但是当fill被set的时候会被当成背景图使用

*值*

- 数字，代表图像的像素或矢量图的坐标
- 百分比，代表源图中的百分比
- fill

#### border-image-width ####
边框的宽度
#### border-image-outset ####
代表边框外侧偏离border-box的距离
#### border-image-repeat ####
代表边区域如何展示和适应

*值* stretch repeat round space



border-shadow:边框阴影

border-radius:边框圆角

## 背景 ##
> background:url()
> 
> background-position:[left top],可以为多个background-image设置多组postion;
> 
> background-repeat:repeat no-repeat，默认是repeat;
> 
> background-size: 背景大小 cover覆盖 contain一个方向顶格
> 
> background-origin:[content-box|padding-box|border-box]
> background-clip:[border-box padding-box content-box text]背景的绘制区域（应该是针对图片内的坐标）
> background-position: 属性设置背景图像的起始位置 background-position的百分比值引起的偏移是基于 (width-backgroundSize)[该width是基于backgroundOrigin]
> object-fit： CSS 属性指定可替换元素的内容应该如何适应到其使用的高度和宽度确定的框 contain|cover|fill|none|scale-down
> background-image:可以设置多张图片，第一张靠近用户，background-color绘制在background之下

#### 蚂蚁行军边框 ####
	@keyframes ants { to {background-position:100%}} //backgroun-position的移动导致斜线条纹的移动
	
	.marching-ants {
		padding: 1em;
		border: 1px solid transparent; //边框变为1
		background:
		linear-gradient(white, white) padding-box,//padding-box白色背景遮掩整体条纹
		repeating-linear-gradient(-45deg,
		  black 0 25%, white 0 50%) 0/.6em .6em; //构造斜线的条纹 background-position 为 0 center;
		animation:ants 12s linear infinite;
	}

## css animation ##
@keyframes 定义各个时间的css状态 to/from , percentage%,...

animation-delay 设置延时
animation-direction 动画运行完成后反向运行还是重新回到开始位置 [normal reverse alternate]
animation-duration 动画周期时长
animation-interation-count 动画重复次数 可以infinite
animation-name 指定可以frame关键帧的名称
animation-play-state 允许暂定和恢复动画
animation-fill-mode 

## 边框 ##
> border-image 设置边框图片
> border-image-source 图源
> border-image-repeat 设置边框9个区的缩放
> border-image-slice 设置边框9个区从背景image中截取的大小
> border-image-outset 边框image对边框的偏离，不能小于0
> 
> outline 可以模拟边框，但是不能为圆角，圆角border可以通过box-shadow来模拟遮掩空白处

## Table ##
border-collapse: collapse
## 渐变 ##

- radial-gradient:径向渐变
- linear-gradient:线性渐变

## 文本效果 ##
- text-shadow:文字阴影
- box-shadow:盒子阴影，适用于div
- text-overflow:ellipsis(...) clip(裁剪字)
- white-space:nowrap;pre保留空格
- word-wrap:break-work 换行,分裂一个字
- word-break: keep-all 保持单词不拆分， break-all 拆分单词

box-shadow叠在元素之上，重叠的部分被裁剪，所以元素设置为半透明是看不到底层的阴影的，这与text-shadow不同，text-shadow不会裁剪阴影



> **阴影第三个参数 模糊度，越大越模糊**
> 
> ** 第四个参数，阴影的大小，扩散到的范围**
> 
> box-shadow: h-shadow v-shadow blur spread color inset;


## 伪元素 ##
- :first-letter:
- :first-line
- :before  向元素之前添加内容
- :after 在该元素之后添加内容

## CSS滤镜 ##
	filter: blur(5px);
	filter: brightness(0.4);
	filter: contrast(200%);
	filter: drop-shadow(16px 16px 20px blue);
	filter: grayscale(50%);
	filter: hue-rotate(90deg);
	filter: invert(75%);
	filter: opacity(25%);
	filter: saturate(30%);
	filter: sepia(60%);
## CSS3字体 ##
### @font-face ###

    <style> 
    @font-face
    {
    	font-family: myFirstFont;
    	src: url(sansation_light.woff);
    }
     
    div
    {
    	font-family:myFirstFont;
    }
    </style>

## CSS3 多列 ##
- column-count 需要分割的列数
- column-gap  列与列之间的间隙
- column-rule-style:[dotted solid]  列边框的样式
- column-rule-width  边框的厚度
- column-rule-color  边框的颜色
- column-span: 1|all 跨越多少列
- column-width  列的宽度

## 属性继承 ##
### 无继承性的属性 ###
1、display：规定元素应该生成的框的类型

2、文本属性：

vertical-align：垂直文本对齐

text-decoration：规定添加到文本的装饰

text-shadow：文本阴影效果

white-space：空白符的处理

unicode-bidi：设置文本的方向

3、盒子模型的属性：width、height、margin 、margin-top、margin-right、margin-bottom、margin-left、border、border-style、border-top-style、border-right-style、border-bottom-style、border-left-style、border-width、border-top-width、border-right-right、border-bottom-width、border-left-width、border-color、border-top-color、border-right-color、border-bottom-color、border-left-color、border-top、border-right、border-bottom、border-left、padding、padding-top、padding-right、padding-bottom、padding-left

4、背景属性：background、background-color、background-image、background-repeat、background-position、background-attachment

5、定位属性：float、clear、position、top、right、bottom、left、min-width、min-height、max-width、max-height、overflow、clip、z-index

6、生成内容属性：content、counter-reset、counter-increment

7、轮廓样式属性：outline-style、outline-width、outline-color、outline

8、页面样式属性：size、page-break-before、page-break-after

9、声音样式属性：pause-before、pause-after、pause、cue-before、cue-after、cue、play-during
### 可继承属性 ###
1、字体系列属性

font：组合字体

font-family：规定元素的字体系列

font-weight：设置字体的粗细

font-size：设置字体的尺寸

font-style：定义字体的风格

font-variant：设置小型大写字母的字体显示文本，这意味着所有的小写字母均会被转换为大写，但是所有使用小型大写字体的字母与其余文本相比，其字体尺寸更小。

font-stretch：对当前的 font-family 进行伸缩变形。所有主流浏览器都不支持。

font-size-adjust：为某个元素规定一个 aspect 值，这样就可以保持首选字体的 x-height。

2、文本系列属性

text-indent：文本缩进

text-align：文本水平对齐

line-height：行高
> 
> div中文字的高度由line-height产生
> line-height的值使单行文字垂直居中对齐
> 对于块级元素，CSS属性line-height指定了元素内部line-boxes的最小高度。
> 对于非替代行内元素，line-height用于计算line box的高度。

word-spacing：增加或减少单词间的空白（即字间隔）

letter-spacing：增加或减少字符间的空白（字符间距）

text-transform：控制文本大小写

direction：规定文本的书写方向

color：文本颜色

3、元素可见性：visibility

4、表格布局属性：caption-side、border-collapse、border-spacing、empty-cells、table-layout

5、列表布局属性：list-style-type、list-style-image、list-style-position、list-style

6、生成内容属性：quotes

7、光标属性：cursor

8、页面样式属性：page、page-break-inside、windows、orphans

9、声音样式属性：speak、speak-punctuation、speak-numeral、speak-header、speech-rate、volume、voice-family、pitch、pitch-range、stress、richness、、azimuth、elevation

### 所有元素可以继承的属性 ###
1、元素可见性：visibility

2、光标属性：cursor
### 内联元素可以继承的属性 ###
1、字体系列属性

2、除text-indent、text-align之外的文本系列属性
### 块级元素可以继承的属性 ###
1、text-indent、text-align
## 用户界面 ##
resize: [none both horizontal vertical]
box-size: content-box 应用在内容框 border-box 应用在整个盒（包括边框内边距） inherit从父元素继承
outline-offset 轮廓距离
outline 轮廓样式
max-with：100% 响应屏幕变化
filter:grayscale(100%)图片滤镜


## 动画例子 ##
[modal动画](http://www.runoob.com/try/try.php?filename=trycss_image_modal_js)

### transform-origin ###
旋转的锚点

## 过渡 ##
- transition-property;
- transition-duration;
- transition-timing-function;
- transition-delay;

要有明确的起始终止数值transition才会起效，例如auto这种transition就无法生效
## CSS3 动画 ##

## CSS规则执行顺序 ##
> 样式表中最后出现的规则优先

### 特殊性 ###
一般的HTML

- 元素选择符(H1,P等)具有特殊性1 
- 类选择符特殊性10，
- ID选择符100
- 继承值特殊性0
- Style元素权值100，比ID有更高特殊性
- 重要性！important特殊性1000

**即便定义!important，继承里的特殊性也只有0**
> 权重顺序为：继承=>HTML普通选择符=>类选择符=>style元素=>!important


ID (#id) > Class (.class) > Type (例如 header)

### 层叠 ###

- 1)若两条规则具有相同的权值、起源及特殊性，那在样式表中最后出现的规则优先。
- 2)任何位于文档中的规则都比引入的规则优先。

## Margin和Padding ##

> 边界，元素周围生成额外的空白区。“空白区”通常是指其他元素不能出现且父元素背景可见的区域。——CSS权威指南

### Collapsing Margin ###
相邻或嵌套的盒元素Margin会重合为单一个Margin

(相邻元素的Margin-top相同，比较合理)

> In this specification, the expression collapsing margins means that ad
> joining margins (no non-empty content, padding or border areas or clearance separate them) of two or more boxes (which may be next to one another or nested) combine to form a single margin. 

**当一个元素包含在另一个元素中时（假设没有内边距或边框把外边距分隔开），它们的上和/或下外边距也会发生合并**

> 只有垂直方向才会出现margin重合



**父Padding和子Margin会叠加** （That's what I want）

>>:子盒的Margin属于父的content

inline元素size自动计算，inline-block元素可设置size，block元素从新行开始

> margin也能用于内联元素，这是规范所允许的，但是margin-top和margin-bottom对内联元素（对行）的高度没有影响
> 
> 如果你要改变内联元素的行高即类似文本的行间距，那么你只能使用这三个属性：line-height，fong-size，vertical-align

> 这些个元素img|input|select|textarea|button|label虽然是内联元素，但margin依旧可以影响到他的上下左右

> 可置换元素（Replaced element）。他们区别一般inline元素（相对而言，称non-replaced element）是：这些元素拥有内在尺寸(intrinsic dimensions),他们可以设置width/height属性。他们的性质同设置了display:inline-block的元素一致

### 负Margin ###

> 在margin属性中一共有两类参考线，top和left的参考线属于一类，right和bottom的参考线属于另一类。top和left是以外元素为参考，right和bottom是以元素本身为参考。margin的位移方向是指margin数值为正值时候的情形，如果是负值则位移方向相反。

在absolute定位中，margin-left，margin-top调节相对于lef和right已经定位好的位置

## run-in ##
run-in框的行为如下：

> - 如果run-in框包含一个块框，那么run-in框变为块框。
> - 如果run-in框的后继兄弟元素为块框（非浮动，非绝对定位），那么run-in框变为该块框的第一个行内框。run-in不能插入本身为run-in的块中，也不能插入块中已有run-in的块中。
> - 否则，run-in框变为块框。


浏览器支持：IE8+，chrome（unsupport）


## BFC ##
### BFC和IFC ###

> - block-level box:display 属性为 block, list-item, table 的元素，会生成 block-level box。并且参与 block fomatting context；
> - inline-level box:display 属性为 inline, inline-block, inline-table 的元素，会生成 inline-level box。并且参与 inline formatting context；

### BFC布局规则： ###
- 内部的Box会在垂直方向，一个接一个地放置。
- Box垂直方向的距离由margin决定。属于同一个BFC的两个相邻Box的margin会发生重叠
- 每个元素的margin box的左边， 与包含块border box的左边相接触(对于从左往右的格式化，否则相反)。即使存在浮动也是如此。
- BFC的区域不会与float box重叠。
- BFC就是页面上的一个隔离的独立容器，容器里面的子元素不会影响到外面的元素。反之也如此。
- 计算BFC的高度时，浮动元素也参与计算

### 哪些元素会生成BFC? ###
- 根元素
- float属性不为none
- position为absolute或fixed
- display为inline-block, table-cell, table-caption, flex, inline-flex
- overflow不为visible

生成新的BFC，位置仍然从原先的位置开始排，后继的Block元素会跟着之前的元素

对p使用overflow:hidden;生成一个新的BFC，不被float元素遮盖，可以解决文字环绕的问题；
### div ###
div默认占满一行

### 总结 ###

> 因为BFC内部的元素和外部的元素绝对不会互相影响，因此， 当BFC外部存在浮动时，它不应该影响BFC内部Box的布局，BFC会通过变窄，而不与浮动有重叠。同样的，当BFC内部有浮动时，为了不影响外部元素的布局，BFC计算高度时会包括浮动的高度。避免margin重叠也是这样的一个道理。

## 行内格式化上下文(Inline formatting contexts) ##
- 在一个行内格式化上下文中，盒是一个接一个水平放置的，从包含块的顶部开始
- 这些盒之间的水平margin，border和padding都有效
- 盒可能以不同的方式竖直对齐：以它们的底部或者顶部对齐，或者以它们里面的文本的基线对齐

### 行盒(line box) ###

- 包含来自同一行的盒的矩形区域叫做行盒(line box)
- line box的宽度由包含块和float情况决定,一般来说,line box的宽度等于包含块两边之间的宽度,**然而float可以插入到包含块和行盒边之间,如果有float,那么line box的宽度会比没有float时小** float占据了原本属于行盒的控件，float不属于行盒的空间,text-align在float占据空间之后的剩余空间进行计算，text-align决定了自身包含的行盒内的元素如何对齐
- line box的高度由line-height决定,而line box之间的高度各不相同(比如只含文本的line box高度与包含图片的line box高度之间)
- line box的高度能够容纳它包含的所有盒,当盒的高度小于行盒的高度(例如,如果盒是baseline对齐)时,盒的竖直对齐方式由vertical-align属性决定//vertical-align决定了自身和其他元素的对齐方式
- 当一行的行内级盒的总宽度小于它们所在的line box的宽度时，它们在行盒里的水平分布由text-align属性决定。如果该属性值为justify，用户代理可能会拉伸行内盒（不包括inline-table和inline-block盒）里的空白和字（间距）

### 行内盒(inline box) ###

- 一个inline box是一个（特殊的）行内级盒，其内容参与了它的包含行内格式化上下文
- 当一个inline box超出一个line box的宽度时，它会被分成几个盒，并且这些盒会跨多line box分布。如果一个inline-block无法分割（例如，如果该inline box含有一个单个字符，或者特定语言的单词分隔规则不允许在该inline box里分隔，或如果该inline box受到了一个值为nowrap或者pre的white-space的影响），那么该inline box会从line box溢出
- 当一个inline box被分割后，margin，border和padding在发生分割的地方（或者在任何分割处，如果有多处的话）不会有可视化效果
- 同一个line box里的inline box也可能因为双向（bidirectional）文本处理而被分割成几个盒
- 需要盛放（hold）一个行内格式化上下文中的行内级内容时，创建一个line box。不含文本、保留空白符（preserved white space）、margin，padding或者border非0的行内元素、其它流内内容（例如，图片，inline block或者inline table），并且不以保留换行符（preserved newline）结束的line box必须被当作一个0高度的line box，为了确定它里面所有元素的位置，而其它时候（for any other purpose）必须当它不存在

### 行高 ###
![line-height.png](http://ww1.sinaimg.cn/large/48ceb85dly1gcs25kvyflj20go0a0q3h.jpg)

line-height不是相邻文本行间上一个baseline与下一文本行baseline之间的距离,而是line box的高度,也就是相邻文本行间底线的距离

决定line box高度的是line-height值,但是实际上是content area以及vertical spacing决定的,line-height=content area+vertical spacing

需要注意的是,content area不等于font-size,只有在simsun(宋体)下,两者相等

- 计算line box中每个行内级盒的高度时,对于可替换元素,inline-block元素和inline-table元素,这个值就是其margin box的高度;对于inline box,这个值是其line-height
- 行内级盒是根据其vertical-align属性竖直对齐的.如果它们是top或者bottom对齐,它们必须对齐得让line box高度最小化.如果这样的盒足够高,存在多个解,而CSS 2.1没有定义line box基线的位置
- line box高度是最高的盒的top与最低的盒的bottom之间的距离

当行高为数字或百分比时，会根据font-size来进行计算

符合以下任一条件即会生成一个IFC

- 块级元素中仅包含内联级别元素

形成条件非常简单，需要注意的是当IFC中有块级元素插入时，会产生两个匿名块将父元素分割开来，产生两个IFC

IFC布局规则

- 子元素水平方向横向排列，并且垂直方向起点为元素顶部。
- 子元素只会计算横向样式空间，【padding、border、margin】，**垂直方向样式空间不会被计算**，【padding、border、margin】。
- 在垂直方向上，子元素会以不同形式来对齐（vertical-align）
- 能把在一行上的框都完全包含进去的一个矩形区域，被称为该行的行框（line box）。行框的宽度是由包含块（containing box）和与其中的浮动来决定。
- IFC中的“line box”一般左右边贴紧其包含块，但float元素会优先排列。
- IFC中的“line box”高度由 CSS 行高计算规则来确定，同个IFC下的多个line box高度可能会不同。
- 当 inline-level boxes的总宽度少于包含它们的line box时，其水平渲染规则由 text-align 属性值来决定。
- 当一个“inline box”超过父元素的宽度时，它会被分割成多个boxes，这些 oxes 分布在多个“line box”中。如果子元素未设置强制换行的情况下，“inline box”将不可被分割，将会溢出父元素。

**很多时候，上下间距不生效可以使用IFC来解释**

#### 图片去除缝隙的解决方案 ####

- 图片display:block
- 图片vertical-align:bottom
- 父元素设置line-height:0 原因是当line-height:0时,行盒的基线会上移

 可替换元素的基线取决于marginbox的底部

float优先级
![articlex.png](http://ww1.sinaimg.cn/large/48ceb85dly1gcrb0o0lwuj20b4041aan.jpg)

## vertical-align ##
vertical-align用来指定行内元素（inline）或表格单元格（table-cell）元素的垂直对齐方式。也就是说，**对于块级元素，vertical-align是不起作用的**

vertical-align的属性值可以归为以下4类：

- 线类，如 baseline、top、middle、bottom；
- 文本类，如 text-top、text-bottom；
- 上标下标类，如 sub、super；
- 数值百分比类，如 10px、1em、5%；

### 线类 ###

baseline，baseline为vertical-align的默认值，其意思是指基线对齐，所谓基线，指的是字母x的下边缘

如果一个inline-block元素，里面没有内联元素，或者overflow不是visible，则该元素的基线是其margin底边缘如果一个inline-block元素，里面没有内联元素，或者overflow不是visible，则**该元素的基线是其margin底边缘**；

top，对于内联元素，指的是元素的顶部和当前行框盒子的顶部对齐；对于table-cell元素，指的是元素的顶padding边缘和表格行的顶部对齐。

### 文本类 ###

text-top，指的是盒子的顶部和父级内容区域的顶部对齐。

text-bottom，指的是盒子的底部和父级内容区域的底部对齐

### 数值百分比类 ###

vertical-align是支持数值的，并且兼容性也非常好，但大部分开发人员却不知道vertical-align支持数值。对于数值，正值表示由基线往上偏移，负值表示由基线往下偏移。而百分比则是基于line-height来计算的，百分比用得比较少，因为line-height一般都是开发人员给出的，这时候数值就可以精确定位元素，不需要再使用百分比再去计算一遍。

### vertical-align起作用的前提 ###

vertical-align起作用是有前提条件的，这个前提条件就是：只能应用于内联元素以及display值为table-cell的元素。在css中，有些css属性是会改变元素的display值的，例如float和position: absolute，一旦设置了这两个属性之一，元素的display值就是变为block，因此，vertical-align也就失去了作用。

父元素没有line-height或者line-height很小，也可能会使vertical-align不起作用

### line-height ###
行高是作用在每一个行框盒子(line-box)上的，而行框盒子则是由内联盒子组成

对于块级元素和替换元素，行高是无法决定最终高度的，只能决定行框盒子的最小高度。

每一个行框盒子都有一个看不见的节点，该节点继承了line-height;*内联元素是会继承父元素的line-height的；*

如果元素本身有line-height，父元素的实际高度/line-height由内部元素的line-height和vertical-align计算而得

x-height指的就是字母x的高度，ex是一个尺寸单位，其大小是相对字母x的来计算的，即1ex就表示1个字母x的高度

行距的计算为：line-height - em-box，em-box指的是1em的大小，因此行距可以表示为：line-height - font-size

span也继承了line-height: 32px，但两者的font-size不一样，这就导致了"strut"的font-size比较小，而span的font-size比较大，也就是说它们的基线不在同一位置上，"strut"偏上一点，而span默认又是基线对齐，为此，span总体会往上移以便跟"strut"基线对齐，.box元素就是这样被撑高了。

而解决方案可以有以下几种：

- span元素不使用基线对齐，可以改为top对齐
- span元素块状化
- line-height设置为0
- font-size设置为0

### 行高与图片 ###
我们知道了图片是基于baseline对齐的,所以行高影响的是文本的高度,而不是图片的高度

图片去除缝隙的解决方案

- 图片display:block
- 图片vertical-align:bottom
- 父元素设置line-height:0 原因是当line-height:0时,行盒的基线会上移

&lt;!DOCTYPE html&gt; 会影响line-height的呈现//没有标签，line-height无法撑起没有直接文字的容器；

## 移动端显示 ##
在移动端显示太小，需要添加以下元数据，使页面适应设备

    <meta name="viewport" content="width=device-width,initial-scale=1,shrink-to-fit=no">

## Grid 布局 ##
> 设计师通过在网格容器（grid container）上定义网格定义行（grid definition rows）和网格定义列（grid definition columns）属性各在网格项目（grid item）上定义网格行（grid row）和网格列（grid columns）为每一个网格项目（grid item）定义位置和空间著作权归作者所有。

[Grid](https://segmentfault.com/a/1190000012889793)

**grid-definition-x 更正为 grid-template-x**

**端线命名 [column-line-name] 引用 grid-column-start:column-line-name;**

### 父容器(Grid Container)的属性 ###
#### display:grid|inline-grid|subgrid; ####
column,float,clear,vertical-align对 grid container没有影响
#### grid-template-columns/grid-template-grid ####
行列的网格宽度定义
#### grid-template-areas ####
定义网格模板
#### grid-template ####
grid-template-rows,grid-template-columns,grid-template-areas的简写

grid-template:none|subgrid|<grid-template-rows>/<grid-template-columns>

#### grid-column-gap/grid-row-gap ####
网格间距的距离
#### justify-items ####
justify-items:start|end|center|stretch(default);

沿着行轴对齐的网格内的内容，适用于容器内所有的grid items
#### align-items ####
justify-items:start|end|center|stretch(default);

沿着列轴对齐grid item里的内容，同上
#### justify-content ####
网格总大小小于其网格容器的大小，如果grid items使用px这样非弹性单位设置大小可能遇到此种情况

justify-content:start|end|center|stretch|space-around|space-between|space-evenly
#### align-content ####
列轴方向，同上
#### grid-auto-columns/grid-auto-rows ####
隐式轨道大小，超出布局范围的计算
#### grid-auto-flow ####
grid-auto-flow:row|column|dense;

没有指明放置位置的项目的摆放方式
### 孩子(Grid Items)的属性 ###
grid-column-start|grid-column-end|grid-row-start|grid-row-end

确定grid item在网格内的位置

span<number> 越过指定数量的网格轨道

span<name> 越过知道遇到特定的名字的网格线

默认越过1个

#### grid-area ####
给grid item以命名的方式使用确定位置

<name>

<row-start>/<column-start>/<row-end>/<column-end>
#### justify-self ####
justify-self:start|end|center|stretch;

沿行轴对戏网格项的内容，对单个网格内容生效
#### align-self ####
沿列轴方向对齐内容，同上
### 按网格线布局 ###
    #grid{
      display: grid;
      background: orange;
    
      grid-template-columns: auto minmax(min-content, 1fr);
      grid-template-rows: auto minmax(min-content, 1fr) auto;
    }
    
    #title{
      grid-column: 1;
      grid-row: 1;
      background-color: red;
    }
    
    #score{
      grid-column: 1;
      grid-row: 3;
      background-color: green;
    }
    
    #stats{
      grid-column: 1;
      grid-row: 2;
      justify-self:start;
      background-color: #e9f;
    }
    
    #board{
      grid-column: 2;
      grid-row: 1/span 2;
      background-color: #ccc;
    }
    
    #controls{
      grid-column: 2;
      grid-row: 3;
      align-self: center;
      background-color: yellow;
    }

### 按区域布局 ###
    @media(orientation:portrait){
    #grid{
    display: grid;
    grid-template: "title stats"
       			   "score stats"
                   "board board"
                   "ctrls ctrls";
    grid-template-columns: auto minmax(min-content,1fr);
    grid-template-rows: auto auto minmax(min-content,1fr) auto;
      }
    }
    
    @media (orientation:landscape) {
    #grid{
    display: grid;
    grid-template: "title board"
                   "stats board"
                   "score ctrls";
    grid-template-columns: auto minmax(min-content,1fr);
    grid-template-rows: auto minmax(min-content,1fr) auto;
      }
    }
    
    #title{grid-area: title;background-color: red;}
    #score{grid-area: score;background-color: green;}
    #stats{grid-area: stats;background-color: #e9f;}
    #board{grid-area: board;background-color: #ccc;}
    #controls{grid-area: ctrls;background-color: yellow;}

## 内联元素 ##
内联元素的宽度由内容计算，不能设置width

内联元素之间默认有4px列间距，5px行间距(处理字体间间距)，可设置父元素font-size:0;

去除边距，使用div包裹，设置font-size:0;或line-height:0;

## 块元素 ##
postion:absolute时，块元素自动伸缩

正常文档流等于父元素的宽度，absolute脱离了正常的文档流，宽度为最小宽度

## box-sizing ##
- content-box:默认，元素的width不包括padding和border
- border-box: 元素的width包含padding和border
- padding-box: 已删除

## calc函数 ##
 width: calc(100% - 100px);
## 伪元素 ##
:first-child 元素为其父元素的第一个子元素

：first 符合条件的第一个元素

:before 的元素属于当前的div定位
## 元素隐藏 ##
display:none; 不绘制

visibility:hidden; 不绘制，占据空间 与height:0;配合

    { display: none; /* 不占据空间，无法点击 */ } 
    { visibility: hidden; /* 占据空间，无法点击 */ } 
    { position: absolute; top: -999em; /* 不占据空间，无法点击 */ } 
    { position: relative; top: -999em; /* 占据空间，无法点击 */ } 
    { position: absolute; visibility: hidden; /* 不占据空间，无法点击 */ } 
    { height: 0; overflow: hidden; /* 不占据空间，无法点击 */ } 
    { opacity: 0; filter:Alpha(opacity=0); /* 占据空间，可以点击 */ } 
    { position: absolute; opacity: 0; filter:Alpha(opacity=0); /* 不占据空间，可以点击 */ } 
    { 
	    zoom: 0.001; 
	    -moz-transform: scale(0); 
	    -webkit-transform: scale(0); 
	    -o-transform: scale(0); 
	    transform: scale(0); 
	    /* IE6/IE7/IE9不占据空间，IE8/FireFox/Chrome/Opera占据空间。都无法点击 */ 
    } 
    { 
	    position: absolute; 
	    zoom: 0.001; 
	    -moz-transform: scale(0); 
	    -webkit-transform: scale(0); 
	    -o-transform: scale(0); 
	    transform: scale(0); 
	    /* 不占据空间，无法点击 */ 
    } 
## 文本换行 ##
word-break:break-all|keep-all|keep-all;

## 重绘与回流 ##

[重绘与回流](http://www.zhangxinxu.com/wordpress/2010/01/%E5%9B%9E%E6%B5%81%E4%B8%8E%E9%87%8D%E7%BB%98%EF%BC%9Acss%E6%80%A7%E8%83%BD%E8%AE%A9javascript%E5%8F%98%E6%85%A2%EF%BC%9F/)

> 当一个元素的外观的可见性visibility发生改变的时候，**重绘(repaint)**也随之发生，但是不影响布局。类似的例子包括：outline, visibility, or background color

**重绘的代价是高昂的，因为浏览器必须验证DOM树上其他节点元素的可见性**

> **回流**更是性能的关键因为其变化涉及到部分页面（或是整个页面）的**布局**。一个元素的回流导致了其所有子元素以及DOM中紧随其后的祖先元素的随后的回流。

### 那什么会导致回流呢？ ###
1. 调整窗口大小（Resizing the window）
1. 改变字体（Changing the font）
1. 增加或者移除样式表（Adding or removing a stylesheet）
1. 内容变化，比如用户在input框中输入文字（Content changes, such as a user typing text in
1. an input box）
1. 激活 CSS 伪类，比如 :hover (IE 中为兄弟结点伪类的激活)（Activation of CSS pseudo classes such as :hover (in IE the activation of the pseudo class of a sibling)）
1. 操作 class 属性（Manipulating the class attribute）
1. 脚本操作 DOM（A script manipulating the DOM）
1. 计算 offsetWidth 和 offsetHeight 属性（Calculating offsetWidth and offsetHeight）
1. 设置 style 属性的值 （Setting a property of the style attribute）

### 如何避免回流或将它们对性能的影响降到最低？ ###
1. 尽可能在DOM树的最末端改变class
1. 避免设置多层内联样式
1. 动画效果应用到position属性为absolute或fixed的元素上
1. 牺牲平滑度换取速度
1. 避免使用table布局
2. 避免使用CSS的JavaScript表达式

## 动态添加和更改css ##
### 添加style节点 ###
    var style = document.createElement("style");
    style.type = "text/css";
    var textNode = document.createTextNode("body{background-color:red}");
    style.appendChild(textNode);
    var head = document.getElementsByTagName("head")[0];
    head.append(style);

IE 

    style.styleSheet.cssText = "body{background-color:red}";

### 添加CSS规则 ###
    var sheet = document.styleSheets[0]; //styleSheet的length为0，undefined
    var rules = sheet.cssRules||sheet.rules;
    var rule = rules[0];
    rule.style.backgroundColor = "red";

创建规则

    sheet.insertRule("body {background-color:silver}",0);

删除规则

    sheet.deleteRule(0);

### JQuery 修改style属性###
    $(this).css("color","red");
## hack ##

### 头部底部固定，内容滚动布局 ###

    .container{
    	height:100vh; //屏幕的高度是100vh
	    display:flex;
	    flex-direction:column;//竖直方向布局
    	overflow-y:hidden;//可以不用
    }
    
    .content{
    	flex:1; //内容部分撑开或缩小适应剩余屏幕，shrink默认为1
    	overflow-y:scroll;//内容可滚动
    }
    
    .content-item{
	    flex-shrink:0;//子项目不可以缩小，以超出父高度滚动
    }

### 嵌套内部部分滑动 ###
外部设置scroll，内部也设置scroll，内部高度将不会超过外部，使外部不会产生滑动，问题是会在网页端产生两条滑动条。手机APP端将不会看到条状的滑动条
### 边框重叠 ###
使用负margin，margin-right:-1px;

## 过渡与动画 ##
### Transition ###
transition的优点在于简单易用，但是它有几个很大的局限。

1. transition需要事件触发，所以没法在网页加载时自动发生。
2. transition是一次性的，不能重复发生，除非一再触发。
3. transition只能定义开始状态和结束状态，不能定义中间状态，也就是说只有两个状态。
4. 一条transition规则，只能定义一个属性的变化，不能涉及多个属性。

### Animation ###
#### step函数 ####
第一个参数number为指定的间隔数，指的是把两个关键帧之间的动画分为n步阶段性展示，而不是keyframes写的变化次数

第二个参数可选，接受start和end两个值：指定在每个间隔的起点或是终点发生阶跃变化

![](http://ww1.sinaimg.cn/large/48ceb85dgy1frr5c4620lj20e30ed0sr.jpg)
## SASS ##
SASS是一种CSS的开发工具，提供了许多便利的写法，大大节省了设计者的时间，使得CSS的开发，变得简单和可维护。

    sass --style compressed test.sass test.css

基本语法

    $side : left;
    .rounded {
	    border-#{$side}-radius: 5px;
    }

允许定义变量， 计算， 可嵌套

嵌套

	#content {
	  article {
	    h1 { color: #333 }
	    p { margin-bottom: 1.4em }
	  }
	  aside { background-color: #EEE }
	}
### 注释 ###
/*comment*/ 保留到编译的文件

//comment 编译后省略 
### 继承 ###

    .class2 {
		@extend .class1;
	　　 font-size:120%;
	}

### Mixin 宏 ###
    @mixin left {
       float: left;
    　　margin-left: 10px;
    }
    
    div {
    　　@include left;
    }

使用参数

    @mixin rounded($vert, $horz, $radius: 10px) {
      border-#{$vert}-#{$horz}-radius: $radius;
      -moz-border-radius-#{$vert}#{$horz}: $radius;
      -webkit-border-#{$vert}-#{$horz}-radius: $radius;
    }

支持条件控制语句

@if @else @for @while @each 

支持函数

    @function double($n) {
      @return $n * 2;
    }

    #sidebar {
    　width: double(5px);
    }

### 父选择器 & ###
	article a {
	  color: blue;
	  :hover { color: red }
	}
	这意味着color: red这条规则将会被应用到选择器article a :hover，article元素内链接的所有子元素在被hover时都会变成红色。这是不正确的！
	
	article a {
	  color: blue;
	  &:hover { color: red }
	}

在选择器之前添加父选择器

	#content aside {
	  color: red;
	  body.ie & { color: green }
	}

### 群组选择器的嵌套 ###
	.container {
	  h1, h2, h3 {margin-bottom: .8em}
	}
	
	nav, aside {
	  a {color: blue}
	}

### 子组合选择器和同层组合选择器：>、+和~ ###
	article {
	  ~ article { border-top: 1px dashed #ccc }
	  > section { background: #eee }
	  dl > {
	    dt { color: #333 }
	    dd { color: #555 }
	  }
	  nav + & { margin-top: 0 }
	}

### 嵌套属性 ###
nav {
  border: 1px solid #ccc {
  left: 0px;
  right: 0px;
  }
}

### 嵌套导入 ###
	aside {
	  background: blue;
	  color: white;
	}
然后把它导入到一个CSS规则内，如下所示：

	.blue-theme {@import "blue-theme"}

	//生成的结果跟你直接在.blue-theme选择器内写_blue-theme.scss文件的内容完全一样。
	
	.blue-theme {
	  aside {
	    background: blue;
	    color: #fff;
	  }
	}
### 导入SASS文件 ###
sass局部文件的文件名以下划线开头。这样，sass就不会在编译时单独编译这个文件输出css

### 混合器 ###
	@mixin rounded-corners {
	  -moz-border-radius: 5px;
	  -webkit-border-radius: 5px;
	  border-radius: 5px;
	}
然后就可以在你的样式表中通过@include来使用这个混合器，放在你希望的任何地方。@include调用会把混合器中的所有样式提取出来放在@include被调用的地方。如果像下边这样写：

	notice {
	  background-color: green;
	  border: 2px solid #00aa00;
	  @include rounded-corners;
	}
	
	//sass最终生成：
	
	.notice {
	  background-color: green;
	  border: 2px solid #00aa00;
	  -moz-border-radius: 5px;
	  -webkit-border-radius: 5px;
	  border-radius: 5px;
	}

**实际上，混合器太好用了，一不小心你可能会过度使用。大量的重用可能会导致生成的样式表过大，导致加载缓慢**

### 混合器传参 ###
	@mixin link-colors($normal, $hover, $visited) {
	  color: $normal;
	  &:hover { color: $hover; }
	  &:visited { color: $visited; }
	}
### 选择器继承 ###
	//通过选择器继承继承样式
	.error {
	  border: 1px solid red;
	  background-color: #fdd;
	}
	.seriousError {
	  @extend .error;
	  border-width: 3px;
	}

以class="seriousError" 修饰的html元素最终的展示效果就好像是class="seriousError error"。

**.seriousError不仅会继承.error自身的所有样式，任何跟.error有关的组合选择器样式也会被.seriousError以组合选择器的形式继承**
## AntDesign ##
#### antDesign样式无法生效 ####

AntDesign手动引入 "antd/dist/antd.css"
可参考在index.html中引入

*使用 babel-plugin-import*

	npm install babel-plugin-import --save-dev

可在package.json的babel中加入

	// .babelrc or babel-loader option
	{
	  "plugins": [
	    ["import", {
	      "libraryName": "antd",
	      "libraryDirectory": "es",
	      "style": "css" // `style: true` 会加载 less 文件
	    }]
	  ]
	}
## CSS方案 ##
#### 竖直居中 ####
	& .vertCenter {
	    position: absolute;
	    top: 50%;
	    transform: translate(0, -50%);
	  }
## 设计 ##

> 建议读者不要使用以rem或者小程序rpx来实现等比缩放为主的布局手段，而使用面向逻辑像素px为主，面向等比缩放的vx和vxxx（vw/vh/vmax/vmin）为辅助的布局单位，搭配一些flex等布局手段
> 同样观看距离情况下，大屏看的更多而不是大屏看的更大的设计最佳实践来进行布局，并且以这种最佳实践作为理论依据来传递给设计师 ---知乎猫5号

**px是viewport像素，不是物理像素，不是逻辑像素，不是渲染像素**

![r.jpg](http://ww1.sinaimg.cn/large/48ceb85dly1g9llssnlz7j20lx0oqmz9.jpg)

- 物理像素（physical pixel）就是反映显示屏的硬件条件，反映的就是显示屏内部led灯的数量
- 渲染像素（render pixel），则是在系统内部对物理像素的分配进行再一次的调整，在pc上，渲染像素其实就是设置里边的分辨率。对于显示设备，系统为显示设备提供渲染尺寸
- 逻辑像素/点（device point / device pixel / point ），是为了调和距离不一样导致的差异，将所有设备根据距离，透视缩放到一个相等水平的观看距离之后得到的尺寸，是一个抽象的概念，这个单位就是ios开发的px，安卓开发的dp。
- ppi（pixel per inch） 每英寸像素，指的是屏幕在每英寸的物理像素，更高的ppi意味着屏幕的清晰度更佳

> 所谓高分屏，其实就是指ppi大于同类设备的屏幕。比如对于桌面设备，大于96ppi。对于移动设备，大于160ppi
> 
> 所谓视网膜屏，其实就是指在该观看距离内超出人类的辨认能力的屏幕。比如对于桌面设备，大于192ppi。对于移动设备大于326ppi

逻辑像素长度 = 物理像素长度 * 160 / ppi

- dpr （device point ratio / device pixel ratio） 渲染像素与逻辑像素的比例。由于渲染像素一般等于逻辑像素，如果ppi是以160为基准的话，那么 dpr = ppi / 160多少倍屏或者多少x（三倍屏，3x，意思就是3dpr），一般来说就是说的是这个值

> viewport像素又是什么，它本质是DIP（Device Independent Pixels），中文意思设备无关像素，是与上述所有像素都无绝对逻辑关系的一个单位。其实是浏览器内部对逻辑像素进行再处理的结果，简单来理解就是调整逻辑像素的缩放来达到适应设备的一个中间层

**对于pc，viewport是不生效的，所以在pc上，px其实就是逻辑像素（chrome）**

面向逻辑像素开发的基本开发流程

1. 在head 设置width=device-width的viewport
2. 在css中使用px
3. 在适当的场景使用flex布局，或者配合vw进行自适应
4. 在跨设备类型的时候（pc <-> 手机 <-> 平板）使用媒体查询
5. 在跨设备类型如果交互差异太大的情况，考虑分开项目开发那么


> viewport width=device-width是什么意思，其实就是让viewport的尺寸等于逻辑像素的尺寸

## 屏幕适配 ##
web app简单原则：文字流式，控件弹性，图片等比缩放。

### 网易适配 ###
375*680的比320*680的导航栏明显要高。能够达到这种效果的根本原因就是因为网易页面里除了font-size之外的其它css尺寸都使用了rem作为单位

最根本的原因在于，网易页面上html的font-size不是预先通过媒介查询在css里定义好的，而是通过js计算出来的

拿网易来说，它的设计稿应该是基于iphone4或者iphone5来的，所以它的设计稿竖直放时的横向分辨率为640px；为了计算方便，取一个100px的font-size为参照，那么body元素的宽度就可以设置为width: 6.4rem，于是html的font-size=deviceWidth / 6.4。这个deviceWidth就是viewport设置中的那个deviceWidth

这个deviceWidth通过document.documentElement.clientWidth就能取到了，所以当页面的dom ready后，做的第一件事情就是：

document.documentElement.style.fontSize = document.documentElement.clientWidth / 6.4 + 'px';

font-size可能需要额外的媒介查询，并且font-size不能使用rem，如网易的设置：
@media screen and (max-width:321px){
    .m-navlist{font-size:15px}
}

@media screen and (min-width:321px) and (max-width:400px){
    .m-navlist{font-size:16px}
}

@media screen and (min-width:400px){
    .m-navlist{font-size:18px}
}

device-width的计算公式为：设备的物理分辨率/(devicePixelRatio * scale)

比较网易与淘宝的做法
共同点：

- 都能适配所有的手机设备，对于pad，网易与淘宝都会跳转到pc页面，不再使用触屏版的页面
- 都需要动态设置html的font-size
- 布局时各元素的尺寸值都是根据设计稿标注的尺寸计算出来，由于html的font-size是动态调整的，所以能够做到不同分辨率下页面布局呈现等比变化
- 容器元素的font-size都不用rem，需要额外地对font-size做媒介查询
- 都能应用于尺寸不同的设计稿，只要按以上总结的方法去用就可以了

不同点

- 淘宝的设计稿是基于750的横向分辨率，网易的设计稿是基于640的横向分辨率，还要强调的是，虽然设计稿不同，但是最终的结果是一致的，设计稿的尺寸一个公司设计人员的工作标准，每个公司不一样而已
- 淘宝还需要动态设置viewport的scale，网易不用
- 最重要的区别就是：网易的做法，rem值很好计算，淘宝的做法肯定得用计算器才能用好了 。不过要是你使用了less和sass这样的css处理器，就好办多了，以淘宝跟less举例，我们可以这样编写less：


	//定义一个变量和一个mixin

	@baseFontSize: 75; //基于视觉稿横屏尺寸/100得出的基准font-size
	.px2rem(@name, @px){
	    @{name}: @px / @baseFontSize * 1rem;
	}
	//使用示例：
	.container {
	    .px2rem(height, 240);
	}
	//less翻译结果：
	.container {
	    height: 3.2rem;
	}
# SVG #
- SVG 指可伸缩矢量图形 (Scalable Vector Graphics)
- SVG 用来定义用于网络的基于矢量的图形
- SVG 使用 XML 格式定义图形
- SVG 图像在放大或改变尺寸的情况下其图形质量不会有所损失
- SVG 是万维网联盟的标准
- SVG 与诸如 DOM 和 XSL 之类的 W3C 标准是一个整体

## SVG形状 ##
### 矩形 rect ### 
	rect width="300" height="100"
	style="fill:rgb(0,0,255);stroke-width:1;
	stroke:rgb(0,0,0)"

rx 和 ry 属性可使矩形产生圆角

### 圆形 circle ###
	circle cx="100" cy="50" r="40" stroke="black"
	stroke-width="2" fill="red"
### 椭圆 ellipse ###
	ellipse cx="300" cy="150" rx="200" ry="80"
	style="fill:rgb(200,100,50);
	stroke:rgb(0,0,100);stroke-width:2"

- cx 属性定义圆点的 x 坐标
- cy 属性定义圆点的 y 坐标
- rx 属性定义水平半径
- ry 属性定义垂直半径


### 线 line ###
	line x1="0" y1="0" x2="300" y2="300"
	style="stroke:rgb(99,99,99);stroke-width:2"

- x1 属性在 x 轴定义线条的开始
- y1 属性在 y 轴定义线条的开始
- x2 属性在 x 轴定义线条的结束
- y2 属性在 y 轴定义线条的结束

### 折线 polyline ###
	polyline points="0,0 0,20 20,20 20,40 40,40 40,60"
	style="fill:white;stroke:red;stroke-width:2"
### 多边形 polygon ###
	polygon points="220,100 300,210 170,250"
	style="fill:#cccccc;
	stroke:#000000;stroke-width:1"

- points 属性定义多边形每个角的 x 和 y 坐标
### 路径 path ###
- M = moveto
- L = lineto
- H = horizontal lineto
- V = vertical lineto
- C = curveto
- S = smooth curveto
- Q = quadratic Belzier curve
- T = smooth quadratic Belzier curveto
- A = elliptical Arc
- Z = closepath

## SVG滤镜 ##
- feBlend
- feColorMatrix
- feComponentTransfer
- feComposite
- feConvolveMatrix
- feDiffuseLighting
- feDisplacementMap
- feFlood
### feGaussianBlur ###
	<defs>
	<filter id="Gaussian_Blur">
	<feGaussianBlur in="SourceGraphic" stdDeviation="3" />
	</filter>
	</defs>
	
	<ellipse cx="200" cy="150" rx="70" ry="40"
	style="fill:#ff0000;stroke:#000000;
	stroke-width:2;filter:url(#Gaussian_Blur)"/>

- feImage
- feMerge
- feMorphology
- feOffset
- feSpecularLighting
- feTile
- feTurbulence
- feDistantLight
- fePointLight
- feSpotLight

## SVG 渐变 ##
- 线性渐变
- 放射性渐变

### 线性渐变 ###
linearGradient 可用来定义 SVG 的线性渐变。

- 当 y1 和 y2 相等，而 x1 和 x2 不同时，可创建水平渐变
- 当 x1 和 x2 相等，而 y1 和 y2 不同时，可创建垂直渐变
- 当 x1 和 x2 不同，且 y1 和 y2 不同时，可创建角形渐变

	<defs>
	<linearGradient id="orange_red" x1="0%" y1="0%" x2="100%" y2="0%">
	<stop offset="0%" style="stop-color:rgb(255,255,0);
	stop-opacity:1"/>
	<stop offset="100%" style="stop-color:rgb(255,0,0);
	stop-opacity:1"/>
	</linearGradient>
	</defs>
	
	<ellipse cx="200" cy="190" rx="85" ry="55"
	style="fill:url(#orange_red)"/>

### 放射性渐变 ###

	<defs>
	<radialGradient id="grey_blue" cx="50%" cy="50%" r="50%"
	fx="50%" fy="50%">
	<stop offset="0%" style="stop-color:rgb(200,200,200);
	stop-opacity:0"/>
	<stop offset="100%" style="stop-color:rgb(0,0,255);
	stop-opacity:1"/>
	</radialGradient>
	</defs>
	
	<ellipse cx="230" cy="200" rx="110" ry="100"
	style="fill:url(#grey_blue)"/>

&lt;radialGradient&gt; 标签的 id 属性可为渐变定义一个唯一的名称，fill:url(#grey_blue) 属性把 ellipse 元素链接到此渐变，cx、cy 和 r 属性定义外圈，而 fx 和 fy 定义内圈 渐变的颜色范围可由两种或多种颜色组成。每种颜色通过一个 &lt;stop&gt; 标签来规定。offset 属性用来定义渐变的开始和结束位置。
# 万维网 #
## 备案查询 ##

http://icp.chinaz.com

# Nginx #
## 安装 ##
    ./configure --sbin-path=/usr/local/nginx/nginx \
    --conf-path=/usr/local/nginx/nginx.conf \
    --pid-path=/usr/local/nginx/nginx.pid \
    --with-http_ssl_module \
    --with-pcre=/usr/local/src/pcre-8.34 \
    --with-zlib=/usr/local/src/zlib-1.2.8 \
    --with-openssl=/usr/local/src/openssl-1.0.1c
    make
    make install

#### no rule to make target libpcre.la'. stop ####
使用低版本的pcre pcre2会出错

## 重启 ##
### 简单型 ###
kill -QUIT `cat /usr/local/nginx/nginx/pid`

sudo /usr/local/nginx/nginx

### 平滑重启 ###
#### 检查配置文件 ####
/usr/local/nginx/nginx -t -c xxx.conf

#### 重启 ####
kill -HUP `主进程号`

它会尝试解析配置文件，如果成功就应用新的配置文件，运行新的工作进程并从容关闭旧的工作进程。在没有工作进程的情况下，会为主进程启动一个新的子工作进程

### 平滑升级 ###
使用新的编译版本

*kill -USR2 `旧的主进程号`*

旧的主进程号将被命名为 .oldbin后，执行新版本的nginx，一起启动新的主进程和新的工作进程

*kill -WINCH `旧的主进程号`*

逐步停止旧版本的nginx实例，然后它的工作进程就会从容关闭。

但是原来主进程并没有关闭

*kill -QUIT `旧的主进程`*

关闭旧的主进程，同时.oldbin 也会消失

也可以选择使用旧的主进程: 

*kill -HUP `旧的主进程`*

同时会启动工作进程

## 路由配置 ##
### 配置反向代理 ###
    location /test{
        proxy_pass http://127.0.0.1:8001
    }

### 配置根目录 ###
    location /{
        root www;
        index index.html index.html
    }

### 使用正则表达式重定向静态文件或特定文件 ###
处理php文件

    location \.php${
        proxy_pass http://localhost:xxxx
    }

处理静态文件

    location ^/(css|js|images|static|media)/{
        root xxxx
    }

# 安全 #
## OAuth2.0 ##
[理解OAuth2.0 阮一峰](http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html "理解OAuth2.0 阮一峰")

### 名词定义 ###
    （1） Third-party application：第三方应用程序，本文中又称"客户端"（client），即上一节例子中的"云冲印"。
    
    （2）HTTP service：HTTP服务提供商，本文中简称"服务提供商"，即上一节例子中的Google。
    
    （3）Resource Owner：资源所有者，本文中又称"用户"（user）。
    
    （4）User Agent：用户代理，本文中就是指浏览器。
    
    （5）Authorization server：认证服务器，即服务提供商专门用来处理认证的服务器。
    
    （6）Resource server：资源服务器，即服务提供商存放用户生成的资源的服务器。它与认证服务器，可以是同一台服务器，也可以是不同的服务器。

### OAuth的思路 ###
> OAuth在"客户端"与"服务提供商"之间，设置了一个授权层（authorization layer）。"客户端"不能直接登录"服务提供商"，只能登录授权层，以此将用户与客户端区分开来。"客户端"登录授权层所用的令牌（token），与用户的密码不同。用户可以在登录的时候，指定授权层令牌的权限范围和有效期。
> 
> "客户端"登录授权层以后，"服务提供商"根据令牌的权限范围和有效期，向"客户端"开放用户储存的资料。

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fq8o66u8x5j20la0bfjre.jpg)

（A）用户打开客户端以后，客户端要求用户给予授权。

（B）用户同意给予客户端授权。

（C）客户端使用上一步获得的授权，向认证服务器申请令牌。

（D）认证服务器对客户端进行认证以后，确认无误，同意发放令牌。

（E）客户端使用令牌，向资源服务器申请获取资源。

（F）资源服务器确认令牌无误，同意向客户端开放资源。

### 客户端的授权模式 ###
- 授权码模式（authorization code）
- 简化模式（implicit）
- 密码模式（resource owner password credentials）
- 客户端模式（client credentials）

# 许可证 #
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqnlm8ar2nj20sg0f775l.jpg)

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqnlnhzcd2j20m80dw3zd.jpg)


# Lodash #
数据分组

_(dataList.list).groupBy(item=>item.lc).map((items,lc)=>{return {lc:lc, items:items}}).value()

# TypeScript #
### TypeScript的module加载模式 ###
**import {b} from "moduleB"**
#### Classic ####

- /root/src/folder/moduleB.ts
- /root/src/folder/moduleB.d.ts
- /root/src/moduleB.ts
- /root/src/moduleB.d.ts
- /root/moduleB.ts
- /root/moduleB.d.ts
- /moduleB.ts
- /moduleB.d.ts

#### Node ####
- /root/src/node_modules/moduleB.js
- /root/src/node_modules/moduleB/package.json (if it specifies a "main" property)
- /root/src/node_modules/moduleB/index.js 

- /root/node_modules/moduleB.js
- /root/node_modules/moduleB/package.json (if it specifies a "main" property)
- /root/node_modules/moduleB/index.js 

- /node_modules/moduleB.js
- /node_modules/moduleB/package.json (if it specifies a "main" property)
- /node_modules/moduleB/index.js


# React #
## JSX元素 ##
> 元素是构成组件的”材料”
> 组件也是一种元素
### JSX表示对象 ###
	const element = (
	  <h1 className="greeting">
	    Hello, world!
	  </h1>
	);

等同于

	const element = React.createElement(
	  'h1',
	  {className: 'greeting'},
	  'Hello, world!'
	);

相当于创建了一个如下对象

	// 注意: 这是简化的结构
	const element = {
	  type: 'h1',
	  props: {
	    className: 'greeting',
	    children: 'Hello, world'
	  }
	};

<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

> React 元素是 不可突变（immutable） 的. 一旦你创建了一个元素, 就不能再修改其子元素或任何属性。一个元素就像电影里的一帧: 它表示在某一特定时间点的 UI

## 组件 ##
> 组件可以将UI切分成一些的独立的、可复用的部件，这样你就只需专注于构建每一个单独的部件。
### 函数定义/类定义组件 ###
	function Welcome(props) {
	  return <h1>Hello, {props.name}</h1>;
	}

在React中等同于 

	class Welcome extends React.Component {
	  render() {
	    return <h1>Hello, {this.props.name}</h1>;
	  }
	}

## 高阶组件 ##
高阶组件是参数为组件，返回值为新组件的函数

	const EnhancedComponent = higherOrderComponent(WrappedComponent);

组件是将 props 转换为 UI，而高阶组件是将组件转换为另一个组件


### ES6的模块导出 ###

	// 写法一
	export var m = 1;
	
	// 写法二
	var m = 1;
	export {m};
	
	// 写法三
	var n = 1;
	export {n as m};
	
	//使用
	
	import {m} from "./moduleName"

> 输出接口，不可输出值

<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

> Function are not valid as React child. This may happen if you return a Component instead of <Component/from render.

**组件可以理解为一组HTML标签，函数式组件必须对组件函数求值**

	function App(){return <Component/>}
	ReactDOM.render(<App/>,#ele);
	ReactDOM.render(App(),#ele);

## State & 生命周期 ##
> 状态与属性十分相似，但是状态是私有的，完全受控于当前组件。定义为类的组件有一些特性。局部状态就是如此：一个功能只适用于类。使用类就允许我们使用其它特性，例如局部状态、生命周期钩子

### State更新可能是异步的 ###
建议使用

	this.setState((prevState, props) => {
		counter:preState.counter + props.increment
	});

> setState将对象合并到当前的状态，即浅合并,所以可单独设置属性，也就是说this.setState({comments})完整保留了this.state.posts，但完全替换了this.state.comments

### 数据自顶向下流动 ###
父组件或子组件都不能知道某个组件是有状态还是无状态，并且它们不应该关心某组件是被定义为一个函数还是一个类。

这就是为什么状态通常被称为局部或封装。 除了拥有并设置它的组件外，其它组件不可访问

### this绑定 ###
#### 使用箭头函数 ####

	class LoggingButton extends React.Component {
	  // 这个语法确保 `this` 绑定在 handleClick 中。
	  // 警告：这是 *实验性的* 语法。
	  handleClick = () => {
	    console.log('this is:', this);
	  }
	
	  render() {
	    return (
	      <button onClick={this.handleClick}>
	        Click me
	      </button>
	    );
	  }
	}

或者

	class LoggingButton extends React.Component {
	  handleClick() {
	    console.log('this is:', this);
	  }
	
	  render() {
	    // 这个语法确保 `this` 被绑定在 handleClick 中
	    return (
	      <button onClick={(e) => this.handleClick(e)}>
	        Click me
	      </button>
	    );
	  }
	}

### 组件更新一致性 ###
#### Diffing 算法 ####
无论什么时候，当根元素类型不同时，React 将会销毁原先的树并重写构建新的树

> 当销毁原先的树时，之前的 DOM 节点将销毁。实例组件执行 componentWillUnmount() 。当构建新的一个树，新的 DOM 节点将会插入 DOM 中。组件将会执行 componentWillMount() 以及 componentDidMount() 。与之前旧的树相关的 state 都会丢失

当比较两个相同类型的 React DOM 元素时，React 检查它们的属性（attributes），保留相同的底层 DOM 节点，只更新反生改变的属性（attributes）

> 当一个组件更新的时候，组件实例保持不变，以便在渲染中保持state。React会更新组件实例的属性来匹配新的元素，并在元素实例上调用 componentWillReceiveProps() 和 componentWillUpdate()。
> 
> 接下来， render() 方法会被调用并且diff算法对上一次的结果和新的结果进行递归。

**利弊权衡**

- 算法不会尝试匹配不同节点类型的子树。如果你发现在有类似输出的两个不同节点类型中相互切换，你可能需要将其转化成同种类型，事实上，我们没有在其中发现问题。
- keys 应该是稳定的、可预测的并且是唯一的。不稳定的 key (类似于 Math.random() 函数的结果)可能会产生非常多的组件实例并且 DOM 节点也会非必要性的重新创建。这将会造成极大的性能损失和组件内state的丢失。

## Redux ##
减少reducer中的switch

#### 原代码 ####
	function todoApp(state = initialState, action){
		swtich(action.type) {
			//或者{...state,...newState}
			case SET_VISIBLITY_FILTER:
				return Object.assign({},state, {
					visiblityFilter:action.filter
				})
			default:
				return state
		}
	}
#### Reducers生成器 ####
	export const todos = createReducer([], {
		[ActionTypes.ADD_TODO](state, action){
			let text = action.text.trim();
			return [...state,text]
		}
	})
	
	function ceateReducer(initialState, handlers){
		return function reducer(state = initialState, action){
			if(handlers.hasOwnProperty(action.type)){
				return handlers[action.type](state, action);
			} else {
				return state;
			}
		}
	}

# Webpack #
分离css，js配置

	const HtmlWebpackPlugin = require('html-webpack-plugin');
	const MiniCssExtractPlugin = require('mini-css-extract-plugin');
	const webpack = require('webpack');
	
	module.exports = {
	  entry:{
	    app:__dirname + "/src",
	  },
	  output:{
	    path: __dirname + "/public",
	    filename:"[name].js",
	    chunkFilename:"[name].js"
	  },
	  devtool:'source-map',
	  module:{
	    rules:[
	      {
	        test:/\.js$/,
	        exclude:/node_modules/,
	        use:{
	          loader:"babel-loader"
	        }
	      },
	      {
	        test:/\.css$/,
	        use:[
	          {
	            loader:"style-loader"
	          },
	          {
	            loader:"css-loader",
	            options:{
	              modules:true,
	              importLoaders:1,
	              localIdentName:"[name]_[local]_[hash:base64]",
	              sourceMap:true,
	              minimize:true
	            }
	          }
	        ]
	      },
	      {
	        test:/\.scss$/,
	        use: [
	            // process.env.NODE_ENV !== 'production' ? "style-loader":
	            MiniCssExtractPlugin.loader,
	            "css-loader",
	            "sass-loader"
	          ]
	      }
	    ]
	  },
	  plugins:[
	    new HtmlWebpackPlugin({
	      template:"./src/index.html",
	      filename:"./index.html"
	    }),
	    new MiniCssExtractPlugin({
	      filename:"[name].css",
	      chunkFilename:"[id].css"
	    })
	  ],
	  optimization:{
	    runtimeChunk:{
	      name:"manifest"
	    },
	    splitChunks:{
	      // chunks:"async",
	      // minSize:300000,
	      // minChunks:1,
	      // maxAsyncRequests:5,
	      // maxInitialRequests:3,
	      // name:"vendors",
	      cacheGroups:{
	        vendor:{
	          test:/[\\/]node_modules[\\/]/,
	          name:"vendors",
	          priority:-20,
	          chunks:"all"
	        }
	      }
	    }
	  }
	}

# 移动端 #
## 屏幕宽度 ##
- iPhone6 Plus 398
- Huawei P9 344

# Node #

#### 更新node版本： ####
先清除npm缓存：npm cache clean -f

然后安装n模块：npm install -g n

升级node.js到最新稳定版：n stable

如果是mac 升级出现错误 在命令前面家sudo

# Ajax #
## 四种常见的 POST 提交数据方式 ##
协议规定 POST 提交的数据必须放在消息主体（entity-body）中，但协议并没有规定数据必须使用什么编码方式

服务端通常是根据请求头（headers）中的 Content-Type 字段来获知请求中的消息主体是用何种方式编码

### application/x-www-form-urlencoded ###
如果不设置 enctype 属性，默认

> POST http://www.example.com HTTP/1.1
> Content-Type: application/x-www-form-urlencoded;charset=utf-8
> 
> title=test&sub%5B%5D=1&sub%5B%5D=2&sub%5B%5D=3

### multipart/form-data ###
必须让 <form> 表单的 enctype 等于 multipart/form-data

> POST http://www.example.com HTTP/1.1
> Content-Type:multipart/form-data; boundary=----WebKitFormBoundaryrGKCBY7qhFd3Tr
> wA
> 
> ------WebKitFormBoundaryrGKCBY7qhFd3TrwA
> Content-Disposition: form-data; name="text"
> 
> title
> ------WebKitFormBoundaryrGKCBY7qhFd3TrwA
> Content-Disposition: form-data; name="file"; filename="chrome.png"
> Content-Type: image/png
> 
> PNG ... content of chrome.png ...
> ------WebKitFormBoundaryrGKCBY7qhFd3TrwA--

上面提到的这两种 POST 数据的方式，都是浏览器原生支持的，而且现阶段标准中原生 <form> 表单也只支持这两种方式

### application/json ###
告诉服务端消息主体是序列化后的 JSON 字符串

> var data = {'title':'test', 'sub' : [1,2,3]};
> $http.post(url, data).success(function(result) {
>     ...
> });

最终发送的请求是：

> POST http://www.example.com HTTP/1.1 
> Content-Type: application/json;charset=utf-8
> 
> {"title":"test","sub":[1,2,3]}

### text/xml ###
POST http://www.example.com HTTP/1.1 
Content-Type: text/xml

	<?xml version="1.0"?>
	<methodCall>
	    <methodName>examples.getStateName</methodName>
	    <params>
	        <param>
	            <value><i4>41</i4></value>
	        </param>
	    </params>
	</methodCall>
# Vue #
## Props ##
不要试图在组件内修改Props，在组件内Props是不可更改的，从父组件传递修改子组件props的函数，在子组件内调用函数可以修改父组件属性，从而更新子组件。

## 组件继承 ##
### extend ### 
> 可以扩展 Vue 构造器，从而用预定义选项创建可复用的组件构造器

	let BaseComponent = Vue.extend(baseOptions);
	//基于基础组件BaseComponent,再扩展新逻辑.
	new BaseComponent({
	    created(){
	        //do something
	        console.log('onCreated-2');
	    }
	    //其他自定义逻辑
	});

### mixins ###
> mixins 选项接受一个混合对象的数组。这些混合实例对象可以像正常的实例对象一样包含选项,他们将在 Vue.extend() 里最终选择使用相同的选项合并逻辑合并。

	new Vue({
	    mixins: [baseOptions],
	    created(){
	        //do something
	        console.log('onCreated-2');
	    }
	    //其他自定义逻辑
	});

### extends ###

> 这和 mixins 类似，区别在于，组件自身的选项会比要扩展的源组件具有更高的优先级.

new Vue({
    extends: baseOptions,
    created(){
        //do something
        console.log('onCreated-2');
    }
    //其他自定义逻辑
});

> 从结果上看,三种方式都能实现需求,但是形式却有不同.
> 
> Vue.extend
> 
> Vue.extend只是创建一个构造器,他是为了创建可复用的组件.
> 
> mixins,extends
> 
> 而mixins和extends是为了拓展组件.

## Vuex ## 

### 简单store ###
	var store = {
	  debug: true,
	  state: {
	    message: 'Hello!'
	  },
	  setMessageAction (newValue) {
	    if (this.debug) console.log('setMessageAction triggered with', newValue)
	    this.state.message = newValue
	  },
	  clearMessageAction () {
	    if (this.debug) console.log('clearMessageAction triggered')
	    this.state.message = ''
	  }
	}
	var vmA = new Vue({
	  data: {
	    privateState: {},
	    sharedState: store.state
	  }
	})
	
	var vmB = new Vue({
	  data: {
	    privateState: {},
	    sharedState: store.state
	  }
	})

## 状态管理 ##
如何决定使用哪种管理方法？以下原则可以帮助您决定：

- 如果状态是用户数据，如复选框的选中状态、滑块的位置，则该状态最好由父widget管理
- 如果所讨论的状态是有关界面外观效果的，例如动画，那么状态最好由widget本身来管理.
# 浏览器兼容性 #
querySelectorAll()的结果是NodeList，在IE上不支持forEach

classlist在IE上不支持

：visited在 IE Firefox上不支持

img{ content:url}在 IE Firefox上不支持

没有src的img在Firefox Chrome上会显示边框，建议改用 div{display:inline-block;}

# JS混淆 #
	false       =>  ![]
	true        =>  !![]
	undefined   =>  [][[]]
	NaN         =>  +[![]]
	0           =>  +[]
	1           =>  +!+[]
	2           =>  !+[]+!+[]
	10          =>  [+!+[]]+[+[]]
	Array       =>  []
	Number      =>  +[]
	String      =>  []+[]
	Boolean     =>  ![]
	Function    =>  []["filter"]
	eval        =>  []["filter"]["constructor"]( CODE )()
	window      =>  []["filter"]["constructor"]("return this")()

# Q&A #
#### 百度地图自定义图层的点击 ####
多个自定义图层的父元素的z-index不同，会影响加入的元素的触发

#### 百度地图检测地图的点击 ####
判断点击了地图，但没有点击其他覆盖物，检测地图点击事件的overlay，没有覆盖物时e.overlay为null，自定义覆盖也为null，需要在覆盖物内阻止事件的传递

点击自定义覆盖物后，marker点击失效

	SquareOverlay.prototype.addEventListener = function(event,fun){
	    this._div['on'+event] = fun;
	}


#### 提交文件到服务器没反应 ####
musicBook 写成了musicbook Springboot没有解析该字段

#### div默认高度和宽度 ####
浏览器一般默认解释为内容的高度，而不是100%。但是只要为html和body设置高度为100%就可以了：html,body{height:100%;}

Html级元素默认宽度是100%，即整行；但是高度并不是100%，而仅仅是一行而已。

### 水平垂直居中 ###
[https://div.io/topic/1155](https://div.io/topic/1155)

	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);

**text-align属性对应用了position:absloute/fixed声明的元素无效！**

在FireFox浏览器，以及貌似IE6/IE7下，标签内似乎有不占据任何空间的匿名文字节点元素，于是，图片就跟在这个“隐形”的节点元素之后显示，而这个节点因为由于受text-align:center;声明影响居中显示了，于是，紧跟其后的inline水平的图片元素就对着中垂线显示了！

而其他浏览器，如Chrome浏览器，标签内貌似很干净，因为应用了absolute属性值的图片前面没有可以依靠的元素，因此，左对齐显示了。

**text-align属性虽然可以实现absolute元素的水平居中对齐，但是，并不是直接对absolute元素起的作用，而是absolute元素之前的inline/inline-block水平的元素**

absolute是相对于第一个非static元素

#### element-ui el-menu 右侧有1px的border ####
&lt;ul role="menubar" class="el-menu" style="border-right-width:0px"&gt;

#### 文字不被识别为 :last-child ####
给文字加上&lt;span&gt;标签
#### 父元素设置min-height子元素高度设置100%取不到值 ####
解决方法：给父元素加绝对定位，然后子元素加相对定位，这样子元素的高度就会根据父元素的高度进行计算

- 第一种是父级有显性高度，即写了具体值
- 第二种就是自身绝对定位，父级相对定位脱离文档流

子元素绝对定位是计算父元素的 padding 值的


#### 如果CSS支持了父选择器，会有什么后果？ ####
后果之一，就是原先的一次渲染被破坏，子元素能够影响父元素的渲染，于是乎，“死循环”开始了，页面渲染会出现各种各样的死循环，现有的很多CSS规则会被颠覆，无限宽度反复渲染等问题就会出现。这就是为什么父选择器呼声那么高，却迟迟不支持的原因。
#### padding设置50%造成的padding循环错误 ####
CSS中没有死循环的说法
#### CSS中height:100%和height:inherit的异同 ####
在absolute定位中，inherit相对DOM中的父元素，否则是定位了的父元素

#### Error in v-on handler: "TypeError: handler.apply is not a function" ####
method被赋值了，可能原因 Vue中的method和data重名了，导致给data赋值的时候将方法覆盖了


#### > TypeError: cannot read property 'dataXXXXX' of null · Issue  -Browser-sync ####

Browsersync works by injecting an asynchronous script tag right after the body tag during initial request. In order for this to work properly the body tag must be present. Alternatively you can provide a custom rule for the snippet using snippetOptions

**使用了load载入新的页面，新的页面中存在body标签，browser-sync向body中注入异步标签，此时页面存在两个body标签** 

#### flex布局下的与span高度为父元素的高度 ####
flex的align-content默认为stretch，故撑开
#### Error: Couldn't find preset "es2015" relative to directory "/root" ####

安装preset 不要加-g

npm install --save-dev babel-preset-es2015

.babelrc配置 

	{
	    "presets": [
	      "es2015"
	    ],
	    "plugins": []
	}
#### iOS中Date.parse返回NaN ####
iOS只识别2017/03/05 不识别 2017-03-05

####Weui pullToRefresh刷新不出来 ####
 在手机上刷新即可

#### 子元素使用css float 导致父元素没有高度 ####
- 对父级设置固定高度 
- 对父级div标签闭合div前加一个clear清除浮动对象
- 只需要对父级加一个overflow:hidden样式即可

#### jquery 点击在safari上失效 ####
[jquery中on绑定click事件在苹果手机失效的问题](https://blog.csdn.net/yuexiage1/article/details/51612496)

因为是动态添加的内容，所以想要使用click事件，需要给他用on绑定一下：

	$(document).on("click",".next_button",function(){
	    alert();
	});

苹果有这么个设置： 
对于点击的对象，拥有cursor:pointer这个样式的设置，也就是说，鼠标放上去，能够出现“手”型的图标才被认作可以使用点击事件，增加样式

	<style>
	    .next_button{
	        cursor:pointer
	    }
	</style>

#### ios上input disabled 不显示文字 ####
	input:disabled, textarea:diabled {
	    -webkit-text-fill-color: #000;
	    -webkit-opacity: 1;
	    color: #000;
	}

#### jquery发送application/json格式数据 ####
jquery将请求分为两次，第一为options(不会带上cookie)，第二次才是真实的post

springboot设置 spring.mvc.dispatch-options-request=true

#### 父元素 width：100vh, flex布局，justify-content:center,子元素不居中 ####
改成width:100%; 可能是因为子元素的位置计算使用了vh；

#### iOS HTML5 无法定位 ####
IOS系统在10以上版本考虑到安全问题禁止用户在http协议下定位，要想定位必须将http协议升级成https协议

微信公众号的浏览器版本等同于android的chrome浏览器,高德定位为http

微信企业号的浏览器版本等同于ios的safari浏览器, 高德定位为https


ios 系统访问定位 需要满足以下两点：

1、需要使用JS API的高精度定位功能，在iOS 11上，请通过https访问定位。

2、您的个人（或企业）网站在iOS 11系统下也请您调整成https形式对终端用户提供。

#### line-height,em,font-size的关系 ####
#### 页面刷新或离开时的提示框 ####
	window.onbeforeunload = function() {
		// 兼容IE8和Firefox 4之前的版本
		  if (e) {
		    e.returnValue = '关闭提示';
		  }
	
		return thewordtoshow；
	}

#### 网页路径问题 ####
被多个页面使用的模块，不要使用相对路径 "../../xxx/xxx"，

使用相对于主机名的路径 "/xxx/xxx"

#### JS自动插入分号 ####
- empty statement
- var statement
- expression statement
- do-while statement
- continue statement
- break statement
- return statement
- throw statement

Three cases are described:

When a token (LineTerminator or }) is encountered that is not allowed by the grammar, a semicolon is inserted before it if:

1. The token is separated from the previous token by at least one LineTerminator.
The token is }
e.g.:

	{ 1
	2 } 3
is transformed to

	{ 1
	;2 ;} 3;
The NumericLiteral 1 meets the first condition, the following token is a line terminator.
The 2 meets the second condition, the following token is }.

2. When the end of the input stream of tokens is encountered and the parser is unable to parse the input token stream as a single complete Program, then a semicolon is automatically inserted at the end of the input stream.

	e.g.:
	
		a = b
		++c
	is transformed to:
	
		a = b;
		++c;
3. This case occurs when a token is allowed by some production of the grammar, but the production is a restricted production, a semicolon is automatically inserted before the restricted token.


	UpdateExpression :
	    LeftHandSideExpression [no LineTerminator here] ++
	    LeftHandSideExpression [no LineTerminator here] --
	
	ContinueStatement :
	    continue ;
	    continue [no LineTerminator here] LabelIdentifier ;
	
	BreakStatement :
	    break ;
	    break [no LineTerminator here] LabelIdentifier ;
	
	ReturnStatement :
	    return ;
	    return [no LineTerminator here] Expression ;
	
	ThrowStatement :
	    throw [no LineTerminator here] Expression ; 
	
	ArrowFunction :
	    ArrowParameters [no LineTerminator here] => ConciseBody
	
	YieldExpression :
	    yield [no LineTerminator here] * AssignmentExpression
	    yield [no LineTerminator here] AssignmentExpression

7.9.1 Rules of Automatic Semicolon Insertion
There are three basic rules of semicolon insertion:

- When, as the program is parsed from left to right, a token (called the offending token) is encountered that is not allowed by any production of the grammar, then a semicolon is automatically inserted before the offending token if one or more of the following conditions is true:
- The offending token is separated from the previous token by at least one LineTerminator.
- The offending token is }.
- When, as the program is parsed from left to right, the end of the input stream of tokens is encountered and the parser is unable to parse the input token stream as a single complete ECMAScript Program, then a semicolon is automatically inserted at the end of the input stream.
- When, as the program is parsed from left to right, a token is encountered that is allowed by some production of the grammar, but the production is a restricted production and the token would be the first token for a terminal or nonterminal immediately following the annotation "[no LineTerminator here]" within the restricted production (and therefore such a token is called a restricted token), and the restricted token is separated from the previous token by at least one LineTerminator, then a semicolon is automatically inserted before the restricted token.

However, there is an additional overriding condition on the preceding rules: a semicolon is never inserted automatically if the semicolon would then be parsed as an empty statement or if that semicolon would become one of the two semicolons in the header of a for statement (see 12.6.3).

#### jquery点击事件无效 ####
jquery点击事件无效，父元素的mousedown事件一直有效，跟随鼠标移动的图片的大小只能保持固定值才有效；

鼠标点一直位于图片上，所有的点击事件效果都发生在绝对定位的图标上，不会再传给子元素。

#### Vue的组件的$attrs上多了一个同名的label属性 ####
label拼写成了lable，因此不属于组件固有的props，会被添加到$attrs上
## 视频宽度 ##
视频宽度引起的问题总结

#### 设定视频父容器高度为固定高度； ####
video元素会因为内容的高度来决定video元素本身的宽高

#### 设置兄弟元素的flex-shrink：0； ####
video元素的伸展会导致兄弟元素的收缩，在flex布局中设置flex-shrink：0，保持非视频部分不收缩；但是设置的部分必须在flex中的顺序靠前，否则位于flex-grow：1的元素之后将没有效果；

#### 设定video的高度 width：100%；height：100%； ####
在父容器的高度固定的情况下，设置100%是安全的，使得视频可以充满整个容器

#### 设置object-fit：contain； ####
考虑视频实际大小小于容器的情况；视频较小时，scale-down使得视频保持原有大小，而无法充满整个容器；fill无法保持视频长宽比；cover使得视频不能完整实现；

#### 内容超出预定宽度，产生换行问题 ####
可以设置长度，并设置css
	white-space:nowrap;
	overflow:hidden;
	text-overflow:ellipsis;

在flex中，摆在首位的元素默认不换行，可以直接设置
	overflow:hidden;
	text-overflow:ellipsis;