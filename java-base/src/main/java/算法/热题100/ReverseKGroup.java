package 算法.热题100;

import java.util.Stack;

/**
 * @Author binbin
 * @Date 2024 07 05 14 50
 **/
public class ReverseKGroup {
    public static ListNode reverseKGroup(ListNode head, int k) {
        //head为空且k为1得时候不需要做任何变换
        if (head == null|| k==1) {
            return head;
        }
        ListNode tmp = new ListNode(0, head);
        ListNode prev = tmp;
        Stack<ListNode> stack = new Stack<>();
        ListNode newHead = tmp;
        while (prev.next != null || k == 0) {
            if (k > 0) {
                stack.push(prev.next);
                k--;
                prev = prev.next;
            } else if (k == 0) {
                prev = prev.next;
                while (!stack.empty()) {
                    newHead.next = stack.pop();
                    newHead = newHead.next;
                    k++;
                }
                newHead.next = prev;
                prev=newHead;
            }

        }
        //stack 不为空，k>0 不转换
        if (k > 0 && !stack.empty()) {
            newHead.next = stack.firstElement();
        }
        return tmp.next;
    }

    public static void main(String[] args) {
        ListNode five = new ListNode(5, null);
        ListNode four = new ListNode(4, five);
        ListNode three = new ListNode(3, four);
        ListNode two = new ListNode(2, three);
        ListNode one = new ListNode(1, two);
        reverseKGroup(one,3);
    }
}
