package 练习;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author binbin
 * @Date 2024 06 18 11 16
 **/
public class ThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i=0;i<10;i++){
            executorService.execute(new Task());
        }
    }
}
class Task implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}