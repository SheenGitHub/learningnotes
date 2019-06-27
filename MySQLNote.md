# 安装 #

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

# 基础 #
## 事务隔离级别 ##

SET SESSION TRANSACTION ISOLATION LEVEL ~ 

1. READ UNCOMMITTED
2. READ COMMITTED
3. REPEATEABLE READ
4. SERIALIZABLE

SHOW TABLE STATUS LIKE '' /G

## 数据库引擎 ##

### MyISAM ###
 
- 数据文件MYD，索引文件MYI
- 平台通用,
- 动态行和静态行，大小只受限于磁盘空间大小
- 加锁与并发， 并发插入,表级别的加锁
- 自动修复
- 手工修复
- 支持全文索引
- 延迟更新索引

### InnoDB ###

- 专为支持事物处理，大量短期事物（Short-lived）
- 使用MVCC支持高并发性能（MultiVersion Concurrency Control）
- 基于聚簇索引，尽量吧主键定义的小，不会压缩索引
- 外键约束，可预测性的预读Predictive read-ahead，支持数据预先提取Prefetching，
- hash索引，插入缓冲区，快速插入

### Memory ###
- 内存表，速度快，易丢失
- 支持hash索引，快速查找
- 不支持TEXT，BLOG，只支持固定大小行，Varchar存储为Char

### Archive ###
- 只支持Insert和Select查询，不支持索引，缓冲了数据写操作，
- 适合于数据记录和数据采集的应用，
- 适合复制服务器上的快速数据插入
- 支持行级加锁和专用的缓冲区系统

### CVS ###

### Federated ###
不再本地存储，远程MySQL服务器上的表

### Blackhole ###
没有存储机制，假想复制设置，日记保留休息，审查日记记录

### NDB ###
- 集群引擎，支持冗余和负载均衡特性
- 基于无共享概念
- 数据节点，管理节点和SQL节点
- 不容节点上系统保留一份数据的多份拷贝
- 管理节点获取集中式配置，以及监控，控制所有集群节点，所有节点彼此同性

### Falcon ###

### solidDB ###
支持悲观并发控制和乐观并发控制

### PBXT ###
使用事务日志和数据文件来避免预写日志，减少事务开销

### Maria ###
取代MyISAM

1. 每张表支持事务性存储和非事务性存储两种选项
2. 在运行非事务性模式下，也能进行崩溃后的恢复
3. 行级锁和支持MVCC
4. 更好的BOLG数据处理

### 数据库引擎选择 ###
- 事务 InnoDB
- 并发 MyISAM
- 备份 奔溃后恢复

尽量避免NULL，难以优化可空列的查询，需要特殊处理

## 数据类型 ##
### 时间类型 ###
DATETIME,TIMESTAMP,DATE,YEAR

### 整数 ###
TINYINT, SMALLINT,MEDIUMINT,INT,BIGINT, 8，16，24，32，64 / -2^(n-1) ~ 2^(N-1)-1

### 实数 ###
- Decimal 精确计算，服务器上进行计算，cpu不支持
- Decimal最多65个数字，早期版本中254位，计算中专为Decimal
- Float和Double近似计算，cpu上直接计算，会快一点
- Float4字节，Double8字节
- 只有对小数精确计算时才使用Decimal，比如金融数据

### 字符串 ###
每个字符串列都有自己的字符集和排序规则，这会极大影响性能

#### varchar ####
varchar使用额外的1到2字节存储长度
varchar长度增加具体行为和存储引擎有关，MyISAM会将行拆开，
InnoDB可能进行分页，当最大长度远大于平均长度，并且更新较少是，使用varchar
保留末尾空格

#### char ####
CHAR，适合存储密码的MD5值，经常改变的列，去掉末尾的空格

数据如何保存取决于存储引擎，Memory存储引擎使用了固定长度，而Falcon引擎使用了可变长度
但是截取和填充空格的行为在各个存储引擎之间是一样的，因为这是MySQL服务器自身的行为

#### BINARY和VARBINARY ####

BLOG TEXT分别以二进制和字符串形式保留大量数据
只按max_sort_length规定的前若干个字节进行排序
不能索引这些数据的长度，也不能为排序使用索引
Memory不支持BLOG和TEXT， 实际使用MyISAM，引起性能开销

ENUM可以存储65535个不同的字符串

### BIT SET ### 

## 索引 ##

- B-Tree索引
- MyISAM使用前缀压缩（Prefix Compression）以减小索引
- InnoDB不会压缩索引，因为它不能把压缩索引用于某些优化
- MyISAM按照存储的物理位置引用被索引的行
- InnoDB按照主键值引用行

B-Tree按顺序保存了索引的列

### 最左原则 ###
- 不能跳过索引中的列
- 不能优化任何在第一个范围条件右边的列

列顺序几段重要
对于高性能应用程序，也许要针对不同的顺序创建多个索引，以满足程序的需要

### 哈希索引 ###
- 它吧哈希吗保存在索引中，并且保存了一个指向哈希表中每一个行的指针
- 只有Memory存储索引支持显示地哈希索引
- 只包含哈希码和行指针，而不是值自身
- 不能进行排序
- hash索引不支持部分匹配
- 只支持= IN <=>,不能加快范围查询
- InnoDB 自适应哈希索引 
- 当InnoDB注意到一些索引值被很频繁地访问的时候，它就会为B-Tree的顶端为这些值建立起内存的索引
- 避免hash碰撞

### 空间索引R-Tree ### 
### 全文索引 ###

### 数据布局 ###

MyISAM按照插入的顺序把值保存在磁盘上

InnoDB支持聚集索引
聚集索引中的每个叶子节点上都包含主键值，事务，ID，用于事务和MVCC的回滚指针及剩下的列
使用行的主键值作为指针使得索引变得更大，意味着InnoDB可以移动行，而无需更新指针
使用InnoDB的时候要以主键的顺序插入数据，并且使用单调增的主键来插入新行

MyISAM只在MySQL的内存中缓存引擎

聚集的数据优点：

- 可以把相关的数据保存在一起。
- 数据访问快。聚集索引吧索引和数据都保存在同一颗树B-Tree中，因此从聚集索引中过去的数据比非聚集快
- 使用覆盖索引的查询可以使用包含在叶子节点中的主键值
- 聚集能最大限度提升IO密集负载的性能
- 插入速度眼中依赖插入顺序，否则需要OPTIMIZE TABLE
- 跟新聚集索引列是昂贵的，它强制InnoDB吧每个更新的行列移到新的位置
- 建立在聚集索引上的表插入新行或者主键被更新，改行必须被移动的时候会进行分页
- 聚集表可能会比全变扫描


索引不能覆盖查询
选择了所有的列，没有索引覆盖所有列
MySQL不能在索引中前缀匹配的LIKE 

## SQL ##
### 函数 ###
#### 自定义Nextval ####
currval函数

	delimiter #  
	create function currval(v_seq_name VARCHAR(50))  
	returns integer  
	begin  
	    declare value integer;  
	    set value = 0;  
	    select current_val into value  from sequence where seq_name = v_seq_name;  
	   return value;  
	end;

nextval函数

	delimiter #
	create function nextval (v_seq_name VARCHAR(50)) returns integer
	begin
	    update sequence set current_val = current_val + increment_val  where seq_name = v_seq_name;
		return currval(v_seq_name);
	end;

 查询使用

	select nextval('seq_test1_num1');

函数的参数必须使用 ' 引号，否则报错unknown column
### 触发器 ###
CREATE TRIGGER trigger_name trigger_time trigger_event ON tb_name FOR EACH ROW trigger_stmt

- trigger_name：触发器的名称
- tirgger_time：触发时机，为BEFORE或者AFTER
- trigger_event：触发事件，为INSERT、DELETE或者UPDATE
- tb_name：表示建立触发器的表明，就是在哪张表上建立触发器
- trigger_stmt：触发器的程序体，可以是一条SQL语句或者是用BEGIN和END包含的多条语句

所以可以说MySQL创建以下六种触发器：

BEFORE INSERT,BEFORE DELETE,BEFORE UPDATE
AFTER INSERT,AFTER DELETE,AFTER UPDATE

NEW和OLD

- NEW.columnname：新增行的某列数据
- OLD.columnname：删除行的某列数据

	CREATE TRIGGER `TRI_test1_num1` BEFORE INSERT ON `test1` FOR EACH ROW BEGIN
	set NEW.num1 = nextval('seq_test1_num1');
	set NEW.num2 = nextval('seq_test1_num2');
	END

# 索引 #
## B-Tree索引 ##
索引顺序的重要性
## 哈希索引 ##
存储引擎会对所有的索引列计算一个Hash值

仅有Memory引擎支持, 无法排序

## 前缀索引 ##
MySQL无法使用前缀索引做ORDER BY 和GROUP BY ，也无法使用前缀做覆盖扫描

## 多列索引 ##
MySQL 5.0起查询能够同时使用两个单列索引进行扫描，并将结果进行合并。

索引列顺序：将选择性最高的列放在索引的最前列,但通常不如避免随机IO和排序那么重要。

## 聚簇索引 ##
当表有聚簇索引时，它的数据行实际上存放在索引的叶子页。

InnoDB在移动时无需更新二级索引中的这个“指针”

高并发时 AUTO_INCREMENT锁机制会成为问题

## 覆盖索引 ##
using Index

## 索引扫描来排序 ##
## 压缩(前缀压缩)索引 ##
对于CPU密集型应用，因为扫描需要随机查找，压缩索引是的MyISAM在索引查找上要慢了好几倍。索引压缩的倒叙扫描就更慢了。压缩索引需要在CPU内存资源与磁盘之间做权衡。压缩索引可能只需要十分之一大小的空间，如果是I/O密集型应用，对某些查询带来的好处比成本多很多。
## 索引和锁 ##
## 索引使用 ##
### 支持多种过滤条件 ###
### 避免多个范围条件 ###
### 优化排序 ###
- 增加一些特殊的索引来排序
- 使用延迟策略，使用覆盖索引查询返回需要的主键，再根据这些主键关联原表获得需要的行，这样可以减少MySQL扫描那些需要丢弃的行数。

# 查询性能优化 #
## 慢查询基础：优化数据访问 ##
### 不需要的查询 ###
- 查询不需要的记录
- 多表关联时返回全部列
- 总是取出全部列
- 重复查询相同的数据

### 额外的扫描 ###

#### WHERE使用条件 ####
- 在索引中使用WHERE来过滤不匹配的记录，这是在存储引擎层完成的
- 使用引擎覆盖扫描(Using index)来返回记录，在服务层完成，无需回表查询
- 从数据表中返回数据，再过滤不满足条件的记录。在服务层完成，需要从数据表读出记录然后过滤

### 切分查询 ###
### 分离查询关联 ###
- 让缓存的效率更高
- 将查询分解后，执行单个查询可减少锁的竞争
- 在应用层做关联，可以更容易对数据库拆分，提供高性能和可扩展
- 减少冗余查询
- 相当于实现哈希关联

## 查询执行基础 ##
## 查询执行路径 ##
1. 客户端发送一条查询给服务器
1. 服务器先检查查询缓存
1. 服务器进行SQL解析、预处理，再由优化器生成对应的执行计划
1. MySQL根据优化器生成的执行计划，再调用存储引擎的API来执行查询
1. 将结果返回给客户端

## 查询状态 ##
- Sleep
- Query
- Locked
- Analyzing and Statistics
- Copying to tmp table [on disk]
- Sorting result
- sending data

## 优化查询器 ##
- 重新定义关联表的顺序
- 将外连接转化成内连接
- 使用等价变换规则
- 优化COUNT MIN MAX
- 预估并转化为常数表达式
- 覆盖索引扫描
- 子查询优化
- 提前终止查询

## 关联查询优化器 ##
## 排序优化 ##
当不能使用索引生成排序的时候，MySQL需要自己进行排序，这个过程统一称为文件排序。当小于“排序缓冲区”，使用内存进行快速排序，如果内存不够排序，MySQL会将数据分块排序，最终进行归并。

# 分区表 #
PARTITION BY
## 如何使用分区表 ##
当数据量超大的时候，B-Tree索引就无法起作用了。除非是索引覆盖查询，否则数据库服务器需要根据索引扫描的结果回表，查询所有符合条件的记录，如果数据量巨大，这将产生大量随机I/O,随之，数据库的响应时间将大到不可接受的程度。
## 查询优化 ##
即便创建分区时可以使用表达式，但在查询时却只能根据列来过滤分区。

# 视图 #
视图和临时表 对应 SQL合并算法和临时表 

如果视图中包含GROUP BY、DISTINCT、任何聚合函数、UNION、子查询等，只要无法在原表记录和视图记录中建立一一映射的场景中，MySQL都将使用临时表算法来实现视图

使用临时表的视图无法被更新，不能更新WHERE所定义的列

# 内部存储代码 #
效率更高，不易调试

## 存储过程和函数 ##
## 触发器 ##
掩盖了服务器背后的工作，出现问题难以排查，基于行的触发的限制
## 事件 ##
给服务器带来额外的工作

# 游标 #
不支持客户端的游标

# 绑定变量 #
绑定变量是会话级别的

### 自定义函数 ###
User Defined Function

# 字符集和校对规则 #
字符集是指一种从二进制编码到某类字符符号的映射。“校对”是指一组用于某个字符集的排序规则。

**只有基于字符的值才真正的“有”字符集的概念**

#### 默认设置 ####
MySQL服务器有默认的字符集和校对规则，每个数据库也有自己的默认值，每个表也有自己的默认值。

- 创建数据库的时候，将根据服务器上的character_set_server来设定来设定该数据库的默认字符集。
- 创建表的时候，将根据数据库的字符集设置指定这个表的字符集设置。
- 创建列的时候，将根据表的设置指定列的字符集设置。

一个表的默认字符集设置无法影响在这个表中某个列的值。只有创建列而没有为列指定字符集的时候，表的默认字符集才有作用

- character_set_client 服务器假设客户端以此来传输数据，以此解析转义序列
- character_set_connection 收到客户端SQL，先将其转化成此字符集，并以此决定如何将数据转换成字符串
- character_set_result 将数据返回给客户端时，将其转换为此格式
- character_set_database 默认值改变，这个值也会改变 和 server相同，使用LOAD DATA INFILE时按此解析
- character_set_server 默认值
- character_set_filesystem
- character_set_system
- character_set_dir /usr/share/mysql/charsets/

可以使用SET NAMES或者SET CHARACTER SET语句来改变上面的设置，在服务器上使用这个命令只能改变服务器端的设置。

### 校对 ###
比较的两个字符串的字符集不同，先将其转成同一个字符集再进行比较。

只有排序查询要求的字符集与服务器数据的字符集相同的时候，才能使用索引进行排序

大小写敏感校对规则的前缀分别是_cs、_ci、_bin,大小写敏感和二进制校对的不同之处在于，二进制校对规则直接使用字符的字节进行比较。
# 全文索引 #
# XA #
# 查询缓存 #
# 配置 #
## I/O ##
### InnoDB I/O配置 ###
InnoDB 使用日志来减少提交事务时的开销。

InnoDB用日志把随机I/O变成顺序I/O。


# Q&A #
#### 查看技巧 ####
\G 结尾，竖直查看表

desc查看表结构

#### MySQL导出数据库结构出现Cannot load from mysql.proc错误的解决方法 ####
从mysql5.5的版本开始，proc这张表中的comment字段的列属性已经由char(64)改为text类型，我们需要更改一下数据类型：

	ALTER TABLE `proc`
	
	MODIFY COLUMN `comment`  text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL AFTER `sql_mode`;

以上无效

**mysql_upgrade -uroot -p 即可**