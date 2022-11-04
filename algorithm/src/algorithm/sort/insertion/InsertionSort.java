package algorithm.sort.insertion;

import algorithm.sort.Sort;

/**
 * InsertionSort 插入排序（非常类似于扑克牌的排序 [稳定排序]
 * 执行流程
 * (1) 在执行过程中，插入排序会将序列分为2部分
 *     ✓ 头部是已经排好序的，尾部是待排序的
 * (2) 从头开始扫描每一个元素
 *     ✓ 每当扫描到一个元素，就将它插入到头部合适的位置，使得头部数据依然保持有序
 * 
 * @author avril
 *
 * @param <E>
 */
@SuppressWarnings("unused")
public class InsertionSort<E extends Comparable<E>> extends Sort<E> {
	@Override
	protected void sort() {
		sort_v3();
	}
	
	/** 插入排序 */
	private void sort_v1() {
		for (int begin = 1; begin < array.length; begin++) {
			int cur = begin;
			while (cur > 0 && cmp(cur, cur - 1) < 0) {
				swap(cur, cur - 1);
				cur--;
			}
		}
	}
	
	/** 优化： 思路是将【交换】转为【挪动】 */
	private void sort_v2() {
		for (int begin = 1; begin < array.length; begin++) {
			int cur = begin;
			E v = array[cur];
			while (cur > 0 && cmp(cur, cur - 1) < 0) {
				array[cur] = array[cur - 1];
				cur--;
			}
			array[cur] = v;
		}
	}
	
	/** 优化： 利用二分搜索 */
	private void sort_v3() {
		for (int begin = 1; begin < array.length; begin++) {
			E v = array[begin];
			int insertIndex = search(begin);
			// 将 [insertIndex, begin) 范围内的元素往右边挪动一个单位
			for (int i = begin; i > insertIndex; i--) {
				array[i] = array[i - 1];
			}
			array[insertIndex] = v;
		}
	}
	
	/*
	 * 利用二分搜索找到index位置元素的待插入位置
	 * 已经排好序数组的区间范围是 [0, index)
	 * @param index
	 */
	private int search(int index) {
		int begin = 0;
		int end = index;
		while (begin < end) {
			int mid = (begin + end) >> 1;
			if (cmp(array[index], array[mid]) < 0) {
				end = mid;
			} else {
				begin = mid + 1;
			}
		}
		return begin;
	}
	
	/**
	 * 演示查找v在有序数组array中待插入位置
	 * @param array 事先排序过的整型数组
	 * @param v 要查找的整数
	 */
	public static int search(int[] array, int v) {
		if (array == null || array.length == 0) return -1;
		
		int begin = 0;
		int end = array.length;
		while (begin < end) {
			int mid = (begin + end) >> 1;
			if (v < array[mid]) {
				end = mid;
			} else {
				begin = mid + 1;
			}
		}
		
		return begin;
	}
	
	/**
	 * 演示二分查找
	 * @param array 事先排序过的整型数组
	 * @param v 要查找的整数
	 */
	public static int indexOf(int[] array, int v) {
		if (array == null || array.length == 0) return -1;
		
		int begin = 0;
		int end = array.length;
		while (begin < end) {
			int mid = (begin + end) >> 1;
			if (v < array[mid]) {
				end = mid;
			} else if (v > array[mid]) {
				begin = mid + 1;
			} else {
				return mid;
			}
		}
		
		return -1;
	}
}
