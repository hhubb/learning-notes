package 算法.热题100.技巧;

/**
 * https://leetcode.cn/problems/next-permutation/description/?envType=study-plan-v2&envId=top-100-liked
 * 31. 下一个排列
 *
 * @Author binbin
 * @Date 2024 10 16 09 56
 **/
public class NextPermutation {
    /**
     * 123456
     * 123465
     * 123546
     * 123564
     * 654321
     *
     * @param nums
     */
    public void nextPermutation(int[] nums) {
        int len = nums.length;
        for (int i = len - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {//找到较小数i
                //继续找较大数
                for (int j = len - 1; j > i; j--) {
                    //最差就是i+1,尽量靠右
                    if (nums[j] > nums[i]) {//找到较大数j
                        //交换i，j
                        swap(nums, i, j);
                        //将i之后的元素升序排列
                        reverse(nums, i + 1, len - 1);
                        return;
                    }
                }
            }
        }
        //如果没有提前退出说明是降序，已经是最大的排序了，所以直接升序处理
        reverse(nums, 0, len - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
