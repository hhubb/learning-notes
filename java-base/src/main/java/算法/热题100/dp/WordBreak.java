package 算法.热题100.dp;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**https://leetcode.cn/problems/word-break/description/?envType=study-plan-v2&envId=top-100-liked
 * 139. 单词拆分
 * @Author binbin
 * @Date 2024 09 09 10 48
 **/
public class WordBreak {
    public static boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length() ; i++) {
            for (String word : wordDict) {
                if (i  >= word.length()) {
                    String sub = s.substring(i - word.length() , i );
                    dp[i] = sub.equals(word) && dp[i - word.length()];
                    if (dp[i]) {
                        break;
                    }

                }
            }
        }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        wordBreak("leetcode", Arrays.asList("leet", "code"));
    }
}
