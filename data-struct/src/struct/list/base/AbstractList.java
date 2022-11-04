package struct.list.base;

/**
 * Dynamic Array 动态数组
 * @author avril
 *
 * @param <E>
 */
public abstract class AbstractList<E> implements List<E> {
	
	// 初始化时的默认容量
	public static final int DEFAULT_CAPACIT = 10;
	
	// 索引不存在时的返回值	
	public static final int NO_INDEX = -1;
	
	// 数组的元素数量
	protected int size = 0;

	/**
	 * 数组元素的数量
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * 是否为空
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 是否包含当前元素
	 * @param element
	 */
	@Override
	public boolean contains(E element) {
		return indexOf(element) != NO_INDEX;
	}
	
	/**
	 * 添加当前元素到数组的末尾
	 * @param element
	 */
	@Override
	public void add(E element) {
		// 往数组的末尾添加的话，索引的位置正好等于数组的大小
		add(size, element);
	}

	/**
	 * 索引的边界检查
	 *   -注意添加的场合、有可能会往末尾追加、所以允许【index = size】的情况
	 * @param index
	 * @param isAdd
	 */
	protected void legalIndexCheck(int index, boolean isAdd) {
		if (isAdd) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("size = " + size + ", index = " + index + "!");
		} else {
			if (index < 0 || index > size - 1)
				throw new IndexOutOfBoundsException("size = " + size + ", index = " + index + "!");
		}
	}

}
