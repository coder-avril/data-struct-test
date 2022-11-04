package algorithm.unionFind;

/**
 * QuickUnionByRank 基于rank（树的高度）的优化
 * 
 * @author avril
 *
 */
public class QuickUnionByRank extends QuickUnion {
	/* 用于存放某个根节点所在树的高度是多少 比如ranks[2]=3 意思是以2为根节点的那棵树 高度为3 */
	private int[] ranks;
	
	public QuickUnionByRank() { 
		this(DEFUALT_CAPACITY);
	}
	
	public QuickUnionByRank(int capacity) {
		super(capacity);
		
		ranks = new int[capacity];
		for (int i = 0; i < ranks.length; i++) {
			/* 初始化的时候 大家都是以自己为根节点 所有默认都是1 */
			ranks[i] = 1;
		}
	}
	
	/**
	 * 查找v所属的集合（根节点）
	 * @param v
	 */
	@Override
	public int find(int v) {
		rangeCheck(v);
		return ver2_pathSpliting(v);
	}
	
	/**
	 * 基于路径压缩进行优化
	 * 每次查找的时候 让该节点到根节点之间的所有节点 都指向根节点（以此降低该路径高度 
	 * @param v
	 */
	@SuppressWarnings("unused")
	private int ver1_pathCompression(int v) {
		if (parents[v] != v) {
			parents[v] = find(parents[v]);
		}
		return parents[v];
	}
	
	/**
	 * 基于路径分裂进行优化
	 * 使路径上的每个节点都指向其祖父节点（parent的parent）
	 * @param v
	 */
	private int ver2_pathSpliting(int v) {
		while (v != parents[v]) {
			int p = parents[v];
			parents[v] = parents[parents[v]];
			v = p;
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
		
		if (ranks[p1] < ranks[p2]) { // 如果p1所在树的高度 小于 p2所在树的高度
			parents[p1] = p2; // 让高度低的p1根节点 指向p2
		} else if (ranks[p1] > ranks[p2]) {
			parents[p2] = p1;
		} else { // 高度相等的时候 谁嫁接谁都可以
			parents[p1] = p2;
			ranks[p2] += 1;
		}
	}
}
