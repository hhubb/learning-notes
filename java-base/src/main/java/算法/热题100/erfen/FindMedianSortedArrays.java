package 算法.热题100.erfen;

/** https://leetcode.cn/problems/median-of-two-sorted-arrays/description/?envType=study-plan-v2&envId=top-100-liked
 * 4. 寻找两个正序数组的中位数
 * @Author binbin
 * @Date 2024 08 01 14 38
 **/
public class FindMedianSortedArrays {
    /**
     * 二分
     * 获取中位数就是获取长度为nums1.length + nums2.length的有序数组第k小的数
     * 如果是偶数就是找k和k+1小的数
     * 如果是奇数就是找第k小，k向下取整
     * todo  没看懂
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int m = nums2.length;
        //因为数组是从索引0开始的，因此我们在这里必须+1，即索引(k+1)的数，才是第k个数。
        int left = (n + m + 1) / 2;
        int right = (n + m + 2) / 2;
        //将偶数和奇数的情况合并，如果是奇数，会求两次同样的 k
        return (getKth(nums1, 0, n - 1, nums2, 0, m - 1, left) + getKth(nums1, 0, n - 1, nums2, 0, m - 1, right)) * 0.5;
    }
    private int getKth(int[] nums1, int start1, int end1, int[] nums2, int start2, int end2, int k) {
        //因为索引和算数不同6-0=6，但是是有7个数的，因为end初始就是数组长度-1构成的。
        //最后len代表当前数组(也可能是经过递归排除后的数组)，符合当前条件的元素的个数
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;
        //让 len1 的长度小于 len2，这样就能保证如果有数组空了，一定是 len1
        //就是如果len1长度小于len2，把getKth()中参数互换位置，即原来的len2就变成了len1，即len1，永远比len2小
        if (len1 > len2) return getKth(nums2, start2, end2, nums1, start1, end1, k);
        //如果一个数组中没有了元素，那么即从剩余数组nums2的其实start2开始加k再-1.
        //因为k代表个数，而不是索引，那么从nums2后再找k个数，那个就是start2 + k-1索引处就行了。因为还包含nums2[start2]也是一个数。因为它在上次迭代时并没有被排除
        if (len1 == 0) return nums2[start2 + k - 1];

        //如果k=1，表明最接近中位数了，即两个数组中start索引处，谁的值小，中位数就是谁(start索引之前表示经过迭代已经被排出的不合格的元素，即数组没被抛弃的逻辑上的范围是nums[start]--->nums[end])。
        if (k == 1) return Math.min(nums1[start1], nums2[start2]);

        //为了防止数组长度小于 k/2,每次比较都会从当前数组所生长度和k/2作比较，取其中的小的(如果取大的，数组就会越界)
        //然后素组如果len1小于k / 2，表示数组经过下一次遍历就会到末尾，然后后面就会在那个剩余的数组中寻找中位数
        int i = start1 + Math.min(len1, k / 2) - 1;
        int j = start2 + Math.min(len2, k / 2) - 1;

        //如果nums1[i] > nums2[j]，表示nums2数组中包含j索引，之前的元素，逻辑上全部淘汰，即下次从J+1开始。
        //而k则变为k - (j - start2 + 1)，即减去逻辑上排出的元素的个数(要加1，因为索引相减，相对于实际排除的时要少一个的)
        if (nums1[i] > nums2[j]) {
            return getKth(nums1, start1, end1, nums2, j + 1, end2, k - (j - start2 + 1));
        }
        else {
            return getKth(nums1, i + 1, end1, nums2, start2, end2, k - (i - start1 + 1));
        }
    }

    public static double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        int i = 0;
        int j = 0;
        int mid = (nums1.length + nums2.length - 1) / 2;
        int flag = (nums1.length + nums2.length) % 2;//1是奇数，0是偶数
//        if (flag == 1) { //1是奇数，0是偶数
//            mid = mid + 1;
//        }
        int res = Integer.MIN_VALUE;
        int respre = 0; //偶数需要
        while (i <= nums1.length - 1 && j <= nums2.length - 1) {
            if (nums1[i] < nums2[j]) {
                res = nums1[i];
                i++;

            } else {
                res = nums2[j];
                j++;

            }
            if (mid == -1) {
                return (Double.valueOf(res + respre)) / 2;
            }
            if (mid == 0) {
                if (flag == 1) {
                    return Double.valueOf(res);
                }
                respre = res;
            }
            mid--;

        }
        while (i <= nums1.length - 1) {
            res = nums1[i];
            if (mid == -1) {
                return (Double.valueOf(res + respre)) / 2;
            }
            if (mid == 0) {
                if (flag == 1) {
                    return Double.valueOf(res);
                }
                respre = res;
            }
            mid--;
            i++;
        }
        while (j <= nums2.length - 1) {
            res = nums2[j];
            if (mid == -1) {
                return (Double.valueOf(res + respre)) / 2;
            }
            if (mid == 0) {
                if (flag == 1) {
                    return Double.valueOf(res);
                }
                respre = res;
            }
            mid--;
            j++;

        }

        return res;
    }

    public static double findMedianSortedArrays1(int[] nums1, int[] nums2) {

        int i = 0;
        int j = 0;
        int[] newNums = new int[nums1.length + nums2.length];
        int k = 0;
        while (i <= nums1.length - 1 && j <= nums2.length - 1) {
            if (nums1[i] < nums2[j]) {
                newNums[k] = nums1[i];
                i++;
            } else {
                newNums[k] = nums2[j];
                j++;
            }
            k++;
        }
        while (i <= nums1.length - 1) {
            newNums[k] = nums1[i];
            i++;
            k++;
        }
        while (j <= nums2.length - 1) {
            newNums[k] = nums2[j];
            j++;
            k++;
        }
        int flag = newNums.length % 2;
        if (flag == 1) { //1是奇数，0是偶数
            return newNums[(newNums.length - 1) / 2];
        } else {
            return (Double.valueOf(newNums[(newNums.length - 1) / 2] + newNums[(newNums.length - 1) / 2 + 1])) / 2;
        }
    }


}
