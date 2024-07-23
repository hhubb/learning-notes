package 算法.热题100;

import java.util.*;

/**
 * https://leetcode.cn/problems/kth-smallest-element-in-a-bst/description/?envType=study-plan-v2&envId=top-100-liked
 * 230. 二叉搜索树中第K小的元素
 *
 * @Author binbin
 * @Date 2024 07 19 09 53
 **/
public class KthSmallest {

    /**
     * 递归法中序
     *
     * @param root
     * @param k
     * @return
     */
    public int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        int index = 1;
        TreeNode node = root;
        while (node != null || !stack.isEmpty()) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else {
                TreeNode curr = stack.pop();
                if (k == index) {
                    return curr.val;
                }
                index++;
                node = curr.right;
            }

        }
        return 0;
    }

    /**
     * 标记法中序找到最小
     * 二叉搜索树具有如下性质：
     * <p>
     * 结点的左子树只包含小于当前结点的数。
     * <p>
     * 结点的右子树只包含大于当前结点的数。
     * <p>
     * 所有左子树和右子树自身必须也是二叉搜索树。
     * <p>
     * 利用二叉树的中序遍历是升序的原理
     *
     * @param root
     * @param k
     * @return
     */
    public int kthSmallest1(TreeNode root, int k) {
        Map<TreeNode, Integer> map = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        map.put(root, 0);
        stack.push(root);
        int index = 1;
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            if (node != null) {
                if (map.get(node) == 0) {
                    TreeNode r = node.right;
                    TreeNode l = node.left;
                    stack.push(r);
                    stack.push(node);
                    stack.push(l);
                    map.put(node, 1);
                    map.put(r, 0);
                    map.put(l, 0);
                } else {
                    if (k == index) {
                        return node.val;
                    }
                    k--;
                }
            }
        }
        return 0;
    }
}
