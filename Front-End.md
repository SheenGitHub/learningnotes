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

radial-gradient:径向渐变
linear-gradient:线性渐变

## 文本效果 ##



