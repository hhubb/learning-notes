package 算法.热题100.heap;

/**
 * @Author binbin
 * @Date 2024 08 12 10 14
 **/
public class QuickSort {

    //4,6,1,3,9,5,8,2
    //4,2,1,3,9,5,8,6
    //4,2,1,3,9,5,8,6
    public static void quickSort(int[] nums, int low, int high) {
        if (low >= high) {
            return;
        }
        int l = low;
        int r = high;
        //基准节点
        int flag = nums[low];
        //将比基准点大的元素都放在基准点右面，比基准点小的元素都放在左边
        while (l != r) {
            while (l < r && nums[r] > flag) {
                r--;
            }
            while (l < r && nums[l] <= flag) {
                l++;
            }
            if (l < r) {
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
            }
        }
        //这一步是为了解决nums[low]作为基准元素后后面所有的元素都比它小或者比他大，造成不平衡
        //所以随机选择一个元素与他交换，所以选择l与r重合的元素与nums[low]交换
        nums[low] = nums[l];
        nums[l] = flag;

        quickSort(nums, low, l - 1);
        quickSort(nums, l + 1, high);
    }

    public static void main(String[] args) {
        int[] nums = new int[]{4, 6, 1, 3, 9, 5, 8, 2};
        quickSort(nums, 0, nums.length - 1);
        System.out.println(nums);
    }
}
