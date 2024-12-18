# ES SQL
ES SQL的目的是提供一个强大但轻量的的SQL接口给ES

ES SQL是一个特性：提供类SQL的查询方式在ES中做到近实时检索。
无论是否使用REST接口，命令行或者JDBC，任何客户端可以使用SQL进行搜索、聚合ES的数据。可以把ES SQL认为是一种可以明白SQL和ES转换器，既可以方便阅读，又可以通过利用ES的能力来实时的处理数据。
后台引擎本身是Elasticsearch
为什么使用ES SQL？
1. 原生整合：ES SQL是为ES从头构建的。每一次查询都根据底层存储有效的分配给相关的节点执行。
2. 无额外的附件：不需要额外的硬件、程序、运行器或者库来执行ES查询。Elasticsearch SQL通过在Elasticsearch集群内运行消除了额外的移动部分。
3. 轻量级且有效：Elasticsearch SQL并没有抽象Elasticsearch及其搜索功能——相反，它包含并公开了SQL，允许以相同的声明性、简洁的方式实时地进行适当的全文搜索。
## 查询方式
1. 通过API方式查询

```
POST /_sql?format=txt
{
  "query": "SELECT * FROM library WHERE release_date < '2000-01-01'"
}
```
1. 通过Client方式查询
```
$ ./bin/elasticsearch-sql-cli

sql> SELECT * FROM library WHERE release_date < '2000-01-01';
```
## 术语
跨SQL和Elasticsearch的概念映射
当SQL和ES对于一些数据祖师方式上，有一些术语不一样但本质上是同一个目的。

| SQL              | ES              | 描述                                                                                                                                                                                               |
|------------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| colum            | field           | 在这两种情况下，在最低级别，数据存储在各种数据类型的命名项中，其中包含一个值。SQL将这样的条目称为列，而Elasticsearch将其称为字段。注意，在Elasticsearch中，一个字段可以包含多个相同类型的值（本质上是一个列表），而在SQL中，一个列只能包含上述类型的一个值。Elasticsearch SQL将尽最大努力保留SQL语义，并根据查询，拒绝那些返回多个值的字段。 |
| row              | document        | 列和字段本身不存在；它们是行或文档的一部分。两者的语义略有不同：行倾向于严格（并且有更多的强制执行），而文档倾向于更灵活或松散（同时仍然具有结构）                                                                                                                        |
| table            | index           | 执行SQL或Elasticsearch查询的目标。                                                                                                                                                                        |
| schema           | implicit（隐藏、暗含） | 模式主要是表的名称空间，通常用作安全边界。Elasticsearch并没有提供一个等价的概念。但是，当启用安全性时，Elasticsearch会自动应用安全强制，以便角色只能看到允许的数据（在SQL术语中，它的模式）。                                                                                    |
| catalog/database | cluster 实例      | 在SQL中，目录或数据库可以互换使用，并表示一组模式，即许多表。在Elasticsearch中，可用的索引集被分组在一个集群中。语义也有一点不同；数据库本质上是另一个名称空间（这可能对数据的存储方式有一些影响），而Elasticsearch集群是一个运行时实例，或者说是至少一个Elasticsearch实例的集合（通常是分布式运行的）。 在实践中，这意味着在SQL中，一个实例中可能有多个目录，而在Elasticsearch中，一个目录被限制为只有一个。                       |
| cluster          | cluster 分布      | 传统上，在SQL中，集群是指单个RDBMS实例，它包含许多目录或数据库（见上文）。同样的单词也可以在Elasticsearch中重复使用，但是它的语义稍微澄清了一点。RDBMS倾向于在一台机器上（不是分布式的）只有一个运行实例，而Elasticsearch则相反，默认情况下是分布式和多实例的。 此外，一个Elasticsearch集群可以以联邦的方式连接到其他集群，因此集群意味着： 单个集群：多个Elasticsearch实例，通常是分布式的                                                                                                                                                                                                 |

## 安全
如果在集群上启用了Elasticsearch SQL，那么它将与安全性集成在一起。在这种情况下，Elasticsearch SQL支持传输层（通过加密消费者和服务器之间的通信）和身份验证（用于访问层）的安全性。

### SSL/TLS配置
在加密传输的情况下，ES需要开启SSL/TLS配置才能建立连接。设置ssl 为true或者使用https前缀的url。根据情况也会需要配置CA证书

### 身份认证
ES SQL支持两种类型的权限
1. Username/Password ：用户名密码
2. PKI/X.509:使用X.509证书对Elasticsearch SQL进行认证。应该设置ssl.keystore.location和ssl.truststore.location属性，以指示要使用的密钥库和信任库。

### 权限（服务端）
在服务器上，需要向用户添加一些权限，以便他们可以运行SQL。要运行SQL，用户至少需要read和indices:admin/get权限，而API的某些部分需要cluster:monitor/main。
您可以通过创建角色并将该角色分配给用户来添加权限。
可以使用Kibana、API调用或Yml配置文件来创建角色。
1. Kibana、API调用
使用Kibana或角色管理api是定义角色的首选方法。**但不能使用角色管理api来查看或编辑roles.yml中定义的角色。**
```
POST /_security/role/cli_or_drivers_minimal
{
  "cluster": ["cluster:monitor/main"],
  "indices": [
    {
      "names": ["test"],
      "privileges": ["read", "indices:admin/get"]
    }
  ]
}
```
2. roles.yml配置文件
如果希望定义不需要更改的角色，基于文件的角色管理非常有用。
```
cli_or_drivers_minimal:
  cluster:
    - "cluster:monitor/main"
  indices:
    - names: test
      privileges: [read, "indices:admin/get"]
    - names: bort
      privileges: [read, "indices:admin/get"]
```