package struct.list.queue;

import struct.list.queue.base.Queue;

/**
 * CircleQueue 循环队列
 * 
 * @author avril
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class CircleQueue<E> implements Queue<E> {
	/* 队头指针 */
	private int front;
	
	private int size;
	
	private E[] elements;
	
	/* 数组初始化的默认大小 */
	private static final int DEFAULT_CAPACIT = 10;
	
	public CircleQueue() {
		this.elements = (E[]) new Object[DEFAULT_CAPACIT];
	}

	/**
	 * 返回队列的元素的数量
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * 队列是否为空
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 入队
	 */
	@Override
	public void enQueue(E element) {
		// 动态扩容（保证多加一个元素不至于出现数组越界
		increaseCapacity(size + 1);
		// 注意需要取模
		elements[index(size)] = element;
		size++;
	}

	/**
	 * 出队
	 */
	@Override
	public E deQueue() {
		E frontElement = elements[front];
		elements[front] = null;
		front = index(1); // 注意需要取模
		size--;
		return frontElement;
	}

	/**
	 * 获取队列的头元素
	 */
	@Override
	public E front() {
		return elements[front];
	}

	/**
	 * 清空队列
	 */
	@Override
	public void clear() {
		// 为了方便GC回收对象，设上NULL控制
		for (int i = 0; i < size; i++) {
			elements[index(i)] = null;
		}
		size = 0;
		front = 0;
	}
	
	/**
	 * 动态扩容（扩展到原来的1.5倍）
	 * @param capacity
	 */
	private void increaseCapacity(int capacity) {
		int oldCapacity = elements.length;
		// 容量充足的话 直接返回
		if (oldCapacity > capacity) return;
		
		// 容量不足的话 扩展到原来大小的1.5倍
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		E[] newElements = (E[]) new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			newElements[i] = elements[index(i)]; // 注意取模
		}
		elements = newElements;
		
		front = 0; // 重置front
	}
	
	/**
	 * 将下标转换为循环队列的真实下标
	 * @param index
	 */
	private int index(int index) {
		// index % elements.length 比起加减性能低 所以可以用下面的代码进行替换
		// n%m 等价于 n – (n >= m ? m : 0) 前提条件 n < 2m
		index += front;
		return index - (index > elements.length ? elements.length : 0);
	}
}
