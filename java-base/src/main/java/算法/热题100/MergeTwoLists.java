package 算法.热题100;

/**
 * 21. 合并两个有序链表
 * https://leetcode.cn/problems/merge-two-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 07 03 09 51
 **/
public class MergeTwoLists {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode tmp = new ListNode(0,list1);
        /**
         * 这里要用prev而不能用next，因为这样不会遍历到最后丢失整条链。
         */
        ListNode prev=tmp;
        while (prev.next!=null && list2!=null) {
            //如果list2节点小，则插入
            if (list2.val<=prev.next.val){
                //暂存list2后续的节点，防止
               ListNode ln=  list2.next;
               //先让list2与插入位置后续节点关联
                list2.next=prev.next;
                //再让list2让前面连接
               prev.next=list2;
               //list2下移
               list2=ln;
            }
            //前置节点也下移
            prev=prev.next;
        }
        //如果list2节点还有剩余，直接拼接在尾部，这也就是为什么用的是prev
        if (list2!=null){
            prev.next=list2;
        }
        return tmp.next;
    }
}
