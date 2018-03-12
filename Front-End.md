# HTML #
# JavaScript #
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