package algorithm.backTracking;

/**
 * N皇后问题
 * 
 * ◼ 八皇后问题是一个古老而著名的问题
 * 	在8x8格的国际象棋上摆放八个皇后，使其不能互相攻击：任意两个皇后都不能处于同一行、同一列、同一斜线上
 * 	请问有多少种摆法？
 * 
 * @author avril
 *
 */
public class Nqueens {
	/**
	 * 皇后所处的位置
	 * 数组索引号是行号，数组元素是列号
	 */
	int[] queens;
	
	/** 总共多少摆法 */
	int ways;
	
	/* 标记着某一列是否有皇后（优化版） */
	boolean[] cols;
	
	/* 标记着某一对角线是否有皇后了（左上角\） */
	boolean[] leftTop;
	
	/* 标记着某一对角线是否有皇后了（又上角/） */
	boolean[] rightTop;
	
	/* 总共多少摆法 */
	int newWays;
	
	/* 测试 */
	public static void main(String[] args) {
		Nqueens queen = new Nqueens();
		queen.placeQueens(8, false);
		queen.placeQueens(8, true);
	}

	/**
	 * 处理N皇后问题（原始版本）
	 * 
	 * @param n
	 */
	public void placeQueens(int n, boolean quickly) {
		if (n < 1) return;
		if (quickly) { // 优化版本
			cols = new boolean[n];
			// 斜边的数量等于2n - 1
			leftTop = new boolean[(n << 1) - 1];
			rightTop = new boolean[leftTop.length];
			place_ver1(0);
			System.out.println(n + "皇后一共有" + newWays + "种摆法");
		} else { // 普通版本
			queens = new int[n];
			place_ver0(0);
			System.out.println(n + "皇后一共有" + ways + "种摆法");
		}
		
	}
	
	/**
	 * 从第row行开始摆放
	 * @param row
	 */
	private void place_ver0(int row) {
		if (row == queens.length) { // 代表最后一行已经摆放完毕
			ways++;
			printWays();
			return;
		}
		
		/* 回溯起始发生在每次递归结束的时候 */
		for (int col = 0; col < queens.length; col++) {
			if (isValid(row, col)) { // isValid就相当于剪枝
				// 在第row行第col列摆放皇后
				queens[row] = col;
				place_ver0(row + 1);
			}
		}
	}
	
	/**
	 * 判断第row行第col列是否可以摆放皇后
	 * @param row
	 * @param col
	 */
	private boolean isValid(int row, int col) {
		for (int i = 0; i < row; i++) {
			// 第col列已经有皇后
			if (queens[i] == col) return false;
			// 第i行的皇后跟第row行第col列格子处在同一斜线上
			// 其实就是利用数学的斜率知识： 45度斜率 x坐标差和y的坐标差的比例是绝对值1
			if (row - i == Math.abs(col - queens[i])) return false;
		}
		
		return true;
	}
	
	/**
	 * 打印具体的摆法
	 * 
	 * 1代表皇后
	 * 0代表空格
	 */
	private void printWays() {
		for (int row = 0; row < queens.length; row++) {
			for (int col = 0; col < queens.length; col++) {
				if (queens[row] == col) {
					System.out.print("1 ");
				} else {
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
		System.out.println("------------");
	}
	
	/**
	 * 从第row行开始摆放
	 * 
	 * @param row
	 */
	private void place_ver1(int row) {
		if (row == cols.length) {
			newWays++;
			return;
		}
		
		for (int col = 0; col < cols.length; col++) {
			// 已经有皇后
			if (cols[col]) continue;
			int ltIndex = row - col + cols.length - 1;
			if (leftTop[ltIndex]) continue;
			int rtIndex = row + col;
			if (rightTop[rtIndex]) continue;
			
			// 摆放皇后
			cols[col] = true;
			leftTop[ltIndex] = true;
			rightTop[rtIndex] = true;
			place_ver1(row + 1);
			
			// 回溯重置
			cols[col] = false;
			leftTop[ltIndex] = false;
			rightTop[rtIndex] = false;
		}
	}
} 
