package 算法.热题100.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode.cn/problems/rotate-array/description/?envType=study-plan-v2&envId=top-100-liked
 * 189. 轮转数组
 * todo 没太搞懂
 * @Author binbin
 * @Date 2024 08 20 15 44
 **/
public class Rotate {
    /**
     * 额外数组
     *
     * @param nums
     * @param k
     */
    public static void rotate(int[] nums, int k) {
        int length = nums.length;
        int[] newArray = new int[length];
        for (int i = 0; i <= length - 1; i++) {
            newArray[(i + k) % length] = nums[i];
        }
        System.arraycopy(newArray, 0, nums, 0, length);
    }

    /**
     * 环形替换
     *
     * @param nums
     * @param k
     */
    public static void rotate2(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        int count = gcd(k, n);
        for (int start = 0; start < count; start++) {
            int curr = start;
            int prev = nums[start];
            do {
                int next = (curr + k) % n;
                int tmp = nums[next];
                nums[next] = prev;
                prev = tmp;
                curr = next;
            } while (start != curr);
        }
    }

    private static int gcd(int k, int n) {
        return n > 0 ? gcd(n, k % n) : k;
    }


    /**
     * 暴力超时
     *
     * @param nums
     * @param k
     */
    public void rotate1(int[] nums, int k) {
        int length = nums.length - 1;
        while (k > 0) {
            int last = nums[length];
            for (int i = length; i > 0; i--) {
                nums[i] = nums[i - 1];
            }
            nums[0] = last;
            k--;
        }
    }
}
