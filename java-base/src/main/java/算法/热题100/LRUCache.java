package 算法.热题100;

import java.security.Key;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

/** https://leetcode.cn/problems/lru-cache/description/?envType=study-plan-v2&envId=top-100-liked
 * 146. LRU 缓存
 * @Author binbin
 * @Date 2024 07 11 09 41
 **/
public class LRUCache {
    HashMap<Integer, Integer> cache;
    Queue<Integer> queue;
    Integer capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity, 1);
        this.queue = new ArrayDeque<Integer>(capacity);
    }

    public int get(int key) {
        if (cache.containsKey(key)) {
            queue.remove(key);
            queue.offer(key);
            return cache.get(key);
        }
        return -1;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)){
            cache.put(key, value);
            queue.remove(key);
            queue.offer(key);
        }else if (cache.size() < capacity){
            cache.put(key, value);
            queue.offer(key);
        }else {
            Integer first = queue.peek();
            cache.remove(first);
            queue.poll();
            cache.put(key, value);
            queue.offer(key);
        }
    }
}
