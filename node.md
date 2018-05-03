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
### 
 ###

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