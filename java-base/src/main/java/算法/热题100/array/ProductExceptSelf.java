package 算法.热题100.array;

/**
 * https://leetcode.cn/problems/product-of-array-except-self/description/?envType=study-plan-v2&envId=top-100-liked
 * 238. 除自身以外数组的乘积
 *
 * @Author binbin
 * @Date 2024 08 26 10 42
 **/
public class ProductExceptSelf {
    /**
     * 不用除法，且在 O(n) 时间复杂度内完成此题。
     * 除自身可以将值拆成自身之前的数组乘积*自身之后的数组乘积
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {
        int[] resArray = new int[nums.length];
        //第一次循环获取每个元素自身之前的所有数组的乘积
        int preRes = 1;
        for (int i = 0; i <= nums.length - 1; i++) {
            if (i > 0) {
                preRes = preRes * nums[i - 1];
            }
            resArray[i] = preRes;
        }
        //第二次循环倒叙获取每个元素自身之后的所有数组的乘积同时乘以resArray中上一次循环得到的每个元素自身之前的所有数组的乘积，就是每个元素除自身的乘积
        int lastRes = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i < nums.length - 1) {
                lastRes = lastRes * nums[i + 1];
            }
            resArray[i] = resArray[i] * lastRes;
        }
        return resArray;
    }


    /**
     * 自己作答，使用了除法
     *
     * @param nums
     * @return
     */
    public int[] productExceptSelf1(int[] nums) {
        int res = 1;
        int res_outZero = 1;
        int zeroCount = 0;
        for (int i = 0; i <= nums.length - 1; i++) {
            if (nums[i] == 0) {
                zeroCount++;
            } else {
                res_outZero = res_outZero * nums[i];
            }
            res = res * nums[i];
        }
        int[] resArray = new int[nums.length];
        for (int i = 0; i <= nums.length - 1; i++) {
            if (zeroCount > 1) {
                resArray[i] = 0;
                continue;
            }
            if (nums[i] == 0) {
                resArray[i] = res_outZero;
            } else {
                resArray[i] = res / nums[i];
            }

        }
        return resArray;
    }
}
