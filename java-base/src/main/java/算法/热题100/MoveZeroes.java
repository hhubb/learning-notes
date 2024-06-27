package 算法.热题100;

/**
 * 283. 移动零
 * https://leetcode.cn/problems/move-zeroes/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 25 09 22
 **/
public class MoveZeroes {
    public void moveZeroes(int[] nums) {
        int i = 0;
//        for (int j = 1; j <= nums.length - 1; j++) {
//            if (nums[i] == 0 && nums[j] != 0) {
//                nums[i] = nums[j];
//                nums[j] = 0;
//                i = j;
//            } else if (nums[i] != 0) {
//                i++;
//            }
//        }
        int j = 0;
        while (i <= j && j < nums.length) {
            if (nums[i] == 0 && nums[j] != 0) {
                nums[i] = nums[j];
                nums[j] = 0;
            }
            i++;
            j++;
        }


    }
}
