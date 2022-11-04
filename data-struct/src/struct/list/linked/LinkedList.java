package struct.list.linked;

import struct.list.base.AbstractList;

/**
 * LinkedList 双向链表
 * @author avril
 *
 * @param <E>
 */
public class LinkedList<E> extends AbstractList<E> {
	
	// 首节点的指针	
	private Node<E> first = null;
	
	// 尾节点的指针
	private Node<E> last = null;

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
		
		if (index == size) { // 往最后添加元素
			Node<E> oldLast = last;
			last = new Node<>(last, element, null);
			if (oldLast == null) { // 这是链表添加的第一个元素（即 size==0 index==0）
				first = last;
			} else {
				oldLast.next = last;
			}
			
		} else {
			// 往指定的位置添加元素的话 原位置的元素充当next
			Node<E> next = node(index);
			Node<E> prev = next.prev;
			Node<E> node = new Node<>(prev, element, next);
			
			next.prev = node;
			if (prev == null) { // index == 0
				first = node;
			} else {
				prev.next = node;
			}
		}
		
		size++;
	}

	@Override
	public E remove(int index) {
		legalIndexCheck(index, false);
		
		Node<E> node = node(index);
		Node<E> next = node.next;
		Node<E> prev = node.prev;
		
		if (prev == null) { // index == 0
			first = next;
		} else {
			prev.next = next;
		}
		
		if (next == null) { // index == size -1
			last = prev;
		} else {
			next.prev = prev;
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
		// 首尾节点断掉后 其他元素会因为GC自动被清理掉 无需给所有的next赋值为null
		first = null;
		last = null;
	}
	
	/**
	 * 返回指定索引对应的节点
	 * @param index
	 * @return node
	 */
	private Node<E> node(int index) {
		legalIndexCheck(index, false);
		
		Node<E> node = null;
		if (index < (size >> 1)) { // 如果指定的index在左边
			node = first;
			// 当前节点等于前一个节点的next
			for (int i = 0; i < index; i++) {
				node = node.next;
			}
		} else { // 如果指定的index在右边
			node = last;
			for (int i = size - 1; i > index; i--) {
				node = node.prev;
			}
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
			sbr.append(" ").append(node).append(",");
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
		// 指向上一个节点的引用
		Node<E> prev = null;
		
		/**
		 * 生成有参的构造函数
		 * @param element
		 * @param next
		 */
		public Node(Node<E> prev, E element, Node<E> next) {
			this.prev = prev;
			this.element = element;
			this.next = next;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (prev != null) {
				sb.append(prev.element);
			} else {
				sb.append("null");
			}
			sb.append("_(").append(element).append(")_");
			if (next != null) {
				sb.append(next.element);
			} else {
				sb.append("null");
			}
			return sb.toString();
		}
	}

}
