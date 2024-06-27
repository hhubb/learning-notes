package 练习;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author binbin
 * @Date 2024 06 17 10 16
 **/
public class ForkJoinDemo extends RecursiveTask<List<String>> {
    private int start; // 0
    private int end; // 10
    private List<String> ppt; // 10

    private Long size = 3l;

    public ForkJoinDemo(int start, int end, List<String> ppt) {
        this.start = start;
        this.end = end;
        this.ppt = ppt;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<String> ppt = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        long start = System.currentTimeMillis();
        //或者直接 ForkJoinPool pool=new ForkJoinPool();
        ForkJoinPool pool = (ForkJoinPool)Executors.newWorkStealingPool();
        ForkJoinTask<List<String>> task = new ForkJoinDemo(0, ppt.size(), ppt);
        ForkJoinTask<List<String>> submit = pool.submit(task);// 提交任务
        List<String> result = submit.get();
        long end = System.currentTimeMillis();
        System.out.println(" 时间：" + (end - start));
        System.out.println(JSONObject.toJSONString(result));
    }

    @Override
    protected List<String> compute() {
        List<String> resultList = new ArrayList<>();
        if ((end - start) < size) {
            String result = "获取讲义第%s页";
            for (int i = start; i < end; i++) {
                resultList.add(Thread.currentThread().getName()+":"+String.format(result, ppt.get(i)));
            }
            return resultList;
        }
        // forkjoin 递归
        int middle = (start + end) / 2; // 中间值
        ForkJoinDemo task1 = new ForkJoinDemo(start, middle, ppt);
        task1.fork();
        ForkJoinDemo task2 = new ForkJoinDemo(middle, end, ppt);
        task2.fork();
        resultList.addAll(task1.join());
        resultList.addAll(task2.join());
        return resultList;
    }
}
