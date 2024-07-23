package 算法.热题100;

import java.util.Stack;

/**
 * https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
 * 114. 二叉树展开为链表
 *
 * @Author binbin
 * @Date 2024 07 22 11 17
 **/
public class Flatten {

    /**
     * 先序：根左右
     * 将左子树插入到右子树的地方
     * 将原来的右子树接到左子树的最右边节点
     * 考虑新的右子树的根节点，一直重复上边的过程，直到新的右子树为 null
     *
     * @param root
     */
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode tmp=root;
        while (tmp!=null){
            TreeNode left=tmp.left;
            if (left!=null){
                //找到左节点最右边的节点
                while (left.right!=null){
                    left=left.right;
                }
                //将原来的右子树接到左子树的最右边节点
                left.right=tmp.right;
                tmp.right=tmp.left;
                tmp.left=null;
            }

            tmp=tmp.right;
        }

    }

    
}
