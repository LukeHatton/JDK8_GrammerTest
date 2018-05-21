package lambda;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

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
}
