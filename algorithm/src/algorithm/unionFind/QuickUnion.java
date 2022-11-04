package algorithm.unionFind;

import algorithm.unionFind.base.AbstractUnionFind;

/**
 * QuickUnion 并查集（快速合并版
 * 
 * @author avril
 *
 */
public class QuickUnion extends AbstractUnionFind {
	public QuickUnion() { 
		this(DEFUALT_CAPACITY);
	}
	
	public QuickUnion(int capacity) {
		super(capacity);
	}

	/**
	 * 查找v所属的集合（根节点）
	 * @param v
	 */
	@Override
	public int find(int v) {
		rangeCheck(v);
		while (v != parents[v]) {
			v = parents[v];
		}
		return v;
	}

	/**
	 * 合并v1和v2所属的集合
	 * @param v1
	 * @param v2
	 */
	@Override
	public void union(int v1, int v2) {
		int p1 = find(v1);
		int p2 = find(v2);
		if (p1 == p2) return;
		
		/* 让 v1 的根节点指向 v2 的根节点 */
		parents[p1] = p2;
	}
}
