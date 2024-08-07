package 算法.热题100.stack;

import java.util.Stack;

/**
 * @Author binbin
 * @Date 2024 08 06 10 52
 **/
public class DecodeString {
    //输入：s = "2[abc]3[cd]ef"
    //输出："abcabccdcdcdef"
    //输入：s = "3[accacc]"
    //输出："accaccacc"
    public static void main(String[] args) {
        decodeString("3[z]2[2[y]pq4[2[jk]e1[f]]]ef");
    }

    public static String decodeString(String s) {
        int length = s.length();
        if (length == 0) {
            return "";
        }
        Stack<String> stack = new Stack();


        int i = 0;
        while (i <= length - 1) {
            char c = s.charAt(i);
            stack.push(String.valueOf(c));
            if (!stack.empty()) {
                String cr = stack.peek().toString();
                if (cr.equals("]")) {
                    stack.pop();
                    String str = "";
                    String baseStr = getBaseStr(stack);
                    stack.pop();
                    Integer count = getCount(stack);
                    for (int j = 1; j <= count; j++) {
                        str += baseStr;
                    }
                    while (!stack.empty() && !"[".equals(stack.peek())) {
                        str = stack.pop() + str;
                    }
                    stack.push(str);
                }
            }
            i++;
        }
        String res = "";
        while (!stack.empty()) {
            res = stack.pop() + res;
        }
        return res;

    }

    private static Integer getCount(Stack<String> stack) {
        String count = "";
        while (!stack.empty()) {
            String num = stack.peek();
            if (num.length() > 1) {
                break;
            }
            if (num.charAt(0) >= '0' && num.charAt(0) <= '9') {
                count = stack.pop() + count;
            } else {
                break;
            }
        }
        return Integer.valueOf(count);


    }

    private static String getBaseStr(Stack<String> stack) {
        String baseStr = "";
        while (!"[".equals(stack.peek())) {
            baseStr = stack.pop() + baseStr;
        }
        return baseStr;
    }
}
