## Node ##


# Express #

### Request 对象 ###

 - request 对象表示 HTTP 请求，包含了请求查询字符串，参数，内容，HTTP 头部等属性。常见属性有：

- req.app：当callback为外部文件时，用req.app访问express的实例
- req.baseUrl：获取路由当前安装的URL路径
- req.body / req.cookies：获得「请求主体」/ Cookies
- req.fresh / req.stale：判断请求是否还「新鲜」
- req.hostname / req.ip：获取主机名和IP地址
- req.originalUrl：获取原始请求URL
- req.params：获取路由的parameters
- req.path：获取请求路径
- req.protocol：获取协议类型
- req.query：获取URL的查询参数串
- req.route：获取当前匹配的路由
- req.subdomains：获取子域名
- req.accepts()：检查可接受的请求的文档类型
- req.acceptsCharsets / req.acceptsEncodings / req.acceptsLanguages：返回指定字符集的第一个可接受字符编码
- req.get()：获取指定的HTTP请求头
- req.is()：判断请求头Content-Type的MIME类型


### Response 对象 ### 

- response 对象表示 HTTP 响应，即在接收到请求时向客户端发送的 HTTP 响应数据。常见属性有：

- res.app：同req.app一样
- res.append()：追加指定HTTP头
- res.set()在res.append()后将重置之前设置的头
- res.cookie(name，value [，option])：设置Cookie
- opition: domain / expires / httpOnly / maxAge / path / secure / signed
- res.clearCookie()：清除Cookie
- res.download()：传送指定路径的文件
- res.get()：返回指定的HTTP头
- res.json()：传送JSON响应
- res.jsonp()：传送JSONP响应
- res.location()：只设置响应的Location HTTP头，不设置状态码或者close response
- res.redirect()：设置响应的Location HTTP头，并且设置状态码302
- res.render(view,[locals],callback)：渲染一个view，同时向callback传递渲染后的字符串，如果在渲染过程中有错误发生next(err)将会被自动调用。callback将会被传入一个可能发生的错误以及渲染后的页面，这样就不会自动输出了。
- res.send()：传送HTTP响应
- res.sendFile(path [，options] [，fn])：传送指定路径的文件 -会自动根据文件extension设定Content-Type
- res.set()：设置HTTP头，传入object可以一次设置多个头
- res.status()：设置HTTP状态码
- res.type()：设置Content-Type的MIME类型


**解决sudo: npm: command not found**

    sudo ln -s /usr/local/bin/npm /usr/bin/npm  
    sudo ln -s /usr/local/bin/node /usr/bin/node

linux平台的非root用户要让node运行在80端口下，可以这样：

    sudo setcap CAP_NET_BIND_SERVICE+ep  /usr/local/bin/node


# mysql #
关闭数据库连接，结束进程

    connection.end()
# xml2js #

[xml2js builder.coffee](https://github.com/Leonidas-from-XIV/node-xml2js/blob/master/src/builder.coffee)

requiresCDATA 认为字符串中必需存在 &,>,< 等会被解析的符号时才使用CDATA对这段代码进行保护

xml2js/lib/builder.js

    54,40 删除 requiresCDATA(obj) 
    80,44 删除 requiresCDATA(obj) 
    90,48 删除 requiresCDATA(obj) 

# npm #
## 安装 ##
### windows中安装 ###
- npm config set prefix "C:\Program Files\nodejs\node_global"
- npm config set cache "C:\Program Files\nodejs\node_cache"
- 新建”NODE_PATH”，输入”C:\Program Files\nodejs\node_global\node_modules“
- 变量”PATH”修改为“C:\Program Files\nodejs\node_global\”

### 淘宝源安装 ###
#### 临时使用 ####
npm --registry https://registry.npm.taobao.org install express
#### 持久使用 ####
npm config set registry https://registry.npm.taobao.org

配置后可通过下面方式来验证是否成功

npm config get registry

npm info express

#### 配置cnpm ####
npm install -g cnpm --registry=https://registry.npm.taobao.org
#### npm install unexpected token ####
npm cache verify 清理cache

#### Cannot enqueue Handshake after invoking quit ####
mysql 关闭的connection不能再重新connect；

## --save-dev --save ##

npm install module：
-

- 会把module包安装到node_modules目录中
- 不会修改package.json
- 之后运行npm install 命令时，不会自动安装module包

npm install module --save
-

- 会把module包安装到node_modules目录汇总
- 会修改package.json，将模块名和版本号添加到dependencies部分
- 之后运行npm install 命令时，会自动安装module包
- 之后运行npm install --production或者注明NODE_ENV变量值为production时，会自动安装 module到node_modules目录中,即是在线上环境运行时会将包安装

npm install module --save-dev
-

- 会把module包安装到node_modules目录汇总
- 会修改package.json，将模块名和版本号添加到devDependencies部分
- 之后运行npm install 命令时，会自动安装module包
- 之后运行npm install --production或者注明NODE_ENV变量值为production时，不会自动安装msbuild到node_modules目录中，即是在线上环境并不会进行安装。

# 问题 #
#### TypeError: First argument must be a string or buffer ####

    response.write(200,{"Content-Type":"text/plain"});
    
    修改为 response.writeHead(*,*);

#### Most middleware (like bodyParser) is no longer bundled with Express ####

    新版express不包含bodyparser,需要单独安装

## 微信公众号开发 ##
> {"errcode":"40016","errmsg":"invalid button size hint:[xxxxx]"}

将post的对象从json对象转为string， JSON.stringigfy(jsonObject)

> {"errcode":"40054","errmsg":"invalid button url domain hint:[xxxxx]"}

url地址前缺少http:// 协议名称

> {"errcode":"40016","errmsg":"invalid button key size hint:[xxxxx]"}

在button中添加key

> {"errcode":"45015","errmsg":"response out of time limit or subscription is canceled hint:[rnk_TA0921ge21]"}

当用户微信不活跃时间超过24小时（此时间当前是多少由腾讯定），微信公众号不会将信息推送到用户。

# Webpack #

标准配置

// 一个常见的`webpack`配置文件
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

	module.exports = {
		entry: __dirname + "/app/main.js", //已多次提及的唯一入口文件
		output: {
		path: __dirname + "/build",
		filename: "bundle-[hash].js"
	},
	devtool: 'none',
	devServer: {
		contentBase: "./public", //本地服务器所加载的页面所在的目录
		historyApiFallback: true, //不跳转
		inline: true,
		hot: true
	},
	module: {
		rules: [{
			test: /(\.jsx|\.js)$/,
			use: {
				loader: "babel-loader"
			},
			exclude: /node_modules/
			}, {
				test: /\.css$/,
				use: ExtractTextPlugin.extract({
					fallback: "style-loader",
					use: [{
						loader: "css-loader",
						options: {
							modules: true,
							localIdentName: '[name]__[local]--[hash:base64:5]'
						}
						}, {
							loader: "postcss-loader"
					}],
				})
			}
			}
		]
	},
	plugins: [
		new webpack.BannerPlugin('版权所有，翻版必究'),
		new HtmlWebpackPlugin({
			template: __dirname + "/app/index.tmpl.html" //new 一个这个插件的实例，并传入相关的参数
		}),
		new webpack.optimize.OccurrenceOrderPlugin(),
		new webpack.optimize.UglifyJsPlugin(),
		new ExtractTextPlugin("style.css")
	]
	};

## 依赖包 ##
### Babel依赖 ###
// npm一次性安装多个依赖模块，模块之间用空格隔开
npm install --save-dev babel-core babel-loader babel-preset-env babel-preset-react

### React React-DOM 依赖 ###
npm install --save react react-dom

### Webpack-server依赖 ###
局部安装 webpack-dev-server webpack webpack-cli

### CSS 依赖 ###
- css-loader使你能够使用类似@import 和 url(...)的方法实现 require()的功能
- style-loader将所有的计算后的样式加入页面中


# 常用包 #
### async ###
[async模块](https://blog.csdn.net/zzwwjjdj1/article/details/51857959)

async模块是为了解决嵌套金字塔,和异步流程控制而生

## Q&A ##

> webpack一直提示安装webpack-cli 

全局安装webpack-cli

> ERROR in multi ./src/index.js ./public/bundle.js

webpack --display-error-details 查看错误细节

需要配置文件webpack.config.js

> module parse failed Unpected token g in JSON at postion:4

JSON 文件里使用  "property":"value", 不直接使用名称或 ""



## 加密 ##

### AES ###


### Base64 ###

# Vue #
## 属性 ##
非HTML的标准属性，需要使用v-bind:,例如data-*等

HTML 中的特性名是大小写不敏感的，所以浏览器会把所有大写字符解释为小写字符。这意味着当你使用 DOM 中的模板时，camelCase (驼峰命名法) 的 prop 名需要使用其等价的 kebab-case (短横线分隔命名) 命名

### v-for ###


为了在循环的item中使用属性，最好将item本身定义为组件，方便使用组件实例中的属性

还是由于 JavaScript 的限制，Vue 不能检测对象属性的添加或删除

可以直接使用push

#### 就地复用 ####
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fsitszq21ag209p07dq46.gif)
**可以使用 Vue.set(object, key, value) 方法向嵌套对象添加响应式属性**

**Vue 包含一组观察数组的变异方法，所以它们也将会触发视图更新。这些方法如下：**

- push()
- pop()
- shift()
- unshift()
- splice()
- sort()
- reverse()
#### 数组对象更新的检测 ####
不要使用 Array.prototype.push(target,added);

这种情况不会检测到更新
## 组件 ##
组件中的参数要放在 props:[]数组中

组件的内容必须只有单个根元素

### DOM中模板解析 ###
	

	<table>
	  <tr is="blog-post-row"></tr>
	</table>

需要注意的是如果我们从以下来源使用模板的话，这条限制是不存在的：

	- 字符串 (例如：template: '...')
	- 单文件组件 (.vue)
	- <script type="text/x-template">
### 动态组件 ###
	<component v-bind:is="currentTabComponent"></component>

使用计算属性currentTabcomponent来切换组件

### 禁用特性继承 ###
*inheritAttrs:false*

不在props中的属性(除class和style外)将不被渲染

*$attrs*

	v-bind="$attrs"

没有声明传递的属性将被传递到子组件，减少多个属性的传递声明

### v-model绑定 ###
> 一个组件上的 v-model 默认会利用名为 value 的 prop 和名为 input 的事件

自定义 checked (checked 和 onchange事件 ，**不是默认的value和input事件**)

	model: {
	    prop: 'customchecked',
	    event: 'custom' //自定义事件名称
	},
	props: {
    	customchecked: Boolean
	},
	template: `
    <input
      type="checkbox"
      v-bind:checked="checked"
	  //在change事件发生时，触发custom事件，
	  //将$event.target.checked绑定在custom事件要求的prop上，即customchecked
	  //同时customchecked绑定了组件的checked属性，触发属性的变化
      v-on:change="$emit('custom', $event.target.checked)" 
    >
	`
	})

### Prop ###
驼峰命名法的prop名在DOM模板中要使用kebab-case(短横线分隔命名)

	props:['postTitle']
	<blog-post post-title="hello!"></blog-post>
	<blog-post v-bind:postTitle="post.postTitle"></blog-post> //动态绑定
	<blog-post v-on:enlarge-text="postFontSize+= 0.1"></blog-post>//v-on在组件上定义了一个事件

	<blog-post v-bind="post"></blog-post>
	等价于
	定义了['id','title'],同理v-bind="$attrs";v-on="$listeners"
	<blog-post
	  v-bind:id="post.id"
	  v-bind:title="post.title"
	></blog-post>
#### 单向数据流 ####
所有的 prop 都使得其父子 prop 之间形成了一个单向下行绑定：父级 prop 的更新会向下流动到子组件中


### data ###
#### 组件上的data ####
一个组件的data选项必须是一个函数

组件上定义的参数，在组件自身的模板上可以使用
DOM上属性由外部传入，不能调动组件上的函数
### slot ###
使用 

	<slot name="header"></slot>

定义具名插槽

使用

	<template slot ="header"></template>
	或者
	<Tag slot="header"></Tag>

来使用插槽

向slot传值

	<slot v-bind:todo="todo">
		{{todo.text}}
	</slot>

#### slot-scope ####
解构使用 slot-scope

	<todo-list v-bind:todos="todos">
	  <template slot-scope="{ todo }">
	    <span v-if="todo.isComplete">✓</span>
	    {{ todo.text }}
	  </template>
	</todo-list>

### Template标签 ###
template不会渲染成元素，用div的话会被渲染成元素。把if,show,for等语句抽取出来放在template上面，把绑定的事件放在temlpate里面的元素上，可以使html结构更加清晰，还可以改善一个标签过长的情况

## Virtual DOM ##
> createElement 到底会返回什么呢？其实不是一个实际的 DOM 元素。它更准确的名字可能是 createNodeDescription，因为它所包含的信息会告诉 Vue 页面上需要渲染什么样的节点，及其子节点。我们把这样的节点描述为“虚拟节点 (Virtual Node)”，也常简写它为“VNode”。“虚拟 DOM”是我们对由 Vue 组件树建立起来的整个 VNode 树的称呼。

## 单文件组件 ##
### CSS预处理器 ###
- Sass
- Less
- Stylus

## Vue-router ##
	var app = new Vue({
		el:'#app',
		router,
		component:{APP},
		template:'<APP/>'
	})

相当于

	const router = new VueRouter({
	  routes // (缩写) 相当于 routes: routes
	})
	
	const app = new Vue({
	  router
	}).$mount('#app')

### URL中的 #  ###
实际上存在三种模式：

- Hash: 使用URL的hash值来作为路由。支持所有浏览器。 
- History: 以来HTML5 History API 和服务器配置。参考官网中HTML5 History模式 
- Abstract： 支持所有javascript运行模式。如果发现没有浏览器的API，路由会自动强制进入这个模式。

### 嵌套路由 ###

	const router = new VueRouter({
	  routes: [
	    { path: '/user/:id', component: User,
	      children: [
	        // UserHome will be rendered inside User's <router-view>
	        // when /user/:id is matched
	        { path: '', component: UserHome },
					
	        // UserProfile will be rendered inside User's <router-view>
	        // when /user/:id/profile is matched
	        { path: 'profile', component: UserProfile },
	
	        // UserPosts will be rendered inside User's <router-view>
	        // when /user/:id/posts is matched
	        { path: 'posts', component: UserPosts }
	      ]
	    }
	  ]
	})
#### 代码导航 ####
	router.push(location, onComplete?, onAbort?)
	
	router.push({ path: 'register', query: { plan: 'private' }})

## Transition ##
1. v-enter：定义进入过渡的开始状态。在元素被插入之前生效，在元素被插入之后的下一帧移除。
- v-enter-active：定义进入过渡生效时的状态。在整个进入过渡的阶段中应用，在元素被插入之前生效，在过渡/动画完成之后移除。这个类可以被用来定义进入过渡的过程时间，延迟和曲线函数。
- v-enter-to: 2.1.8版及以上 定义进入过渡的结束状态。在元素被插入之后下一帧生效 (与此同时 v-enter 被移除)，在过渡/动画完成之后移除。
- v-leave: 定义离开过渡的开始状态。在离开过渡被触发时立刻生效，下一帧被移除。
- v-leave-active：定义离开过渡生效时的状态。在整个离开过渡的阶段中应用，在离开过渡被触发时立刻生效，在过渡/动画完成之后移除。这个类可以被用来定义离开过渡的过程时间，延迟和曲线函数。
- v-leave-to: 2.1.8版及以上 定义离开过渡的结束状态。在离开过渡被触发之后下一帧生效 (与此同时 v-leave 被删除)，在过渡/动画完成之后移除。

![](http://ww1.sinaimg.cn/large/48ceb85dgy1frnos8jwyhj20xc0godfv.jpg)

# Network #
## curl ##

curl URL -d {}

向URL发起请求
# 中间件 #
## morgan ##
代替 express.logger()

## body-parser ## 

bodyParser.json()

bodyParser.urlencoded( { extended : false } );

## method-override ##
[method-override](https://blog.csdn.net/millions_02/article/details/78945189)

## multer ##
处理文件上传

## express-error-handler ##

## app.router ##
无需使用，使用单独的文件描述响应路径下的路由

 
	  app.route('/book')
	  .get(function (req, res) {
	    res.send('Get a random book')
	  })
	  .post(function (req, res) {
	    res.send('Add a book')
	  })
	  .put(function (req, res) {
	    res.send('Update the book')
	  })
## Q&A ##
#### 无法使用ip访问vue项目 ####
修改 config/index.js 中 port:'localhost' 为 '0.0.0.0'

#### 回调中无法访问Vue的根元素 ####
使用箭头函数绑定this
#### 安装React Devtools ####
Run npm install or yarn
Run npm run build:extension:chrome, the extension will be compiled to shells/chrome/build/unpacked/

# MongoDB #
## 配置 ##
### 安装 ###

[安装MongoDB](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-red-hat/)
创建 a /etc/yum.repos.d/mongodb-org-4.0.repo

#### 配置内容 #### 

	[mongodb-org-4.0]
	name=MongoDB Repository
	baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.0/x86_64/
	gpgcheck=1
	enabled=1
	gpgkey=https://www.mongodb.org/static/pgp/server-4.0.asc

#### 安装  ####

sudo yum install -y mongodb-org

#### 启动 ####

启动  sudo service mongod start


确认启动 查看 /var/log/mongodb/mongod.log，默认端口27017

关闭 sudo service mongod stop

重启 sudo service mongod restart

启动客户端 mongo --host 127.0.0.1:27017

### CRUD ###
	显示所有库 show dbs
	创建数据库 use DATA_BASENAME
	删除数据库 db.dropDatabase
	当前数据库 db
	创建集合 db.createCollection(name, options)
	查看集合 show collections 
	插入文档时，自动创建集合
	删除集合 db.collection.drop()
	插入文档 db.collection.insert(document)
	向指定集合中插入一条文档数据 db.collection.insertOne():
	向指定集合中插入多条文档数据 db.collection.insertMany()

更新 update

	update db.collection.update(
	   <query>,
	   <update>,
	   {
	     upsert: <boolean>,
	     multi: <boolean>,
	     writeConcern: <document>
	   }
	)

update中的操作符 $inc,$set

save

	db.collection.save(
	   <document>,
	   {
	     writeConcern: <document>
	   }
	)

查看  db.col.find().pretty()

删除文档  

	db.collection.remove(
	   <query>,
	   <justOne>
	)

query中的比较  $lt,$lte,$gt,$gte,$ne

AND条件 

db.col.find({key1:value1, key2:value2}).pretty()

OR条件 

db.col.find({$or: [{key1: value1}{key2:value2}]}).pretty()

$type 类型查询

limit db.COLLECTION_NAME.find().limit(NUMBER)

skip 跳过条数 db.COLLECTION_NAME.find().limit(NUMBER).skip(NUMBER)

排序 db.COLLECTION_NAME.find().sort({KEY:1}) 1升序，2降序

索引 db.collection.createIndex(keys, options)

聚合 db.col.aggregate([{$group:{_id:'$by',number:{$sum:"$likes"}}}])

### Nodejs操作mongoDB ###

插入操作

	var MongoClient = require('mongodb').MongoClient;
	var url = "mongodb://localhost:27017/";
	 
	MongoClient.connect(url, function(err, db) {
	    if (err) throw err;
	    var dbo = db.db("runoob");
	    var myobj =  [
	        { name: '菜鸟工具', url: 'https://c.runoob.com', type: 'cn'},
	        { name: 'Google', url: 'https://www.google.com', type: 'en'},
	        { name: 'Facebook', url: 'https://www.google.com', type: 'en'}
	       ];
	    dbo.collection("site").insertMany(myobj, function(err, res) {
	        if (err) throw err;
	        console.log("插入的文档数量为: " + res.insertedCount);
	        db.close();
	    });
	});

查询操作 

	var MongoClient = require('mongodb').MongoClient;
	var url = "mongodb://localhost:27017/";
	 
	MongoClient.connect(url, function(err, db) {
	    if (err) throw err;
	    var dbo = db.db("runoob");
	    dbo.collection("site"). find({}).toArray(function(err, result) { // 返回集合中所有数据
	        if (err) throw err;
	        console.log(result);
	        db.close();
	    });
	});


#### mongoose的 useNewUrlParser 警告解决办法 ####
	mongoose.connect('mongodb://127.0.0.1:27017/admin', { useNewUrlParser: true },(err,res)=>{
	    if(!err){
	        console.log(res)
	    }
	})

