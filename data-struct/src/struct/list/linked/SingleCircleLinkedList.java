package struct.list.linked;

import struct.list.base.AbstractList;

/**
 * SingleCircleLinkedList 单向循环链表
 * @author avril
 *
 * @param <E>
 */
public class SingleCircleLinkedList<E> extends AbstractList<E> {
	
	// 首节点的指针	
	private Node<E> first = null;

	@Override
	public E get(int index) {
		return node(index).element;
	}

	@Override
	public E set(int index, E element) {
		Node<E> node = node(index);
		E oldElement = node.element;
		node.element = element;
		return oldElement;
	}

	/**
	 * 用指定的值 追加到 指定的索引
	 * @param index
	 * @param element
	 */
	@Override
	public void add(int index, E element) {
		// 索引的边界检查
		legalIndexCheck(index, true);
		
		if (index == 0) { // 添加首节点
			Node<E> newFirst = new Node<E>(element, first); // 不可以先改first 否则会影响后面node的查询
			// 拿到最后1个节点
			Node<E> last = (size == 0) ? newFirst : node(size - 1);
			last.next = newFirst;
			first = newFirst;
		} else {
			Node<E> prev = node(index - 1);
			prev.next = new Node<E>(element, prev.next);
		}
		size++;
	}

	@Override
	public E remove(int index) {
		legalIndexCheck(index, false);
		
		Node<E> node = first;
		if (index == 0) {
			if (size == 1) {
				first = null;
			} else {
				Node<E> last = node(size - 1);
				first = first.next;
				last.next = first;
			}
			first = first.next;
		} else {
			Node<E> prev = node(index - 1);
			node = prev.next;
			prev.next = node.next;
		}
		size--;
		return node.element;
	}

	@Override
	public int indexOf(E element) {
		Node<E> node = first;
		if (element == null) {
			for (int i = 0; i < size; i++) {
				if (node.element == null) return i;
				node = node.next;
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (element.equals(node.element)) return i;
				node = node.next;
			}
		}
		return NO_INDEX;
	}

	@Override
	public void clear() {
		size = 0;
		// 首节点断掉后 其他元素会因为GC自动被清理掉 无需给所有的next赋值为null
		first = null;
	}
	
	/**
	 * 返回指定索引对应的节点
	 * @param index
	 * @return node
	 */
	private Node<E> node(int index) {
		legalIndexCheck(index, false);
		
		Node<E> node = first;
		// 当前节点等于前一个节点的next
		for (int i = 0; i < index; i++) {
			node = node.next;
		}
		return node;
	}
	
	/**
	 * 重写toString方法
	 *  格式[ a, b, c, ..., d ]
	 */
	@Override
	public String toString() {
		StringBuffer sbr = new StringBuffer();
		sbr.append("[");
		Node<E> node = first;
		for (int i = 0; i < size; i++) {
			sbr.append(" ").append(node.element).append(",");
			node = node.next;
		}
		if (size > 0) sbr.deleteCharAt(sbr.length() - 1);

		sbr.append(" ]");
		return sbr.toString();
	}
	
	/**
	 * 保存元素的内部节点（静态内部类
	 * @author avril
	 *
	 * @param <E>
	 */
	private static class Node<E> {
		// 元素
		E element = null;
		// 指向下一个节点的引用
		Node<E> next = null;
		
		/**
		 * 生成有参的构造函数
		 * @param element
		 * @param next
		 */
		public Node(E element, Node<E> next) {
			this.element = element;
			this.next = next;
		}
	}

}
