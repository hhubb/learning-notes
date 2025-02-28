package 算法.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode.cn/problems/pascals-triangle-ii/description/
 * 119. 杨辉三角 II
 *
 * @Author binbin
 * @Date 2025 02 10 18 10
 **/
public class GetRow {
    public static List<Integer> getRow(int rowIndex) {
        if (rowIndex == 0) {
            return Arrays.asList(1);
        }
        int[][] nums = new int[rowIndex+1][rowIndex + 2];
        List<Integer> result = new ArrayList<>();
        nums[0][0] = 1;
        for (int i = 1; i <= rowIndex; i++) {
            result = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    nums[i][j] = 1;
                } else {
                    nums[i][j] = nums[i - 1][j-1] + nums[i - 1][j];
                }
                result.add(nums[i][j]);
            }
        }
        return result;

    }

    public static void main(String[] args) {
        getRow(3);
    }

}
