package 算法.热题100.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @Author binbin
 * @Date 2024 08 02 09 38
 **/
public class IsValid {

    /**
     * 栈
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');
        for (int i = 0; i <= s.length() - 1; i++) {
            if (!stack.empty()) {
                Character character = stack.peek();
                if (character.equals(map.get(s.charAt(i)))) {
                    stack.pop();
                } else {
                    stack.push(s.charAt(i));
                }
            } else {
                stack.push(s.charAt(i));
            }

        }
        return stack.empty();
    }


}
