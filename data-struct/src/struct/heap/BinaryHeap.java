package struct.heap;

import java.util.Comparator;

import printer.BinaryTreeInfo;
import struct.heap.base.AbstractHeap;
import struct.heap.base.Heap;

/**
 * 二叉堆（默认大顶堆　任意节点的值总是 ≥ 子节点的值）
 * 另外可以在创建的时候 通过比较器的策略 来将二叉堆变为小顶堆
 * 
 * @author avril
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class BinaryHeap<E> extends AbstractHeap<E> implements Heap<E>, BinaryTreeInfo {
    
    private E[] elements = null;

    private static final int DEFAULT_CAPACITY = 10;
    
    public BinaryHeap(E[] elements, Comparator<E> comparator) {
        super(comparator);
        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            size = elements.length;
            int capcity = Math.max(elements.length, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capcity];
            for (int i = 0; i < elements.length; i++) {
                this.elements[i] = elements[i];
            }
            heapify();
        }
    }

    public BinaryHeap(Comparator<E> comparator) {
        this(null, comparator);
    }
    
    public BinaryHeap(E[] elements) {
        this(elements, null);
    }

    public BinaryHeap() {
        this(null, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(E element) {
        elementEmptyCheck(element);
        ensureCapacity(size + 1);
        // 将元素添加到末尾后让size+1
        elements[size++] = element; 
        // 让添加的元素上滤
        shiftUp(size -1);
    }

    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    @Override
    public E remove() {
        emptyCheck();
        E root = elements[0];
        // size -1
        int lastIndex = --size; 
        elements[0] = elements[lastIndex];
        elements[lastIndex] = null;
        
        shiftDown(0);
        return root;
    }

    @Override
    public E replace(E element) {
        elementEmptyCheck(element);
        E root = null;
        if (size == 0) {
            elements[0] = element;
            size++;
        } else {
            // 删除堆顶元素后下滤
            root = elements[0];
            elements[0] = element;
            shiftDown(0);
        }
        return root;
    }
    
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;
        
        // 容量不够时，自动扩容到原来的1.5倍
        int newCapacity = oldCapacity + oldCapacity >> 1; 
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < elements.length; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }
    
    /**
     * 让指定的index上滤
     * @param index
     */
    private void shiftUp(int index) {
        E element = elements[index];
        while(index > 0) {
            int pIndex = (index - 1) >> 1; 
            E pElement = elements[pIndex];
            if (compare(element, pElement) <= 0) break;
            
            elements[index] = pElement;
            index = pIndex;
        }
        elements[index] = element;
    }
    
    /**
     * 让指定的index下滤
     * @param index
     */
    private void shiftDown(int index) {
        E element = elements[index];
        // 第一个叶子节点的index === 非叶子节点的数量
        int half = size >> 1;
        while (index < half) { // 必须保证index位置是非叶子节点
            // index的节点有2种情况：1.只有左子节点 2.同时有左右子节点
            // 默认用左子节点的索引
            int cIndex = (index << 1) + 1;
            E cElement = elements[cIndex];
            int rightIndex = cIndex + 1;
            // 选出左右节点最大的那个
            if (rightIndex < size && compare(elements[rightIndex], cElement) > 0) {
                cElement = elements[cIndex=rightIndex];
            }
            
            if (compare(element, cElement) >= 0) break;
            // 将子节点存放到index位置
            elements[index] = cElement;
            // 重新设置index
            index = cIndex;
        }
        elements[index] = element;
    }
    
    /**
     * 自下而上的下滤（类似删除的后半部分）
     */
    private void heapify() {
        for (int i = (size >> 1) -1; i >= 0; i--) {
            shiftDown(i);
        }
    }
    
    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("heap is empty!");
        }
    }
    
    private void elementEmptyCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element is null!");
        }
    }

    @Override
    public Object root() {
        return 0;
    }

    @Override
    public Object left(Object node) {
        int index = ((int)node << 1) + 1;
        return index >= size ? null : index;
    }

    @Override
    public Object right(Object node) {
        int index = ((int)node << 1) + 2;
        return index >= size ? null : index;
    }

    @Override
    public Object string(Object node) {
        return elements[(int)node];
    }

}
