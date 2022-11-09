package algorithm.recursion;

/**
 * 
 * 斐波那契数列（Fibonacci sequence）
 * 
 * ◼ 斐波那契数列：1、1、2、3、5、8、13、21、34、……
 *   F(1)=1，F(2)=1, F(n)=F(n-1)+F(n-2)（n≥3）
 * 
 * @author avril
 *
 */
public class Fibonacci {
	/** 对不断优化的版本进行测试 */
	public static void main(String[] args) {
		Fibonacci fib = new Fibonacci();
		System.out.println(fib.fib_ver0(10));
		System.out.println(fib.fib_ver1(10));
		System.out.println(fib.fib_ver2(10));
		System.out.println(fib.fib_ver3(10));
		System.out.println(fib.fib_ver4(10));
		System.out.println(fib.fib_ver5(10));
		System.out.println(fib.fib_ver6(10));
	}

	/**
	 * 最原始版本： 直接通过递归来实现
	 * 
	 * ◼ 根据递推式 T n = T n − 1 + T(n − 2) + O(1)，可得知时间复杂度：O 2^n
	 * ◼ 空间复杂度：O(n)
	 *   递归调用的空间复杂度 = 递归深度 * 每次调用所需的辅助空间
	 * @param n
	 */
	public int fib_ver0(int n) {
		if (n <= 2) return 1;
		return fib_ver0(n - 1) + fib_ver0(n - 2);
	}
	
	/**
	 * 优化版本1： 记忆法
	 * 
	 * ◼ 用数组存放计算过的结果，避免重复计算
	 * ◼ 时间复杂度：O(n)
	 * ◼ 空间复杂度：O(2n)
	 * @param n
	 */
	public int fib_ver1(int n) {
		if (n <= 2) return 1;
		// 这里故意让容量多一个 可以直接让数组的下标与n匹配 避免n-1操作 提升效率
		int[] array = new int[n + 1]; 
		/* 初始化 */
		array[2] = array[1] = 1;
		
		return fib_ver1(array, n);
	}
	
	private int fib_ver1(int[] array, int n) {
		if (array[n] == 0) { // 整型int数组 创建后如果不初始化的话 默认是0（也就是代表还没有被赋值
			array[n] = fib_ver1(array, n - 1) + fib_ver1(array, n - 2);
		}
		return array[n];
	}
	
	/**
	 * 优化版本2： 去除递归
	 * 
	 * ◼ 时间复杂度：O(n)
	 * ◼ 空间复杂度：O(n)
	 * @param n
	 */
	public int fib_ver2(int n) {
		if (n <= 2) return 1;
		int[] array = new int[n + 1];
		array[2] = array[1] = 1;
		/* 通过for循环替换掉递归 */
		for (int i = 3; i < n + 1; i++) { // 减少了栈空间的开辟（本质上ver1是O(2n)
			array[i] = array[i - 1] + array[i - 2];
		}
		
		return array[n];
	}
	
	/**
	 * 优化版本3： 动态数组
	 * 
	 * 由于每次运算只需要用到数组中的 2 个元素，所以可以使用滚动数组来优化
	 * ◼ 时间复杂度：O(n)，空间复杂度：O(1)
	 * @param n
	 */
	public int fib_ver3(int n) {
		if (n <= 2) return 1;
		/* 仔细思考斐波那契额数列 可以知道 其实每次运算只用到2个元素 */
		int[] array = new int[2];
		array[1] = array[0] = 1;
		
		for (int i = 3; i < n + 1; i++) {
			array[i % 2] = array[(i - 1) % 2] + array[(i - 2) % 2];
		}
		
		return array[n % 2];
	}
	
	/**
	 * 优化版本4： 位运算取代模运算
	 * 
	 * 乘、除、模运算效率较低，建议用其他方式取代
	 * %2 的可以发现这么个规律 就是按位与&上1
	 * 4 % 2 = 0	0b100 & 0b001 = 0
	 * 3 % 2 = 1	0b011 & 0b001 = 0
	 * 5 % 2 = 1	0b101 & 0b001 = 0
	 * 6 % 2 = 0	0b110 & 0b001 = 0
	 * 
	 * @param n
	 */
	public int fib_ver4(int n) {
		if (n <= 2) return 1;
		/* 仔细思考斐波那契额数列 可以知道 其实每次运算只用到2个元素 */
		int[] array = new int[2];
		array[1] = array[0] = 1;
		
		for (int i = 3; i < n + 1; i++) {
			array[i & 1] = array[(i - 1) & 1] + array[(i - 2) & 1];
		}
		
		return array[n & 1];
	}
	
	/**
	 * 优化版本5： 用普通变量代替数组
	 * 
	 * ◼ 时间复杂度：O(n)，空间复杂度：O(1)
	 * @param n
	 */
	public int fib_ver5(int n) {
		if (n <= 2) return 1;
		int first = 1;
		int second = 1;
		
		for (int i = 3; i < n + 1; i++) {
			second = first + second;
			first = second - first;
		}
		
		return second;
	}
	
	/**
	 * 最优解： 通过斐波那契数列的线性代数【特征方程】来求解
	 * 
	 * ◼ 时间复杂度、空间复杂度取决于 pow 函数（至少可以低至O(logn) ）
	 * @param n
	 */
	public int fib_ver6(int n) {
		double c = Math.sqrt(5);
		return (int) ((Math.pow((1 + c) / 2, n) - Math.pow((1 - c) / 2, n)) / c);
	}
}
