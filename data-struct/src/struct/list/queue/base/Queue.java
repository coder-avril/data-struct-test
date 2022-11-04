package struct.list.queue.base;

public interface Queue<E> {
	/**
	 * 返回队列的元素的数量
	 */
	int size();
	
	/**
	 * 队列是否为空
	 */
	boolean isEmpty();
	
	/**
	 * 入队（往队列尾部添加元素）
	 * @param element
	 */
	void enQueue(E element);
	
	/**
	 * 出队（从队列头部取出元素）
	 */
	E deQueue();
	
	/**
	 * 获取队列的头元素
	 */
	E front();
	
	/**
	 * 清空队列
	 */
	void clear();
}
