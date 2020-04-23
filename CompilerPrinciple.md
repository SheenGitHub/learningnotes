# 介绍 #
编译器就是一个程序，它可以阅读已某一种语言(源语言)编写的程序，并把该程序翻译成为一个等价的、用另一种语言（目标语言）编写的程序。

解释器是另一种语言处理器。它并不通过翻译的方式生成目标程序。从用户的角度看，解释器直接利用用户提供的输入执行源程序中指定的操作。

## 编译器的结构 ##
编译器把源程序映射为语义上等价的目标代码，映射过程分成两部分：分析部分(前端)和综合部分(后端)

分析：把源程序分解成多个组成要素，并在这些要素上加上语法结构。符号表和中间表示形式一起传送给综合部分。

综合：根据中间表示和符号表的信息来构造用户期待的目标程序。

![4216.png](http://ww1.sinaimg.cn/large/48ceb85dly1gdry4av2cjj20eu0evjt6.jpg)

### 词法分析 ###
lexical analysis/scannning

读入组成源程序的字符流，将它们组织成有意义的词素(lexeme)的序列，对于每个词素，词法分析器产生一个词法单元<token-name, attribute-value>

### 语法分析 ###
syntax analysis/parsing

语法分析器使用词法分析器生成的各个词法单元的第一个分量来创建树形的中间表示（syntax tree）
### 语义分析 ###
semantic analysis

语义分析器使用语法树和符号表中的信息来检查源程序是否和语言定义的语法一直，同时也手机类型信息，进行类型检查(type checking)和自动类型转换(coercion)

### 中间代码生成 ###
语法树是一种中间表示形式

### 代码优化 ###

### 代码生成 ###
代码生成一个至关重要的方面是合理分配寄存器以存放变量的值

#### 计算机体系结构的优化 ####
几乎所有的高性能系统都利用了两种技术:并行(parallelism)和内存层次结构(memory hierarchy)。

#### 环境与状态 ####
1. 环境是一个从名字到存储位置的映射。因为变量就是指内存环境。
1. 状态时从一个内存位置到它们值的映射

**环境与状态的映射**

1. 名字到位置的静态绑定与动态绑定:大部分是动态，全局变量是静态确定
1. 从位置到值的静态绑定与动态绑定：大部分是动态，静态常量和宏定义是静态

#### 静态作用域和动态作用域类比 ####
静态规则让我们寻找的声明位于最内层、包含变量使用位置的单元(块)中；而动态规则则让我们寻找的声明位于最内层、包含了变量使用时间的单元(过程调用)

# 语法制导翻译 #
syntaxt-directed translation

分析阶段的工作是围绕编译语言的“语法”展开的。一个程序设计语言的语法(syntax)描述了该语言的程序的正确行使，而该语言的语义(sematic)则定义了程序的含义，即每个程序在运行时做什么事情。

#### 一个编译器前端的模型 ####

![3247.png](http://ww1.sinaimg.cn/large/48ceb85dly1gdt1mhlgyaj20e303ct90.jpg)

## 语法定义 ##
**上下文无关文法**

文法自然地描述了大多数程序设计语言构造的层次化语法结构，形如Java的if-else

**if** (expression) statement **else** statement

产生式(production) stmt-> **if** (expr) stmt **else** stmt

**if**和括号这样的词法单元称为终结符(terminal),像expr和stmt这样的变量表示终结符号的序列，称为非终结符(nonterminal)

### 文法定义 ###
上下文无关文法(context-free grammar)由四个元素组成：

1. 一个终结符号集合，有时也称为“词法单元”。终结符号是该文法所定义的语言的基本符号的集合。
1. 一个非终结符号集合，有时也称为“语法变量”。每个非终结符号表示一个终结符号串的集合。
1. 一个产生式，其中每个产生式包括一个产生式头或左部的非终结符号，一个箭头，和一个称为产生式体或右部的由终结符号及非终结符号组成的序列。
1. 指定一个非终结符号为开始符号

> 在编译器中，词法分析器读入源程序的字符序列，将它们组织成为具有词法含义的词素，生成并输出代表这些词素的词法单元序列。词法单元由两个部分组成：名字和属性值。词法单元的名字是语法分析器进行语法分析时使用的抽象符号。词法单元名字通常称为终结符号。如果词法单元具有属性值，那么这个值就是一个指向符号表的指针，符号表中包含了该词法单元的附加信息。

#### 例子文法 ####

*list*->*list* + *digit*

*list*->*list* - *digit*

*list*->*digit*

*digit*->0|1|2|3|4|5|6|7|8|9|

以非终结符list为头部的三个产生式可以等价组合为：

*list*->*list* + *list* | *list* - *digit* | *digit*

该文法的终结符号包括如下字符：+ - 0 1 2 3 4 5 6 7 8 9 0  

该文法的终结符号是*list* 和 *digit*，因为list的产生式首先被列出，所以*list*是此文法的开始符号

如果某个非终结符号是某个产生式的头部，称该产生式是该终结符号的产生式。一个终结符号串是由0个或多个终结符号组成的序列。零个终结符号组成的串成为空串。

可以从**开始符号推导得到的所有终结符号串的集合**称为该文法定义的语言(language)

函数的调用列表
	
	     call -> id ( optparams )
	optparams -> params | ε
	params    -> params, param | param

> **语法分析的任务是：接收一个终结符号作为输入，找出从文法的开始符号推导出这个串的方法。如果不能从文法的开始符号推导得到该终结符号串，则报告该中介符号串中包含的语法错误。**

### 语法分析树 ###
词法分析树用图形方式展示了从文法的开始符号推导出相应语言中的符号串的过程。如果非终结符号A有一个产生式A->XYZ，那么在语法分析树中就可能有一个标号为A的内部结点，该结点有三个子节点，从左向右的标号分别为X,Y,Z:

	   A
	 / | \
	X  Y  Z

给定一个上下文无关文法，该文法的语法分析树(parse tree)是具有以下性质的树：

1. 根节点的标号为文法的开始符号。
1. 每个叶子结点的标号为一个终结符号或ε。
1. 每个内部结点的标号为一个非终结符号。
1. 如果非终结符号A是某个内部结点的标号，并且它的子结点的标号从左至右分别为X<sub>1</sub>,X<sub>2</sub>,X<sub>3</sub>,...,X<sub>n</sub>,那么必然存在一个产生式A-> X<sub>1</sub>X<sub>2</sub>X<sub>3</sub>...X<sub>n</sub>,其中X<sub>1</sub>,X<sub>2</sub>,X<sub>3</sub>,...,X<sub>n</sub>既可以是终结符号，也可以是非终结符号。作为一个特殊情况，如果A->ε是一个产生式，那么一个标号为A的结点可以只有一个标号为ε的子结点

一个文法的语言的另一个定义是指任何能够由某课语法分析树生成的符号串的集合。为一个给定的终结符号串创建一课语法分析树的称为对该符号串进行**语法分析**
。

### 二义性 ###
ambiguous

一个文法可能有多棵语法分析树能够生成同一个给定的终结符号串。这样的文法具有二义性。要证明文法具有二义性，我们只需找到一个终结符号串，说明它是两棵以上语法分析树的结果。

### 运算符的结合性 ###
运算符'+'是左结合的，表示运算分量两侧都有'+'时，它属于其左边的运算符;运算符'='及其后裔'+=','-='是右结合的 a=b=c;

带有右结合运算符的串，比如a=b=c可以由以下文法产生

	right -> letter=right|letter
	letter -> a|b|...|z

### 运算符的优先级 ###
运算符结合性规则只能用于同一运算符的多次出现

算数表示的文法可根据表示运算符结合性和优先级的表格来构建。

优先级表：

- 左结合:+ -
- 左结合:* /

穿件两个非终结符expr和term，分别对应这两个优先级层次，并使用非终结符factor来生成表达式中的基本单元。表达式的基本单元是数位和带括号的表达式。

	factor -> digit|(expr)
	item -> item * factor | term / factor | term
	expr -> expr + term | expr - term| term

最终文法是 

	expr -> expr + term | expr - term | term
	term -> term * factor | term / factor | factor
	factor -> digit|(expr)

对于每个优先级都有一个终结符，表示能被该优先级或更高级的运算符分开的表达式。

## 语法制导翻译 ##
语法制导翻译是通过向一个文法产生式附加一些规则和程序片段而得到的。

*属性*

属性表示与某个程序构造相关的任意的量

*翻译方案*(translation scheme)

翻译方案是一种将程序片段附加到一个文法的各个产生式上的表示法。

### 后缀表达 ###
一个表达式E的后缀表示(postfix notation)可按照下面的方法进行归纳定义：

1. 如果E是一个变量或常量，则E的后缀表示是E本身。
1. 如果E是一个形如E1 **op** E2 的表达式，其中op是一个二元目运算符，那么E的后缀表示是E1'E2'**op**,这里E1',E2'是E1和E2的后缀表示。
1. 如果E是一个形如(E1)的被括号括起来的表达式则E的后缀表示就是E1的后缀表示

### 综合属性 ###
如果某个属性在语法分析树结点N上的值是由N的子结点以及N本身的属性值确定的，那么这个属性成为综合属性(synthesized attribute)。综合属性只要遍历一次就可以计算出属性值,相对应的叫"继承"属性，需要由结点本身、父结点以及兄弟结点上的属性值决定。

### 简单语法制导定义 ###
要得到代表产生式头部的非终结符号的翻译结果的字符串，只需要将产生式体中各非终结符号的翻译结果按照它们在非终结符号中的出现顺序连接起来，并在其中穿插一些附加的串即可。具有这个性质的语法制导定义成为**简单语法制导定义**。

产生式 expr->expr1 + term

语法规则 expr.t = expr1.t || term.t || '+'

语法制导翻译方案是一种在文法产生式中附加一些程序片段来描述翻译结果的表示方法。

被嵌入产生式的程序片段成为语义动作(semantic action)

## 语法分析 ##
词法分析是决定如何使用一个文法生成一个终结符号串的过程。

对于任何上下文无关文法，我们都可以构造出一个时间复杂度为O(n<sup>3</sup>)的语法分析器，它最多使用O(n<sup>3</sup>)的时间来完成一个长度为n的符号串的语法分析。

大多数语法分析方法都可以归为两类：自顶向下和自底向上方法。在自顶向下语法分析器中，构造过程从根结点开始，逐步向叶子结点方向进行；而自底向上语法分析器中，构造过程从叶子结点开始，逐步构造出根节点。自顶向下语法分析器可以较容易地手工构造出高效的语法分析器。自底向上分析方法可以处理更多种文法和翻译方案，所以直接从文法生成语法分析器的软件常常使用自底向上的方法。

### 自顶向下分析方法 ###
在自顶向下地构造一颗语法分析树时，从标号为开始非终结符stmt的根结点开始，反复执行下面两个步骤：

1. 在标号为非终结符号A的结点N上，选择A的一个产生式，并为该产生式体中的各个符号构造出N的子结点。
1. 寻找下一个结点来构造子树，通常选择语法分析树最左边的尚未扩展的非终结符

输入中当前被扫描的终结符号通常称为向前看(lookahead)符号。

一般来说，为一个非终结符选择一个产生式是一个“尝试并犯错”的过程。也就是说，我们首先选择一个产生式，并在这个产生式不适合时进行回溯，再尝试另一个产生式。一个产生式“不适合”是指使用了该产生式后，我们无法构造得到一颗与当前输入串匹配的语法分析树。但是在称为预测语法分析的情况下不需要进行回溯。

### 预测分析法 ###
递归下降分析方法(recursive-descent parsing)是一种自顶向下的语法分析方法，它使用一组递归来处理输入。文法的每个非终结符都有一个相关联的过程，**预测分析法**(predictive parsing)是一种其中的简单形式。

**预测分析法**需要知道哪些符号可能成为一个产生式体所生成串的第一个符号。令α是一个文法符号串(终结符或非终结符)串，我们将FIRST(α)定义为可以由α生成的一个或多个终结符号串的第一个符号的集合。如果a就是ε或者可以生成ε，那么ε也在FIRST(α)中。

如果有两个产生式A->α和A->β, 我们就必须考虑相应的FIRST集合,如果我们不考虑ε产生式，**预测分析法**要求FIRST(α)和FIRST(β)不想交，那么就可以用向前看符号来确定改使用哪个产生式。

*预测分析器的伪代码*
	
	void stmt(){
		switch(lookhead){
			case expr:
				match(expr);match(';');break;
			case if:
				match(if);match('(');match(expr);match(')');stmt();
				break;
			case for:
				match(for);match('(');
				optexpr();match(';');optexpr();match(';');optexpr();
				match(')');stmt();break;
			case other:
				match(other);break;
			default:
				report('syntax error');
		}
	}
	
	void optexpr(){
		if( lookhead == expr ) match(expr);
	}
	
	void match(){
		if( lookhead == t ) lookhead = nextTerminal;
		else report('syntax error');
	}

### 何时使用ε产生式 ###
optexpr 被调用，其过程体中的代码：

	if( lookhead == expr ) match(expr);

lookhead符号';'与终结符号expr不匹配。因此不能以expr为体的产生式，该过程没有没有改变向前看符号，也没有做任何其他操作就返回了。不做任何操作就对应用ε产生式的情形；如果向前看符号不在FIRST(expr)产生式中，我们就使用ε产生式。

### 设计一个预测分析器 ###
对于文法的任何非终结符号，它的各个产生式的FIRST集合互不相交。如果我们有一个翻译方案，即一个增加语义动作的文法，那么我们就可以将这些语义动作当作此语法分析器的过程的一部分执行。

构造方法：

1. 先不考虑产生式中的动作，构造一个预测分析器
1. 将翻译方案中的动作拷贝到语法分析器中。如果一个动作出现在产生式p中的文法符号X的后面，则该动作就被拷贝到到p的代码中X的实现之后。否则，如果该动作出现在一个产生式的开否，那么它就被拷贝到该产生式体的实现代码之前。

### 左递归 ###
递归下降语法分析器有可能进入无线循环。当出现所示的产生式时，分析器就会出现无限循环。第二次调用产生式的过程和第一次调用时一样，并不断循环。

	expr->expr + term

消除左递归，考虑产生式

	A -> Aα | β

其中α和β是不以A开头的终结符/非终结符的序列，

A->Aα的右部的最左符号是A本身，非终结符号A和它的产生式就被称为左递归(left recursive),不断应用这个产生式就会将A的右边生成一个a的序列。最终将A替换为β时可以得到β后面有0到多个α的序列

如下产生式可达到一样的效果

	A->βR
	R->αR|e

非终结符R和它的产生式是R->αR是右递归的。对于包含了左结合运算符(比如减法)的表达式的翻译就变得较为困难。
 
## 简单表达式的翻译器 ##

*翻译为后缀表示形式的动作*

	expr -> expr + term { print('+') }
		  | expr - term { print('-') }
	      | term
	
	term -> 0 { print('0') }
		 -> 1 { print('1') }
		 -> 2 { print('2') }
		 ...
	   	 -> 9 { print('9') }

### 抽象语法和具体语法 ###
在一个表达式的抽象语法树中，每个内部结点代表一个运算符，该结点的子结点代表这个运算符的运算分量。

在抽象语法树中，内部结点代表的是函数构造；而在语法分析树中，内部结点代表的是非终结符号。

文法中有部分是辅助符号，抽象语法树(abstract syntax tree)中，通常不需要这些辅助符号，有时语法分析树成为具体语法树(concrete syntax tree),相应的文法成为该语言的具体语法(concrete syntax)。

	令 A = expr
   	  α = + term {print('+')}
	  β = - term {print('-')}
	  γ = term

进行左递归消除转换

	A-> γR
	R-> αR|ε 

![捕获.PNG](http://ww1.sinaimg.cn/large/48ceb85dly1ge3gpcddptj20c506udfr.jpg)

*翻译方案伪代码*

	void expr(){
		term();rest();
	}
	
	void rest(){
		if( lookhead == '+' ){
			match('+');term();print('+');rest();
		}
		else if( lookhead == '-' ) {
			match('-');term();print('-');rest();
		}
		else{}/*不对输入做任何处理*/
	}
	
	void term() {
		if( lookhead 是一个数位) {
			t = lookhead; match(lookhead); print(t);
		}
		else report('语法错误');
	}

如果一个过程体中执行的最后一条语句时对该过程的递归调用，那么这个调用就被称为尾递归。

*消除尾递归*

	void rest() {
		while( true ) {
			if( lookhead == '+' ){
				match('+');term();print('+');continue;
			}
			else if ( lookhead == '-' ) {
				match('-');term();print('-');continue;
			}
			break;
		}
	}

*中缀表达式转后缀表达式的Java代码*

	import java.io.*;
	class Parser {
	    static int lookhead;
	
	    public Parser() throws IOException {
	        lookhead = System.in.read();
	    }
	
	    void expr() throws IOException{
	        term();
	        while (true) {
	            if( lookhead == '+' ){
	                match('+'); term();System.out.write('+');
	            }
	            else if( lookhead == '-' ) {
	                match('-'); term();System.out.write('-');
	            }
	            else return;
	        }
	    }
	
	    void term() throws  IOException {
	        if(Character.isDigit((char)lookhead)) {
	            System.out.write((char)lookhead);
	            match(lookhead);
	        }
	        else throw new Error("syntax error");
	    }
	
	    void match(int t) throws IOException {
	        if( lookhead == t ) lookhead = System.in.read();
	        else throw new Error("Syntax error");
	    }
	}
	public class Postfix {
	    public static void main(String[] args) throws IOException {
	        Parser parser = new Parser();
	        parser.expr();
	        System.out.write('\n');
	    }
	}

### 预读 ###
在决定向语法分析器返回哪个词法单元之前，语法分析器可能需要预先读入一些字符。例如读入> 之后必须预先读下一个字符，如果下一个字符是=那么这个序列代表大于等于的词法单元词素。像*这样的运算符不需要预读就能够识别。

一个通用预选读取的方法就是使用输入缓冲区。

### 常量 ###
将字符组成整数并计算它的数值的工作通常由词法分析器完成，在语法分析和翻译过程中可以将数字当做一个单元进行处理。

可以创建一个代表整形的终结符号,比如num,也可以将整数常量语法加入到文法中,将输入32+28+59转换成序列<num,31><+><num,28><+><num,59>,这里终结符号+没有属性，它的元组就是<+>

### 关键字和标识符 ###
语言的文法通常把标识符当做终结符号进行处理。

只有一个字符串不是关键字时它才能组成一个标识符。

用一个表来保存字符串，解决单一表示的字符串和保留字问题

*词法分析器代码*

	public class Lexer {
	    public int line = 1;
	    private char peek = ' ';
	    private Hashtable words = new Hashtable();
	    void reserve(Word t){
	        words.put(t.lexeme, t);
	    }
	    public Lexer(){
	        reserve(new Word(Tag.TRUE, "true"));
	        reserve(new Word(Tag.FALSE, "false"));
	    }
	    public Token scan() throws IOException{
	        for(;; peek = (char)System.in.read()){
	            if(peek == ' ' || peek == '\t') continue;
	            else if( peek == '\n') line = line + 1;
	            else break;
	        }
	
	        if(Character.isDigit(peek)){
	            int v = 0;
	            do {
	                v = 10*v + Character.digit(peek, 10);
	                peek = (char)System.in.read();
	            }while(Character.isDigit(peek));
	            return new Num(v);
	        }
	        if(Character.isLetter(peek)){
	            StringBuffer b = new StringBuffer();
	            do{
	                b.append(peek);
	                peek = (char)System.in.read();
	            }while (Character.isLetter(peek));
	            String s = b.toString();
	            Word w = (Word)words.get(s);
	            if( w != null )return w;
	            w = new Word(Tag.ID, s);
	            words.put(s, w);
	            return w;
	        }
	        Token t = new Token(peek);
	        peek = ' ';
	        return t;
	    }
	}


# 希腊字母表 #
αβγδεζηθικλμνξοπρστυφχψω