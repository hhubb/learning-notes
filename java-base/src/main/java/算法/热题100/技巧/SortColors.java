package 算法.热题100.技巧;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/sort-colors/description/?envType=study-plan-v2&envId=top-100-liked
 * 75. 颜色分类
 *
 * @Author binbin
 * @Date 2024 10 15 10 14
 **/
public class SortColors {
    public static void sortColors(int[] nums) {
        int start = 0;
        int end = nums.length - 1;
        quickSort(nums, start, end);
    }

    private static void quickSort(int[] nums, int start, int end) {
        if (start<end){
            int partition=partition(nums,start,end);
            quickSort(nums,start,partition-1);
            quickSort(nums,partition+1,end);
        }
    }

    private static int partition(int[] nums, int start, int end) {
        int low=start;
        int high=end;
        int flag=nums[start];
        while (low<high){
            while (low<high && nums[high]>=flag){
                high--;
            }
            if (low<high){
                nums[low]=nums[high];
                low++;
            }
            while (low<high && nums[low]<=flag){
                low++;
            }
            if (low<high){
                nums[high]=nums[low];
                high--;
            }
        }
        nums[low]=flag;
        return low;
    }

    public static void main(String[] args) {
        sortColors(new int[]{2,0,2,1,1,0});
    }

    public void sortColors1(int[] nums) {
        Arrays.sort(nums);
    }
}
