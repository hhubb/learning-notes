package 算法.热题100;

import java.util.*;

/** 438. 找到字符串中所有字母异位词
 * https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/?envType=study-plan-v2&envId=top-100-liked
 * @Author binbin
 * @Date 2024 06 27 09 09
 **/
public class FindAnagrams {

    /**
     * 滑动窗口
     * 异位词，通常定义一个长度为26的数组 new int[26] 解决
     * @param s
     * @param p
     * @return
     */
    public static List<Integer> findAnagrams(String s, String p) {
        if (s.length() < p.length()) {
            return new ArrayList<>();
        }

        List<Integer> res = new ArrayList<>();
        int[] sc = new int[26];
        int[] pc = new int[26];
        for (int i = 0; i <= p.length() - 1; i++) {
            pc[p.charAt(i) - 'a'] = pc[p.charAt(i) - 'a']+1;
        }
        int i = 0;
        while (i <= s.length() - p.length()) {
            for (int j = 0; j <= p.length() - 1; j++) {
                sc[s.charAt(i + j) - 'a'] = sc[s.charAt(i + j) - 'a']+1;
            }
            if (Arrays.equals(sc, pc)) {
                res.add(i);
            }
            sc = new int[26];
            i++;
        }
        return res;
    }


    /**
     * 超时
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams1(String s, String p) {
        char[] cArray = s.toCharArray();
        Map<Character, Integer> map = getInitMap(p);

        List<Integer> res = new ArrayList<>();
        int i = 0;
        while (i <= s.length() - p.length()) {
            boolean anagrams = true;
            for (int j = 0; j <= p.length() - 1; j++) {
                char c = cArray[i + j];
                if (map.containsKey(c) && map.get(c) > 0) {
                    map.put(c, map.get(c) - 1);
                } else if (map.containsKey(c) && map.get(c) == 0) {
                    anagrams = false;
                    i++;
                } else {
                    i = i + j + 1;
                    anagrams = false;
                    break;
                }
            }
            if (anagrams) {
                res.add(i);
                i++;
            }
            map = getInitMap(p);
        }
        return res;
    }

    private  Map<Character,Integer> getInitMap(String p){
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i <= p.toCharArray().length - 1; i++) {
            char c = p.charAt(i);
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }
        return map;
    }


}
