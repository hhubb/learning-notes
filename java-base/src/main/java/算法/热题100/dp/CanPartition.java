package 算法.热题100.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**https://leetcode.cn/problems/partition-equal-subset-sum/description/?envType=study-plan-v2&envId=top-100-liked
 * 416. 分割等和子集
 * @Author binbin
 * @Date 2024 09 18 10 43
 **/
public class CanPartition {
    public static boolean canPartition(int[] nums) {
        Arrays.sort(nums);
        int sum = 0;
        for (int i = 0; i <= nums.length - 1; i++) {
            sum += nums[i];

        }
        if (sum % 2 == 1) {
            return false;
        }
        int target = sum / 2;

        boolean[][] dp = new boolean[nums.length][target + 1];
        for (int i = 1; i <= nums.length - 1; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i <= nums.length - 1; i++) {
            for (int j = 1; j <= target; j++) {
                if (j>=nums[i]){
                    dp[i][j] = dp[i - 1][j - nums[i]] | dp[i-1][j];
                }else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[nums.length - 1][target];

    }


    public static void main(String[] args) {
        canPartition(new int[]{1, 2, 5});
    }
}
