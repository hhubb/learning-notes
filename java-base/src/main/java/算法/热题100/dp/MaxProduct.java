package 算法.热题100.dp;

import java.util.Arrays;

/**https://leetcode.cn/problems/maximum-product-subarray/description/?envType=study-plan-v2&envId=top-100-liked
 * 152. 乘积最大子数组
 * todo 没弄懂
 * @Author binbin
 * @Date 2024 09 12 10 38
 **/
public class MaxProduct {
    public static int maxProduct(int[] nums) {
        long maxF = nums[0], minF = nums[0];
        int ans = nums[0];
        int length = nums.length;
        for (int i = 1; i < length; ++i) {
            long mx = maxF, mn = minF;
            maxF = Math.max(mx * nums[i], Math.max(nums[i], mn * nums[i]));
            minF = Math.min(mn * nums[i], Math.min(nums[i], mx * nums[i]));
            if(minF<-1<<31){
                minF=nums[i];
            }
            ans = Math.max((int)maxF, ans);
        }
        return ans;

    }

    public static void main(String[] args) {
        maxProduct(new int[]{3, -1, 4});
    }
}
