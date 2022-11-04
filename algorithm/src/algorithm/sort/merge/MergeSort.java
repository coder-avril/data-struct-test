package algorithm.sort.merge;

import algorithm.sort.Sort;

/**
 * MergeSort 归并排序 [稳定排序]
 * 执行流程
 * (1) 不断地将当前序列平均分割成2个子序列
 *     ✓ 直到不能再分割（序列中只剩1个元素）
 * (2) 不断地将2个子序列合并成一个有序序列
 *     ✓ 直到最终只剩下1个有序序列
 *     
 * @author avril
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class MergeSort<E extends Comparable<E>> extends Sort<E> {
	/* 内置左半数组 */
	private E[] leftArray;
	
	@Override
	protected void sort() {
		leftArray = (E[]) new Comparable[array.length >> 1];
		sort(0, array.length);
	}
	
	/*
	 * 对 [begin, end) 范围的数据进行归并排序 
	 * @param begin
	 * @param end
	 */
	private void sort(int begin, int end) {
		if (end - begin < 2) return;
		
		int mid = (begin + end) >> 1;
		sort(begin, mid);
		sort(mid, end);
		merge(begin, mid, end);
	}
	
	/*
	 * 对 [begin, mid) 和 [mid, end) 范围的序列合并成一个有序序列
	 * @param begin
	 * @param mid
	 * @param end
	 */
	private void merge(int begin, int mid, int end) {
		int li = 0, le = mid - begin;
		int ri = mid, re = end;
		int ai = begin;
		
		// 备份左边数组
		for (int i = li; i < le; i++) {
			leftArray[i] = array[begin + i];
		}
		
		// 如果左边还没有结束
		while (li < le) {
			if (ri < re && cmp(array[ri], leftArray[li]) < 0) {
				array[ai++] = array[ri++];
			} else {
				array[ai++] = leftArray[li++];
			}
		}
	}
}
