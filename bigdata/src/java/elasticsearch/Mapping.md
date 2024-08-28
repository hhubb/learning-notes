# Mapping

## 动态字段映射

通过设置dynamic参数来动态创建字段。可以在文档和对象级别禁用dynamic。默认true

1. true：根据字段内容判断字段类型，将新字段添加到索引中
2. runtime：根据字段内容判断字段类型，将新字段添加到动态运行时字段中，因为只存_source中，所以不被索引，但与false不同，依然可以被查询，查询的代价会更大。
3. false ：忽略新字段，只存在_source中，也就是不能查询只能展示
4. strict：拒绝为定义的字段插入

Elasticsearch默认字段类型

| **JSON data type**                                           | **`"dynamic":"true"`**                             | **`"dynamic":"runtime"`**                          |
| ------------------------------------------------------------ | -------------------------------------------------- | -------------------------------------------------- |
| `null`                                                       | No field added                                     | No field added                                     |
| `true` or `false`                                            | `boolean`                                          | `boolean`                                          |
| `double`                                                     | `float`                                            | `double`                                           |
| `long`                                                       | `long`                                             | `long`                                             |
| `object`                                                     | `object`                                           | No field added                                     |
| `array`                                                      | Depends on the first non-`null` value in the array | Depends on the first non-`null` value in the array |
| `string` that passes [date detection](https://www.elastic.co/guide/en/elasticsearch/reference/current/dynamic-field-mapping.html#date-detection) | `date`                                             | `date`                                             |
| `string` that passes [numeric detection](https://www.elastic.co/guide/en/elasticsearch/reference/current/dynamic-field-mapping.html#numeric-detection) | `float` or `long`                                  | `double` or `long`                                 |
| `string` that doesn’t pass `date` detection or `numeric` detection | `text` with a `.keyword` sub-field                 | `keyword`                                          |

也可以为date_detection和numeric_detection自定义字段映射规则。也可以使用dynamic_templates去定义额外的动态字段映射规则。

### date_detection（日期检测）

如果date_detection开启（默认开启），当心的字段被检测后发现他们的内容匹配任何一个`dynamic_date_formats`参数中配置的日期表达式

，那么这个字段就被认为是日期类型。`dynamic_date_formats`默认是`[ "strict_date_optional_time"`,`"yyyy/MM/dd HH:mm:ss Z||yyyy/MM/dd Z"`]



![image-20240730152224435](../img/image-20240730152224435.png)

这时候date_detection参数，尽管数据内容满足dynamic_date_formats参数的格式，这时ES也会将字段当成String类型处理。

```
PUT my_d_mapping_001
{
  "mappings": {
    "date_detection": false
  }
}
```



![image-20240730152435858](../img/image-20240730152435858.png)

自定义探索日期格式

`dynamic_date_formats` 格式

1.  使用`,`f分割表示匹配任意一个即可，如果满足`dynamic_date_formats`配置的其中一个表达式，映射到Mapping的字段格式为匹配上的第一个。
2.  使用`||`f分割表示匹配任意一个即可，，如果满足`dynamic_date_formats`配置的其中一个表达式，映射到Mapping的字段格式为dynamic_date_formats表达式

```
PUT my_d_mapping_001
{
  "mappings": {
    "dynamic_date_formats": ["yyyy-MM-dd"]
  }
}
```

![image-20240730152849601](../img/image-20240730152849601.png)

```
PUT my_d_mapping_001
{
  "mappings": {
    "dynamic_date_formats": ["yyyy-MM-dd || MM/dd/yyyy "]
  }
}
```

![image-20240730153625687](../img/image-20240730153625687.png)

### numeric_detection（数值检测）

numeric_detection参数默认关闭，它可以检测字段的值是否满足数值类型，如果是将字段类型设置为对应的数值类型。

![image-20240730154128424](../img/image-20240730154128424.png)

```
PUT my_d_mapping_001
{
  "mappings": {
    "numeric_detection": true
  }
}
```

![image-20240730154222141](../img/image-20240730154222141.png)

## 动态映射模板

使用动态映射模板可以控制ES去做字段映射。**前提是dynamic设置为true或者runtime**。动态添加字段基于以下匹配条件：

1. ES根据`match_mapping_type` and `unmatch_mapping_type`这两个操作去做**字段类型**检测
2. ES根据`match` and `unmatch`这两个操作配置的表达式去做**字段名**检测
3. ES根据`path_match` and `path_unmatch`这两个操作在**字段的全路径**检测

如果一个动态模板， `match_mapping_type`, `match`, or `path_match`这三个操作都没定义，那将会匹配不上任何字段。也可以通过模板的名称指向一个模板。

使用映射规范中的`{name}`和`{dynamic_type}`模板变量作为占位符。

注意：默认情况下动态字段映射只有当这个字段有值时才会映射，如果这个字段的值是null或空数组，就不会在Mapping上增加这个字段的映射信息。但如果动态模板允许空值，则只有在第一个具有该字段具体值的文档被索引之后才会应用该选项。

```
  "dynamic_templates": [
    {
      "my_template_name": { //模板名称
        ... match conditions ...  //匹配条件match_mapping_type, match, match_pattern, unmatch, path_match, path_unmatch
        "mapping": { ... } //匹配字段应该使用的映射
      }
    },
    ...
  ]
```

### 校验动态模板

如果提供的映射包含无效的映射片段，则返回验证错误。验证发生在索引时应用动态模板时，在大多数情况下，发生在更新动态模板时。在某些情况下，提供无效的映射片段可能会导致动态模板的更新或验证失败:

1. 如果没有指定`match_mapping_type`，但是这个模版提供了最少一个的有效映射，那么这个映射片段是有效的。但是如果将与模版匹配的字段映射为其他类型，则在index时返回验证错误。例如：配置一个动态模版，不包含`match_mapping_type`，这样就是一个有效的字符串类型，但是如果有一个字段匹配动态模版时被匹配为`long`，那么在`index`时将返回验证错误。建议就是将`match_mapping_type`配置为预期的`JSON`类型（参考开头的映射关系表）或者在`mapping`中配置好所需的类型

![image-20240730161321682](../img/image-20240730161321682.png)

![image-20240730161648614](../img/image-20240730161648614.png)

2. 如果我们在`mapping`的片段中使用了`{name}`占位符，那么在动态模版的更新时是跳过验证的。这是因为当时的字段名还是不知道的，所以在`index`时进行验证

### 动态模板在dynamic: runtime时使用

如果你想让Elasticsearch动态地将某个类型的新字段映射为运行时字段，在索引映射中设置dynamic: runtime。这些字段不会被索引，并且在查询时从_source加载。**尽管您的字符串字段不会被索引，但它们的值存储在_source中，并可用于搜索请求、聚合、过滤和排序**。

除了dynamic: runtime，也可以在dynamic: true的条件下，单独设置某个字段为runtime字段。

![image-20240730164547688](../img/image-20240730164547688.png)

| 条件                   | 含义                                 | 示例                                                         |
| ---------------------- | ------------------------------------ | ------------------------------------------------------------ |
| `match_mapping_type`   | 字段类型符合的字段，按照模板映射     | "match_mapping_type": ["long", "double"] 或者"match_mapping_type": "*", |
| `unmatch_mapping_type` | 字段类型符合的字段，不按照模板映射   | "unmatch_mapping_type": "object",                            |
| `match`                | 字段名称符合的字段，按照模板映射     | "match": "count"或者 "match_pattern": "regex",   "match": "^profit_\d+$" （match_pattern参数调整match参数的行为，以支持在字段名上匹配完整的Java正则表达式，而不是简单的通配符） |
| `unmatch`              | 字段名称符合的字段，不按照模板映射   | "unmatch": "*_text"                                          |
| `path_match`           | 字段全路径符合的字段，按照模板映射   | "path_match":   "name.*",或"path_match":   ["name.*", "user.name.*"], |
| `path_unmatch`         | 字段全路径符合的字段，不按照模板映射 | "path_unmatch": "*.middle",或"path_unmatch": ["*.middle", "*.midinitial"], |



![image-20240730165814184](../img/image-20240730165814184.png)

![image-20240730170414921](../img/image-20240730170414921.png)

### 模板参数

{name}和{dynamic_type}占位符将在映射中用字段名和检测到的动态类型替换。下面的例子将所有字符串字段设置为使用与该字段同名的分析器，并禁用所有非字符串字段的doc_values:

![image-20240730171108382](../img/image-20240730171108382.png)

### 关闭norms

norms是打分因素之一，如果不关心分数，也不需要按照分数排序，可以把他关掉。**禁用索引中这些评分因素的存储，从而节省一些空间。![image-20240730171931483](../img/image-20240730171931483.png)

### 时序

使用ES做时序分析时，通常会有很多数值字段，他们呢会经常被聚合而不是过滤。在这个场景下，可以关闭对这些字段的索引，这样可以节约磁盘空间以及提升搜索速度。

![image-20240730173207575](../img/image-20240730173207575.png)

## 显示映射

ES除了动态映射字段，还可以显示的定义字段的标准。在创建index的时候，可以为这个index添加字段。

### 创建

```
PUT my_d_mapping_001
{
  "mappings": {
    "properties": {
      "age":{
        "type": "long"
      },
      "name":{
        "type": "text"
      }
    }
  }
}
```

![image-20240731110215934](../img/image-20240731110215934.png)

### 更新

1. 在原有的mapping上新增字段

```
PUT my_d_mapping_001/_mapping
{
  "properties":{
    "sex":{
      "type":"keyword"
    }
  }
}
```



![image-20240731110520393](../img/image-20240731110520393.png)

2. 不能修改已存在的mapping字段

如果需要修改，需要重建索引，然后将原来的数据移入新的索引下。

### 查询

1. 查询索引mapping

```
GET my_d_mapping_001/_mapping
```

![image-20240731161718025](../img/image-20240731161718025.png)

2. 查询索引指定的字段

```
GET my_d_mapping_001/_mapping/field/title
```

![image-20240731161809905](../img/image-20240731161809905.png)



## 运行时字段

运行时字段在被查询时评估，运行时字段可以让你：

1. 不需要重建索引就可以在已存在的文档上增加字段
2. 在不了解数据结构的情况下开始处理数据
3. 在查询时覆盖一个索引字段的返回值
4. 在不修改底层模式的情况下为特定用途定义字段

可以通过搜索的API访问任何一个字段，ES认为运行时字段没有什么不一样。你可以定义一个运行时字段在Mapping中或者在搜索请求中。

在`_search`API使用`fields`参数去检索需要的运行时字段的值，搜索的字段不会展示在`_source`中，但是 `fields` API 会在所有字段上生效，尽管一些字段不属于原始`_source`.

运行时字段在日志数据中很有用，尤其是当你不确定数据的结构时。您的搜索速度会降低，但只要你的索引足够的小并且您可以更快地处理数据那就不必为它们建立索引。

### 好处

1. 不会增加索引大小。因为运行时字段不会被索引。您可以直接在索引映射中定义运行时字段，从而节省存储成本并提高摄取速度。您可以更快地将数据摄取到Elastic Stack中并立即访问它。定义运行时字段后，可以立即在搜索请求、聚合、过滤和排序中使用它。

2. 快速提取运行时字段为索引字段。如果将运行时字段更改为索引字段，则不需要修改引用该运行时字段的任何查询。更好的是，您可以引用字段为运行时字段的某些索引，以及字段为索引字段的其他索引。您可以灵活地选择要索引哪些字段以及保留哪些字段作为运行时字段。

3. 无需预先定义数据结构。使用运行时字段随时修改映射。

4. 节约资源。使用运行时字段允许使用更小的索引和更快的摄取时间，从而使用更少的资源并降低操作成本。



### 优点

1. 仅对搜索命中的数据构建运行时字段。运行时字段可以取代许多使用`_search`API编写脚本的方法。运行时字段的使用方式受到所包含脚本运行的文档数量的影响。如果您正在使用_search API上的fields参数来检索运行时字段的值，那么脚本就像脚本字段一样，只针对top命中值运行。
2. 比脚本查询灵活。您可以使用脚本字段访问_source中的值，并根据脚本估值返回计算值。运行时字段具有相同的功能，但提供了更大的灵活性，因为您可以在搜索请求中查询和聚合运行时字段。脚本字段只能获取值。类似地，您可以编写一个脚本查询，根据脚本过滤搜索请求中的文档。运行时字段提供了一个非常相似的更灵活的特性。您编写了一个脚本来创建字段值，它们在任何地方都可用，例如字段、所有查询和聚合。您也可以使用脚本对搜索结果进行排序，但是相同的脚本在运行时字段中的工作方式完全相同。如果将脚本从搜索请求中的任何这些部分移动到从相同数量的文档计算值的运行时字段，则性能应该是相同的。这些特性的性能在很大程度上取决于所包含的脚本运行的计算以及脚本运行的文档数量。

### 使用索引字段和运行时字段的选择方案

1. 使用运行时可能会影响基于运行时脚本中定义的计算的搜索性能。搜索性能和灵活性是需要平衡的，大部分情况会在索引字段上进行查找和过滤。当运行一个请求时，ES自动地使用这些索引字段，并且快速的响应结果。您可以使用运行时字段来限制Elasticsearch需要计算值的字段数量。将索引字段与运行时字段一起使用，可以为索引的数据以及为其他字段定义查询的方式提供灵活性。
2. 使用异步搜索API来运行包含运行时字段的搜索。这种搜索方法有助于抵消计算包含该字段的每个文档中的运行时字段值对性能的影响。如果查询不能同步返回结果集，您将在结果可用时异步获得结果。

### 禁用运行时字段

使用运行时字段查询被认为是高消耗的查询，使用参数`search.allow_expensive_queries` 设置为false可以禁用掉使用运行时字段进行查询。

```
search.allow_expensive_queries:false
```

### 更新运行时字段

在Mapping中添加一个与需要更新的运行时字段名字相同的运行时字段，就可以覆盖掉原来的运行时字段。

### 删除运行时字段

直接将运行时字段设置为null

```
PUT my-index-000001/_mapping
{
 "runtime": {
   "day_of_week": null
 }
}
```

**当运行时字段依赖脚本在运行中计算返回值的时候，更新或删除运行时字段可能会返回不一致的结果。每个shard可以访问脚本的不同版本，这取决于映射更改生效的时间。**

### 映射一个运行时字段

通过在映射定义下添加一个运行时部分并定义一个Painless脚本来映射运行时字段。该脚本可以访问文档的整个上下文，包括通过params访问原始的_source。_source和任何映射字段及其值。在查询时，脚本运行并为查询所需的每个脚本字段生成值。

**如果`dynamic`设置为`runtime`，那么添加的字段都是动态运行时字段。**

1. 运行时字段可用数据类型

- `boolean`
- `composite`
- `date`
- `double`
- `geo_point`
- `ip`
- `keyword`
- `long`
- `lookup`

2. 忽略脚本处理运行时字段报错

脚本在进行处理时，可能会因为确实数据或者字段产生异常，可以使用`on_script_error`参数设置为`continue`,这时运行时字段产生的报错就不会影响l

#### 使用脚本定义运行时字段

![image-20240731173836957](../img/image-20240731173836957.png)

#### 不使用脚本定义运行时字段

使用Painless脚本定义一个运行时字段时典型的做法，然而也可以直接定义一个运行时字段通过脚本。例如想要从`_source`中不需要做任何改变检索一个字段，那就不需要脚本。

当没有提供脚本时，Elasticsearch在查询时隐式地在`_source`中查找与运行时字段同名的字段，如果存在，则返回一个值。如果具有相同名称的字段不存在，则响应不包含该运行时字段的任何值。

![image-20240731175134214](../img/image-20240731175134214.png)

在大多数情况下，尽可能通过doc_values检索字段值。使用运行时字段访问doc_values比从_source检索值要快，因为数据是如何从Lucene加载的。

当将`_source`关闭，不使用脚本定义运行时字段，数据就查询不到了。

![image-20240731180130298](../img/image-20240731180130298.png)

但使用脚本定义的运行时字段就可以查询到

![image-20240731180338768](../img/image-20240731180338768.png)

### 执行查询时定义运行时字段

在查询请求时，可以通过`runtime_mappings`参数定义一个运行时字段，这个字段只在这次请求中存在。定义方式和在Mapping中使用脚本定义运行时字段一样。

可以看到在第一个查询中定义的运行时字段，返回结果也可以查到运行时字段。第二个查询没有定义，也查询不到。

![image-20240801164525395](../img/image-20240801164525395.png)

### 使用其他运行时字段创建运行时字段

![image-20240801165020357](../img/image-20240801165020357.png)

### 使用查询重写字段值（敏感词搜索）

这个功能可以解决敏感词搜索问题。

场景：如果创建一个与Mapping字段相同名称的运行时字段名，那么这个运行时字段会覆盖Mapping字段，并按照你定义的脚本返回这个字段值。

要求：敏感词不能展示，但是可以被搜索。

如，定义敏感词为：“敏感”，此时搜索”敏感“二字可以将这个文本搜索出来，原始文本不变（毕竟敏感词多了全是*已经认不出原文了），但不能将敏感两个字在搜索结果中展示出来。

这条数据 {"text":"我是一个敏感的内容"}  搜索关键词“敏感”时，这条数据应展示为”我是一个**的内容“。

```
POST my_d_mapping_002/_bulk?refresh=true
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个敏感的内容"}  //搜索关键词“敏感”时，这条数据应展示为我是一个**的内容。
```

使用运行时字段在查询时覆盖掉原始原始字段text

```
POST my_d_mapping_002/_search
{
  "runtime_mappings": {
    "text": { //运行时字段与Mapping中需要被覆盖的字段名称相同。
      "type": "keyword",
      "script": {
        "source": //如果text文本中有敏感两个字
        """
emit(/[敏感]/.matcher(doc['text.keyword'].value).replaceAll('*'))  //表示如果text.keyword中包含“敏感”一词时，将“敏感”词变为”*“
          """
      }
    }
  },
  "query": {
    "query_string": {
      "default_field": "text.keyword",
       "query": "*敏感*"
    }
  }, 
  "fields": [
    "text"
  ]
}
```

结果可以看到这条数据被查询出来，且fields里的text已经脱敏展示。原本的_source内容没有改变。

![image-20240802155055713](../img/image-20240802155055713.png)

如果需要原始_source不展示，在Mapping中这时`_source` 参数为false

```
//注意，需要在创建索引时设置。
PUT my_d_mapping_002
{
  "mappings": {
    "_source": {
      "enabled": false
    }
  }
}
```

![image-20240802155508031](../img/image-20240802155508031.png)

#### 对比_update_by_query

脱敏处理还有一个API看起来好像也能做到那就是update_by_query。

这个API，顾名思义在查询时更新。那一起来看看这个区别。

```
PUT my_d_mapping_003/_bulk?refresh
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个可以正常返回的内容"}
{"index":{}}
{"text":"我是一个敏感的内容"}
```

调用_update_by_query,jiang 字段text进行敏感字替换。同样的这条数据 {"text":"我是一个敏感的内容"}  搜索关键词“敏感”时，这条数据应展示为”我是一个**的内容“。

```
POST my_d_mapping_003/_update_by_query
{
  "script": {
    "lang": "painless",
    "source": "ctx._source.text = /[敏感]/.matcher(ctx._source.text).replaceAll('*')"
  }
}
```

执行查询语句

```
GET my_d_mapping_003/_search
{
   "query": {
    "query_string": {
      "default_field": "text.keyword",
      "query": "***"
    }
  }
}
```

![image-20240802160415293](../img/image-20240802160415293.png)

看起来数据被查出来了还脱敏了。

实际上仔细想想我们之前的业务需求，除了需要脱敏，还需要保证原始文本内容不变，那么就看一下原始文本内容。

![image-20240802160613332](../img/image-20240802160613332.png)

原始文本已经被替换成了**。细心的人已经发现了，我第一次搜索的不是”脱敏“，而是”\*“，当我搜索”脱敏“一词是无法将文档搜索出来的。

![image-20240802160713462](../img/image-20240802160713462.png)

所以可以看到，_update_by_query这个API实际上是在搜索时将我们的文档进行了替换并查询出来，而使用运行时字段对原Mapping进行替换并不会改变原始的文档内容，还可以根据自己的需要判断是否返回原始内容。

#### Painless 语法继续扩展

通过刚才查询出来的内容可以看到这条数据 {"text":"我是一个敏感的内容"}  搜索关键词“敏感”时，这条数据应展示为”我是一个**的内容“。有两个\*。说明语法将“敏感”一词中的两个字分别进行了替换，但很多时候敏感的不是词是一个词或者一句话。所以我们需要脱敏后展示”我是一个\*的内容“

最有可能对结果产生影响的就是脚本里的内容了，让我们看脚本里都有哪些内容。

```
emit(/[敏感]/.matcher(doc['text.keyword'].value).replaceAll('*'))
```

1. emit() ：函数处理得到的返回值会赋值给我们定义好的运行时字段
2. /pattern/：是一个匹配的模型，在painless语法中使用双\表示一个模型，这里也支持Java中的正则表达式。
3. matcher()：匹配函数
4. doc['filed'].value：表示获取文档中这个字段的值，这里为什么用keyword呢，因为运行时字段支持的文本类型只有keyword。
5. replaceAl()：替换函数。

看了这些可以看到问题出现在匹配上，`/[敏感]/.matcher`将文本匹配成两个字分别进行后续的替换操作。那为什么不是词呢，关键就是`[]`这个中括号，敏感的程序员一下子就能想到，这表示数组。

没错，这里`/[敏感]/`在Painless 语法中被认为是由`敏`和`感`两个字组成的数组，所以替换后会有两个\*

可以验证一下，这里我再加入一个数据，这时我搜索敏，可以看到新的数据也被替换了

```
POST my_d_mapping_002/_bulk?refresh=true
{"index":{}}
{"text":"我是一个敏的内容"}
```

![image-20240802163814156](../img/image-20240802163814156.png)

那么如何修改呢，当然就是去掉中括号了

```
POST my_d_mapping_002/_search
{
  "runtime_mappings": {
    "text": {
      "type": "keyword",
      "script": {
        "source": //如果text文本中有敏感两个字
        """
emit(/敏感/.matcher(doc['text.keyword'].value).replaceAll('*'))
          """
      }
    }
  },
  "query": {
    "query_string": {
      "default_field": "text.keyword",
       "query": "*敏*"
    }
  }, 
  "fields": [
    "text"
  ]
}
```

再次搜索，新加的数据并没有被脱敏。

![image-20240802163957247](../img/image-20240802163957247.png)

![image-20240802164040601](../img/image-20240802164040601.png)

问题：如果有多敏感词呢？

### 搜索一个运行时字段

使用`_search`API中的`fields`参数可以将运行时字段的值搜索出来，运行时字段不会出现在`_source`字段中，但是`fields`参数可以返回所有的字段，不管这个字段是否在原始`_source`中。

使用通配符"*"表示返回所有字段。

```
GET my_d_mapping_001/_search
{
  "fields" : ["*"]
}
```

#### 从关联索引搜索fields //todo

注意：未来可能被删除

暂时不细究了。

```
POST ip_location/_doc?refresh
{
  "ip": "192.168.1.1",
  "country": "Canada",
  "city": "Montreal"
}

PUT logs/_doc/1?refresh
{
  "host": "192.168.1.1",
  "message": "the first message"
}

PUT logs/_doc/2?refresh
{
  "host": "192.168.1.2",
  "message": "the second message"
}

POST logs/_search
{
  "runtime_mappings": {
    "location": {
        "type": "lookup", 
        "target_index": "ip_location", 
        "input_field": "host", 
        "target_field": "ip", 
        "fetch_fields": ["country", "city"] 
    }
  },
  "fields": [
    "host",
    "message",
    "location"
  ],
  "_source": false
}
```

### 索引一个运行时字段

运行时字段可以在一个文本运行时被定义，例如在进行搜索时定义一个一运行时字段并返回。如果您决定为运行时字段建立索引以获得更高的性能，只需将完整的运行时字段定义(包括脚本)移动到索引映射的上下文中。ES将自动索引字段驱动查询，快速返回结果。这个能力意味着只需要定义一次脚本，每哥文本都能使用到这个运行时字段。

**注意：目前不支持索引复合运行时字段。**

可以使用运行时字段来限制Elasticsearch需要计算值的字段数量。将索引字段与运行时字段一起使用，可以为索引的数据以及为其他字段定义查询的方式提供灵活性。

**注意：索引运行时字段后，无法更新包含的脚本。如果需要更改脚本，请使用更新后的脚本创建一个新字段。**

`on_script_error`参数时可选参数，该参数决定如果脚本在索引时抛出错误(默认值)，是否拒绝整个文档。将值设置为ignore将在文档的_ignored元数据字段中注册该字段并继续索引。

```
PUT my_d_mapping_001/_mapping
{
  "runtime": {
    "voltage_corrected": {
      "type": "double",
       "on_script_error": "fail", 
      "script": {
        "source": """
        emit(doc['voltage'].value * params['multiplier'])
        """,
        "params": {
          "multiplier": 2
        }
      }
    }
  }
}
```

查询一样可以在fields中获取这个字段。

**与映射一个运行时字段不一样的是：映射一个运行时字段是在索引创建时一同创建，索引一个运行时字段是在索引已经创建后再创建一个运行时字段。**

### 从运行时字段导出数据

考虑要从中提取字段的大型日志数据集。为数据建立索引非常耗时，而且会占用大量磁盘空间，并且只需要数据结构，不需要预先定义数据模式。你知道你的日志数据中包含你想要提取的指定字段。通过使用运行时字段你可以定义脚本在搜索时获取到这些字段值。

1. 定义一个Index的Mapping
2. 放入一些数据
3. 查看索引
4. 定义一个运行时字段通过grok pattern或者dissect pattern解析文本里想要提取的字段

```
//1. 定义Mapping
PUT my_d_mapping_001
{
  "mappings": { 
    "properties": {
      "@timestamp": {
        "format": "strict_date_optional_time||epoch_second", 
        "type": "date"
      },
      "message": {
        "type": "wildcard"
      }
    }
  }
}
//2. 插入APACHE日志格式的数据
POST my_d_mapping_001/_bulk?refresh
{"index":{}}
{"timestamp":"2020-04-30T14:30:17-05:00","message":"40.135.0.0 - - [30/Apr/2020:14:30:17 -0500] \"GET /images/hm_bg.jpg HTTP/1.0\" 200 24736"}
{"index":{}}
{"timestamp":"2020-04-30T14:30:53-05:00","message":"232.0.0.0 - - [30/Apr/2020:14:30:53 -0500] \"GET /images/hm_bg.jpg HTTP/1.0\" 200 24736"}
{"index":{}}
{"timestamp":"2020-04-30T14:31:12-05:00","message":"26.1.0.0 - - [30/Apr/2020:14:31:12 -0500] \"GET /images/hm_bg.jpg HTTP/1.0\" 200 24736"}
{"index":{}}
{"timestamp":"2020-04-30T14:31:19-05:00","message":"247.37.0.0 - - [30/Apr/2020:14:31:19 -0500] \"GET /french/splash_inet.html HTTP/1.0\" 200 3781"}
{"index":{}}
{"timestamp":"2020-04-30T14:31:22-05:00","message":"247.37.0.0 - - [30/Apr/2020:14:31:22 -0500] \"GET /images/hm_nbg.jpg HTTP/1.0\" 304 0"}
{"index":{}}
{"timestamp":"2020-04-30T14:31:27-05:00","message":"252.0.0.0 - - [30/Apr/2020:14:31:27 -0500] \"GET /images/hm_bg.jpg HTTP/1.0\" 200 24736"}
{"index":{}}
{"timestamp":"2020-04-30T14:31:28-05:00","message":"not a valid apache log"}

```

#### 使用Grok pattern解析文本数据

grok时一个正则表达式的另一种说法，支持可重用的别名表达式。Grok对处理syslog日志很友好，Apache和其他的web服务日志、mysql日志、以及一般任何为人类而不是计算机编写的日志格式。

Grok位于Oniguruma正则表达式库之上，所以任何正则表达式再grok中都时合法的。grok使用正则表达式去允许名称已经存在表达式以及将它们组合成为更复杂的表达式去比配你的字段。

ES内置了许多预定义的grok表达式去简化工作，重用grok模式的语法采用以下形式之一:

| `%{SYNTAX}`                                                  | `%{SYNTAX:ID}`                                               | `%{SYNTAX:ID:TYPE}`                                          |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 与文本匹配的模式的名称.例如，NUMBER和IP都是默认模式集中提供的模式。NUMBER模式匹配像3.44这样的数据，IP模式匹配像55.3.244.1这样的数据。 | 指定要匹配的文本的标识符。例如，3.44可能是事件的持续时间，因此您可以将其称为duration。字符串55.3.244.1可以标识发出请求的客户端。 | 要转换命名字段的数据类型。支持Int、long、double、float和Boolean类型。 |

例如：3.44 55.3.244.1 使用grok解析 `%{NUMBER:duration} %{IP:client}`

//todo https://www.elastic.co/guide/en/elasticsearch/reference/current/grok.html

```
GET /my_d_mapping_001

//3. 在索引上定义运行时字段http.client_ip
//脚本含义：使用grok内置的APACHE日志解析模板，提取message字段中的值，返回clientip
//          如果clientip!=null 输出clientip
//          if (clientip != null) emit(clientip); 没有这个判断时，后续的任何分片上插入的数据一旦不符合脚本的grok模式就会失败
//                                                有了这个判断后如果不符合查询会跳过这个数据
PUT my_d_mapping_001/_mappings
{
  "runtime": {
    "http.client_ip": {
      "type": "ip",
      "script": """
        String clientip=grok('%{COMMONAPACHELOG}').extract(doc["message"].value)?.clientip;
        if (clientip != null) emit(clientip); 
      """
    }
  }
}
GET my_d_mapping_001/_search
{
    "query": {
    "match": {
      "http.client_ip": "252.0.0.0"
    }
  },
  "fields" : ["http.client_ip"]
}

//或者也可以在搜索时定义运行时字段
GET my_d_mapping_001/_search
{
  "runtime_mappings": {
    "http.clientip": {
      "type": "ip",
      "script": """
        String clientip=grok('%{COMMONAPACHELOG}').extract(doc["message"].value)?.clientip;
        if (clientip != null) emit(clientip);
      """
    }
  },
  "query": {
    "match": {
      "http.clientip": "40.135.0.0"
    }
  },
  "fields" : ["http.clientip"]
}
//定义一个复杂的运行时字段，通过一个脚本输出多个字段
//脚本含义：使用grok内置的APACHE日志解析模板，提取message字段中的值，返回解析模板所有内置返回的值
//          clientip、verb、response
PUT my_d_mapping_001/_mappings
{
  "runtime": {
    "http": {
      "type": "composite",
      "script": "emit(grok(\"%{COMMONAPACHELOG}\").extract(doc[\"message\"].value))",
      "fields": {
        "clientip": {
          "type": "ip"
        },
        "verb": {
          "type": "keyword"
        },
        "response": {
          "type": "long"
        }
      }
    }
  }
}
```



![image-20240805141431349](../img/image-20240805141431349.png)

#### 使用Dissect pattern解析文本数据

与Grok pattern不同的是Dissect pattern不是使用正则表达式去解析文本提取结构字段的。它使用解剖语法更为简单在一些场景下比Grok pattern解析更快。

Dissect pattern 更适合数据格式固定不变的文本。

```
1.2.3.4 - - [30/Apr/1998:22:00:52 +0000] \"GET /english/venues/cities/images/montpellier/18.gif HTTP/1.0\" 200 3171
```

Dissect pattern 每个%{}之间使用空格分割，表示解析上面的文本也是根据空格分割，匹配到的内容就是%{}内定义的字段的值

```
%{clientip} %{ident} %{auth} [%{@timestamp}] \"%{verb} %{request} HTTP/%{httpversion}\" %{status} %{size}
```

//todo https://www.elastic.co/guide/en/elasticsearch/reference/current/dissect-processor.html

```
PUT my_d_mapping_001/_mappings
{
  "runtime": {
    "http.client.ip": {
      "type": "ip",
      "script": """
        String clientip=dissect('%{clientip} %{ident} %{auth} [%{@timestamp}] "%{verb} %{request} HTTP/%{httpversion}" %{status} %{size}').extract(doc["message"].value)?.clientip;
        if (clientip != null) emit(clientip);
      """
    }
  }
}
PUT my_d_mapping_001/_mappings
{
  "runtime": {
    "http.responses": {
      "type": "long",
      "script": """
        String response=dissect('%{clientip} %{ident} %{auth} [%{@timestamp}] "%{verb} %{request} HTTP/%{httpversion}" %{response} %{size}').extract(doc["message"].value)?.response;
        if (response != null) emit(Integer.parseInt(response));
      """
    }
  }
}
GET my_d_mapping_001/_search
{
  "query": {
    "match": {
      "http.responses": "304"
    }
  },
  "fields" : ["http.client_ip","timestamp","http.verb"]
}

```

![image-20240805142411958](../img/image-20240805142411958.png)

## 移除Mapping的_type字段

**7.0.0之后的版本不在支持自定义Mapping 的_type**

### _type

7.0.0之前支持一个索引对应多个mapping类型，也就是一个索引可以有不同的字段。

`_type` 字段与文档的 `_id` 字段结合生成`_uid` 字段，所以允许相同的`_id`不同的  `_type` 存在同一个索引下。

`_type` 也被用于父子关系的文档，也就是一个文档的 `question` 类型的父文档可能是 `answer`

### 为什么移除

最开始，解释ES的index类似于database，type类似于table。实际上这时一个这是一个错误的类比，会导致错误的假设。在SQL数据库中，表是相互独立的。

一个表中的列与另一个表中同名的列没有关系，但这与ES的mapping的Type完全不一样。统一索引下不同的mapping type中**相同字段名称的字段实际上是同一个的，在内部由相同的Lucene字段支持，所以这两个type中相同的字段必须是相同的类型**。

因此，如果想要**删除一个类型下与其他类型相同的字段时，会失败**。

**最重要的是，在同一个索引中存储具有很少或没有共同字段的不同实体会导致数据稀疏，并干扰Lucene有效压缩文档的能力**。

总结：

1. 相同index不同Type的相同字段内部由相应的lucene字段支持，可不单独删除
2. 相同index不同Type中不同字段过多会导致数据稀疏，降低lucene压缩能力



## 元数据字段

### _source

`_source`字段包含在索引时传递的原始JSON文档主体。这个**`_source`字段本身不会被索引，因此是不可搜索的**，但是因为他被存储起来所以当执行了get或者search请求的时候也可以返回。

如果对磁盘利用率要求高的话，`_source`设置为synthetic可以减少磁盘使用，但代价是只支持映射的子集和较慢的读取，或者(不推荐)禁用`_source`字段，这也会减少磁盘使用，但会禁用许多特性。

#### synthetic

synthetic通常在时序数据索引下使用（此时index.mode设置成time_series）。对于其他的索引这个参数属于技术预览阶段。未来可能改变或者移除。

`_source`虽然非常方便，但这个字段占用了磁盘上的大量空间。所以当 `_source`的`mode设置为 synthetic` 时，Elasticsearch可以**在检索时动态地重建源内容**，而不是在发送源文档时将它们完全存储在磁盘上。虽然这种动态重建通常比逐字保存源文档并在查询时加载它们要慢，但它节省了大量存储空间。

设置方法：

```
PUT idx
{
  "mappings": {
    "_source": {
      "mode": "synthetic"
    }
  }
}
```

限制：

1. 当设置为synthetic后检索_source文本信息是，与原始的数据JSON会有一些不同。

- 数组数据子字段丢失

![image-20240805152027636](../img/image-20240805152027636.png)

- 字段名称改变

![image-20240805152241446](../img/image-20240805152241446.png)

- 按字母顺序排序

动态重组后的source数据会根据字段的字母排序返回，也会与原始的字段顺序不同。

![image-20240805152430557](../img/image-20240805152430557.png)

- 删除字段类型为keyword的重复数据

![image-20240805161602259](../img/image-20240805161602259.png)

如果将store这是为true则不会删除重复数据。

![image-20240805161655441](../img/image-20240805161655441.png)

增加参数ignore_above被忽略的数据也会返回，但会放在最后。比如ignore_above为3，bang长度大于3，所以被放在最后。

![image-20240805162111577](../img/image-20240805162111577.png)

- 字段类型使用text

重新排序文本字段可能会对短语和跨度查询产生影响。有关更多细节，请参阅关于position_increment_gap的讨论。您可以通过确保短语查询上的slop参数低于position_increment_gap来避免这种情况。这是默认值。



没有子类型为keyword的text类型不支持设置为synthetic

![image-20240805170922474](../img/image-20240805170922474.png)

字段类型是text，子字段类型是keyword

![image-20240805170727585](../img/image-20240805170727585.png)

字段类型是text，也可以开启store参数，设置source为synthetic。并不删除重复数据。

![image-20240805171356405](../img/image-20240805171356405.png)

2. 只有索引使用的是以下类型的字段才能使用这个参数。

- aggregate_metric_double
- boolean
- byte
- date
- date_nanos
- dense_vector
- double
- flattened
- float
- geo_point
- half_float
- histogram
- integer
- ip
- keyword
- long
- scaled_float
- short
- text
- version
- wildcard

#### 禁用_source字段

`_source`虽然非常方便，但这个字段占用了磁盘上的大量空间。所以可以将他禁用。

```
PUT my-index-000001
{
  "mappings": {
    "_source": {
      "enabled": false
    }
  }
}
```

如果禁用`_source`字段而不考虑后果，然后后悔。如果`_source`字段不可用，则不支持以下功能:禁用前需要注意：

1.  update、update_by_query以及reindex这些API都将不支持
2.  使用Kibana的Discover工具是字段将无法展示
3.  高亮功能不能正常使用
4.  从一个Elasticsearch索引重新索引到另一个索引的能力，无论是更改映射或分析，还是将索引升级到新的主要版本都无法使用。
5.  通过查看索引时使用的原始文档来调试查询或聚合的能力。
6.  自动修复索引损坏的能力。

**如果需要考虑磁盘空间，不如提高压缩级别，而不是禁用_source。**

#### 选择哪些字段保留在_source

只有专家才能使用的特性是，在文档被索引之后，但在存储`_source`字段之前，选择哪些字段保留在_source。移除的字段也同样会再也不存储，所以从一个Elasticsearch索引重新索引到另一个索引的能力，无论是更改映射或分析，还是将索引升级到新的主要版本都无法使用。所以需要慎重。

```
PUT logs
{
  "mappings": {
    "_source": {
      "includes": [
        "*.count",
        "meta.*"
      ],
      "excludes": [
        "meta.description",
        "meta.other.*"
      ]
    }
  }
}
```

### _routing

文档被路由到索引中的特定分片使用以下公式:

```
routing_factor = num_routing_shards / num_primary_shards
shard_num = (hash(_routing) % num_routing_shards) / routing_factor
```

num_routing_shards是索引`number_of_routing_shards`参数的值。

um_primary_shards是索引`number_of_shards`参数的值。

`_routing`默认情况下是索引的`_id`的值。可以通过为每个文档指定自定义路由值来实现自定义路由模式

#### 自定义路由

**主要功能：可以指定数据路由到哪个分片上，并根据路由查到该分片的数据**

![image-20240806184120732](../img/image-20240806184120732.png)

**数据流不支持自定义路由，除非在模板中启用了allow_custom_routing设置。**

在搜索中使用自定义路由，这个搜索请求将只在指定的路由值相关联的分片上执行。

**如果只有一个分片却指定多个路由可能存在问题**

```
GET my-index-000001/_search?routing=user1,user2 
{
  "query": {
    "match": {
      "title": "document"
    }
  }
}
```

#### 强制指定路由参数

在使用自定义路由时，在索引、获取、删除或更新文档时提供路由值是很重要的。忘记路由值可能导致文档在多个分片上被索引。为了安全起见，_routing字段可以配置为所有CRUD操作所需的自定义路由值:

```
PUT my-index-000002
{
  "mappings": {
    "_routing": {
      "required": true 
    }
  }
}
```

![image-20240806185048582](../img/image-20240806185048582.png)

注意

1. 自定义路由的ID唯一问题：当索引指定自定义_routing的文档时，不能保证在索引中的所有分片中_id的唯一性。事实上，如果使用不同的_routing值进行索引，具有相同_id的文档可能会在不同的分片上结束。所以**需要由用户来确保id在整个索引中是唯一的**

#### 路由到分区索引

可以配置索引，这样自定义路由值将转到分片的一个子集，而不是单个分片。这有助于降低最终导致集群不平衡的风险，同时仍然减少搜索的影响。这是通过提供索引级别设置索引来实现的。创建索引时的`Routing_partition_size`随着分区大小的增加，数据的分布将变得更加均匀，但代价是每个请求必须搜索更多的分片。

使用这个参数时，路由公式：

```
routing_value = hash(_routing) + hash(_id) % routing_partition_size
shard_num = (routing_value % num_routing_shards) / routing_factor
```

`_routing`字段用于计算索引中的一组分片，然后使用`_id`在该集合中选择一个分片。

**要启用此特性，请执行索引。Routing_partition_size的值应该大于1，小于index.number_of_shards。**

一旦启用，分区索引将有以下限制:

1. 不能在其中创建具有连接字段关系的映射。

2. 索引中的所有映射都必须将_routing字段标记为必需的。

### _doc_count

分桶聚合经常返回一个字段doc_count ，该字段显示每个桶中聚合和分区的文档数量。计算doc_count这个值很简单，doc_count在同个每个桶收集每个文档的适合就会加1。

虽然这种简单的方法在计算单个文档的聚合时是有效的，但它不能准确地表示存储预聚合数据的文档(如含有 `histogram` 或`aggregate_metric_double`字段)，因为一个汇总字段可能表示多个文档。

为了在使用预聚合数据时能够正确的计算文档数量，可以使用元数据字段 `_doc_count` ， `_doc_count` 必须始终是一个正整数，表示在单个摘要字段中聚合的文档数量。

当字段 `_doc_count` 被添加到文档中时，所有桶聚合将以它的值为主，此时桶的文档数不再是加1而是加上`_doc_count` 的值。，如果没有这个字段，默认为1。

注意：

1.  `_doc_count` 是整数，不允许使用嵌套数组
2.  `_doc_count` 不填写默认为1
3.  `_doc_count` 在插入文档时增加，主要用在含有 `histogram` 或`aggregate_metric_double`字段的文档

```
DELETE my_d_mapping_001
PUT my_d_mapping_001
{
  "mappings" : {
    "properties" : {
      "my_histogram" : {
        "type" : "histogram"
      },
      "my_text" : {
        "type" : "keyword"
      }
    }
  }
}

PUT my_d_mapping_001/_doc/2
{
  "my_text" : "histogram_2",
  "my_histogram" : {
      "values" : [0.1, 0.25, 0.35, 0.4, 0.45, 0.5],
      "counts" : [8, 17, 8, 7, 6, 2]
   },
  "_doc_count": 62 
}
PUT my_d_mapping_001/_doc/3
{
  "my_text" : "histogram_3",
  "my_histogram" : {
      "values" : [0.21],
      "counts" : [1]
   }
}
```



![image-20240806173254261](../img/image-20240806173254261.png)

### _field_names（8.0废弃字段）

`_field_names` 字段被使用在索引每个在文档里的字段的值除了null值。exists查询使用该字段来查找具有或不具有特定字段的非空值的文档。目前 `_field_names` 与 `doc_values` 和`norms` 字段互斥。对于exists查询而言可以使用 `doc_values` 和`norms` 但不能同时使用`_field_names` 。

现在不再需要禁用`_field_names` 。**它现在默认启用**，因为它不再像以前那样承担索引开销。**8.0之后取消了对禁用_field_names的支持**，**8.0之前如果使用了`doc_values` 和`norms` 或者不需要做exist查询可以禁用，但不推荐**

```
PUT my_d_mapping_001
{
  "mappings" : {
    "properties" : {
     
      "my_text" : {
        "type" : "keyword"
      }
    },
    "_field_names": {
      "enabled": true
    }
  }
}
```

![image-20240806175317341](../img/image-20240806175317341.png)

![image-20240806175725950](../img/image-20240806175725950.png)

### _ignored

`_ignored` 字段标识索引并存储文档中需要被忽略的每个字段的名称。例如，当字段格式错误且`ignore_malformed`被打开时，当keyword字段的值超过其可选的`ignore_above`设置时，或者当字段数量已达到`index.mapping.total_fields`限制，`index.mapping.total_fields`和`Ignore_dynamic_beyond_limit`设置为true。

`_ignored` 字段在term、terms、exists查询中可以被搜索的，返回在hits中。

**主要功能：查到被忽略的文档**

![image-20240806181337709](../img/image-20240806181337709.png)

### _id

每个文档的`_id `都是唯一标识，可以通过id进行ids查询查找数据。`_id `在索引时就被分配好了，或者根据ES自动生成，这个字段不是在Mapping中配置的。

`_id `可以被很多查询支持，如 `term`, `terms`, `match`, and `query_string`.

![image-20240806181750778](../img/image-20240806181750778.png)

**`_id`字段被限制在聚合、排序和脚本中使用。如果需要对`_id`字段进行排序或聚合，建议将`_id`字段的内容复制到另一个启用了`doc_values`的字段中。**

### _index

在跨多个索引执行查询时，有时需要添加仅与某些索引的文档相关联的查询子句。 `_index` 字段允许匹配文档被索引到的索引。它的值在某些查询和聚合，排序或脚本时是可访问的

**主要功能：多索引查询时根据索引过滤**

![image-20240806182442582](../img/image-20240806182442582.png)

![image-20240806182611150](../img/image-20240806182611150.png)

### _meta

映射类型可以具有与之关联的自定义元数据。Elasticsearch根本不使用这些，但可以用来存储特定于应用程序的元数据，例如文档所属的类

**主要功能：自定义元数据，一般不适用**

```
PUT my-index-000001
{
  "mappings": {
    "_meta": { 
      "class": "MyApp::User",
      "version": {
        "min": "1.0",
        "max": "1.3"
      }
    }
  }
}
//更新
PUT my-index-000001/_mapping
{
  "_meta": {
    "class": "MyApp2::User3",
    "version": {
      "min": "1.3",
      "max": "1.5"
    }
  }
}
```

### _tier

跨索引查询时，执行查询根据索引所在的节点所属的层 (`data_hot`, `data_warm`, `data_cold` or `data_frozen`等).

可以在重写为术语查询的任何查询中使用_tier字段，例如match、query_string、term、terms或simple_query_string查询，以及前缀和通配符查询。但是，它不支持regexp和模糊查询。

**主要功能：在分层数据中查找**

```
PUT index_1/_doc/1
{
  "text": "Document in index 1"
}

PUT index_2/_doc/2?refresh=true
{
  "text": "Document in index 2"
}

GET index_1,index_2/_search
{
  "query": {
    "terms": {
      "_tier": ["data_hot", "data_warm"] 
    }
  }
}
```

