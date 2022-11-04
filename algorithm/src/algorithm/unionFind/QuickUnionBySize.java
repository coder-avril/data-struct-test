package algorithm.unionFind;

/**
 * QuickUnionBySize 基于size的优化
 * 虽然性能大幅提升 但是还是有树不平衡的情况
 * 
 * @author avril
 *
 */
public class QuickUnionBySize extends QuickUnion {
	/* 用于存放某个根节点所在树里面包含了多少个元素 比如sizes[2]=3 意思是以2为根节点的那棵树 有3个子节点 */
	private int[] sizes;
	
	public QuickUnionBySize() { 
		this(DEFUALT_CAPACITY);
	}
	
	public QuickUnionBySize(int capacity) {
		super(capacity);
		
		sizes = new int[capacity];
		for (int i = 0; i < sizes.length; i++) {
			/* 初始化的时候 大家都是以自己为根节点 所有默认都是1 */
			sizes[i] = 1;
		}
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
		
		if (sizes[p1] < sizes[p2]) { // 如果p1所在树的数量 小于 p2所在树的数量
			parents[p1] = p2; // 让数量小的p1根节点 指向p2
			sizes[p2] += sizes[p1];
		} else {
			parents[p2] = p1;
			sizes[p1] += sizes[p2];
		}
	}
}
