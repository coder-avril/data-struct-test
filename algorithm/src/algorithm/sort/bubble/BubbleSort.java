package algorithm.sort.bubble;

import algorithm.sort.Sort;

/**
 * BubbleSort 冒泡排序 [稳定排序]
 * 执行流程
 * (1) 从头开始比较每一对相邻元素，如果第1个比第2个大，就交换它们的位置
 *     ✓ 执行完一轮后，最末尾那个元素就是最大的元素
 * (2) 忽略(1)中曾经找到的最大元素，重复执行步骤(1)，直到全部元素有序
 * 
 * @author avril
 *
 */
@SuppressWarnings("unused")
public class BubbleSort<E extends Comparable<E>> extends Sort<E> {
	@Override
	protected void sort() {
		sort_v3();
	}
	
	/** 冒泡排序 */
	private void sort_v1() {
		for (int end = array.length - 1; end > 0; end--) {
			for (int begin = 1; begin <= end; begin++) {
				if (cmp(begin, begin - 1) < 0) { // array[begin] < array[begin - 1]
					swap(begin, begin - 1);
				}
			}
		}
	}
	
	/** 优化1： 如果序列已经完全有序，可以提前终止冒泡排序 */
	private void sort_v2() {
		for (int end = array.length - 1; end > 0; end--) {
			boolean sorted = true;
			for (int begin = 1; begin <= end; begin++) {
				if (cmp(begin, begin - 1) < 0) {
					swap(begin, begin - 1);
					sorted = false;
				}
			}
			if (sorted) break;
		}
	}
	
	/** 优化2： 如果序列尾部已经局部有序，可以记录最后1次交换的位置，减少比较次数 */
	private void sort_v3() {
		for (int end = array.length - 1; end > 0; end--) {
			int sortedIndex = 0;
			for (int begin = 1; begin <= end; begin++) {
				if (cmp(begin, begin - 1) < 0) {
					swap(begin, begin - 1);
					sortedIndex = begin;
				}
			}
			end = sortedIndex;
		}
	}
}
