package 算法.热题100;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 128. 最长连续序列
 * https://leetcode.cn/problems/longest-consecutive-sequence/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 25 09 03
 **/
public class LongestConsecutive {
    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int maxLong = 0;
        for (Integer num : set) {
            //如果存在比当前元素小一的元素，那就跳过
            //因为以下是假设当前元素是连续数列中最小元素进行统计的
            //简单来说就是每个数都判断一次这个数是不是连续序列的开头那个数。
            if (set.contains(num-1)){
                continue;
            }
            int x = num + 1;
            int currentLong = 1;
            while (set.contains(x)) {
                currentLong += 1;
                x += 1;
            }
            maxLong = Math.max(maxLong, currentLong);
        }
        return maxLong;

    }
}
