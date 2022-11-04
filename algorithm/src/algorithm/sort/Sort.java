package algorithm.sort;

public abstract class Sort<E extends Comparable<E>> {
	/* 内置数组 */
	protected E[] array;
	
	/* 用于记录比较和交换的次数 */
	private int cmpCount;
	private int swpCount;
	public void sort(E[] array) {
		if (array == null || array.length < 2) return;
		this.array = array;
		sort();
	}
	
	/**
	 * 交由子类（具体排序算法）来实现
	 */
	protected void sort() {}
	
	/**
	 * 比较数组索引i1和i2的大小
	 * 返回值>0 表示 array[i1] > array[i2]
	 * 返回值<0 表示 array[i1] < array[i2]
	 * 返回值=0 表示 array[i1] = array[i2]
	 * @param i1
	 * @param i2
	 */
	protected int cmp(int i1, int i2) {
		cmpCount++;
		return ((Comparable<E>) array[i1]).compareTo(array[i2]);
	}
	
	/**
	 * 比较值的大小
	 * @param v1
	 * @param v2
	 */
	protected int cmp(E v1, E v2) {
		cmpCount++;
		return v1.compareTo(v2);
	}
	
	/**
	 * 交换数组索引i1和i2的值
	 * @param i1
	 * @param i2
	 */
	protected void swap(int i1, int i2) {
		swpCount++;
		E tmp = array[i1];
		array[i1] = array[i2];
		array[i2] = tmp;
	}
}
