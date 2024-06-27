package 算法.热题100;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 49. 字母异位词分组
 * https://leetcode.cn/problems/group-anagrams/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 24 10 02
 **/
public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] sc = s.toCharArray();
            Arrays.sort(sc);
            String key = String.valueOf(sc);
            List<String> val = new ArrayList<>();
            if (map.containsKey(key)) {
                val = map.get(key);
            }
            val.add(s);
            map.put(key, val);
        }
        return new ArrayList<List<String>>(map.values());
    }
}
