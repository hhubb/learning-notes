package 算法.热题100.stack;

import java.util.Arrays;
import java.util.Stack;

/**
 * https://leetcode.cn/problems/largest-rectangle-in-histogram/description/?envType=study-plan-v2&envId=top-100-liked
 * 84. 柱状图中最大的矩形
 *
 * @Author binbin
 * @Date 2024 08 07 13 42
 **/
public class LargestRectangleArea {
    /**
     * 单调栈
     * 
     */
    /**
     * 哨兵概念+栈
     * 认为原来的数组前后都有一个高度为0的圆柱
     * 这样就不需要最后判断栈是否为空了
     * @param heights
     * @return
     */
    public static int largestRectangleArea(int[] heights) {

        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();
        //增加左右高度为0的哨兵
        int[] newHeights= new int[heights.length+2];
        for (int i = 0; i <= heights.length - 1; i++) {
            newHeights[i+1]=heights[i];
        }

        for (int i = 0; i <= newHeights.length - 1; i++) {
            //严格小于栈顶才出栈
            while (!stack.empty() && newHeights[i] < newHeights[stack.peek()]) {
                Integer topIndex = stack.pop();
                //如果出栈后栈空了，说明向左可以延伸到0,如果不为空，底长为当前index-新栈顶index-1
                int l=i-stack.peek()-1;
                maxArea = Math.max(maxArea, (l) * newHeights[topIndex]);
            }
            stack.push(i);

        }
        return maxArea;
    }

    public static void main(String[] args) {
        largestRectangleArea(new int[]{4,2,0,3,2,5});
    }
    /**
     * 使用栈解决
     * @param heights
     * @return
     */
    public static int largestRectangleArea2(int[] heights) {
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i <= heights.length - 1; i++) {
            //严格小于栈顶才出栈
            while (!stack.empty() && heights[i] < heights[stack.peek()]) {
                Integer topIndex = stack.pop();
                //如果出栈后栈空了，说明向左可以延伸到0,如果不为空，底长为当前index-新栈顶index-1
                int l=stack.empty()?i:i-stack.peek()-1;
                maxArea = Math.max(maxArea, (l) * heights[topIndex]);
            }
            if (stack.empty() || heights[i] >= heights[stack.peek()]) {
                //严格大于栈顶才入栈
                stack.push(i);
            }

        }
        if (stack.empty()) {
            return maxArea;
        }
        //如果栈不为空，可以虚拟一个右边有一个高度为0的柱子
        int endIndex = heights.length;
        while (!stack.empty()) {
            Integer topIndex = stack.pop();
            int l=stack.empty()?endIndex:endIndex-stack.peek()-1;
            maxArea = Math.max(maxArea, l * heights[topIndex]);
        }
        return maxArea;
    }



    /**
     * 暴力
     * 高度不变，左右扩展，直到遇到比自己高度小的，说明不能继续扩展。
     *
     * @param heights
     * @return
     */
    public int largestRectangleArea1(int[] heights) {
        int maxArea = 0;

        for (int i = 0; i <= heights.length - 1; i++) {
            int left = i;
            int right = i;
            while (left >= 0 && heights[left] >= heights[i]) {
                left--;
            }
            left++;
            while (right <= heights.length - 1 && heights[right] >= heights[i]) {
                right++;
            }
            right--;
            maxArea = Math.max(maxArea, (right - left + 1) * heights[i]);
        }
        return maxArea;
    }
}
