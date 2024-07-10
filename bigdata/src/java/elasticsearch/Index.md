# Index

## 分片分配策略

### 索引级分片分配筛选器

可以根据筛选器将索引分配特定的地方。通常搭配**集群范围的分配筛选**和**分配感知**一起应用。

主副分片不可以在同一个节点。

通过自定义属性（在elasticsearch.yml定义）或者内置的属性（_name`, `_host_ip`, `_publish_ip`, `_ip`, `_host`, `_id`, `_tier` and `_tier_preference），通过分片分配筛选器将索引的分片分到合适的硬件机器上。

1. 自定义属性

自定义属性：size 并给值为 meduim

```
node.attr.size: medium
```

或者在启动的时候设置

```
./bin/elasticsearch -Enode.attr.size=medium
```

2. 在索引上添加分片分配筛选器

index.routing.allocation.include.{**attribute**}

将索引分配给其{attribute}至少具有一个逗号分隔值的节点。

index.routing.allocation.require.{**attribute**}

将索引分配给{attribute}具有所有逗号分隔值的节点。

index.routing.allocation.exclude.{**attribute**}

将索引分配给{attribute}中没有逗号分隔值的节点。

如指定test索引分片要分配到size为big或者medium的节点上

```

PUT test/_settings
{
  "index.routing.allocation.include.size": "big,medium"
}
```

如指定test索引分片要分配到size为big，并且rack为rack1的节点上

也就是说明这个几点的属性size一定要为big，rack一定要为rack1，这样test的分片才会分配到上面

```
PUT test/_settings
{
  "index.routing.allocation.require.size": "big",
  "index.routing.allocation.require.rack": "rack1"
}
```

如指定test索引分片要分配到_ip为192.168.2.*节点上

```
PUT test/_settings
{
  "index.routing.allocation.include._ip": "192.168.2.*"
}
```

### 当节点离线延迟分配

当节点离开集群时，主节点会：

1. 将副本分片变为主分片
2. 副本分片替换丢失的副本
3. 剩余节点上的分片重新分配平衡

但这样的shard-shuffle会有一定的问题，比如节点很快加入回来的话这会造成资源的浪费。

所以可以通过index.unassigned.node_left.delayed_timeout来设置节点离线延迟时间。

```
PUT _all/_settings
{
  "settings": {
    "index.unassigned.node_left.delayed_timeout": "5m" //当值为0的时候，认为节点离开后就永远不会回来，es会立刻重分配
  }
}
```

这时节点离开集群时，主节点会：

1. 将副本分片变为主分片
2. 主节点将未分配的分片记录到日志中（记录什么时候开始的以及离开多久），等待延时结束。（此时集群状态时黄色，因为有未分配的副本分片）
3. 如果在延时结束前，节点回归。丢失的副本被重新分配给回归的节点
4. 如果延迟分配超时，主服务器将丢失的分片分配给另一个节点，该节点将开始恢复。如果丢失的节点重新加入集群，并且其分片的sync-id仍然与主节点相同，则将取消重新定位分片，使用已同步的分片进行恢复。

通过 health API可以监控到延时未分配的分片

```
GET _cluster/health 
```

### 索引恢复优先级

优先策略（按顺序）：

1. 通过index.priority参数设置优先级（高到低）
2. 通过索引生成日期（近到远）
3. 通过索引名字（高到低）

```
PUT index_1

PUT index_2

PUT index_3
{
  "settings": {
    "index.priority": 10
  }
}

PUT index_4
{
  "settings": {
    "index.priority": 5
  }
}
```

恢复顺序： index_3、 index_4、 index_2、 index_1

### 每个节点的总分片限制

可以设置

每个结点的分片数上限（将分配给单个节点的分片(副本和主)的最大数量。默认为unbounded。）

```
index.routing.allocation.total_shards_per_node
```

集群的分配给每个节点的分片数上限（分配给每个节点的主分片和副本分片的最大数目。默认为-1(无限制)。）

```
cluster.routing.allocation.total_shards_per_node
```

### 索引级数据层分配筛选器

创建索引时通过_tier_preferenc参数控制一个索引分片分配到哪个数据层 （data_warm,data_hot等），ES会根据数据节点角色将索引和分片分配到对应的节点

```
index.routing.allocation.include._tier_preference:data_warm
```

数据节点可以有一些角色：

1. 内容数据节点（Content data node）

存放内容层数据

通常是产品目录或文章存档等项的集合，内容相对不变所以无需做层迁移。

内容节点内的数据通常进行负责的搜索和聚合，所以节点通常针对查询性能进行了优化，cpu性能比IO能力更重要。

从弹性的角度来看，该层中的索引应该配置为使用一个或多个副本。

```
node.roles: [ data_content ]
```

1. 热数据节点（Hot data node）

存放热层数据

时序数据或者最近、最常搜索的时间序列数据放在这层。

热数据节点内的数据通常需要快速读写，所以需要更多的硬件资源和更快速的存储比如（SSDs）

从弹性的角度来看，该层中的索引应该配置为使用一个或多个副本。

```
node.roles: [ data_hot ]
```

1. 温数据节点（）

存放温层数据

使用率降低的时序数据被放在这层。温层通常保存最近几周的数据。

温层的数据也允许更细，但频率不高，所以不需要和热层一样高的配置。

从弹性的角度来看，该层中的索引应该配置为使用一个或多个副本。

```
node.roles: [ data_warm ]
```

1. 冷数据节点

存放冷层数据

存放基本上不用的数据，从温层转移过来。

仍然是可搜索的，但这一层通常针对更低的存储成本而不是搜索速度进行了优化。

为了更好地节省存储，可以在冷层上保留可搜索快照的完全挂载索引。与常规索引不同，这些完全挂载的索引不需要副本来保证可靠性。如果发生故障，它们可以从底层快照恢复数据。

快照存储库需要在冷层中使用完全挂载的索引。完全挂载的索引是只读的。

或者，您可以使用冷层来存储带有副本的常规索引，而不是使用可搜索的快照。这使您可以将较旧的数据存储在较便宜的硬件上，但与热层相比，并不会减少所需的磁盘空间。

总结：

1. 使用可搜索快照的完全挂载索引，约磁盘空间。
2. 使用副本不要求机器性能，但不能节约磁盘空间。

```
node.roles: [ data_cold ]
```

1. 冻结数据节点(Frozen data node)

存放冻结层数据

数据不再被查询，或者很少被查询，它可能会从冷层移动到冻结层.

使用部分挂载索引去存储和加载数据从快照仓库。这减少了本地存储和操作成本，同时仍然允许您搜索冻结的数据。

因为要生成快照，所以搜索比冷层还慢。

```
node.roles: [ data_frozen ]
```

## 索引阻碍Index blocks

可以限制一个索引都能进行什么操作。可以动态设置索引也可以使用API，一个索引一旦设置了索引阻碍，所有的分片都会生效。

#### 动态索引阻碍参数

```
//是否只读 true/false，设置为true表示索引和索引元数据为只读，设置为false表示允许写入和元数据更改
index.blocks.read_only
index.blocks.write

```

**从索引中删除文档以释放资源(而不是删除索引本身)会暂时增加索引大小，因此在节点磁盘空间不足时可能不可行。**

```
//true:不允许删除
//当磁盘利用率超过阈值时，Elasticsearch会自动添加只读允许删除的索引块;当磁盘利用率低于阈值时，Elasticsearch会自动删除该索引块
index.blocks.read_only_allow_delete
//设置为true将禁用对索引的读操作。
index.blocks.read
//设置为true将禁用对索引的数据写操作。
//与read_only不同，此设置不影响元数据。
//index.blocks.write可以调整带有写块的索引的设置，index.blocks.read_only不能调整
index.blocks.write
//设置为true将禁用索引元数据读写
index.blocks.metadata
```

#### API方式设置

```
PUT /my-index-000001/_block/write



PUT /<index>/_block/<block>
```

**block**：**metadata**、**read**、**read_only**、**write**

## 索引分片合并

ES中的分片实际上时Lucene索引，每个Lucene索引会有很多个段组成，每个段大小是可变的，小的段会定期合并到大的段，然后小的会删除。

### 合并任务

ES使用单独的线程去执行合并任务，当任务线程满了，合并任务需要等待。

合并线程配置参数 （默认值： `Math.max(1, Math.min(4, <<node.processors, node.processors>> / 2))`）这对于一个好的固态硬盘(SSD)来说非常有效。如果索引位于旋转盘片驱动器上，则将其减小为1。

```
index.merge.scheduler.max_thread_count
```

