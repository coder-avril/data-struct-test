package algorithm.recursion;

/**
 * 汉诺塔
 * ◼ 编程实现把 A 的 n 个盘子移动到 C（盘子编号是 [1, n] ）
 * 	每次只能移动1个盘子
 * 	大盘子只能放在小盘子下面
 * 
 * @author avril
 *
 */
public class Hanoi {
	/* 测试 */
	public static void main(String[] args) {
		new Hanoi().hanoi(3, "A", "B", "C");
	}

	/**
	 * 将n个盘子从p1挪动到p3
	 * ◼ T(n) = 2 ∗ T(n − 1) + O(1)	因此时间复杂度是：O(2^n)
	 * ◼ 空间复杂度：O(n)
	 * 
	 * @param n 盘子的数量
	 * @param p1 起始柱子
	 * @param p2 中间柱子
	 * @param p3 目标柱子
	 */
	public void hanoi(int n, String p1, String p2, String p3) {
		if (n == 1) { // 当 n == 1时，直接将盘子从 A 移动到 C
			move(n, p1, p3);
			return;
		}
		
		/* 步骤 1和3 明显是个递归调用 */
		hanoi(n - 1, p1, p3, p2); // 将 n – 1 个盘子从 A 移动到 B
		move(n, p1, p3); // 将编号为 n 的盘子从 A 移动到 C
		hanoi(n - 1, p2, p1, p3); // 将 n – 1 个盘子从 B 移动到 C
	}
	
	/**
	 * 将n号盘子从from移动到to
	 * 
	 * @param no 第几号盘子
	 * @param from 起始柱子
	 * @param to 目标柱子
	 */
	private void move(int no, String from, String to) {
		System.out.println("将" + no + "号盘子从" + from + "移动到" + to);
	}
}
