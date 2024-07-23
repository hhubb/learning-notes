package 算法.热题100;

import java.util.*;

/**
 * https://leetcode.cn/problems/binary-tree-right-side-view/solutions/213494/er-cha-shu-de-you-shi-tu-by-leetcode-solution/?envType=study-plan-v2&envId=top-100-liked
 * 199. 二叉树的右视图
 *
 * @Author binbin
 * @Date 2024 07 19 10 52
 **/
public class RightSideView {
    List<Integer> res2 = new ArrayList<>();
    /**
     * 深度优先
     * 按照：根->右->左顺序遍历的话就可以保证每层都是最先访问右节点
     *
     * @param root
     * @return
     */
    public List<Integer> rightSideView(TreeNode root) {

        if (root == null) {
            return res2;
        }
        dfs(root,1);
        return res2;
    }

    private void dfs(TreeNode root,int depth){
        if (root==null){
            return;
        }
        //如果当前深度没有出现在res2结果里，那么表示这时第一次出现的，将它加入res2
        if (depth==res2.size()){
            res2.add(root.val);
        }
        depth++;
        //先访问右再访问左
        dfs(root.right,depth);
        dfs(root.left,depth);

    }

    /**
     * 广度优先
     * 记录下每层最后一个元素
     *
     * @param root
     * @return
     */
    public List<Integer> rightSideView2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            //获取当前层的元素个数，防止将下一层的元素遍历出来
            int size = queue.size();
            for (int i = 0; i <= size - 1; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                if (i == size - 1) {
                    res.add(node.val);
                }
            }
        }
        return res;
    }

    /**
     * ————————————————————————————————————————————————自己做的
     */
    /**
     * 广度优先
     * 把每一层从右到左都放到rNodes
     * Nodes中的第一个就是看到的节点，放入res
     * 直到Nodes为空
     *
     * @param nodes
     */
    List<Integer> res = new ArrayList<>();
    TreeNode tmp = null;

    public List<Integer> rightSideView1(TreeNode root) {
        if (root == null) {
            return res;
        }
        searchRightSideView(Arrays.asList(root));
        return res;
    }

    /**
     * 把每一层从右到左都放到rNodes
     * Nodes中的第一个就是看到的节点，放入res
     * 直到Nodes为空
     *
     * @param nodes
     */
    private void searchRightSideView(List<TreeNode> nodes) {
        if (nodes.isEmpty()) {
            return;
        }
        res.add(nodes.get(0).val);
        List<TreeNode> rNodes = new ArrayList<>();
        for (TreeNode node : nodes) {
            if (node.right != null) {
                rNodes.add(node.right);
            }
            if (node.left != null) {
                rNodes.add(node.left);
            }
        }
        searchRightSideView(rNodes);
    }
}
