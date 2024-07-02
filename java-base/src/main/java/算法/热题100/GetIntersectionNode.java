package 算法.热题100;

/**
 * @Author binbin
 * @Date 2024 07 01 10 25
 **/
public class GetIntersectionNode {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode tmpA = headA;
        ListNode tmpB = headB;
        while (tmpA != null && tmpB != null) {
            if (tmpA == tmpB ) {
                return tmpA;
            }
            tmpA = tmpA.next;
            tmpB = tmpB.next;
            if (tmpA == null && tmpB == null) {
                return null;
            }
            if (tmpA == null) {
                tmpA = headB;
            }
            if (tmpB == null) {
                tmpB = headA;
            }
        }
        return null;
    }
}
