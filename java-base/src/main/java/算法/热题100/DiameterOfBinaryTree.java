package 算法.热题100;

/**
 * https://leetcode.cn/problems/diameter-of-binary-tree/description/?envType=study-plan-v2&envId=top-100-liked
 * 543. 二叉树的直径
 *
 * @Author binbin
 * @Date 2024 07 16 10 12
 **/
public class DiameterOfBinaryTree {
    int diameter = -1;

    /**
     * 两叶子节点之间的路径=两节点同根的左右儿子（需要计算的两个节点）的深度之和
     * +++++++++++++++++++++++ 优化写法+++++++++++++++++++
     */
    public int diameterOfBinaryTree(TreeNode root) {
        depth1(root);
        return diameter;
    }
    public int depth1(TreeNode root){
        if (root==null){
            return 0;
        }
        int leftDepth=depth1(root.left);
        int rightDepth=depth1(root.right);
        diameter=Math.max(leftDepth+rightDepth,diameter);
        return Math.max(leftDepth,rightDepth)+1;
    }







    /**
     *
     * +++++++++++++++++++++++ 原始写法+++++++++++++++++++
     */




    int maxDepth = 0;

    /**
     * 两叶子节点之间的路径=两节点同根的左右儿子（需要计算的两个节点）的深度之和
     *
     * @param root
     * @return
     */
    public int diameterOfBinaryTree1(TreeNode root) {
        child(root);
        return maxDepth;
    }

    public void child(TreeNode root) {
        if (root == null) {
            return;
        }
        int left = depth(root.left);
        int right = depth(root.right);
        maxDepth = Math.max(maxDepth, left + right);
        child(root.left);
        child(root.right);
    }

    private int depth(TreeNode root) {
        return root == null ? 0 : Math.max(depth(root.right), depth(root.left)) + 1;
    }

}
