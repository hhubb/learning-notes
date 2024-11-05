package 算法.热题100.多维dp;

/**https://leetcode.cn/problems/edit-distance/description/?envType=study-plan-v2&envId=top-100-liked
 * 72. 编辑距离
 * @Author binbin
 * @Date 2024 10 28 10 23
 **/
public class MinDistance {
    public int minDistance(String word1, String word2) {
        int[][] dp=new int[word1.length()+1][word2.length()+1];
        //如果word2为空，那么word1到word2的距离是word1的长度（将word1都删除）
        for (int i=0;i<=word1.length();i++){
            dp[i][0]=i;
        }
        //如果word1为空，那么word1到word2的距离是word2的长度（将word2都插入）
        for (int j=0;j<=word2.length();j++){
            dp[0][j]=j;
        }

        for (int i=1;i<=word1.length();i++){
            for (int j=1;j<=word2.length();j++){
                //如果两个字母相等，说明不需要变化了，最短距离和两哥字符串上一个不相等的一样
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else {
                    /**
                     * 如果不等word1当前的字母与word2当前的字母相等有三个办法
                     * 1. 在当前i-1的基础上删除当前的word1[i]，dp[i-1][j]+1
                     * 2. 在当前i的基础上增加一个word[j]，dp[i][j-1]+1
                     * 3. 在i-1和j-1的基础上将word1[i]替换成word2[j]，dp[i-1][j-1]+1
                     */
                    dp[i][j]=Math.min(Math.min(dp[i-1][j],dp[i][j-1]),dp[i-1][j-1])+1;
                }
            }
        }
        return dp[word1.length()][word2.length()];

    }
}
