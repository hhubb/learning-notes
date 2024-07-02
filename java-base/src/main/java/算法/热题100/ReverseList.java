package 算法.热题100;

/**
 * @Author binbin
 * @Date 2024 07 01 13 28
 **/
public class ReverseList {
    public ListNode reverseList(ListNode list) {
        if (list == null) {
            return null;
        }
        ListNode tmp = new ListNode(0, list);
        while (list.next != null) {
            ListNode next = list.next;
            list.next = next.next;
            next.next = tmp.next;
            tmp.next = next;
        }
        return tmp.next;
    }
}
