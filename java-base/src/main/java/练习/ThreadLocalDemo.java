package 练习;

import java.text.SimpleDateFormat;

/**
 * @Author binbin
 * @Date 2024 06 13 14 36
 **/
public class ThreadLocalDemo {
    private static ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    //从当前线程范围内取出设置的值
                    Integer num = count.get();
                    num += 5;
                    //set在当前线程范围内设置一个值，只对当前线程可见
                    //相当于建立一个副本
                    count.set(num);
                    System.out.println(Thread.currentThread().getName() + "  count：" + count.get());
                    //移除当前线程中存储得值
                    count.remove();
                    //Java8出现的快速初始化得方法
                    //ThreadLocal.withInitial();
                }
            };
            thread.start();

        }
    }
}
