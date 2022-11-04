package leetcode.cn.queue;

import java.util.Stack;

/**
 * 232 用栈实现队列
 * 
 * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）
 * 
 * @author https://leetcode.cn/problems/implement-queue-using-stacks/
 *
 */
public class _232_implement_queue_using_stacks {
	/* 接替模板 */
	@SuppressWarnings("unused")
	private static class MyQueue {
		/* 准备2个栈：inStack、outStack */
		private Stack<Integer> inStack = null;
		private Stack<Integer> outStatck = null;
		
	    public MyQueue() {
			this.inStack = new Stack<>();
			this.outStatck = new Stack<>();
	    }
	    
	    /**
	     * 将元素 x 推到队列的末尾（入队 enQueue）
	     * @param x
	     */
	    public void push(int x) {
	    	this.inStack.push(x);
	    }
	    
	    /**
	     * 从队列的开头移除并返回元素（出队 deQueue）
	     */
	    public int pop() {
	    	checkOutStack();
	    	return this.outStatck.pop();
	    }
	    
	    /**
	     * 返回队列开头的元素
	     */
	    public int peek() {
	    	checkOutStack();
	    	return this.outStatck.peek();
	    }
	    
	    /**
	     * 如果队列为空，返回 true ；否则，返回 false
	     */
	    public boolean empty() {
	    	return this.inStack.isEmpty() && this.outStatck.isEmpty();
	    }
	    
	    private void checkOutStack() {
	    	if (this.outStatck.isEmpty()) { // 如果 outStack 为空
	    		while (!this.inStack.isEmpty()) { // 将 inStack 所有元素逐一弹出
	    			this.outStatck.push(this.inStack.pop()); // push 到 outStack
	    		}
	    	}
	    }
	}
}
