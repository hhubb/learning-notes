package 算法.热题100.dp;

import java.util.List;
import java.util.Stack;

/**
 * https://leetcode.cn/problems/longest-valid-parentheses/description/?envType=study-plan-v2&envId=top-100-liked
 * 32. 最长有效括号
 *
 * @Author binbin
 * @Date 2024 09 19 10 47
 **/
public class LongestValidParentheses {
    public int longestValidParentheses(String s) {
        Stack<Character> stack = new Stack<>();
        int max = 0;
        int count = 0;
        for (int i = 0; i <= s.length() - 1; i++) {
            char c = s.charAt(i);
            if ('(' == c) {
                stack.push(c);
            } else {
                stack.pop();
                count += 2;
                max = Math.max(count, max);
            }
        }
        return max;
    }
}
