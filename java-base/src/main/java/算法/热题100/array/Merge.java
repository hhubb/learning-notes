package 算法.热题100.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * https://leetcode.cn/problems/merge-intervals/description/?envType=study-plan-v2&envId=top-100-liked
 * 56. 合并区间
 *
 * @Author binbin
 * @Date 2024 08 19 11 07
 **/
public class Merge {
    public static int[][] merge(int[][] intervals) {
        sort(intervals, 0, intervals.length - 1);
        int[][] res = new int[intervals.length][2];
        int start = intervals[0][0];
        int end = intervals[0][1];
        int rs = 0;
        for (int i = 1; i <= intervals.length - 1; i++) {
            int[] nums = intervals[i];

            if (nums[0] > end || nums[1] < start) {
                int[] r = new int[2];
                r[0] = start;
                r[1] = end;
                res[rs] = r;
                rs++;
                start = nums[0];
                end = nums[1];
            } else {
                end = Math.max(end, nums[1]);
            }
            start = Math.min(start, nums[0]);
        }
        int[] r = new int[2];
        r[0] = start;
        r[1] = end;
        res[rs] = r;
        return Arrays.copyOf(res, rs + 1);
    }

    private static void sort(int[][] intervals, int begin, int end) {
        if (begin > end) {
            return;
        }

        int l = begin;
        int r = end;
        int[] flag = intervals[begin];
        while (l < r) {
            while (l < r && intervals[r][0] > flag[0]) {
                r--;
            }
            while (l < r && intervals[l][0] <= flag[0]) {
                l++;
            }
            int[] tmp = intervals[r];
            intervals[r] = intervals[l];
            intervals[l] = tmp;
        }
        intervals[begin] = intervals[l];
        intervals[l] = flag;
        sort(intervals, begin, l - 1);
        sort(intervals, l + 1, end);
    }

    public static void main(String[] args) {
        merge(new int[][]{{2,3}, {4,5},{6,7},{8,9},{1,10}});
    }
}
