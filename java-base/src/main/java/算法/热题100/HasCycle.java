package 算法.热题100;

import java.util.List;

/**
 * 141. 环形链表
 * https://leetcode.cn/problems/linked-list-cycle/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 02 09 58
 **/
public class HasCycle {
    public boolean hasCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && slow != null) {
            fast = fast.next;
            if (fast == null) {
                return false;
            }
            if (slow.next == null) {
                return false;
            }
            slow = slow.next.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;

    }
}
