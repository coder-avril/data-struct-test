package struct.PriorityQueue;

import java.util.Comparator;

import struct.heap.BinaryHeap;

/**
 * PriorityQueue 优先级队列
 * 
 * @author avril
 *
 * @param <E>
 */
public class PriorityQueue<E> {
	/* 内置二叉堆（大顶堆） */
	private BinaryHeap<E> heap = new BinaryHeap<>();
	
	public PriorityQueue(Comparator<E> comparator) {
		heap = new BinaryHeap<>(comparator);
	}
	
	public PriorityQueue() {
		this(null);
	}
	
	/**
	 * 返回队列的元素的数量
	 */
	public int size() {
		return heap.size();
	}
	
	/**
	 * 队列是否为空
	 */
	public boolean isEmpty() {
		return heap.isEmpty();
	}
	
	/**
	 * 清空队列
	 */
	public void clear() {
		heap.clear();
	}
	
	/**
	 * 入队（往队列尾部添加元素）
	 * @param element
	 */
	public void enQueue(E element) {
		heap.add(element);
	}
	
	/**
	 * 出队（从队列头部取出元素）
	 */
	public E deQueue() {
		return heap.remove();
	}
	
	/**
	 * 获取队列的头元素
	 */
	public E front() {
		return heap.get();
	}
}
