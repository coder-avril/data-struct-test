package algorithm.sort.selection;

import algorithm.sort.Sort;

/**
 * SelectionSort 选择排序 [不稳定排序]
 * 执行流程
 * (1) 从序列中找出最大的那个元素，然后与最末尾的元素交换位置
 *     ✓ 执行完一轮后，最末尾的那个元素就是最大的元素
 * (2) 忽略(1)中曾经找到的最大元素，重复执行步骤(1)
 * 
 * @author avril
 *
 */
public class SelectionSort<E extends Comparable<E>> extends Sort<E> {
	@Override
	protected void sort() {
		for (int end = array.length - 1; end > 0; end--) {
			int maxIndex = 0;
			for (int begin = 1; begin <= end; begin++) {
				if (cmp(maxIndex, begin) <= 0) { // array[maxIndex] <= array[begin]
					maxIndex = begin;
				}
			}
			swap(maxIndex, end);
		}
	}
}
