package struct.tree.base;

import java.util.LinkedList;
import java.util.Queue;
import printer.BinaryTreeInfo;

/**
 * BinaryTree 二叉树
 * 
 * @author avril
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public abstract class BinaryTree<E> implements BinaryTreeInfo {
	/* 数量 */
	protected int size;
	
	/* 根节点 */
	protected Node<E> root;
	
	/**
	 * 包含元素的数量
	 */
	public int size() {
		return size;
	}
	
	/**
	 * 是否为空
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * 清空二叉搜索树
	 */
	public void clear() {
		root = null;
		size = 0;
	}
	
	/**
	 * 获得指定节点的前驱
	 * @param node
	 */
	protected Node<E> predecessor(Node<E> node) {
		if (node == null) return null;
		
		// 前驱节点在左子树当中（left.right.right...
		Node<E> p = node.left;
		if (p != null) {
			while (p.right != null) {
				p = p.right;
			}
			return p;
		}
		
		// 从父节点、祖父节点中寻找前驱
		while (node.parent != null && node == node.parent.left) {
			node = node.parent;
		}
		
		return node.parent;
	}
	
	/**
	 * 获得指定节点的后继
	 * @param node
	 */
	protected Node<E> successor(Node<E> node) {
		if (node == null) return null;
		
		// 前驱节点在右子树当中（right.left.left...
		Node<E> p = node.right;
		if (p != null) {
			while (p.left != null) {
				p = p.left;
			}
			return p;
		}
		
		// 从父节点、祖父节点中寻找前驱
		while (node.parent != null && node == node.parent.right) {
			node = node.parent;
		}
		
		return node.parent;
	}
	
	/**
	 * 层序遍历
	 * @param visitor
	 */
	public void levelOrder(Visitor<E> visitor) {
		if (root == null || visitor == null) return;
		
		// 使用队列
		Queue<Node<E>> queue = new LinkedList<>(); // 这里使用的是Java官方的Queue
		queue.offer(root); // 将根节点入队
		
		while (!queue.isEmpty()) { // 循环执行以下操作，直到队列为空
			// 将队头节点 A 出队，进行访问
			Node<E> node = queue.poll();
			visitor.visit(node.element);
			// 将 A 的左子节点入队
			if (node.left != null) queue.offer(node.left);
			// 将 A 的右子节点入队
			if (node.right != null) queue.offer(node.right);
		}
	}
	
	/**
	 * 前序遍历（递归版）
	 * @param visitor
	 */
	public void preorderByRecursion(Visitor<E> visitor) {
		preorder(root, visitor);
	}
	
	private void preorder(Node<E> node, Visitor<E> visitor) {
		if (node == null || visitor == null) return;
		visitor.visit(node.element);
		preorder(node.left, visitor);
		preorder(node.right, visitor);
	}
	
	/**
	 * 中序遍历（递归版）
	 * @param visitor
	 */
	public void inorderByRecursion(Visitor<E> visitor) {
		inorder(root, visitor);
	}
	
	private void inorder(Node<E> node, Visitor<E> visitor) {
		if (node == null || visitor == null) return;
		inorder(node.left, visitor);
		visitor.visit(node.element);
		inorder(node.right, visitor);
	}
	
	/**
	 * 后续遍历（递归版）
	 * @param visitor
	 */
	public void postorderByRecursion(Visitor<E> visitor) {
		postorder(root, visitor);
	}
	
	private void postorder(Node<E> node, Visitor<E> visitor) {
		if (node == null || visitor == null) return;
		postorder(node.left, visitor);
		postorder(node.right, visitor);
		visitor.visit(node.element);
	}
	
	/**
	 * 练习1： 获得二叉树的高度（递归版和迭代版）
	 * @return
	 */
	public int heightByRecursion() {
		return heightByRecursion(root);
	}
	
	private int heightByRecursion(Node<E> node) {
		if (node == null) return 0;
		return 1 + Math.max(heightByRecursion(node.left), heightByRecursion(node.right));
	}
	
	public int height() {
		if (root == null) return 0;
		
		int height = 0;
		// 存储着每一层的元素数量
		int levelSize = 1; // 第一层只有1个元素（根节点）
		// 采用层序遍历
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			levelSize--;
			if (node.left != null) queue.offer(node.left);
			if (node.right != null) queue.offer(node.right);
			
			if (levelSize == 0) { // 意味着即将要访问下一层
				levelSize = queue.size();
				height++;
			}
		}
		
		return height;
	}
	
	/**
	 * 练习2： 判断一颗二叉树是否为完全二叉树
	 * 通过层序遍历实现
	 */
	public boolean isCompleted() {
		if (root == null) return false;
		
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		
		boolean leaf = false;
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			
			if (leaf && !node.isLeaf()) return false;
			
			if (node.left != null) {
				queue.offer(node.left);
			} else if (node.right != null) { // node.left == null && node.right != null
				return false;
			}
			
			if (node.right != null) {
				queue.offer(node.right);
			} else {
				// node.left == null && node.right == null
				// node.left != null && node.right == null
				leaf = true;
			}
		}
		return true;
	}
	
	/**
	 * 创建新节点（子类根据情况可以重写
	 * @param element
	 * @param parent
	 */
	protected Node<E> createNode(E element, Node<E> parent) {
		return new Node<E>(element, parent);
	}
	
	/**
	 * 定义内部节点
	 * @param <E>
	 */
	protected static class Node<E> {	
		public E element;
		public Node<E> parent, left, right;
		
		public Node(E element, Node<E> parent) {
			this.element = element;
			this.parent = parent;
		}
		
		public boolean isLeaf() {
			return left == null && right == null;
		}
		
		public boolean hasTwoChildren() {
			return left != null && right != null;
		}
		
		public boolean isLeftChild() {
			return parent != null && this == parent.left;
		}
		
		public boolean isRightChild() {
			return parent != null && this == parent.right;
		}
		
		/** 获取兄弟节点 */
		public Node<E> sibling() {
			if (isLeftChild()) {
				return parent.right;
			}
			
			if (isRightChild()) {
				return parent.left;
			}
			
			return null;
		}
	}
	
	/**
	 * 二叉树遍历用接口
	 * @param <E>
	 */
	@FunctionalInterface
	public static interface Visitor<E> {
		void visit(E element);
	}
	
	@Override
	public Object root() {
		return root;
	}

	@Override
	public Object left(Object node) {
		return ((Node<E>) node).left;
	}

	@Override
	public Object right(Object node) {
		return ((Node<E>) node).right;
	}

	@Override
	public Object string(Object node) {
		// ((Node<E>) node).element 会直接打印值（这里传节点是为了让子类Node可以自由重写toString来打印
		return ((Node<E>) node);
	}
}
