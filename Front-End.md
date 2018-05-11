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

## viewport ##
<meta name=”viewport” content=”width=device-width, initial-scale=1, maximum-scale=1″>

- width：控制 viewport 的大小，可以指定的一个值，如果 600，或者特殊的值，如 device-width 为设备的宽度（单位为缩放为 100% 时的 CSS 的像素）。
- height：和 width 相对应，指定高度。
- initial-scale：初始缩放比例，也即是当页面第一次 load 的时候缩放比例。
- maximum-scale：允许用户缩放到的最大比例。
- minimum-scale：允许用户缩放到的最小比例。
- user-scalable：用户是否可以手动缩放
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

- 3 + {valueOf(){return 3}  = 6
- 3 + {toString(){return "3"}} = "33"
- 3 + {toString(){return 3}} = 6
- 3 + {value:3} = 3[object Object]

 -Infinity + Infinity = NaN

undefined  和 null 调用 toString()转换为 "undefined" 和 "null"

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

## 函数 ##

### apply, call, bind ###

apply: 第一个参数为环境对象，第二个参数是参数数组（Array 或 arguments对象）

call: 第一个参数为环境对象，后面的是直接传递参数

bind: 返回绑定环境对象的函数实例

apply和call实际执行了函数，bind只是反回了一个新函数实例

return 返回 undefined；

arguments与对应的命名参数值同步，它们并不是同一内存空间，但是值会同步

arguments的length是由传入的参数个数决定，并非定义的参数个数

**ECMAScript中所有的参数都是值传递，不可能通过引用传递参数**

没有重载，参数是有包含0或多个值的数组表示

### 箭头函数 ###> 
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



# CSS #

## width ##
100% 只与父元素有关，与屏幕无关

position:absolute 的元素的 width x%，与父元素的父元素有关，脱离了父元素的计算，使用relative可以限制absolute是的width的比例相对于父元素

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

div有非static的postion属性，默认宽度为0，div里面有内容，会被内容撑起

同上，div有float:left, float:right等样式，div默认宽度为0，会被内部内容撑起
### absolute ###
> 脱离文档流，将与其他文档处于不同的层级
> 若父元素不是relative定位，将相对于整个页面定位，默认位置是其在文档流中的定位
> 若父元素是relative定位，将相对于父元素定位

*本质，非static父元素*
> 当给绝对定位元素设置定位值时，该元素会延着DOM树向上查找，直到找到一个具有定位属性的祖先元素，则定位相对于该元素，在该例子中，由于其祖先元素都没有定位属性，则该绝对定位元素会相对于body体进行定位；如果给其父元素加上一个定位属性，则该绝对定位元素会相对于这个父元素；

#### 自动伸缩 ####
当width为auto时，根据left，right自动伸缩，相对于第一个有定位属性的祖先

> 如果 width 值为 auto 此时如果我们设置 left 和 right 都为0，则该元素会填充满其相对的元素，如果此时我们将宽度设置为固定值，这是绝对定位元素会优先取 left 值作为定位标志

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

### 边框重叠 ###
使用负margin，margin-right:-1px;

## SASS ##
SASS是一种CSS的开发工具，提供了许多便利的写法，大大节省了设计者的时间，使得CSS的开发，变得简单和可维护。

    sass --style compressed test.sass test.css

基本语法

    $side : left;
    .rounded {
	    border-#{$side}-radius: 5px;
    }

允许定义变量， 计算， 可嵌套

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


# Q&A #
> TypeError: cannot read property 'dataXXXXX' of null · Issue  -- Browser-sync

Browsersync works by injecting an asynchronous script tag right after the body tag during initial request. In order for this to work properly the body tag must be present. Alternatively you can provide a custom rule for the snippet using snippetOptions

**使用了load载入新的页面，新的页面中存在body标签，browser-sync向body中注入异步标签，此时页面存在两个body标签**  
