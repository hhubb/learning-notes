# 数据类型
## 核心数据类型
### Strings
#### 原理
Redis Strings 存储了一些类的字节，包括文本、序列化的对象和二进制数组。Strings是Redis最简单的类型，你可以通过一个Redis的key关联这个类型的值。
通常被用于缓存，但是他们可以支持额外的功能比如你可以用这个类型实现计数功能和执行位操作。
Strings作为值，大小不能超过512M`SUBSTR, GETRANGE, SETRANGE`是 O(n)
大部分的操作是 O(1)，所以很高效。但是
如果数据的格式是可序列化的，也可以考虑Hash和JSON类型
#### 常见命令
1. `GET、SET`命令。 如果这个Key已经存在，值会被覆盖。
```
    > SET bike:1 Deimos
    OK
    > GET bike:1
    "Deimos"
```
2. `INCR`命令。执行后会将原来的值+1，用来实现计数功能。
此外还有 `INCRBY`指定加多少, `DECR`减一，  `DECRBY`指定减多少，`INCRBYFLOAT`指定加一个浮点数
```
    > set total_crashes 0
    OK
    > incr total_crashes
    (integer) 1
    > incrby total_crashes 10
    (integer) 11
```
INCR这一系列的命令是原子的，因为在执行INCR命令的时候，永远都是先read-increment-set,一个客户端执行操作时，其他所有客户端都不会执行命令。（串行）
3. 其他命令如，将新的值设置进去返回旧的值。
4. `MSET、MGET`批量命令，执行多个命`GET、SET`
```
    > mset bike:1 "Deimos" bike:2 "Ares" bike:3 "Vanth"
    OK
    > mget bike:1 bike:2 bike:3
    1) "Deimos"
    2) "Ares"
    3) "Vanth"
```

#### 其他使用场景
1.Redis锁 
  `nx`Strings有一个额外的参数`nx`，添加这个参数后如果这个Key存在就设置失败。
```
    > set bike:1 bike nx
    (nil)
    > set bike:1 bike xx
    OK
```
### List
#### 原理
从一个通常的角度来看，List只是一个元素的序列:10,20,1,2,3是一个列表。但是，使用数组实现的List的属性与使用链表实现的List的属性非常不同。
**Redis Lists是通过链表实现的**。这意味着，即使列表中有数百万个元素，在列表的头部或尾部添加新元素的操作也会在常量时间内执行，但访问一个元素就会变得很慢。
通过index访问一个由数组构建的Lists是非常快的（可以在常熟时间内访问到）。
**为什么Redis Lists选择链表实现呢？因为对于数据库系统来说，能够以非常快的方式将元素添加到非常长的列表中是至关重要的。**
访问它的头或尾的列表操作是O(1)，这意味着它们是非常高效的。但是，操作列表中的元素的命令通常是O(n)。这些例子包括LINDEX、LINSERT和LSET。运行这些命令时要小心，特别是在操作大型列表时。

最大存储`2^32 - 1 `个元素

当键不存在时,Redis自动创建一个空list,当列表为空时，Redis自动负责删除键.这不是特定于列表，它适用于由多个元素组成的所有Redis数据类型——流、集、排序集和哈希。

List结构是被存放String类型的值的集合。通常List被用于
1. 实现栈和队列
2. 为后台工作系统构建队列管理

如果需要快速访问大型元素集合的中间值时，可以使用一种不同的数据结构Sorted Sets。
如果需要存储和处理一系列不确定的事件时，考虑使用Redis Stream作为列表的替代方案。

#### 常见命令
1. LPUSH 向队列中添加一个元素到头部 RPUSH 向队列中添加一个元素到尾部

2. LPOP 从队列头部移除一个元素并返回该元素 LPOP 从队列尾部移除一个元素并返回该元素
   Redis返回一个NULL值，表示列表中没有元素。
```
## 栈
> LPUSH bikes:repairs bike:1
(integer) 1
> LPUSH bikes:repairs bike:2
(integer) 2
> LPOP bikes:repairs
"bike:2"
> LPOP bikes:repairs
"bike:1"
## 队列
> LPUSH bikes:repairs bike:1
(integer) 1
> LPUSH bikes:repairs bike:2
(integer) 2
> RPOP bikes:repairs
"bike:1"
> RPOP bikes:repairs
"bike:2"
```
3. LLEN 队列总长度
```
> LLEN bikes:repairs
(integer) 0
```
4. LMOVE 原子操作，将一个队列中的元素移入另一个队列。
```
> LPUSH bikes:repairs bike:1
(integer) 1
> LPUSH bikes:repairs bike:2
(integer) 2
> LMOVE bikes:repairs bikes:finished LEFT LEFT
"bike:2"
> LRANGE bikes:repairs 0 -1
1) "bike:1"
> LRANGE bikes:finished 0 -1
1) "bike:2"
```
5. LRANG 从list中获取指定范围的元素，有两个参数，第一个是开始index，第二个是结束index，两个参数都需要是正整数，告诉Redis从末尾开始计数:所以-1是列表的最后一个元素，-2是倒数第二个元素，以此类推。
6. LTRIM 将list的元素缩减到指定范围
```
> RPUSH bikes:repairs bike:1 bike:2 bike:3 bike:4 bike:5
(integer) 5
> LTRIM bikes:repairs 0 2
OK
> LRANGE bikes:repairs 0 -1
1) "bike:1"
2) "bike:2"
3) "bike:3"
```

#### 阻塞命令
1. BLPOP 从队列头部移除一个元素并返回该元素，如果队列是空则阻塞直到队列有元素或者指定的阻塞时间到了
   a. 客户端以有序的方式提供服务:第一个阻塞等待列表的客户端，在其他客户端推送元素时首先提供服务，依此类推。
   b. 与RPOP相比，返回值是不同的:它是一个双元素数组，因为它还包括键的名称，因为BRPOP和BLPOP能够阻止等待来自多个列表的元素。
   c. 如果达到了超时，则返回NULL。
```
> RPUSH bikes:repairs bike:1 bike:2
(integer) 2
> BRPOP bikes:repairs 1
1) "bikes:repairs"
2) "bike:2"
> BRPOP bikes:repairs 1
1) "bikes:repairs"
2) "bike:1"
> BRPOP bikes:repairs 1
(nil)
(2.01s)
```
2. BLMOVE 原子的将的目标list中的指定元素到另一个list中，如果目标list是空则阻塞，直到目标list有元素。

#### 其他使用场景
1. 记住用户在社交网络上发布的最新更新。
   ```
   假设主页显示了在照片共享社交网络上发布的最新照片，您希望加快访问速度。
   每当用户发布一张新照片时，我们使用LPUSH将其ID添加到列表中。
   当用户访问主页时，我们使用LRANGE 0 9来获取最近发布的10个条目。
   ```
2. Redis允许我们使用列表作为一个有上限的集合
   使用LTRIM命令只记住最新的N项，丢弃所有最旧的项
 
5. 进程之间的通信，使用消费者-生产者模式，其中生产者将项目推入列表，消费者(通常是工作者)消费这些项目并执行操作。Redis有特殊的列表命令，使这个用例更加可靠和高效。
   ```
   假设希望使用一个进程将项目推入列表，并使用另一个进程来实际处理这些项目。这是通常的生产者/消费者设置，可以通过以下简单的方式实现:
   1. 为了将项目推入列表，生产者调用LPUSH。
   2. 为了从列表中提取/处理项目，消费者调用BRPOP。
   可以在列表为空时阻塞:它们只会在列表中添加新元素或达到用户指定的超时时返回调用者。
   ```

### Set
#### 原理
Redis Set是一个无序的，元素是Strings类型的且唯一的集合。
可以用来
跟踪唯一项(例如，跟踪访问给定博客文章的所有唯一IP地址)。
表示关系(例如，具有给定角色的所有用户的集合)。
执行常见的集合操作，如交集、并集和差集。

最大元素个数：2^32 - 1

添加、移除、检测都是O(1).对于具有数十万或更多成员的大型集合，在运行SMEMBERS命令时应该谨慎。该命令为O(n)，并在单个响应中返回整个集合。作为一种替代方案，SCAN，它允许您迭代地检索集合的所有成员。

对大型数据集(或流数据)进行成员检查会占用大量内存。如果你担心内存使用，不需要完美的精度，可以考虑使用Bloom过滤器或Cuckoo过滤器来替代一组。
Redis SETS 经常被用作一种索引。如果需要索引和查询数据，请考虑JSON数据类型以及Search和查询特性

#### 常见命令
1. SADD 添加一个新的元素,如果存在会被忽略
```
> SADD bikes:racing:france bike:1
(integer) 1
> SADD bikes:racing:france bike:1
(integer) 0
> SADD bikes:racing:france bike:2 bike:3
(integer) 2
> SADD bikes:racing:usa bike:1 bike:4
(integer) 2
```
2. SREM 移除一个指定元素
3. SPOP 随机移除一个元素，并返回这个元素
4. SRANDMEMBER 返回一个随机数，不删除
```
> SADD bikes:racing:france bike:1 bike:2 bike:3 bike:4 bike:5
(integer) 5
> SREM bikes:racing:france bike:1
(integer) 1
> SPOP bikes:racing:france
"bike:3"
> SMEMBERS bikes:racing:france
1) "bike:2"
2) "bike:4"
3) "bike:5"
> SRANDMEMBER bikes:racing:france
"bike:2"
```
5. SMEMBERS 返回集合所有元素
```
> SADD bikes:racing:france bike:1 bike:2 bike:3
(integer) 3
> SMEMBERS bikes:racing:france
1) bike:3
2) bike:1
3) bike:2
```
6. SISMEMBER 测试字符串的集合成员关系
```
> SISMEMBER bikes:racing:usa bike:1
(integer) 1
> SISMEMBER bikes:racing:usa bike:2
(integer) 0
> SMISMEMBER bikes:racing:france bike:2 bike:3 bike:4
1) (integer) 1
2) (integer) 1
3) (integer) 0
```
4. SINTER 返回两个或多个集合共有的成员集合
```
> SINTER bikes:racing:france bikes:racing:usa
1) "bike:1"
```
5. SCARD 返回集合的大小
```
> SCARD bikes:racing:france
(integer) 3
```
6. SDIFF 以A集合为主，哪些元素不在B集合中,如果A包含B，则返回空
```
> SADD bikes:racing:usa bike:1 bike:4
(integer) 2
> SDIFF bikes:racing:france bikes:racing:usa
1) "bike:3"
2) "bike:2"
```
7. SUNION 两个或以上的集合中所有的元素
```
> SADD bikes:racing:france bike:1 bike:2 bike:3
(integer) 3
> SADD bikes:racing:usa bike:1 bike:4
(integer) 2
> SADD bikes:racing:italy bike:1 bike:2 bike:3 bike:4
(integer) 4
> SINTER bikes:racing:france bikes:racing:usa bikes:racing:italy
1) "bike:1"
> SUNION bikes:racing:france bikes:racing:usa bikes:racing:italy
1) "bike:2"
2) "bike:1"
3) "bike:4"
4) "bike:3"
```
### Hash
#### 原理
Redis Hash 是由字段值对组成的记录类型。您可以使用散列来表示基本对象和存储计数器分组等。
虽然散列可以方便地表示对象，但实际上您可以放入散列中的字段数量没有实际限制(除了可用内存)，因此您可以在应用程序中以许多不同的方式使用散列。
值得注意的是，小哈希(即，具有小值的几个元素)在内存中以特殊的方式编码，使它们非常节省内存。

可存储字段个数2^32 - 1。在实践中，你的哈希值只受Redis部署的虚拟机的总内存的限制。

大部分操作都是 O(1).一些命令(如HKEYS、HVALS、HGETALL)和大多数与过期相关的命令是0 (n)，其中n是字段值对的个数。



#### 常见命令
1. HSET 设置一个或多个字段以及值到同一个Hash中
2. HGET 从这个哈希中获取一个字段
3. HGETALL 返回这个哈希所有的字段和值
```
> HSET bike:1 model Deimos brand Ergonom type 'Enduro bikes' price 4972
(integer) 4
> HGET bike:1 model
"Deimos"
> HGET bike:1 price
"4972"
> HGETALL bike:1
1) "model"
2) "Deimos"
3) "brand"
4) "Ergonom"
5) "type"
6) "Enduro bikes"
7) "price"
8) "4972"****
```
4. HMGET 从这个哈希中返回一个或者多个字段
```
> HMGET bike:1 model price no-such-field
1) "Deimos"
2) "4972"
3) (nil)
```
5. HINCRBY 给这个哈希中的某一个字段加上一个数
```
> HINCRBY bike:1:stats rides 1
(integer) 1
> HINCRBY bike:1:stats rides 1
(integer) 2
> HINCRBY bike:1:stats rides 1
(integer) 3
> HINCRBY bike:1:stats crashes 1
(integer) 1
> HINCRBY bike:1:stats owners 1
(integer) 1
> HGET bike:1:stats rides
"3"
> HMGET bike:1:stats owners crashes
1) "1"
2) "1"
```
6. HLEN 获取哈希内字段个数


#### TTL指令
Redis Community Edition 7.4新增了为单个哈希字段指定过期时间或生存时间(TTL)值的功能。此功能与密钥过期相当，并且包含许多类似的命令。
1. HEXPIRE 设置存留一个时间，精确到秒
2. HPEXPIRE 设置存留一个时间，精确到毫秒
3. HEXPIREAT 设置一个过期时间，参数为精确到秒的时间戳
4. HPEXPIREAT 设置一个过期时间，参数为精确到毫秒的时间戳
使用以下命令来检索特定字段到期的确切时间或剩余TTL:
5. HTTL 获取剩余存留时间，精确到秒
6. HPTTL 获取剩余存留时间，精确到毫秒
7. HEXPIRETIME 获取以秒为单位的时间戳形式的过期时间。
8. HPEXPIRETIME 获取以毫秒为单位的时间戳形式的过期时间。
使用如下命令移除过期字段
9. HPERSIST 删除过期字段

使用场景
1. 事件跟踪：将事件设置一个有效期为1小时，这样就可以获取最近一小时内的数据。
2. 令牌/会话管理：给令牌或者会话增加一个存留时间，到期后自动失效。还可以根据HLEN获取活跃的令牌或会话数
3. 做锁：为了避免分布式的服务场景下，会将其他服务设置的锁释放，所以可以用大key作为锁的唯一值，小Key做唯一与业务相关的唯一值，进行加锁。并且为了避免服务异常导致锁无法释放，可以加一个存留时间，到期后自动释放锁。

官方客户端库中还没有对哈希字段过期的支持，但是您现在可以使用Python (redis-py)和Java (jedi)客户端库的测试版来测试哈希字段过期。



### Sort Set
#### 原理
Redis Sort Set是一个通过关联性分数排序的，元素是Strings类型的且唯一的集合。当多个字符串具有相同的分数时，这些字符串按字典顺序排序
Redis Sort Set像是Set 和Hash的结合。
和Set一样元素都是唯一的，不同的是Sort Set是根据一个浮点数的值，也叫分数进行排序的，每个元素都会映射一个分数，这一点和Hash很像。
使用场景
游戏排行榜。例如，您可以使用排序集来轻松维护大型在线游戏中最高分数的有序列表。
速率限制器。特别是，您可以使用排序集来构建滑动窗口速率限制器，以防止过多的API请求。
排序集合中的元素是按顺序获取的(因此它们不是按插入顺序排序的，顺序是用于表示排序集合的数据结构的特性)。它们按照以下规则进行排序:
   如果B和A的的 分数不一样，A的分数>B的分数 那么A>B
   如果B和A的的 分数一样，A的字典顺序>B的字典顺序 那么A>B
Redis Sort Set 是通过双端口数据结构实现的，该结构包含一个跳跃表和一个哈希表。所以每次添加一个元素时数据已经按照分数进行排序和插入性能是O(log(N))。

Redis Sort Set大多数操作是O(log(n))，其中n是成员的数目。
在使用较大的返回值(例如数万或更多)运行ZRANGE命令时要谨慎一些。该命令的时间复杂度为O(log(n) + m)，其中m是返回的结果数。

Redis排序集有时用于索引其他Redis数据结构。如果需要索引和查询数据，请考虑JSON数据类型以及Search和查询特性。

#### 常见命令
1. ZADD 添加一个元素，以及这个元素的分数
```
> ZADD racer_scores 10 "Norem"
(integer) 1
> ZADD racer_scores 12 "Castilla"
(integer) 1
> ZADD racer_scores 8 "Sam-Bodden" 10 "Royce" 6 "Ford" 14 "Prickett"
(integer) 4
```
2. ZREM 移除某个元素
3. ZREMRANGEBYSCORE 某个分数范围内的元素，返回移除的个数
```
> ZREM racer_scores "Castilla"
(integer) 1
> ZREMRANGEBYSCORE racer_scores -inf 9
(integer) 2
> ZRANGE racer_scores 0 -1
1) "Norem"
2) "Royce"
3) "Prickett"
```
4. ZRANGE 返回范围内的元素，并从低到高排列 参数和LRANG类似，使用WITHSCORES可以连分数也返回
5. ZREVRANGE 返回范围内的元素，并从高到低排列
```
> ZRANGE racer_scores 0 -1
1) "Ford"
2) "Sam-Bodden"
3) "Norem"
4) "Royce"
5) "Castilla"
6) "Prickett"
> ZREVRANGE racer_scores 0 -1
1) "Prickett"
2) "Castilla"
3) "Royce"
4) "Norem"
5) "Sam-Bodden"
6) "Ford"
> ZRANGE racer_scores 0 -1 withscores
 1) "Ford"
 2) "6"
 3) "Sam-Bodden"
 4) "8"
 5) "Norem"
 6) "10"
 7) "Royce"
 8) "10"
 9) "Castilla"
10) "12"
11) "Prickett"
12) "14"
```
6. ZRANGEBYSCORE 返回分数在某个值之下的所有元素
```
## 返回负无穷到10之间的元素，闭区间
> ZRANGEBYSCORE racer_scores -inf 10 
1) "Ford"
2) "Sam-Bodden"
3) "Norem"
4) "Royce"
```
7. ZRANK 根据分数从低到高，返回元素所在的位置
8. ZREVRANK  根据分数从高到低，返回元素所在的位置
```
> ZRANK racer_scores "Norem"
(integer) 0
> ZREVRANK racer_scores "Norem"
(integer) 3
```
#### 字典排序命令
Redis2.8后增加的新特新，当所有元素的分数相同时，额可以使用字典命令进行数据操作,根据第一个字母排序。字符串无限和负无限分别用+和-字符串指定
1. ZRANGEBYLEX 返回范围内的数据 参数是字母 从A到Z
2. ZREVRANGEBYLEX 返回范围内的数据 参数是字母 从Z到A
3. ZREMRANGEBYLEX  移除范围内的数据 参数是字母 
4. ZLEXCOUNT 返回范围内的数据个数 参数是字母 
```
> ZADD racer_scores 0 "Norem" 0 "Sam-Bodden" 0 "Royce" 0 "Castilla" 0 "Prickett" 0 "Ford"
(integer) 3
> ZRANGE racer_scores 0 -1
1) "Castilla"
2) "Ford"
3) "Norem"
4) "Prickett"
5) "Royce"
6) "Sam-Bodden"
> ZRANGEBYLEX racer_scores [A [L
1) "Castilla"
2) "Ford"
```
#### 更新分数
排序集的分数可以随时更新。只要对已经包含在排序集中的元素调用ZADD，就会以O(log(N))的时间复杂度更新它的分数(和位置)。因此，排序集适用于有大量更新的情况。
由于这一特点，排行榜是一个常见的用例。典型的应用是Facebook游戏，你可以根据用户的高分对其进行排序，再加上获得排名操作，从而显示前n名用户和排名第1名的用户
1. ZINCRBY 在原来的分数基础上加上一个指定分数，返回新的分数
```
> ZADD racer_scores 100 "Wood"
(integer) 1
> ZADD racer_scores 100 "Henshaw"
(integer) 1
> ZADD racer_scores 150 "Henshaw"
(integer) 0
> ZINCRBY racer_scores 50 "Wood"
"150"
> ZINCRBY racer_scores 50 "Henshaw"
"200"
```