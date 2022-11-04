package algorithm.sort.counting;

import algorithm.sort.Sort;

/**
 * CountingSort 计数排序
 * 
 * 适合对一定范围内的整数进行排序
 * 计数排序的核心思想是 统计每个整数在序列中出现的次数，进而推导出每个整数在有序序列中的索引
 * 
 * 如果自定义对象可以提供用以排序的整数类型，依然可以使用计数排序
 * 
 * @author avril
 *
 */
public class CountingSort extends Sort<Integer> {
	@Override
	protected void sort() {
		sort_v2();
	}
	
	/**
	 * 计数排序 – 改进思路 [稳定排序]
	 */
	private void sort_v2() {
		// 找出最值
		int max = array[0];
		int min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
			if (array[i] < min) {
				min = array[i];
			}
		}
		
		// 开辟内存空间 存储次数
		int[] counts = new int[max - min + 1];
		// 统计每个整数出现的次数
		for (int i = 0; i < array.length; i++) {
			counts[array[i] - min]++;
		}
		// 累加次数
		for (int i = 1; i < counts.length; i++) {
			counts[i] += counts[i - 1];
		}
		
		// 从后往前遍历元素 将它放到有序数组中的合适位置
		int[] newArray = new int[array.length];
		for (int i = array.length - 1; i >= 0; i--) {
			newArray[--counts[array[i] - min]] = array[i];
		}
		
		// 将有序数组赋值到array
		for (int i = 0; i < newArray.length; i++) {
			array[i] = newArray[i];
		}
	}
	
	/**
	 * 计数排序 – 最简单的实现 [不稳定排序]
	 */
	@SuppressWarnings("unused")
	private void sort_v1() {
		// 找出最大值
		int max = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		
		// 开辟内存空间 存储每个整数出现的次数
		int[] counts = new int[max + 1]; // 注意整型数组被开辟后 元素默认值都是0
		// 统计每个整数的出现次数
		for (int i = 0; i < array.length; i++) {
			counts[array[i]]++;
		}
		
		// 根据整数的出现次数，对整数进行排序
		int index = 0;
		for (int i = 0; i < counts.length; i++) {
			while (counts[i]-- > 0) {
				array[index++] = i;
			}
		}
	}
}
