package algorithm.unionFind;

import algorithm.unionFind.base.AbstractUnionFind;

/**
 * QuickFind 并查集（快速查找版
 * 
 * @author avril
 *
 */
public class QuickFind extends AbstractUnionFind {
	public QuickFind() {
		this(DEFUALT_CAPACITY);
	}
	
	public QuickFind(int capacity) {
		super(capacity);
	}

	/**
	 * 查找v所属的集合（根节点）
	 * @param v
	 */
	@Override
	public int find(int v) {
		rangeCheck(v);
		return parents[v];
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
		if (p1 == p2) return; // 根节点相同 说明它们原本就是同一个集合 没必要合并
		
		/* 让 v1 所在集合的所有元素都指向 v2 的根节点 */
		for (int i = 0; i < parents.length; i++) {
			if (parents[i] == p1) {
				parents[i] = p2;
			}
		}
	}
}
