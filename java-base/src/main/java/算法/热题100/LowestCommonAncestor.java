package 算法.热题100;

import java.util.*;

/**
 * @Author binbin
 * @Date 2024 07 26 10 18
 **/
public class LowestCommonAncestor {
    /**
     * 存储父节点
     * 向上访问，直到出现交集节点
     * 模式识别：判断是否出现过或者设计出现次数，可以用散列表
     *
     * 1. 从根节点开始遍历整棵二叉树，用哈希表记录每个节点的父节点指针。
     * 2. 从 p 节点开始不断往它的祖先移动，并用数据结构记录已经访问过的祖先节点。
     * 3. 同样，我们再从 q 节点开始不断往它的祖先移动，如果有祖先已经被访问过，即意味着这是 p 和 q 的深度最深的公共祖先，即 LCA 节点。
     *

     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root==null){
            return null;
        }
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        //从根节点开始遍历整棵二叉树，用哈希表记录每个节点的父节点指针。
        findParent(parentMap, root);
        //从 p 节点开始不断往它的祖先移动，并用数据结构记录已经访问过的祖先节点。
        Set<TreeNode> setParentForP=new HashSet<>();
        while (p!=null){
            setParentForP.add(p);
            p=parentMap.get(p);
        }
        //同样，我们再从 q 节点开始不断往它的祖先移动，如果有祖先已经被访问过，即意味着这是 p 和 q 的深度最深的公共祖先，即 LCA 节点。
        while (q!=null){
            if (setParentForP.contains(q)){
                return q;
            }
            q=parentMap.get(q);
        }
        return null;


    }

    private void findParent(Map<TreeNode, TreeNode> parentMap, TreeNode root) {
        if (root.left != null) {
            parentMap.put(root.left, root);
            findParent(parentMap, root.left);
        }
        if (root.right != null) {
            parentMap.put(root.right, root);
            findParent(parentMap, root.right);
        }
    }

    /**
     * 递归
     */
    private TreeNode res = null;

    public TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {

        if (root == null) {
            return root;
        }
        if (root == p || root == q) {
            return root;
        }

        findNode(root, p, q);
        return res;
    }

    private boolean findNode(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null) {
            return false;
        }
        //判断当前节点是否是p或者q
        Boolean self = false;
        if (node == p || node == q) {
            self = true;
        }
        //判断当前节点的子节点是否有p或者q
        Boolean findOne = findNode(node.right, p, q);
        Boolean findTow = findNode(node.left, p, q);
        //如果当前节点是P或者q，且子节点有p或者q，那么当前节点就是结果
        if ((self && findOne) || (self && findTow) || (findOne && findTow)) {
            res = node;
        }
        //否则返回当前节点以及子节点是否有p或者q
        return findOne || findTow || self;
    }
}
