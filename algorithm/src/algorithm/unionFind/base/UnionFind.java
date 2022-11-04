package algorithm.unionFind.base;

/**
 * UnionFind 并查集
 * 
 * 这里使用整型演示 如果想用自定义类型的话 可以尝试以下方案
 *  方案一：通过一些方法将自定义类型转为整型后使用并查集（比如生成哈希值）
 *  方案二：使用链表+映射（Map）
 *  
 * @author avril
 *
 */
public interface UnionFind {
	/** 查找v所属的集合（根节点） */
	int find(int v);
	
	/** 合并v1和v2所属的集合 */
	void union(int v1, int v2);
	
	/** 检查v1和v2是否属于同一个集合 */
	boolean isSame(int v1, int v2);
}
