package 算法.热题100;

/** https://leetcode.cn/problems/binary-tree-maximum-path-sum/description/?envType=study-plan-v2&envId=top-100-liked
 * 124. 二叉树中的最大路径和
 * @Author binbin
 * @Date 2024 07 29 13 18
 **/
public class MaxPathSum {
    int res = -2000;

    /**
     * 递归
     * @param root
     * @return
     */
    public int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        res = root.val;
        path(root);

        return res;
    }

    public int path(TreeNode root) {
        if (root == null) {
            return -2000;
        }
        int max = root.val;
        int leftMax = path(root.left);
        int rightMax = path(root.right);
        max = Math.max(max, leftMax + root.val);
        max = Math.max(max, rightMax + root.val);
        max = Math.max(max, leftMax + root.val + rightMax);
        //找出最大值
        res = Math.max(max, res);
        //返回比较当前节点、当前节点+左最大、当前节点+右最大，返回，保障当前返回的路径值是经过当前节点最大的
        return Math.max(Math.max(rightMax + root.val, leftMax + root.val),root.val);
    }
}
