package 算法.热题100;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**https://leetcode.cn/problems/path-sum-iii/description/?envType=study-plan-v2&envId=top-100-liked
 * 437. 路径总和 III
 * todo 前缀和解法没看懂
 * @Author binbin
 * @Date 2024 07 25 10 07
 **/
public class PathSum {
    Map<TreeNode, List<Integer>> map=new HashMap<>();



    /**
     * 深度优先，递归
     * 先算出一个节点满足的结果，再计算出她的左右子树可能满足的结果
     * @param root
     * @param targetSum
     * @return
     */
    public int pathSum1(TreeNode root, int targetSum) {
        if (root==null){
            return 0;
        }
        int ret= rootSum1(root,targetSum);
        ret+=pathSum1(root.left,targetSum);
        ret+=pathSum1(root.right,targetSum);
        return ret;
    }

    /**
     * 如果当前节点等于目标值，结果累计1
     * 递归判断子节点满足目标值-当前节点值。结果继续累计
     * @param root
     * @param targetSum
     * @return
     */
    public int rootSum1(TreeNode root, long targetSum) {
        int ret = 0;
        if (root==null){
            return ret;
        }
        int val=root.val;
        if (val==targetSum){
            ret++;
        }
        ret+=rootSum1(root.left,targetSum-val);
        ret+=rootSum1(root.right,targetSum-val);
        return ret;
    }
}
