package 算法.热题100.greedy;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/jump-game-ii/description/?envType=study-plan-v2&envId=top-100-liked
 * 45. 跳跃游戏 II
 *
 * @Author binbin
 * @Date 2024 08 16 11 11
 **/
public class Jump {
    public static int jump(int[] nums) {
        if (nums.length==1){
            return 0;
        }
        //记录起跳能获取的最大长度
        int start=0;
        int maxLength=nums[start];
        int count=1;
        //最大长度小于结束就继续
        while (maxLength<nums.length-1){
            //跳跃次数+1
            count++;
            int end=maxLength;
            int begin=start;
            //获取每次起跳点到最远距离之内的点能跳跃到的最远的位置。
            for (int i = begin+1; i <= end; i++) {
                int length=i+nums[i];
                if (length>maxLength){
                    maxLength=length;
                    start=i;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        jump(new int[]{2,3,1,1,4});
    }
}
