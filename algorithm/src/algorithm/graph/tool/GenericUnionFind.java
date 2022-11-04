package algorithm.graph.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 并查集（通用版）
 * 
 * @author avril
 *
 * @param <V>
 */
public class GenericUnionFind<V> {
	/* 内置存储节点的Map */
	private Map<V, Node<V>> nodes = new HashMap<>();
	
	/**
	 * 初始化加进来的自定义对象 使其成为集合
	 * @param v
	 */
	public void makeSet(V v) {
		if (nodes.containsKey(v)) return;
		nodes.put(v, new Node<>(v));
	}
	
	/**
	 * 查找v所属的集合（根节点）
	 * @param v
	 */
	public V find(V v) {
		Node<V> node = findNode(v);
		return node == null ? null : node.value;
	}
	
	/**
	 * 找到v的根节点
	 * @param v
	 */
	private Node<V> findNode(V v) {
		Node<V> node = nodes.get(v);
		if (node == null) return null;
		
		/* 路径分裂 */
		while (!Objects.equals(node.value, node.parent.value)) {
			Node<V> p = node.parent;
			node.parent = node.parent.parent;
			node = p;
		}
		return node;
	}
	
	/**
	 * 合并v1和v2所属的集合
	 * @param v1
	 * @param v2
	 */
	public void union(V v1, V v2) {
		Node<V> p1 = findNode(v1);
		Node<V> p2 = findNode(v2);
		if (p1 == null || p2 == null) return;
		if (Objects.equals(p1.value, p2.value)) return;
		
		if (p1.rank < p2.rank) { // 如果p1所在树的高度 小于 p2所在树的高度
			p1.parent = p2; // 让高度低的p1根节点 指向p2
		} else if (p1.rank > p2.rank) {
			p2.parent = p1;
		} else { // 高度相等的时候 谁嫁接谁都可以
			p1.parent = p2;
			p2.rank += 1;
		}
	}
	
	/**
	 * 检查v1和v2是否属于同一个集合
	 * @param v1
	 * @param v2
	 * @return
	 */
	public boolean isSame(V v1, V v2) {
		return Objects.equals(find(v1), find(v2));
	}
	
	private static class Node<V> {
		private V value;
		/* 父节点指针 默认指向自己 */
		private Node<V> parent = this;
		/* 节点高度 默认是1 */
		private int rank = 1;
		
		public Node(V value) {
			this.value = value;
		}
	}
}
