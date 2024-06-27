package 练习;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author binbin
 * @Date 2024 06 04 09 43
 **/
public class LockTest {
    public static void main(String[] args) {
        ReentrantLock lock=new ReentrantLock();
        lock.lock();
        try {
            Condition condition= lock.newCondition();
            condition.await();
            condition.signal();
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
