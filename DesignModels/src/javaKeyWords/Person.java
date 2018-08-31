package javaKeyWords;

/**
 * Created by huanghl on 2018/8/31.
 * static 关键字可以理解为全局修饰符，可以修饰成员属性，调用时可通过类名，方法明调用。
 * static 修饰的方法内部无法引用其他成员变量，因为和this 或super 这两个针对具体对象的关键字是互斥的
 *
 */
public class Person {

    private  String name;
    private  int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("构造Person:" + "name:" + name + " age:" + age);
    }

    static void walk(Person person){
         //无法引用
        // this.age
        System.out.println(person.name + " is walking....");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        Person p = new Person("hhl",30);

        Person.walk(p);
    }
}
