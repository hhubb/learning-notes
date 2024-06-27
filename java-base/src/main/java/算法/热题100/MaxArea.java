package 算法.热题100;

/**
 * 11. 盛最多水的容器
 * https://leetcode.cn/problems/container-with-most-water/description/?envType=study-plan-v2&envId=top-100-liked
 *
 * @Author binbin
 * @Date 2024 06 25 10 03
 **/
public class MaxArea {
    public int maxArea(int[] height) {
        int i = 0;
        int j = height.length - 1;
        int maxArea = 0;
        while (i < j) {
            int x = j - i;
            int y = Math.min(height[i], height[j]);
            maxArea = Math.max(maxArea, x * y);
            if (height[i]<height[j]){
                i++;
            }else {
                j--;
            }
        }
        return maxArea;
    }
}
