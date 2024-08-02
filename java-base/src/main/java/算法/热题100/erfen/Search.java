package 算法.热题100.erfen;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/search-in-rotated-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked
 * 33. 搜索旋转排序数组
 *
 * @Author binbin
 * @Date 2024 07 31 10 05
 **/
public class Search {

    /**
     * 二分
     * @param nums
     * @param target
     * @return
     */
    public static int search(int[] nums, int target) {
        if (nums.length==0){
            return -1;
        }
        if (nums.length==1){
            return nums[0]==target?0:-1;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low<high){
            int mid=(low+high)/2;
            if (nums[mid]==target){
                return mid;
            }
            //判断左侧是否有序
            if (nums[mid]>=nums[low]){
                if (nums[low]<=target && nums[mid]>=target){
                    high=mid-1; //因为已经判断过nums[mid]不等于目标值
                }else {
                    low=mid+1;
                }
            }else { //左侧无序那么右侧一定有序
                if (nums[mid]<=target && nums[high]>=target){
                    low=mid+1;
                }else {
                    high=mid-1;
                }
            }
        }
        return nums[low]==target?low:-1;


    }



    public static void main(String[] args) {
        search(new int[]{1,3},0);

    }


    public static int erfen(int[] nums, int low, int high, int target) {

        while (low <= high) {
            int mid = (low + high) / 2;
            if (nums[mid] > target) {
                 high = mid - 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }


    /**
     * 暴力
     *
     * @param nums
     * @param target
     * @return
     */
    public int search1(int[] nums, int target) {
        for (int i = 0; i <= nums.length - 1; i++) {
            if (target == nums[i]) {
                return i;
            }
        }
        return -1;
    }
}
