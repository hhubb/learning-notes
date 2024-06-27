package 算法.热题100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 1. 两数之和
 * https://leetcode.cn/problems/two-sum/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 24 09 50
 **/
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        int[] res=new int[2];
        HashMap<Integer, Integer> hashMap = new HashMap<>(nums.length, 1);
        for (int i = 0; i <= nums.length - 1; i++) {
            Integer n = target - nums[i];
            if (hashMap.containsKey(n)) {
                res[0]=i;
                res[1]=hashMap.get(n);
                return res;
            }
            hashMap.put(nums[i],i);
        }
        return null;
    }
}
