package algorithm.greedy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import algorithm.greedy.demo.Article;

/**
 * 贪心策略，也称为贪婪策略
 * 
 * 每一步都采取当前状态下最优的选择（局部最优解），从而希望推导出全局最优解
 * 
 * @author avril
 *
 */
public class GreedyTest {
	/* 测试 */
	public static void main(String[] args) {
		pirate();
		System.out.println("****************");
		
		coinChange_ver1();
		System.out.println("****************");
		// 贪心策略并不一定能得到全局最优解
		// 最终的解是 1 枚 25 分、3 枚 5 分、1 枚 1 分的硬币，共 5 枚硬币
		// 实际上本题的最优解是：2 枚 20 分、1 枚 1 分的硬币，共 3 枚硬币
		coinChange_ver2(new Integer[] {25, 5, 20, 1}, 41);
		System.out.println("****************");
		
		knapsack("价值主导", (o1, o2) -> { // 优先选择价值最高的物品放进背包
			return o2.value - o1.value;
		});
		knapsack("重量主导", (o1, o2) -> { // 优先选择重量最轻的物品放进背包
			return o1.weight - o2.weight;
		});
		knapsack("价值密度主导", (o1, o2) -> { // 优先选择价值密度最高的物品放进背包（价值密度 = 价值 ÷ 重量）
			return Double.compare(o2.valueDensity, o1.valueDensity);
		});
	}
	
	/**
	 * 练习1 – 最优装载问题（加勒比海盗）
	 * 
	 * ◼ 在北美洲东南部，有一片神秘的海域，是海盗最活跃的加勒比海
	 *  有一天，海盗们截获了一艘装满各种各样古董的货船，每一件古董都价值连城，一旦打碎就失去了它的价值
	 *  海盗船的载重量为 W，每件古董的重量为 𝑤i，海盗们该如何把尽可能多数量的古董装上海盗船？
	 *  比如 W 为 30，𝑤i 分别为 3、5、4、10、7、14、2、11
	 */
	static void pirate() {
		/* 贪心策略：每一次都优先选择重量最小的古董 */
		int[] weights = {3, 5, 4, 10, 7, 14, 2, 11};
		Arrays.sort(weights);
		
		int capacity = 30, weight = 0, count = 0;
		for (int i = 0; i < weights.length && weight < capacity; i++) {
			int newWeight = weight + weights[i];
			if (newWeight <= capacity) {
				weight = newWeight;
				count++;
				System.out.println("装载: " + weights[i]);
			}
		}
		System.out.println("一共选了" + count + "件古董");
	}
	
	/**
	 * 练习2 – 零钱兑换
	 * 
	 * ◼ 假设有 25 分、10 分、5 分、1 分的硬币，现要找给客户 41 分的零钱，如何办到硬币个数最少？
	 *   注意这里的各个面值的零钱是可以重复使用的
	 */
	static void coinChange_ver1() {
		/* 贪心策略：每一次都优先选择面值最大的硬币 */
		int[] faces = {25, 5, 10, 1};
		Arrays.sort(faces);
		
		int money = 41, coins = 0;
		for (int i = faces.length - 1; i >= 0; i--) { // 从大到小使用
			if (money < faces[i]) continue;
			
			System.out.println("选择面值为 " + faces[i] + " 的硬币");
			money -= faces[i];
			coins++;
			i = faces.length; // 复位 再从从最大面值开始遍历
		}
		System.out.println("一共用了" + coins + "枚硬币");
	}
	
	static void coinChange_ver2(Integer[] faces, int money) {
		Arrays.sort(faces, (o1, o2) -> { // 通过比较器 让数组从大到小排序
			return o2 - o1;
		});
		
		int coins = 0, i = 0;
		while (i < faces.length) {
			if (money < faces[i]) {
				i++;
				continue;
			}
			
			System.out.println("选择面值为 " + faces[i] + " 的硬币");
			money -= faces[i];
			coins++;
			/*i = 0;*/ // 这个代码不需要 能来到这 说明不可能再重新选择首个最大面值的硬币了
		}
		System.out.println("一共用了" + coins + "枚硬币");
	}
	
	/**
	 * 练习3 – 0-1背包
	 * 
	 * 有 n 件物品和一个最大承重为 W 的背包，每件物品的重量是 𝑤i、价值是 𝑣i
	 * 在保证总重量不超过 W 的前提下，将哪几件物品装入背包，可以使得背包的总价值最大？
	 * 注意：每个物品只有 1 件，也就是每个物品只能选择 0 件或者 1 件，因此称为 0-1背包问题
	 * 
	 * @param title
	 * @param cmp
	 */
	static void knapsack(String title, Comparator<Article> cmp) {
		Article[] articles = new Article[] {
			new Article(35, 10), new Article(30, 40), new Article(60, 30),
			new Article(50, 50), new Article(40, 35), new Article(10, 40),
			new Article(25, 30)
		};
		Arrays.sort(articles, cmp); // 按照给定的贪心策略进行排序
		
		int capacity = 150, weight = 0, value = 0;
		List<Article> selected = new LinkedList<>();
		for (int i = 0; i < articles.length && weight < capacity; i++) {
			int newWeight = weight + articles[i].weight;
			if (newWeight <= capacity) {
				weight = newWeight;
				value += articles[i].value;
				selected.add(articles[i]);
			}
		}
		
		System.out.println("【" + title + "】");
		System.out.println("总价值： " + value);
		for (int i = 0; i < selected.size(); i++) {
			System.out.println(selected.get(i));
		}
		System.out.println("--------------------");
	}
}
