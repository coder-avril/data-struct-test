package algorithm.unionFind.base;

/**
 * AbstractUnionFind 抽象并查集
 * 
 * @author avril
 *
 */
public abstract class AbstractUnionFind implements UnionFind {
	/* 内置整型数组 */
	protected int[] parents;
	/* 默认容量 */
	protected final static int DEFUALT_CAPACITY = 10;
	
	protected AbstractUnionFind(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("capacity must be >= 0");
		}
		
		parents = new int[capacity];
		/* 初始化时，每个元素各自属于一个单元素集合 */
		for (int i = 0; i < parents.length; i++) {
			parents[i] = i;
		}
	}
	
	/**
	 * 检查v1和v2是否属于同一个集合
	 * @param v1
	 * @param v2
	 */
	@Override
	public boolean isSame(int v1, int v2) {
		return find(v1) == find(v2);
	}
	
	/**
	 * 边界检查
	 * @param v
	 */
	protected void rangeCheck(int v) {
		if (v < 0 || v >= parents.length) {
			throw new IllegalArgumentException("v is out of bounds!");
		}
	}
}
