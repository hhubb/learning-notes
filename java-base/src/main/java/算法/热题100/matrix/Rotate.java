package 算法.热题100.matrix;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/rotate-image/description/?envType=study-plan-v2&envId=top-100-liked
 * 48. 旋转图像
 *
 * @Author binbin
 * @Date 2024 09 26 10 39
 **/
public class Rotate {
    /**
     * 暴力
     * 找到规律：
     * x=y1
     * y=(col-1)-x1
     * @param matrix
     */
    public void rotate(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        int[][] res = new int[matrix.length][matrix[0].length];
        for (int i = 0; i <= row - 1; i++) {
            for (int j = 0; j <= col - 1; j++) {
                // x=j; y=(col-1)-i
                res[j][col - 1 - i] = matrix[i][j];
            }
        }
        for (int i = 0; i <= row - 1; i++) {
            System.arraycopy(res[i], 0, matrix[i], 0, col - 1 + 1);
        }


    }
}
