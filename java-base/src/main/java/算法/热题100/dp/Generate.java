package 算法.热题100.dp;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/pascals-triangle/description/?envType=study-plan-v2&envId=top-100-liked
 * 118. 杨辉三角
 *
 * @Author binbin
 * @Date 2024 09 03 10 31
 **/
public class Generate {

    /**
     * 动态规划
     *
     * @param numRows
     * @return
     */
    public static List<List<Integer>> generate1(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 1; j <= i; j++) {
                if (j == 1 || j == i) {
                    list.add(1);
                } else {
                    //i-1 ,j-1,j 标识上一行的上一元素和同一位置的元素，再减去1是因为index是从0开始
                    list.add(res.get((i - 1) - 1).get((j - 1) - 1) + res.get((i - 1) - 1).get((j) - 1));
                }
            }
            res.add(list);
        }
        return res;
    }

    public static void main(String[] args) {
        generate(5);
    }

}
