package 算法.热题100.erfen;

/**
 * https://leetcode.cn/problems/search-a-2d-matrix/description/?envType=study-plan-v2&envId=top-100-liked
 * 74. 搜索二维矩阵
 *
 * @Author binbin
 * @Date 2024 07 30 10 28
 **/
public class SearchMatrix {
    /**
     * 二分
     *
     * @param matrix
     * @param target
     * @return
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        int i = 0;
        int x = matrix.length - 1;
        int j = 0;
        int y = matrix[0].length - 1;
        //先使用二分找到x也是i的位置
        while (i < x ) {
            int midx = (i + x) / 2;
            if (matrix[midx][0] > target) {
                x = midx - 1;
            } else if (matrix[midx][matrix[0].length - 1] < target) {
                i = midx + 1;
            } else if (matrix[midx][0] <= target && target <= matrix[midx][matrix[0].length - 1]) {
                x = midx;
                i = midx;
            }
        }
        //再使用二分找到具体元素
        while ( j < y) {
            int midy = (j + y) / 2;
            if (matrix[i][midy] < target) {
                j = midy + 1;
            } else if (matrix[i][midy] > target) {
                y = midy - 1;
            } else if (matrix[i][midy] <= target && target <= matrix[i][midy]) {
                j = midy;
                y = midy;
            }
        }

        return matrix[i][j] == target;
    }

    /**
     * 暴力
     *
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix1(int[][] matrix, int target) {
        for (int i = 0; i <= matrix[0].length - 1; i++) {
            for (int j = 0; j <= matrix.length - 1; j++) {
                if (matrix[j][i] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        searchMatrix(new int[][]{{1,3,5,7},  {10,11,16,20},{23,30,34,50}}, 5);
    }
}
