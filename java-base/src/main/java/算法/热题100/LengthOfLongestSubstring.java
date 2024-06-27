package 算法.热题100;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author binbin
 * @Date 2024 06 26 11 07
 **/
public class LengthOfLongestSubstring {

    /**
     * 滑动窗口，使用Map改变窗口开始位置
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }
        int maxLength = 0;
        int start = 0; //窗口起始位置
        int end = start + 1; //窗口结束位置
        Map<Character, Integer> map = new HashMap<>();
        while (start < s.length()) {
            map.put(s.charAt(start), start);
            //获取不重复字串的长度：end元素在map中不存在，或者重复元素的索引小于窗口起始位置（因为此时窗口已经划过重复元素，无需考虑）
            while (end < s.length() && (!map.containsKey(s.charAt(end)) || map.get(s.charAt(end)) < start)) {
                map.put(s.charAt(end), end);
                end++;
            }
            maxLength = Math.max(end - start, maxLength);
            //判断窗口结束是否已经超过达到字符串边界（start从0开始end最大为s.length-1）
            if (end == s.length()) {
                break;
            }
            //窗口起始位置变成重复元素位置的下一个
            start = map.get(s.charAt(end)) + 1;
            //把重复元素的索引位置更新
            map.put(s.charAt(end), end);
            //结束窗口移到重复元素的下一个
            end++;
        }
        return maxLength;
    }

    /**
     * 按顺序遍历，使用set记录重复元素
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring1(String s) {
        int maxLength = 0;
        Set<Character> set = new HashSet<>();
        int i = 0;
        while (i < s.length()) {
            int j = i + 1;
            //保存字符
            set.add(s.charAt(i));
            //判断字符是否重复如果没有就加进去，继续检查下一个
            while (j < s.length() && !set.contains(s.charAt(j))) {
                set.add(s.charAt(j));
                j++;
            }
            maxLength = Math.max(set.size(), maxLength);
            set.clear();
            i++;
        }
        return maxLength;
    }
}
