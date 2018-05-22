package lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * ClassName: LambdaTest
 * Description:
 * Author: lizhao.dev
 * Date: 2018/5/21 10:19
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class LambdaTest {

    public void doConsume(Consumer<String> consumer, String s) {
        System.out.print("调用doConsume方法 : ");
        consumer.accept(s);
    }

    /**
     * 测试:Consumer
     */
    @Test
    public void test01() {
        String s = "ssa";
        doConsume(System.out::println, s);
    }

    /**
     * 测试:Predicate
     */
    @Test
    public void test02() {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(5);
        int sum = list.stream()
                .filter(e -> e >= 3)
                .mapToInt(e -> e)
                .sum();
        System.out.println(sum);
    }

    /**
     * 测试:流自动优化
     * 期望:副作用代码不会被执行,但实际上被执行了
     */
    @Test
    public void test03() {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(5);
        long count = list.stream()
                .map(a -> {
                    System.out.println("saw : " + a);
                    return a;
                })
                .count();

    }

    /**
     * 测试:Stream 缩减 Reduction
     */
    @Test
    public void test04() {
        /****************字符串使用reduce****************/
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        String concat = list.stream().reduce("", String::concat);
        System.out.println(concat);

        /****************数组使用reduce****************/
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        Integer reduce = Stream.of(arr).reduce(0, (a, b) -> a + b);
        System.out.println(reduce);
    }

    /**
     * 测试:可变缩减(Mutable Reduction)
     */
    @Test
    public void test05() {
        /**
         * 上一例中字符串拼接效率较低,因为需要拷贝字符串,这会消耗O(n2)时间
         *  可以使用StringBuilder来提高字符串拼接的效率
         */
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        StringBuilder collect = list.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append);
        System.out.println(collect.toString());
    }
}
