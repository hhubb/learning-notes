package 算法.array;

/**
 * @Author binbin
 * @Date 2025 02 25 11 17
 **/
public class IslandPerimeter {
    public int islandPerimeter(int[][] grid) {
        int l = 0;
        for (int i = 0; i <= grid.length - 1; i++) {
            for (int j = 0; j <= grid[0].length - 1; j++) {
                if (grid[i][j] == 1) {
                    if (i == 0) {
                        l++;
                    }
                    if (i == grid.length - 1) {
                        l++;
                    }
                    if (i != 0 && grid[i - 1][j] == 0) {
                        l++;
                    }
                    if (i != grid.length - 1 && grid[i + 1][j] == 0) {
                        l++;
                    }
                    if (j == 0) {
                        l++;
                    }
                    if (j == grid[0].length - 1) {
                        l++;
                    }
                    if (j != 0 && grid[i][j - 1] == 0) {
                        l++;
                    }
                    if (j != grid[0].length - 1 && grid[i][j + 1] == 0) {
                        l++;
                    }

                }
            }
        }
        return l;
    }
}
