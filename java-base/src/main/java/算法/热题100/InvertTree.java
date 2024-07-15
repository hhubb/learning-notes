package 算法.热题100;

/**
 * https://leetcode.cn/problems/invert-binary-tree/description/?envType=study-plan-v2&envId=top-100-liked
 * 226. 翻转二叉树
 *
 * @Author binbin
 * @Date 2024 07 15 09 36
 **/
public class InvertTree {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode left = root.left;
        TreeNode right = root.right;
        //交换左右
        root.right = left;
        root.left = right;
        //继续向下遍历左右子树
        invertTree(left);
        invertTree(right);
        return root;
    }
}
