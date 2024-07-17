package 算法.热题100;

import java.util.HashMap;
import java.util.Stack;

/**
 * https://leetcode.cn/problems/validate-binary-search-tree/description/?envType=study-plan-v2&envId=top-100-liked
 * 98. 验证二叉搜索树
 *
 * @Author binbin
 * @Date 2024 07 17 10 15
 **/
public class IsValidBST {

    /**
     * 递归法
     * 判断节点的左节点 小于 当前节点的数、判断节点的右节点 大于 当前节点的数。
     * 同时用上下界，判断后续的节点都不能超过，防止子树的右节点值大于当前节点值类似的情况发生
     *
     * @param root
     * @return
     */

    public boolean isValidBST(TreeNode root) {
        return isValid(root,null,null);
    }

    private boolean isValid(TreeNode root, Integer low, Integer up) {
        if (root==null){
            return true;
        }
        if (root.left!=null && root.val<=root.left.val){
            return false;
        }
        if (root.right!=null && root.val>=root.right.val){
            return false;
        }
        //如果low==null 表示是左边遍历
        if (up!=null && root.right!=null && root.right.val>=up){
            return false;
        }

        //如果up=null 表示是y右边遍历
        if (low!=null && root.left!=null && root.left.val<=low){
            return false;
        }
        boolean leftValid = isValid(root.left, low, root.val);
        boolean rightValid = isValid(root.right, root.val, up);
        return leftValid&& rightValid;
    }


    /**
     * * 有效 二叉搜索树定义如下：
     * *
     * * 节点的左
     * * 子树
     * * 只包含 小于 当前节点的数。
     * * 节点的右子树只包含 大于 当前节点的数。
     * * 所有左子树和右子树自身必须也是二叉搜索树。
     * 按照有效树的规则该树的中序是升序，所以可以先把树的中序排列出来然后验证是否是升序。
     *
     * @param root
     * @return
     */
    public boolean isValidBST1(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        HashMap<TreeNode, Integer> map = new HashMap<>();
        map.put(root, 0);
        stack.push(root);
        Integer val = null;
        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            if (curr != null) {
                if (map.get(curr) == 0) {
                    TreeNode left = curr.left;
                    TreeNode right = curr.right;
                    map.put(curr, 1);
                    map.put(left, 0);
                    map.put(right, 0);
                    //按照中序逆序放入  中序：左中右  逆序 右中左
                    stack.push(right);
                    stack.push(curr);
                    stack.push(left);
                } else {
                    if (val == null || val < curr.val) {
                        val = curr.val;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
