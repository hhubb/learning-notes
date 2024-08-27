package 算法.热题100.array;

import java.util.*;

/**
 * https://leetcode.cn/problems/first-missing-positive/description/?envType=study-plan-v2&envId=top-100-liked
 * 41. 缺失的第一个正数
 *
 * @Author binbin
 * @Date 2024 08 27 11 09
 **/
public class FirstMissingPositive {
    public static int firstMissingPositive(int[] nums) {
        //从题目可知，缺少的最小正整数一定是【0-nums.length】
        //第一次遍历，将数组中小于等于0的元素都替换成nums.length+1
        for (int i = 0; i <= nums.length - 1; i++) {
            if (nums[i] <= 0) {
                nums[i] = nums.length + 1;
            }
        }
        //第二次遍历，将元素对应的下表里的值都变成-1
        for (int i = 0; i <= nums.length - 1; i++) {
            //当某个元素已经被置换为负数后，再获取nums[num - 1]会越界，所以先驱绝对值，先取绝对值，再处理
            int num = Math.abs(nums[i]);
            //因为数组从0开始所以用num-1
            if (num <= nums.length) {
                nums[num - 1] = -Math.abs(nums[num - 1]);
            }

        }
        //第三次遍历找出大于0的元素，就是缺少的最小正整数，因为数组从0开始所以返回i+1
        for (int i = 0; i <= nums.length - 1; i++) {
            if (nums[i] > 0) {
                return i + 1;
            }
        }
        //如果刚好数组中的元素【0-nums.length】都有，就返回nums.length + 1
        return nums.length + 1;
    }

    public static int firstMissingPositive1(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i <= nums.length - 1; i++) {
            set.add(nums[i]);

        }
        for (int i = 1; i <= nums.length ; i++) {
         if (!set.contains(i)){
             return i;
         }
        }
        return nums.length + 1;

    }

    public static void main(String[] args) {
        firstMissingPositive(new int[]{1});
    }
}
