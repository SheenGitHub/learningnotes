# OpenGL ES #

#### 什么是 OpenGLES ####
OpenGLES 全称 OpenGL for Embedded Systems ，是三维图形应用程序接口 OpenGL 的子集，本质上是一个跨编程语言、跨平台的编程接口规范，主要应用于嵌入式设备，如手机、平板等。
#### 官方在线文档 ####
[https://www.khronos.org/registry/OpenGL-Refpages/es3/](https://www.khronos.org/registry/OpenGL-Refpages/es3/)

## 基础概念 ##
### 核心模式与立即渲染模式 ###
早期的OpenGL使用立即渲染模式（Immediate mode，也就是固定渲染管线），这个模式下绘制图形很方便。OpenGL的大多数功能都被库隐藏起来，开发者很少有控制OpenGL如何进行计算的自由

现代函数的优势是更高的灵活性和效率，然而也更难于学习。立即渲染模式从OpenGL实际运作中抽象掉了很多细节，因此它在易于学习的同时，也很难让人去把握OpenGL具体是如何运作的。现代函数要求使用者真正理解OpenGL和图形编程，它有一些难度，然而提供了更多的灵活性，更高的效率，更重要的是可以更深入的理解图形编程。

### 状态机 ###
OpenGL自身是一个巨大的状态机(State Machine)：一系列的变量描述OpenGL此刻应当如何运行。OpenGL的状态通常被称为OpenGL上下文(Context)。

当使用OpenGL的时候，我们会遇到一些状态设置函数(State-changing Function)，这类函数将会改变上下文。以及状态使用函数(State-using Function)，这类函数会根据当前OpenGL的状态执行一些操作。

### 程序代码实例 ###
	// 创建对象
	unsigned int objectId = 0;
	glGenObject(1, &objectId);
	// 绑定对象至上下文
	glBindObject(GL_WINDOW_TARGET, objectId);
	// 设置当前绑定到 GL_WINDOW_TARGET 的对象的一些选项
	glSetObjectOption(GL_WINDOW_TARGET, GL_OPTION_WINDOW_WIDTH, 800);
	glSetObjectOption(GL_WINDOW_TARGET, GL_OPTION_WINDOW_HEIGHT, 600);
	// 将上下文对象设回默认
	glBindObject(GL_WINDOW_TARGET, 0);

> 我们首先创建一个对象，然后用一个id保存它的引用（实际数据被储存在后台）。然后我们将对象绑定至上下文的目标位置（例子中窗口对象目标的位置被定义成GL_WINDOW_TARGET）。接下来我们设置窗口的选项。最后我们将目标位置的对象id设回0，解绑这个对象。设置的选项将被保存在objectId所引用的对象中，一旦我们重新绑定这个对象到GL_WINDOW_TARGET位置，这些选项就会重新生效。

### 顶点着色器 ###
在图元光栅化阶段，为每个生成的片段计算定点着色器输出值，并作为输入传递给片段着色器。用于从分配给每个图元定点的顶点着色器输出生成每个片段值的机制称作插值(Interpolation)

变换反馈，使顶点着色器输出可以选择性地写入一个缓冲区
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

> 先绑定纹理到对应的纹理单元，然后定义哪个uniform采样器对应哪个纹理单元：
> 注意，我们使用glform1i设置uniform采样器的位置值，或者说纹理单元。
> 通过glUniform1i的设置，我们保证每个uniform采样器对应着正确的纹理单元。

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

VBO 和 EBO 的作用是在显存中提前开辟好一块内存，用于缓存顶点数据或者图元索引数据，从而避免每次绘制时的 CPU 与 GPU 之间的内存拷贝，可以提升渲染性能，降低内存带宽和功耗。

OpenGLES3.0 支持两类缓冲区对象：顶点数组缓冲区对象、图元索引缓冲区对象。GL_ARRAY_BUFFER 标志指定的缓冲区对象用于保存顶点数组，GL_ELEMENT_ARRAY_BUFFER 标志指定的缓存区对象用于保存图元索引。

#### 纹理映射的一般步骤 ####
- 生成纹理，编译链接着色器程序
- 确定纹理坐标及对应的顶点坐标
- 加载图像数据到纹理，加载纹理坐标和顶点坐标到着色器程序
- 绘制


## VBO、EBO和VAO ##
VBO（Vertex Buffer Object）是指顶点缓冲区对象，而 EBO（Element Buffer Object）是指图元索引缓冲区对象，VAO 和 EBO 实际上是对同一类 Buffer 按照用途的不同称呼。

VAO（Vertex Array Object）是指顶点数组对象，VAO 的主要作用是用于管理 VBO 或 EBO ，减少 glBindBuffer 、glEnableVertexAttribArray、 glVertexAttribPointer 这些调用操作，高效地实现在顶点数组配置之间切换。

VAO管理一组绘制会话的属性，这些属性指向VBO、EBO所指向的缓冲区，切换一组VAO可以切换一组会话，数据缓存在显存的缓冲区，不用多次从内存读到显存，减少了数据拷贝

![](https://img-blog.csdnimg.cn/20190801184256585.png)

## FBO ##
FBO（Frame Buffer Object）即帧缓冲区对象，实际上是一个可添加缓冲区的容器，可以为其添加纹理或渲染缓冲区对象（RBO）。

FBO 本身不能用于渲染，只有添加了纹理或者渲染缓冲区之后才能作为渲染目标，它仅且提供了 3 个附着（Attachment），分别是颜色附着、深度附着和模板附着。

RBO（Render Buffer Object）即渲染缓冲区对象，是一个由应用程序分配的 2D 图像缓冲区。渲染缓冲区可以用于分配和存储颜色、深度或者模板值，可以用作 FBO 中的颜色、深度或者模板附着。

![](https://img-blog.csdnimg.cn/20190808194620511.png)

​NDK OpenGLES 开发中，一般使用 GLSurfaceView 将绘制结果显示到屏幕上，然而在实际应用中，也有许多场景不需要渲染到屏幕上，如利用 GPU 在后台完成一些图像转换、缩放等耗时操作，这个时候利用 FBO 可以方便实现类似需求。

使用 FBO 可以让渲染操作不用再渲染到屏幕上，而是渲染到离屏 Buffer 中，然后可以使用 glReadPixels 或者 HardwareBuffer 将渲染后的图像数据读出来，从而实现在后台利用 GPU 完成对图像的处理。

## EGL ##
EGL 是 OpenGL ES 和本地窗口系统（Native Window System）之间的通信接口，它的主要作用：

- 与设备的原生窗口系统通信；
- 查询绘图表面的可用类型和配置；
- 创建绘图表面；
- 在OpenGL ES 和其他图形渲染API之间同步渲染；
- 管理纹理贴图等渲染资源。

本地窗口相关的 API 提供了访问本地窗口系统的接口，而 EGL 可以创建渲染表面 EGLSurface ，同时提供了图形渲染上下文 EGLContext，用来进行状态管理，接下来 OpenGL ES 就可以在这个渲染表面上绘制。

![](https://img-blog.csdnimg.cn/20190815204323341.png)

- Display(EGLDisplay) 是对实际显示设备的抽象；
- Surface（EGLSurface）是对用来存储图像的内存区域 FrameBuffer 的抽象，包括 Color Buffer（颜色缓冲区）， Stencil Buffer（模板缓冲区） ，Depth Buffer（深度缓冲区）；
- Context (EGLContext) 存储 OpenGL ES 绘图的一些状态信息；

## 颜色 ##
定义物体的颜色为这个物体从一个光源反射各个颜色分量的多少

光源照射产生的颜色

	glm::vec3 lightColor(0.33f, 0.42f, 0.18f);
	glm::vec3 toyColor(1.0f, 0.5f, 0.31f);
	glm::vec3 result = lightColor * toyColor; // = (0.33f, 0.21f, 0.06f);

#### RGB转换成灰度图片的一个常用公式 ####
RGB转换成灰度图像的一个常用公式Gray = R*0.299 + G*0.587 + B*0.114

Grey = (R*38 + G*75 + B*15)>> 7

## 色彩编码模型 ##
### YUV ###
[https://blog.csdn.net/Kennethdroid/article/details/94031821](https://blog.csdn.net/Kennethdroid/article/details/94031821)

YUV 是一种色彩编码模型，也叫做 YCbCr，其中 “Y” 表示明亮度（Luminance），“U” 和 “V” 分别表示色度（Chrominance）和浓度（Chroma）

YUV 色彩编码模型，其设计初衷为了解决彩色电视机与黑白电视的兼容问题，利用了人类眼睛的生理特性（对亮度敏感，对色度不敏感），允许降低色度的带宽，降低了传输带宽。

YUV 编码模型的图像数据一般不能直接用于显示，还需要将其转换为 RGB（RGBA） 编码模型，才能够正常显示图像

- YUV 4：4：4，每一个 Y 分量对于一对 UV 分量，每像素占用 （Y + U + V = 8 + 8 + 8 = 24bits）3 字节
- YUV 4：2：2，每两个 Y 分量共用一对 UV 分量，每像素占用 （Y + 0.5U + 0.5V = 8 + 4 + 4 = 16bits）2 字节
- YUV 4：2：0，每四个 Y 分量共用一对 UV 分量，每像素占用 （Y + 0.25U + 0.25V = 8 + 2 + 2 = 12bits）1.5 字节

## 3D模型 ##
3D 模型的设计一般是由许多小模型拼接组合成一个完整的大模型，一个小模型作为一个独立的渲染单元，我们称这些小模型为网格（Mesh）。

网格作为独立的渲染单元至少需要包含一组顶点数据，每个顶点数据包含一个位置向量，一个法向量和一个纹理坐标，有了纹理坐标也需要为网格指定纹理对应的材质，还有绘制时顶点的索引。
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

### glDrawElements ###
从数组数据渲染primitives

	void glDrawElements(	GLenum mode,
	 	GLsizei count,
	 	GLenum type,
	 	const void * indices);

indices 指定从绑定到GL_ELEMENT_ARRAY_BUFFER的buffer开始的位移开始读取数据，如果没有指定buffer，指定一个指向存储图元索引位置的指针



### glGenVertexArrays ###
generate vertex array object names
### glBindVertexArray ###
bind a vertex array object
### glGenBuffers ###
generate buffer object names
### glBindBuffer ###
绑定一个命名缓冲对象

	void glBindBuffer(	GLenum target,
	 	GLuint buffer);

target

- GL_ARRAY_BUFFER Vertex attributes
- GL_ELEMENT_ARRAY_BUFFER Vertex array indices
- GL_TEXTURE_BUFFER Texture data buffer
### glBufferData ###
创建和初始化一个缓冲对象的数据存储

	void glBufferData(	GLenum target,
	 	GLsizeiptr size,
	 	const void * data,
	 	GLenum usage);

*target* 

GL_ARRAY_BUFFER 顶点属性
GL_ELEMENT_ARRAY_BUFFER 顶点数组indices

*usage* 

The symbolic constant must be GL_STREAM_DRAW, GL_STREAM_READ, GL_STREAM_COPY, GL_STATIC_DRAW, GL_STATIC_READ, GL_STATIC_COPY, GL_DYNAMIC_DRAW, GL_DYNAMIC_READ, or GL_DYNAMIC_COPY.

STREAM
The data store contents will be modified once and used at most a few times.

STATIC
The data store contents will be modified once and used many times.

DYNAMIC
The data store contents will be modified repeatedly and used many times.

The nature of access may be one of these:

DRAW
The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.

READ
The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.

COPY
The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.
 
### glGetError ###
返回错误，错误被检测到后，错误标志会被置为错误,glGetError返回错误，并将标志置为GL_NO_ERROR
### glGetUniformLocation ###
	GLint glGetUniformLocation(	GLuint program,
	 	const GLchar *name);

glGetUniformLocation returns an integer that represents the location of a specific uniform variable within a the default uniform block of a program object. 

This location value can then be passed to **glUniform** to set the value of the uniform variable or to **glGetUniform** in order to query the current value of the uniform variable.

### glDrawElements ###
从数组数据渲染原始图像

	void glDrawElements(	GLenum mode,
	 	GLsizei count,
	 	GLenum type,
	 	const void * indices);
### glPixelStorei ###
设置像素存储模式

	void glPixelStorei(	GLenum pname,
	 	GLint param);

*pname*

影响像素数据到内存的打包:GL_PACK_ROW_LENGTH, GL_PACK_SKIP_PIXELS, GL_PACK_SKIP_ROWS, and GL_PACK_ALIGNMENT

影响内存数据到像素的拆包:GL_UNPACK_ROW_LENGTH, GL_UNPACK_IMAGE_HEIGHT, GL_UNPACK_SKIP_PIXELS, GL_UNPACK_SKIP_ROWS, GL_UNPACK_SKIP_IMAGES, and GL_UNPACK_ALIGNMENT 

GL_PACK_ALIGNMENT:像素的对齐要求 1 (byte-alignment), 2 (rows aligned to even-numbered bytes), 4 (word-alignment), and 8 (rows start on double-word boundaries).

涉及到像素在CPU和GPU上的传输，那就有个存储格式的概念[https://www.cnblogs.com/dongguolei/p/11982230.html](https://www.cnblogs.com/dongguolei/p/11982230.html)

在OpenGL的“解释”中，纹理就是一个“可以被采样的复杂的数据集合”

#### 传输方向 ####
- PACK —— 把像素从一堆X的状态转变到规则的状态（把一堆泥土装载进一个花盆，把散散的货物装上货柜，或者把一堆各样的文件打包成一个rar压缩包，等等）；
- UNPACK —— 把像素从规则的状态转变到一堆X的状态（把花盆里的泥倒出来，把货柜中的货物卸载到盐田港，或者解压压缩包，等等）。

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
# Android 平台 #
## 绘制方法 ##
### Android Bitmap 和 ByteArray的互相转换 ###
#### Bitmap -> byte[] ####
		int bytes = bmp.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);

        byte[] byteArray = buf.array();

#### byte[] -> Bitmap ####
		// use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, type);

        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(byteArray));

        imageView.setImageBitmap(stitchBmp);
## Java ##
#### ByteBuffer.allocateDirect() ####
分配一块native存储，这块内存不会被垃圾回收期管理
#### ByteOrder.nativeOrder() ####
获取native字节序(大端机或小端机)
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

nullptr关键字用于标识空指针，是std::nullptr_t类型的（constexpr）变量。它可以转换成任何指针类型和bool布尔类型（主要是为了兼容普通指针可以作为条件判断语句的写法），但是不能被转换为整数。
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