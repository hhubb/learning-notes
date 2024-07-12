package 算法.热题100;

import java.util.HashMap;

/** https://leetcode.cn/problems/lru-cache/description/?envType=study-plan-v2&envId=top-100-liked
 * 146. LRU 缓存
 * @Author binbin
 * @Date 2024 07 11 10 42
 **/
public class LRUCache2 {
    HashMap<Integer, DoubleLinkNode> cache;
    Integer capacity;
    DoubleLinkNode head;
    DoubleLinkNode tail;

    public class DoubleLinkNode {
        private int key;
        private int val;
        private DoubleLinkNode next;
        private DoubleLinkNode prev;

        public DoubleLinkNode() {
        }

        public DoubleLinkNode(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    public LRUCache2(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity, 1);
        this.head = new DoubleLinkNode();
        this.tail = new DoubleLinkNode();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (cache.containsKey(key)){
            DoubleLinkNode doubleLinkNode= cache.get(key);
            //从当前位置移到队尾
            doubleLinkNode.prev.next=doubleLinkNode.next;
            doubleLinkNode.next.prev=doubleLinkNode.prev;
            //插入队尾
            tail.prev.next = doubleLinkNode;
            doubleLinkNode.prev = tail.prev;
            doubleLinkNode.next = tail;
            tail.prev = doubleLinkNode;
            return doubleLinkNode.val;
        }
        return -1;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            DoubleLinkNode doubleLinkNode= cache.get(key);
            //从当前位置移到队尾
            doubleLinkNode.prev.next=doubleLinkNode.next;
            doubleLinkNode.next.prev=doubleLinkNode.prev;
            //插入队尾
            tail.prev.next = doubleLinkNode;
            doubleLinkNode.prev = tail.prev;
            doubleLinkNode.next = tail;
            tail.prev = doubleLinkNode;
            doubleLinkNode.val = value;
        } else if (cache.size() < capacity) {
            //插入队尾
            DoubleLinkNode doubleLinkNode = new DoubleLinkNode(key, value);
            tail.prev.next = doubleLinkNode;
            doubleLinkNode.prev = tail.prev;
            doubleLinkNode.next = tail;
            tail.prev = doubleLinkNode;
            cache.put(key, doubleLinkNode);
        } else { //容量不够
            //移除队头最近最少使用的元素
            DoubleLinkNode deleteNode = head.next;
            head.next = head.next.next;
            head.next.prev = head;
            cache.remove(deleteNode.key);
            //插入队尾
            DoubleLinkNode doubleLinkNode = new DoubleLinkNode(key, value);
            tail.prev.next = doubleLinkNode;
            doubleLinkNode.prev = tail.prev;
            doubleLinkNode.next = tail;
            tail.prev = doubleLinkNode;
            cache.put(key, doubleLinkNode);
        }
    }
}
