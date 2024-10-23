package 算法.热题100.技巧;

import java.util.Arrays;

/**https://leetcode.cn/problems/find-the-duplicate-number/description/?envType=study-plan-v2&envId=top-100-liked
 * 287. 寻找重复数
 * @Author binbin
 * @Date 2024 10 17 10 10
 **/
public class FindDuplicate {
    public int findDuplicate(int[] nums) {
        int[] flag=new int[nums.length];
        Arrays.fill(flag,-1);
        for (int i=0;i<=nums.length-1;i++){
            int n=nums[i];
            flag[n]=flag[n]+1;
            if (flag[n]>0){
                return n;
            }

        }
        return 0;
    }
}
