package lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * ClassName: binary_operator
 * Description: 测试二元操作符
 * Author: lizhao
 * Date: 2018/5/22 11:04
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class binary_operator {

    private List<Person> people = new ArrayList<>();

    {
        people.add(new Person(175.0, "李四"));
        people.add(new Person(170.0, "张三"));
        people.add(new Person(185.0, "赵六"));
        people.add(new Person(180.0, "王五"));
    }

    /**
     * 使用reduction操作实现:取所有Person中最高的Person
     */
    @Test
    void test01() {
        //不需要identity值
        Comparator<Person> comparator = Comparator.comparingDouble(Person::getTall);
        BinaryOperator<Person> operator = BinaryOperator.maxBy(comparator);
        Optional<Person> reduce = people.stream().reduce(operator);
        if (reduce.isPresent()) {
            Person person = reduce.get();
            System.out.println(person);
        }
    }

    /**
     * 创建map
     */
    @Test
    void test02() {
        HashMap<String, Person> collect = people.stream()
                .collect(HashMap::new, (map, p) -> map.put(p.getName(), p), Map::putAll);
        System.out.println(collect.toString());
        Person wangwu = collect.get("王五");
        System.out.println(wangwu);
    }

    /**
     * 使用Collectors.groupingBy 方法作为下游收集器
     */
    @Test
    void test03() {
        /*===============根据身高分组,并以身高为key===============*/
        Map<Double, List<Person>> collectMap = people.stream().
                collect(Collectors.groupingBy(Person::getTall, Collectors.toList()));
        System.out.println("身高为180.0的人有 :");
        collectMap.get(180.0).forEach(System.out::println);
        //根据姓姓氏分组,以姓为key
        Map<String, List<Person>> nameMap = people.stream().
                collect(Collectors.groupingBy(p -> p.getName().substring(0, 1), Collectors.toList()));
        System.out.println("姓氏为\"张\"的人有 :");
        nameMap.get("张").forEach(System.out::println);

        /*===============建立名字和身高的映射map(只是demo使用这种写法自找麻烦)===============*/
        List<?> list = people.stream().collect(ArrayList::new, (a, b) -> {
            Map<String, Double> map = new HashMap<>();
            map.put(b.getName(), b.getTall());
            a.add(map);
        }, List::add);
        list.forEach((s) -> {
            if (s instanceof Map) {
                Object o = ((Map) s).get("王五");
                if (o != null) System.out.println("王五的身高是 : " + o);
            }
        });
    }

    /**
     * 使用Collectors:groupingByConcurrent 并发收集数据到一个并发数据结构中
     * 可以用来提高无序流的处理效率
     */
    @Test
    void test04() {
        ConcurrentMap<Double, List<Person>> collectMap = people.stream()
                .collect(Collectors.groupingByConcurrent(Person::getTall));
        System.out.println("----map iterator----");
        collectMap.forEach((a, b) -> {
            System.out.println("身高为" + a + "的人有 :");
            b.forEach(System.out::println);
        });
        System.out.println("\r\n----list iterator----");
        System.out.println("身高为180.0的人有 :");
        collectMap.get(180.0).forEach(System.out::println);

        /*===============看一看序列收集是否会影响encounter order===============*/
        //测试证明,影响了
        System.out.println("\r\n看一看序列收集是否会影响encounter order");
        Map<Double, List<Person>> collect = people.stream().
                collect(Collectors.groupingBy(Person::getTall, Collectors.toList()));
        collect.forEach((a, b) -> {
            System.out.println("身高为" + a + "的人有 :");
            b.forEach(System.out::println);
        });
    }

    /**
     * 测试:Collectors.summarizing
     */
    @Test
    void test05() {
        DoubleSummaryStatistics summaryStatistics = people.stream()
                .collect(Collectors.summarizingDouble(Person::getTall));
        long count = summaryStatistics.getCount();
        double average = summaryStatistics.getAverage();
        double min = summaryStatistics.getMin();
        double max = summaryStatistics.getMax();
        double sum = summaryStatistics.getSum();
        System.out.println(summaryStatistics);
    }

    /**
     * 测试:Collectors的其他方法
     */
    @Test
    void test06() {
        Double sum1 = people.stream().mapToDouble(Person::getTall).sum();
        //换种途径
        Double sum2 = people.stream().collect(Collectors.summingDouble(Person::getTall));
        DoubleStream doubleStream = people.stream().mapToDouble(Person::getTall);
        System.out.println(sum1);
        System.out.println(sum2);
        doubleStream.forEach(System.out::println);
    }

}