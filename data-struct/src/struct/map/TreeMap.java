package struct.map;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import struct.map.base.Map;

/**
 * TreeMap 树形映射
 * 
 * @author avril
 *
 * @param <K, V>
 */
public class TreeMap<K, V> implements Map<K, V> {
	/* 定义红黑树特有的颜色常量 */
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	
	/* 数量 */
	private int size;
	
	/* 根节点 */
	private Node<K, V> root;
	
	/* 比较器 */
	private Comparator<K> comparator;
	
	public TreeMap(Comparator<K> comparator) {
		this.comparator = comparator;
	}
	
	public TreeMap() {
		this(null);
	}
	
	/**
	 * 包含元素的数量
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * 是否为空
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * 清空元素
	 */
	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	/**
	 * 添加键值对
	 * @return 原本key对应的值
	 */
	@Override
	public V put(K key, V value) {
		keyNotNullCheck(key);
		
		if (root == null) { // 添加的是第一个节点root
			root = new Node<>(key, value, null);
			size++;
			afterPut(root); // 添加之后的操作
			return null;
		}
		
		// 添加的不是第一个节点的话 首先得找到根节点
		Node<K, V> node = root;
		Node<K, V> parent = root; // 用于记录最终用于插入父节点
		int cmp = 0; // 用于记录插入的方向
		while (node != null) {
			cmp = compare(key, node.key);
			parent = node;
			if (cmp > 0) {
				node = node.right;
			} else if (cmp < 0) {
				node = node.left;
			} else { // 相等覆盖
				node.key = key;
				V oldValue = node.value;
				node.value = value;
				return oldValue;
			}
		}
		
		Node<K, V> newNode = new Node<>(key, value, parent);
		// 看看插入到父节点的哪个位置
		if (cmp > 0) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}
		size++;
		
		afterPut(newNode);
		return null;
	}

	/**
	 * 通过key获取value
	 * @param key
	 */
	@Override
	public V get(K key) {
		Node<K, V> node = node(key);
		return node != null ? node.value : null;
	}

	/**
	 * 删除key
	 * @param key
	 */
	@Override
	public V remove(K key) {
		return remove(node(key));
	}

	/**
	 * 是否包含key
	 * @param key
	 */
	@Override
	public boolean containsKey(K key) {
		return node(key) != null;
	}

	/**
	 * 是否包含值
	 * @param value
	 */
	@Override
	public boolean containsValue(V value) {
		if (root == null) return false;
		Queue<Node<K, V>> queue = new LinkedList<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			Node<K, V> node = queue.poll();
			if (valEquals(value, node.value)) return true;
			
			if (node.left != null) queue.offer(node.left);
			if (node.right != null) queue.offer(node.right);
		}
		
		return false;
	}

	/**
	 * 遍历映射
	 * @param visitor
	 */
	@Override
	public void traversal(Visitor<K, V> visitor) {
		if (visitor == null) return;
		traversal(root, visitor);
	}
	
	/**
	 * 遍历映射（中序遍历 数值默认从大到小 更有意义）
	 * @param node
	 * @param visitor
	 */
	private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
		if (node == null || visitor == null) return;
		
		traversal(node.left, visitor);
		if (visitor.visit(node.key,  node.value)) return;
		traversal(node.right, visitor);
	}
	
	/**
	 * 添加之后的操作
	 * @param node
	 */
	private void afterPut(Node<K, V> node) {
		Node<K, V> parent = node.parent;
		if (parent == null) { // 添加的是根节点
			black(node);
			return;
		}
		
		// 如果父节点是黑色可以直接返回
		if (isBlack(parent)) return;
		
		Node<K, V> uncle = parent.sibling();
		Node<K, V> grand = red(parent.parent); // 观察发现 后面不论哪种情况都需要将grand染成红色
		if (isRed(uncle)) { // 如果【叔父】节点是红色（这里其实相当于B树的上溢
			black(parent);
			black(uncle);
			// 把祖父节点当作是新添加的节点进行递归
			afterPut(grand);
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
	 * key的非空检查
	 * @param key
	 */
    private void keyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("the key must not be null !!");
        }
    }
    
    /**
     * 判断两个值是否相等
     * @param v1
     * @param v2
     */
    private boolean valEquals(V v1, V v2) {
    	return v1 == null ? v2 == null : v1.equals(v2);
    }
    
	/**
	 * 删除指定节点
	 * @param node
	 */
	private V remove(Node<K, V> node) {
		if (node == null) return null;
		
		V oldValue = node.value;
		
		if (node.hasTwoChildren()) { // 度为2的节点
			// 找到后继节点
			Node<K, V> s = successor(node);
			// 用后继节点的值覆盖度为2的节点的值
			node.key = s.key;
			node.value = s.value;
			// 删除后继节点
			node = s; // 这里将后继节点s赋值给node 交给后续处理来删除
		}
		
		// 删除node节点（这里的node的度必然为1或者0）
		Node<K, V> replace = node.left != null ? node.left : node.right;
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
		return oldValue;
	}
	
	/**
	 * 删除节点后的平衡操作
	 * @param node 被删除的节点 或者用于替代的节点
	 */
	private void afterRemove(Node<K, V> node) {
		// 如果删除的节点是红色 或者用以取代node的子节点是红色
		if (isRed(node)) {
			black(node);
			return;
		}
		
		Node<K, V> parent = node.parent;
		// 如果删除的是根节点
		if (parent == null) return;
		
		// 如果删除的是黑色叶子节点
		boolean left = parent.left == null || node.isLeftChild(); // 判断被删除的node是左还是右
		Node<K, V> sibling = left ? parent.right : parent.left;
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
	 * 获得指定节点的前驱
	 * @param node
	 */
	@SuppressWarnings("unused")
	private Node<K, V> predecessor(Node<K, V> node) {
		if (node == null) return null;
		
		// 前驱节点在左子树当中（left.right.right...
		Node<K, V> p = node.left;
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
	private Node<K, V> successor(Node<K, V> node) {
		if (node == null) return null;
		
		// 前驱节点在右子树当中（right.left.left...
		Node<K, V> p = node.right;
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
	 * 获取指定元素的节点
	 * @param key
	 */
	private Node<K, V> node(K key) {
		Node<K, V> node = root;
		while (node != null) {
			int cmp = compare(key, node.key);
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
	 * 左旋转（为了方便理解 以AVL-PPT里面的RR-左旋转示意图为例
	 * @param grand
	 */
    private void rotateLeft(Node<K, V> grand) {
		Node<K, V> parent = grand.right;
		Node<K, V> child = parent.left; // 示意图里面的T0
		grand.right = child;
		parent.left = grand;
		
		afterRotate(grand, parent, child);
	}
	
	/**
	 * 右旋转（为了方便理解 以AVL-PPT里面的LL-右旋转示意图为例
	 * @param grand
	 */
    private void rotateRight(Node<K, V> grand) {
		Node<K, V> parent = grand.left;
		Node<K, V> child = parent.right; // 示意图里面的T2
		grand.left = child;
		parent.right = grand;
		
		afterRotate(grand, parent, child);
	}
	
    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
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
    
	/**
	 * 比较两个元素的大小
	 * @param k1
	 * @param k2
	 */
	@SuppressWarnings("unchecked")
	private int compare(K k1, K k2) {
		if (comparator != null) {
			return comparator.compare(k1, k2);
		}
		return ((Comparable<K>)k1).compareTo(k2);
	}
	
	/**
	 * 是否为黑色
	 * @param node
	 */
	private boolean isBlack(Node<K, V> node) {
		return colorOf(node) == BLACK;
	}
	
	/**
	 * 是否为红色
	 * @param node
	 */
	private boolean isRed(Node<K, V> node) {
		return colorOf(node) == RED;
	}
	
	/**
	 * 查看节点的颜色
	 * @param node
	 */
	private boolean colorOf(Node<K, V> node) {
		return node == null ? BLACK : node.color;
	}
	
	/**
	 * 将节点染成红色
	 * @param node
	 */
	private Node<K, V> red(Node<K, V> node) {
		return color(node, RED);
	}
	
	/**
	 * 将节点染成黑色
	 * @param node
	 */
	private Node<K, V> black(Node<K, V> node) {
		return color(node, BLACK);
	}
	
	/**
	 * 对指定的节点染上指定的颜色
	 * @param node
	 * @param color
	 */
	private Node<K, V> color(Node<K, V> node, boolean color) {
		if (node == null) return node;
		node.color = color;
		return node;
	}
	
	/**
	 * 定义内部节点
	 * @param <K, V>
	 */
	private static class Node<K, V> {	
		private K key;
		private V value;
		private Node<K, V> parent, left, right;
		private boolean color = RED;
		
		public Node(K key, V value, Node<K, V> parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		@SuppressWarnings("unused")
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
		public Node<K, V> sibling() {
			if (isLeftChild()) {
				return parent.right;
			}
			
			if (isRightChild()) {
				return parent.left;
			}
			
			return null;
		}
	}
}
