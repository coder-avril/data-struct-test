package test.util;

public class Integers {
	/**
	 * 生成指定个数的随机数
	 * @param count 随机数的总数量
	 * @param min 最小值
	 * @param max 最大值
	 */
	public static Integer[] random(int count, int min, int max) {
		if (count < 0 || min > max) return null;
		Integer[] array = new Integer[count];
		int delta = max - min + 1;
		for (int i = 0; i < count; i++) {
			array[i] = min + (int) (Math.random() * delta);
		}
		return array;
	}
	
	/**
	 * 遍历并打印数组的元素
	 * @param array
	 */
	public static void print(Integer[] array) {
		if (array == null || array.length == 0) return;
		StringBuilder sbr = new StringBuilder();
		sbr.append("{ ");
		for (int i = 0; i < array.length; i++) {
			sbr.append(array[i]).append(", ");
		}
		int end = sbr.length() - 1;
		sbr.delete(end - 1, end);
		sbr.append("}");
		
		System.out.println(sbr.toString());
		
	}
}
