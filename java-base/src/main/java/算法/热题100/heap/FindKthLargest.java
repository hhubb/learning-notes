package 算法.热题100.heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**https://leetcode.cn/problems/kth-largest-element-in-an-array/description/?envType=study-plan-v2&envId=top-100-liked
 * 215. 数组中的第K个最大元素
 * 模式识别：维护动态数据的最大最小值，可以考虑堆
 * @Author binbin
 * @Date 2024 08 09 10 06
 **/
public class FindKthLargest {
    public int findKthLargest(int[] nums, int k) {
        //[3,1,2,5,6,4]
        //[3,2,1,5,6,4]
        quickSort(nums,0,nums.length-1,k);
        return nums[nums.length-1-(k-1)];
        //堆顶是最小元素
//        PriorityQueue<Integer> priorityQueue=new PriorityQueue<>((n1,n2)->n1-n2);
//        for (int n:nums){
//            if (priorityQueue.size()==k){
//                priorityQueue.poll();
//            }else {
//                priorityQueue.add(n);
//            }
//        }
//
//        return priorityQueue.poll();
    }


    /**
     * 快排超时
     * @param nums
     * @param low
     * @param high
     */
    private void quickSort(int[] nums, int low, int high,int k) {
        if (low>=high){
            return;
        }
        int i=low;
        int j=high;
        int flag=nums[low];
        while (i<j){
            while ( nums[j]>=flag && i<j){
               j--;
            }
            while ( nums[i]<=flag && i<j){
                i++;
            }
            //nums[j]与nums[i] 交换位置后，此时nums[i]一定是小于flag也就是nums[low]
            int tmp=nums[i];
            nums[i]=nums[j];
            nums[j]=tmp;
        }
        //此时i==j 循环结束，由于nums[i]一定是小于nums[low]，所以交换nums[i]和nums[low]
        nums[low]=nums[i];
        nums[i]=flag;
        //此时[low,i-1] 都是小于nums[low]的，[j+1,high]都是大于nums[low]的
        //可以判断i与nums.length-1-(k-1)的关系，如果i=nums.length-1-(k-1)说明num[i]已经是第k个元素了
        //如果小于只需要递归右边，大于只需要递归左边
        if (i==nums.length-1-(k-1)){
            return;
        }
        if (i>nums.length-1-(k-1)){
            quickSort(nums,low,i-1,k);
        }else {
            quickSort(nums,i+1,high,k);
        }


    }


}
