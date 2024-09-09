package 算法.热题100.dp;

import java.util.Arrays;
import java.util.Collections;

/**
 * https://leetcode.cn/problems/coin-change/description/?envType=study-plan-v2&envId=top-100-liked
 * 322. 零钱兑换
 *
 * @Author binbin
 * @Date 2024 09 06 10 51
 **/
public class CoinChange {
    public static int coinChange(int[] coins, int amount) {
        int[] res = new int[amount + 1];
        for (int i = 1; i <= res.length - 1; i++) {
            res[i] = -1;
            for (int j = 0; j <= coins.length - 1; j++) {
                if (i >= coins[j] && res[i - coins[j]] != -1) {
                    int count = res[i - coins[j]] + 1;
                    res[i] = res[i] == -1 ? count : Math.min(count, res[i]);
                }

            }
        }
        return res[amount];
    }


    public static void main(String[] args) {
        coinChange(new int[]{186, 419, 83, 408}, 6249);
    }
}
