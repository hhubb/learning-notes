package 算法.热题100.dp;

/**
 * https://leetcode.cn/problems/longest-increasing-subsequence/description/?envType=study-plan-v2&envId=top-100-liked
 * 300. 最长递增子序列
 *
 * //todo https://writings.sh/post/longest-increasing-subsequence-revisited
 * @Author binbin
 * @Date 2024 09 10 10 58
 **/
public class LengthOfLIS {
    public static int lengthOfLIS(int[] nums) {
        int length = nums.length;
        int[] dp = new int[length + 1];
        dp[0] = 1;
        int max=1;
        for (int i = 1; i <= length; i++) {
            dp[i] = 1;
            for (int j = i; j >= 1; j--) {
                if (nums[j - 1] < nums[i - 1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);

                }
            }
            max=Math.max(dp[i],max);

        }
        return max;
    }

    public static void main(String[] args) {
        lengthOfLIS(new int[]{7,7,7,7,7,7,7});
    }
}
