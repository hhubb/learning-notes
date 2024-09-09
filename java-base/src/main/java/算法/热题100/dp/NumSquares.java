package 算法.热题100.dp;

/**
 * https://leetcode.cn/problems/perfect-squares/description/?envType=study-plan-v2&envId=top-100-liked
 * 279. 完全平方数
 *
 * @Author binbin
 * @Date 2024 09 05 10 23
 **/
public class NumSquares {
    public int numSquares(int n) {
        int[] res = new int[n+1];
        for (int i = 1; i <= res.length-1; i++) {
            for (int j = 1; j * j <= i; j++) {
                //当前i 想要可以拆解出j的完全平方，必须减去j的完全平方，然后加上剩余数值的最小完全平方数的个数就是，i目前的最小完全平方数的个数。
                int count = res[i - j * j] + 1;
                if (res[i] == 0) {
                    //如果是0，则将数值写入
                    res[i] = count ;
                } else {
                    //否则将对比原来的数值，保留小的那个
                    res[i] = Math.min(count, res[i]);
                }
            }
        }
        return res[n];
    }
}
