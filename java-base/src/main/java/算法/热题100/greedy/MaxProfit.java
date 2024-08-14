package 算法.热题100.greedy;

/**
 * @Author binbin
 * @Date 2024 08 14 10 12
 **/
public class MaxProfit {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int min = prices[0];
        int max = prices[0];
        for (int i = 1; i <= prices.length - 1; i++) {
            //当前值如果小于最小值，计算差值并重新定义最大值和最小值
            if (prices[i] < min) {
                maxProfit = Math.max(maxProfit, max - min);
                min = prices[i];
                max = prices[i];
                continue;
            }
            //当前值如果大于最大值就修改最大值。
            if (prices[i] > max) {
                max = prices[i];
            }
        }
        return Math.max(maxProfit, max - min);
    }

    /**
     * 暴力
     *
     * @param prices
     * @return
     */
    public int maxProfit1(int[] prices) {
        int maxProfit = 0;
        int max = 0;
        int min = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            int j = i + 1;
            while (j <= prices.length - 1) {
                maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
                j++;
            }
        }
        return maxProfit;
    }
}
