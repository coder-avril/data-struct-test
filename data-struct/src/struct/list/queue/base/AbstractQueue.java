package struct.list.queue.base;

import struct.list.base.List;
import struct.list.linked.LinkedList;

/**
 * 通过组合的方式 利用现有的线性表（这里使用双向链表）实现
 * 
 * @author avril
 *
 * @param <E>
 */
public abstract class AbstractQueue<E> implements Queue<E> {
	/* 双向链表 */
	protected List<E> list = new LinkedList<>();
	
	/**
	 * 返回队列的元素的数量
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * 队列是否为空
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	/**
	 * 清空队列
	 */
	public void clear() {
		list.clear();
	}
	
	/**
	 * 入队（往队列尾部添加元素）
	 * @param element
	 */
	public void enQueue(E element) {
		list.add(element);
	}
	
	/**
	 * 出队（从队列头部取出元素）
	 */
	public E deQueue() {
		return list.remove(0);
	}
	
	/**
	 * 获取队列的头元素
	 */
	public E front() {
		return list.get(0);
	}
}
