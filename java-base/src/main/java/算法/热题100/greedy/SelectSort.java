package 算法.热题100.greedy;

/**
 * 选择排序
 * @Author binbin
 * @Date 2024 08 14 13 46
 **/
public class SelectSort {
    //[4,2,6,1,3,7,4,5]
    //[1,2,6,4,3,7,4,5]
    //[1,2,6,4,3,7,4,5]
    //[1,2,6,4,3,7,4,5]

    /**
     * 每次找到最小的数与自己交换，以实现排序，我不关心整体是一个声明样子，我只关心我这一步的最小值。
     * @param nums
     */
    public static void selectSort(int[] nums) {
        for (int i=0;i<=nums.length-1;i++){
            int j=i+1;
            int min=i;
            while (j<=nums.length-1){
                if (nums[min]<nums[j]){
                    min=j;
                }
                j++;
            }
            int tmp=nums[i];
            nums[i]=nums[min];
            nums[min]=tmp;
        }
        System.out.println(nums);
    }

    public static void main(String[] args) {
        selectSort(new int[]{4,2,6,1,3,7,4,5});
    }
}
