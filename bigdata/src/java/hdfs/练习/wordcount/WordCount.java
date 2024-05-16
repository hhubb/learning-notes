package hdfs.练习.wordcount;


import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
/**
 * Text的包千万要引对
 */
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * @Author binbin
 * @Date 2024 05 16 15 46
 **/
@Slf4j
public class WordCount {
    /*input.txt文件：
hadoop hive hbase spark flink
hadoop hive hbase spark flink
hadoop hive hbase spark flink
hadoop hive hbase spark flink
hadoop hive hbase spark flink
java scala python
java scala python
java scala python
java scala python
java scala python
     */

    /**
     * 操作HDFS
     * [root@lavm-h1qfimpzbs test_data]# hadoop fs -mkdir /wordCount/
     * [root@lavm-h1qfimpzbs test_data]# hadoop fs -mkdir /wordCount/input
     * [root@lavm-h1qfimpzbs test_data]# hadoop fs -put WordCountInput.txt /wordCount/input
     *
     * 删除文件/文件夹
     * hadoop fs -rm -r /wordCount/output
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();
        //再代码中指定NM和DN之间的通信为外网，此方式仅适用于api操作HDFS
        configuration.set("dfs.client.use.datanode.hostname", "true");
        String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("wordCount job  <in> [<in>....] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(configuration, "word Count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(MyMap.class);
        job.setCombinerClass(MyReduce.class);
        job.setReducerClass(MyReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //除最后一个外都是输入参数，本练习只有一个输入参数
        for (int i = 0; i < otherArgs.length - 1; i++) {
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        //最后一个是输出参数
        FileOutputFormat.setOutputPath(job,new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
