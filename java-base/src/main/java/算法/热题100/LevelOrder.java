package 算法.热题100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**https://leetcode.cn/problems/binary-tree-level-order-traversal/description/?envType=study-plan-v2&envId=top-100-liked
 * 102. 二叉树的层序遍历
 * @Author binbin
 * @Date 2024 07 16 11 00
 **/
public class LevelOrder {
    List<List<Integer>> resList = new ArrayList<>();

    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root==null){
            return resList;
        }
        List<TreeNode> childNode = new ArrayList<>();
        childNode.add(root);
        getVal(childNode);
        return resList;
    }

    private void getVal(List<TreeNode> nodes) {
        if (nodes.isEmpty()) {
            return;
        }
        List<TreeNode> childNode = new ArrayList<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode node : nodes) {
            if (node==null){
                continue;
            }
            res.add(node.val);
            if (node.left != null) {
                childNode.add(node.left);
            }
            if (node.right != null) {
                childNode.add(node.right);
            }
        }
        resList.add(res);
        getVal(childNode);
    }
}
