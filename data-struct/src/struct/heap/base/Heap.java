package struct.heap.base;

public interface Heap<E> {
	/**
	 * 元素数量
	 */
    int size(); 
    
    /**
     * 是否为空
     */
    boolean isEmpty();
    
    /**
     * 清空元素
     */
    void clear();
    
    /**
     * 添加元素
     * @param element
     */
    void add(E element);
    
    /**
     * 获取堆顶元素
     */
    E get(); 
    
    /**
     * 删除堆顶元素
     */
    E remove();
    
    /**
     * 删除堆顶元素，添加新元素
     * @param element
     */
    E replace(E element); 
}
