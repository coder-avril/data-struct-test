package test;

import algorithm.bloomFilter.BloomFilter;
import algorithm.graph.ListGraph;

public class Test {
	public static void main(String[] args) {
		ListGraph<String, Integer> graph = new ListGraph<>();
		graph.addEdge("V1", "V0", 9);
		graph.addEdge("V1", "V2", 3);
		graph.addEdge("V2", "V0", 2);
		graph.addEdge("V2", "V3", 5);
		graph.addEdge("V3", "V4", 1);
		graph.addEdge("V0", "V4", 6);
		
		graph.removeVertex("V0");
		graph.print();
		
		BloomFilter<Integer> bf = new BloomFilter<>(1_00_0000, 0.01);
		for (int i = 1; i <= 50; i++) {
			bf.put(i);
		}
		for (int i = 1; i <= 50; i++) {
			System.out.println(bf.contains(i));
		}
	}
}
