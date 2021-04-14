# React #
#### /topics/:topicId 无法显示 path为 /topics/rendering的Rote的内容 ####

    Topic = ({match})=>{
    ......
    <Route path={`${match.url}/:topicId`}
    ......
    }
    
    <Route exact path="topics" component={Topic}/>

Route中多了 exact ，exact需要严格匹配，当路径为/topics/rendering时，path="topics"已经无法严格匹配 Topic组件本已经无法显示，Topic组件内的子组件显然无法再显示。

# Git #
#### 执行git push出现"Everything up-to-date" ####
1. 没有git add .
2. 没有git commit -m "提交信息"

# VS Code #
####vue-cli项目 Eslint保存时不自动格式化 ####
> 配置vue.config.js的module.exports输出对象的listOnSave属性
> 
> lintOnSave: 'warning'|true|lintOnSave: process.env.NODE_ENV === 'development',

以上并非关键有效，点击状态来的ESLint，双钩状态即可

*vue-cli中安装如下依赖实现eslint的检查*

	@vue/cli-plugin-eslint
	
	eslint
	
	eslint-plugin-prettier
	
	@vue/eslint-config-prettier
	
	eslint-plugin-vue


*.eslintrc.js配置*

	module.exports = {
	  root: true,
	  env: {
	    node: true
	  },
	  extends: ["plugin:vue/essential", "eslint:recommended", "@vue/prettier"],
	  parserOptions: {
	    parser: "babel-eslint"
	  },
	  rules: {
	    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
	    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off"
	  }
	};

重启VsCode即生效
#### 手动保存命令 ####
npm run lint --fix

# Vue #
#### You are using the runtime-only build of Vue where the template compiler is not available. Either pre-compile the templates into render functions, or use the compiler-included build. ####

**js代码**

	import Vue from 'vue';import App from './App.vue';
	new Vue({    
	    el: "#app",    
	    template: '<App/>',    
	    component: {App}
	});

vue有两种形式的代码 compiler（模板）模式和 runtime 模式（运行时），vue模块的package.json的main字段默认为runtime模式， 指向了"dist/vue.runtime.common.js"位置

使用 template属性，需要引入带编译器的完整版的vue.esm.js

# Webpack/Node #
#### Error: Cannot find module 'webpack-cli/bin/config-yargs' ####

修改page.json文件中的脚本，webpack-dev-server 改写成 webpack serve 即可解决报错问题。

#### Uncaught TypeError: Cannot set property count of #<Object> which has only a getter ####
ES Module导出值为**动态映射**，只读，不可修改, CommonJS为**值拷贝**，可本地使用

#### Uncaught ReferenceError: Cannot access '__WEBPACK_DEFAULT_EXPORT__' before initialization ####
ES Module依赖循环引用时出现，

模块完成初始化之前不可以访问，可以被引用(通过映射，可在后期完成初始化后再使用)
#### 源文本中存在无法识别的标记。 ####
npm install babel-loader @babel/core @babel/preset-env

给具体依赖选项加上双引号

npm install "babel-loader" "@babel/core" "@babel/preset-env"

#### Error: Cannot find module 'C:\Program Files\nodejs\node_global\node_modules\webpack\bin\webpack.js' ####
重新npm install一下，更新下依赖配置，或者提前清一下缓存

# 依赖库 #
## d3 ##
#### TypeError: d3.scale is undefined ####
Be careful of the version for d3.js In version 4.2.1 d3.scale.linear(); gives the following error. TypeError: d3.scale.linear() scale is undefined

I fixed this error in version 4.2.1 by using d3.scaleLinear();

# Python #
#### can only concatenate str (not "int") to str ####
python并不能像java一样，在做拼接的时候自动把类型转换为string类型，譬如将print(1+"a")改为print(str(1)+"a")就可以了

# Redis #
#### Could not get a resource from the pool(SocketTimeoutException:) ####
- redis本身有没有启动
- 电脑能不能ping通redis所在的ip
- redis的设置里有没有设置ip白名单

#### redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketException: Connection reset ####