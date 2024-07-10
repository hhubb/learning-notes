package 算法.热题100;

/**
 * 19. 删除链表的倒数第 N 个结点
 * https://leetcode.cn/problems/remove-nth-node-from-end-of-list/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 04 10 46
 **/
public class RemoveNthFromEnd {
    /**
     * 双指针
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode tmp = new ListNode(0, head);
        ListNode first = tmp;
        ListNode second = tmp;
        //快指针比慢指针先走n步
        while (n>0){
            first = first.next;
            n--;
        }
        while ( first.next != null) {
            first=first.next;
            second=second.next;
        }
        //快指针到尾节点的适合second的下一个节点就是要移除的节点
        second.next=second.next.next;
        return tmp.next;
    }

    public static void main(String[] args) {
        ListNode two = new ListNode(2, null);
        ListNode one = new ListNode(1, null);
        removeNthFromEnd(one,2);
    }

    /**
     * 遍历
     *
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd1(ListNode head, int n) {
        //计算节点个数
        int count = 0;
        ListNode curr = head;
        while (curr != null) {
            count++;
            curr = curr.next;
        }
        int c = 0;
        ListNode tmp = new ListNode(0, head);
        ListNode next = tmp.next;
        //如果节点等于要删掉的倒数第n个节点，也就是删掉第一个节点，那直接返回第一个节点后的节点
        if (count == n) {
            return next.next;
        }
        while (next != null) {
            c++;
            //如果遇到要删掉的节点，直接移除,并返回
            if (count - n == c) {
                next.next = next.next.next;
                return tmp.next;
            }
            next = next.next;
        }
        return tmp.next;
    }
}
