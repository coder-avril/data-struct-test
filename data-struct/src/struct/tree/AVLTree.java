package struct.tree;

import java.util.Comparator;

import struct.tree.base.BinaryBalancedSearchTree;

/**
 * AVLTree 
 * 
 * AVL 取名于两位发明者的名字 G. M. Adelson-Velsky 和 E. M. Landis（来自苏联的科学家）
 * 
 * @author avril
 *
 * @param <E>
 */
public class AVLTree<E> extends BinaryBalancedSearchTree<E> {
	public AVLTree() {
		this(null);
	}
	
	public AVLTree(Comparator<E> comparator) {
		super(comparator);
	}
	
	/**
	 * 定义AVL内部节点
	 * @param <E>
	 */
	private static class AVLNode<E> extends Node<E> {
		// 新添加的节点肯定是叶子节点 所以初始时都是1
		private int height = 1;
		
		public AVLNode(E element, Node<E> parent) {
			super(element, parent);
		}
		
		/** 获取平衡因子 */
		public int balanceFactor() {
			int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
			int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
			
			return leftHeight - rightHeight;
		}
		
		/** 更新自身高度 */
		public void updateHeight() {
			int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
			int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
			height = 1 + Math.max(leftHeight, rightHeight);
		}
		
		/** 返回比较高的子节点 */
		public Node<E> tallerChild() {
			int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
			int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
			if (leftHeight > rightHeight) return left;
			if (leftHeight < rightHeight) return right;
			
			// 当相等的时候 返回跟父节点同方向的子节点（其实左右都可以 这里只是这么规定
			return isLeftChild() ? left : right;
		}
	}
	
	/**
	 * 添加节点后的平衡操作
	 * @param node
	 */
	@Override
	protected void afterAdd(Node<E> node) {
		while ((node = node.parent) != null) {
			if (isBalanced(node)) {
				// 更新高度
				updateHeight(node);
			} else {
				// 恢复平衡（也可以使用reBalanceByLeftRightRotate(Node<E>)
				reBalance(node); // 这里的node其实是第一个不平衡的祖先节点
				// 整棵树恢复平衡
				break;
			}
		}
	}
	
	/**
	 * 判断节点是否平衡
	 * @param node
	 */
	private boolean isBalanced(Node<E> node) {
		// 平衡因子是否小于等于1
		return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
	}
	
	/**
	 * 更新节点高度
	 * @param node
	 */
	private void updateHeight(Node<E> node) {
		((AVLNode<E>) node).updateHeight();
	}
	
	/**
	 * 恢复节点平衡
	 * @param grand 高度最低的那个不平衡节点
	 */
	private void reBalance(Node<E> grand) {
		// 可以发现相对于Grand Parent和Node都是更高的子节点
		Node<E> parent = ((AVLNode<E>) grand).tallerChild();
		Node<E> node = ((AVLNode<E>) parent).tallerChild();
		
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				rotateRight(grand);
			} else { // LR
				rotateLeft(parent);
				rotateRight(grand);
			}
		} else { // R
			if (node.isLeftChild()) { // RL
				rotateRight(parent);
				rotateLeft(grand);
			} else { // RR
				rotateLeft(grand);
			} 
		}
	}
	
	@Override
	protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
		super.afterRotate(grand, parent, child);
		// 更新高度
		updateHeight(grand);
		updateHeight(parent);
	}
	
	/**
	 * 恢复平衡（统一旋转版 这其实是另外一种思路）
	 * @param grand
	 */
	@SuppressWarnings("unused")
	private void reBalance2(Node<E> grand) {
		// 可以发现相对于Grand Parent和Node都是更高的子节点
		Node<E> parent = ((AVLNode<E>) grand).tallerChild();
		Node<E> node = ((AVLNode<E>) parent).tallerChild();
		
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				rotate(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
			} else { // LR
				rotate(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
			}
		} else { // R
			if (node.isLeftChild()) { // RL
				rotate(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);
			} else { // RR
				rotate(grand, grand.left, grand, parent.left, parent, node.left, node, node.right);
			} 
		}
	}
	
	/**
	 * 统一旋转
	 * @param r 原本的根节点
	 * @param a 左子树的左节点
	 * @param b 左子树的根节点
	 * @param c 左子树的右节点
	 * @param d 根节点
	 * @param e 右子树的左节点
	 * @param f 右子树的根节点
	 * @param g 右子树的右节点
	 */
	private void rotate(Node<E> r, 
			Node<E> a, Node<E> b, Node<E> c,
			Node<E> d,
			Node<E> e, Node<E> f, Node<E> g) {
		// 让d成为这个子树的根节点
		d.parent = r.parent;
		if (r.isLeftChild()) {
			r.parent.left = d;
		} else if (r.isRightChild()) {
			r.parent.right = d;
		} else {
			root = d;
		}
		
		// a-b-c
		b.left = a;
		if (a != null) {
			a.parent = b;
		}
		b.right = c;
		if (c != null) {
			c.parent = b;
		}
		updateHeight(b); // b的左右子树发生改变 所以需要重新计算高度
		
		// e-f-g
		f.left = e;
		if (e != null) {
			e.parent = f;
		}
		f.right = g;
		if (g != null) {
			g.parent = f;
		}
		updateHeight(f); // f的左右子树发生改变 所以需要重新计算高度
		
		// b-d-f
		d.left = b;
		b.parent = d;
		d.right = f;
		f.parent = d;
		updateHeight(d); // d的左右子树发生改变 所以需要重新计算高度	
	}
	
	/**
	 * 删除节点后的平衡操作
	 * @param node 被删除的节点或者用于替代的节点
	 */
	@Override
	protected void afterRemove(Node<E> node) {
		while ((node = node.parent) != null) {
			if (isBalanced(node)) {
				// 更新高度
				updateHeight(node);
			} else {
				// 恢复平衡
				reBalance(node);
			}
		}
	}
	
	/**
	 * 创建AVL树的新节点
	 * @param element
	 * @param parent
	 */
	@Override
	protected Node<E> createNode(E element, Node<E> parent) {
		return new AVLNode<>(element, parent);
	}
}
