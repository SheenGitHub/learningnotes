
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
