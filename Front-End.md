# HTML #
## DOM ##
### Node ###
#### nodeType ####

document Node.DOCUMENT_NODE(9)

div Node.ELEMENT_NODE(1)

nodeName是标签名 "DIV", nodeValue始终为null

childNodes  NodeList类数组对象

ownerDocument === document

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

- document.anchors 带name特性的< a>元素
- document.forms 文档中所有的<form/>元素
- document.images 文档中所有的<image>元素
- document.links 文档中所有带href的< a>元素

*getElementsByName* 仅HTMLDocument支持


## 标签 ##
标签的<之后不能跟空格

### body ###
浏览器在遇到body标签才开始呈现内容，head中的js加载会延迟页面加载 
## 页面加载 ##


### href使用 ###
[a标签中href=""的几种用法](http://blog.csdn.net/u010297791/article/details/52784879)


# JavaScript #
## 严格模式 ##
ES6的模块自动采用严格模式

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

### String ###
#### Unicode表示法 ####
- \uxxxx
- \u{xxxx} 超过\uffff
- '\u{1F680}' === '\uD82D\uDE80' 大括号表示法与UTF-16编码等价

**codePointAt()**

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

#### for循环特别之处，设置变量的那部分是一个父作用域，而循环体内是个单独的子作用域 ####

### 变量提升 ###
var命令会发生**变量提升**现象，在声明之前使用值为undfined，

let命令变量一定要在声明之后使用，否则报错

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



#### 执行js文件没有输出，可能是函数定义未被执行 ####

## 作用域 ##
ES5 规定，函数只能在顶层作用域和函数作用域之中声明，不能在块级作用域声明。

ES6 引入了块级作用域，明确允许在块级作用域之中声明函数。ES6 规定，块级作用域之中，函数声明语句的行为类似于let，在块级作用域之外不可引用。

在浏览器的 ES6 环境中，块级作用域内声明的函数，行为类似于var声明的变量。

ES6 的块级作用域允许声明函数的规则，只在使用大括号的情况下成立，如果没有使用大括号，就会报错。

- 允许在块级作用域内声明函数
- 函数声明相当于var会提升到全局作用域或函数作用域的头部
- 函数声明还会提升到所在块级作用域的头部

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
## 顶层对象 ##
**顶层对象的属性与全局变量挂钩，被认为是 JavaScript 语言最大的设计败笔之一**



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


## CPS（Continuation Programming Style） ##
> 我们在学习函数时只了解了一半事实，因为我们基于一个错误的假定：函数只能将结果返回到它的调用端

## Async函数 ##
> async函数的返回值包装成Promise
> 
> await 的返回值是Promise的对象，resolve(data)的data

## Chrome调试 ##

> Snippets：Identifier 'obj' has already been declared

每次当前页面js运行都在给页面的js环境中，故每次定义变量，都会添加到全局js环境中，刷新页面即可

# CSS #
## 前缀 ##
> - -webkit-*   safari 
> - -o-*        Opera
> - -moz-*      Firefox

## float ##
> float元素脱离里定位的标准流，随后的标准流元素将忽略该元素，按前一个标准流元素的位置定位。

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
### absolute ###> 
> 脱离文档流，将与其他文档处于不同的层级
> 若父元素不是relative定位，将相对于整个页面定位，默认位置是其在文档流中的定位
> 若父元素是relative定位，将相对于父元素定位

### relative ###
相对于原来在文档中的位置布局

### fix ###
固定在页面中的位置

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
- stretch:如果侧轴大小为auto，使盒边距接近行的尺寸，但遵照'min/max-width/height'限制，相当于完成撑开

#### align-self ####
定义flex子项单独的对齐方式，会重写align-items的参数

### 水平居中方案 ###
 在div中设置,可使在主轴上居

> display:flex;
> 
> justify-content:center;


## 屏幕大小响应 ##

@media all and(min-width:768px) 适应768px以上的屏幕


## 字体粗细 ##
font-weight:设置粗细，但也与字体有关

font-family:设置字体

## 边框属性 ##
border-image: 设置边框图像 url() stretch/round

border-shadow:边框阴影

border-radius:边框圆角

## 背景 ##
> background:url()
> 
> background-position:[left top];
> 
> background-repeat:repeat no-repeat;
> 
> background-size: 背景大小
> 
> background-origin:[content-box|padding-box|border-box]
> background-clip:背景的绘制区域（应该是针对图片内的坐标）

## 渐变 ##

- radial-gradient:径向渐变
- linear-gradient:线性渐变

## 文本效果 ##
- text-shadow:文字阴影
- box-shadow:盒子阴影，适用于div
- text-overflow:ellipsis(...) clip(裁剪字)
- word-wrap:break-work 换行,分裂一个字
- word-break: keep-all 保持单词不拆分， break-all 拆分单词



> **阴影第三个参数 模糊度，越大越模糊**
> 
> box-shadow: h-shadow v-shadow blur spread color inset;


## 伪元素 ##
- :first-letter:
- :first-line
- :before  向元素之前添加内容
- :after 在该元素之后添加内容

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

### 层叠 ###

- 1)若两条规则具有相同的权值、起源及特殊性，那在样式表中最后出现的规则优先。
- 2)任何位于文档中的规则都比引入的规则优先。

## Margin和Padding ##

> 边界，元素周围生成额外的空白区。“空白区”通常是指其他元素不能出现且父元素背景可见的区域。——CSS权威指南

### Collapsing Margin ###
相邻或嵌套的盒元素Margin会重合为单一个Margin

(相邻元素的Margin-top相同，比较合理)

> In this specification, the expression collapsing margins means that adjoining margins (no non-empty content, padding or border areas or clearance separate them) of two or more boxes (which may be next to one another or nested) combine to form a single margin. 

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
### div ###
div默认占满一行

### 总结 ###

> 因为BFC内部的元素和外部的元素绝对不会互相影响，因此， 当BFC外部存在浮动时，它不应该影响BFC内部Box的布局，BFC会通过变窄，而不与浮动有重叠。同样的，当BFC内部有浮动时，为了不影响外部元素的布局，BFC计算高度时会包括浮动的高度。避免margin重叠也是这样的一个道理。

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