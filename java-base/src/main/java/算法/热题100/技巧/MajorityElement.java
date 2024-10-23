package 算法.热题100.技巧;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/majority-element/description/?envType=study-plan-v2&envId=top-100-liked
 * 169. 多数元素
 *
 * @Author binbin
 * @Date 2024 10 10 09 51
 **/
public class MajorityElement {
    /**
     * 暴力
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i <= nums.length - 1; i++) {
            Integer count = map.get(nums[i]);
            if (null == count) {
                count = 1;
            } else {
                count = count + 1;
            }
            if (count > (nums.length - 1) / 2) {
                return nums[i];
            }
            map.put(nums[i], count);
        }
        return 0;
    }

    /**
     * 排序后找中间值
     * @param nums
     * @return
     */
    public int majorityElement1(int[] nums) {
        Arrays.sort(nums);
        return nums[(nums.length - 1) / 2];
    }

}
