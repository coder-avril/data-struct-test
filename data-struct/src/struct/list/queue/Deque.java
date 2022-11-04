package struct.list.queue;

import struct.list.queue.base.AbstractQueue;

/**
 * Deque 双端队列
 * 
 * @author avril
 *
 * @param <E>
 */
public class Deque<E> extends AbstractQueue<E> {
	/**
	 * 向对头添加元素
	 * @param element
	 */
	public void enQueueFront(E element) {
		list.add(0, element);
	}
	
	/**
	 * 从队尾出队
	 */
	public E deQueueRear() {
		return list.remove(list.size() - 1);
	}
	
	/**
	 * 获取队尾的元素
	 */
	public E rear() {
		return list.get(list.size() - 1);
	}
}
