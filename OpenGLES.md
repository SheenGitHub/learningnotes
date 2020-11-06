# OpenGL ES #

#### 什么是 OpenGLES ####
OpenGLES 全称 OpenGL for Embedded Systems ，是三维图形应用程序接口 OpenGL 的子集，本质上是一个跨编程语言、跨平台的编程接口规范，主要应用于嵌入式设备，如手机、平板等。
#### 官方在线文档 ####
[https://www.khronos.org/registry/OpenGL-Refpages/es3/](https://www.khronos.org/registry/OpenGL-Refpages/es3/)

## 基础概念 ##
### 统一变量 ###
统一变量（uniform）是存储应用程序通过 API 传递给着色器的 只读常量 数值的变量。例如上例中的 u_Matrix。

如果统一变量在顶点着色器和片段着色器中均有声明，则声明的类型必须相同，且值也需相同。

#### 获取 ####
	GLint glGetUniformLocation(GLuint program, const GLchar *name);

- program：你创建的着色器程序返回的id。
- name：着色器语言中该变量的名称，例如 u_Matrix

#### 传值 ####
void glUniform1f(GLint location, GLfloat v0);

- location：统一变量的位置，即上述步骤中获取到的值。
- count：需要加载的元素数量，针对向量类型和矩阵类型。例如我们定义的是 uniform mat4 u_matrix 那么数量就是 1，如果是 uniform vec3 u_Position[3] 那么数量就是 3。
- transpose：针对矩阵类型，指定矩阵采用列优先顺序（GL_FALSE）还是行顺序优先（GL_TRUE）。
- v0, v1, v2, v3：输入的统一变量值。
- value：输入的统一变量的数组指针。

### 属性 ###
属性只是指顶点着色器的输入变量，因为片段着色器的输入是顶点着色器的输出

#### 获取 ####
	GLint glGetAttribLocation(GLuint program, const GLchar *name);

#### 传值 ####
	void glVertexAttrib1f(GLuint index, GLfloat x);
## 纹理映射 ##
在 OpenGL 中，纹理实际上是一个可以被采样的复杂数据集合，是 GPU 使用的图像数据结构，纹理分为 2D 纹理、 立方图纹理和 3D 纹理。

纹理中的一个单独数据元素称为纹素或纹理像素

纹理映射就是通过为图元的顶点坐标指定恰当的纹理坐标，通过纹理坐标在纹理图中选定特定的纹理区域，最后通过纹理坐标与顶点的映射关系，将选定的纹理区域映射到指定图元上。

#### 纹理映射的一般步骤 ####
- 生成纹理，编译链接着色器程序
- 确定纹理坐标及对应的顶点坐标
- 加载图像数据到纹理，加载纹理坐标和顶点坐标到着色器程序
- 绘制
## 方法 ##
### glGenTextures  ###
生成纹理名称

	void glGenTextures(	GLsizei n,
	 	GLuint * textures);
> n
> Specifies the number of texture names to be generated.
> 	
> textures
> Specifies an array in which the generated texture names are stored.

textures中返回的名称只是标志为used，只有首次使用glBindTexture绑定才能获取state和dimensionality

### glBindTexture ###
	//将纹理 m_TextureId 绑定到类型 GL_TEXTURE_2D 纹理
	glBindTexture(GL_TEXTURE_2D, m_TextureId);

### glTexParameterf ###
	void glTexParameterf(	GLenum target,
	 	GLenum pname,
	 	GLfloat param);

> GL_TEXTURE_WRAP_S 横轴
> 
> Sets the wrap parameter for texture coordinate s to either GL_CLAMP_TO_EDGE, GL_MIRRORED_REPEAT, GL_REPEAT, or GL_CLAMP_TO_BORDER.

### glTexImage2D  ###
设置二维纹理图像


	void glTexImage2D(	GLenum target,
	 	GLint level,
	 	GLint internalFormat,
	 	GLsizei width,
	 	GLsizei height,
	 	GLint border,
	 	GLenum format,
	 	GLenum type,
	 	const void * data);
### glLinkProgram  ###
链接program，attached到program上的shader将会被使用来创建一个可执行的program来运行vertex点的处理器，被attached到program上的Shader将会运行在可编程fragment处理器上。

连接操作的状态会被保存在program对象的state里，可以通过glGetProgramiv调用来查看

link操作的结果是，所有的用户定义的属于program的uniforms变量将被初始化为0，每个program对象的uniform变量会被赋值到一个位置(可被glGetUniformLocation调用查询)，program的命名uniform块的所有的活动uniforms会被在uniform块中赋一个偏移量。同时，未被绑定到vertext属性Index的用户定义属性变量将会被同时绑定上去。

连接操作要遵循OpenGL ES着色语言规范。

不管连接操作成功与否，特定program对象信心会被overwritten，这些信息可以用glGetProgramInfoLog调用

> After the link operation, applications are free to modify attached shader objects

### glUseProgram  ###
安装program对象作为当前渲染的对象状态

	void glUseProgram(	GLuint program);

如果program是0，那么当前的渲染状态指向一个非法的program对象，着色器的使用将是不确定的，这并不是一个error
### glVertexAttribPointer ###
定义通用vertex属性data的数组

	void glVertexAttribPointer(	GLuint index,
	 	GLint size,
	 	GLenum type,
	 	GLboolean normalized,
	 	GLsizei stride,
	 	const void * pointer);
	 
	void glVertexAttribIPointer(	GLuint index,
	 	GLint size,
	 	GLenum type,
	 	GLsizei stride,
	 	const void * pointer);



- index 通用顶点属性中的index
- size 每个通用vertex组件vertext属性的数量
- normalized 是否将数据正规化
- pointer 数组

### glDrawArrays ###
从数组数据开始渲染primitives


	void glDrawArrays(	GLenum mode,
	 	GLint first,
	 	GLsizei count);
### glGetError ###
返回错误，错误被检测到后，错误标志会被置为错误,glGetError返回错误，并将标志置为GL_NO_ERROR
### glGetUniformLocation ###
	GLint glGetUniformLocation(	GLuint program,
	 	const GLchar *name);

glGetUniformLocation returns an integer that represents the location of a specific uniform variable within a the default uniform block of a program object. 

This location value can then be passed to **glUniform** to set the value of the uniform variable or to **glGetUniform** in order to query the current value of the uniform variable.
## 坐标系 ##
[https://blog.csdn.net/shan286/article/details/78611457](https://blog.csdn.net/shan286/article/details/78611457)

![undefined](http://ww1.sinaimg.cn/large/48ceb85dly1gkd59py06tj20hm0ftt91.jpg)
# 着色器 #
## 方法 ##
### texture ###
	gvec4 texture (gsampler1D sampler, float P [, float bias] )
	gvec4 texture (gsampler2D sampler, vec2 P [, float bias] )
	gvec4 texture (gsampler3D sampler, vec3 P [, float bias] )

使用坐标点P，在绑定于sampler的当前纹理上，做纹理查询
# C/C++ 知识点#
## JNI ##
#### 注册本地方法 ####
Normally, in order for the JVM to find your native functions, they have to be named a certain way. e.g., for **java.lang.Object.registerNatives**, the corresponding C function is named **Java_java_lang_Object_registerNatives**. By using registerNatives (or rather, the JNI function RegisterNatives), you can name your C functions whatever you want.

Here's the associated C code (from OpenJDK 6):

	static JNINativeMethod methods[] = {
	    {"hashCode",    "()I",                    (void *)&JVM_IHashCode},
	    {"wait",        "(J)V",                   (void *)&JVM_MonitorWait},
	    {"notify",      "()V",                    (void *)&JVM_MonitorNotify},
	    {"notifyAll",   "()V",                    (void *)&JVM_MonitorNotifyAll},
	    {"clone",       "()Ljava/lang/Object;",   (void *)&JVM_Clone},
	};
	
	JNIEXPORT void JNICALL
	Java_java_lang_Object_registerNatives(JNIEnv *env, jclass cls)
	{
	    (*env)->RegisterNatives(env, cls,
	                            methods, sizeof(methods)/sizeof(methods[0]));
	}
### 配置CMake ###
[https://developer.android.com/studio/projects/configure-cmake?hl=zh-cn](https://developer.android.com/studio/projects/configure-cmake?hl=zh-cn "CMake配置")
## 语言 ##
#### static_cast 和 reinterpret区别 ####
The C++ standard guarantees the following:

**static_casting** a pointer to and from **void*** preserves the address. That is, in the following, **a**, **b** and **c** all point to the same address:

	int* a = new int();
	void* b = static_cast<void*>(a);
	int* c = static_cast<int*>(b);

reinterpret_cast only guarantees that if you cast a pointer to a different type, and then reinterpret_cast it back to the original type, you get the original value. So in the following:

	int* a = new int();
	void* b = reinterpret_cast<void*>(a);
	int* c = reinterpret_cast<int*>(b);

**a** and **c** contain the same value, but the value of **b** is unspecified. (in practice it will typically contain the same address as **a** and **c**, but that's not specified in the standard, and it may not be true on machines with more complex memory systems.)

For casting to and from **void***, **static_cast** should be preferred
#### C++中的nullptr ####
[https://blog.csdn.net/hancoder/article/details/89814638](https://blog.csdn.net/hancoder/article/details/89814638)

C++中为了避免“野指针”（即指针在首次使用之前没有进行初始化）的出现，我们声明一个指针后最好马上对其进行初始化操作

NULL在C++中的定义，NULL在C++中被明确定义为整数0

	/* Define NULL pointer value */
	#ifndef NULL
	    #ifdef __cplusplus
	        #define NULL    0
	    #else  /* __cplusplus */
	        #define NULL    ((void *)0)
	    #endif  /* __cplusplus */
	#endif  /* NULL */

在C中，NULL实质上是一个void *指针

C++之所以做出这样的选择，根本原因和C++的函数重载机制有关

**NULL只是一个宏定义，而nullptr是一个C++关键字**

ullptr关键字用于标识空指针，是std::nullptr_t类型的（constexpr）变量。它可以转换成任何指针类型和bool布尔类型（主要是为了兼容普通指针可以作为条件判断语句的写法），但是不能被转换为整数。
#### C++中有三种创建对象的方法 ####
[https://www.cnblogs.com/lightmare/p/10434832.html](https://www.cnblogs.com/lightmare/p/10434832.html)
	
	int main()
	{
	    A a(1);  //栈中分配
	    A b = A(1);  //栈中分配
	    A* c = new A(1);  //堆中分配
	　　delete c;
	    return 0;
	}

栈中内存的分配和释放是由系统管理，而堆中内存的分配和释放必须由程序员手动释放

#### 虚函数与纯虚函数 ####
> 定义一个函数为虚函数，不代表函数为不被实现的函数。
> 
> 定义他为虚函数是为了允许用基类的指针来调用子类的这个函数。
> 
> 定义一个函数为纯虚函数，才代表函数没有被实现。
> 
> 定义纯虚函数是为了实现一个接口，起到一个规范的作用，规范继承这个类的程序员必须实现这个函数。

	class A
	{
	public:
	    virtual void foo()
	    {
	        cout<<"A::foo() is called"<<endl;
	    }
	};
	class B:public A
	{
	public:
	    void foo()
	    {
	        cout<<"B::foo() is called"<<endl;
	    }
	};
	int main(void)
	{
	    A *a = new B();
	    a->foo();   // 在这里，a虽然是指向A的指针，但是被调用的函数(foo)却是B的!
	    return 0;
	}

它虚就虚在所谓"推迟联编"或者"动态联编"上，一个类函数的调用并不是在编译时刻被确定的，而是在运行时刻被确定的。

虚函数只能借助于指针或者引用来达到多态的效果

纯虚函数是在基类中声明的虚函数，它在基类中没有定义，但要求任何派生类都要定义自己的实现方法。在基类中实现纯虚函数的方法是在函数原型后加 =0: