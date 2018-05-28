package lambda;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * ClassName: LambdaTest
 * Description:
 * Author: lizhao
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
        /****************collect:List->StringBuilder****************/
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        StringBuilder collect = list.stream().collect(StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append);
        System.out.println(collect.toString());

        /****************collect:List->HashSet****************/
        HashSet<Object> set = list.stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
        set.forEach(System.out::println);
        //或者,换一种语法
        Set<String> stringSet = new HashSet<>();
        list.forEach(stringSet::add);
        stringSet.forEach(System.out::println);
    }

    @Test
    public void test06() {
        //使用匿名内部类
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("这是线程1输出");
            }
        });
        thread1.start();

        //使用lambda表达式.因为Runnable的run()方法没有传参,所以括号中不写入参
        Thread thread2 = new Thread(() -> {
            System.out.println("这是线程2输出");
        });
        thread2.start();
    }

    /**
     * 测试:Function接口
     */
    @Test
    public void test07() {
        /* ----------------apply----------------- */
        Function<String, Integer> function = (s) -> {
            try {
                Integer i = Integer.parseInt(s);
                return i / 2;
            } catch (Exception e) {
                return 0;
            }
        };
        Integer apply = function.apply("5");
        System.out.println("调用apply方法 : " + apply);

        /* ----------------compose----------------- */
        //测试Function接口中的其他方法.apply方法必须有返回值
        Function<Integer, String> function1 = Object::toString;
        //function1的出参必须是function的入参类型的子类
        Function<Integer, Integer> compose = function.compose(function1);
        Integer composeApply = compose.apply(7);
        System.out.println("调用compose的apply方法 : " + composeApply);

        /* ----------------andThen----------------- */
        Function<Integer, Integer> function2 = (s) -> {
            return s * 5;
        };
        Function<String, Integer> andThen = function.andThen(function2);
        Integer andThenApply = andThen.apply("5");
        System.out.println("调用andThen的apply方法 : " + andThenApply);
    }

    public String[] getStringArray(Function<String, String[]> function, String str) {
        return function.apply(str);
    }

    public String arrayToString(Function<String[], String> function, String[] strArr) {
        return function.apply(strArr);
    }

    /**
     * 测试:方法引用
     */
    @Test
    public void test08() {
        /**
         * 方法引用的唯一用途是支持lambda表达式的简写
         * 当lambda表达式只是执行一个入参对象的方法调用时,可以
         * 通过方法引用的方式提高可读性
         */
        /* ----------------静态方法引用----------------- */
        Function<String, Integer> function1 = Integer::parseInt;  //引用Integer的parseInt方法,会自动应用入参
        Integer apply = function1.apply("11");
        System.out.println("静态方法引用 : " + apply);

        /* ----------------实例方法引用----------------- */
        /**
         * 引用实例方法时,需要有一个包含了函数式接口的入参,
         * 并将实际调用方法的入参一并传入,在新定义的方法中处理
         */
        //实例方法引用相当于只能使用一次,只在相应的接口时使用实例方法引用
        String s = "1,2,3,4";
        String[] apply1 = getStringArray(s::split, ",");
        System.out.println("实例方法引用 : " + arrayToString(Arrays::toString, apply1));
    }

    /**
     * 测试:Predicate接口
     */
    @Test
    public void test09() {
        Predicate<Integer> predicate = (i) -> i > 2;
        Predicate<Integer> predicate1 = (i) -> i < 10;
        Predicate<Integer> and = predicate.and(predicate1);
        Predicate<Integer> or = predicate.or(predicate1);
        System.out.println("11是否大于2 : " + predicate.test(11));
        System.out.println("1是否小于10 : " + predicate1.test(1));
        System.out.println("11是否大于2且小于10 : " + and.test(11));
        System.out.println("1是否大于2且小于10 : " + and.test(1));
        System.out.println("5是否大于2且小于10 : " + and.test(5));
        System.out.println("11是否大于2或小于10 : " + or.test(11));
        System.out.println("1是否大于2或小于10 : " + or.test(1));
    }
}
