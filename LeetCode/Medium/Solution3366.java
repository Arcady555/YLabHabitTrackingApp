package Medium;

import java.util.Arrays;

/**
 * Minimum Array Sum
 * <p>
 * You are given an integer array nums and three integers k, op1, and op2.
 * <p>
 * You can perform the following operations on nums:
 * <p>
 * Operation 1: Choose an index i and divide nums[i] by 2, rounding up to the nearest whole number. You can perform this operation at most op1 times, and not more than once per index.
 * Operation 2: Choose an index i and subtract k from nums[i], but only if nums[i] is greater than or equal to k. You can perform this operation at most op2 times, and not more than once per index.
 * Note: Both operations can be applied to the same index, but at most once each.
 * <p>
 * Return the minimum possible sum of all elements in nums after performing any number of operations.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [2,8,3,19,3], k = 3, op1 = 1, op2 = 1
 * <p>
 * Output: 23
 * <p>
 * Explanation:
 * <p>
 * Apply Operation 2 to nums[1] = 8, making nums[1] = 5.
 * Apply Operation 1 to nums[3] = 19, making nums[3] = 10.
 * The resulting array becomes [2, 5, 3, 10, 3], which has the minimum possible sum of 23 after applying the operations.
 * Example 2:
 * <p>
 * Input: nums = [2,4,3], k = 3, op1 = 2, op2 = 1
 * <p>
 * Output: 3
 * <p>
 * Explanation:
 * <p>
 * Apply Operation 1 to nums[0] = 2, making nums[0] = 1.
 * Apply Operation 1 to nums[1] = 4, making nums[1] = 2.
 * Apply Operation 2 to nums[2] = 3, making nums[2] = 0.
 * The resulting array becomes [1, 2, 0], which has the minimum possible sum of 3 after applying the operations.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 105
 * 0 <= k <= 105
 * 0 <= op1, op2 <= nums.length
 */
public class Solution3366 {

    public int minArraySum(int[] nums, int k, int op1, int op2) {
        Arrays.sort(nums);
        int[] numsOp1 = new int[nums.length];
        int[] numsOp2 = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            numsOp1[i] = Math.round((float) nums[i] / 2);
            numsOp2[i] = nums[i] - k >= 0 ? nums[i] - k : -1;
        }

        int countBefore = 0;
        int countAfter = 0;
        for (int i = 0; i < numsOp2.length; i++) {
            if (numsOp2[i] == -1) {
                countBefore = i;
            } else if (numsOp2[i] != -1 && op2 > 0) {
                if (numsOp2[i] < numsOp1[i]) {
                    nums[i] = numsOp2[i];
                    op2--;
                } else {
                    if (op1 > 0) {
                        nums[i] = numsOp1[i];
                        op1--;
                    }
                }
            } else {
                countAfter = i;
                break;
            }
        }

        for (int i = numsOp1.length - 1; i >= 0; i--) {
            if (i < countAfter && i > countBefore) {
                continue;
            } else {
                if (op1 > 0) {
                    nums[i] = numsOp1[i];
                    op1--;
                }
            }
        }
        return Arrays.stream(nums).sum();
    }
}