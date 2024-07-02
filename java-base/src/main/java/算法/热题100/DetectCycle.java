package 算法.热题100;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author binbin
 * @Date 2024 07 02 10 06
 **/
public class DetectCycle {
    /**
     * 快慢指针
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        ListNode tmp = null;

        while (slow != null && fast != null) {
            if (slow.next == null || fast.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
            //快慢指针找到圆环，这时定义一个tmp指针从头结点开始遍历
            if (tmp==null && slow == fast) {
                tmp=head;
            }
            //如果 tmp指针指到的节点和慢指针的节点相遇，这个节点就是交点
            if (tmp!=null){
                if (tmp==slow){
                    return slow;
                }
                tmp=tmp.next;
            }

        }
        return null;
    }

    /**
     * 哈希解法
     * @param head
     * @return
     */
    public ListNode detectCycle1(ListNode head) {
        if (head == null) {
            return null;
        }
        Set<ListNode> hashSet = new HashSet<>();
        while (head.next != null) {
            if (!hashSet.contains(head)) {
                hashSet.add(head);
            } else {
                return head;
            }
            head = head.next;
        }
        return null;
    }
}
