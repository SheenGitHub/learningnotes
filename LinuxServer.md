
/etc/services 应用层服务端口名
/etc/resolv.conf  DNS服务器的IP地址

## 概念说明 ##
### 用户空间和内核空间 ###

> 现在操作系统都是采用虚拟存储器，那么对32位操作系统而言，它的寻址空间（虚拟存储空间）为4G（2的32次方）。操作系统的核心是内核，独立于普通的应用程序，可以访问受保护的内存空间，也有访问底层硬件设备的所有权限。为了保证用户进程不能直接操作内核（kernel），保证内核的安全，操心系统将虚拟空间划分为两部分，一部分为内核空间，一部分为用户空间。针对linux操作系统而言，将最高的1G字节（从虚拟地址0xC0000000到0xFFFFFFFF），供内核使用，称为内核空间，而将较低的3G字节（从虚拟地址0x00000000到0xBFFFFFFF），供各个进程使用，称为用户空间。

### 进程切换 ###

从一个进程的运行转到另一个进程上运行，这个过程中经过下面这些变化：

- 1. 保存处理机上下文，包括程序计数器和其他寄存器。
- 2. 更新PCB信息。
- 3. 把进程的PCB移入相应的队列，如就绪、在某事件阻塞等队列。
- 4. 选择另一个进程执行，并更新其PCB。
- 5. 更新内存管理的数据结构。
- 6. 恢复处理机上下文。

### 进程的阻塞 ###

进程的阻塞是进程自身的一种**主动行为**，也因此**只有处于运行态**的进程（获得CPU），才可能将其转为阻塞状态。当进程进入阻塞状态，是**不占用CPU**资源的

### 文件描述符fd ###
> 用于表述指向文件的引用的抽象化概念

它是一个索引值，指向内核为每一个进程所维护的该进程打开文件的记录表

### 缓存 I/O ###

> 缓存 I/O 又被称作标准 I/O

操作系统会将 I/O 的数据缓存在文件系统的页缓存（ page cache ）中

数据会先被拷贝到操作系统内核的缓冲区中，然后才会从操作系统内核的缓冲区拷贝到应用程序的地址空间。

**缓存 I/O 的缺点：**
数据在传输过程中需要在应用程序**地址空间和内核**进行多次数据拷贝操作，这些数据拷贝操作所带来的 CPU 以及内存开销是非常大的。

Select, Poll, Epoll对比

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fkfjcjrmkxj20r80d7te5.jpg)

Libevent 支持的事件类型

![](http://ww1.sinaimg.cn/mw690/48ceb85dgy1fkvngd2h81j20gq06ujso.jpg)

# C语言 #
字符串、内存复制 
strcpy,strncpy, memcpy

### gets不安全 ###

> //使用gets()
>  
> char buffer[4096];
>  
> gets(buffer);
>  
> //使用fgets()替换gets()
>  
> char buffer[4096];
>  
> fgets(buffer, (sizeof buffer / sizeof buffer[0]), stdin);

## Q&A ##
Q:undefined reference to 'pthread_create'
A:pthread不是Linux下的默认的库,在gcc编译的时候，附加要加 -lpthread参数即可解决 （mmap 使用 -lrt）

Q:./helloworldlibevent: error while loading shared libraries: libevent-2.1.so.6: cannot open shared object file: No such file or directory
A:sudo ln -s /usr/lib/libevent-2.1.so.6 /usr/lib64/libevent-2.1.so.6

Q:fork是用来创建进程的，子进程复制与父进程完全一样代码，但是与pipe连用时，也要复制pipe吗？个自往自己管道也东西，读东西，是如何实现进程间通信的呢？

A:
> fork()不仅创建出与父进程代码相同的子进程，而且父进程在fork执行点的所有上下文场景也被自动复制到子进程中，包括：
> —全局和局部变量
> —打开的文件句柄
> —共享内存、消息等同步对象
>   由于pipe调用相当于创建了2个文件句柄，因此在fork后这两个句柄也被自动复制到子进程中，对这两个句柄的操作效果与在主进程中的操作效果是一致的，这就使父子进程之间很容易通过该机制实现数据交换，如：
>   假设pipe产生的句柄为P[0],P[1]，在fork后父子进程都拥有了P[0],P[1]句柄，那么：
> —父进程可向自己的P[1]中写入数据，子进程从P[0]中即可读出该数据；切记此时父进程不能也从P[0]读数据，否则自己写入的数据可能先被自己读走了
> —反之亦然，子进程向P[1]中写入数据后，父进程从P[0]中可读出该数据；切记此时子进程不要从P[0]读走数据
>   你可能难以理解为什么进程内部的数据读写会被传递到另一个进程，但别忘了，pipe匿名管道和文件，socket等一样是属于操作系统的管理对象，对其进行读写都是由OS的内核代码来进行控制的。在父进程调用pipe创建出管道后，该管道对象是存储在OS内部的，父进程得到的P[0]和P[1]都只是对该对象的引用（相当于指针）；在fork出子进程后，子进程复制出的也只是同一个指针，所指向的还是OS中的同一个对象，这就是为什么父子进程能通过其进行互相通信的原因。

Q:管道命令和xargs的区别

A:管道符后不加xargs相当于先将xargs后面的命令回车执行一下再从键盘里输入
管道符前面命令执行的结果内容
加上xargs 相当于直接从键盘输入管道符前面命令执行的结果内容再回车

Q:scanf()函数如何才能输入带空格的字符串

A:scanf("%[^\n]", a); 


#### 批处理命令 ####

安装java

rpm -qa|grep java|xargs -I {} rpm -e --nodeps {} 

rpm -ivh jdk-8u151-linux-x64.rpm 

vim /etc/profile

> export JAVA_HOME=/usr/java/jdk1.8.0_151
> 
> export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
> 
> export PATH=.:$PATH:$JAVA_HOME/bin

source /etc/profile

#### 安装tomcat ####

[安装tomcat](http://www.itzgeek.com/how-tos/linux/centos-how-tos/install-apache-tomcat-8-on-centos-7-rhel-7.html)

groupadd tomcat
useradd -g tomcat -d /opt/tomcat -s /bin/nologin tomcat

tar -zxvf apache-tomcat-*.tar.gz
mv apache-tomcat-8.5.20/* /opt/tomcat/

chown -R tomcat:tomcat /opt/tomcat/

**需要开启iptables**

/etc/sysconfig/iptables

> -A INPUT -p tcp -m state --state NEW -m tcp --dport 80 -j ACCEPT

#### 安装iptables ####
- yum install iptables-services
- systemctl enable iptables
- systemctl start iptables
- service iptables save


#### tar命令 ####

- -A或--catenate：新增文件到以存在的备份文件； 
- -B：设置区块大小； 
- -c或--create：建立新的备份文件； 
- -C <目录>：这个选项用在解压缩，若要在特定目录解压缩，可以使用这个选项。 
- -d：记录文件的差别； 
- -x或--extract或--get：从备份文件中还原文件； 
- -t或--list：列出备份文件的内容； 
- -z或--gzip或--ungzip：通过gzip指令处理备份文件；
-  -Z或--compress或--uncompress：通过compress指令处理备份文件； 
- -f<备份文件>或--file=<备份文件>：指定备份文件； 
- -v或--verbose：显示指令执行过程； 
- -r：添加文件到已经压缩的文件； 
- -u：添加改变了和现有的文件到已经存在的压缩文件； 
- -j：支持bzip2解压文件； 
- -v：显示操作过程； 
- -l：文件系统边界设置； 
- -k：保留原有文件不覆盖； 
- -m：保留文件不被覆盖； 
- -w：确认压缩文件的正确性； 
- -p或--same-permissions：用原来的文件权限还原文件； 
- -P或--absolute-names：文件名使用绝对名称，不移除文件名称前的“/”号； 
- -N <日期格式> 或 --newer=<日期时间>：只将较指定日期更新的文件保存到备份文件里； 
- --exclude=<范本样式>：排除符合范本样式的文件。

#### 更新系统时间 ####
ntpdate -u ntp.api.bz

#### 设置时区 ####
tzselect

#### 查看系统版本 ####
- uname -a
- cat /proc/version
- cat /etc/issue


#### tomcat关闭 connection refused ####
修改 java*_*/jre/lib/security/java.security


## 问题备注 ##

rm -rf /xxx/xxx 删除符号链接

rm -rf /xxx/xxx/ 删除实际目录

make && make install

yum install mysql 安装的是mariaDB

#### 下载rpm包安装MySQL ####

    wget http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
    rpm -ivh mysql-community-release-el7-5.noarch.rpm
    yum install mysql-community-server
    service mysqld restart

mysql.service unit not found

/etc/init.d/mysql start 启动Mysql

Mysql-server 和 Mysql-client之间的区别

#### 安装MariaDB ####
    yum install mysql
    yum install mariadb-server mariadb
    yum install mysql-devel

设置防火墙

    iptables -I INPUT -p tcp --dport 3306 -m state --state NEW,ESTABLISHED -j ACCEPT
    iptables -I OUTPUT -p tcp --sport 3306 -m state --state ESTABLISHED -j ACCEPT

允许远程访问Mysql

grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
flush privileges;

**ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/var/lib/mysql/mysql.sock' (111)**

重装mysql 

Mysql5.1 默认密码123456

#### Too many levels of symbolic links  ####
使用绝对路径建立软链接，建立软软链接是采用相对路径的

#### mysql输出的错误提示是法语 ####
mysql.ini 文件里头有一个参数叫做 lc-messages
设成 lc-messages=en_US

# Shell #
## awk ##
[三十分钟学会AWK](http://blog.jobbole.com/109089/)
### 第N个参数 ###
$0 全行 $N 第N个参数

批量添加 svn add

*svn status 结果*

??  filename.ext

    svn status|awk 'print $2'|xargs -I {} svn add {}

### 常用参数 ###
- FILENAME 当前文件名
- FS 字段分隔符
- NF 字段数目
- NR 行号

### 运算操作 ###
算数操作

    $ awk 'BEGIN { a = 50; b = 20; print "(a + b) = ", (a + b) }'

数组操作

    $ awk 'BEGIN { 
       arr[0] = 1; arr[1] = 2; arr[2] = 3; for (i in arr) printf "arr[%d] = %d\n", i, arr[i]
    }'

正则操作

    $ echo -e "Apple Juice\nApple Pie\nApple Tart\nApple Cake" | awk '/Apple (Juice|Cake)/'
    Apple Juice
    Apple Cake
# Spring boot #
#Q&A#
### 如何嵌套解析XML ###
使用JAXB，嵌套元素使用

    @XmlElementWrapper

根元素使用

    @XmlRootElement(name ="xml")

元素使用

    @XmlElement

CDATA元素转换使用

    @XmlJavaTypeAdapter(value= AdapterCDATA.class)

**在转换时去掉getter方法，去除条目的重复**

#### 阻止JAXB的转义 ####

    m.setProperty("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler",
    
    new CharacterEscapeHandler(){
        @Override
        public void escape(char[] ch, int start,
           int length, boolean isAttVal,
           Writer writer) throws IOException
        {
            writer.write(ch, start, length);
        }
        });

#### XML 文档结构必须从头至尾包含在同一个实体内 ####
XML 格式不对

### POST中的查询路径 ###
在方法的参数中使用 

    @RequestParam("timestamp") String timestamp

### maven 打包时，程序包com.sun.xml.internal.bind.v2.xxxx不存在 ###
因为使用了jdk内部的类库，在pom中加入以下对jre中类库的引用

    <plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-compiler-plugin</artifactId>
		<configuration>
			<source>1.8</source>
			<target>1.8</target>
			<encoding>UTF-8</encoding>
			<compilerArguments>
				<bootclasspath>${java.home}/lib/rt.jar;${java.home}/lib/jce.jar</bootclasspath>
			</compilerArguments>
		</configuration>
	</plugin>

### Ambiguous mapping ###
在RequestMapping中使用了Method = GET ，导致正常post可以获得GET的请求，出现歧义

## Mybatis ##
### 测试 ###
在测试中正常使用Mapper接口即可

    @Resource
    private UserDao userDao;



## Reactor-Flux ##
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqnpsbjq4cj212i0pkdwp.jpg)
![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqnpwsjbolj20v00i6n36.jpg)

# 面向对象设计原则 #
## 单一职责原色 ##
SRP,Single Responsibility Priciple

> 一个类应该仅有一个引起它变化的原因

职责的定义：变化的原因(a reason for change)

软件的首要技术失明————管理复杂度，而找出容易变化的区域，隔离变化，就是一种很好的管理复杂度的启发方法

### 分层 ###
1. UserManageService

    	public interface UserManageService {  
      
		    public ResultMsg create(Integer tokenId, String sign, User user);  
		      
		    public ResultMsg delete(Integer tokenId, String sign, Integer userId);  
		      
		    public ResultMsg update(Integer tokenId, String sign, User user);  
		      
		    public ResultMsg get(Integer tokenId, String sign, Integer userId);  
      
    	}  

2. UserDao接口

	    public interface UserDao{  
	      
		    public boolean add(User user);  
		      
		    public boolean delete( Integer userId);  
		      
		    public boolean update(User user);  
		      
		    public User get(Integer userId);  
	      
	    } 

3. Service的实现

		@Service("userManageService")  
  
		public class UserManageServiceImpl implements UserManageService {  
		  
			@Resource(name="UserDaoImpl")  
			  
			private UserDao<User> userDao;  
			  
			……  
			  
			@Override  
			  
			public ResultMsg create(Integer tokenId, String sign, User user) {  
			  
				…..  
				  
				//处理完业务后，判断是否需要调用UserDao接口的相应实现  
				  
				userDao.add(user);  
				  
				……  
			  
			 }  
			  
			 …..  
		  
		}

## 开放-封闭原则 ##
OCP, Open-Closed Principle

软件实体(类，模块，函数等)应该是可以扩展的，但是不可修改的

> 由于模块依赖一个固定的抽象体，所以它对于更改可以是封闭的。
> 
> 同时，通过这个抽象体派生，也可以扩展次模块的行为
> 
> 使用接口抽象，创建多个实现，彼此多态地替换
> 
> 使用抽象接口来调解客户端对原始类的访问，可以通过同一接口访问新类实现新功能

### 刺激变化 ###
因为变化发生（发现）越早、越快就越有利；刺激变化的一些方法：

      1、首先编写测试。

      2、使用很短的迭代周期进行开发。

      3、经常把开发特性展示给涉众。

      4、首先开发最重要的特性。

      5、尽早地、经常性地发布软件。尽可能频繁地把软件展示给客户和使用人员，得到他们的反馈。

**仅仅对程序中出现频繁变化的那些部分作出抽象，而拒绝不成熟的抽象**

## 里氏替换原则 ##
LSP, Liskov Substitution Priciple

派生类型必须能够替换掉它们的基类型

> 这里需要如下的替换性质：若对类型S的每一个对象O1,都存在一个类型T的对象O2，使得在所有针对T编写的程序P中，用O1替换O2后，程序P的行为功能不变，则S是T的子类型 ———— Barbara Liskov

Andy Hunt和Dave Thomas总结

> 派生类必须能通过基类的接口而被使用，且使用者无须了解两者之间的差异

 **除非派生类是一个"更特殊"的类，否则不应该从基类继承**

- 基于契约设计（Design By Contract，DBC） 
- 前置后置条件  

**子类必须接受父类可以接受的一切。父类的用户不应被所使用的子类的输出扰乱**

## 接口隔离原则 ##
ISP, Interface Segregation Principle

不应该强迫用户依赖于它们不用的方法

适应适配器分离转换接口

> 使用依赖性反转原则添加了Job类和其客户端之间的接口层。（适配器）
> 
> 创建了一个装订作业的Staple接口和一个打印作业的Print接口，并分别由Staple类和Print类使用，用来调用Job类的相关方法。
> 
> 这样就分离了Job类到两个类型的接口中。


 ISP与SRP的区别是：

- ISP主要针对接口，SRP主要针对类； 
-  ISP主要针对客户、SRP主要针对职责；
-  ISP是以客户需求/类型来分离接口的，

ISP与SRP的联系是：

- 一个类可能需要实现多个接口，这会产生职责耦合；
- 但分离多个接口就是一种解耦表现，一个接口相关职责的变化一般不会引起其他接口的变化。

## 依赖倒置原则 ##
DIP,Dependency Inversion Principle

a. 高层模块不应该依赖于底层模块，两者应该依赖于抽象

b. 抽象不应该依赖于细节；细节应该依赖于抽象

![](http://ww1.sinaimg.cn/large/48ceb85dgy1fqpv8w79k5j20qp06r74p.jpg)

### 实现 ###
#### 基础：依赖于抽象 ####

> 程序中所有的依赖关系都应该终止于抽象类或者接口中；
> 而不应该依赖于具体类。

（1）、类中的所有成员变量必须是接口或抽象，不应该持有一个指向具体类的引用或指针。

 即所有具体类只能通过接口或抽象类连接。

 （3）、任何类都不应该从具体类派生。

 （4）、任何方法都不应该覆写它的任何基类中已经实现的方法。（里氏替换原则）

 （5）、任何变量实例化都需要实现创建模式（如：工厂方法/模式），或使用依赖注入框架（如：Spring IOC）。

#### 核心：依赖倒置 ####
由客户类来声明它们需要的服务接口，改变实现抽象接口的类就不会影响到客户了

#### DIP在系统架构中的应用 ####
1、数据存储

 传统：

 业务层在不同数据类型的CURD地方，直接调用不同数据系统（Mysql/Mongo/Rides缓存/本地文件系统）的相关接口，把数据保存到相应系统。

 DIP：

 抽象数据存储层（DAO），DAO模块实现不同数据系统的接口；

 业务层调用DAO层接口传入不同类型数据，DAO层适配到相应数据系统。

 2、消息传输

 通常：

 上层通过调用下层接口，发消息给下层。

 一般情况上层需要同步等待下层处理后的响应。

 DIP：

 通过消息队列（Message Queue）解耦，实现异步传输；

 上层调用MQ的发送消息接口，消息发送到MQ，下层调用MQ获取消息的接口进行消费；

 这时上/下层都可以轻松进行扩展。

 注意：如果上层实时关注下层的处理结果，MQ就不适用。

 3、配置/服务注册中心

 传统：

 下层服务配置保存在配置文件或数据库，改动一些配置或扩展下层服务时可能影响到上层服务；

 如：

 上层服务依赖下层服务的IP、RPC服务地址等等。

 DIP：

 独立出来配置/服务注册中心；

 当下层服务配置改变时，更新到配置心中；配置中心把更新推送到上层服务。

 如：

 针对IP，可以通过DNS，上层使用的是域名，下层服务IP更新时，只需在DNS改动记录指向新的IP。

 针对RPC服务地址，通过服务注册中心（如：ZooKeeper）来发布；上层通过注册中心接口订阅所需服务，当下层通过注册中心接口扩展新注册服务或更新服务地址时，上层服务会收到订阅的服务更新情况。

## 迪米特法则 ##
LOD, Law of Demeter
最少知道原则

只与直接的朋友通信(Only talk to your immediate friends)

1. 从被依赖者的角度，只应该暴露应该暴露的方法
2. 从依赖者的角度来说，只依赖应该依赖的对象

## 组合/聚集复用原则 ##
CARP, Composite/Aggregate Reuse Principle

合成复用原则是指尽量使用合成/聚合，而不是使用继承

 继承复用通过扩展一个已有对象的实现来得到新的功能，基类明显地捕获共同的属性和方法，而子类通过增加新的属性和方法来扩展父类的实现。继承是类型的复用。