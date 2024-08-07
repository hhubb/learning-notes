package 算法.热题100.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * https://leetcode.cn/problems/daily-temperatures/description/?envType=study-plan-v2&envId=top-100-liked
 * 739. 每日温度
 *
 * @Author binbin
 * @Date 2024 08 07 09 57
 **/
public class DailyTemperatures {


    public static int[] dailyTemperatures(int[] temperatures) {
        //定义一个单调递减少的栈
        Stack<Integer> stack=new Stack<>();
        int[] answer = new int[temperatures.length];
        for (int i=0;i<=temperatures.length-1;i++){
            while (!stack.empty()&& temperatures[stack.peek()]<temperatures[i]){
               Integer topIndex= stack.pop();
                answer[topIndex]=i-topIndex;
            }
            stack.push(i);
        }
        return answer;
    }
    /**
     * 使用栈，根据答案自己写的
     * @param temperatures
     * @return
     */
    public static int[] dailyTemperatures2(int[] temperatures) {
        //定义一个单调递减少的栈
        Stack<Integer> stack=new Stack<>();
        int[] answer = new int[temperatures.length];
        int i=0;
        while (i<=temperatures.length-1){
            //如果栈为空或者栈定温度大于等于当前温度，把当前温度坐标入栈
            if (stack.empty() || temperatures[stack.peek()]>=temperatures[i]){
                stack.push(i);

            }else //如果栈顶温度小于当前温度，说明遇到了上升点
                if ( temperatures[stack.peek()]<temperatures[i]){
                    //不断弹出栈顶温度和当前温度比较，如果栈顶温度小于当前温度，则把栈顶元素弹出
                while (!stack.empty() && temperatures[stack.peek()]<temperatures[i]){
                    Integer topIndex= stack.pop();
                    //栈顶元素坐标就是答案坐标，当前温度坐标与栈顶坐标差值是答案
                    answer[topIndex]=i-topIndex;
                }
                //最后也把当前元素坐标入栈
                stack.push(i);
            }
            i++;
        }

        return answer;
    }

    public static void main(String[] args) {
        dailyTemperatures(new int[]{73,74,75,71,69,72,76,73});
    }
    /**
     * 暴力会超时
     * @param temperatures
     * @return
     */
    public int[] dailyTemperatures1(int[] temperatures) {
        Stack<Integer> stack = new Stack<>();
        int[] answer = new int[temperatures.length];
        for (int i = 0; i <= temperatures.length - 1; i++) {
            int j = i + 1;
            while (j <= temperatures.length - 1) {
                if (temperatures[j]>temperatures[i]){
                    answer[i]=j-i;
                    break;
                }
                j++;
            }
        }
        return answer;
    }
}
