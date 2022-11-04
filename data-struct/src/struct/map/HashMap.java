package struct.map;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import struct.map.base.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HashMap<K, V> implements Map<K, V> {
	/* 定义红黑树特有的颜色常量 */
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	
	/* 定义默认容量 */
	private static final int DEFAULT_CAPACITY = 1 << 4;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	/* 数量 */
	private int size;
	
	/* 内置hash表（数组） 里面用来存放红黑树得root 也就是最终数组里面会存在很多棵红黑树 */
	private Node<K, V>[] table;
	
	public HashMap() {
        table = new Node[DEFAULT_CAPACITY];
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
        if (size == 0) return;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
	}

	/**
	 * 添加键值对
	 * @return 原本key对应的值
	 */
	@Override
	public V put(K key, V value) {
		reSize();
		
		int index = index(key);
		// 取出index位置的红黑树根节点
		Node<K, V> root = table[index];
		if (root == null) {
			root = createNode(key, value, null);
			table[index] = root;
			size++;
			fixAfterPut(root);
			return null;
		}
		
		// 添加新的节点到红黑树上面
		Node<K, V> node = root;
		Node<K, V> parent = root;
		int cmp = 0; // 用于记录插入的方向
        K k1 = key;
        int h1 = hash(k1);
        Node<K, V> result = null;
        boolean searched = false;
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (Objects.equals(k1, k2)) {
                cmp = 0;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
            } else if (searched){ // 已经扫描了
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            } else { // searched == false; 还没有扫描 然后再根据内存地址大小决定左右
                if ((node.right != null && (result = node(node.right, k1)) != null)
                        || (node.left != null && (result = node(node.left, k1)) != null)) {
                    node = result; // 已经存在这个key
                    cmp = 0;
                } else { // 不存在这个key
                    searched = true;
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }
            
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                node.hash = h1;
                return oldValue;
            }
        } while (node != null);
		
		Node<K, V> newNode = createNode(key, value, parent);
		// 看看插入到父节点的哪个位置
		if (cmp > 0) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}
		size++;
		
		fixAfterPut(newNode);
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
	 * 是否包含value
	 * @param value
	 */
	@Override
	public boolean containsValue(V value) {
		if (size == 0) return false;
		Queue<Node<K, V>> queue = new LinkedList<>();
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) continue;
			
			queue.offer(table[i]);
			while (!queue.isEmpty()) {
				Node<K, V> node = queue.poll();
				if (Objects.equals(value, node.value)) return true;
				
				if (node.left != null) queue.offer(node.left);
				if (node.right != null) queue.offer(node.right);
			}
		}
		
		return false;
	}

	/**
	 * 遍历映射
	 * @param visitor
	 */
	@Override
	public void traversal(Visitor<K, V> visitor) {
		if (size == 0 || visitor == null) return;
		Queue<Node<K, V>> queue = new LinkedList<>();
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) continue;
			
			queue.offer(table[i]);
			while (!queue.isEmpty()) {
				Node<K, V> node = queue.poll();
				if (visitor.visit(node.key, node.value)) return;
				
				if (node.left != null) queue.offer(node.left);
				if (node.right != null) queue.offer(node.right);
			}
		}
	}
	
	/**
	 * 动态扩容
	 */
    private void reSize() {
        if (size / table.length <= DEFAULT_LOAD_FACTOR) return;
        
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null) continue;
            queue.offer(oldTable[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                // 挪动代码放到最后
                moveNode(node);
            }
        }
    }
    
    private void moveNode(Node<K, V> newNode) {
        // 重置节点的属性
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;
        
		int index = index(newNode);
		// 取出index位置的红黑树根节点
		Node<K, V> root = table[index];
		if (root == null) {
			root = newNode;
			table[index] = root;
			fixAfterPut(root);
			return;
		}
		
		// 添加新的节点到红黑树上面
		Node<K, V> node = root;
		Node<K, V> parent = root;
		int cmp = 0; // 用于记录插入的方向
        K k1 = newNode.key;
        int h1 = newNode.hash;
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
            } else {
            	cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }
            
            if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        } while (node != null);
       
		// 看看插入到父节点的哪个位置
        newNode.parent = parent;
		if (cmp > 0) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}
		fixAfterPut(newNode);
    }
	
    /**
     * 根据key计算出索引
     * @param key
     */
    private int index (K key) {
        return hash(key) & (table.length -1);
    }
    
    /**
     * 高16 XOR 低16
     * @param key
     */
    private int hash(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return hash ^ (hash >>> 16); 
    }
    
    /**
     * 根据节点算出当前红黑树对应的索引
     * @param node
     */
    private int index(Node<K, V> node) {
    	return node.hash & (table.length - 1);
    }
	
	/**
	 * 获取key所在的节点
	 * @param key
	 */
	private Node<K, V> node(K key) {
		Node<K, V> root = table[index(key)];
        return root == null ? null : node(root, key);
	}
	
    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        // 用于存储查找结果
        Node<K, V> result = null;
        int cmp = 0;
        while(node != null){
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) { // 先比较哈希值
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
                node = cmp > 0 ? node.right : node.left;
            } else if (node.right != null && (result = node(node.right, k1)) != null) {
                return result;
            } else { // 只能往左边找
                node = node.left;
            }
        }
        return null;
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
			node.hash = s.hash;
			// 删除后继节点
			node = s; // 这里将后继节点s赋值给node 交给后续处理来删除
		}
		
		// 删除node节点（这里的node的度必然为1或者0）
		Node<K, V> replace = node.left != null ? node.left : node.right;
		/* 获取当前节点所在红黑树的index */
		int index = index(node);
		if (replace != null) { // node是度为1的节点
			// 更改parent 
			replace.parent = node.parent;
			// 更改parent的left和right指向
			if (node.parent == null) { // 度为1并且是root
				table[index] = replace;
			} else if (node == node.parent.left) {
				node.parent.left = replace;
			} else { // node == node.parent.right
				node.parent.right = replace;
			}
			
			// 删除节点之后的处理
			fixAfterRemove(replace);
		} else if (node.parent == null) { // node是叶子节点并且是根节点
			table[index] = null;
		} else { // node是普通叶子节点
			if (node == node.parent.left) {
				node.parent.left = null;
			} else { // node == node.parent.right
				node.parent.right = null;
			}
			
			// 删除节点之后的处理
			fixAfterRemove(node);
		}
		
		size--;
		return oldValue;
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
	 * 添加之后的操作
	 * @param node
	 */
	private void fixAfterPut(Node<K, V> node) {
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
			fixAfterPut(grand);
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
	 * 
	 * @param willNode
	 * @param removedNode
	 */
	protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) {}
	
	/**
	 * 删除节点后的平衡操作
	 * @param node 被删除的节点 或者用于替代的节点
	 */
	protected void fixAfterRemove(Node<K, V> node) {
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
					fixAfterRemove(parent);
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
					fixAfterRemove(parent);
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
		} else { // grand是root节点（当前节点所在红黑树的根节点）
			table[index(grand)] = parent; // 其实参数的3个节点都可以 因为他们都在一棵树上
		}
		
		// 更新child的parent
		if (child != null) {
			child.parent = grand;
		}
		
		// 更新grand的parent
		grand.parent = parent;
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
	 * 创建新节点
	 * @param key
	 * @param value
	 * @param parent
	 */
    protected Node<K, V> createNode(K key, V value , Node<K, V> parent) {
        return new Node<K, V>(key, value, parent);
    }

	/**
	 * 定义内部节点
	 * @param <K, V>
	 */
	protected static class Node<K, V> {	
		public K key;
		public V value;
		public int hash;
		public Node<K, V> parent, left, right;
		public boolean color = RED;
		
		public Node(K key, V value, Node<K, V> parent) {
			this.key = key;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
			this.value = value;
			this.parent = parent;
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
