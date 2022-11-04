package test;

import struct.list.linked.CircleLinkedList;

public class TestMain {
	
	public static void main(String[] args) {
		josephus();
		
	}
	
	/** 约瑟夫问题 */
	static void josephus() {
		CircleLinkedList<Integer> list = new CircleLinkedList<>();
		for (int i = 1; i <= 8; i++) {
			list.add(i);
		}
		// 重置（正式开始
		list.resetForJosephus();
		while (!list.isEmpty()) {
			list.nextForJosephus();
			list.nextForJosephus();
			System.out.println(list.removeForJosephus());
		}
	}
}
