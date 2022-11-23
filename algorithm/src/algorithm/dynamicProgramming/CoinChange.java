package algorithm.dynamicProgramming;

/**
 * 找硬币
 * 
 * 动态规划（Dynamic Programming）
 * ◼ 动态规划，简称DP 是求解最优化问题的一种常用策略
 * ◼ 通常的使用套路（一步一步优化）
 * 	暴力递归（自顶向下，出现了重叠子问题）
 * 	记忆化搜索（自顶向下）
 * 	递推（自底向上）
 * 
 * @author avril
 *
 */
public class CoinChange {
	/* 测试 */
	public static void main(String[] args) {
		// 总额为41分最少需要几枚硬币？
		System.out.println(coinChange_ver1(41)); // 3
		System.out.println(coinChange_ver2(41)); // 3
		System.out.println(coinChange_ver3(41)); // 3
		System.out.println(coinChange_ver4(41)); // 3
	}

	/**
	 * 暴力递归（自顶向下，出现了重叠子问题）
	 * ◼ 类似于斐波那契数列的递归版，会有大量的重复计算，时间复杂度较高
	 * 
	 * @param n 金钱总额
	 */
	static int coinChange_ver1(int n) {
		if (n < 1) return Integer.MAX_VALUE;
		/* 存在面值为25、20、5、1的硬币 */
		if (n == 25 || n == 20 || n == 5 || n == 1) return 1;
		
		/**
		 * ◼ 假设 dp(n) 是凑到 n 分需要的最少硬币个数
		 *	如果第 1 次选择了 25 分的硬币，那么 dp(n) = dp(n – 25) + 1
		 *	如果第 1 次选择了 20 分的硬币，那么 dp(n) = dp(n – 20) + 1
		 *	如果第 1 次选择了 5 分的硬币，那么 dp(n) = dp(n – 5) + 1
		 *	如果第 1 次选择了 1 分的硬币，那么 dp(n) = dp(n – 1) + 1
		 */
		int min1 = Math.min(coinChange_ver1(n - 25), coinChange_ver1(n - 20));
		int min2 = Math.min(coinChange_ver1(n - 5), coinChange_ver1(n - 1));
		
		return Math.min(min1, min2) + 1;
	}
	
	/**
	 * 记忆化搜索（自顶向下）
	 * 
	 * @param n 金钱总额
	 */
	static int coinChange_ver2(int n) {
		if (n < 1) return -1;
		int[] dp = new int[n + 1];
		
		int[] faces = {1, 5, 20, 25};
		for (int face: faces) {
			if (n < face) break;
			dp[face] = 1;
		}
		
		return coinChange_ver2(n, dp);
	}
	
	static int coinChange_ver2(int n, int[] dp) {
		if (n < 1) return Integer.MAX_VALUE;
		if (dp[n] == 0) {
			int min1 = Math.min(coinChange_ver2(n - 25, dp), coinChange_ver2(n - 20, dp));
			int min2 = Math.min(coinChange_ver2(n - 5, dp), coinChange_ver2(n - 1, dp));
			dp[n] = Math.min(min1, min2) + 1;
		}
		return dp[n];
	}
	
	/**
	 * 递推（自底向上）
	 * 
	 * ◼ 时间复杂度、空间复杂度：O(n)
	 * @param n
	 */
	static int coinChange_ver3(int n) {
		if (n < 1) return -1;
		int[] dp = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			int min = dp[i - 1];
			if (i >= 5) min = Math.min(dp[i - 5], min);
			if (i >= 20) min = Math.min(dp[i - 20], min);
			if (i >= 25) min = Math.min(dp[i - 25], min);
			
			dp[i] = min + 1;
		}
		return dp[n];
	}
	
	/**
	 * 递推（自底向上） + 显示出具体的方案细节
	 * 
	 * @param n
	 */
	static int coinChange_ver4(int n) {
		if (n < 1) return -1;
		int[] dp = new int[n + 1];
		// faces[i]是凑够i分时最后那枚硬币的面值
		int[] faces = new int[dp.length];
		for (int i = 1; i <= n; i++) {
			int min = Integer.MAX_VALUE;
			if (i >= 1 && dp[i - 1] < min) {
				min = dp[i - 1];
				faces[i] = 1;
			}
			if (i >= 5 && dp[i - 5] < min) {
				min = dp[i - 5];
				faces[i] = 5;
			}
			if (i >= 20 && dp[i - 20] < min) {
				min = dp[i - 20];
				faces[i] = 20;
			}
			if (i >= 25 && dp[i - 25] < min) {
				min = dp[i - 25];
				faces[i] = 25;
			}
			dp[i] = min + 1;
			print(faces, i);
		}
		return dp[n];
	}
	
	static void print(int[] faces, int n) {
		System.out.print("[" + n + "]");
		while (n > 0) {
			System.out.print(faces[n] + " ");
			n -= faces[n];
		}
		System.out.println();
	}
}
