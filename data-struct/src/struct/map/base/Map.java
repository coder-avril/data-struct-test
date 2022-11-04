package struct.map.base;

/**
 * Map 映射
 * 
 * @author avril
 *
 * @param <K>
 * @param <V>
 */
public interface Map<K, V> {
	/* 获取键值对数量 */
    int size();
    
    /* 是否为空 */
    boolean isEmpty();
    
    /* 清空键值对 */
    void clear();

    /* 添加键值对 */
    V put(K key, V value);
    
    /* 通过key获取value */
    V get(K key);
    
    /* 删除key */
    V remove(K key);
    
    /* 是否包含key */
    boolean containsKey(K key);
    
    /* 是否包含value */
    boolean containsValue(V value);
    
    /* 遍历映射 */
    void traversal(Visitor<K, V> visitor);
    
    /** 访问器 */
    @FunctionalInterface
    public static interface Visitor<K, V>{
        boolean visit(K key, V value);
    }
}
