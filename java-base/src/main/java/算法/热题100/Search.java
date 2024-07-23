package 算法.热题100;

/**https://leetcode.cn/problems/binary-search/description/
 * 704. 二分查找
 * @Author binbin
 * @Date 2024 07 23 11 18
 **/
public class Search {
    public int search(int[] nums, int target) {
        int low=0;
        int high=nums.length-1;
        while (low<=high){
            int mid=(low+high)/2;
            if (nums[mid]<target){
                low=mid+1;
            }else if (nums[mid]>target){
                high=mid-1;
            }else {
                return mid;
            }
        }
        return 0;
    }
}
