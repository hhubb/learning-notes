package 算法.热题100.heap;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @Author binbin
 * @Date 2024 08 13 10 41
 **/
public class MedianFinder {
    static PriorityQueue<Integer> maxQueue;
    static PriorityQueue<Integer> minQueue;

    public MedianFinder() {
        //从小到大
        minQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        //从大到小
        maxQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

    }

    public void addNum(int num) {
        if (minQueue.isEmpty() || num <= minQueue.peek()){
            minQueue.offer(num);
            if (minQueue.size()-maxQueue.size()>1){
                maxQueue.offer(minQueue.poll());
            }
        }else {
            maxQueue.offer(num);
            if (maxQueue.size()>minQueue.size()){
                minQueue.offer(maxQueue.poll());
            }
        }

    }

    public double findMedian() {
        if (maxQueue.isEmpty() && minQueue.isEmpty()) {
            return 0.0;
        }
        if (maxQueue.size()==minQueue.size()){
            return (maxQueue.peek()+minQueue.peek())/2.0;
        }
        return minQueue.peek();
    }

    public static void main(String[] args) {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(-1);
        medianFinder.addNum(-2);
        medianFinder.addNum(-3);
        medianFinder.addNum(-4);
        medianFinder.addNum(-5);
        System.out.println(medianFinder.findMedian());
    }
}
