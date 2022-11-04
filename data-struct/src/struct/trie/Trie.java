package struct.trie;

import java.util.HashMap;

/**
 * Trie 也叫做字典树、前缀树（Prefix Tree）、单词查找树
 * 
 * @author avril
 *
 * @param <V>
 */
public class Trie<V> {
	/* 数量 */
	private int size;
	
	/* 根节点 */
	private Node<V> root;
	
	/**
	 * 元素数量
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
	 * 清空元素
	 */
	public void clear() {
		size = 0;
		root = null;
	}
	
	/**
	 * 获取key所对应的值
	 * @param key
	 */
	public V get(String key) {
		Node<V> node = node(key);
		return node != null && node.word ? node.value : null;
	}
	
	/**
	 * 是否包含字符串
	 * @param key
	 */
	public boolean contains(String key) {
		Node<V> node = node(key);
		return node != null && node.word;
	}
	
	/**
	 * 添加字符串同时添加一些值（键值对）
	 * @param key
	 * @param value
	 */
	public V add(String key, V value) {
		keyCheck(key);
		
		// 创建根节点
		if (root == null) {
			root = new Node<>(null);
		}
		
		Node<V> node = root;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			char c = key.charAt(i);
			boolean emptyChildren = node.children == null;
			Node<V> childNode = emptyChildren ? null : node.children.get(c);
			if (childNode == null) {
				childNode = new Node<>(node);
				childNode.character = c;
				node.children = emptyChildren ? new HashMap<>() : node.children;
				node.children.put(c, childNode);
			}
			node = childNode;
		}
		
		if (node.word) { // 已经存在这个单词
			V oldValue = node.value;
			node.value = value;
			return oldValue;
		}
		
		// 新增一个单词
		node.word = true;
		node.value = value;
		size++;
		
		return null;
	}
	
	/**
	 * 删除字符串
	 * @param key
	 */
	public V remove(String key) {
		// 找到最后一个节点
		Node<V> node = node(key);
		// 如果不是单词结尾 不用做任何处理
		if (node == null || !node.word) return null;
		
		V oldValue = node.value;
		// 如果还有子节点
		if (node.children != null && !node.children.isEmpty()) {
			
			node.word = false;
			node.value = null;
			return oldValue;
		}
		
		// 如果没有子节点
		Node<V> parent = null;
		while ((parent = node.parent) != null) {
			parent.children.remove(node.character);
			if (parent.word || !parent.children.isEmpty()) break;
			node = parent;
		}
		size--;
		
		return oldValue;
	}
	
	/**
	 * 是否含有指定前缀
	 * @param prefix
	 */
	public boolean startWith(String prefix) {
		return node(prefix) != null;
	}
	
	/**
	 * 通过字符串找到对应的节点
	 * @param key
	 */
	private Node<V> node(String key) {
		keyCheck(key);
		
		Node<V> node = root;
		int len = key.length();
		for (int i = 0; i < len; i++) {
			if (node == null || node.children == null || node.children.isEmpty()) return null;
			char c = key.charAt(i);
			node = node.children.get(c);
		}
		
		return node;
	}
	
	/**
	 * 非空check
	 * @param key
	 */
	private void keyCheck(String key) {
		if (key == null || key.length() == 0) {
			throw new IllegalArgumentException("key must not be empty.");
		}
	}
	
	/**
	 * 内置节点
	 * @author avril
	 *
	 * @param <V>
	 */
	private static class Node<V> {
		private Node<V> parent;
		private HashMap<Character, Node<V>> children;
		private V value;
		private Character character;
		/* 是否为单词的结尾 */
		private boolean word;
		
		public Node(Node<V> parent) {
			this.parent = parent;
		}
	}
}
