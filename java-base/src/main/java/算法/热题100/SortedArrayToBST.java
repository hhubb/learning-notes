package 算法.热题100;

/**https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/description/?envType=study-plan-v2&envId=top-100-liked
 * 108. 将有序数组转换为二叉搜索树
 * @Author binbin
 * @Date 2024 07 17 09 34
 **/
public class SortedArrayToBST {
    /**
     * 二叉搜索树的中序遍历是升序序列，题目给定的数组是按照升序排序的有序数组，因此可以确保数组是二叉搜索树的中序遍历序列。
     * 中序：左根右
     * 可以发现如果是平衡二叉数的话，数组的中间数字是根节点
     * 所以可以递归，找到中间节点作为根，根左边的数组都在根节点的左边，右边数组都在根的右边，直到l>r
     *
     * @param nums
     * @return
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        return treeNode(nums,l,r);
    }

    private TreeNode treeNode(int[] nums, int l, int r) {
        if (l > r) {
            return null;
        }
        int mid = (l + r ) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        TreeNode leftChild = treeNode(nums, l, mid - 1);
        TreeNode rightChild = treeNode(nums, mid + 1, r);
        root.left = leftChild;
        root.right = rightChild;
        return root;
    }
}
