package 算法.热题100.greedy;

/**https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/description/
 * 122. 买卖股票的最佳时机 II
 * @Author binbin
 * @Date 2024 08 14 14 07
 **/
public class MaxProfit2 {
    //怎么保证最大的利润，只有看第二天是增加还是减小，连续增加的情况下，我从最低点进行买入，到下一次即将要下跌的位置卖出，这个时候我的利润是最大的。
    //贪心思想是什么呢？我不关心整体是高是低，我只关心下一天如果比我今天高，我就买入，如果比我今天低，我就卖出。
    public int maxProfit(int[] prices) {
        int maxProfit=0;
        int min=prices[0];
        int max=prices[0];
        for (int i=1;i<=prices.length-1;i++){
            //当前数值比前一天小就就卖出，累加一次收益，并重置最大最小值
            if (prices[i]<prices[i-1]){
                maxProfit=maxProfit+(max-min);
                 min=prices[i];
                 max=prices[i];
                continue;
            }
            //当前数值比前一天大，更新最大值
            if (prices[i]>max){
                max=prices[i];
            }
        }
        maxProfit=maxProfit+(max-min);
        return maxProfit;
    }
}
