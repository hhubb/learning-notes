package 算法.热题100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 23. 合并 K 个升序链表
 * https://leetcode.cn/problems/merge-k-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 10 10 55
 **/
public class MergeKLists {
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        ListNode tmp = null;
        for (int i = 0; i <= lists.length - 1; i++) {
            tmp = merge(tmp, lists[i]);
        }

        return tmp;
    }

    public static ListNode merge(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode tmp = new ListNode(0, l1);
        ListNode prev = tmp;
        while (prev.next != null && l2 != null) {
            if (prev.next.val >= l2.val) {
                ListNode l2n = l2.next;
                l2.next = prev.next;
                prev.next = l2;
                l2 = l2n;
            }
            prev = prev.next;
        }
        if (l2 != null) {
            prev.next = l2;
        }
        return tmp.next;
    }

    public static void main(String[] args) {

        ListNode three = new ListNode(5, null);
        ListNode two = new ListNode(4, three);
        ListNode one = new ListNode(1, two);

        ListNode three1 = new ListNode(4, null);
        ListNode two1 = new ListNode(3, three1);
        ListNode one1 = new ListNode(1, two1);
        List<ListNode> list = new ArrayList<>();
        list.add(one);
        list.add(one1);
        ListNode[] A = new ListNode[2];
        list.toArray(A);
        mergeKLists(A);
    }
}
