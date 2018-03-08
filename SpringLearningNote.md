# Spring MVC #
[使用IDEA搭建Spring MVC](http://www.cnblogs.com/qixiaoyizhan/p/5819392.html)

> Q：错误: 代理抛出异常错误: java.rmi.server.ExportException: Port already in use: 1099; nested exception is: java.net.BindException: Address already in use: JVM_Bind
> 
> 这里说的是1099端口被其它进程占用了.
> 
> 还是重启吧

后台数据返回

> Q：返回json
> 
> 添加 jackson-databind 的依赖，默认转换成Json
> 
> 在@RequestMapping中
> 
> produces用于解决返回中文乱码问题，application/json;为json解决中文乱码
> 
> 不要使用text/plain，会返回406问题

Spring采用Java配置，不适用web.xml

**扩展AbstractAnnotationConfigDispatchServletInitializer**
> 
> Servlet3.0环境中容器会在路径中查找实现javax.servlet.ServletContainerInitializer接口的类，如果能发现，就会用它来配置Servlet容器
> 
> Spring提供了这个接口的实现，名为SpringServletContainerInitializer，这个类又会查找实现WebApplicationInitializer的类并将配置任务交给它来完成
> 
> Spring3.2 引入一个便利的WebApplicationInitializer基础实现，也就是AbstractAnnotationConfigdspatcherServletInitializer

在AbstractAnnotationConfigDispatcherServletInitializer将DispatcherServlet注册到Servlet容器之后，就会调用customizeRegistration(),并将Servlet注册后得到的Registration.Dynamic传递进来。通过重载customizeRegistration()方法，我们可以对DispatcherServlet进行额外的配置

两个应用上下文

> DispatchSertlet加载应用上下文时，使用WebConfig中的配置类（getServletConfigClasses返回）中定义的bean
> 
> Spring Web应用通常会有另一个应用上下文，这个上下文是由ContextLoaderListener创建的
> 
> DispatcherServlet加载包含Web组件的bean，如控制器，视图解析器以及处理器映射
> 
> ContextLoaderListener要加载应用中的其他bean
> 
> getServletConfigClasses()方法返回的带有@Configuration注解的类将会用来定义DispatcherServlet应用上下文中的bean
> 
> getRootConfigClasses()方法返回的带有@Configuration注解的类将会用来配置ContextLoaderListener创建的应用上下文中的bean

@EnableWebMvc 对应于 xml中的 <mvc:annotation-driven>

**Controller级别的RequestMapping中的映射必须在url中加上"/"，方法级别的映射url中可以没有"/"**

表单POST的映射中 模型的字段必须有getter 和 setter

**@ComponentScan **

这个注解在Sping中启动组件扫描，默认扫描与配置类相同的包

xml中使用<context:component-scan>

**@RunWith(SpringJUnit4ClassRunner.class)**

在测试中创建Spring的应用上下文

@ContextConfiguration 会告诉它需要在CDPlayerConfig中加载配置

@ComponentScan(basePackages={})

可使用空标记接口

自动化配置无法将第三方库中的组件配置到应用中

    @Bean
    public CDPlayer cdPlayer(){
    	return new CDPlayer(sgtPeppers());
    }

因为sgtPeppers()方法上添加了@Bean注解，Spring将会拦截所有对它的调用，并确保直接返回该方法锁创建的bean，而不是每次都对其进行实际的调用

默认情况下，Spring中所有的bean都是单例
 
**@Scope(value=WebApplicationContext.SCOPE_SESSION,
proxyMode = ScopedProxyMode.INTERFACES)**


> Spring并不会将实际的ShoppingCart注入到StoreService中，Spring会注入一个到ShoppingCart bean的代理，这个代理会暴露与ShoppingCart相同的方法，代理在调用的时候将进行懒解析并将调用委托给回话作用域内真正的ShoppingCart bean,
如果ShoppingCart是接口的话没有问题，但是ShoppingCart是一个具体的类的话，就要使用CGLib来生成基于类的代理,请求域的bean应该也以作用域代理的方式进行注入
