# Linux命令 #

### rsync ###
rsync -zvr /var/opt/installation/inventory/ /root/temp

- -z 打开压缩功能
- -v verbose更多打印信息
- -r recursive
- --existing 不创建新文件
- --exclude 排除
- --delete 删除destination
- --progress 传输速度
- -u 不覆盖destination


/etc/services 应用层服务端口名
/etc/resolv.conf  DNS服务器的IP地址

# LInux系统 #
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

Q:packet_write_wait: Connection to x.x.x.x port 22: Broken pipe

A:Unresolved

Q:ssh登录报ssh_exchange_identification: read: Connection reset by peer

A:cd /var 
chmod -R 755 *



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

启动 停止 重启 MariaDB

      systemctl start mariadb.service #启动MariaDB

      systemctl stop mariadb.service #停止MariaDB
      systemctl restart mariadb.service #重启MariaDB
      systemctl enable mariadb.service #设置开机启动

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


# CentOS7 防火墙操作 #

CentOS7使用firewalld打开关闭防火墙与端口

1、firewalld的基本使用

- 启动： systemctl start firewalld
- 查看状态： systemctl status firewalld 
- 停止： systemctl disable firewalld
- 禁用： systemctl stop firewalld
 
2.systemctl是CentOS7的服务管理工具中主要的工具，它融合之前service和chkconfig的功能于一体。

- 启动一个服务：systemctl start firewalld.service
- 关闭一个服务：systemctl stop firewalld.service
- 重启一个服务：systemctl restart firewalld.service
- 显示一个服务的状态：systemctl status firewalld.service
- 在开机时启用一个服务：systemctl enable firewalld.service
- 在开机时禁用一个服务：systemctl disable firewalld.service
- 查看服务是否开机启动：systemctl is-enabled firewalld.service
- 查看已启动的服务列表：systemctl list-unit-files|grep enabled
- 查看启动失败的服务列表：systemctl --failed

3.配置firewalld-cmd

- 查看版本： firewall-cmd --version
- 查看帮助： firewall-cmd --help
- 显示状态： firewall-cmd --state
- 查看所有打开的端口： firewall-cmd --zone=public --list-ports
- 更新防火墙规则： firewall-cmd --reload
- 查看区域信息:  firewall-cmd --get-active-zones
- 查看指定接口所属区域： firewall-cmd --get-zone-of-interface=eth0
- 拒绝所有包：firewall-cmd --panic-on
- 取消拒绝状态： firewall-cmd --panic-off
- 查看是否拒绝： firewall-cmd --query-panic
 
那怎么开启一个端口呢

- 添加
- firewall-cmd --zone=public --add-port=80/tcp --permanent    （--permanent永久生效，没有此参数重启后失效）
- 重新载入
- firewall-cmd --reload
- 查看
- firewall-cmd --zone= public --query-port=80/tcp
- 删除
- firewall-cmd --zone= public --remove-port=80/tcp --permanent


# C/C++ #

## 指针 ##
### 指向类成员函数的函数指针 ###
基本上要注意的有两点：

①函数指针赋值要使用 &

②使用.*(实例对象)或者->*（实例对象指针）调用类成员函数指针所指向的函数

	A a;
	B b;
	void (A::*ptr)(int) = &A::setA;
	A* pa = &a;
	(pa->*ptr)(1000);
	(a.*ptr)(10000);



## 运算符重载 ##
->和* 运算符重载

	Image* ImagePtr::operator->(){
		return LoadImage();
	}
	
	Image& ImagePtr::operator* (){
		return *LoadImage();
	}
	
	ImagePtr image = ImagePtr("anImageFileName");
	image->Draw(Point(50, 100));
	//(image.operator->())->Draw(Point(50, 100))

成员访问运算符和间接引用运算符的重载

## 虚函数 ##
包含纯虚函数的类称为抽象类

### 虚函数表 ###

![](http://ww1.sinaimg.cn/large/48ceb85dly1g19b7q057lj20bo04tq34.jpg)

## 模板 ##
	template <class Receiver>
	class SimpleCommand:public Command{
	public:
		typedef void (Receiver::* Action)();
		
		SimpleCommand(Receiver* r, Action a) :
			_receiver(r), _action(a){}
		
		virtual void Execute();
	private:
		Action _action;
		Receiver* _receiver;
	};
	
	template <class Receiver>
	void SimpleCommand<Receiver>::Execute(){
		(_receiver->*action)();
	}
	
	MyClass* receiver = new MyClass;
	
	Command* aCommand = new SimpleCommand<MyClass>(receiver, &MyClass::Action);
	
	aCommand->Execute();

命令模式中简单命令的应用

## 左值引用与右值引用 ##
*左值引用在汇编层面其实和普通的指针是一样的*

C++对于左值和右值没有标准定义，但是有一个被广泛认同的说法：

可以取地址的，有名字的，非临时的就是左值；
不能取地址的，没有名字的，临时的就是右值；

**使用右值的拷贝构造函数，通过引用临时值右值的数据，减少一次数据拷贝**

临时量都会自动匹配右值引用版本的成员方法，旨在提高内存资源使用效率。

带右值引用参数的拷贝构造和赋值重载函数，又叫移动构造函数和移动赋值函数

## 浅拷贝与深拷贝 ##
### 拷贝构造函数 ###
当定义一个新对象并用一个同类型的对象对它进行初始化时，将显示使用拷贝构造函数。当该类型的对象传递给函数或从函数返回该类型的对象时，将隐式调用拷贝构造函数

#### 必须定义复制构造函数的情况 ####
只包含类类型成员或内置类型（但不是指针类型）成员的类，无须显式地定义复制构造函数也可以复制；有的类有一个数据成员是指针，或者是有成员表示在构造函数中分配的其他资源，这两种情况下都必须定义复制构造函数。

## const ##
①：構造函數和析構函數不可以是常函數

②：常函數不能對class的類成員進行修改（只能調用）如下面是不可以的

常對象只能調用常對象

## explicit ##
explicit 最基本的用法：避免隱式轉換

## static_cast ##
static_cast is used for cases where you basically want to reverse an implicit conversion, with a few restrictions and additions. static_cast performs no runtime checks. This should be used if you know that you refer to an object of a specific type, and thus a check would be unnecessary.

(dynamic_cast)必须要有虚函数才能进行转换，static_cast静态转换，不安全。）

运行时类型信息通过运算符dynamic_cast来提供。dynamic_cast用来向下转型，将基类指针转换为派生类指针.（把基类指针转换为派生类指针）向下转型,向上转型的话就用强制转换。

## 容器 ##
### Vector ###
#### emplace_back ####
添加一个新元素到结束的容器。该元件是构成在就地，即没有复制或移动操作进行
# 设计模式 #
## 创建型模式 ##

### 抽象工厂模式 ###
#### 意图 ####
提供一个创建一系列相关或互相依赖对象的接口，而无需指定它们具体的类
#### 动机 ####
一个应用不应该为一个特定的实感外观硬编码它的窗口组件

### 对象创建型模式 ###
#### 意图 ####
将一个复杂对象的构建与它的表示分开，使得同样的构建过程可以创建不同的表示。
#### 动机 ####
一个RTF文档交换格式的阅读器能将RTF转换为多种正文格式

TextConvert的子类对不同的转换和不同格式进行特殊处理
#### 效果 ####
使得生成器可以隐藏这个产品的表示和内部结构，客户不需要知道定义产品内部结构的类的所有信息

### 原型模式 ###
#### 意图 ####
用原型示例指定创建对象的种类，并且通过拷贝这些原型创建新的对象

## 结构型模式 ##

### 代理模式 ###
#### 意图 ####
为其他对象提供一种代理以控制对这个对象的访问
#### 动机 ####
对一个对象进行访问控制的一个原因是为了只有在我们确实需要这个对象时才对它进行创建和初始化。
#### 相关模式 ####
Decorator 为对象添加一个或多个功能，但代理则控制对对象的访问。

Remote Proxy不包含对实体的直接引用，而只是一个间接的引用，Virtual Proxy开始的时候使用一个间接引用，例如一个文件名

### 享元模式 ###
#### 意图 ####
运用共享技术有效地支持大量细粒度的对象
#### 动机 ####
Flyweight模式对那些因为数量太大而难以用对象表示的概念和实体进行建模

### 桥接模式 ###
#### 意图 ####
将抽象部分与它的实现部分分离，使它们都可以独立地变化
#### 动机 ####
客户在创建窗口时不应该设计到具体的实现部分

### 外观模式 ###
#### 意图 ####
为子系统中的一组接口提供一个一致的界面，Facade模式定义了一个高层接口，这一接口是的这一子系统容易使用。
#### 动机 ####
将一个系统划分成若干个子系统有利于降低系统的复杂性。
#### 效果 ####
Facade模式可以消除复杂的循环依赖关系
#### 相关模式 ####
抽象工厂模式 可以与Facade模式一起使用以提供一个接口，这一接口可以用来以一种子系统独立的方式创建子系统对象。

中介者模式 抽象一些已有类的功能。 Mediator的目的是对同事之间的任意通讯进行抽象。Facade对子系统对象的接口进行抽象，从而使它们更容易使用。

## 行为模式 ##

### 命令模式 ###
#### 意图 ####
将一个请求封装为一个对象，从而可以用不同的请求对客户进行参数化
#### 动机 ####
只有工具箱对象的应用知道该由哪个对象做哪个操作，而工具箱的设计者无法知道请求的接受者或执行的操作。

#### 实用性 ####
Command模式是回调机制的一个面向对象的替代品，可以封装一组动作

#### 效果 ####
Command模式将调用操作的对象与知道如何实现该操作的对象解耦

### 中介者模式 ###
#### 意图 ####
用一个中介对象来封装一系列的对象交互。中介者使各个对象不需要显示地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。
#### 动机 ####
中介者负责控制和协调一组对象之间的交互。充当窗口组件间通信的中转站。
#### 协作 ####
同事向一个中介者对象发送和接收请求。中介者各同事之间适当地转发以实现协作行为
#### 效果 ####
中介者模式将交互的复杂性变为中介者的复杂性。

# Git #
## SSH到HTTPS 切换 ##
git remote set-url origin(远程仓库名称) https://email/username/ProjectName.git

git remote set-url origin git@email:username/ProjectName.git

git remote -v

git config --global http.sslVerify false

# Q&A #
#### 退出控制台不挂断运行后台程序 ####
nohup java -jar spring-boot01-1.0-SNAPSHOT.jar > log.file 2>&1 &

#### 关闭后台进程脚本 ####
	#!/bin/bash
	PID=$(ps -ef | grep complaint.jar | grep -v grep | awk '{ print $2 }')
	if [ -z "$PID" ]
	then
	    echo Application is already stopped
	else
	    echo kill $PID
	    kill $PID
	fi

#### Nginx监听8880端口，但是浏览器无法访问 ####
防火墙没有打开

# Vim实用技巧 #
\. 重复之前的动作 重复之前的修改

\>G 当前行到文档末尾的缩进层级 

\> 之后添加缩进范围

; 重复上一次的查找

s 删当前字符进入插入模式

#### 回退 ####
u修改回退 ，查找回退 N匹配回退
#### 替换 ####
%s/content/copy/g

#### 单词周围 ####
daw diw

<C-a>数字加 <C-x>数字减
#### 能重复就别用次数 ####
d3w dw.

#### 操作符+动作命令 ####
dl字符 daw完整单词 dap段落

gUaw 单词大写

#### 缓冲区切换 ####
bnext bprev blast bfirst

bd 删除缓冲区 不影响文件

#### 缓冲区操作 ####
wa写入所有 a e回滚 qa退出所有放弃修改

#### 打开新窗口 ####
<C-w>s <C-w>v sp vsp

<C-w>w切换 <C-w>h <C-w>j <C-w>k <C-w>l 

<C-w>c 关闭 <C-w>o 关闭其它

<C-w>= 等高等宽 <C-w>_最大化高度 <C-w>|最大化宽度 [N]<C-w>_ 设为N行

#### 标签页 ####
tabedit tabclose关闭当前 tabonly关闭其它

{N}gt在标签间切换 gT反方向

tabn tabn tabp 切换前后标签

tabmove移动当前标签符号
#### 插入-普遍模式 ####
<C-o> 在插入模式中切换，可以执行一次普遍模式命令

### 插入模式 ###
<C-r>0 寄存器0中的数据粘贴

<C-r><C-p>{register} 按原义插入

<C-r>=表达式寄存器

#### 插入字符 ####
<C-v>065 <C-v>u{1234}十六进制 <C-k>二合字母 13三分之一

#### 替换模式 ####
R

#### 制表符宽度 ####
set tabstop=4

#### 虚拟替换模式 ####
gR 按屏幕实际显示的宽度来替换字符

r{cahr}单词替换 gr覆盖一个字符
### 可视模式 ###
viw单词 c 删除单词进入插入模式

- v面向字符
- V面向行
- <C-v>面向列块
- gv重选上次选区

o 切换选区活动端
#### 切换选择模式 ####
<C-g>

#### 多次缩进 ####
Vj >.

#### 选择标签内的内容 ####
vit U 变成大写

#### 替换行或列 ####
- <C-v>3j r|
- Vr-

#### 行尾插入 ####
<C-v>jj$ A;<Esc>

### 命令行模式 ###
- 1 文件第一行
- $ 文件最后一行
- 0 虚拟行
- . 光标所在行
- 'm 包含位置标记m的行
- '< 高亮选区的起始行
- '> 高亮选区的结束行
- % 整个文件 1,$的简写形式

#### 复制和移动 ####
copy(:t) move(:m)

t. 和yyp的区别 t. 不会覆盖默认寄存器

#### 对选区每个行执行普通模式 ####
:'<,'>normal .

:%normal A; 对所有的行添加；

#### 执行上一条ex命令 ####
@: 之后使用@@

#### 补全Ex命令 ####
<C-d>

#### 补全单词 ####
<C-n>

#### 替换光标下的单词 ####
%s//<C-r><C-w>/g

//的内容即当前选中的单词，<C-r><C-w>显示当前光标下的单词

<C-r><C-a>插入光标下的字串，包括非字母字符


#### 回溯历史命令 ####
<Up\>,<Down\>

/之后up或down会遍历查找记录

<C-n> <C-p>

### 命令窗口 ###
q: <Cr>可执行 也可以合并编辑命令

#### 执行shell命令 ####
！{cmd}

#### 挂起Vim ####
<C-z>挂起 Vim， fg [n] 调到前台

read !{cmd}把执行命令结果写到当前缓冲区

write !{cmd} 把当前缓冲区作为输入传递给cmd

write！ {cmd} 会把当前内容覆盖名为{cmd}的文件

#### 设置find路径 ####
set path+=app/** 可以查找app下所有路径

#### 临时新建目录 ####
:!mkdir -p %:h

%:h<Tab> 会被展开为当前文件所在目录的路径

#### 超级用户权限使用命令 ####
w !sudo tee % > /dev/null

触发sudo权限

### 移动 ###

h j k l

w b e ge 基于单词

W B E GE 基于字符
#### 按屏幕移动 ####
gj 和gk 则是按屏幕行向下及向上移动

#### 在单词尾添加 ####
ea 命令连在一起可被解读为“在当前单词结尾后添加”，gea在上一个单词尾添加

#### 范围 ####
iw iW is ip

i) i} i] i> i' i"" i` at 分割对象


#### 标记 ####
m{a-zA-Z}

`{a-zA-Z}跳到标记

%括号跳动
#### 自动标记 ####
- `` 当前文件中上次跳转动作之前的位置
- `. 上次修改的地方
- `^ 上次插入的地方 上一次退出插入模式的位置
- `[ 上次修改或复制的起始位置
- `] 上次修改或复制的结束位置
- `< 上次高亮选区的起始位置
- `> 上次高亮选区的结束位置

#### 跳动 ####
<C-o>后退 <C-i>（同Tab）前进，在文件和修改间跳动

- H/M/L 跳到屏幕最上方/正中间/最下方
- gf 跳转到光标下的文件名
- <C-]> 跳转到光标下关键字的定义之下

g; g, 遍历修改位置

gi 退出的地方继续编辑


vimgrep /fooBar/ ** 查找所有fooBar

#### 交换单词 ####
- xp 交换单词
- ddp 交换行

#### 复制专用寄存器 ####
复制专用寄存器，顾名思义，仅当使用y{motion} 命令时才会被赋值。换句话讲，使用x、s、c{motion} 以及d{motion} 命令均不会覆盖该寄存器

diw仍会覆盖无名寄存器

"_diw 不会覆盖任何寄存器

加号寄存器，与系统剪切板等效 "+p 插入模式下用<C-r>+

"* 主剪切板

#### 其他寄存器 ####
- "% 当前文件名
- "# 轮换文件名
- ". 上次插入的文本
- ": 上次执行的Ex 命令
- "/ 上次查找的模式

#### 外部粘贴问题 ####
set paste

set paste! 取消设置

set pastetoggle=<f5> 设置f5切换paste选项

#### 录制宏 ####
q{register} 录制宏 q结束

@{register}执行宏

@@重复最近的宏

宏中使用 0f. 查找失败宏就会直接结束

#### 并行执行宏 ####
jVG选择所有行 :normal 切换命令行模式 在每一行上执行

qa覆盖操作到a寄存器中

qA在原有的a寄存器之后添加录制

#### 对文件列表执行宏 ####
argdo 命令允许我们对参数列表内的所有缓冲区执行一条Ex 命令

先用edit！取消对当前文件的修改

argdo write 可保存参数列表中的全部文件

:wall 会更快

args *.rb 显示所有参数列表的内容

#### 可变量 ####
:let i=0

:echo i

插入数字到行首

	:let i=1
	qa
	|<C-r>=i<CR>)<Esc>
	:let i+=1
	q

jvG :normal @a 在每一行上执行Vim命令

#### 编辑寄存器 ####
"add将内容删除到a寄存器，然后用@a去执行

#### 正则搜索 ####
如果启用‘ignorecase’设置，Vim 的查找模式将不区分大小写

通过使用元字符\c 与\C，可以覆盖Vim缺省的大小写敏感性设置。小写字母\c会让查找模式忽略大小写，而大写字母\C 则会强制区分大小写。

‘smartcase’选项。该选项被启用后，无论何时，只要我们在查找模式中输入了大写字母，‘ignorecase’设置就不再生效了。

\v将会激活verymagic 搜索模式，即假定除_、大小写字母以及数字0 到9 之外的所有字符都具有特殊含义

字符类\x 代替完整的字符集[0-9a-fA-F]

/\v<(\w+)\_s+\1>

\v verymagic模式 () 捕获 <> 单词边界 \\_s匹配空白符或换行符

可以用\W\ze\w 模拟元字符 <，而用\w\ze\W 表示元字符 >

元字符\zs 标志着一个匹配的起始，而元字符\ze 则用来界定匹配的结束