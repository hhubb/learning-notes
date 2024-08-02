package 算法.热题100.erfen;

/**https://leetcode.cn/problems/search-insert-position/description/?envType=study-plan-v2&envId=top-100-liked
 * 35. 搜索插入位置
 * @Author binbin
 * @Date 2024 07 30 09 37
 **/
public class SearchInsert {
    public static int searchInsert(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (nums[mid] < target) {
                low = mid + 1;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return target<nums[low]?low:low+1;
    }

    public static void main(String[] args) {
        searchInsert(new int[]{1,3,5,6},0);
    }
}
