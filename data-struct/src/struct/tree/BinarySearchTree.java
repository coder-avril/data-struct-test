package struct.tree;

import java.util.Comparator;

import struct.tree.base.BinaryTree;

/**
 * BinarySearchTree 二叉搜索树
 * 
 * @author avril
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class BinarySearchTree<E> extends BinaryTree<E> {
	/* 比较器 */
	private Comparator<E> comparator;
	
	public BinarySearchTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	public BinarySearchTree() {
		this(null);
	}
	
	/**
	 * 添加元素
	 * @param element
	 */
	public void add(E element) {
		elementNotNullCheck(element);
		
		if (root == null) { // 添加的是第一个节点root
			root = createNode(element, null);
			size++;
			afterAdd(root);
			return;
		}
		
		// 添加的不是第一个节点的话 首先得找到根节点
		Node<E> node = root;
		Node<E> parent = root; // 用于记录最终用于插入父节点
		int cmp = 0; // 用于记录插入的方向
		while (node != null) {
			cmp = compare(element, node.element);
			parent = node;
			if (cmp > 0) {
				node = node.right;
			} else if (cmp < 0) {
				node = node.left;
			} else { // 相等覆盖
				node.element = element;
				return;
			}
		}
		
		Node<E> newNode = createNode(element, parent);
		// 看看插入到父节点的哪个位置
		if (cmp > 0) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}
		size++;
		
		afterAdd(newNode);
	}
	
	/**
	 * 添加之后的平衡操作（交由子类去实现
	 * @param node
	 */
	protected void afterAdd(Node<E> node) {}
	
	/**
	 * 删除之后的平衡操作（交由子类去实现
	 * @param node 被删除的节点 或者用于替代的节点
	 */
	protected void afterRemove(Node<E> node) {}
	
	/**
	 * 删除元素
	 * @param element
	 */
	public void remove(E element) {
		remove(node(element));
	}
	
	/**
	 * 是否包含对象元素
	 * @param element
	 */
	public boolean contains(E element) {
		return node(element) != null;
	}
	
	/**
	 * 元素非空check
	 * @param element
	 */
	private void elementNotNullCheck(E element) {
		if (element == null) {
			throw new IllegalArgumentException("element must not be null!");
		}
	}
	
	/**
	 * 比较两个元素的大小（正为e1 > e2 负为e1 < e2 0为相等
	 * @param e1
	 * @param e2
	 */
	private int compare(E e1, E e2) {
		if (comparator != null) {
			return comparator.compare(e1, e2);
		}
		return ((Comparable<E>)e1).compareTo(e2);
	}
	
	/**
	 * 获取指定元素的节点
	 * @param element
	 */
	private Node<E> node(E element) {
		Node<E> node = root;
		while (node != null) {
			int cmp = compare(element, node.element);
			if (cmp == 0) return node;
			if (cmp > 0) { // 说明得去右边找更大的节点才有相等的可能
				node = node.right;
			} else { // 说明得去左边找更小得节点才有相等的可能
				node = node.left;
			}
		}
		return null;
	}
	
	/**
	 * 删除指定节点
	 * @param node
	 */
	private void remove(Node<E> node) {
		if (node == null) return;
		
		if (node.hasTwoChildren()) { // 度为2的节点
			// 找到后继节点
			Node<E> s = successor(node);
			// 用后继节点的值覆盖度为2的节点的值
			node.element = s.element;
			// 删除后继节点
			node = s; // 这里将后继节点s赋值给node 交给后续处理来删除
		}
		
		// 删除node节点（这里的node的度必然为1或者0）
		Node<E> replace = node.left != null ? node.left : node.right;
		if (replace != null) { // node是度为1的节点
			// 更改parent 
			replace.parent = node.parent;
			// 更改parent的left和right指向
			if (node.parent == null) { // 度为1并且是root
				root = replace;
			} else if (node == node.parent.left) {
				node.parent.left = replace;
			} else { // node == node.parent.right
				node.parent.right = replace;
			}
			
			// 删除节点之后的处理
			afterRemove(replace);
		} else if (node.parent == null) { // node是叶子节点并且是根节点
			root = null;
			
			// 删除节点之后的处理
			afterRemove(node);
		} else { // node是普通叶子节点
			if (node == node.parent.left) {
				node.parent.left = null;
			} else { // node == node.parent.right
				node.parent.right = null;
			}
			
			// 删除节点之后的处理
			afterRemove(node);
		}
		
		size--;
	}
}
