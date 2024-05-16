Lock是J.U.C中提供解决线程同步问题的实现方式

Lock是一个接口

```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```

![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

lock()方法是主要的使用方法，与之配合的是unLock()

eg

```java
public class LockDemo {
    static Lock lock=new ReentrantLock();

    private static int count=0;
    public static void inc() throws InterruptedException {
        lock.lock();//抢占锁，如果没有抢占到，会阻塞
        Thread.sleep(1);
        count++;
        lock.unlock();
    }
    public static void main(String[] args) throws InterruptedException {
        for (int i=0;i<1000;i++){
            Thread thread=new Thread(()->{
                try {
                    LockDemo.inc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
        Thread.sleep(6000);
        System.out.println(count);
    }
}
```

![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

使用lock()方法后，如果线程抢占到锁则执行，没有抢占到则阻塞。于synchronized关键字一个很大不不同是，lock可以主动释放锁，释放锁的方法就是unLock()。所以私用Lock一定要释放锁，不然就会造成死锁。通常推荐使用try（）catch（）finally的形式，在finally代码块里释放锁。

```java
try{
    //处理任务
}catch(Exception ex){
     
}finally{
    lock.unlock();   //释放锁
}
```

![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

tryLock()和tryLock(long time, TimeUnit unit)都是有返回值的

tryLock()表示当前线程是否抢占到锁，抢占到则返回true，没有则false。

tryLock(long time, TimeUnit unit)表示会等一段时间，如果在这段时间内抢占到则返回true，没有则false

tryLock尝试抢占锁时不会造成阻塞，没有抢占到立刻返回。

lockInterruptibly()表示通过这个方法获取锁时，如果线程正在等待获取锁，则这个县城能够响应中断，也就是中断这个线程的等待状态。当两个线程同时调用lock.lockInterruptibly()获取锁，假如此时A获取到了锁，而线程B只有等待，那么对线程B调用threadB.imterrupt()方法时就可以中断线程B的等待过程。同时需要抛出InterruptedException 。

```java
public void method() throws InterruptedException {
    lock.lockInterruptibly();
    try {  
     //.....
    }
    finally {
        lock.unlock();
    }  
}
```

![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

**interrupt()方法是无法中断执行中线程的，所以这里的中断只能中断处于阻塞中的等待的线程。**

synchronized关键字没办法中断一个处于等待的线程。

从以上也可以看出来synchronized与Lock的区别

1. synchronized是Java提供的关键字，Lock是JUC中提供的接口
2. synchronized可以作用于静态方法、实例方法、代码块。Lock是接口通过实现类来实现一个锁对象同加锁解锁来保证一段代码的线程安全问题
3. synchronized阻塞的处于等待中的线程不能被中断，只能一直等待下去。Lock阻塞的线程可以被中断取消等待。

但他们都可以解决线程安全。