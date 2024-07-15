package 算法.热题100;

/** https://leetcode.cn/problems/symmetric-tree/description/?envType=study-plan-v2&envId=top-100-liked
 * 101. 对称二叉树
 * @Author binbin
 * @Date 2024 07 15 09 43
 **/
public class IsSymmetric {
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return checkEq(root.right, root.left);
    }

    private boolean checkEq(TreeNode right, TreeNode left) {
        if (right==null && left==null){
            return true;
        }
        if (right == null && left != null) {
            return false;
        }
        if (left == null && right != null) {
            return false;
        }
        if (right.val!=left.val){
            return false;
        }
        return checkEq(left.left,right.right) && checkEq(left.right,right.left);
    }
}
