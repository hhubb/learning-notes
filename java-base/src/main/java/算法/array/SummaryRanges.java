package 算法.array;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/summary-ranges/description/
 * 228. 汇总区间
 *
 * @Author binbin
 * @Date 2025 02 18 17 58
 **/
public class SummaryRanges {
    public static List<String> summaryRanges(int[] nums) {
        List<String> result = new ArrayList<>();
        if (nums.length==0){
            return result;
        }
        int s = nums[0];
        int e = nums[0];
        for (int i = 1; i <= nums.length - 1; i++) {
            if (nums[i]-nums[i-1]==1){
                e=nums[i];
            }else {
                if (s==e){
                    result.add(String.format("%s", s));
                }else {
                    result.add(String.format("%s->%s", s, e));
                }
                s=nums[i];
                e=nums[i];
            }
        }
        if (s<e){
            result.add(String.format("%s->%s", s, e));
        }else {
            result.add(String.format("%s", s));
        }

        return result;
    }
    // List<String> result = new ArrayList<>();
//   result.add(String.format("%s->%s", s, e));
    // result.add(String.format("%s", s));
    public static void main(String[] args) {
        summaryRanges(new int[]{0,1,2,4,5,7});
    }
}
