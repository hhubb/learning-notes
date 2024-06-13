package 练习;

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
                    Integer num = count.get();
                    num += 5;
                    count.set(num);
                    System.out.println(Thread.currentThread().getName() + "  count："+count.get());
                }
            };
            thread.start();

        }
    }
}
