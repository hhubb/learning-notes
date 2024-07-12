package 算法.热题100;

import java.util.*;

/**
 * https://leetcode.cn/problems/binary-tree-inorder-traversal/description/?envType=study-plan-v2&envId=top-100-liked
 * 94. 二叉树的中序遍历
 *
 * @Author binbin
 * @Date 2024 07 12 09 51
 **/
public class InorderTraversal {


    /**
     * 标记法——————前中后都可以用这个方法，只需要调整入栈顺序
     * 0表示未访问，1表示已访问过 用map存放节点与访问关系
     * 先把root放入栈中，且标记为0未访问
     * 遍历栈
     * 如果节点不为空，
     *             如果节点未被访问过：则标记它的左和右未未访问，将他自己标记为已访问，并按照右中左顺序入栈（因为栈是后进先出，所以和中序访问相反）、
     *             如果节点被访问过： 则记录指
     * 如果节点为空则继续访问
     *
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        Map<TreeNode,Integer> map = new HashMap<>();
        stack.push(root);
        map.put(root,0);
        while (!stack.isEmpty()){
            TreeNode node=  stack.pop();
            if (node!=null){
                if (map.get(node).equals(0)){
                    map.put(node.left,0);
                    map.put(node,1);
                    map.put(node.right,0);
                    stack.push(node.right);
                    stack.push(node);
                    stack.push(node.left);
                }else {
                    res.add(node.val);
                }

            }

        }
        return res;
    }

    /**
     * 迭代法
     * 中序顺序：左中右
     * 先遍历左子树，使用栈保存根节点，
     * 当左子节点为空的时候，表示左子树遍历完，将最近一个根节点出栈，记录值，然后继续遍历右子树
     * 直到所有节点以及栈中的节点都为空，退出循环
     *
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal1(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode treeNode = stack.pop();
                res.add(treeNode.val);
                curr = treeNode.right;
            }
        }
        return res;
    }

}
