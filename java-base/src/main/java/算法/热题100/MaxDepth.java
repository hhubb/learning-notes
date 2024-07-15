package 算法.热题100;

/**https://leetcode.cn/problems/maximum-depth-of-binary-tree/description/?envType=study-plan-v2&envId=top-100-liked
 * 104. 二叉树的最大深度
 * @Author binbin
 * @Date 2024 07 15 09 14
 **/
public class MaxDepth {
    /**
     * 深度优先
     * 递归左右子树，对比取最大值然后+1
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if(root==null){
            return 0;
        }
        return Math.max(maxDepth(root.left),maxDepth(root.right))+1;
    }
}
