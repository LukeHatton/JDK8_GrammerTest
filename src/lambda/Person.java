package lambda;

/**
 * ClassName: Person
 * Description: 定义一个没有实现Comparable接口的Person类
 * Author: lizhao.dev
 * Date: 2018/5/24 14:06
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class Person {

    private Double tall;

    private String name;

    public Person() {
    }

    public Person(Double tall, String name) {
        this.tall = tall;
        this.name = name;
    }

    public Double getTall() {
        return tall;
    }

    public void setTall(Double tall) {
        this.tall = tall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("tall=").append(tall);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }


}
