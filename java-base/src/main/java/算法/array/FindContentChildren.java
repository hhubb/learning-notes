package 算法.array;

import java.util.Arrays;

/**
 * @Author binbin
 * @Date 2025 02 21 15 25
 **/
public class FindContentChildren {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int count = 0;
        if (s.length == 0) {
            return count;
        }
        if (s[s.length - 1] < g[0]) {
            return count;
        }
        int i=0;
        int j=0;
        while (j<=s.length-1 && i<=g.length-1){
            if (s[j] >= g[i]) {
                i++;
                count += 1;
            }
            j++;
        }
        return count;

    }
    public int findContentChildren1(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int count = 0;
        if (s.length == 0) {
            return count;
        }
        if (s[s.length - 1] < g[0]) {
            return count;
        }
        for (int j = 0; j <= s.length - 1; j++) {
            for (int i = 0; i <= g.length - 1; i++) {
                if (s[j] == 0) {
                    continue;
                }
                if (s[j] >= g[i]) {
                    count += 1;
                    g[i] = s[s.length - 1] + 1;
                    s[j] = 0;
                }
            }
        }
        return count;

    }

}
