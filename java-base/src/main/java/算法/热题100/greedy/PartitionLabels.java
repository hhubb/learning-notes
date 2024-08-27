package 算法.热题100.greedy;

import java.util.*;

/**https://leetcode.cn/problems/partition-labels/description/?envType=study-plan-v2&envId=top-100-liked
 * 763. 划分字母区间
 * @Author binbin
 * @Date 2024 08 16 14 24
 **/
public class PartitionLabels {
    /**
     * 标准贪心
     *
     * @param s
     * @return
     */
    public List<Integer> partitionLabels(String s) {
        //统计每个字母的结束位置
        int[] lastList = new int[26];
        for (int i = 0; i <= s.length() - 1; i++) {
            lastList[s.charAt(i) - 'a'] = i;
        }
        //串开始和截至位置
        int start = 0;
        int end = 0;
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i <= s.length() - 1; i++) {
            //如果遍历到的字符的截至位置大于end，则更新end
            int elementEnd = lastList[s.charAt(i) - 'a'];
            end = Math.max(end, elementEnd);
            //如果当前位置等于end位置，说明当前串满足同一字母最多出现在一个片段中。记录这个片段
            if (i == end) {
                res.add(end - start + 1);
                //记录后更新下一串的起始位置，继续循环，直至字符串s遍历完
                start = end + 1;
            }
        }
        return res;
    }

    /**
     * 自己写的
     *
     * @param s
     * @return
     */
    public List<Integer> partitionLabels1(String s) {
        List<Integer> res = new ArrayList<>();
        Map<Character, Stack<Integer>> map = new HashMap<>();
        for (int i = 0; i <= s.length() - 1; i++) {
            char c = s.charAt(i);
            Stack<Integer> stack = new Stack<>();
            if (map.containsKey(c)) {
                stack = map.get(c);
            }
            if (stack.size() >= 2) {
                stack.pop();
            }
            stack.push(i);
            map.put(c, stack);
        }
        char start = s.charAt(0);
        Stack<Integer> startRang = map.get(start);
        while (startRang != null) {
            Integer startIndex = startRang.firstElement();
            Integer endIndex = startRang.peek();
            Set<Character> set = new HashSet<>();
            for (int i = startIndex + 1; i <= endIndex - 1; i++) {
                char e = s.charAt(i);
                if (!set.contains(s.charAt(i))) {
                    Stack<Integer> rang = map.get(e);
                    endIndex = Math.max(endIndex, rang.peek());
                    set.add(e);
                }
            }
            res.add(endIndex - startIndex + 1);
            if (endIndex == s.length() - 1) {
                break;
            }
            startRang = map.get(s.charAt(endIndex + 1));

        }
        return res;

    }
}
