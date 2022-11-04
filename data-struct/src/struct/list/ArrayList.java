package struct.list;

import struct.list.base.AbstractList;

/**
 * ArrayList 动态数组
 * 
 * @author avril
 *
 */
public class ArrayList<E> extends AbstractList<E> {

	// 存储数据的内部数组
	private E[] elements = null;
	
	/**
	 * 无参构造方法（使用默认容量）
	 */
	public ArrayList() {
		this(DEFAULT_CAPACIT);
	}
	
	/**
	 * 有参构造方法
	 * @param capacity 容量
	 */
	@SuppressWarnings("unchecked")
	public ArrayList(int capacity) {
		// 判断入力容量是否低于默认容量（低于的话，使用默认容量）
		int tmpCapa = capacity < DEFAULT_CAPACIT ? DEFAULT_CAPACIT : capacity;
		elements = (E[]) new Object[tmpCapa];
	}

	/**
	 * 返回指定索引所对应的元素
	 */
	@Override
	public E get(int index) {
		// 边界check，非添加时，第二个参数必须为false
		legalIndexCheck(index, false);
		return elements[index];
	}

	/**
	 * 用指定的值 来设置 指定的索引对应的元素，并返回原来的值
	 * @param index
	 * @param element
	 */
	@Override
	public E set(int index, E element) {
		// 边界check，非添加时，第二个参数必须为false
		legalIndexCheck(index, false);
		
		// 将原来的值先临时保存
		E oldElement = elements[index];
		elements[index] = element;
		
		return oldElement;
	}

	/**
	 * 用指定的值 追加到 指定的索引
	 * @param index
	 * @param element
	 */
	@Override
	public void add(int index, E element) {
		// 边界和值check
		legalIndexCheck(index, true);
		
		// 扩容检测 保证多加一个元素不至于出现数组越界
		increaseCapacity(size + 1);
		
		// 在数组中间位置加元素时，需要把指定位置之后的元素（包含指定位置），整体往后挪动
		/*
		 * for (int i = size - 1; i >= index; i--) {
		 * elements[i + 1] = elements[i]; 
		 * }
		 * 	为了提高效率可以将以下代码改写为
		 */
		for (int i = size; i > index; i--) {
			elements[i] = elements[i - 1];
		}
		// 添加元素到指定index，size增加
		elements[index] = element;
		size++;
	}

	/**
	 * 删除指定的索引索引对应的元素，并返回该元素
	 * @param index
	 */
	@Override
	public E remove(int index) {
		// 边界check
		legalIndexCheck(index, false);
		
		// 将原来的值先临时保存
		E oldElement = elements[index];
		
		// 在数组中间位置加元素时，需要把指定位置之后的元素，整体往前挪动
		// 然后把最后一位清空
		for (int i = index + 1; i < size; i++) {
			elements[i - 1] = elements[i];
		}
		// 下面两句可以合并为一句：elements[--size] = null;
		elements[size - 1] = null;
		size--;
		
		// 缩容处理
		decreaseCapacity();
		
		return oldElement;
	}

	/**
	 * 返回指定元素对应的索引
	 * @param element エレメント
	 */
	@Override
	public int indexOf(E element) {
		for (int index = 0; index < size; index++) {
			if (elements[index].equals(element)) {
				return index;
			}
		}
		return NO_INDEX;
	}

	/**
	 * 清空数组的所有元素
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		// 为了方便GC回收对象，设上NULL控制
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
		
		if (elements != null && elements.length > DEFAULT_CAPACIT) { // 缩容
			elements = (E[]) new Object[DEFAULT_CAPACIT];
		}
	}
	
	/**
	 * 动态扩容（扩展到原来的1.5倍）
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	private void increaseCapacity(int capacity) {
		int oldCapacity = elements.length;
		// 容量充足的话 直接返回
		if (oldCapacity > capacity) return;
		
		// 容量不足的话 扩展到原来大小的1.5倍
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		E[] newElements = (E[]) new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			newElements[i] = elements[i];
		}
		
		elements = newElements;
	}
	
	/**
	 * 动态缩容（减小到原来的2倍
	 */
	@SuppressWarnings("unchecked")
	private void decreaseCapacity() {
		int capacity = elements.length;
		int newCapacity = capacity >> 1;
		
		if (size >= newCapacity || capacity <= DEFAULT_CAPACIT) return;
		// 否则表面剩余空间很多
		E[] newElements = (E[]) new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			newElements[i] = elements[i];
		}
		elements = newElements;
	}
	
	/**
	 * 重写toString方法
	 *  格式[ a, b, c, ..., d ]
	 */
	@Override
	public String toString() {
		StringBuffer sbr = new StringBuffer();
		sbr.append("[");
		for (int i = 0; i < size; i++) {
			sbr.append(" ").append(elements[i]).append(",");
		}
		if (size > 0) sbr.deleteCharAt(sbr.length() - 1);

		sbr.append(" ]");
		return sbr.toString();
	}

}
