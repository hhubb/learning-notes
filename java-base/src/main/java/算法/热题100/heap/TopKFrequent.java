package 算法.热题100.heap;

import java.util.*;

/**
 * @Author binbin
 * @Date 2024 08 12 13 40
 **/
public class TopKFrequent {
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] res = new int[k];
        for (int num : nums) {
            int count = 0;
            if (map.containsKey(num)) {
                count = map.get(num);
                count++;
            }
            map.put(num, count);

        }
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return map.get(o1) - map.get(o2);
            }
        });
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (priorityQueue.size()<k){
                priorityQueue.add(entry.getKey());
            }else {
                if (map.get(priorityQueue.peek())<entry.getValue()){
                    priorityQueue.remove();
                    priorityQueue.add(entry.getKey());
                }
            }

        }

        for (int i =0; i <=k-1; i++) {
            res[i] = priorityQueue.remove();
        }
        return res;
    }

    public static void main(String[] args) {
        topKFrequent(new int[]{1, 2}, 2);
    }

    public static void quickSort(Integer[] nums, int low, int high) {
        if (low >= high) {
            return;
        }
        int l = low;
        int r = high;
        int flag = nums[low];
        while (l != r) {
            while (l < r && nums[r] > flag) {
                r--;
            }
            while (l < r && nums[l] <= flag) {
                l++;
            }
            if (l < r) {
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
            }
        }
        nums[low] = nums[l];
        nums[l] = flag;
        quickSort(nums, low, l - 1);
        quickSort(nums, l + 1, high);
    }
}
