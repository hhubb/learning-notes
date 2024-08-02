package 算法.热题100.erfen;

/**
 * https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked
 * 153. 寻找旋转排序数组中的最小值
 *
 * @Author binbin
 * @Date 2024 08 01 13 12
 **/
public class FindMin {
    /**
     * 二分
     * @param nums
     * @return
     */
    public int findMin(int[] nums) {
        if (nums.length==1){
            return nums[0];
        }
        int low = 0;
        int high = nums.length - 1;
        int min=nums[low];
        while (low<=high){
            int mid = (low + high) / 2;
            if (nums[low]<=nums[mid]){//说明此时左边是有序的
                min=Math.min(min,nums[low]);
                low=mid+1; //下一次寻找右边
            }else { //说明右边是有序的
                min=Math.min(min,nums[mid]);
                high=mid-1;//下一次寻找左边
            }
        }
        return min;
    }

    /**
     * 个人解题思路
     * @param nums
     * @return
     */
    public int findMin1(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        int flag = (low + high) / 2;
        //找拐点
        if (nums[flag] < nums[low]) {
            while (flag >= 0 && nums[flag] < nums[low]) {
                flag--;
            }
            flag++;
        }
        if (nums[flag] > nums[high]) {
            while (flag <= high && nums[flag] > nums[high]) {
                flag++;
            }
        }
        //比较拐点和第一个元素谁小
        return Math.min(nums[low],nums[flag]);
    }
}
