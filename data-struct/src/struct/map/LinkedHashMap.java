package struct.map;

import java.util.Objects;

/**
 * LinkedHashMap
 * @author avril
 *
 * @param <K, V>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LinkedHashMap<K, V> extends HashMap<K, V> {
    /* 头尾指针 */
    private LinkedNode<K, V> first = null;
    private LinkedNode<K, V> last = null;

    @Override
    public void clear() {
        super.clear();
        first = null;
        last = null;
    }
    
    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        LinkedNode<K, V> node = first;
        while (node != null) {
            if(visitor.visit(node.key, node.value)) return;
            node = node.next;
        }
    }
    
    @Override
    public boolean containsValue(V value) {
        LinkedNode<K, V> node = first;
        while (node != null) {
            if(Objects.equals(value, node.value)) return true;
            node = node.next;
        }
        return false;
    }
    
    @Override
    protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) {
        LinkedNode<K, V> node1 = (LinkedNode<K, V>)willNode;
        LinkedNode<K, V> node2 = (LinkedNode<K, V>)removedNode;
        if (node1 != node2) { 
            LinkedNode<K, V> temp = node1.prev;
            node1.prev = node2.prev;
            node2.prev = temp;
            if (node1.prev == null) {
                first = node1;
            } else {
                node1.prev.next = node1;
            }
            if (node2.prev == null) {
                first = node2;
            } else {
                node2.prev.next = node2;
            }

            temp = node1.next;
            node1.next = node2.next;
            node2.next = temp;
            if (node1.next == null) {
                last = node1;
            } else {
                node1.next.prev = node1;
            }
            if (node2.next == null) {
                last = node2;
            } else {
                node2.next.prev = node2;
            }
        }
        
        LinkedNode<K, V> prev = node2.prev;
        LinkedNode<K, V> next = node2.next;
        if (prev == null) { //first
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) { //last
            last = prev;
        } else {
            next.prev = prev;
        }
    }

    @Override
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        LinkedNode<K, V> node = new LinkedNode(key, value, parent);
        if (first == null) {
            first = last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }
        return node;
    }
    
    private static class LinkedNode<K, V> extends Node<K, V> {
        LinkedNode<K, V> prev = null;
        LinkedNode<K, V> next = null;
        
        public LinkedNode(K key, V value, Node<K, V> parent) {
            super(key, value, parent);
        }
    }
}
