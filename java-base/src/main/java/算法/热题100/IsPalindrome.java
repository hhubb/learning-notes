package 算法.热题100;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** 234. 回文链表
 * https://leetcode.cn/problems/palindrome-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
 * @Author binbin
 * @Date 2024 07 02 09 17
 **/
public class IsPalindrome {


    public boolean isPalindrome1(ListNode head) {
        ListNode tmp=new ListNode(0,head);
        List<Integer> list=new ArrayList<>();
        list.add(head.val);
        //反转链表
        while (head.next!=null){
            ListNode next=head.next;
            list.add(next.val);
            head.next=next.next;
            next.next=tmp.next;
            tmp.next=next;
        }
        int i=0;
        //对比反转后的值
        while (tmp.next!=null){
            ListNode next= tmp.next;
            if (next.val!=list.get(i)){
                return false;
            }
            tmp=tmp.next;
            i++;
        }
        return true;
    }


}
