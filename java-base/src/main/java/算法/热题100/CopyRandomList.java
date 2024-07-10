package 算法.热题100;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 138. 随机链表的复制
 * https://leetcode.cn/problems/copy-list-with-random-pointer/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 09 09 46
 **/
public class CopyRandomList {
    public static Node copyRandomList(Node head) {
        if (head == null) {
            return head;
        }
        Node tmp = new Node(0);
        tmp.next = head;
        Node newNodeTmp = new Node(0);
        Node prev = newNodeTmp;
        Map<Node, Integer> nodeIndex = new HashMap<>();
        Map<Integer, Node> newNodeMap = new HashMap<>();
        int i = 1;
        while (tmp.next != null) {
            Node node = tmp.next;
            Node newNode = new Node(node.val);
            nodeIndex.put(node, i);
            newNodeMap.put(i, newNode);
            prev.next = newNode;
            prev = prev.next;
            tmp = tmp.next;
            i++;
        }
        tmp = new Node(0);
        tmp.next = head;
        prev = newNodeTmp;
        while (tmp.next != null) {
            Node node = tmp.next;
            Node newNode = prev.next;
            if (node.random != null) {
                Integer index = nodeIndex.get(node.random);
                newNode.random = newNodeMap.get(index);
            }
            tmp = tmp.next;
            prev = prev.next;

        }
        return newNodeTmp.next;
    }

    public static void main(String[] args) {
//        Node five = new Node(1, null);
//        Node four = new Node(10, five);
//        Node three = new Node(11, four);
        Node two = new Node(0, null);
        Node one = new Node(-1, two);
        one.random=null;
        two.random=one;
//        three.random=five;
//        four.random=three;
//        five.random=one;
        copyRandomList(one);
    }

}
