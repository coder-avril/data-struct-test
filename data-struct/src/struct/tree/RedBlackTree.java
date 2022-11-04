package struct.tree;

import java.util.Comparator;

import struct.tree.base.BinaryBalancedSearchTree;

/**
 * RedBlackTree 红黑树
 * 
 * ◼必须满足以下 5 条性质
 * 1. 节点是 RED 或者 BLACK
 * 2. 根节点是 BLACK
 * 3. 叶子节点（外部节点，空节点）都是 BLACK
 * 4. RED 节点的子节点都是 BLACK
 *    ✓ RED 节点的 parent 都是 BLACK
 *    ✓ 从根节点到叶子节点的所有路径上不能有 2 个连续的 RED 节点
 * 5. 从任一节点到叶子节点的所有路径都包含相同数目的 BLACK 节点
 * 
 * @author avril
 *
 * @param <E>
 */
public class RedBlackTree<E> extends BinaryBalancedSearchTree<E> {
	/* 定义红黑树特有的颜色常量 */
	private static final boolean RED = false;
	private static final boolean BLACK = true;

	public RedBlackTree() {
		this(null);
	}
	
	public RedBlackTree(Comparator<E> comparator) {
		super(comparator);
	}
	
	/**
	 * 添加节点后的平衡操作
	 * @param node
	 */
	@Override
	protected void afterAdd(Node<E> node) {
		Node<E> parent = node.parent;
		if (parent == null) { // 添加的是根节点
			black(node);
			return;
		}
		
		// 如果父节点是黑色可以直接返回
		if (isBlack(parent)) return;
		
		Node<E> uncle = parent.sibling();
		Node<E> grand = red(parent.parent); // 观察发现 后面不论哪种情况都需要将grand染成红色
		if (isRed(uncle)) { // 如果【叔父】节点是红色（这里其实相当于B树的上溢
			black(parent);
			black(uncle);
			// 把祖父节点当作是新添加的节点进行递归
			afterAdd(grand);
			return;
		}
		
		// 如果【叔父】节点不是红色
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				black(parent);
			} else { // LR
				black(node);
				rotateLeft(parent);
			}
			rotateRight(grand);
		} else { // R
			if (node.isLeftChild()) { // RL
				black(node);
				rotateRight(parent);
			} else { // RR
				black(parent);
			}
			rotateLeft(grand);
		}
	}
	
	/**
	 * 删除节点后的平衡操作
	 * @param node 被删除的节点 或者用于替代的节点
	 */
	@Override
	protected void afterRemove(Node<E> node) {
		// 如果删除的节点是红色 或者用以取代node的子节点是红色
		if (isRed(node)) {
			black(node);
			return;
		}
		
		Node<E> parent = node.parent;
		// 如果删除的是根节点
		if (parent == null) return;
		
		// 如果删除的是黑色叶子节点
		boolean left = parent.left == null || node.isLeftChild(); // 判断被删除的node是左还是右
		Node<E> sibling = left ? parent.right : parent.left;
		if (left) { // 被删除的节点在左边 兄弟节点在右边
			/** 直接copy分歧内容 利用对称性 进行修改 */
			if (isRed(sibling)) { // 兄弟节点是红色
				black(sibling);
				red(parent);
				rotateLeft(parent);
				// 更换兄弟
				sibling = parent.right;
			}
			
			// 来到这里的兄弟节点必然是黑色
			if (isBlack(sibling.left) && isBlack(sibling.right)) { // 兄弟节点没有红色子节点 父节点要向下跟兄弟节点合并
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if (parentBlack) { // 父节点原来就是黑色的场合 会连锁导致父节点下溢 可以将父节点当作一个要删除的黑【叶子】来对待
					afterRemove(parent);
				}
			} else { // 兄弟节点至少有1个红色子节点（可以向兄弟节点借元素
				if (isBlack(sibling.right)) {
					rotateRight(sibling);
					sibling = parent.right;
				}
				
				color(sibling, colorOf(parent));
				black(sibling.right);
				black(parent);
				rotateLeft(parent);
			}
		} else { // 被删除的节点在右边 兄弟节点在左边
			if (isRed(sibling)) { // 兄弟节点是红色
				black(sibling);
				red(parent);
				rotateRight(parent);
				// 更换兄弟
				sibling = parent.left;
			}
			
			// 来到这里的兄弟节点必然是黑色
			if (isBlack(sibling.left) && isBlack(sibling.right)) { // 兄弟节点没有红色子节点 父节点要向下跟兄弟节点合并
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if (parentBlack) { // 父节点原来就是黑色的场合 会连锁导致父节点下溢 可以将父节点当作一个要删除的黑【叶子】来对待
					afterRemove(parent);
				}
			} else { // 兄弟节点至少有1个红色子节点（可以向兄弟节点借元素
				if (isBlack(sibling.left)) { // 兄弟节点的左边是黑色 兄弟要先旋转
					rotateLeft(sibling);
					sibling = parent.left; // 这里要注意兄弟节点在旋转后 需要更换 不然无法适用后面的代码
				}
				
				color(sibling, colorOf(parent));
				black(sibling.left);
				black(parent);
				rotateRight(parent);
			}
		}
	}
	
	/**
	 * 是否为黑色
	 * @param node
	 */
	private boolean isBlack(Node<E> node) {
		return colorOf(node) == BLACK;
	}
	
	/**
	 * 是否为红色
	 * @param node
	 */
	private boolean isRed(Node<E> node) {
		return colorOf(node) == RED;
	}
	
	/**
	 * 查看节点的颜色
	 * @param node
	 */
	private boolean colorOf(Node<E> node) {
		return node == null ? BLACK : ((RBNode<E>) node).color;
	}
	
	/**
	 * 将节点染成红色
	 * @param node
	 */
	private Node<E> red(Node<E> node) {
		return color(node, RED);
	}
	
	/**
	 * 将节点染成黑色
	 * @param node
	 */
	private Node<E> black(Node<E> node) {
		return color(node, BLACK);
	}
	
	/**
	 * 对指定的节点染上指定的颜色
	 * @param node
	 * @param color
	 */
	private Node<E> color(Node<E> node, boolean color) {
		if (node == null) return node;
		((RBNode<E>) node).color = color;
		return node;
	}
	
	/**
	 * 创建红黑树的新节点
	 * @param element
	 * @param parent
	 */
	@Override
	protected Node<E> createNode(E element, Node<E> parent) {
		return new RBNode<>(element, parent);
	}
	
	/**
	 * 定义RedBlackTree内部节点
	 * @param <E>
	 */
	private static class RBNode<E> extends Node<E> {
		/* 建议新添加的节点默认为 RED，这样能够让红黑树的性质尽快满足 */
		private boolean color = RED;
		
		public RBNode(E element, Node<E> parent) {
			super(element, parent);
		}
		
		@Override
		public String toString() {
			String st = color == RED ? "R_" : "";
			return st + element;
		}
	}
}
