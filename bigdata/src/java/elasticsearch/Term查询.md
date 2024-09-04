# Terms 查询
可以通过terms-level-query去查询一个精确的值，例如日期范围、IP、价格或者ID。
与 full-text-query查询不同，term查询是不需要分词得，它是去精确的匹配存储在这个字段里的值。
## Exist query
返回包含查询字段的文档。
一个被索引的值可能不存在在一个文档中的理由可能是：
1. 这个字段的值是NULL或者[]
2. 这个字段在mapping中开启了配置`"index":false`以及`"doc_values":false`、
3. 这个字段在mapping中设置了`ignore_above`
4. 这个字段值是`malformed`,且在mapping中定义了`ignore_malformed`
```
POST my_d_mapping_001/_doc
{
  "my_text":"i love china"
}
POST my_d_mapping_001/_doc
{
  "my_text":null
}

GET my_d_mapping_001/_search
{
  "query": {
    "exists": {
      "field": "my_text"
    }
  }
}

```
![img_23.png](img_23.png)
如果想要找到不存在这个字段的所有文档，可以使用must_not配合使用
```
GET my_d_mapping_001/_search
{
  "query": {
    "bool": {
      "must_not": [
        {
          "exists": {
            "field": "my_text"
          }
        }
      ]
    }
  }
}

```
![img_24.png](img_24.png)
参数：
field  必填。字段名称，如果这个字段的值是null或者[]，都会被视为不存在。所以为了避免这个情况可以自定义一个空值或者给空字符串"".

## Fuzzy query
返回的文档包含搜索词的相似词，比如搜索box，文档包含fox的也会被匹配上等。
**注意：如果`search.allow_expensive_queries`设置为false，则不能使用Fuzzy query**

参数
1. filed: 必填。字段名称
2. fuzziness：选填。允许匹配的最大编辑距离。
3. max_expansions：选填。允许创建最大的变体数，默认50.需要注意的是，这个值要避免设置的过大。如果这个值很大会影响性能，尤其是prefix_length为0的时候。
4. prefix_length：选填。告知从第几个字母开始模糊，默认是0.
5. transpositions：是否支持邻位字母换位，如ab->ba。默认true
6. rewrite：选填。重写查询。
```
POST my_d_mapping_001/_doc
{
  "my_text":"three"
}
POST my_d_mapping_001/_doc
{
  "my_text":"tree"
}
POST my_d_mapping_001/_doc
{
  "my_text":null
}

GET my_d_mapping_001/_search
{
  "query": {
   "fuzzy": {
     "my_text": {
       "value": "tree",
       "fuzziness": "AUTO",
       "max_expansions":"50",
       "prefix_length":0,
       "transpositions":true,
       "rewrite":"constant_score_blended"
     }
   }
  }
}

GET my_d_mapping_001/_search
{
  "query": {
   "fuzzy": {
     "my_text": {
       "value": "tree",
       "fuzziness": "AUTO",
       "max_expansions":"50",
       "prefix_length":2,
       "transpositions":true,
       "rewrite":"constant_score_blended"
     }
   }
  }
}

```
![img_25.png](img_25.png)

## IDS query
返回的文档都基于他们的ID，也就是根据_id字段对文档的ID进行查询。

```
POST my_d_mapping_001/_doc
{
  "my_text":"three"
}
POST my_d_mapping_001/_doc
{
  "my_text":"tree"
}
POST my_d_mapping_001/_doc
{
  "my_text":null
}

GET my_d_mapping_001/_search
{
  "query": {
   "ids": {
     "values": ["POUevJEBk-WCeltBP0bU"]
   }
  }
}

```
![img_26.png](img_26.png)

## Prefix query
返回的文档中包含指定字段且该字段的值包含指定的前缀。
**注意：如果`search.allow_expensive_queries`设置为false，则不能使用Prefix query。但是如果`index_prefixes`是开启的，则会优化查询，就不会被认为是慢的，且可以执行。因为`index_prefixes`后ES在段度的字段中索引前缀，这可以让ＥＳ以更大的索引为代价，让前缀查询更快。**


参数
1. filed: 必填。字段名称
2. rewrite：选填。重写查询
3. case_insensitive： 选填。当设置为true时，允许值与索引字段不区分ASCII大小写的匹配。（这意味着匹配的大小写敏感性取决与底层字段的映射）默认false。

```
POST my_d_mapping_001/_doc
{
  "my_text":"three"
}
POST my_d_mapping_001/_doc
{
  "my_text":"tree"
}
POST my_d_mapping_001/_doc
{
  "my_text":null
}

GET my_d_mapping_001/_search
{
  "query": {
    "prefix": {
      "my_text": {
        "value": "t",
          "case_insensitive":true
      }
    }
  }
}

GET my_d_mapping_001/_search
{
  "query": {
    "prefix": {
      "my_text": {
        "value": "TH",
          "case_insensitive":true
      }
    }
  }
}
GET my_d_mapping_001/_search
{
  "query": {
    "prefix": {
      "my_text": {
        "value": "TH",
        "case_insensitive":false
      }
    }
  }
}

```
![img_27.png](img_27.png)

## Range query
返回的文档在查询的范围之间。
**注意：如果`search.allow_expensive_queries`设置为false，则对text或者keyword字段类型使用range查询不会执行。**


参数
1. filed: 必填。字段名称
2. gt\gte\lt\lte\：选填。大于、大于等于、小于、小于等于。如果是进行日期查询也可以使用函数公式如`now/d 表示今天，now-1d/d 表示昨天 `
3. format：选填。日期格式，会将日期字段进行转换后查询，默认条件下会按照创建索引是为date类型的字段使用的日期合适，这个字段的值会覆盖mapping的设置。
4. relation：选填。指示让range查询如何生效。
```
INTERESCTS：表示返回与查询范围有交集的文档
CONTAINS：表示返回完全包含查询范围的文档
WITHIN：表示返回完全被查询范围包含的文档

```
5. time_zone：选填。时区转换。
```
POST my_d_mapping_001/_doc
{
  "my_text":[0,1]
}
POST my_d_mapping_001/_doc
{
  "my_text":[9,20]
}
POST my_d_mapping_001/_doc
{
  "my_text":[11,20]
}
POST my_d_mapping_001/_doc
{
  "my_text":[0,20]
}


GET my_d_mapping_001/_search
{
  "query": {
  "range": {
    "my_text": {
      "gte": 0,
      "lte": 10,
      "relation":"INTERSECTS"
    }
  }
  }
}

```
![img_28.png](img_28.png)
