package 算法.热题100;

import java.util.*;

/**
 * 560. 和为 K 的子数组
 * https://leetcode.cn/problems/subarray-sum-equals-k/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 27 10 52
 **/
public class SubarraySum {
    /**
     * 哈希前缀
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum(int[] nums, int k) {
        int res = 0;
        Map<Integer, Integer> sumMap = new HashMap<>();
        sumMap.put(0, 1);
        int sum = 0;
        for (int i = 0; i <= nums.length - 1; i++) {
            sum += nums[i];
            if (sumMap.containsKey(sum - k)) {
                res += sumMap.get(sum - k);
            }
            if (sumMap.containsKey(sum)) {
                sumMap.put(sum, sumMap.get(sum) + 1);
            } else {
                sumMap.put(sum, 1);
            }
        }
        return res;
    }

    /**
     * 暴力枚举
     *
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum1(int[] nums, int k) {
        int res = 0;
        for (int end = 0; end <= nums.length - 1; end++) {
            int sum = 0;
            for (int start = end; start >= 0; start--) {
                sum += nums[start];
                if (sum == k) {
                    res++;
                }
            }

        }
        return res;
    }
}
