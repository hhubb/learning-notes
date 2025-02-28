package 算法.array;

/**
 * https://leetcode.cn/problems/zigzag-conversion/description/
 * 6. Z 字形变换
 *
 * @Author binbin
 * @Date 2025 02 27 11 06
 **/
public class Convert {
    /**
     * 找规律
     * n=3
     * 0 4              、、 4  maxInterval
     * 1 3 5 7 9 11 13  、、 2  maxInterval - i * 2
     * 2 6 10           、、 4  maxInterval
     * <p>
     * n=4
     * 0 6 12            、、 6    maxInterval
     * 1 5 7 11 13       、、 4 2  maxInterval - i * 2 | maxInterval-(maxInterval - i * 2)
     * 2 4 8 10          、、 2 4
     * 3 9               、、 6
     * <p>
     * n=4
     * 0 8                、、 8   maxInterval
     * 1 7  9  15 17      、、 6 2 maxInterval - i * 2 | maxInterval-(maxInterval - i * 2)
     * 2 6  10 14         、、 4
     * 3 5  11            、、 2 6
     * 4 12               、、 8
     * <p>
     * n=5
     * 0 10 20            、、 10   maxInterval
     * 1 9  11 19         、、 8 2  maxInterval - i * 2 | maxInterval-(maxInterval - i * 2)
     * 2 8  12 18         、、 6 4
     * 3 7  13 17         、、 4 6
     * 4 6  14 16         、、 2 8
     * 5 15               、、 10
     *
     * @param s
     * @param numRows
     * @return
     */
    public static String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }
        StringBuilder sr = new StringBuilder();
        int maxInterval = 2 * numRows - 2;
        for (int i = 0; i <= numRows - 1; i++) {
            int l = i;
            int interval = maxInterval - i * 2;
            if (interval == 0) {
                interval = maxInterval;
            }
            while (l <= s.length() - 1) {
                char c = s.charAt(l);
                sr.append(c);
                l = l + interval;
                if (maxInterval - interval != 0) {
                    interval = maxInterval - interval;
                }

            }
        }
        return sr.toString();
    }

    public static void main(String[] args) {
        convert("PAYPALISHIRING", 4);
    }
}
