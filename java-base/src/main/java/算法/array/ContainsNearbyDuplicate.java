package 算法.array;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/contains-duplicate-ii/description/
 * 219. 存在重复元素 II
 *
 * @Author binbin
 * @Date 2025 02 17 18 00
 **/
public class ContainsNearbyDuplicate {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> mapLast = new HashMap<>();
        for (int i = 0; i <= nums.length - 1; i++) {
            if (map.containsKey(nums[i])) {
                if (!mapLast.containsKey(nums[i])) {
                    mapLast.put(nums[i], i);
                } else {
                    map.put(nums[i], mapLast.get(nums[i]));
                    mapLast.put(nums[i], i);
                }
                if (i - map.get(nums[i]) <= k) {
                    return true;
                }

            } else {
                map.put(nums[i], i);
            }
        }
        return false;
    }
}
