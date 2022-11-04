package test.demo;

/**
 * Person对象
 * （这里用于说明使用hash表的时候 必须得重写hashCode和equals方法）
 * 
 * @author avril
 *
 */
public class Person {
	private int age;
	private float height;
	private String name;
	
	public Person(int age, float height, String name) {
		this.age = age;
		this.height = height;
		this.name = name;
	}
	
	/**
	 * 计算hash值
	 * （得到的hash值一般用来计算对应的hash表的索引）
	 */
	@Override
	public int hashCode() {
		int hashCode = Integer.hashCode(age);
		hashCode = hashCode * 31 + Float.hashCode(height);
		hashCode = hashCode * 31 + (name != null ? name.hashCode() : 0);
		return hashCode;
	}
	
	/**
	 * 比较是否相等
	 * （在hash表中一般用于 hash冲突后比较key是否相等 来判断是否为同一个内容）
	 * @param obj
	 */
	@Override
	public boolean equals(Object obj) {
		// 内存地址
		if (this == obj) return true;
		// 比较类型
		if (obj == null || obj.getClass() != getClass()) return false;
		// 比较成员
		Person person = (Person) obj;
		return person.age == age &&
				person.height == height &&
				(person.name == null ? name == null : person.name.equals(name));
	}
}
