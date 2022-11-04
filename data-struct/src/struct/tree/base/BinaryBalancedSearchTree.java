package struct.tree.base;

import java.util.Comparator;

import struct.tree.BinarySearchTree;

/**
 * BinaryBalancedSearchTree 二叉平衡搜索树
 * 
 * @author avril
 *
 * @param <E>
 */
public abstract class BinaryBalancedSearchTree<E> extends BinarySearchTree<E> {
	public BinaryBalancedSearchTree() {
		this(null);
	}
	
	public BinaryBalancedSearchTree(Comparator<E> comparator) {
		super(comparator);
	}

	/**
	 * 左旋转（为了方便理解 以AVL-PPT里面的RR-左旋转示意图为例
	 * @param grand
	 */
	protected void rotateLeft(Node<E> grand) {
		Node<E> parent = grand.right;
		Node<E> child = parent.left; // 示意图里面的T0
		grand.right = child;
		parent.left = grand;
		
		afterRotate(grand, parent, child);
	}
	
	/**
	 * 右旋转（为了方便理解 以AVL-PPT里面的LL-右旋转示意图为例
	 * @param grand
	 */
	protected void rotateRight(Node<E> grand) {
		Node<E> parent = grand.left;
		Node<E> child = parent.right; // 示意图里面的T2
		grand.left = child;
		parent.right = grand;
		
		afterRotate(grand, parent, child);
	}
	
	protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
		// 让parent成为子树的根节点
		parent.parent = grand.parent;
		if (grand.isLeftChild()) {
			grand.parent.left = parent;
		} else if (grand.isRightChild()) {
			grand.parent.right = parent;
		} else { // grand是root节点
			root = parent;
		}
		
		// 更新child的parent
		if (child != null) {
			child.parent = grand;
		}
		
		// 更新grand的parent
		grand.parent = parent;
	}
}
