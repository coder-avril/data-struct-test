package leetcode.cn.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 226 翻转二叉树
 * 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点
 * 
 * @author https://leetcode.cn/problems/invert-binary-tree/
 *
 */
public class _226_invert_binary_tree {
	@SuppressWarnings("unused")
	private static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode() {}
		TreeNode(int val) { 
			this.val = val;
		}
		TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}
	
	/**
	 * 前序遍历版
	 * @param root
	 */
	public TreeNode invertTree_preorder(TreeNode root) {
		if (root == null) return root;
		
		TreeNode tmp = root.left;
		root.left = root.right;
		root.right = tmp;
		
		invertTree_preorder(root.left);
		invertTree_preorder(root.right);
		
		return root;
	}
	
	/**
	 * 中序遍历版
	 * @param root
	 */
	public TreeNode invertTree_inorder(TreeNode root) {
		if (root == null) return root;
		
		invertTree_inorder(root.left);
		TreeNode tmp = root.left;
		root.left = root.right;
		root.right = tmp;
		invertTree_inorder(root.left); // 注意这个地方由于交换的缘故 还是left
		
		return root;
	}
	
	/**
	 * 后序遍历版
	 * @param root
	 */
	public TreeNode invertTree_postorder(TreeNode root) {
		if (root == null) return root;
		
		invertTree_postorder(root.left);
		invertTree_postorder(root.right);
		TreeNode tmp = root.left;
		root.left = root.right;
		root.right = tmp;
		
		return root;
	}
	
	/**
	 * 层序遍历版
	 * @param root
	 */
	public TreeNode invertTree_levelorder(TreeNode root) {
		if (root == null) return root;
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			
			TreeNode tmp = node.left;
			node.left = node.right;
			node.right = tmp;
			
			if (node.left != null) queue.offer(node.left);
			if (node.right != null) queue.offer(node.right);
		}
		
		return root;
	}
}
