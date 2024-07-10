package 算法.热题100;

import java.util.*;

/**
 * 148. 排序链表
 * https://leetcode.cn/problems/sort-list/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 09 10 48
 **/
public class SortList {
    /**
     * 递归
     *
     * @param head
     * @return
     */
    public static ListNode sortList(ListNode head) {

        return sortList(head, null);
    }

    /**
     * 不断拆分，最终拆分成单个节点，然后按顺序合并。
     *
     * @param head
     * @param end
     * @return
     */
    private static ListNode sortList(ListNode head, ListNode end) {
        if (head == null) {
            return head;
        }
        if (head.next == end) {
            head.next = null;
            return head;
        }
        ListNode tmp = new ListNode(0, head);
        ListNode fast = tmp;
        ListNode slow = tmp;
        /**
         * 找到中间节点，快慢指针，快指针走两步，慢指针走一步，当快指针走到终点，慢指针就处于中间节点
         */
        while (fast != end) {
            fast = fast.next;
            slow = slow.next;
            if (fast != end) {
                fast = fast.next;
            }
        }
        ListNode mid = slow;
        ListNode left = sortList(head, mid);
        ListNode right = sortList(mid, end);
        /**
         * 合并链表
         */
        return merge(left, right);
    }

    /**
     * 合并两个有序链表
     *
     * @param left
     * @param right
     * @return
     */
    private static ListNode merge(ListNode left, ListNode right) {
        ListNode tmp = new ListNode(0, left);
        ListNode prev = tmp;
        while (prev.next != null && right != null) {
            ListNode rn = right.next;
            if (prev.next.val >= right.val) {
                right.next = prev.next;
                prev.next = right;
                right = rn;

            }
            prev = prev.next;
        }
        if (right != null) {
            prev.next = right;
        }
        return tmp.next;
    }

    public static void main(String[] args) {
//        ListNode five = new ListNode(5, null);
        ListNode four = new ListNode(3, null);
        ListNode three = new ListNode(1, four);
        ListNode two = new ListNode(2, three);
        ListNode one = new ListNode(4, two);
        sortList(one);
    }

    public static ListNode sortList1(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode tmp = new ListNode(0, head);
        ListNode prev=tmp;
        while (prev.next != null) {
            ListNode curr = prev.next;
            ListNode next = prev.next.next;
            curr.next = null;
            prev.next = merge(curr, next);
            if (prev.next == curr) {
                prev = prev.next;
            }
        }
        return tmp.next;
    }
}
