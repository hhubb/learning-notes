package 算法.热题100.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/climbing-stairs/description/?envType=study-plan-v2&envId=top-100-liked
 * 70. 爬楼梯
 *
 * @Author binbin
 * @Date 2024 09 02 10 45
 **/
public class ClimbStairs {

    /**
     * 动态规划
     *
     * @param n
     * @return
     */
    public int climbStairs(int n) {
        if(n==1 || n==2){
            return n;
        }
        int dp[] = new int[n + 1];
        dp[1]=1;
        dp[2]=2;
        for (int i = 3; i <= n; i++) {
            int r = dp[i - 2] + dp[i - 1];
            dp[i] = r;
        }
        return dp[n];

    }

    /**
     * 递归，斐波那契+哈希
     *
     * @param n
     * @return
     */
    Map<Integer, Integer> res = new HashMap<>();

    public int climbStairs1(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        if (res.containsKey(n)) {
            return res.get(n);
        }
        int r = climbStairs1(n - 1) + climbStairs1(n - 2);
        res.put(n, r);
        return r;
    }
}
