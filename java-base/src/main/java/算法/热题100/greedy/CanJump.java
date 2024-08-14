package 算法.热题100.greedy;

/**55. 跳跃游戏
 * https://leetcode.cn/problems/jump-game/solutions/203549/tiao-yue-you-xi-by-leetcode-solution/?envType=study-plan-v2&envId=top-100-liked
 * @Author binbin
 * @Date 2024 08 14 10 57
 **/
public class CanJump {
    public boolean canJump(int[] nums) {

        int reach=0;
        for (int i=0;i<nums.length-1;i++){
            //当前位置超过了最远能到达的位置
            if (i>reach){
                return false;
            }
            //nums[i]+i为当前位置能像后跳的最远的位置，比较更新最远能到达的位置
            reach =Math.max(reach,nums[i]+i);
        }

        return true;
    }
}
