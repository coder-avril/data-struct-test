package struct.set;

import java.util.Comparator;

import struct.set.base.Set;
import struct.tree.RedBlackTree;
import struct.tree.base.BinaryTree;

/**
 * TreeSet 树形集合（红黑树实现版）
 * 
 * @author avril
 *
 * @param <E>
 */
public class TreeSet<E> implements Set<E> {
	/* 内置红黑树 */
	private RedBlackTree<E> tree;
	
	public TreeSet() {
		this(null);
	}
	
	public TreeSet(Comparator<E> comparator) {
		tree = new RedBlackTree<>(comparator);
	}

	/**
	 * 获取元素数量
	 */
	@Override
	public int size() {
		return tree.size();
	}

	/**
	 * 是否为空
	 */
	@Override
	public boolean isEmpty() {
		return tree.isEmpty();
	}

	/**
	 * 清空元素
	 */
	@Override
	public void clear() {
		tree.clear();
	}

	/**
	 * 是否包含元素
	 * @param element
	 */
	@Override
	public boolean contains(E element) {
		return tree.contains(element);
	}

	/**
	 * 添加元素
	 * @param element
	 */
	@Override
	public void add(E element) {
		// 红黑树默认就是去重
		tree.add(element);
	}

	/**
	 * 删除元素
	 * @param element
	 */
	@Override
	public void remove(E element) {
		tree.remove(element);
	}

	/**
	 * 遍历集合
	 * @param visitor
	 */
	@Override
	public void traversal(Visitor<E> visitor) {
		tree.inorderByRecursion(new BinaryTree.Visitor<E>() {
			@Override
			public void visit(E element) {
				// 由于Visitor是不同的类和接口 所以需要像这样转换一下
				if(visitor.visit(element)) return;
			}
		});
	}

}
