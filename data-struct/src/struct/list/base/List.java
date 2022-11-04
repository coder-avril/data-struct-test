package struct.list.base;

/**
 * Dynamic Array 动态数组
 * @author avril
 *
 * @param <E>
 */
public interface List<E> {
	// 数组元素的数量
	int size();
	
	// 是否为空
	boolean isEmpty(); 
	
	// 是否包含当前元素
	boolean contains(E element);
	
	// 添加当前元素到数组的末尾
	void add(E element);
	
	// 返回指定索引所对应的元素
	E get(int index);
	
	// 用指定的值 来设置 指定的索引对应的元素，并返回原来的值
	E set(int index, E element);
	
	// 用指定的值 追加到 指定的索引
	void add(int index, E element);
	
	// 删除指定的索引索引对应的元素，并返回该元素
	E remove(int index);
	
	// 返回指定元素对应的索引
	int indexOf(E element);
	
	// 空数组的所有元素
	void clear();
	
}
