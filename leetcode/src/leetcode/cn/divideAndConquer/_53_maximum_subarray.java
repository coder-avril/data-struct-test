package leetcode.cn.divideAndConquer;

/**
 * 53. 最大连续子序列和
 * 
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和
 * 子数组 是数组中的一个连续部分
 * 
 * @author avril
 *
 */
public class _53_maximum_subarray {
	/* 测试 */
	public static void main(String[] args) {
		int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
		System.out.println(maxSubarray_ver2(nums));
	}
	
	/**
	 * 解法1 – 暴力出奇迹
	 * ◼ 穷举出所有可能的连续子序列，并计算出它们的和，最后取它们中的最大值
	 * ◼ 空间复杂度：O(1)，时间复杂度：O(n3)
	 * 
	 * @param nums
	 */
	static int maxSubarray_ver0(int[] nums) {
		if (nums == null || nums.length ==0) return 0;
		int max = Integer.MIN_VALUE;
		
		for (int begin = 0; begin < nums.length; begin++) {
			for (int end = begin; end < nums.length; end++) {
				// sum是[begin, end]的和
				int sum = 0;
				for (int i = begin; i <= end; i++) {
					sum += nums[i];
				}
				max = Math.max(max, sum);
			}
		}
		
		return max;
	}
	
	/**
	 * 解法1 – 暴力出奇迹 – 优化
	 * ◼ 重复利用前面计算过的结果
	 * ◼ 空间复杂度：O(1)，时间复杂度：O(n2)
	 * 
	 * @param nums
	 */
	static int maxSubarray_ver1(int[] nums) {
		if (nums == null || nums.length ==0) return 0;
		int max = Integer.MIN_VALUE;
		
		for (int begin = 0; begin < nums.length; begin++) {
			int sum = 0;
			for (int end = begin; end < nums.length; end++) {				
				sum += nums[end];
				max = Math.max(max, sum);
			}
		}
		
		return max;
	}
	
	/**
	 * 解法2 – 分治
	 * ◼ 空间复杂度：O(logn)，时间复杂度：O(nlogn)
	 * 
	 * @param nums
	 */
	static int maxSubarray_ver2(int[] nums) {
		if (nums == null || nums.length == 0) return 0;
		return maxSubarray_ver2(nums, 0, nums.length);
	}
	
	static int maxSubarray_ver2(int[] nums, int begin, int end) {
		if (end - begin < 2) return nums[begin];
		
		int mid = (begin + end) >> 1;
		int leftMax = nums[mid - 1];
		int leftSum = leftMax;
		for (int i = mid - 2; i >= begin; i--) {
			leftSum += nums[i];
			leftMax = Math.max(leftMax, leftSum);
		}
		
		int rightMax = nums[mid];
		int rightSum = rightMax;
		for (int i = mid + 1; i < end; i++) {
			rightSum += nums[i];
			rightMax = Math.max(rightMax, rightSum);
		}
		
		return Math.max(leftMax + rightMax, 
				Math.max(maxSubarray_ver2(nums, begin, mid), maxSubarray_ver2(nums, mid, end)));
	}
}
