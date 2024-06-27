package 算法.热题100;

import java.util.*;

/**
 * 15. 三数之和
 * https://leetcode.cn/problems/3sum/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 25 10 12
 **/
public class ThreeSum {
    /**
     * 输入：nums = [-1,0,1,2,-1,-4]   [-4,-1,-1,0,1,2]
     * 输出：[[-1,-1,2],[-1,0,1]]
     * 解释：
     * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
     * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
     * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
     * 不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
     * 双指针
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i <= nums.length - 1; i++) {
            // 需要和上一次枚举的数不相同,跳过重复元素
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {
                if (nums[j] + nums[k] < -nums[i]) {
                    j++;
                } else if (nums[j] + nums[k] > -nums[i]) {
                    k--;
                } else {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(-nums[i] - nums[j]);
                    res.add(list);
                    // 跳过重复元素
                    while (j < k && nums[j] == nums[j + 1]) { //找到最后一哥重复元素
                        j++;
                    }
                    j++; //跳过最后一个重复元素
                    // 跳过重复元素
                    while (j < k && nums[k] == nums[k - 1]) {
                        k--;
                    }
                    k--;
                }
            }
        }
        return res;
    }


    /**
     * N^2
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSum2(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        Set<Integer> tmp = new HashSet<>();
        for (int i = 0; i <= nums.length - 1; i++) {
            if (tmp.contains(nums[i])) {
                continue;
            }
            tmp.add(nums[i]);
            HashMap<Integer, Integer> map = new HashMap<>();
            Set<Integer> tmp2 = new HashSet<>();
            for (int j = i + 1; j <= nums.length - 1; j++) {
                if (map.containsKey(-nums[i] - nums[j]) && !tmp2.contains(nums[i] + nums[j])) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(-nums[i] - nums[j]);
                    res.add(list);
                    tmp2.add(nums[i] + nums[j]);
                } else {
                    map.put(nums[j], j);
                }

            }
        }
        return res;
    }

    /**
     * 暴力
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSum1(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i <= nums.length - 1; i++) {
            for (int j = 0; j <= nums.length - 1; j++) {
                for (int k = 0; k <= nums.length - 1; k++) {
                    if (i != j && j != k && i != k && nums[i] + nums[j] + nums[k] == 0) {
                        Integer[] resArray = new Integer[]{nums[i], nums[j], nums[k]};
                        Arrays.sort(resArray);
                        List<Integer> resList = Arrays.asList(resArray);
                        if (!res.contains(resList)) {
                            res.add(resList);
                        }

                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        threeSum(new int[]{0, 0, 0});
    }
}
