package 算法.array;

/**
 * https://leetcode.cn/problems/range-sum-query-immutable/description/
 * 303. 区域和检索 - 数组不可变
 *
 * @Author binbin
 * @Date 2025 02 11 11 03
 **/
public class NumArray {
    private int[] sum;

    public NumArray(int[] nums) {
        sum = new int[nums.length + 1];
        for (int i = 0; i <= nums.length - 1; i++) {
            sum[i + 1] += sum[i] + nums[i];
        }
    }

    public int sumRange(int left, int right) {
        return sum[right+1] - sum[left ];
    }
}
