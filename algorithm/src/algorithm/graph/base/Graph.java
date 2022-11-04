package algorithm.graph.base;

import java.util.List;
import java.util.Set;

/**
 * Graph 图
 * 
 * @author avril
 *
 * @param <V>
 * @param <E>
 */
public abstract class Graph<V, E> {
	/* 权值管理器 */
	protected WeightManager<E> weightManager;
	
    /** 无权图 */
    public Graph() {}
    
    /** 有权图 */
    public Graph(WeightManager<E> weightManager) {
        this.weightManager = weightManager;
    }
	
	/** 边数 */
	public abstract int edgesSize();
	
	/** 顶点数 */
	public abstract int verticesSize();
	
	/** 添加顶点 */
	public abstract void addVertex(V v);
	
	/** 添加边 */
	public abstract void addEdge(V from, V to);
	
	/** 添加边（带有权值） */
	public abstract void addEdge(V from, V to, E weight);
	
	/** 删除顶点 */
	public abstract void removeVertex(V v);
	
	/** 删除边 */
	public abstract void removeEdge(V from, V to);
	
	/** 广度优先搜索（Breadth First Search，BFS），又称为宽度优先搜索、横向优先搜索 */
	public abstract void bfs(V begin, VertexVisitor<V> visitor);
	
	/** 深度优先搜索（Depth First Search） */
	public abstract void dfs(V begin, VertexVisitor<V> visitor);
	
	/** 拓扑排序（解决有向无环图的顺序问题） */
	public abstract List<V> topologicalSort();
	
	/** 最小生成树 */
	public abstract Set<EdgeInfo<V, E>> mst();
	
	/** 最小生成树的边信息 */
	public static class EdgeInfo<V, E> {
		private V from;
		private V to;
		private E weight;
		public EdgeInfo(V from, V to, E weight) {
			this.from = from;
			this.to = to;
			this.weight = weight;
		}

        public V getFrom() {
            return from;
        }

        public void setFrom(V from) {
            this.from = from;
        }

        public V getTo() {
            return to;
        }

        public void setTo(V to) {
            this.to = to;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "EdgeInfo{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }
	
    /** 对有权图的权值进行管理 */
    public interface WeightManager<E> {
    	/* 比较权值的大小 */
        int compare(E w1, E w2);
        
        /* 为最短路径服务（两个权值相加） */
        E add(E w1, E w2);
    }
	
	@FunctionalInterface
	public interface VertexVisitor<V> {
		boolean visit(V v);
	}
}
