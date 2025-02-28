package 算法.array;

import java.util.Arrays;

/**
 * @Author binbin
 * @Date 2025 02 12 17 59
 **/
public class ThirdMax {
    public static void main(String[] args) {
        thirdMax(new int[]{1, 1, 2});
    }

    public static int thirdMax(int[] nums) {
       // quickSort(nums, 0, nums.length - 1);
        Arrays.sort(nums);
        if (nums.length == 2) {
            return nums[1];
        }
        int f = nums[0];
        int count = 3;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (count > 0) {
                if (f != nums[i]) {
                    count--;
                    f = nums[i];
                }
            }

        }
        if (count == 0) {
            return f;
        }

        return nums[nums.length - 1];

    }


    private static void quickSort(int[] nums, int l, int r) {
        if (l >= r) {
            return;
        }
        int low = l;
        int high = r;
        int f = nums[l];
        while (low < high) {
            while (low < high && nums[high] >= f) {
                high--;
            }
            while (low < high && nums[low] <= f) {
                low++;
            }

            if (low < high) {
                int tmp = nums[high];
                nums[high] = nums[low];
                nums[low] = tmp;
            }
        }
        nums[l] = nums[low];
        nums[low] = f;


        quickSort(nums, l, low - 1);
        quickSort(nums, low + 1, r);
    }
}
