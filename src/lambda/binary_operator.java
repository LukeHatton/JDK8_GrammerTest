package lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

/**
 * ClassName: binary_operator
 * Description: 测试二元操作符
 * Author: lizhao.dev
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
     * 使用groupingBy
     */
    @Test
    void test03(){
        //根据身高分组,并以身高为key
//        people.stream().collect();
    }

}