package 算法.热题100;

import lombok.Data;

import java.util.List;

/**
 * @Author binbin
 * @Date 2024 07 01 10 25
 **/
public class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
    ListNode(int val,ListNode next){
        this.val = val;
        this.next = next;
    }
    ListNode() {
    }


}
