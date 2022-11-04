package struct.set;

import struct.list.base.AbstractList;
import struct.list.base.List;
import struct.list.linked.LinkedList;
import struct.set.base.Set;

/**
 * ListSet 线性表集合（链表实现版）
 * 
 * @author avril
 *
 * @param <E>
 */
public class ListSet<E> implements Set<E> {
	/* 内置双向链表 */
	private List<E> list = new LinkedList<>();

	/**
	 * 获取元素数量
	 */
	@Override
	public int size() {
		return list.size();
	}

	/**
	 * 是否为空
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * 清空元素
	 */
	@Override
	public void clear() {
		list.clear();
	}

	/**
	 * 是否包含元素
	 * @param element
	 */
	@Override
	public boolean contains(E element) {
		return list.contains(element);
	}

	/**
	 * 添加元素
	 * @param element
	 */
	@Override
	public void add(E element) {
		// 集合不允许有相同的元素出现 这里直接覆盖旧元素
		int index = list.indexOf(element);
		if (index != AbstractList.NO_INDEX) {
			list.set(index, element);
		} else {
			list.add(element);
		}
	}

	/**
	 * 删除元素
	 * @param element
	 */
	@Override
	public void remove(E element) {
		int index = list.indexOf(element);
		if (index != AbstractList.NO_INDEX) {
			list.remove(index);
		}
	}

	/**
	 * 遍历集合
	 * @param visitor
	 */
	@Override
	public void traversal(Visitor<E> visitor) {
		if (visitor == null) return;
		
		int size = list.size();
		for (int i = 0; i < size; i++) {
			// 添加终止的机制（如果返回true则直接退出
			if (visitor.visit(list.get(i))) return;
		}
	}
}
