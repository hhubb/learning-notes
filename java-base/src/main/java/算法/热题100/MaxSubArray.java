package 算法.热题100;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author binbin
 * @Date 2024 06 28 10 20
 **/
public class MaxSubArray {

    /**
     * 动态规划升级版
     *
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
        int pre=nums[0];
        int max= nums[0];
        for (int i = 1; i <= nums.length - 1; i++) {
            //前一元素累计和大于0则与当前元素相加记录为最大和。否则最大和为当前元素
            if (pre>0){
                pre=pre+nums[i];
            }else {
                pre=nums[i];
            }
            max=Math.max(pre,max);
        }
        return max;

    }
    /**
     * 动态规划
     *
     * @param nums
     * @return
     */
    public int maxSubArray2(int[] nums) {
        int[] resArray = new int[nums.length ];
        resArray[0] = nums[0];
        int max= nums[0];
        for (int i = 1; i <= nums.length - 1; i++) {
            //前一元素累计和大于0则与当前元素相加记录为最大和。否则最大和为当前元素
            if (resArray[i-1]>0){
                resArray[i]=resArray[i-1]+nums[i];
            }else {
                resArray[i]=nums[i];
            }
            max=Math.max(resArray[i],max);
        }
        return max;

    }

    /**
     * 暴力 超时
     *
     * @param nums
     * @return
     */
    public int maxSubArray1(int[] nums) {
        int maxSum = -Integer.MAX_VALUE;
        for (int end = 0; end <= nums.length - 1; end++) {
            int sum = 0;
            for (int start = end; start >= 0; start--) {
                sum += nums[start];
                maxSum = Math.max(sum, maxSum);
            }
        }
        return maxSum;
    }
}
