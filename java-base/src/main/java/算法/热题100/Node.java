package 算法.热题100;

/**
 * @Author binbin
 * @Date 2024 07 09 09 47
 **/
public class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }

    public Node(int val, Node next) {
        this.val = val;
        this.next = next;
        this.random = null;
    }
}
