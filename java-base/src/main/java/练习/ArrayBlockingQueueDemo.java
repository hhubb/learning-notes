package 练习;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author binbin
 * @Date 2024 06 11 15 41
 **/
public class ArrayBlockingQueueDemo {
    private final static ArrayBlockingQueue<Apple> queue = new ArrayBlockingQueue<>(1);

    public static void main(String[] args) {
        new Thread(new Producer(queue)).start();
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

class Apple {
    public Apple() {

    }
}

class Producer implements Runnable {
    private static ArrayBlockingQueue<Apple> queue;

    public Producer(ArrayBlockingQueue<Apple> queue) {
        this.queue = queue;

    }

    @Override
    public void run() {
        try {
            while (true) {
                Apple apple = new Apple();
                System.out.println(Thread.currentThread().getName()+"生产了" + apple);
                queue.put(apple);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private static ArrayBlockingQueue<Apple> queue;

    public Consumer(ArrayBlockingQueue<Apple> queue) {
        this.queue = queue;

    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                Apple apple = queue.take();
                System.out.println(Thread.currentThread().getName()+"消费了" + apple);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}