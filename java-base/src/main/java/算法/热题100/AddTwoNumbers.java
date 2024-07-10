package 算法.热题100;

/** 2. 两数相加
 * https://leetcode.cn/problems/add-two-numbers/description/?envType=study-plan-v2&envId=top-100-liked
 * @Author binbin
 * @Date 2024 07 03 14 27
 **/
public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        int count = 0;
        ListNode tmp = new ListNode();
        ListNode curr = tmp;
        while (l1 != null && l2 != null) {
            int sum = l1.val + l2.val+count;
            int val = sum % 10 ;
            count = sum / 10;
            curr.next = new ListNode(val, null);
            curr=curr.next;
            l1=l1.next;
            l2=l2.next;
        }
        if (l1!=null){
            while (l1!=null){
                int sum = l1.val +count;
                int val = sum % 10 ;
                count = sum / 10;
                curr.next = new ListNode(val, null);
                curr=curr.next;
                l1=l1.next;
            }
        }
        if (l2!=null){
            while (l2!=null){
                int sum = l2.val +count;
                int val = sum % 10 ;
                count = sum / 10;
                curr.next = new ListNode(val, null);
                curr=curr.next;
                l2=l2.next;
            }
        }
        if (count!=0){
            curr.next = new ListNode(count, null);
        }
        return tmp.next;
    }
}
