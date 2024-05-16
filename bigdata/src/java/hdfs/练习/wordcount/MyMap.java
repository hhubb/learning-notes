package hdfs.练习.wordcount;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @Author binbin
 * @Date 2024 05 15 15 50
 **/

/**
 * 输入
 * Mapper<Object, Text, Text, IntWritable>
 */
public class MyMap extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    /**
     * Text： 一种可变长度的字节数组，用于表示文本数据。相当于Java中的String。
     * LongWritable、IntWritable、FloatWritable、DoubleWritable： 分别用于表示长整型、整型、浮点型和双精度浮点型数据。相当于Java中的long、int、float和double。
     * BooleanWritable： 用于表示布尔类型数据。相当于Java中的boolean。
     * NullWritable： 用于表示空值，通常用于表示Map任务的输出中间结果数据中的值为空。相当于Java中的null。
     * ArrayWritable： 用于表示数组类型数据。相当于Java中的数组。
     * MapWritable： 一种可序列化的Map数据结构，可以作为Map任务的输出（中间结果数据）或Reduce任务的输入。相当于Java中的Map<>。
     * WritableComparable： 一种可序列化的、可比较的数据类型接口，可以作为Map任务或Reduce任务的输入输出数据类型。
     */

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        //将文本按照空格来切分字符
        StringTokenizer stringTokenizer=new StringTokenizer(value.toString());
        //统计字符出现次数，字符为key，次数为1，输出到MR框架中
        while (stringTokenizer.hasMoreTokens()){
            context.write(new Text(stringTokenizer.nextToken()),one);
        }
    }
}
