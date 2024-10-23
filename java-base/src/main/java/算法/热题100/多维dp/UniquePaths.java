package 算法.热题100.多维dp;

/**https://leetcode.cn/problems/unique-paths/description/?envType=study-plan-v2&envId=top-100-liked
 * 62. 不同路径
 * @Author binbin
 * @Date 2024 10 21 10 29
 **/
public class UniquePaths {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (i - 1 == 0 && j - 1 == 0) {
                    dp[i][j] =1;
                } else {
                    dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
                }


            }
        }
        return dp[m][n];
    }
}
