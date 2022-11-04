package leetcode.cn.linkedList;

/**
 * 141. 环形链表
 * 
 * 给你一个链表的头节点 head ，判断链表中是否有环
 * 
 * @author https://leetcode.cn/problems/linked-list-cycle/
 *
 */
public class _141_linked_list_cycle {
	/** 快慢指针 */
    public boolean hasCycle(ListNode head) {
    	if (head == null || head.next == null) return false;
    	
    	ListNode slow = head;
    	ListNode fast = head.next;
    	
    	while (fast != null && fast.next != null) {
    		slow = slow.next;
    		fast = fast.next.next;
    		if (fast == slow) return true;
    	}
    	
    	return false;
    }

	/* Definition for singly-linked list. */
	public class ListNode {
		int val;
		ListNode next;
		ListNode(int x) { 
			val = x;
			next = null;
		}
	}
}
