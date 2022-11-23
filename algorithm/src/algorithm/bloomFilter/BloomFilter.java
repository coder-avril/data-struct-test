package algorithm.bloomFilter;

/**
 * BloomFilter 布隆过滤器
 * 
 * 它是一个空间效率高的概率型数据结构，可以用来告诉你：一个元素一定不存在或者可能存在
 * 它实质上是一个很长的二进制向量和一系列随机映射函数（Hash函数）
 * 
 * ◼ 优缺点
 * 优点：空间效率和查询时间都远远超过一般的算法
 * 缺点：有一定的误判率、删除困难
 * 
 * ◼ 常见应用
 * 网页黑名单系统、垃圾邮件过滤系统、爬虫的网址判重系统、解决缓存穿透问题
 * 
 * @author avril
 *
 * @param <T>
 */
public class BloomFilter<T> {
	/* 二进制向量的长度（一共有多少个二进制位） */
	private int bitSize;
	
	/* 二进制向量（一个long有8byte-64bit */
	private long[] bits;
	
	/* 哈希函数的个数 */
	private int hashSize;

	/**
	 * 构造器
	 * 
	 * @param n 数据规模
	 * @param p 误判率，取值范围是(0, 1)
	 */
	public BloomFilter(int n, double p) {
		if (n <= 0 || p <= 0 || p >= 1) {
			throw new IllegalArgumentException("n should be > 0 and p should b bettween 0 and 1");
		}
		// 得到以常量e为底的2的对数
		double ln2 = Math.log(2);
		// 求出二进制向量的长度（根据公式
		bitSize = (int) (- (n * Math.log(p)) / Math.pow(ln2, 2));
		// 求出hash函数的个数
		hashSize = (int) (bitSize * ln2 / n);
		
		// 求出bits数组的长度
		// 类似分页处理 巧妙利用以下方法 达到整体向上取整效果
		// 例 假设每一页显示100条数据（pageSize） 一共有9999999条数据（n）
		// 请问有多少页？pageCount = (n + pageSize - 1) / pageSize
		bits = new long[(bitSize + Long.SIZE - 1) / Long.SIZE];
	}
	
	/**
	 * 添加元素
	 * 
	 * @param value
	 * @return true代表bit发生了改变
	 */
	public boolean put(T value) {
		nullCheck(value);
		// 利用value生成2个整数（其实是模拟google的算法来尽可能的生成不同的索引
		int hash1 = value.hashCode();
		int hash2 = hash1 >>> 16;
		
		boolean result = false;
		for (int i = 1; i <= hashSize; i++) {
			int combinedHash = hash1 + (i * hash2);
			if (combinedHash < 0) {
				combinedHash = ~combinedHash;
			}
			// 生成一个二进位的索引
			int index = combinedHash % bitSize;
			// 设置index位置的二进制位为1
			if (set(index)) result = true;
		}
		
		return result;
	}
	
	/**
	 * 判断一个元素是否存在
	 * 
	 * @param value
	 */
	public boolean contains(T value) {
		nullCheck(value);
		// 利用value生成2个整数
		int hash1 = value.hashCode();
		int hash2 = hash1 >>> 16;
		
		for (int i = 1; i <= hashSize; i++) {
			int combinedHash = hash1 + (i * hash2);
			if (combinedHash < 0) {
				combinedHash = ~combinedHash;
			}
			// 生成一个二进位的索引
			int index = combinedHash % bitSize;
			// 查询index位置的二进制位是否为0
			if (!get(index)) return false;
		}
		
		return true;
	}
	
	/**
	 * 非空check
	 * 
	 * @param value
	 */
	private void nullCheck(T value) {
		if (value == null) {
			throw new IllegalArgumentException("value must not be null.");
		}
	}
	
	/**
	 * 设置index位置的二进制位为1
	 * 
	 * @param index
	 */
	private boolean set(int index) {
		// 找到对应的long值
		long value = bits[index / Long.SIZE];
		int bitValue = 1 << (index % Long.SIZE);
		bits[index / Long.SIZE] = value | bitValue;
		/*
		 *  100000
		 * |000100   相当于   1<<2
		 * -------
		 *  100100
		*/
		return (value & bitValue) == 0;
	}
	
	/**
	 * 查看index位置的二进制位的值
	 * 
	 * @param index
	 * @return true代表1 false代表0
	 */
	private boolean get(int index) {
		// 找到对应的long值
		long value = bits[index / Long.SIZE];
		return (value & (1 << (index % Long.SIZE))) != 0;
		/*
		 *  110101
		 * &000100   相当于   1<<2
		 * -------
		 *  000100
		*/
	}
}
