package leetcode.cn.stack;

import java.util.HashMap;
import java.util.Stack;

/**
 * 20 有效的括号
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效
 * 
 * @author https://leetcode.cn/problems/valid-parentheses/
 *
 */
public class _20_valid_parentheses {
	private static HashMap<Character, Character> map = new HashMap<>();
	static {
		map.put('(', ')');
		map.put('{', '}');
		map.put('[', ']');
	}
	
    public boolean isValid(String s) {
    	Stack<Character> stack = new Stack<>();
    	
    	int len = s.length();
    	for (int i = 0; i < len; i++) {
    		char c = s.charAt(i);
    		if (map.containsKey(c)) { // 左括号
    			stack.push(c);
    		} else { // 右括号
    			if (stack.isEmpty()) return false;
    			if (c != map.get(stack.pop())) return false;
    		}
    	}
    	
    	return stack.isEmpty();
    }
}
