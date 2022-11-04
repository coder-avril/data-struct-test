package algorithm.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import algorithm.graph.base.Graph;
import algorithm.graph.tool.GenericUnionFind;
import algorithm.graph.tool.MinHeap;

/**
 * ListGraph 图（邻接表实现方案）
 * 
 * @author avril
 *
 * @param <V>
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class ListGraph<V, E> extends Graph<V, E> {
	/* 内置顶点map */
	private Map<V, Vertex<V, E>> vertices = new HashMap<>();
	
	/* 内置边Set */
	private Set<Edge<V, E>> edges = new HashSet<>();
	
	/* 内置比较器 */
	private Comparator<Edge<V, E>> edgeComparator = (e1, e2) -> weightManager.compare(e1.weight, e2.weight);
	
    public ListGraph() { }

    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }
	
	/* 打印（测试用） */
	public void print() {
		vertices.forEach((v, vertex) -> {
			System.out.println(v);
			System.out.println("OUT: " + vertex.outEdges);
			System.out.println("IN : " + vertex.inEdges);
			System.out.println("-----------------------------------------------");
		});
		
		edges.forEach(edge -> {
			System.out.println(edge);
		});
	}

	/**
	 * 边数
	 */
	@Override
	public int edgesSize() {
		return edges.size();
	}

	/**
	 * 顶点数
	 */
	@Override
	public int verticesSize() {
		return vertices.size();
	}

	/**
	 * 添加顶点
	 * @param v
	 */
	@Override
	public void addVertex(V v) {
		if (vertices.containsKey(v)) return;
		vertices.put(v, new Vertex<>(v));
	}

	/**
	 * 添加边
	 * @param from
	 * @param to
	 */
	@Override
	public void addEdge(V from, V to) {
		addEdge(from, to, null);
	}

	/**
	 * 添加边（带有权值）
	 * @param from
	 * @param to
	 * @param weight
	 */
	@Override
	public void addEdge(V from, V to, E weight) {
		Vertex<V, E> fromVertex = vertices.get(from);
		if (fromVertex == null) {
			fromVertex = new Vertex<>(from);
			vertices.put(from, fromVertex);
		}
		
		Vertex<V, E> toVertex = vertices.get(to);
		if (toVertex == null) {
			toVertex = new Vertex<>(to);
			vertices.put(to, toVertex);
		}
		
		Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
		edge.weight = weight;
		// 边如果存在 一般的逻辑是进行覆盖 由于是hashSet不方便 所以这里换个思维 先尝试删除旧边 再添加新的边
		if (fromVertex.outEdges.remove(edge)) {
			toVertex.inEdges.remove(edge);
			edges.remove(edge);
		}
		fromVertex.outEdges.add(edge);
		toVertex.inEdges.add(edge);
		edges.add(edge);
	}

	/**
	 * 删除顶点
	 * @param v
	 */
	@Override
	public void removeVertex(V v) {
		// 一进来就尝试删除顶点
		Vertex<V, E> vertex = vertices.remove(v);
		if (vertex == null) return;
		
		// 通过迭代器删除顶点相关联的边
		for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext();) {
			Edge<V, E> edge = iterator.next();
			edge.to.inEdges.remove(edge);
			iterator.remove(); // 将当前遍历到的元素edge从集合vertex.outEdges中删除
			edges.remove(edge);
		}
		
		for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext();) {
			Edge<V, E> edge = iterator.next();
			edge.from.outEdges.remove(edge);
			iterator.remove(); // 将当前遍历到的元素edge从集合vertex.inEdges中删除
			edges.remove(edge);
		}
	}

	/**
	 * 删除边
	 * @param from
	 * @param to
	 */
	@Override
	public void removeEdge(V from, V to) {
		Vertex<V, E> fromVertex = vertices.get(from);
		if (fromVertex == null) return;
		
		Vertex<V, E> toVertex = vertices.get(to);
		if (toVertex == null) return;
		
		Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
		// 尝试删除边
		if (fromVertex.outEdges.remove(edge)) {
			toVertex.inEdges.remove(edge);
			edges.remove(edge);
		}
	}
	
	/**
	 * 广度优先搜索（Breadth First Search，BFS）
	 * @param begin
	 * @param visitor
	 */
	@Override
	public void bfs(V begin, VertexVisitor<V> visitor) {
		if (visitor == null) return;
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return;
		
		Set<Vertex<V, E>> visitedVertices = new HashSet<>();
		Queue<Vertex<V, E>> queue = new LinkedList<>();
		queue.offer(beginVertex);
		visitedVertices.add(beginVertex); /* 顶点被放入队列的同时 就应该放入visitedVertices里面 */
		
		while (!queue.isEmpty()) {
			Vertex<V, E> vertex = queue.poll();
			if (visitor.visit(vertex.value)) return;
			
			for (Edge<V, E> edge : vertex.outEdges) {
				// 防止已经访问的顶点再次被访问
				if (visitedVertices.contains(edge.to)) continue;
				queue.offer(edge.to);
				/* 顶点被放入队列的同时 就应该放入visitedVertices里面 */
				visitedVertices.add(edge.to);
			}
		}
	}
	
	/**
	 * 深度优先搜索（Depth First Search）
	 * @param begin
	 * @param visitor
	 */
	@Override
	public void dfs(V begin, VertexVisitor<V> visitor) {
		if (visitor == null) return;
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return;
		
		/*// 递归版
		dfs(beginVertex, new HashSet<>());*/
		
		Set<Vertex<V, E>> visitedVertices = new HashSet<>();
		Stack<Vertex<V, E>> stack = new Stack<>();
		// 先访问起点
		stack.push(beginVertex);
		visitedVertices.add(beginVertex);
		// begin == beginVertex.value
		if (visitor.visit(begin)) return;
		
		while (!stack.isEmpty()) {
			// 弹出一个顶点
			Vertex<V, E> vertex = stack.pop();
			
			for (Edge<V, E> edge : vertex.outEdges) { // 1. 从outEdges中选择一条边
				if (visitedVertices.contains(edge.to)) continue; // 防止再次访问已经处理过的顶点
				
				// 2. 将选择边的from、to按顺序入栈
				stack.push(edge.from);
				stack.push(edge.to);
				// 3. 打印选择边的to
				if (visitor.visit(edge.to.value)) return;
				// 4. 将to加到已经访问的范围中
				visitedVertices.add(edge.to);
				
				break;
			}
		}
	}
	
	/**
	 * 拓扑排序（解决有向无环图的顺序问题）
	 */
	@Override
	public List<V> topologicalSort() {
		List<V> list = new ArrayList<>();
		Queue<Vertex<V, E>> queue = new LinkedList<>();
		Map<Vertex<V, E>, Integer> ins = new HashMap<>();
		
		/* 初始化（将入度为0的节点都放入队列） */
		vertices.forEach((v, vertex) -> {
			int in = vertex.inEdges.size();
			if (in == 0) {
				queue.offer(vertex);
			} else {
				ins.put(vertex, in); // 记录节点的入度信息
			}
		});
		
		while (!queue.isEmpty()) {
			Vertex<V, E> vertex = queue.poll();
			// 放入返回结果
			list.add(vertex.value);
			
			for (Edge<V, E> edge : vertex.outEdges) {
				// 少一个顶点后 该顶底的出度的顶点就会少一个入度
				int toIn = ins.get(edge.to) - 1;
				if (toIn == 0) {
					queue.offer(edge.to);
				} else {
					ins.put(edge.to, toIn);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 最小生成树
	 */
	@Override
	public Set<EdgeInfo<V, E>> mst() {
		return prim();
	}
	
	/**
	 * 最小生成树-prim算法
	 * 
	 * ◼ 横切边（Crossing Edge）：如果一个边的两个顶点，分别属于切分的两部分，这个边称为横切边
	 * ◼ 切分定理：给定任意切分，横切边中权值最小的边必然属于最小生成树
	 */
	private Set<EdgeInfo<V, E>> prim() {
		Iterator<Vertex<V, E>> it = vertices.values().iterator();
		if (!it.hasNext()) return null;
		
		Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
		Set<Vertex<V, E>> addedVertices = new HashSet<>();
		
		Vertex<V, E> vertex = it.next();
		addedVertices.add(vertex);
		MinHeap<Edge<V, E>> heap = new MinHeap<>(vertex.outEdges, edgeComparator);
		
		int edgeSize = vertices.size() - 1;
		while (!heap.isEmpty() && edgeInfos.size() < edgeSize) {
			// 找到切分 C = (S，V – S) 的最小横切边 (u0，v0) 并入集合 A，同时将 v0 并入集合 S
			Edge<V, E> edge = heap.remove();
			if (addedVertices.contains(edge.to)) continue;
			
			edgeInfos.add(edge.info());
			addedVertices.add(edge.to);
			heap.addAll(edge.to.outEdges);
		}
		
		return edgeInfos;
	}
	
	/**
	 * 最小生成树-kruskal算法
	 * 
	 * ◼ 按照边的权重顺序（从小到大）将边加入生成树中，直到生成树中含有 V – 1 条边为止（ V 是顶点数量）
	 */
	@SuppressWarnings("unused")
	private Set<EdgeInfo<V, E>> kruskal() {
		int edgeSize = vertices.size() - 1;
		if (edgeSize == -1) return null;
		
		Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
		MinHeap<Edge<V, E>> heap = new MinHeap<>(edges, edgeComparator);
		GenericUnionFind<Vertex<V, E>> uf = new GenericUnionFind<>();
		vertices.forEach((v, vertex) -> { // 初始化并查集
			uf.makeSet(vertex);
		});
		
		while (!heap.isEmpty() && edgeInfos.size() < edgeSize) {
			Edge<V, E> edge = heap.remove();
			// 若加入该边会与生成树形成环，则不加入该边
			if (uf.isSame(edge.from, edge.to)) continue;
			
			edgeInfos.add(edge.info());
			uf.union(edge.from, edge.to);
		}
		
		return edgeInfos;
	}
	
	/**
	 * 深度优先搜索（递归版
	 * @param vertex
	 * @param visitedVertices
	 */
	@SuppressWarnings("unused")
	private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertices) {
		System.out.println(vertex.value);
		visitedVertices.add(vertex);
		
		/* 类似于二叉树的前序遍历 遍历完自己后紧接着再对左右子树递归调用 */
		for(Edge<V, E> edge : vertex.outEdges) {
			if (visitedVertices.contains(edge.to)) continue;
			dfs(edge.to, visitedVertices);
		}
	}
	
	/** 顶点 */
	private static class Vertex<V, E> {
		private V value;
		Set<Edge<V, E>> inEdges = new HashSet<>();
		Set<Edge<V, E>> outEdges = new HashSet<>();
		
		/** 构造器 */
		public Vertex(V value) {
			this.value = value;
		}
		
		/* hash表里面存放的对象最好主动实现equals方法 */
		@Override
		public boolean equals(Object obj) {
			return Objects.equals(value, ((Vertex<V, E>) obj).value);
		}
		
		@Override
		public int hashCode() {
			// 可以看到value其实是泛型的 具体是什么样的hashCode 得看传进来的是什么类型 比如是Integer、String或是自定义对象都可以
			return value == null ? 0 : value.hashCode();
		}
		
		@Override
		public String toString() { // 测试用
			return value == null ? "null" : value.toString();
		}
	}
	
	/** 边 */
	private static class Edge<V, E> {
		private Vertex<V, E> from;
		private Vertex<V, E> to;
		private E weight;
		
		/** 构造器 */
		public Edge(Vertex<V, E> from, Vertex<V, E> to) {
			this.from = from;
			this.to = to;
		}
		
		/* 用于将Edge转换为EdgeInfo */
		public EdgeInfo<V, E> info() {
			return new EdgeInfo<>(from.value, to.value, weight);
		}
		
		/* hash表里面存放的对象最好主动实现equals方法 */
		@Override
		public boolean equals(Object obj) {
			Edge<V, E> edge = (Edge<V, E>) obj;
			return Objects.deepEquals(from, edge.from) && Objects.equals(to, edge.to);
		}
		
		/* 要保证equals为true的对象 它们的hashCode也得一样 所以也得主动实现 */
		@Override
		public int hashCode() {
			return from.hashCode() * 31 + to.hashCode();
		}

		@Override
		public String toString() { // 测试用
			return "Edge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
		}
	}
}
