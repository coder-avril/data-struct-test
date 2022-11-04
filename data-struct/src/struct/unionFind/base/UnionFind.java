package struct.unionFind.base;

/**
 * UnionFind 并查集
 * 
 * @author avril
 *
 * @param <V>
 */
public interface UnionFind<V> {
	/** 初始化加进来的自定义对象 使其成为集合 */
	void makeSet(V v);
	
	/** 查找v所属的集合（根节点） */
	V find(V v);
	
	/** 合并v1和v2所属的集合 */
	void union(V v1, V v2);
	
	/** 检查v1和v2是否属于同一个集合 */
	boolean isSame(V v1, V v2);
}
