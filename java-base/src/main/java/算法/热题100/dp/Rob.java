package 算法.热题100.dp;

/**
 * https://leetcode.cn/problems/house-robber/description/?envType=study-plan-v2&envId=top-100-liked
 * 198. 打家劫舍
 */
public class Rob {
    public static int rob(int[] nums) {
        int length = nums.length;
        if (length == 1) {
            return nums[length - 1];
        }
        int[] res = new int[length+1];
        res[1]=nums[0];
        for (int i = 2; i <= res.length - 1; i++) {
            res[i]=Math.max(res[i-2]+nums[i-1],res[i-1]);
        }
        return res[res.length-1];

    }

    public static void main(String[] args) {
        rob(new int[]{2,7,9,3,1});
    }
}
