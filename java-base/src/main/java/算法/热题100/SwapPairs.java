package 算法.热题100;

/**
 * 24. 两两交换链表中的节点
 * https://leetcode.cn/problems/swap-nodes-in-pairs/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 04 13 34
 **/
public class SwapPairs {
    /**
     * 递归
     * @param head
     * @return
     */
    public ListNode swapPairs(ListNode head) {
        //如果链表只有一个节点或没有节点直接返回递归结束
        if (head == null || head.next == null) {
            return head;
        }
        //原本的节点的下一个节点成为新的头节点
        ListNode newHead=head.next;
        //原本的节点的与后续节点迭代后的新头节点连接
        head.next=swapPairs(newHead.next);
        //新的头节点的下一个为原本的头节点
        newHead.next=head;
        //返回新头节点
        return newHead;
    }

    /**
     * 迭代
     * @param head
     * @return
     */
    public ListNode swapPairs1(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tmp = new ListNode(0, head);
        //要使用prev节点不然交换的时候丢失前节点，无法进行连接
        ListNode prev=tmp;
        int n = 1;
        //奇数个节点，最后一个无需交换
        while (prev.next != null &&  prev.next.next!=null) {
            //交换后需要跳过被交换的数才能继续交换
            //1、2交换后，n==2指针到1此是1是prev节点，但2无需和3交换，所以跳过n++
            // n==3指针到2，2是prev节点，3需要和4交换,后续循环
            if (n % 2 == 1) {
                //pre->1->2 ->3 ->4
                //缓存住被交换的节点 2 这样2不会丢失
               ListNode next= prev.next.next;
               //让 1 先和3连接
                // pre->1 （2） ->3 ->4
               prev.next.next=next.next;
               //再让2和1连接
                // pre（2）->1  ->3 ->4
               next.next= prev.next;
               //再让pre和2连接
                //pre->2->1 ->3 ->4
               prev.next=next;
            }
            prev=prev.next;
            n++;
        }
        return tmp.next;
    }
}
