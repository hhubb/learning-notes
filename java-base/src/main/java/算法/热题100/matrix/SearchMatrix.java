package 算法.热题100.matrix;

/**
 * https://leetcode.cn/problems/search-a-2d-matrix-ii/description/?envType=study-plan-v2&envId=top-100-liked
 * 240. 搜索二维矩阵 II
 *
 * @Author binbin
 * @Date 2024 10 09 10 43
 **/
public class SearchMatrix {
    /**
     * 对每一行进行二分查找
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i <= row - 1; i++) {
            if (erFen(matrix[i],0,col-1,target)){
                return true;
            }
        }
        return false;

    }

    private boolean erFen(int[] col, int low, int high, int target) {
        int left = low;
        int right = high;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (col[mid] == target) {
                return true;
            } else if (col[mid] > target) {
                right=mid-1;
            } else {
                left=mid+1;
            }
        }
        return false;
    }

    /**
     * 暴力
     *
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix1(int[][] matrix, int target) {
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                if (matrix[i][j] == target) {
                    return true;
                }
            }
        }
        return false;

    }
}
