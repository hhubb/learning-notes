package 算法.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * https://leetcode.cn/problems/intersection-of-two-arrays/description/
 * 349. 两个数组的交集
 *
 * @Author binbin
 * @Date 2025 02 19 16 24
 **/
public class Intersection {
    public int[] intersection(int[] nums1, int[] nums2) {
        boolean[] array = new boolean[1001];
        for (int i = 0; i <= nums1.length - 1; i++) {
            array[nums1[i]] = true;
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <= nums2.length - 1; i++) {
            if (array[nums2[i]]) {
                result.add(nums2[i]);
                array[nums2[i]] = false;
            }
        }
        int[] a = new int[result.size()];
        for(int i = 0; i < result.size(); i++) {
            a[i] = result.get(i);
        }
        return a;

    }

    public int[] intersection1(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i <= nums1.length - 1; i++) {
            set.add(nums1[i]);
        }
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i <= nums2.length - 1; i++) {
            if (set.contains(nums2[i])) {
                result.add(nums2[i]);
            }
        }
        int[] array = new int[result.size()];
        Iterator<Integer> iterator = result.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Integer integer = iterator.next();
            array[i] = integer;
            i++;

        }
        return array;

    }
}
