package algorithm.sort.shell;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import algorithm.sort.Sort;

/**
 * ShellSort 希尔排序 [不稳定排序]
 * 
 * 把序列看作是一个矩阵，分成 𝑚 列，逐列进行排序
 *   𝑚 从某个整数逐渐减为
 *   当 𝑚 为1时，整个序列将完全有序
 *   
 * @author avril
 *
 * @param <E>
 */
public class ShellSort<E extends Comparable<E>> extends Sort<E> {
	@Override
	protected void sort() {
		List<Integer> stepSequence = shellStepSequence();
		for (Integer step: stepSequence) {
			sort(step);
		}
	}
	
	/*
	 * 分成step列进行排序
	 * @param step
	 */
	private void sort(int step) {
		// col: 第几列 column的简称
		for (int col = 0; col < step; col++) { // 对第几列进行排序
			/* 为了方便理解希尔排序 这里使用最原始的插入算法 以免注意力被分散 */
			/** 假设元素在第 col 列、第 row 行，步长（总列数）是 step， 那么这个元素在数组中的索引是 col + row * step */
			// col、col+step、col+2*step、col+3*step ...
			for (int begin = col + step; begin < array.length; begin += step) {
				int cur = begin;
				while (cur > col && cmp(cur, cur - step) < 0) {
					swap(cur, cur - step);
					cur -= step;
				}
			}
		}
	}
	
	/*
	 * 生成希尔本人推荐的步长序列
	 * @return stepSequence
	 */
	private List<Integer> shellStepSequence() {
		List<Integer> stepSequence = new ArrayList<>();
		int step = array.length;
		// 要保证放进去的步长从大到小
		while ((step >>= 1) > 0) {
			stepSequence.add(step);
		}
		return stepSequence;
	}
	
	/**
	 * 目前已知的最好的步长序列，最坏情况时间复杂度是 O(n4/3) ，1986年由Robert Sedgewick提出
	 * @return stepSequence
	 */
	@SuppressWarnings("unused")
	private List<Integer> sedgewickStepSequence() {
		List<Integer> stepSequence = new LinkedList<>();
		int k = 0, step = 0;
		while (true) {
			if (k % 2 == 0) {
				int pow = (int) Math.pow(2, k >> 1);
				step = 1 + 9 * (pow * pow - pow);
			} else {
				int pow1 = (int) Math.pow(2, (k - 1) >> 1);
				int pow2 = (int) Math.pow(2, (k + 1) >> 1);
				step = 1 + 8 * pow1 * pow2 - 6 * pow2;
			}
			if (step >= array.length) break;
			stepSequence.add(0, step);
			k++;
		}
		
		return stepSequence;
	}
}
