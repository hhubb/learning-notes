package 算法.热题100.多维dp;

/**
 * @Author binbin
 * @Date 2024 10 22 10 01
 **/
public class MinPathSum {
    public int minPathSum(int[][] grid) {
        int[][] dp = new int[grid.length][grid[0].length];
        dp[0][0] = grid[0][0];
        //i==0时
        for (int j = 1; j <= grid[0].length - 1; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }
        //j==0
        for (int i = 1; i <= grid.length - 1; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i <= grid.length - 1; i++) {
            for (int j = 1; j <= grid[0].length - 1; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];

            }
        }
        return dp[grid.length - 1][grid[0].length - 1];

    }
}
