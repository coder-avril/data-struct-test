package algorithm.recursion;

/**
 * 上楼梯
 * 
 * 楼梯有 n 阶台阶，上楼可以一步上 1 阶，也可以一步上 2 阶，走完 n 阶台阶共有多少种不同的走法
 * 
 * @author avril
 *
 */
public class ClimbStairs {
	/** 对不断优化的版本进行测试 */
	public static void main(String[] args) {
		ClimbStairs climb = new ClimbStairs();
		System.out.println(climb.climb_v0(5));
		System.out.println(climb.climb_v1(5));
	}
	
	/**
	 * 假设 n 阶台阶有 f(n) 种走法，第 1 步有 2 种走法
	 * 	✓ 如果上 1 阶，那就还剩 n – 1 阶，共 f(n – 1) 种走法
	 * 	✓ 如果上 2 阶，那就还剩 n – 2 阶，共 f(n – 2) 种走法
	 * 	所以 f(n) = f(n – 1) + f(n – 2)
	 * 
	 * @param n
	 */
	public int climb_v0(int n) {
		if (n <= 2) return n;
		return climb_v0(n - 1) + climb_v0(n - 2);
	}

	/**
	 * 跟斐波那契数列几乎一样，因此优化思路也是一致的
	 * 
	 * @param n
	 */
	public int climb_v1(int n) {
		if (n <= 2) return n;
		
		int first = 1;
		int second = 2;
		for (int i = 3; i < n + 1; i++) {
			second = first + second;
			first = second - first;
		}
		return second;
	}
}
