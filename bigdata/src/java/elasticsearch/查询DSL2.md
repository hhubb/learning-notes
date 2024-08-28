## Match phrase query
匹配短语查询
参数
1. `field` : 必要参数、顶级参数，指定查询哪个字段。
2. `query` : 指定查询的短语文本
3. `analyzer` 选填，指定使用哪个分词器分词，默认使用`field`使用的分词器
4. `stop` 选填，可以允许短语中多出几个词
5. `zero_terms_query` 选填，与match一样
```
POST my_d_mapping_001/_doc
{
  "my_text":"my favorite food is hot water",
  "content":"cold porridge"
}
POST my_d_mapping_001/_doc
{
 "my_text": "my favorite food is salty hot a porridge ",
  "content":"hot porridge"
}
POST my_d_mapping_001/_doc
{
 "my_text": "my favorite food is salty hot  porridge ",
  "content":"hot porridge"
}
POST my_d_mapping_001/_doc
{
 "my_text": "my favorite food is salty hot water  porridge ",
  "content":"hot porridge"
}
POST my_d_mapping_001/_doc
{
 "my_text": "my favorite food is salty hot water water  porridge ",
  "content":"hot porridge"
}
GET my_d_mapping_001/_search
{
  "query": {
   "match": {
     "my_text": "hot porridge"
   }
  }
}

GET my_d_mapping_001/_search
{
  "query": {
   "match_phrase": {
     "my_text": {
       "query": "hot porridge"
     }
   }
  }
}

GET my_d_mapping_001/_search
{
  "query": {
   "match_phrase": {
     "my_text": {
       "query": "hot porridge",
       "slop": 1
     }
   }
  }
}
```
与Match区别：Match查询分词后，只有文档中有一个词满足就会返回，但Match phrase必须两个词挨着也就是以短语的形式存在才会返回。
![img_30.png](img_30.png)
![img_15.png](img_35.png)
stop的作用：允许短语中有几个词间隔
![img_16.png](img_36.png)
## Match phrase prefix query
匹配短语前缀查询，与短语匹配基本相同，文档必须包含这个短语并且按照相同的顺序，但最后一个字可以模糊匹配。
注意：最后一个模糊词会在index词的字典中按照顺序获取前50个，如果你想要搜索的词并不在前50个就会搜不到，所以会有一些歧义。但这通常不是问题，因为用户会不断输入文本让查找变得精确（个人认为适合在搜索框里做搜索的提示词）
参数
1. `field` : 必要参数、顶级参数，指定查询哪个字段。
2. `query` : 指定查询的短语文本
3. `analyzer` 选填，指定使用哪个分词器分词，默认使用`field`使用的分词器
4. `max_expansions` 选填，根据最后一个词最大可模糊匹配的词数，默认50个。
5. `stop` 选填，可以允许短语中多出几个词
6. `zero_terms_query` 选填，与match一样
```
GET my_d_mapping_001/_search
{
  "query": {
   "match": {
     "my_text": "hot porridge"
   }
  }
}
```
![img_17.png](img_37.png)

## Combined fields
组合字段搜索：可以同时搜索多个字段。
注意：`indices.query.bool.max_clause_count` 参数对搜索的字段数量限制，默认4096个字段

参数
1. `fields` : 必要参数、是数组，可以为每个字段分别设置打分系数。如：` "fields" : [ "title^2", "body" ]`
2. `query` : 指定查询的文本
3. `analyzer` 选填，指定使用哪个分词器分词，默认使用`field`使用的分词器
4. `auto_generate_synonyms_phrase_query` 选填，与match一样
5. `operator` 选填，与match一样 注意：这里的and和or与match一样，并不是代表需要在每一个字段里都存在
6. `minimum_should_match` 选填，与match一样
7. `zero_terms_query` 选填，与match一样
![img_18.png](img_38.png)
   //todo 与Multi-match区别
## Multi-match query
多匹配查询
//todo


## Query string query
可以使用query_string查询创建一个复杂的搜索，其中包括通配符、跨多个字段的搜索等等。虽然用途广泛，但查询是严格的，如果查询字符串包含任何无效语法，则返回错误。
参数
1. `query` :  必要参数、提供的查询内容需要追寻查询语法。就是一些的lucene语法，具体查询语法见（https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html#query-string-syntax）
2. `default_field` : 选填、指定查询哪个字段。默认值是索引的配置`index.query.default_field`指定，这个配置是`*` ,表示所有符合匹配搜索的字段都会被查询（不包括 nested 文档， nested 文档需要用 nested 查询）。一次可以查询的字段次数是有限制的，可以设置索引配置`indices.query.bool.max_clause_count`。
3. `allow_leading_wildcard` : 选填,默认true。是否允许query里的查询内容以通配符`*或者？`开头
4. `analyze_wildcard`: 选填,默认false。如果为true，查询将尝试分析查询字符串中的通配符
5. `analyzer` 选填，指定使用哪个分词器分词，默认使用`field`使用的分词器
6. `auto_generate_synonyms_phrase_query` 选填，默认true，如果是true表示开启使用synonym_graph令牌过滤器进行多词同义词扩展，将query中的文本信息找到同义词，一起进行多terms查找，如`ny`与`new york`是同意词，搜索的时候除了匹配包含`ny`的文档，也会匹配包含`new york`的文档，约等于查询`ny OR (new AND york)`
7. `boost` ：选填。分数系数，默认1
8. `enable_position_increments` 选填,默认true。如果为true，则在从query_string搜索构造的查询中启用位置增量
9. `fields` : 必要参数、是数组，可以为每个字段分别设置打分系数。如：` "fields" : [ "title^2", "body" ]`；也支持`*`，进行多字段查询时，也可以使用与多字段查询一样的参数。
10. `max_determinized_states` 选填,默认1000。查询所需的自动机状态的最大数目。（感觉和Lucene语法有关）Elasticsearch内部使用Apache Lucene来解析正则表达式。Lucene将每个正则表达式转换为包含许多确定状态的有限自动机。
    您可以使用此参数来防止该转换无意中消耗太多资源。您可能需要增加此限制以运行复杂的正则表达式。
11. `fuzziness` ：选填。模糊查询，指定可以最多有多少个字符与搜索的内容不同。
12. `max_expansions` ：选填。默认50个，模糊匹配最大可扩展到多少个term词
13. `prefix_length` ：选填。默认0，表示模糊匹配从哪一位开始模糊
14. `transpositions` ：选填，默认值true,指定是否可以交换字符顺序
15. `fuzzy_rewrite` ：选填，模糊重写方法。如果fuzziness参数不为0，则fuzzy_rewrite默认使用top_terms_blended_freqs_${max_expansion}方法
16. `lenient` ：选填，默认false，如果是true，会校验query中设置的查询内容的格式，
17. `operator` ：选填，只有两个选择OR和AND，默认OR。表示query中的文本被分成多个词后是使用OR去匹配还是AND匹配。
18. `minimum_should_match` ：选填，要返回文档必须匹配的最小子句数。如`I love china`经过分词后,相当于有`I` OR `love`OR `china`,三个子句。minimum_should_match配置多少就要返回匹配这些子句最小最小子句数的文档。
19. `zero_terms_query` ：选填，none 或者 all，默认none 。如果query中的文本经过分词器处理后，没有任何词（如全是停止词所以将词全部移除），none表示不返回任何文档，all返回所有文档。
