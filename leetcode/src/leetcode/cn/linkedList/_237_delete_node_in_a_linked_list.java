package leetcode.cn.linkedList;

/**
 * 237. 删除链表中的节点
 * 
 * 输入：head = [4,5,1,9], node = 5
 * 输出：[4,1,9]
 * 解释：指定链表中值为 5 的第二个节点，那么在调用了你的函数之后，该链表应变为 4 -> 1 -> 9
 * 
 * @author https://leetcode.cn/problems/delete-node-in-a-linked-list/
 */
public class _237_delete_node_in_a_linked_list {
	public void deleteNode(ListNode node) {
		// 1. 让后面的节点的值 覆盖掉自己的值
		node.val = node.next.val;
		// 2. 让自己的next 指向后面节点的next
		node.next = node.next.next;
    }
	
	/* Definition for singly-linked list. */
	public class ListNode {
		int val;
		ListNode next;
		ListNode(int x) { 
			val = x;
		}
	}
}
