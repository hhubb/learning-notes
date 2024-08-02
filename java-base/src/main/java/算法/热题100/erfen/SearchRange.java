package 算法.热题100.erfen;

/**
 * https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked
 * 34. 在排序数组中查找元素的第一个和最后一个位置
 *
 * @Author binbin
 * @Date 2024 07 31 09 26
 **/
public class SearchRange {
    /**
     * 二分发
     * @param nums
     * @param target
     * @return
     */
    public int[] searchRange(int[] nums, int target) {
        int[] res = new int[2];
        int begin = -1;
        int end = -1;
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (nums[mid] < target) {
                low = mid + 1;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                //找到目标元素
                begin = mid;
                end = mid;
                //向前找开始元素index的前第一个不是target的元素index
                while (begin>=0 &&nums[begin] == target ) {
                    begin--;
                }
                //加1调整回起始位置
                begin++;
                //向后找结束元素index后第一个不是target的元素index
                while (end<=nums.length - 1 &&nums[end] == target ) {
                    end++;
                }
                //减1调整回结束位置
                end--;
                break;
            }
        }
        res[0] = begin;
        res[1] = end;
        return res;

    }

    /**
     * 暴力
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] searchRange1(int[] nums, int target) {
        int[] res = new int[2];
        int begin = -1;
        int end = -1;
        for (int i = 0; i <= nums.length - 1; i++) {
            if (target == nums[i]) {
                if (begin == -1) {
                    begin = i;
                }
                end = i;

            }
        }
        res[0] = begin;
        res[1] = end;
        return res;
    }
}
