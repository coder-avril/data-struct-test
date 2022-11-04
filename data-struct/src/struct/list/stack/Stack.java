package struct.list.stack;

import struct.list.ArrayList;
import struct.list.base.List;

/**
 * Stack 栈
 * 
 * 通过组合的方式 利用现有的线性表（这里使用动态数组）实现
 * @author avril
 *
 * @param <E>
 */
public class Stack<E> {
	/* 动态数组 */
	private List<E> list = new ArrayList<>();
	
	/**
	 * 返回栈的元素的数量
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * 栈是否为空
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	/**
	 * 将元素压入栈
	 * @param element
	 */
	public void push(E element) {
		list.add(element);
	}
	
	/**
	 * 将栈顶元素弹出
	 */
	public E pop() {
		return list.remove(list.size() - 1);
	}
	
	/**
	 * 查看栈顶元素
	 */
	public E top() {
		return list.get(list.size() - 1);
	}
	
	/**
	 * 清空栈
	 */
	public void clear() {
		list.clear();
	}
}
