package 算法.热题100.greedy;

/**https://leetcode.cn/problems/split-a-string-in-balanced-strings/description/
 * 1221. 分割平衡字符串
 * @Author binbin
 * @Date 2024 08 14 13 56
 **/
public class BalancedStringSplit {
    //贪心思路：只要有R与L一样多就分割，分割出来的子串越短，得到的总数越多
    public int balancedStringSplit(String s) {
        int count=0;
        int rCount=0;
        for (int i=0;i<=s.length()-1;i++){
            if ('R'==s.charAt(i)){
                rCount++;
            }else {
                rCount--;
            }
            if (rCount==0){
                count++;
            }
        }
        return count;
    }
}
