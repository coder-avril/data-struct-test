package algorithm.recursion;

import java.util.Stack;

/**
 * 递归转非递归
 * ◼ 若递归调用深度较大，会占用比较多的栈空间，甚至会导致栈溢出
 * ◼ 在有些时候，递归会存在大量的重复计算，性能非常差
 * 
 * @author avril
 *
 */
public class ConvertToNonRecursive {
	/* 测试 */
	public static void main(String[] args) {
		ConvertToNonRecursive nonRe = new ConvertToNonRecursive();
		nonRe.makeNum(10);
		nonRe.makeNum_ver1(10);
		nonRe.makeNum_ver2(10);
	}

	/**
	 * 递归函数（不要纠结于它有啥含义 知道它是个递归就可以了
	 * @param n
	 */
	public void makeNum(int n) {
		if (n < 1) return;
		makeNum(n - 1);
		int v = n + 10;
		System.out.println(v);
	}
	
	/**
	 * 方法1： 在某些时候，也可以重复使用一组相同的变量来保存每个栈帧的内容
	 * @param n
	 */
	public void makeNum_ver1(int n) {
		for (int i = 1; i < n + 1; i++) {
			System.out.println(i + 10);
		}
	}
	
	/** 方法2： 万金油方法 */
	public void makeNum_ver2(int n) {
		Stack<Frame> frames = new Stack<>();
		while (n > 0) {
			frames.push(new Frame(n, n + 10));
			n--;
		}
		
		while (!frames.isEmpty()) {
			Frame frame = frames.pop();
			System.out.println(frame.v);
		}
	}
	
	/**
	 * 递归100%可以转换成非递归的理由
	 * 	递归转非递归的万能方法： 自己维护一个栈，来保存参数、局部变量
	 *	注意： 但是空间复杂度依然没有得到优化
	 */
	@SuppressWarnings("unused")
	private static class Frame {
		int n;
		int v;
		Frame(int n, int v) {
			this.n = n;
			this.v = v;
		}
	}
}
