package 算法.array;

/**
 * https://leetcode.cn/problems/max-consecutive-ones/description/
 * 485. 最大连续 1 的个数
 *
 * @Author binbin
 * @Date 2025 02 26 18 10
 **/
public class FindMaxConsecutiveOnes {
    public int findMaxConsecutiveOnes(int[] nums) {
        int max = 0;
        int tmp = 0;
        for (int i = 0; i <= nums.length - 1; i++) {
            if (nums[i] == 0) {
                max = Math.max(tmp, max);
                tmp = 0;
                continue;
            }
            tmp += 1;
        }
        max = Math.max(tmp, max);
        return max;
    }
}
