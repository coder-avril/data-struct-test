package leetcode.cn.linkedList;

/**
 * 206. 反转链表
 * 
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表
 * 
 * @author https://leetcode.cn/problems/reverse-linked-list/
 */
public class _206_reverse_linked_list {
	/** 递归版（需要一点想象力）  */
	public ListNode reverseList1(ListNode head) {
		// 如果传进来的链表是空或者只有1个元素的时候 那就是自己 没必要反转了
		if (head == null || head.next == null) return head;
		
		ListNode newHead = reverseList1(head.next);
		head.next.next = head;
		head.next = null;
		
		return newHead;
    }
	
	/** 迭代版（头插法 也需要一点想象力）  */
	public ListNode reverseList2(ListNode head) {
		// 如果传进来的链表是空或者只有1个元素的时候 那就是自己 没必要反转了
		if (head == null || head.next == null) return head;
		
		ListNode newHead = null;
		while (head != null) {
			ListNode tmp = head.next;
			head.next = newHead;
			newHead = head;
			head = tmp;
		}
		
		return newHead;
    }
	
	/* Definition for singly-linked list. */
	public class ListNode {
		int val;
		ListNode next;
		ListNode() {}
		ListNode(int val) {
			this.val = val;
		}
		ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}
}
