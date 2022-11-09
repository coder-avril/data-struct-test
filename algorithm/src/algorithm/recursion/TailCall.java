package algorithm.recursion;

/**
 * TailCall 尾调用（一个函数的最后一个动作是调用函数
 * 
 * 如果最后一个动作是调用自身，称为尾递归（Tail Recursion），是尾调用的特殊情况
 * ◼ 一些编译器能对尾调用进行优化，以达到节省栈空间的目的
 * 
 * @author avril
 *
 */
public class TailCall {
	/* 测试 */
	public static void main(String[] args) {
		TailCall tc = new TailCall();
		System.out.println(tc.factorial(3));
		System.out.println(tc.factorial_nonRe(3));
		System.out.println(tc.fibonacci(10));
		System.out.println(tc.fibonacci_nonRe(10));
	}

	/**
	 * 示例1 – 阶乘
	 * 
	 * 求 n 的阶乘 1*2*3*...*(n-1)*n （n>0）
	 * 普通的递归写法
	 * 
	 * @param n
	 */
	public int factorial(int n) {
		if (n <= 1) return n;
		return n * factorial(n - 1);
	}
	
	/**
	 * 阶乘的尾递归写法
	 * 
	 * @param n
	 */
	public int factorial_nonRe(int n) {
		return factorial_nonRe(n, 1);
	}
	
	/**
	 * 尾递归
	 * 
	 * @param n
	 * @param result 从大到小的累乘结果
	 */
	private int factorial_nonRe(int n, int result) {
		if (n <= 1) return result;
		return factorial_nonRe(n - 1, n * result);
	}
	
	/**
	 * 示例2 – 斐波那契数列
	 * 普通的递归写法
	 * 
	 * @param n
	 */
	public int fibonacci(int n) {
		if (n <= 2) return 1;
		return fibonacci(n - 1) + fibonacci(n - 2);
	}
	
	/**
	 * 斐波那契数列的尾递归写法
	 * 
	 * @param n
	 */
	public int fibonacci_nonRe(int n) {
		return fibonacci_nonRe(n, 1, 1);
	}
	
	private int fibonacci_nonRe(int n, int first, int second) {
		if (n <= 1) return first;
		return fibonacci_nonRe(n - 1, second, first + second);
	}
}
