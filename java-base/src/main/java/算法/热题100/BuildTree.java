package 算法.热题100;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/description/?envType=study-plan-v2&envId=top-100-liked
 * 105. 从前序与中序遍历序列构造二叉树
 *
 * @Author binbin
 * @Date 2024 07 24 09 44
 **/
public class BuildTree {

    /**
     * 优化 递归方式
     * @param preorder
     * @param inorder
     * @return
     */
    private static Map<Integer,Integer> inorderMap=new HashMap<>();
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0|| inorder.length == 0) {
            return null;
        }
        for (int i=0;i<=inorder.length-1;i++){
            inorderMap.put(inorder[i],i);
        }
      return   buildTreeByMap(preorder,0,preorder.length-1,0,inorder.length-1);
    }

    public static TreeNode buildTreeByMap(int[] preorder, int preLeft,int preRight,  int inLeft, int inRight) {
        if (preLeft>preRight||inLeft>inRight){
            return null;
        }
        int rootValue= preorder[preLeft];
        TreeNode treeNode = new TreeNode(rootValue);
        int inRoot=inorderMap.get(rootValue);
        //获取左右子树节点个数
        int leftChildCount=inRoot-inLeft;
        int rightChildCount=inRight-inRoot;

        //递归得到每个子树，然后组装
        TreeNode leftTree = buildTreeByMap(preorder,preLeft+1,preLeft+leftChildCount,inLeft,inRoot-1);
        TreeNode rightTree = buildTreeByMap(preorder,preRight-rightChildCount+1,preRight,inRoot+1,inRight); //1,3,1,3
        treeNode.left = leftTree;
        treeNode.right = rightTree;
        return treeNode;
    }

    /**
     * 递归方式
     * 前序：根左右
     * 中序：左根右
     *
     * 只要我们在中序遍历中定位到根节点，那么我们就可以分别知道左子树和右子树中的节点数目。由于同一颗子树的前序遍历和中序遍历的长度显然是相同的，因此我们就可以对应到前序遍历的结果中，对上述形式中的所有左右括号进行定位。
     *
     * 这样以来，我们就知道了左子树的前序遍历和中序遍历结果，以及右子树的前序遍历和中序遍历结果，我们就可以递归地对构造出左子树和右子树，再将这两颗子树接到根节点的左右位置。
     * @param preorder
     * @param inorder
     * @return
     */
    public static TreeNode buildTree1(int[] preorder, int[] inorder) {
        if (preorder.length == 0|| inorder.length == 0) {
            return null;
        }
        //前序的第一个元素就是根节点
        int rootValue= preorder[0];
        TreeNode treeNode = new TreeNode(rootValue);
        if (preorder.length==1){
            return treeNode;
        }
        //找到中序获取根节点的位置，确定左右子树节点的个数
        int i = 0;
        while (inorder[i] != rootValue) {
            i++; //1
        }
        //获取左右子树节点个数
        int leftChildCount=i;
        int rightChildCount=inorder.length-1-i;

        //递归得到每个子树，然后组装
        TreeNode leftTree = buildTree(subArray(preorder,1,leftChildCount),subArray(inorder,0,leftChildCount));
        TreeNode rightTree = buildTree(subArray(preorder,leftChildCount+1,rightChildCount),subArray(inorder,i+1,rightChildCount));
        treeNode.left = leftTree;
        treeNode.right = rightTree;
        return treeNode;
    }

    /**
     * 左闭合右开
     * @param originalArray
     * @param begin
     * @param count
     * @return
     */
    private static int[] subArray(int[] originalArray, int begin, int count) {
        int[] newArray = new int[count];
        for (int i = 0; i < count; i++) {
            newArray[i]=originalArray[begin+i];
        }
        return newArray;
    }

    public static void main(String[] args) {
        buildTree(new int[]{1,2,3},new int[]{2,3,1});
    }
}
