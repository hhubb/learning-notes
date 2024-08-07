package 算法.热题100.stack;

import com.fasterxml.jackson.databind.node.DoubleNode;
import 算法.热题100.ListNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @Author binbin
 * @Date 2024 08 05 10 00
 **/
public class MinStack {
    private DoubleLinkNode head;
    private DoubleLinkNode tail;
    private List<Integer> value;
    private Stack<DoubleLinkNode> minStack; //辅助栈

    public MinStack() {
        head = new DoubleLinkNode(0);
        value = new ArrayList<>();
        minStack=new Stack<>();
    }

    public void push(int val) {
        DoubleLinkNode node = new DoubleLinkNode(val);
        if (tail == null) {
            tail = node;
            head.next = node;
            node.pre = head;
        } else {
            tail.next = node;
            node.pre = tail;
            tail = node;

        }
        value.add(val);
        if (minStack.empty()) { //如果辅助站为空放入元素
            minStack.push(node);
        } else {
            DoubleLinkNode doubleLinkNode = minStack.peek();  //不为空与栈顶对比大小，比栈顶小则入栈
            if (doubleLinkNode.val>node.val){
                minStack.push(node);
            }
        }


    }

    public void pop() {
        value.remove(value.size() - 1);
        minStack.remove(tail); //移除未元素
        tail = tail.pre;

    }

    public int top() {
        return tail.val;
    }

    public int getMin() {
//        return Collections.min(value);
        return minStack.peek().val; //返回栈顶值
    }

    class DoubleLinkNode {
        int val;
        DoubleLinkNode next;
        DoubleLinkNode pre;

        DoubleLinkNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin());
        minStack.pop();
        System.out.println(minStack.top());
        System.out.println(minStack.getMin());

    }
}
