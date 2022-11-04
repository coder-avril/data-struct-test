package struct.set.base;

/**
 * Set 集合
 * 
 * @author avril
 *
 * @param <E>
 */
public interface Set<E> {
	/* 获取元素数量 */
	int size();
	
	/* 是否为空 */
	boolean isEmpty();
	
	/* 清空元素 */
	void clear();
	
	/* 是否包含元素 */
	boolean contains(E element);
	
	/* 添加元素 */
	void add(E element);
	
	/* 删除元素 */
	void remove(E element);
	
	/* 遍历集合 */
	void traversal(Visitor<E> visitor);
	
	/** 访问器 */
	public static abstract class Visitor<E> {
		boolean stop;
		public abstract boolean visit(E element);
	}
}
