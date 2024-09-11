package 算法.热题100.技巧;

/**
 * https://leetcode.cn/problems/single-number/description/?envType=study-plan-v2&envId=top-100-liked
 * 136. 只出现一次的数字
 *
 * @Author binbin
 * @Date 2024 09 10 13 52
 **/
public class SingleNumber {
    /**
     * 异或  ^
     * 1. 任何数异或0都为任何数
     * 2. 任何数异或自己都为0
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        int a=0;
        for (int i = 0; i <= nums.length - 1; i++) {
            a =  a ^nums[i];
        }
        return a;
    }
}
