package 算法.热题100.多维dp;

/**https://leetcode.cn/problems/longest-common-subsequence/description/?envType=study-plan-v2&envId=top-100-liked
 * 1143. 最长公共子序列
 * @Author binbin
 * @Date 2024 10 25 10 38
 **/
public class LongestCommonSubsequence {
    public int longestCommonSubsequence(String text1, String text2) {
        int m=text1.length();
        int n=text2.length();
        int[][] dp=new int[m][n];
        for (int i=1;i<m-1;i++){
            for (int j=1;j<=n-1;j++){
               if (text1.charAt(i-1)==text2.charAt(j-1)){
                   dp[i][j]=dp[i-1][j-1]+1;
               }else {
                   dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
               }
            }
        }
        return dp[m][n];
    }
}
