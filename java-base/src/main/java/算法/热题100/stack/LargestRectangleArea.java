package 算法.热题100.stack;

import java.util.Stack;

/**https://leetcode.cn/problems/largest-rectangle-in-histogram/description/?envType=study-plan-v2&envId=top-100-liked
 * 84. 柱状图中最大的矩形
 * @Author binbin
 * @Date 2024 08 07 13 42
 **/
public class LargestRectangleArea {
//    public int largestRectangleArea(int[] heights) {
//        int maxArea=heights[0];
//        Stack<Integer> stack=new Stack<>();
//        for (int i=0;i<=heights.length-1;i++){
//            if (heights[stack.peek()]<=heights[i]){
//               int topIndex= stack.pop();
//                maxArea=i-(topIndex-1)*heights[stack.peek()];
//                stack.push(heights[i]);
//            }
//        }
//    }

    /**
     * 暴力
     * @param heights
     * @return
     */
    public int largestRectangleArea(int[] heights) {
        int maxArea=0;

        for (int i=0;i<=heights.length-1;i++){
            int left=i;
            int right=i;
           while (left>=0 && heights[left]>=heights[i]){
               left--;
           }
            left++;
            while (right<=heights.length-1 && heights[right]>=heights[i]){
                right++;
            }
            right--;
            maxArea=Math.max(maxArea,(right-left+1)*heights[i]);
        }
        return maxArea;
    }
}
