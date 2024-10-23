package 算法.热题100.matrix;

import java.util.ArrayList;
import java.util.List;

/**https://leetcode.cn/problems/spiral-matrix/description/?envType=study-plan-v2&envId=top-100-liked
 * 54. 螺旋矩阵
 * @Author binbin
 * @Date 2024 09 24 10 27
 **/
public class SpiralOrder {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> order = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return order;
        }
        //获取边界
        int rows = matrix.length, cols = matrix[0].length;
        //标记已经记录过的值的位置
        boolean[][] visited = new boolean[rows][cols];
        //获取元素总数
        int total = rows * cols;
        //
        int rowIndex = 0;
        int colIndex = 0;
        //方向索引，{0, 1}：行从左向右→； {1, 0}：列从上往下↓ ；{0, -1}：行从右向左←； {-1, 0}：列从下往上↑
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIndex = 0;
        for (int i = 0; i < total; i++) {
            order.add(matrix[rowIndex][colIndex]);
            //标记已记录的坐标
            visited[rowIndex][colIndex] = true;
            //获取下一个行和列
            int nextRow = rowIndex + directions[directionIndex][0];
            int nextCol = colIndex + directions[directionIndex][1];
            //nextRow < 0表示 {-1, 0}：列从下往上↑已经到了第一行，所以需要改变方向
            //nextRow >= rows 表示 {1, 0}：列从上往下↓已经到了最后一行，所以需要改变方向
            //nextCol < 0 表示{0, -1}：行从右向左←已经到了第一列，所以需要改变方向
            //nextCol >= cols 表示{0, 1}：行从左向右→已经到了最后一列，所以需要改变方向
            //visited[nextRow][nextCol]表示元素重复，所以需要改变方向
            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols || visited[nextRow][nextCol]) {
                directionIndex = (directionIndex + 1) % 4;
            }
            //改变方向后再次获取下一次需要记录的rowIndex和colIndex
            rowIndex += directions[directionIndex][0];
            colIndex += directions[directionIndex][1];
        }
        return order;
    }
}
