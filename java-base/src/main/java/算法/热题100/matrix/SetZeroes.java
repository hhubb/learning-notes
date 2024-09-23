package 算法.热题100.matrix;

/**https://leetcode.cn/problems/set-matrix-zeroes/description/?envType=study-plan-v2&envId=top-100-liked
 * 73. 矩阵置零
 * @Author binbin
 * @Date 2024 09 23 10 16
 **/
public class SetZeroes {
    public void setZeroes(int[][] matrix) {
        int rowSize=matrix.length;
        int colSize=matrix[0].length;
        int [] row=new int[rowSize];
        int [] col=new  int[colSize];
        for (int i=0;i<=rowSize-1;i++){
            for (int j=0;j<=colSize-1;j++){
                if (matrix[i][j]==0){
                    row[i]=1;
                    col[j]=1;
                }
            }
        }
        for (int i=0;i<=rowSize-1;i++){
            for (int j=0;j<=colSize-1;j++){
                if (row[i]==1 || col[j]==1){
                    matrix[i][j]=0;
                }
            }
        }

    }
}
